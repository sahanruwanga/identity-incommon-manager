<%@ page import="org.wso2.carbon.identity.incommon.mgt.ui.client.SAMLEntityConfigurationClient" %>
<!--
~ Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied. See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<%
    pageContext.setAttribute("saml_entities", SAMLEntityConfigurationClient.getFailedSPCount());
%>

<jsp:include page="../dialog/display_messages.jsp"/>

<fmt:bundle basename="org.wso2.carbon.identity.incommon.mgt.ui.i18n.Resources">

    <div id="middle">

        <h2>
            <fmt:message key='title.incommon.saml.entities'/>
        </h2>

        <div id="workArea">
            <div class="sectionSub">
                <table class="styledLeft" id="incommonSAMLEntityListTable">
                    <thead>
                    <tr>
                        <th class="leftCol-med"><fmt:message key='last.update.table.column'/></th>
                        <th class="leftCol-med"><fmt:message key='metadata.file.table.column'/></th>
                        <th class="leftCol-med"><fmt:message key='service.provider.table.column'/></th>
                        <th class="leftCol-med"><fmt:message key='identity.provider.table.column'/></th>
                        <th style="width: 30%"><fmt:message key='actions.table.column'/></th>
                    </tr>
                    </thead>
                    <c:if test="${not empty saml_entities}">
                    <tbody>
                    <tr>
                        <td>${saml_entities.lastUpdate}</td>
                        <td>${saml_entities.metadataFile}</td>
                        <td>${saml_entities.successSPCount} - Success <br> ${saml_entities.failedSPCount} - Fail</td>
                        <td>${saml_entities.successIdPCount} - Success <br> ${saml_entities.failedIdPCount} - Fail</td>
                        <td>
                            <a title="<fmt:message key='edit'/>"
                               onclick=""
                               href="#"
                               class="icon-link"
                               style="background-image: url(images/edit.gif)">
                                <fmt:message key='edit'/>
                            </a>
                            <a title="<fmt:message key='fix'/>"
                               onclick=""
                               href="#"
                               class="icon-link"
                               style="background-image: url(images/edit.gif)">
                                <fmt:message key='fix'/>
                            </a>
                            <a title="<fmt:message key='delete'/>"
                               onclick=""
                               href="#"
                               class="icon-link"
                               style="background-image: url(images/delete.gif)">
                            <fmt:message key='delete'/>
                            </a>
                        </td>
                    </tr>
                    </tbody>
                    </c:if>
                    <c:if test="${empty saml_entities}">
                        <tbody>
                        <tr>
                            <td colspan="5"><i>No InCommon SAML Entity Configuration</i></td>
                        </tr>
                        </tbody>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</fmt:bundle>