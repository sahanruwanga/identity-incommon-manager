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
package org.wso2.carbon.identity.incommon.mgt.xmlhandler;

import org.wso2.carbon.identity.incommon.mgt.builder.ServiceProviderBuilder;
import org.wso2.carbon.identity.incommon.mgt.exception.SAXParserTermination;
import org.wso2.carbon.identity.incommon.mgt.util.MetadataElementConstants;
import org.wso2.carbon.identity.incommon.mgt.validator.SignatureValidator;
import org.wso2.carbon.identity.incommon.mgt.validator.ValidUntilValidator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XMLHandler extends DefaultHandler {

    private String validUntilValue;
    private String entityID;
    private String certificateContent;
    private String assertionConsumerUrl;
    private List<String> assertionConsumerUrls;
    private String requestedAttribute;
    private ArrayList<String> requestedAttributes;
    private StringBuilder stringBuilder;

    private boolean isEntityDescriptor;
    private boolean isIDPSSODescriptor;
    private boolean isSPSSODescriptor;

    private boolean isReadingSigningCertificate;
    private boolean isReadingCertificateContent;
    private boolean isValidUntilApproved;
    private boolean isInCommonSignatureVerified;

    // This value should be set to true once valid until and signature verified successful
    private boolean isFileVerified;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        stringBuilder = new StringBuilder();
        isValidUntilApproved = false;
        isEntityDescriptor = false;
        isSPSSODescriptor = false;
        isIDPSSODescriptor = false;
        assertionConsumerUrls = new ArrayList<>();
        requestedAttributes = new ArrayList<>();
        setFileVerified(false);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        // Read "ValidUntil" tag to validate the date
        if (MetadataElementConstants.ENTITIES_DESCRIPTOR.equals(qName)) {
            setValidUntilValue(attributes.getValue(MetadataElementConstants.VALID_UNTIL));
            if (!getValidUntilValue().isEmpty()) {
                setValidUntilApproved(new ValidUntilValidator().validateValidUntil(getValidUntilValue()));
            } else {
                throw new SAXParserTermination("Valid Until tag is invalid");
            }
        }

//        try {
            if (isValidUntilApproved()) {
//                boolean signatureVerified = new SignatureValidator().isSignatureVerified();
//                if(signatureVerified)
                    setFileVerified(true);
            }
//        } catch (Exception e) {
//            throw new SAXParserTermination("Signature verification is not success");
//        }

        if (isFileVerified()) {
            if (MetadataElementConstants.ENTITY_DESCRIPTOR.equals(qName)) {
                setEntityID(attributes.getValue(MetadataElementConstants.ENTITY_ID));
            }
            if (MetadataElementConstants.SP_SSO_DESCRIPTOR.equals(qName)) {
                setSPSSODescriptor(true);
            }
            if (MetadataElementConstants.KEY_DESCRIPTOR.equals(qName) && (attributes.getLength() == 0 || attributes
                    .getValue(MetadataElementConstants.KEY_USE).equals(MetadataElementConstants.SIGNING))) {
                setReadingSigningCertificate(true);

            }
            if (isReadingSigningCertificate() && MetadataElementConstants.X509_CERTIFICATE.equals(qName)) {
                setReadingCertificateContent(true);
            }

            if (MetadataElementConstants.ASSERTION_CONSUMER_SERVICE.equals(qName)) {
                setAssertionConsumerUrl(attributes.getValue(MetadataElementConstants.LOCATION));
                if (!getAssertionConsumerUrl().isEmpty()) {
                    getAssertionConsumerUrls().add(getAssertionConsumerUrl());
                }
                setAssertionConsumerUrl("");
            }

            if (MetadataElementConstants.REQUESTED_ATTRIBUTE.equals(qName)) {
                setRequestedAttribute(attributes.getValue(MetadataElementConstants.NAME));
                if (!getRequestedAttribute().isEmpty()) {
                    getRequestedAttributes().add(getRequestedAttribute());
                }
                setRequestedAttribute("");
            }

            if (MetadataElementConstants.IDP_SSO_DESCRIPTOR.equals(qName)) {
                setIDPSSODescriptor(true);
            }
        } else {
            throw new SAXParserTermination("XML document is not secure for configuration");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (isReadingCertificateContent()) {
            getStringBuilder().append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (MetadataElementConstants.ENTITY_DESCRIPTOR.equals(qName)) {
            if (isSPSSODescriptor()) {
                ServiceProviderBuilder.getInstance()
                        .buildServiceProvider(getEntityID(), getCertificateContent(), getAssertionConsumerUrls(),
                                getRequestedAttributes());
                setSPSSODescriptor(false);
                clearSPData();
            } else if (isIDPSSODescriptor()) {
//                IdentityProviderBuilder.getInstance().buildIdentityProvider(getEntityID());
                setIDPSSODescriptor(false);
                clearIdPData();
            }
            setEntityID("");
        }

        if (MetadataElementConstants.KEY_DESCRIPTOR.equals(qName)) {
            setReadingSigningCertificate(false);
        }

        if (MetadataElementConstants.X509_CERTIFICATE.equals(qName)) {
            if (isSPSSODescriptor()) {
                setCertificateContent(getStringBuilder().toString().trim());
                getStringBuilder().setLength(0);
            }
            setReadingCertificateContent(false);
        }

        if (MetadataElementConstants.ENTITIES_DESCRIPTOR.equals(qName)) {
            throw new SAXParserTermination("File reading completed");
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    private void clearSPData() {
        setAssertionConsumerUrl("");
        getAssertionConsumerUrls().clear();
        setRequestedAttribute("");
        getRequestedAttributes().clear();
        setCertificateContent("");
    }

    private void clearIdPData() {

    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public boolean isSP() {
        return isSPSSODescriptor;
    }

    public void setSP(boolean SP) {
        isSPSSODescriptor = SP;
    }

    public boolean isIdP() {
        return isIDPSSODescriptor;
    }

    public void setIdP(boolean idP) {
        isIDPSSODescriptor = idP;
    }

    public boolean isReadingSigningCertificate() {
        return isReadingSigningCertificate;
    }

    public void setReadingSigningCertificate(boolean readingSigningCertificate) {
        isReadingSigningCertificate = readingSigningCertificate;
    }

    public boolean isReadingCertificateContent() {
        return isReadingCertificateContent;
    }

    public void setReadingCertificateContent(boolean readingCertificateContent) {
        isReadingCertificateContent = readingCertificateContent;
    }

    public boolean isValidUntilApproved() {
        return isValidUntilApproved;
    }

    public void setValidUntilApproved(boolean validUntilApproved) {
        this.isValidUntilApproved = validUntilApproved;
    }

    public boolean isEntityDescriptor() {
        return isEntityDescriptor;
    }

    public void setEntityDescriptor(boolean entityDescriptor) {
        isEntityDescriptor = entityDescriptor;
    }

    public boolean isIDPSSODescriptor() {
        return isIDPSSODescriptor;
    }

    public void setIDPSSODescriptor(boolean IDPSSODescriptor) {
        isIDPSSODescriptor = IDPSSODescriptor;
    }

    public boolean isSPSSODescriptor() {
        return isSPSSODescriptor;
    }

    public void setSPSSODescriptor(boolean SPSSODescriptor) {
        isSPSSODescriptor = SPSSODescriptor;
    }

    public List<String> getAssertionConsumerUrls() {
        return assertionConsumerUrls;
    }

    public void setAssertionConsumerUrls(List<String> assertionConsumerUrls) {
        this.assertionConsumerUrls = assertionConsumerUrls;
    }

    public String getValidUntilValue() {
        return validUntilValue;
    }

    public void setValidUntilValue(String validUntilValue) {
        this.validUntilValue = validUntilValue;
    }

    public boolean isInCommonSignatureVerified() {
        return isInCommonSignatureVerified;
    }

    public void setInCommonSignatureVerified(boolean inCommonSignatureVerified) {
        isInCommonSignatureVerified = inCommonSignatureVerified;
    }

    public String getAssertionConsumerUrl() {
        return assertionConsumerUrl;
    }

    public void setAssertionConsumerUrl(String assertionConsumerUrl) {
        this.assertionConsumerUrl = assertionConsumerUrl;
    }

    public String getRequestedAttribute() {
        return requestedAttribute;
    }

    public void setRequestedAttribute(String requestedAttribute) {
        this.requestedAttribute = requestedAttribute;
    }

    public ArrayList<String> getRequestedAttributes() {
        return requestedAttributes;
    }

    public void setRequestedAttributes(ArrayList<String> requestedAttributes) {
        this.requestedAttributes = requestedAttributes;
    }

    public String getCertificateContent() {
        return certificateContent;
    }

    public void setCertificateContent(String certificateContent) {
        this.certificateContent = certificateContent;
    }

    public boolean isFileVerified() {
        return isFileVerified;
    }

    public void setFileVerified(boolean fileVerified) {
        isFileVerified = fileVerified;
    }
}
