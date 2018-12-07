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
 * this contains string constants which are using in reading data from the metadata xml
 */
public class MetadataElementConstants {

    public static final String ENTITY_ID = "entityID";
    public static final String ENTITY_DESCRIPTOR = "EntityDescriptor";
    public static final String ENTITIES_DESCRIPTOR = "EntitiesDescriptor";
    public static final String VALID_UNTIL = "validUntil";
    public static final String SP_SSO_DESCRIPTOR = "SPSSODescriptor";
    public static final String IDP_SSO_DESCRIPTOR = "IDPSSODescriptor";
    public static final String KEY_DESCRIPTOR = "KeyDescriptor";
    public static final String KEY_USE = "use";
    public static final String X509_CERTIFICATE = "ds:X509Certificate";
    public static final String CLAIM_DIALECT = "http://incommon.org/claims";
    public static final String DEFAULT_TYPE = "default";
    public static final String ASSERTION_CONSUMER_SERVICE = "AssertionConsumerService";
    public static final String LOCATION = "Location";
    public static final String INDEX = "index";
    public static final String INCOMMON_SIGNATURE_VALUE = "ds:SignatureValue";
    public static final String REQUESTED_ATTRIBUTE = "RequestedAttribute";
    public static final String NAME = "Name";
    public static final String SIGNING = "signing";
    public static final String ENCRYPTION = "encryption";
}
