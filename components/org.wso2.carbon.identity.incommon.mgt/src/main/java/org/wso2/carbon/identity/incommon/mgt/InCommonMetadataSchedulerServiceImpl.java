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
import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;
import org.wso2.carbon.identity.incommon.mgt.xmlhandler.XMLDownloader;
import org.wso2.carbon.identity.incommon.mgt.xmlhandler.XMLParser;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of {@link org.wso2.carbon.identity.incommon.mgt.InCommonMetadataSchedulerService}
 * interface.
 */
public class InCommonMetadataSchedulerServiceImpl implements InCommonMetadataSchedulerService {

    private static final Log log = LogFactory.getLog(InCommonMetadataSchedulerServiceImpl.class);

    private static volatile InCommonMetadataSchedulerServiceImpl instance = new InCommonMetadataSchedulerServiceImpl();

    public static final ScheduledExecutorService schedulerService = Executors.newScheduledThreadPool(1);

    @Override
    public void startScheduler(long delay, long period, String url, String tenantDomain) {

        // If period is 0, ignore auto-update
//        getSchedulerService()
//                .scheduleAtFixedRate(() -> this.startConfig(url, false, tenantDomain), delay, period, TimeUnit.SECONDS);
        this.startConfig(url, false, tenantDomain);
    }

    @Override
    public void updateScheduler() {

    }

    @Override
    public void stopScheduler() {

    }

    @Override
    public void perceiveSchedulerStatus() {

    }

    private void startConfig(String url, boolean isForceRefresh, String tenantDomain) {

        String lastModifiedDate = "";
        SchedulerContext context = InCommonMetadataManagementServiceImpl.getInstance().getSchedule(url, tenantDomain);
        if (context != null) {
            lastModifiedDate = context.getLastModifiedDate();
        }
        String newLastModifiedDate = this.downloadMetadataFile(lastModifiedDate, url);
        if (!lastModifiedDate.equals(newLastModifiedDate)) {
            if (lastModifiedDate.equals("")) {
                context.setLastModifiedDate(newLastModifiedDate);
                InCommonMetadataManagementServiceImpl.getInstance().updateSchedule(context, tenantDomain);
                this.parse();
            } else {
                InCommonMetadataManagementServiceImpl.getInstance().updateSchedule(context, tenantDomain);
                // TODO: doRefreshConfig()
            }
        } else if (isForceRefresh) {
            // TODO: doRefreshConfig()
        }
    }

    private String downloadMetadataFile(String lastModifiedDate, String url) {
        return new XMLDownloader().doHTTPConditionalGET(lastModifiedDate, url);
    }

    private void parse() {
        XMLParser.getInstance().parse();
    }

    public static InCommonMetadataSchedulerServiceImpl getInstance() {
        return instance;
    }

    public static void setInstance(InCommonMetadataSchedulerServiceImpl instance) {
        InCommonMetadataSchedulerServiceImpl.instance = instance;
    }

    public static ScheduledExecutorService getSchedulerService() {
        return schedulerService;
    }
}
