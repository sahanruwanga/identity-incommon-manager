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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * this holds the context object for manage metadata xml download and update processes
 */
public class SchedulerContext {

    private static final int MINUTE_IN_MILLIS = 60000;
    // Date Format - 2018-11-23T23:49:17.363
    private String startDateTime;
    // Period in minutes
    private int period;
    private String url;
    private String lastModifiedDate;
    private boolean refreshEnable;

    public SchedulerContext(String startDateTime, int period, String url, String lastModifiedDate, boolean refreshEnable) {

        this.startDateTime = startDateTime;
        this.period = period;
        this.url = url;
        this.lastModifiedDate = lastModifiedDate;
        this.refreshEnable = refreshEnable;
    }

    /**
     * Get period value in milliseconds for calculations
     *
     * @return
     */
    public long getPeriodInMillis() {

        long periodInMillis;
        periodInMillis = this.getPeriod() * MINUTE_IN_MILLIS;
        return periodInMillis;
    }

    /**
     * Get configuration start time in milliseconds for calculations
     *
     * @return
     */
    public long getStartDateTimeInMillis() {

        long startDateTimeInMillis;
        LocalDateTime startDateTime = LocalDateTime
                .parse(this.getStartDateTime(), DateTimeFormatter.ofPattern("yyyy" + "-MM-dd'T'HH:mm:ss.SSS"));
        startDateTimeInMillis = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return startDateTimeInMillis;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean getRefreshEnable() {
        return refreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
    }
}
