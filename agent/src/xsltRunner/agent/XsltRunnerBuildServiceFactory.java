/*
 * Copyright 2000-2010 JetBrains s.r.o.
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

package xsltRunner.agent;


import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.log.Loggers;
import org.jetbrains.annotations.NotNull;
import xsltRunner.common.PluginConstants;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;
import java.io.File;

public class XsltRunnerBuildServiceFactory implements CommandLineBuildServiceFactory {

    public XsltRunnerBuildServiceFactory() {
    }

    @NotNull
    public CommandLineBuildService createService() {
        return new XsltRunnerBuildService();
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {

            @NotNull
            public String getType() {
                return PluginConstants.RUN_TYPE;
            }

            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                setupConfigurationParameters(agentConfiguration);
                return agentConfiguration.getSystemInfo().isWindows() || agentConfiguration.getSystemInfo().isMac();
            }

            /**
             * Setup agent configuration parameters.
             * @param agentConfiguration build agent configuration.
             */
            public void setupConfigurationParameters(@NotNull BuildAgentConfiguration agentConfiguration) {
                String xsltPath = null;
                String xsltLog = null;

                // Get xslt path and editor log path for supported platforms.
                if (agentConfiguration.getSystemInfo().isWindows()) {
                    xsltPath = XsltRunnerConfiguration.getXsltPath(XsltRunnerConfiguration.Platform.Windows);
                    xsltLog = XsltRunnerConfiguration.getXsltLogPath(XsltRunnerConfiguration.Platform.Windows);
                } else if (agentConfiguration.getSystemInfo().isMac()) {
                    xsltPath = XsltRunnerConfiguration.getXsltPath(XsltRunnerConfiguration.Platform.Mac);
                    xsltLog = XsltRunnerConfiguration.getXsltLogPath(XsltRunnerConfiguration.Platform.Mac);
                }

                if (xsltPath != null && xsltLog != null) {
                    File file = new File(xsltPath);

                    if (file.exists()) {
                        agentConfiguration.addConfigurationParameter("xslt.exe", xsltPath);
                        agentConfiguration.addConfigurationParameter("xslt.log", xsltLog);

                        // don't hard-code the xslt version
                        if (agentConfiguration.getSystemInfo().isMac()) {
                            readMacXsltVersion(agentConfiguration);
                        } else {
                            // find on windows - reading versionNumber from the exe ( Windows PE specification)
                            readWindowsXsltVersion(agentConfiguration);
                        }
                    }
                }
            }

            private void readMacXsltVersion(@NotNull BuildAgentConfiguration agentConfiguration) {
                try {
                    XMLPropertyListConfiguration config = new XMLPropertyListConfiguration("/Applications/Xslt/Xslt.app/Contents/Info.plist");
                    if (config != null) {
                        String version = config.getString("CFBundleVersion");   
                        if (version != null) {             
                            // strip off f-part
                            int indexOfF = version.indexOf("f");
                            if (indexOfF > -1) {
                                version = version.substring(0,indexOfF);
                            }
                            Loggers.AGENT.info("Found xslt.version= " + version);
                            agentConfiguration.addConfigurationParameter("xslt.version", version);
                        }

                        String buildNumber = config.getString("XsltBuildNumber");
                        if (buildNumber != null) {
                            Loggers.AGENT.info("Found xslt.buildNumber= " + buildNumber);
                            agentConfiguration.addConfigurationParameter("xslt.buildNumber", buildNumber);
                        }

                    }

                } catch (Exception e) {
                    // had trouble detecting version
                    Loggers.AGENT.error("Exception getting xslt version :" + e.getMessage());
                }
            }

            private void readWindowsXsltVersion(@NotNull BuildAgentConfiguration agentConfiguration){
                String fileName = XsltRunnerConfiguration.getXsltPath(XsltRunnerConfiguration.Platform.Windows);
                Loggers.AGENT.info("Reading file to get buildNumber: " + fileName);
                try{
                    String buildNumber  =  FileVersionInfo.getShortVersionNumber(fileName);
                    Loggers.AGENT.info("Found xslt buildNumber: " + buildNumber);
                    agentConfiguration.addConfigurationParameter("xslt.buildNumber", buildNumber);
                } catch(Exception e){
                    Loggers.AGENT.error("Exception getting xslt version :" + e.getMessage());
                }

            }
        };
    }
}





