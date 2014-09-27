package xdtRunner.agent;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.apache.commons.io.FilenameUtils;
import xdtRunner.common.PluginConstants;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */


public class XdtRunnerConfiguration {
    enum Platform {
        Windows,
        Mac
    }

    final String[] inputPath = new String[10];
    final String[] outputPath = new String[10];
    final String[] xdPath = new String[10];
    final int configsCount;

    final boolean quit;
    final boolean batchMode;
    final boolean showMe;
    final boolean noGraphics;
    final boolean clearBefore;
    final boolean cleanAfter;
    final boolean useCleanedLog;
    final boolean warningsAsErrors;
    final String lineListPath;
    final String projectPath;
    final String executeMethod;
    final String buildPlayer;
    final String buildPath;
    final String extraOpts;

    final Platform platform;
    final java.io.File cleanedLogPath;

    final boolean ignoreLogBefore;
    final String ignoreLogBeforeText;

    final static String windowsXdtPath = "";
    final static String macXdtPath = "";

    public String pluginMsxdPath;

    final static String windowsLogPath = System.getenv("LOCALAPPDATA") + "\\Xdt\\Editor\\Editor.log";
    final static String macLogPath = System.getProperty("user.home") + "/Library/Logs/Xdt/Editor.log";

    public XdtRunnerConfiguration(BuildAgentConfiguration agentConfiguration,
                                    Map<String, String> runnerParameters,
                                    AgentRunningBuild agentRunningBuild) {

        if (agentConfiguration.getSystemInfo().isWindows()) {
            platform = XdtRunnerConfiguration.Platform.Windows;
        } else {
            platform = XdtRunnerConfiguration.Platform.Mac;
        }

        pluginMsxdPath = agentConfiguration.getAgentPluginsDirectory().getPath() + "//" + PluginConstants.RUN_TYPE + "//ctt.exe";

        configsCount = Integer.parseInt(FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_CONFIGS_COUNT)));

        for(int i=0;i<configsCount;i++) {
            inputPath[i] = FilenameUtils.separatorsToSystem(
                    Parameters.getString(runnerParameters, PluginConstants.PROPERTY_INPUT_PATH + '_' + i));
            outputPath[i] = FilenameUtils.separatorsToSystem(
                    Parameters.getString(runnerParameters, PluginConstants.PROPERTY_OUTPUT_PATH + '_' +i));
            xdPath[i] = FilenameUtils.separatorsToSystem(
                    Parameters.getString(runnerParameters, PluginConstants.PROPERTY_XD_PATH + '_' + i));
        }


        quit = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_QUIT);
        showMe = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_SHOW_ME);
        batchMode = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_BATCH_MODE);
        noGraphics = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_NO_GRAPHICS);
        projectPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_PROJECT_PATH));
        lineListPath = FilenameUtils.separatorsToSystem(Parameters.getString(runnerParameters, PluginConstants.PROPERTY_LINELIST_PATH));
        executeMethod = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_EXECUTE_METHOD);
        buildPlayer = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_PLAYER);
        buildPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_PATH));
        extraOpts = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_EXTRA);

        clearBefore = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_CLEAR_OUTPUT_BEFORE);
        cleanAfter = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_CLEAN_OUTPUT_AFTER);
        warningsAsErrors = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_WARNINGS_AS_ERRORS);

        // set cleaned log path to %temp%/cleaned-%teamcity.build.id%.log
        cleanedLogPath = new java.io.File(
                agentRunningBuild.getBuildTempDirectory(),
                String.format("cleaned-%d.log", agentRunningBuild.getBuildId()) );
        useCleanedLog = true;

        ignoreLogBefore = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_LOG_IGNORE);
        ignoreLogBeforeText = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_LOG_IGNORE_TEXT);

    }

    String getXdtPath() {
        return getXdtPath(platform);
    }

    String getXdtLogPath() {
        return getXdtLogPath(platform);
    }
    
    String getCleanedLogPath() {
        return cleanedLogPath.getPath();
    }
    
    String getInterestedLogPath() {
        if (useCleanedLog) {
            return getCleanedLogPath();
        } else {
            return getXdtLogPath();
        }
    }

    static String getXdtPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsXdtPath;
            case Mac:
                return macXdtPath;
        }

        return null;
    }

    static String getXdtLogPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsLogPath;
            case Mac:
                return macLogPath;
        }

        return null;
    }
}


