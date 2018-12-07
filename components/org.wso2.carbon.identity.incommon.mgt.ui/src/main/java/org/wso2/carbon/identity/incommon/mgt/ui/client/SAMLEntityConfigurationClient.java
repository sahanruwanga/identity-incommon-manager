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
package org.wso2.carbon.identity.incommon.mgt.ui.client;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.incommon.mgt.ui.dto.SAMLEntityDetailDTO;
import org.wso2.carbon.identity.incommon.mgt.ui.internal.SAMLEntityUIComponentDataHolder;

public class SAMLEntityConfigurationClient {

    private static final String SP = "sp";
    private static final String IDP = "idp";
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    public static SAMLEntityDetailDTO getFailedSPCount() {

        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

        SAMLEntityDetailDTO samlEntityDetailDTO = new SAMLEntityDetailDTO();
        samlEntityDetailDTO.setLastUpdate(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getLastConfigureTime(tenantDomain));
        samlEntityDetailDTO.setMetadataFile(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getConfiguredMetadataFile(tenantDomain));
        samlEntityDetailDTO.setFailedSPCount(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getRequiredSpOrIdPCount(SP, FAIL, tenantDomain));
        samlEntityDetailDTO.setSuccessSPCount(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getRequiredSpOrIdPCount(SP, SUCCESS, tenantDomain));
        samlEntityDetailDTO.setFailedIdPCount(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getRequiredSpOrIdPCount(IDP, FAIL, tenantDomain));
        samlEntityDetailDTO.setSuccessIdPCount(SAMLEntityUIComponentDataHolder.getInstance()
                .getInCommonMetadataManagementService().getRequiredSpOrIdPCount(IDP, SUCCESS, tenantDomain));

        return samlEntityDetailDTO;
    }
}
