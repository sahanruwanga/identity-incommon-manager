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
import org.apache.http.HttpStatus;
import org.w3c.dom.Document;
import org.wso2.carbon.identity.incommon.mgt.util.FileUtil;
import org.wso2.carbon.identity.incommon.mgt.util.InCommonMgtConstants;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLDownloader {

    private static final Log log = LogFactory.getLog(XMLDownloader.class);

    /**
     * Download metadata xml file using HTTP Conditional GET
     *
     * @param lastModifiedDate this is used to pass when download the file to check the last date of modification
     * @param fileURL this is the URL of the metadata file to be downloaded from InCommon metadata server
     * @return
     */
    public String doHTTPConditionalGET(String lastModifiedDate, String fileURL) {

        log.info("Metadata xml download in process.");

        String newLastModifiedDate = "";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        Document doc = null;
        Transformer transformer = null;
        Result output = null;
        Source input = null;

        try {
            docBuilder = dbf.newDocumentBuilder();
            URL url = new URL(fileURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty(InCommonMgtConstants.IF_MODIFIED_SINCE, lastModifiedDate);
            // Checking the response code, 200 - modified(return the resource)/  304 - not modified
            if (connection.getResponseCode() == HttpStatus.SC_OK) {
                newLastModifiedDate = connection.getHeaderField(InCommonMgtConstants.LAST_MODIFIED);

                doc = docBuilder.parse(connection.getInputStream());
                transformer = TransformerFactory.newInstance().newTransformer();
                output = new StreamResult(new File(FileUtil.getMetadataXMLFileLocation()).getPath());

                input = new DOMSource(doc);
                transformer.transform(input, output);
                log.info("Finish download process and new metadata update is downloaded.");
            } else if (connection.getResponseCode() == HttpStatus.SC_NOT_MODIFIED) {
                newLastModifiedDate = lastModifiedDate;
                log.info("Finished download process and metadata file is not modified.");
            } else {
                newLastModifiedDate = lastModifiedDate;
                log.info("No response from InCommon metadata file server.");
            }
        } catch (ParserConfigurationException e) {
            log.error("Metadata XML is not parsed successfully.");
        } catch (IOException e) {
            log.error("Can not find metadata xml.");
        } catch (TransformerConfigurationException e) {
            log.error("Unable to transform metadata file from DOM to XML.");
        } catch (TransformerException e) {
            log.error("Unable to transform metadata file from DOM to XML.");
        } catch (SAXException e) {
            log.error("Unable to apply SAX parser to parse the XML.");
        }
        return newLastModifiedDate;
    }
}
