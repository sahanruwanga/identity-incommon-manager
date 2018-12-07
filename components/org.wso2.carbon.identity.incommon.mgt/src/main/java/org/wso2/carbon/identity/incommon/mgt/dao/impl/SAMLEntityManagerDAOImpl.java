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
package org.wso2.carbon.identity.incommon.mgt.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.incommon.mgt.dao.SAMLEntityManagerDAO;
import org.wso2.carbon.identity.incommon.mgt.util.JdbcUtils;
import org.wso2.carbon.identity.incommon.mgt.util.SAMLEntityInfo;
import org.wso2.carbon.identity.incommon.mgt.util.SQLConstant;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class SAMLEntityManagerDAOImpl implements SAMLEntityManagerDAO {

    private static final Log log = LogFactory.getLog(SAMLEntityManagerDAOImpl.class);

    @Override
    public SAMLEntityInfo getSAMLEntityInfo(String entityId, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving SAML Entity Info in tenant: %s", String.valueOf(tenantId)));
        }

        SAMLEntityInfo samlEntityInfo = null;
        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            samlEntityInfo = jdbcTemplate
                    .fetchSingleRecord(SQLConstant.GET_SAML_ENTITY_INFO, (resultSet, rowNumber) -> {
                        return new SAMLEntityInfo(resultSet.getString(1), resultSet.getString(2),
                                resultSet.getString(3));
                    }, (PreparedStatement preparedStatement) -> {
                        preparedStatement.setInt(1, tenantId);
                    });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
        return samlEntityInfo;
    }

    @Override
    public void updateSAMLEntityInfo(String entityId, String entityType, String status, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating SAML Entity Info in tenant: %s", String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeUpdate(SQLConstant.UPDATE_SAML_ENTITY_INFO, preparedStatement -> {
                preparedStatement.setString(1, entityType);
                preparedStatement.setString(2, status);
                preparedStatement.setString(3, entityId);
                preparedStatement.setInt(4, tenantId);
            });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    @Override
    public void deleteSAMLEntityInfo(String entityId, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting SAML Entity Info in tenant: %s", String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeUpdate(SQLConstant.DELETE_SAML_ENTITY_INFO, preparedStatement -> {
                preparedStatement.setInt(1, tenantId);
            });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    @Override
    public void importSAMLEntityInfo(String entityId, String entityType, String status, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Importing new SAML Entity Info of %s for tenant: %s", entityId,
                    String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeInsert(SQLConstant.INSERT_SAML_ENTITY_INFO, (preparedStatement -> {
                preparedStatement.setString(1, entityId);
                preparedStatement.setString(2, entityType);
                preparedStatement.setString(3, status);
                preparedStatement.setInt(4, tenantId);
            }), null, true);
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    @Override
    public int getRequiredSpOrIdpCount(String entityType, String status, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving %s SAML Entity Count in tenant: %s", status, String.valueOf(tenantId)));
        }

        int failedCount = 0;
        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            failedCount = jdbcTemplate
                    .fetchSingleRecord(SQLConstant.GET_REQUIRED_SP_OR_IDP_COUNT, (resultSet, rowNumber) -> {
                        return resultSet.getInt(1);
                    }, (PreparedStatement preparedStatement) -> {
                        preparedStatement.setString(1, entityType);
                        preparedStatement.setString(2, status);
                        preparedStatement.setInt(3, tenantId);
                    });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
        return failedCount;
    }

    @Override
    public List<String> getRequiredSpOrIdpId(String entityType, String status, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving %s SAML Entity Count in tenant: %s", status, String.valueOf(tenantId)));
        }

        List<String> entityIds = new ArrayList<>();
        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.fetchSingleRecord(SQLConstant.GET_REQUIRED_SP_OR_IDP_ID, (resultSet, rowNumber) -> {
                entityIds.add(resultSet.getString(1));
                return resultSet.getString(1);
            }, (PreparedStatement preparedStatement) -> {
                preparedStatement.setString(1, entityType);
                preparedStatement.setString(2, status);
                preparedStatement.setInt(3, tenantId);
            });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
        return entityIds;
    }
}
