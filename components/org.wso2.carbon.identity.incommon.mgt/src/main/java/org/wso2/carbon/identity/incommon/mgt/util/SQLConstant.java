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
 * This class contains SQL queries to manage two tables to handle metadata XML
 */
public class SQLConstant {

    // Manager for Scheduler Context
    public static final String INSERT_SCHEDULER =
            "INSERT INTO IDN_INCOMMON_SCHEDULER (START_DATE, PERIOD, URL, LAST_MODIFIED_DATE, REFRESH_ENABLE, "
                    + "TENANT_ID) VALUES (?, ?, ?, ?, ?, ?);";

    public static final String UPDATE_SCHEDULER = "UPDATE IDN_INCOMMON_SCHEDULER SET START_DATE = ?, PERIOD = ?, "
            + "LAST_MODIFIED_DATE = ?, REFRESH_ENABLE = ? WHERE URL = ? AND TENANT_ID = ?;";

    public static final String UPDATE_LAST_MODIFIED_DATE =
            "UPDATE IDN_INCOMMON_SCHEDULER SET LAST_MODIFIED_DATE = ? " + "WHERE URL = ? AND TENANT_ID = ?;";

    public static final String DELETE_SCHEDULER = "DELETE FROM IDN_INCOMMON_SCHEDULER WHERE URL = ? AND TENANT_ID = ?;";

    public static final String GET_SCHEDULER_FROM_URL_AND_TENANT_ID = "SELECT START_DATE, PERIOD, LAST_MODIFIED_DATE, REFRESH_ENABLE FROM "
            + "IDN_INCOMMON_SCHEDULER WHERE URL = ? AND TENANT_ID = ?;";

    public static final String GET_SCHEDULER_FROM_TENANT_ID = "SELECT START_DATE, PERIOD, URL, LAST_MODIFIED_DATE, REFRESH_ENABLE FROM "
            + "IDN_INCOMMON_SCHEDULER WHERE TENANT_ID = ?;";

    // Manager for Configured SAML Entity Info
    public static final String INSERT_SAML_ENTITY_INFO = "INSERT INTO IDN_INCOMMON_SAML_ENTITY (ENTITY_ID, ENTITY_TYPE,"
            + " STATUS, TENANT_ID) VALUES (?, ?, ?, ?);";

    public static final String UPDATE_SAML_ENTITY_INFO = "UPDATE IDN_INCOMMON_SAML_ENTITY SET ENTITY_TYPE = ?, STATUS ="
            + " ? WHERE ENTITY_ID =? AND TENANT_ID = ?;";

    public static final String DELETE_SAML_ENTITY_INFO =
            "DELETE FROM IDN_INCOMMON_SAML_ENTITY WHERE ENTITY_ID = ? " + "AND TENANT_ID = ?;";

    public static final String GET_SAML_ENTITY_INFO =
            "SELECT ENTITY_ID, ENTITY_TYPE, STATUS FROM " + "IDN_INCOMMON_SAML_ENTITY WHERE TENANT_ID = ?;";

    public static final String GET_REQUIRED_SP_OR_IDP_COUNT =
            "SELECT COUNT(ENTITY_ID) FROM IDN_INCOMMON_SAML_ENTITY " + "WHERE (ENTITY_TYPE = ? AND STATUS = ? ) AND TENANT_ID = ?;";

    public static final String GET_REQUIRED_SP_OR_IDP_ID =
            "SELECT ENTITY_ID FROM IDN_INCOMMON_SAML_ENTITY " + "WHERE (ENTITY_TYPE = ? AND STATUS = ?) AND TENANT_ID = ?;";
}
