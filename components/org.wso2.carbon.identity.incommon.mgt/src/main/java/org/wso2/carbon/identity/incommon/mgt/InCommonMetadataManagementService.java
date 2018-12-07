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

import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;

/**
 * This interface used to expose incommon metadata configuration scheduling functionalities as an OSGi Service.
 */
public interface InCommonMetadataManagementService {

    /**
     * Create new schedule for InCommon Metadata Configuration
     *
     * @param context
     * @param tenantDomain
     */
    void createSchedule(SchedulerContext context, String tenantDomain);

    /**
     * Update schedule for InCommon Metadata Configuration
     *
     * @param context
     * @param tenantDomain
     */
    void updateSchedule(SchedulerContext context, String tenantDomain);

    /**
     * Delete schedule of InCommon Metadata Configuration
     *
     * @param url
     * @param tenantDomain
     */
    void deleteSchedule(String url, String tenantDomain);

    /**
     * Retrieve scheduler context object of InCommon Metadata Configuration
     *
     * @param url
     * @param tenantDomain
     * @return
     */
    SchedulerContext getSchedule(String url, String tenantDomain);

    /**
     * This is used to get the total count of failed or successful SPs and IdPs based on the required parameters
     *
     * @param entityType
     * @param status
     * @param tenantDomain
     * @return
     */
    int getRequiredSpOrIdPCount(String entityType, String status, String tenantDomain);

    /**
     * This is used to get the last configuration time of InCommon metadata
     *
     * @param tenantDomain
     * @return
     */
    String getLastConfigureTime(String tenantDomain);

    /**
     * This is used to get the name of the used metadata XML file out of four InCommon metadata aggregate
     * which is used for the configuration
     *
     * @param tenantDomain
     * @return
     */
    String getConfiguredMetadataFile(String tenantDomain);
}
