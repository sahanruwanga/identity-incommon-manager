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
package org.wso2.carbon.identity.incommon.mgt.deployer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.ImportResponse;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;
import org.wso2.carbon.identity.incommon.mgt.dao.SAMLEntityManagerDAO;
import org.wso2.carbon.identity.incommon.mgt.dao.impl.SAMLEntityManagerDAOImpl;
import org.wso2.carbon.identity.incommon.mgt.internal.InCommonSAMLEntityManagementServiceDataHolder;
import org.wso2.carbon.identity.incommon.mgt.internal.InCommonMetadataManagementServiceDataHolder;
import org.wso2.carbon.identity.incommon.mgt.util.InCommonMgtConstants;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.user.core.UserStoreException;

import java.util.ArrayList;
import java.util.List;

/**
 * The class contains methods to configure SP and IdP objects by calling relevant services and
 * the method to call ClaimMetadataManagementService
 */
public class InCommonSAMLEntityDeployer {

    private static final Log log = LogFactory.getLog(InCommonSAMLEntityDeployer.class);

    private static final InCommonSAMLEntityDeployer instance = new InCommonSAMLEntityDeployer();

    /**
     * Import the created SP object to ApplicationManagementService for configuration
     *
     * @param serviceProvider the object is created from the data in metadata xml
     */
    public void importServiceProvider(ServiceProvider serviceProvider) {

        ImportResponse importResponse;
        try {
            // Parameter returns an ImportResponse object
            importResponse = InCommonSAMLEntityManagementServiceDataHolder.getInstance()
                    .getApplicationManagementService()
                    .importSPApplication(serviceProvider, getTenantDomain(), getUserName(), false);
        } catch (IdentityApplicationManagementException e) {
            importResponse = new ImportResponse();
            importResponse.setResponseCode(ImportResponse.FAILED);
            importResponse.setApplicationName(serviceProvider.getApplicationName());
        }
        this.manageStatus(importResponse, InCommonMgtConstants.ENTITY_TYPE_SP);
    }

    /**
     * Import the created IdP object to IdentityProviderManagerService for the configuration
     *
     * @param identityProvider the object is created from the data in metadata xml
     */
    public void importIdentityProvider(IdentityProvider identityProvider) {

        ImportResponse importResponse = new ImportResponse();
        try {
            InCommonSAMLEntityManagementServiceDataHolder.getInstance().getIdentityProviderManagerService()
                    .addIdP(identityProvider, getTenantDomain());
            importResponse.setResponseCode(ImportResponse.CREATED);
            importResponse.setApplicationName(identityProvider.getIdentityProviderName());
        } catch (IdentityProviderManagementException e) {
            importResponse.setResponseCode(ImportResponse.FAILED);
            importResponse.setApplicationName(identityProvider.getIdentityProviderName());
        }
        this.manageStatus(importResponse, InCommonMgtConstants.ENTITY_TYPE_IDP);
    }

    /**
     * Get external claims for the passed claim dialect using the ClaimMetadataManagementService
     *
     * @param claimDialect this is the URI of the required claim dialect
     * @return
     */
    public List<ExternalClaim> returnExternalClaims(String claimDialect) {

        List<ExternalClaim> externalClaims = new ArrayList<>();
        try {
            externalClaims = InCommonMetadataManagementServiceDataHolder.getInstance()
                    .getClaimMetadataManagementService()
                    .getExternalClaims(claimDialect, getTenantDomain());
        } catch (ClaimMetadataException e) {
            log.error("Can not find data for selected external claim");
        }
        return externalClaims;
    }

    private void manageStatus(ImportResponse importResponse, String entityType) {

        SAMLEntityManagerDAO samlEntityManagerDAO = new SAMLEntityManagerDAOImpl();
        if (importResponse.getResponseCode() == ImportResponse.CREATED) {
            samlEntityManagerDAO
                    .importSAMLEntityInfo(importResponse.getApplicationName(), entityType,
                            InCommonMgtConstants.STATUS_SUCCESS, getTenantID());
        } else {
            samlEntityManagerDAO
                    .importSAMLEntityInfo(importResponse.getApplicationName(), entityType,
                            InCommonMgtConstants.STATUS_FAIL, getTenantID());
        }
    }

    private String getUserName() {
        return CarbonContext.getThreadLocalCarbonContext().getUsername();
    }

    private String getTenantDomain() {

        String tenantDomain = "";
        try {
            tenantDomain = InCommonSAMLEntityManagementServiceDataHolder.getInstance().getRealmService()
                    .getTenantManager().getSuperTenantDomain();
        } catch (UserStoreException e) {
            log.error("Error occurred while getting Tenant Domain from realm deployer");
        }
        return tenantDomain;
    }

    private int getTenantID() {
        return CarbonContext.getThreadLocalCarbonContext().getTenantId();
    }

    public static InCommonSAMLEntityDeployer getInstance() {
        return instance;
    }
}
