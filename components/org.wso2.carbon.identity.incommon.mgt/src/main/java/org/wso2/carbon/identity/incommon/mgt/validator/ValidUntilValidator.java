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
package org.wso2.carbon.identity.incommon.mgt.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ValidUntilValidator {

    private static Log log = LogFactory.getLog(ValidUntilValidator.class);
    private static final long THREE_WEEKS_IN_MILLIS = 1814400000;
    private String validUntil;

    /**
     * Validate ValidUntil date in metadata xml as per the current date
     *
     * @param validUntilValue
     * @return
     */
    public boolean validateValidUntil(String validUntilValue) {

        if (validUntilValue.isEmpty()) {
            log.info("Valid Until tag is empty");
            return false;
        }

        long currentMillis = System.currentTimeMillis();
        LocalDateTime validUntil = LocalDateTime
                .parse(validUntilValue, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        long validUntilMillis = validUntil.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (currentMillis > validUntilMillis) {
            log.info("Valid Until value is invalid");
            return false;
        }

        long diff = validUntilMillis - currentMillis;
        // ValidUntil date should not be beyond three weeks in future from the current date
        if (diff < THREE_WEEKS_IN_MILLIS) {
            log.info("Valid Until value is verified successfully");
            return true;
        } else {
            log.info("Valid Until value is invalid");
            return false;
        }
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
