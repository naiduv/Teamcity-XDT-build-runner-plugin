package xsltRunner.agent;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.apache.commons.io.FilenameUtils;
import xsltRunner.common.PluginConstants;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */


public class XsltRunnerConfiguration {
    enum Platform {
        Windows,
        Mac
    }

    final String inputPath;
    final String outputPath;
    final String xslPath;

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

    final static String windowsXsltPath = "C:\\Program Files (x86)\\Xslt\\Editor\\xslt.exe";
    final static String macXsltPath = "/Applications/Xslt/Xslt.app/Contents/MacOS/Xslt";

    public String pluginMsxslPath = "C:\\msxsl.exe";

    final static String windowsLogPath = System.getenv("LOCALAPPDATA") + "\\Xslt\\Editor\\Editor.log";
    final static String macLogPath = System.getProperty("user.home") + "/Library/Logs/Xslt/Editor.log";

    public XsltRunnerConfiguration(BuildAgentConfiguration agentConfiguration,
                                    Map<String, String> runnerParameters,
                                    AgentRunningBuild agentRunningBuild) {

        if (agentConfiguration.getSystemInfo().isWindows()) {
            platform = XsltRunnerConfiguration.Platform.Windows;
        } else {
            platform = XsltRunnerConfiguration.Platform.Mac;
        }

        pluginMsxslPath = agentConfiguration.getAgentPluginsDirectory().getPath() + "//" + PluginConstants.RUN_TYPE + "//msxsl.exe";

        inputPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_INPUT_PATH));
        outputPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_OUTPUT_PATH));
        xslPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_XSL_PATH));

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

    String getXsltPath() {
        return getXsltPath(platform);
    }

    String getXsltLogPath() {
        return getXsltLogPath(platform);
    }
    
    String getCleanedLogPath() {
        return cleanedLogPath.getPath();
    }
    
    String getInterestedLogPath() {
        if (useCleanedLog) {
            return getCleanedLogPath();
        } else {
            return getXsltLogPath();
        }
    }

    static String getXsltPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsXsltPath;
            case Mac:
                return macXsltPath;
        }

        return null;
    }

    static String getXsltLogPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsLogPath;
            case Mac:
                return macLogPath;
        }

        return null;
    }
}


