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

import org.wso2.carbon.identity.incommon.mgt.util.SAMLEntityInfo;

import java.util.List;

/**
 * This interface access the data storage layer to retrieve, store, delete and update configured SAML Entity Info.
 */
public interface SAMLEntityManagerDAO {

    /**
     * Get SAML Entity Info.
     *
     * @param entityId
     * @param tenantId
     * @return SAMLEntityInfo Object
     * @throws
     */
    SAMLEntityInfo getSAMLEntityInfo(String entityId, int tenantId);

    /**
     * Update SAML Entity Info.
     *
     * @param entityId
     * @param entityType
     * @param status
     * @param tenantId
     * @throws
     */
    void updateSAMLEntityInfo(String entityId, String entityType, String status, int tenantId);

    /**
     * Delete SAML Entity Info.
     *
     * @param entityId
     * @param tenantId
     * @throws
     */
    void deleteSAMLEntityInfo(String entityId, int tenantId);

    /**
     * Import SAML Entity Info.
     *
     * @param entityId
     * @param entityType
     * @param status
     * @param tenantId
     * @throws
     */
    void importSAMLEntityInfo(String entityId, String entityType, String status, int tenantId);

    /**
     * Get failed SAML Entity(SP/IdP) Count.
     *
     * @param entityType
     * @param tenantId
     * @return failed entity count
     * @throws
     */
    int getRequiredSpOrIdpCount(String entityType, String status, int tenantId);

    /**
     * Get failed IDs of SAML Entities(SP/IdP).
     *
     * @param entityType
     * @param tenantId
     * @return list of failed entity ids
     * @throws
     */
    List<String> getRequiredSpOrIdpId(String entityType, String status, int tenantId);
}
