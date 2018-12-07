/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.identity.incommon.mgt.stub.InCommonMetadataManagementServiceStub;
import org.wso2.carbon.identity.incommon.mgt.ui.util.ServiceClientConstants;

import java.rmi.RemoteException;

public class InCommonManagementServiceClient {

    InCommonMetadataManagementServiceStub stub;

    public InCommonManagementServiceClient(String cookie, String serverURL, ConfigurationContext configContext)
            throws AxisFault {

        String serviceURL = serverURL + ServiceClientConstants.INCOMMON_METADATA_MANAGEMENT_SERVICE;
        stub = new InCommonMetadataManagementServiceStub(configContext, serviceURL);

        ServiceClient serviceClient = stub._getServiceClient();
        Options option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(HTTPConstants.COOKIE_STRING, cookie);

    }

    /**
     * Call backend for a new configuration
     *
     * @param fileUrl
     * @param refreshPeriod
     * @param refreshEnabled
     */
    public void createNewConfig(String fileUrl, int refreshPeriod, boolean refreshEnabled) {

        try {
            // period should be in seconds
            stub.startConfiguration(fileUrl, refreshPeriod * 60, refreshEnabled);
        } catch (RemoteException e) {
        }
    }

    /**
     * Call backend to do a force refresh
     *
     */
    public void forceRefresh() {

    }

    /**
     * Call backend to update an existing configuration
     *
     */
    public void updateConfig() {

    }

    /**
     * Call backend to delete an existing configuration
     *
     */
    public void deleteConfig() {

    }
}
