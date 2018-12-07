/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.incommon.mgt.validator;

import net.shibboleth.utilities.java.support.xml.AttributeSupport;
import net.shibboleth.utilities.java.support.xml.ElementSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.signature.reference.ReferenceData;
import org.apache.xml.security.signature.reference.ReferenceSubTreeData;
import org.apache.xml.security.transforms.Transform;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.security.x509.X509Credential;
import org.opensaml.security.x509.X509Support;
import org.opensaml.xml.signature.Signature;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.wso2.carbon.identity.incommon.mgt.util.FileUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.cert.X509Certificate;
import java.util.List;

public class SignatureValidator {

    private static Log log = LogFactory.getLog(SignatureValidator.class);

    /**
     * Verify InCommon Signature in metadata xml
     * This uses the InCommon metadata XML from the downloaded path and
     * the authentic InCommon signature certificate for the verification process
     *
     * @return
     * @throws Exception
     */
    public boolean isSignatureVerified() throws Exception {

        boolean isVerified = false;
        try {

            InputStream fileInputStream = new FileInputStream(FileUtil.getMetadataXMLFileLocation());

            DocumentBuilderFactory newFactory = DocumentBuilderFactory.newInstance();
            newFactory.setCoalescing(false);
            newFactory.setExpandEntityReferences(true);
            newFactory.setIgnoringComments(false);
            newFactory.setIgnoringElementContentWhitespace(false);
            newFactory.setNamespaceAware(true);
            newFactory.setValidating(false);
            newFactory.setXIncludeAware(false);

            DocumentBuilder builder = newFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(fileInputStream);

            String certPath = FileUtil.getCertFileLocation();
            X509Certificate x509Certificate = X509Support.decodeCertificate(new File(certPath));
            X509Credential credential = new BasicX509Credential(x509Certificate);

            isVerified = verifySignature(xmlDoc, credential);
        } catch (FileNotFoundException e) {
            log.error("Metadata file is not found for signature verification");
        } catch (IOException e) {
            log.error("Metadata file cannot be loaded for signature verification");
        }
        return isVerified;
    }

    private boolean verifySignature(Document xmlDocument, X509Credential x509Credential) throws Exception {

        Init.init();
        final Element signatureElement = getSignatureElement(xmlDocument);
        if (signatureElement == null) {
            log.error("Signature required but XML document is not signed");
            return false;
        }

        final XMLSignature signature;
        try {
            signature = new XMLSignature(signatureElement, "");
        } catch (final XMLSecurityException e) {
            log.error("Unable to read XML signature", e);
            throw new Exception(e);
        }

        if (signature.getObjectLength() != 0) {
            log.error("Signature contained an Object element, this is not allowed");
            return false;
        }

        final Reference reference = extractReference(signature);
        markIdAttribute(xmlDocument.getDocumentElement(), reference);

        final Key verificationKey = CredentialSupport.extractVerificationKey(x509Credential);
        try {
            if (signature.checkSignatureValue(verificationKey)) {

                /*
                 * Signature is verified here, but again need to check XML signature layer
                 * resolves the reference to the document element and for appropriate transforms.
                 *
                 * To do so reference will be extracted from signature
                 */
                validateSignatureReference(xmlDocument, extractReference(signature));
                log.info("XML document signature verified.");
                return true;
            } else {
                log.error("XML document signature verification failed.");
                return false;
            }
        } catch (final XMLSignatureException e) {
            log.error("XML document signature verification failed with an error", e);
            return false;
        }
    }

    private static Element getSignatureElement(final Document xmlDoc) {

        final List<Element> sigElements =
                ElementSupport
                        .getChildElementsByTagNameNS(xmlDoc.getDocumentElement(),
                                Signature.DEFAULT_ELEMENT_NAME.getNamespaceURI(),
                                Signature.DEFAULT_ELEMENT_NAME.getLocalPart());

        if (sigElements.isEmpty()) {
            return null;
        }

        if (sigElements.size() > 1) {
            log.error("XML document contained more than one signature, unable to process");
        }

        return sigElements.get(0);
    }

