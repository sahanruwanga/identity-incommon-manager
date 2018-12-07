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
package org.wso2.carbon.identity.incommon.mgt.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.incommon.mgt.InCommonMetadataManagementService;
import org.wso2.carbon.identity.incommon.mgt.InCommonMetadataManagementServiceImpl;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.user.core.service.RealmService;

import javax.sql.DataSource;

@Component(name = "identity.incommon.saml.entity.component",
        immediate = true)
public class InCommonSAMLEntityManagementServiceComponent {

    private static final Log log = LogFactory.getLog(InCommonSAMLEntityManagementServiceDataHolder.class);

    /**
     * @param context
     */
    @Activate
    protected void activate(ComponentContext context) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setDataSource(getDataSource());
        InCommonMetadataManagementService inCommonMetadataManagementService = new InCommonMetadataManagementServiceImpl();
        context.getBundleContext().registerService(InCommonMetadataManagementService.class.getName(),
                inCommonMetadataManagementService, null);
        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setInCommonMetadataManagementService(inCommonMetadataManagementService);
    }

    /**
     * @param context
     */
    @Deactivate
    protected void deactivate(ComponentContext context) {

        if (log.isDebugEnabled()) {
            log.debug("InCommon SAML Entity Management bundle is deactivated");
        }
    }

    /**
     * Set RealmService
     *
     * @param realmService
     */
    @Reference(name = "user.realmservice.default",
            service = RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService")
    protected void setRealmService(RealmService realmService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setRealmService(realmService);
        if (log.isDebugEnabled()) {
            log.debug("RealmService set in InCommon SAML Entity Management bundle");
        }
    }

    /**
     * Unset RealmService
     *
     * @param realmService
     */
    protected void unsetRealmService(RealmService realmService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setRealmService(null);
        if (log.isDebugEnabled()) {
            log.debug("RealmService unset in InCommon SAML Entity Management bundle");
        }
    }

    /**
     * Set Application Management Service
     *
     * @param applicationManagerService
     */
    @Reference(name = "user.applicationmanagementservice.default",
            service = ApplicationManagementService.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetApplicationManagerService")
    protected void setApplicationManagerService(ApplicationManagementService applicationManagerService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance()
                .setApplicationManagementService(applicationManagerService);
    }

    /**
     * Unset Application Management Service
     *
     * @param applicationManagerService
     */
    protected void unsetApplicationManagerService(ApplicationManagementService applicationManagerService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setApplicationManagementService(null);
    }

    /**
     * Set Identity Provider Manager Service
     *
     * @param identityProviderManagerService
     */
    @Reference(name = "idp.mgt.dscomponent",
            service = IdpManager.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityProviderManager")
    protected void setIdentityProviderManager(IdpManager identityProviderManagerService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance()
                .setIdentityProviderManagerService(identityProviderManagerService);
    }

    /**
     * Unset Identity Provider Manager Service
     *
     * @param identityProviderManagerService
     */
    protected void unsetIdentityProviderManager(IdpManager identityProviderManagerService) {

        InCommonSAMLEntityManagementServiceDataHolder.getInstance().setIdentityProviderManagerService(null);
    }

    private DataSource getDataSource() {
        return IdentityDatabaseUtil.getDataSource();
    }
}
