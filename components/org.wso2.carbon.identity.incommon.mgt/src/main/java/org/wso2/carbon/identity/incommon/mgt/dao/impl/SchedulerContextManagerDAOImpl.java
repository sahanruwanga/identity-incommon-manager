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
import org.wso2.carbon.identity.incommon.mgt.dao.SchedulerContextManagerDAO;
import org.wso2.carbon.identity.incommon.mgt.util.SchedulerContext;
import org.wso2.carbon.identity.incommon.mgt.util.JdbcUtils;
import org.wso2.carbon.identity.incommon.mgt.util.SQLConstant;

import java.sql.PreparedStatement;

/**
 * Default implementation of {@link SchedulerContextManagerDAO}. This handles {@link SchedulerContext} related db layer
 * operations.
 */
public class SchedulerContextManagerDAOImpl implements SchedulerContextManagerDAO {

    private static final Log log = LogFactory.getLog(SchedulerContextManagerDAOImpl.class);

    private static final SchedulerContextManagerDAO instance = new SchedulerContextManagerDAOImpl();

    @Override
    public SchedulerContext getSchedulerContext(String url, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving Scheduler in tenant: %s and file url: %s", String.valueOf(tenantId), url));
        }

        SchedulerContext schedulerContext = null;
        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            schedulerContext = jdbcTemplate.fetchSingleRecord(SQLConstant.GET_SCHEDULER_FROM_URL_AND_TENANT_ID, (resultSet, rowNumber) -> {
                return new SchedulerContext(resultSet.getString(1), resultSet.getInt(2), url, resultSet.getString(3),
                        resultSet.getBoolean(4));

            }, (PreparedStatement preparedStatement) -> {
                preparedStatement.setString(1, url);
                preparedStatement.setInt(2, tenantId);
            });

        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
        return schedulerContext;
    }

    @Override
    public SchedulerContext getSchedulerContext(int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving Scheduler in tenant: %s", String.valueOf(tenantId)));
        }

        SchedulerContext schedulerContext = null;
        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            schedulerContext = jdbcTemplate.fetchSingleRecord(SQLConstant.GET_SCHEDULER_FROM_TENANT_ID, (resultSet, rowNumber) -> {
                return new SchedulerContext(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getBoolean(5));

            }, (PreparedStatement preparedStatement) -> {
                preparedStatement.setInt(1, tenantId);
            });

        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
        return schedulerContext;
    }

    @Override
    public void updateSchedulerContext(String startDate, int period, String lastModifiedDate, boolean refreshEnable,
                                       String url, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating Scheduler in tenant: %s", String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeUpdate(SQLConstant.UPDATE_SCHEDULER, preparedStatement -> {
                preparedStatement.setString(1, startDate);
                preparedStatement.setInt(2, period);
                preparedStatement.setString(3, lastModifiedDate);
                preparedStatement.setBoolean(4, refreshEnable);
                preparedStatement.setString(5, url);
                preparedStatement.setInt(6, tenantId);
            });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    @Override
    public void deleteSchedulerContext(String url, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting Scheduler in tenant: %s", String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeUpdate(SQLConstant.DELETE_SCHEDULER, preparedStatement -> {
                preparedStatement.setString(1, url);
                preparedStatement.setInt(2, tenantId);
            });
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    @Override
    public void createSchedulerContext(String startDate, int period, String url, String lastModifiedDate,
                                       boolean refreshEnable, int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug(
                    String.format("Creating new Scheduler at %s for tenant: %s", startDate, String.valueOf(tenantId)));
        }

        try {
            JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            jdbcTemplate.executeInsert(SQLConstant.INSERT_SCHEDULER, (preparedStatement -> {
                preparedStatement.setString(1, startDate);
                preparedStatement.setInt(2, period);
                preparedStatement.setString(3, url);
                preparedStatement.setString(4, lastModifiedDate);
                preparedStatement.setBoolean(5, refreshEnable);
                preparedStatement.setInt(6, tenantId);
            }), null, true);
        } catch (DataAccessException e) {
            log.error("Can't access to requested data.");
        }
    }

    public static SchedulerContextManagerDAO getInstance() {
        return instance;
    }
}
