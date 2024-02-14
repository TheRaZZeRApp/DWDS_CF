package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.config.Config;
import com.therazzerapp.milorazlib.files.FileUtils;
import com.therazzerapp.milorazlib.handler.BackupHandler;
import com.therazzerapp.milorazlib.logger.ComplexLogger;
import com.therazzerapp.milorazlib.logger.LoggerFactory;
import com.therazzerapp.milorazlib.logger.Logging;
import de.pauleduardkoenig.dwds_cf.gui.GUIMain;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class DWDS_CF {

    public static final ComplexLogger logger = LoggerFactory.createComplexLogger(1, Constants.PROGRAMM_NAME, Constants.VERSION, null, true);
    public static final Config<ConfigType> config = new Config<>(ConfigType.values(), new File("./data/config.json"), StandardCharsets.UTF_8, logger);

    public static void main(String[] args) {
        logger.setDepth(config.getAsInt(ConfigType.LOG_LEVEL));

        boolean update = BackupHandler.backup(config.getAsString(ConfigType.VERSION), Constants.VERSION, "./data", true, logger);
        if (update) {
            config.setConfigProperty(ConfigType.VERSION, Constants.VERSION);
        }

        initFile(ConfigType.WORDLIST.getDefaultValue());
        initFile(ConfigType.CSV_CUSTOM_ROWS.getDefaultValue());

        File batchFile = new File("./run_windows.bat");
        if (!batchFile.exists()) {
            FileUtils.exportFile("@echo off\njava -jar DWDS_CF.jar\npause", batchFile, StandardCharsets.UTF_8, logger);
        }
        File shFile = new File("./run_mac_linux.sh");
        if (!shFile.exists()) {
            FileUtils.exportFile("#!/bin/bash\njava -jar DWDS_CF.jar\nread -p \"Press Enter to continue...\"", shFile, StandardCharsets.UTF_8, logger);
        }

        GUIMain.init();
    }

    public static void initFile(String path) {
        File customRows = new File(path);
        if (!customRows.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                customRows.createNewFile();
            } catch (IOException e) {
                Logging.stackTrace(logger, e);
            }
        }
    }
}
