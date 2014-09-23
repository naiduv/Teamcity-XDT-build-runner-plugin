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

package xdtRunner.agent;


import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.log.Loggers;
import org.jetbrains.annotations.NotNull;
import xdtRunner.common.PluginConstants;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;
import java.io.File;

public class XdtRunnerBuildServiceFactory implements CommandLineBuildServiceFactory {

    public XdtRunnerBuildServiceFactory() {
    }

    @NotNull
    public CommandLineBuildService createService() {
        return new XdtRunnerBuildService();
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
                String xdtPath = null;
                String xdtLog = null;

                // Get xdt path and editor log path for supported platforms.
                if (agentConfiguration.getSystemInfo().isWindows()) {
                    xdtPath = XdtRunnerConfiguration.getXdtPath(XdtRunnerConfiguration.Platform.Windows);
                    xdtLog = XdtRunnerConfiguration.getXdtLogPath(XdtRunnerConfiguration.Platform.Windows);
                } else if (agentConfiguration.getSystemInfo().isMac()) {
                    xdtPath = XdtRunnerConfiguration.getXdtPath(XdtRunnerConfiguration.Platform.Mac);
                    xdtLog = XdtRunnerConfiguration.getXdtLogPath(XdtRunnerConfiguration.Platform.Mac);
                }

                if (xdtPath != null && xdtLog != null) {
                    File file = new File(xdtPath);

                    if (file.exists()) {
                        agentConfiguration.addConfigurationParameter("ctt.exe", xdtPath);
                        agentConfiguration.addConfigurationParameter("ctt.log", xdtLog);

                        // don't hard-code the xdt version
                        if (agentConfiguration.getSystemInfo().isMac()) {
                            readMacXdtVersion(agentConfiguration);
                        } else {
                            // find on windows - reading versionNumber from the exe ( Windows PE specification)
                            readWindowsXdtVersion(agentConfiguration);
                        }
                    }
                }
            }

            private void readMacXdtVersion(@NotNull BuildAgentConfiguration agentConfiguration) {
                try {
                    XMLPropertyListConfiguration config = new XMLPropertyListConfiguration("/Applications/Xdt/Xdt.app/Contents/Info.plist");
                    if (config != null) {
                        String version = config.getString("CFBundleVersion");   
                        if (version != null) {             
                            // strip off f-part
                            int indexOfF = version.indexOf("f");
                            if (indexOfF > -1) {
                                version = version.substring(0,indexOfF);
                            }
                            Loggers.AGENT.info("Found xdt.version= " + version);
                            agentConfiguration.addConfigurationParameter("xdt.version", version);
                        }

                        String buildNumber = config.getString("XdtBuildNumber");
                        if (buildNumber != null) {
                            Loggers.AGENT.info("Found xdt.buildNumber= " + buildNumber);
                            agentConfiguration.addConfigurationParameter("xdt.buildNumber", buildNumber);
                        }

                    }

                } catch (Exception e) {
                    // had trouble detecting version
                    Loggers.AGENT.error("Exception getting xdt version :" + e.getMessage());
                }
            }

            private void readWindowsXdtVersion(@NotNull BuildAgentConfiguration agentConfiguration){
                String fileName = XdtRunnerConfiguration.getXdtPath(XdtRunnerConfiguration.Platform.Windows);
                Loggers.AGENT.info("Reading file to get buildNumber: " + fileName);
                try{
                    String buildNumber  =  FileVersionInfo.getShortVersionNumber(fileName);
                    Loggers.AGENT.info("Found xdt buildNumber: " + buildNumber);
                    agentConfiguration.addConfigurationParameter("xdt.buildNumber", buildNumber);
                } catch(Exception e){
                    Loggers.AGENT.error("Exception getting xdt version :" + e.getMessage());
                }

            }
        };
    }
}





