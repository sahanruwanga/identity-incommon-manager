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
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.incommon.mgt.dao.SAMLEntityManagerDAO;
import org.wso2.carbon.identity.incommon.mgt.dao.SchedulerContextManagerDAO;
import org.wso2.carbon.identity.incommon.mgt.dao.impl.SAMLEntityManagerDAOImpl;
import org.wso2.carbon.identity.incommon.mgt.dao.impl.SchedulerContextManagerDAOImpl;
import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;
import org.wso2.carbon.identity.incommon.mgt.util.InCommonMgtConstants;

/**
 * Default implementation of {@link org.wso2.carbon.identity.incommon.mgt.InCommonMetadataManagementService}
 * interface.
 */
public class InCommonMetadataManagementServiceImpl implements InCommonMetadataManagementService {

    private static final Log log = LogFactory.getLog(InCommonMetadataManagementServiceImpl.class);

    private static volatile InCommonMetadataManagementServiceImpl instance = new InCommonMetadataManagementServiceImpl();

    private SchedulerContextManagerDAO schedulerContextManagerDAO = new SchedulerContextManagerDAOImpl();
    private SAMLEntityManagerDAO samlEntityManagerDAO = new SAMLEntityManagerDAOImpl();

    @Override
    public void createSchedule(SchedulerContext context, String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        this.schedulerContextManagerDAO
                .createSchedulerContext(context.getStartDateTime(), context.getPeriod(), context.getUrl(),
                        context.getLastModifiedDate(), context.getRefreshEnable(), tenantID);
    }

    @Override
    public void updateSchedule(SchedulerContext context, String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        this.schedulerContextManagerDAO
                .updateSchedulerContext(context.getStartDateTime(), context.getPeriod(), context.getLastModifiedDate(),
                        context.getRefreshEnable(), context.getUrl(), tenantID);
    }

    @Override
    public void deleteSchedule(String url, String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        this.schedulerContextManagerDAO.deleteSchedulerContext(url, tenantID);
    }

    @Override
    public SchedulerContext getSchedule(String url, String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        SchedulerContext context = this.schedulerContextManagerDAO.getSchedulerContext(url, tenantID);

        return context;
    }

    @Override
    public int getRequiredSpOrIdPCount(String entityType, String status, String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        return samlEntityManagerDAO.getRequiredSpOrIdpCount(entityType, status, tenantID);
    }

    @Override
    public String getLastConfigureTime(String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        return schedulerContextManagerDAO.getSchedulerContext(tenantID).getStartDateTime();
    }

    @Override
    public String getConfiguredMetadataFile(String tenantDomain) {

        int tenantID = IdentityTenantUtil.getTenantId(tenantDomain);

        String fileUrl = schedulerContextManagerDAO.getSchedulerContext(tenantID).getStartDateTime();
        String fileName;
        switch (fileUrl) {
            case InCommonMgtConstants.MAIN_METADATA_FILE_URL:
                fileName = InCommonMgtConstants.MAIN_METADATA_XML;
                break;
            case InCommonMgtConstants.PREVIEW_METADATA_FILE_URL:
                fileName = InCommonMgtConstants.PREVIEW_METADATA_XML;
                break;
            case InCommonMgtConstants.FALLBACK_METADATA_FILE_URL:
                fileName = InCommonMgtConstants.FALLBACK_METADATA_XML;
                break;
            case InCommonMgtConstants.IDP_ONLY_METADATA_FILE_URL:
                fileName = InCommonMgtConstants.IDP_ONLY_METADATA_XML;
                break;
            default:
                fileName = InCommonMgtConstants.UNKNOWN_METADATA_XML;
        }
        return fileName;
    }

    public static InCommonMetadataManagementServiceImpl getInstance() {
        return instance;
    }

    public static void setInstance(InCommonMetadataManagementServiceImpl instance) {
        InCommonMetadataManagementServiceImpl.instance = instance;
    }
}
