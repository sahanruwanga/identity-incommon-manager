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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<jsp:include page="../dialog/display_messages.jsp"/>

<script type="text/javascript">

    function saveConfig() {
        $("#config-metadata").submit();
    }

    function enablePeriodTextBox(checkBox) {
        if ($(checkBox).is(':checked')) {
            $("#refresh-period").prop('disabled', false);
        } else {
            $("#refresh-period").prop('disabled', true);
            $("#refresh-period").val("");
        }
    }

    function validateRefreshPeriodForIllegal(refreshPeriod) {
        var isValid = doValidateInput(refreshPeriod, "Provided Refresh Period is invalid.");
        if (isValid) {
            return true;
        }
        return false;
    }

</script>

<fmt:bundle basename="org.wso2.carbon.identity.incommon.mgt.ui.i18n.Resources">

    <div id="middle">

        <h2>
            <fmt:message key='title.incommon.metadata.configuration'/>
        </h2>

        <div id="workArea">
            <form id="config-metadata" name="config-incommon-metadata-file" method="post"
                  action="add-new-incommon-config-finish-ajaxprocessor.jsp">
                <div class="sectionSeperator">
                    <fmt:message key='title.basic.information'/>
                </div>

                <div class="sectionSub">
                    <table class="carbonFormTable">
                        <tr>
                            <td class="leftCol-med">
                                <label for="refresh-enabled"><fmt:message key='config.enable.auto.refresh'/></label>
                            </td>
                            <td>
                                <div class="sectionCheckbox">
                                    <input id="refresh-enabled" name="refresh-enabled" type="checkbox" autofocus
                                           onchange="enablePeriodTextBox(this);"/>
                                    <div class="sectionHelp">
                                        <fmt:message key='help.refresh.enable'/>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:15%" class="leftCol-med labelField">
                                <fmt:message key='config.url.for.metadata.file'/>:
                            </td>
                            <td>
                                <input id="file-url" name="file-url" type="url" style="width:50%"
                                       value="https://md.incommon.org/InCommon/InCommon-metadata.xml" readonly/>
                                <div class="sectionHelp">
                                    <fmt:message key='help.url'/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:15%" class="leftCol-med labelField">
                                <fmt:message key='config.refresh.period'/>:
                            </td>
                            <td>
                                <input id="refresh-period" name="refresh-period" type="text" style="width:10%"
                                       value="" disabled/>
                                <span style="display: inline" class="leftCol-med">
                                    <fmt:message key='config.minute'/>
                                </span>
                                <div class="sectionHelp">
                                    <fmt:message key='help.period'/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="buttonRow">
                    <input type="button" class="button" value="<fmt:message key='button.save.config'/>"
                           onclick="saveConfig();"/>
                    <input type="button" class="button" onclick="" value="<fmt:message key='button.cancel'/>"/>
                </div>
            </form>
        </div>
    </div>
</fmt:bundle>
