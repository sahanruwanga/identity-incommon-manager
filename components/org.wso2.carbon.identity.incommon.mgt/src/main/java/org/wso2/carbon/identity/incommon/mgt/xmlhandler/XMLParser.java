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
package org.wso2.carbon.identity.incommon.mgt.xmlhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.incommon.mgt.util.FileUtil;
import org.xml.sax.SAXException;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLParser {

    private static final Log log = LogFactory.getLog(XMLParser.class);

    private static final XMLParser instance = new XMLParser();

    /**
     * Parse xml file from file system to read in XMLHandler
     * XML file is saved inside the data folder in repository file
     *
     */
    public void parse() {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = saxParserFactory.newSAXParser();
            log.info("InCommon metadata XML is called for parsing from " + FileUtil.getMetadataXMLFileLocation());
            saxParser.parse(FileUtil.getMetadataXMLFileLocation(), new XMLHandler());
        } catch (ParserConfigurationException e) {
            log.error("Couldn't parse the metadata xml file from the given location");
        } catch (SAXException e) {
            log.error("xml file is not parsed correctly or invalid");
        } catch (IOException e) {
            log.error("Metadata xml file can not be found");
        }
    }

    public static XMLParser getInstance() {
        return instance;
    }
}
