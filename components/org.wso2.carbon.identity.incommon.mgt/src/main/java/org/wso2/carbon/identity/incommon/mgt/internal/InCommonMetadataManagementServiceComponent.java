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
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;

import javax.sql.DataSource;

@Component(name = "identity.incommon.metadata.component",
           immediate = true)
public class InCommonMetadataManagementServiceComponent {

    private static final Log log = LogFactory.getLog(InCommonMetadataManagementServiceComponent.class);

    /**
     *
     * @param context
     */
    @Activate
    protected void activate(ComponentContext context) {

        InCommonMetadataManagementServiceDataHolder.getInstance().setDataSource(getDataSource());
    }

    /**
     *
     * @param context
     */
    @Deactivate
    protected void deactivate(ComponentContext context) {

        if (log.isDebugEnabled()) {
            log.debug("InCommon Metadata Management bundle is deactivated");
        }
    }

    /**
     * Set Claim Metadata Management Service implementation
     *
     * @param claimMetadataManagementService Extension Processor
     */
    @Reference(name = "claim.metadata.mgt.deployer",
               service = ClaimMetadataManagementService.class,
               cardinality = ReferenceCardinality.MULTIPLE,
               policy = ReferencePolicy.DYNAMIC,
               unbind = "unsetClaimMetadataManagementService")
    protected void setClaimMetadataManagementService(ClaimMetadataManagementService claimMetadataManagementService) {

        if (log.isDebugEnabled()) {
            log.debug("Claim Metadata Management: " + claimMetadataManagementService.getClass()
                    + " set in InCommon management component");
        }
        InCommonMetadataManagementServiceDataHolder.getInstance()
                .setClaimMetadataManagementService(claimMetadataManagementService);
    }

    /**
     * Unset Claim Metadata Management Service implementation
     *
     * @param claimMetadataManagementService Extension Processor
     */
    protected void unsetClaimMetadataManagementService(ClaimMetadataManagementService claimMetadataManagementService) {

        if (log.isDebugEnabled()) {
            log.debug("Claim Metadata Management: " + claimMetadataManagementService.getClass()
                    + " unset in InCommon management component");
        }
        InCommonMetadataManagementServiceDataHolder.getInstance().setClaimMetadataManagementService(null);
    }

    private DataSource getDataSource() {
        return IdentityDatabaseUtil.getDataSource();
    }
}
