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
package org.wso2.carbon.identity.incommon.mgt.ui.dto;

public class SAMLEntityDetailDTO {

    private String lastUpdate;
    private String metadataFile;
    private int successSPCount;
    private int failedSPCount;
    private int successIdPCount;
    private int failedIdPCount;

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMetadataFile() {
        return metadataFile;
    }

    public void setMetadataFile(String metadataFile) {
        this.metadataFile = metadataFile;
    }

    public int getSuccessSPCount() {
        return successSPCount;
    }

    public void setSuccessSPCount(int successSPCount) {
        this.successSPCount = successSPCount;
    }

    public int getFailedSPCount() {
        return failedSPCount;
    }

    public void setFailedSPCount(int failedSPCount) {
        this.failedSPCount = failedSPCount;
    }

    public int getSuccessIdPCount() {
        return successIdPCount;
    }

    public void setSuccessIdPCount(int successIdPCount) {
        this.successIdPCount = successIdPCount;
    }

    public int getFailedIdPCount() {
        return failedIdPCount;
    }

    public void setFailedIdPCount(int failedIdPCount) {
        this.failedIdPCount = failedIdPCount;
    }
}
