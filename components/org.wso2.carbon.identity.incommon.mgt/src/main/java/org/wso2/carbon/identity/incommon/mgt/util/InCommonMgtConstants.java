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

/**
 * This contains string constants to be used in storing details about metadata download process and configuration in db
 */
public class InCommonMgtConstants {

    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_SUCCESS = "success";
    public static final String ENTITY_TYPE_SP = "sp";
    public static final String ENTITY_TYPE_IDP = "idp";
    public static final String HTTPS_TAG = "https://";
    public static final String PREVIEW_METADATA_FILE_URL = "http://md.incommon.org/InCommon/InCommon-metadata-preview.xml";
    public static final String MAIN_METADATA_FILE_URL = "http://md.incommon.org/InCommon/InCommon-metadata.xml";
    public static final String FALLBACK_METADATA_FILE_URL = "http://md.incommon.org/InCommon/InCommon-metadata-fallback.xml";
    public static final String IDP_ONLY_METADATA_FILE_URL = "http://md.incommon.org/InCommon/InCommon-metadata-idp-only.xml";
    public static final String PREVIEW_METADATA_XML = "preview-metadata xml";
    public static final String MAIN_METADATA_XML = "main-metadata xml";
    public static final String FALLBACK_METADATA_XML = "fallback-metadata xml";
    public static final String IDP_ONLY_METADATA_XML = "idp-only-metadata xml";
    public static final String UNKNOWN_METADATA_XML = "unknown-metadata xml";
}
