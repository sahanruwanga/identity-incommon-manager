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
package org.wso2.carbon.identity.incommon.mgt.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.model.ClaimConfig;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.Claim;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;
import org.wso2.carbon.identity.incommon.mgt.util.InCommonMgtConstants;
import org.wso2.carbon.identity.incommon.mgt.util.MetadataElementConstants;
import org.wso2.carbon.identity.incommon.mgt.util.SPMetadataElementConstants;
import org.wso2.carbon.identity.incommon.mgt.deployer.InCommonSAMLEntityDeployer;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;

import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ServiceProviderBuilder {

    private static final Log log = LogFactory.getLog(ServiceProviderBuilder.class);
    private static final ServiceProviderBuilder instance = new ServiceProviderBuilder();
    private List<ExternalClaim> externalClaims;

    private SAMLSSOServiceProviderDTO samlssoServiceProviderDTO;
    private ServiceProvider serviceProvider;
    private InboundAuthenticationConfig inboundAuthenticationConfig;
    private InboundAuthenticationRequestConfig[] inboundAuthenticationRequestConfigs;

    private ClaimConfig claimConfig;
    private InboundProvisioningConfig inboundProvisioningConfig;
    private LocalAndOutboundAuthenticationConfig localAndOutboundAuthenticationConfig;

    /**
     * Build SP object using metadata and parse to InCommonSAMLEntityDeployer
     *
     * @param entityId read from the metadata xml
     * @param certificate X.509 certificate in the metadata xml
     * @param assertionConsumerUrls list of assertion consumer urls in the metadata xml
     * @param requestedClaims list of requested claims in metadata xml
     */
    public void buildServiceProvider(String entityId, String certificate, List<String> assertionConsumerUrls,
            List<String> requestedClaims) {

        this.serviceProvider = new ServiceProvider();
        this.samlssoServiceProviderDTO = new SAMLSSOServiceProviderDTO();
        this.claimConfig = new ClaimConfig();
        this.inboundProvisioningConfig = new InboundProvisioningConfig();
        this.localAndOutboundAuthenticationConfig = new LocalAndOutboundAuthenticationConfig();

        getServiceProvider().setApplicationName(convertEntityIdToSPName(entityId));
        getServiceProvider().setCertificateContent(certificate);
//        buildClaimConfig(requestedClaims);
        buildInboundProvisioningConfig();
        buildLocalAndOutBoundAuthenticationConfig();

        getSamlssoServiceProviderDTO().setIssuer(convertEntityIdToSPName(entityId));
        buildAssertionConsumerUrls(assertionConsumerUrls);
        setDefaultConfiguration();

        buildInboundAuthenticationConfig();
        InCommonSAMLEntityDeployer.getInstance().importServiceProvider(getServiceProvider());

        setObjectToNull();
    }

    // Since IS does not allow to save SP names as URLs, read name is altered accordingly
    private String convertEntityIdToSPName(String entityId) {

        String spName = "";
        if (entityId.startsWith(InCommonMgtConstants.HTTPS_TAG)) {
            entityId = entityId.substring(8);
        }
        for (char character : entityId.toCharArray()) {
            if (Character.isLetter(character) || Character.isDigit(character) || character == '.') {
                spName += String.valueOf(character);
            } else {
                spName += "_";
            }
        }
        return spName;
    }

    private List<ExternalClaim> returnExternalClaims(List<String> requestedClaims) {

        if (requestedClaims.size() > 0) {
            if (getExternalClaims() == null) {
                this.externalClaims = InCommonSAMLEntityDeployer.getInstance()
                        .returnExternalClaims(MetadataElementConstants.CLAIM_DIALECT);
            }
        }
        return getExternalClaims();
    }

    private void buildClaimConfig(List<String> requestedClaims) {

        ClaimMapping[] claimMappings = new ClaimMapping[requestedClaims.size()];
        ClaimMapping claimMapping;
        Claim localClaim;
        Claim remoteClaim;
        String[] claimDialects = new String[1];
        claimDialects[0] = MetadataElementConstants.CLAIM_DIALECT;
        this.getClaimConfig().setSpClaimDialects(claimDialects);
        this.getClaimConfig().setLocalClaimDialect(false);
        this.getClaimConfig().setAlwaysSendMappedLocalSubjectId(false);

        List<ExternalClaim> externalClaimList = returnExternalClaims(requestedClaims);
        int i = 0;
        for (String claimURI : requestedClaims) {
            for (ExternalClaim externalClaim : externalClaimList) {
                if (claimURI.equals(externalClaim.getClaimURI())) {
                    claimMapping = new ClaimMapping();
                    localClaim = new Claim();
                    localClaim.setClaimId(0);
                    localClaim.setClaimUri(externalClaim.getMappedLocalClaim());
                    remoteClaim = new Claim();
                    remoteClaim.setClaimId(0);
                    remoteClaim.setClaimUri(claimURI);
                    claimMapping.setLocalClaim(localClaim);
                    claimMapping.setRemoteClaim(remoteClaim);
                    claimMapping.setRequested(true);
                    claimMapping.setMandatory(false);
                    claimMappings[i] = claimMapping;
                    break;
                }
            }
            i++;
        }

        this.getClaimConfig().setClaimMappings(claimMappings);
        // Set claim config object to service provider object
        this.getServiceProvider().setClaimConfig(this.getClaimConfig());
    }

    private void buildInboundProvisioningConfig() {

        this.getInboundProvisioningConfig().setProvisioningEnabled(false);
        this.getInboundProvisioningConfig().setProvisioningUserStore("");
        this.getInboundProvisioningConfig().setDumbMode(false);

        // set Inbound Provisioning Config object to service provider
        this.getServiceProvider().setInboundProvisioningConfig(this.getInboundProvisioningConfig());
    }

    private void buildLocalAndOutBoundAuthenticationConfig() {

        this.getLocalAndOutboundAuthenticationConfig().setAuthenticationType(MetadataElementConstants.DEFAULT_TYPE);
        this.getLocalAndOutboundAuthenticationConfig().setAlwaysSendBackAuthenticatedListOfIdPs(false);
        this.getLocalAndOutboundAuthenticationConfig().setUseTenantDomainInLocalSubjectIdentifier(false);
        this.getLocalAndOutboundAuthenticationConfig().setUseUserstoreDomainInLocalSubjectIdentifier(false);
        this.getLocalAndOutboundAuthenticationConfig().setUseUserstoreDomainInRoles(true);
        this.getLocalAndOutboundAuthenticationConfig().setEnableAuthorization(false);

        // Set LocalAndOutBoundAuthentication Config to service provider
        this.getServiceProvider()
                .setLocalAndOutBoundAuthenticationConfig(this.getLocalAndOutboundAuthenticationConfig());
    }

    private void buildAssertionConsumerUrls(List<String> assertionConsumerUrls) {

        if (!assertionConsumerUrls.isEmpty()) {
            getSamlssoServiceProviderDTO().setDefaultAssertionConsumerUrl(assertionConsumerUrls.get(0));
            getSamlssoServiceProviderDTO()
                    .setAssertionConsumerUrls(assertionConsumerUrls.toArray(new String[assertionConsumerUrls.size()]));
        }
    }

    private String doMarshall(SAMLSSOServiceProviderDTO samlssoServiceProviderDTO) {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(SAMLSSOServiceProviderDTO.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(samlssoServiceProviderDTO, sw);
            return sw.toString();
        } catch (JAXBException e) {
            log.error(samlssoServiceProviderDTO.getIssuer() + " marshalling failed");
        }
        return "";
    }

    private void setDefaultConfiguration() {

        this.getSamlssoServiceProviderDTO().setDoSignAssertions(true);
        this.getSamlssoServiceProviderDTO().setDoSignResponse(true);
        this.getSamlssoServiceProviderDTO().setDoSingleLogout(true);
        this.getSamlssoServiceProviderDTO().setEnableAttributeProfile(true);
        this.getSamlssoServiceProviderDTO().setEnableAttributesByDefault(true);
    }

    private void buildInboundAuthenticationConfig() {

        // Create InboundAuthenticationRequestConfig for SAMLSSO
        this.setInboundAuthenticationConfig(new InboundAuthenticationConfig());
        this.setInboundAuthenticationRequestConfigs(new InboundAuthenticationRequestConfig[1]);

        InboundAuthenticationRequestConfig inboundAuthenticationRequestConfigSAMLSSO = new InboundAuthenticationRequestConfig();
        inboundAuthenticationRequestConfigSAMLSSO.setInboundAuthKey(getServiceProvider().getApplicationName());
        inboundAuthenticationRequestConfigSAMLSSO.setInboundAuthType(SPMetadataElementConstants.SAML_SSO);
        inboundAuthenticationRequestConfigSAMLSSO.setInboundConfigType(SPMetadataElementConstants.STANDARD_APP);
        inboundAuthenticationRequestConfigSAMLSSO.setInboundConfiguration(doMarshall(getSamlssoServiceProviderDTO()));
        //        inboundAuthenticationRequestConfigSAMLSSO.setProperties(buildProperty());

        getInboundAuthenticationRequestConfigs()[0] = inboundAuthenticationRequestConfigSAMLSSO;

        this.getInboundAuthenticationConfig()
                .setInboundAuthenticationRequestConfigs(getInboundAuthenticationRequestConfigs());

        this.getServiceProvider().setInboundAuthenticationConfig(getInboundAuthenticationConfig());
    }

    private Property[] buildProperty() {

        Property[] properties = new Property[1];
        Property property = new Property();
        property.setName("attrConsumServiceIndex");
        property.setValue("");
        property.setConfidential(false);
        property.setRequired(false);
        property.setDisplayOrder(0);
        property.setAdvanced(false);
        properties[0] = property;
        return properties;
    }

    private void setObjectToNull() {

        this.setServiceProvider(null);
        this.setSamlssoServiceProviderDTO(null);
        this.setClaimConfig(null);
        this.setInboundProvisioningConfig(null);
        this.setLocalAndOutboundAuthenticationConfig(null);
        this.setInboundAuthenticationConfig(null);
        this.setInboundAuthenticationRequestConfigs(null);
        this.setInboundAuthenticationRequestConfigs(null);
    }

    public static ServiceProviderBuilder getInstance() {
        return instance;
    }

    public InboundAuthenticationConfig getInboundAuthenticationConfig() {
        return inboundAuthenticationConfig;
    }

    public void setInboundAuthenticationConfig(InboundAuthenticationConfig inboundAuthenticationConfig) {
        this.inboundAuthenticationConfig = inboundAuthenticationConfig;
    }

    public InboundAuthenticationRequestConfig[] getInboundAuthenticationRequestConfigs() {
        return inboundAuthenticationRequestConfigs;
    }

    public void setInboundAuthenticationRequestConfigs(
            InboundAuthenticationRequestConfig[] inboundAuthenticationRequestConfigs) {
        this.inboundAuthenticationRequestConfigs = inboundAuthenticationRequestConfigs;
    }

    public SAMLSSOServiceProviderDTO getSamlssoServiceProviderDTO() {
        return samlssoServiceProviderDTO;
    }

    public void setSamlssoServiceProviderDTO(SAMLSSOServiceProviderDTO samlssoServiceProviderDTO) {
        this.samlssoServiceProviderDTO = samlssoServiceProviderDTO;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public ClaimConfig getClaimConfig() {
        return claimConfig;
    }

    public void setClaimConfig(ClaimConfig claimConfig) {
        this.claimConfig = claimConfig;
    }

    public InboundProvisioningConfig getInboundProvisioningConfig() {
        return inboundProvisioningConfig;
    }

    public void setInboundProvisioningConfig(InboundProvisioningConfig inboundProvisioningConfig) {
        this.inboundProvisioningConfig = inboundProvisioningConfig;
    }

    public LocalAndOutboundAuthenticationConfig getLocalAndOutboundAuthenticationConfig() {
        return localAndOutboundAuthenticationConfig;
    }

    public void setLocalAndOutboundAuthenticationConfig(
            LocalAndOutboundAuthenticationConfig localAndOutboundAuthenticationConfig) {
        this.localAndOutboundAuthenticationConfig = localAndOutboundAuthenticationConfig;
    }

    public List<ExternalClaim> getExternalClaims() {
        return externalClaims;
    }

    public void setExternalClaims(List<ExternalClaim> externalClaims) {
        this.externalClaims = externalClaims;
    }
}