    private static Reference extractReference(final XMLSignature signature) throws Exception {

        final int numReferences = signature.getSignedInfo().getLength();
        if (numReferences != 1) {
            log.error("Signature SignedInfo had invalid number of References: " + numReferences);
        }

        final Reference reference;
        try {
            reference = signature.getSignedInfo().item(0);
        } catch (final XMLSecurityException e) {
            log.error("Apache XML Security exception obtaining Reference", e);
            throw new Exception(e);
        }
        if (reference == null) {
            log.error("Signature Reference was null");
        }
        return reference;
    }

    private static void markIdAttribute(final Element docElement, final Reference reference) {

        final String referenceUri = reference.getURI();

        /*
         * If the reference is empty, it implicitly references the document element
         * and no attribute is being referenced.
         */
        if (referenceUri == null || referenceUri.trim().isEmpty()) {
            log.debug("reference was empty; no ID marking required");
            return;
        }

        /*
         * If something has already identified an ID element, don't interfere
         */
        if (AttributeSupport.getIdAttribute(docElement) != null) {
            log.debug("document element already has an ID attribute");
            return;
        }

        /*
         * The reference must be a fragment reference, from which we extract the
         * ID value.
         */
        if (!referenceUri.startsWith("#")) {
            log.error("Signature Reference URI was not a document fragment reference: " + referenceUri);
        }
        final String id = referenceUri.substring(1);

        /*
         * Now look for the attribute which holds the ID value, and mark it as the ID attribute.
         */
        final NamedNodeMap attributes = docElement.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Attr attribute = (Attr) attributes.item(i);
            if (id.equals(attribute.getValue())) {
                docElement.setIdAttributeNode(attribute, true);
                return;
            }
        }
    }

    private static void validateSignatureReference(final Document xmlDocument, final Reference ref) {

        validateSignatureReferenceUri(xmlDocument, ref);
        validateSignatureTransforms(ref);
    }

    private static void validateSignatureReferenceUri(final Document xmlDocument, final Reference reference) {

        final ReferenceData refData = reference.getReferenceData();
        if (refData instanceof ReferenceSubTreeData) {
            final ReferenceSubTreeData subTree = (ReferenceSubTreeData) refData;
            final Node root = subTree.getRoot();
            Node resolvedSignedNode = root;
            if (root.getNodeType() == Node.DOCUMENT_NODE) {
                resolvedSignedNode = ((Document) root).getDocumentElement();
            }

            final Element expectedSignedNode = xmlDocument.getDocumentElement();

            if (!expectedSignedNode.isSameNode(resolvedSignedNode)) {
                log.error("Signature Reference URI \"" + reference.getURI()
                        + "\" was resolved to a node other than the document element");
            }
        } else {
            log.error("Signature Reference URI did not resolve to a subtree");
        }
    }

    private static void validateSignatureTransforms(final Reference reference) {

        Transforms transforms = null;
        try {
            transforms = reference.getTransforms();
        } catch (final XMLSecurityException e) {
            log.error("Apache XML Security error obtaining Transforms instance", e);
        }

        if (transforms == null) {
            log.error("Error obtaining Transforms instance, null was returned");
        }

        final int numTransforms = transforms.getLength();
        if (numTransforms > 2) {
            log.error("Invalid number of Transforms was present: " + numTransforms);
        }

        boolean sawEnveloped = false;
        for (int i = 0; i < numTransforms; i++) {
            Transform transform = null;
            try {
                transform = transforms.item(i);
            } catch (final TransformationException e) {
                log.error("Error obtaining transform instance", e);
            }
            final String uri = transform.getURI();
            if (Transforms.TRANSFORM_ENVELOPED_SIGNATURE.equals(uri)) {
                log.debug("Saw Enveloped signature transform");
                sawEnveloped = true;
            } else if (Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS.equals(uri)
                    || Transforms.TRANSFORM_C14N_EXCL_WITH_COMMENTS.equals(uri)) {
                log.debug("Saw Exclusive C14N signature transform");
            } else {
                log.error("Saw invalid signature transform: " + uri);
            }
        }

        if (!sawEnveloped) {
            log.error("Signature was missing the required Enveloped signature transform");
        }
    }
}
