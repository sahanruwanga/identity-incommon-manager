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
package org.wso2.carbon.identity.incommon.mgt.dao;

import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;

/**
 * This interface access the data storage layer to retrieve, store, delete and update scheduler.
 */
public interface SchedulerContextManagerDAO {

    /**
     * Get a Scheduler.
     *
     * @param url
     * @param tenantId
     * @return
     */
    SchedulerContext getSchedulerContext(String url, int tenantId);

    /**
     * Get a Scheduler using the Tenant ID
     *
     * @param tenantId
     * @return
     */
    SchedulerContext getSchedulerContext(int tenantId);

    /**
     * Update a Scheduler.
     *
     * @param startDate
     * @param period
     * @param url
     * @param tenantId
     * @throws
     */
    void updateSchedulerContext(String startDate, int period, String lastModifiedDate, boolean refreshEnable, String url,
            int tenantId);

    /**
     * Delete a Scheduler.
     *
     * @param tenantId
     * @param url
     * @throws
     */
    void deleteSchedulerContext(String url, int tenantId);

    /**
     * Create a Scheduler.
     *
     * @param startDate
     * @param period
     * @param url       url of the xml file
     * @param tenantId
     * @throws
     */
    void createSchedulerContext(String startDate, int period, String url, String lastModifiedDate, boolean refreshEnable,
            int tenantId);
}
