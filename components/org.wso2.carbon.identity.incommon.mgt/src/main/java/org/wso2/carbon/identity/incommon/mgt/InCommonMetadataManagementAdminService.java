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
package org.wso2.carbon.identity.incommon.mgt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;

import java.time.LocalDateTime;

public class InCommonMetadataManagementAdminService extends AbstractAdmin {

    private static final Log log = LogFactory.getLog(InCommonMetadataManagementAdminService.class);

    /**
     * Start new configuration process
     *
     * @param url
     * @param period
     * @param refreshEnable
     */
    public void startConfiguration(String url, int period, boolean refreshEnable) {

        if(isAllowedToConfig()){
            SchedulerContext context = new SchedulerContext(getCurrentDateTime(), period, url, "", refreshEnable);
            String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

            InCommonMetadataManagementServiceImpl.getInstance().createSchedule(context, tenantDomain);

            InCommonMetadataSchedulerServiceImpl.getInstance()
                    .startScheduler(0, context.getPeriod(), context.getUrl(), tenantDomain);
        }else{
            log.info("EnableConfig should be set to true in identity.xml for initiating the configuration");
        }
    }

    /**
     * Update the existing configuration
     *
     */
    public void updateSchedule() {

    }

    /**
     * Refresh configuration process according to the refresh period
     *
     */
    public void refreshConfiguration() {

    }

    /**
     * Delete existing configuration
     *
     */
    public void deleteCofiguration() {

    }

    private String getCurrentDateTime() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        return String.valueOf(currentDateTime);
    }

    private boolean isAllowedToConfig(){

        String tagValue = IdentityUtil.getProperty("InCommon.EnableConfig");
        if (tagValue == null)
            return false;
        if(tagValue.equals("true"))
            return true;
        else
            return false;
    }
}
