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

import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;

import javax.sql.DataSource;

public class InCommonMetadataManagementServiceDataHolder {

    private static InCommonMetadataManagementServiceDataHolder instance = new InCommonMetadataManagementServiceDataHolder();
    private DataSource dataSource;
    private ClaimMetadataManagementService claimMetadataManagementService;

    public ClaimMetadataManagementService getClaimMetadataManagementService() {
        return claimMetadataManagementService;
    }

    public void setClaimMetadataManagementService(ClaimMetadataManagementService claimMetadataManagementService) {
        this.claimMetadataManagementService = claimMetadataManagementService;
    }

    public static void setInstance(InCommonMetadataManagementServiceDataHolder instance) {
        InCommonMetadataManagementServiceDataHolder.instance = instance;
    }

    public static InCommonMetadataManagementServiceDataHolder getInstance() {
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
