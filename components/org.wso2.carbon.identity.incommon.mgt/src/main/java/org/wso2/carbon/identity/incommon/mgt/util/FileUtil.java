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
package org.wso2.carbon.identity.incommon.mgt.util;

import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;

public class FileUtil {

    private static final String INCOMMON_METADATA_XML = "incommon-metadata.xml";
    private static final String INCOMMON_CERT = "inc-md-cert.pem";

    /**
     * This returns the metadata xml file location in file system
     * It is saved in data folder inside the repository folder
     *
     * @return InCommon metadata XML file location
     */
    public static String getMetadataXMLFileLocation() {
        return CarbonUtils.getCarbonHome() + File.separator + "repository" + File.separator + "data" + File.separator
                + INCOMMON_METADATA_XML;
    }

    /**
     * This returns the InCommon cert file location
     * It is saved in data folder inside the repository folder as default
     *
     * @return InCommon Signature certificate
     */
    public static String getCertFileLocation() {
        return CarbonUtils.getCarbonHome() + File.separator + "repository" + File.separator + "data" + File.separator
                + INCOMMON_CERT;
    }
}
