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

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.incommon.mgt.InCommonMetadataManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.user.core.service.RealmService;

import javax.sql.DataSource;

public class InCommonSAMLEntityManagementServiceDataHolder {

    private static InCommonSAMLEntityManagementServiceDataHolder instance = new InCommonSAMLEntityManagementServiceDataHolder();
    private ApplicationManagementService applicationManagementService;
    private IdpManager identityProviderManagerService;
    private RealmService realmService;
    private DataSource dataSource;
    private InCommonMetadataManagementService inCommonMetadataManagementService;

    private InCommonSAMLEntityManagementServiceDataHolder(){}

    public RealmService getRealmService() {
        return realmService;
    }

    public void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    public static InCommonSAMLEntityManagementServiceDataHolder getInstance() {
        return instance;
    }

    public static void setInstance(InCommonSAMLEntityManagementServiceDataHolder instance) {
        InCommonSAMLEntityManagementServiceDataHolder.instance = instance;
    }

    public ApplicationManagementService getApplicationManagementService() {
        return applicationManagementService;
    }

    public void setApplicationManagementService(ApplicationManagementService applicationManagementService) {
        this.applicationManagementService = applicationManagementService;
    }

    public IdpManager getIdentityProviderManagerService() {
        return identityProviderManagerService;
    }

    public void setIdentityProviderManagerService(IdpManager identityProviderManagerService) {
        this.identityProviderManagerService = identityProviderManagerService;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InCommonMetadataManagementService getInCommonMetadataManagementService() {
        return inCommonMetadataManagementService;
    }

    public void setInCommonMetadataManagementService(InCommonMetadataManagementService inCommonMetadataManagementService) {
        this.inCommonMetadataManagementService = inCommonMetadataManagementService;
    }
}
