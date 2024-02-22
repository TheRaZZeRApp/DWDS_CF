package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.config.Config;
import com.therazzerapp.milorazlib.excel.csv.CSVWriter;
import com.therazzerapp.milorazlib.files.FileUtils;
import com.therazzerapp.milorazlib.handler.BackupHandler;
import com.therazzerapp.milorazlib.logger.ComplexLogger;
import com.therazzerapp.milorazlib.logger.LoggerFactory;
import com.therazzerapp.milorazlib.logger.Logging;
import de.pauleduardkoenig.dwds_cf.gui.GUIMain;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class DWDS_CF{

	public static final ComplexLogger logger = LoggerFactory.createComplexLogger(1, Constants.PROGRAMM_NAME, Constants.VERSION, null, true);
	public static final Config<ConfigType> config = new Config<>(ConfigType.values(), new File("./data/config.json"), StandardCharsets.UTF_8, logger);

	public static void main(String[] args){
		logger.setDepth(config.getAsInt(ConfigType.LOG_LEVEL));

		if (BackupHandler.backup(config.getAsString(ConfigType.VERSION), Constants.VERSION, "./data", true, logger)){
			config.setConfigProperty(ConfigType.VERSION, Constants.VERSION);
		}

		initFile(ConfigType.WORDLIST.getDefaultValue());
		initFile(ConfigType.CSV_CUSTOM_ROWS.getDefaultValue());
		File autotag = new File(ConfigType.CSV_AUTOTAG_LIST.getDefaultValue());
		if (!autotag.exists()){
			initFile(ConfigType.CSV_AUTOTAG_LIST.getDefaultValue());
			CSVWriter csvWriter = new CSVWriter(autotag, logger);
			csvWriter.addNextLine(new String[]{"Lemma", "Tag"});
			csvWriter.export();
		}

		File batchFile = new File("./run_windows.bat");
		if (!batchFile.exists()){
			FileUtils.exportFile("@echo off\njava -jar DWDS_CF.jar\npause", batchFile, StandardCharsets.UTF_8, logger);
		}
		File shFile = new File("./run_mac_linux.sh");
		if (!shFile.exists()){
			FileUtils.exportFile("#!/bin/bash\njava -jar DWDS_CF.jar\nread -p \"Press Enter to continue...\"", shFile, StandardCharsets.UTF_8, logger);
		}

		if (args.length == 6 && args[0].equalsIgnoreCase("-t")){
			try {
				Tagger.tagCSVFile(new File(args[1]), new File(args[2]), args[3].charAt(0), args[4].charAt(0), Charset.forName(args[5]));
			} catch (UnsupportedCharsetException e){
				Logging.error(logger, "(Tagger) Charset: " + args[5] + " not found!");
			} catch (NullPointerException e){
				Logging.stackTrace(logger, e);
			} catch (IndexOutOfBoundsException e){
				Logging.errorStackTrace(logger, e, "(Tagger) Invalid separator or encloser!");
			}
		} else if (args.length >= 1 && args.length < 6 && args[0].equalsIgnoreCase("-t")){
			Logging.error(logger, "(Tagger) not enough arguments for tagging!");
			Logging.error(logger, "(Tagger) Syntax: -t inputPath outputPath separator:char encloser:char charset");
			Logging.error(logger, "(Tagger) Charsets, use Java standard charsets e.g. UTF-8");
		}

		GUIMain.init();
	}

	public static void initFile(String path){
		File customRows = new File(path);
		if (!customRows.exists()){
			try{
				//noinspection ResultOfMethodCallIgnored
				customRows.createNewFile();
			} catch (IOException e){
				Logging.stackTrace(logger, e);
			}
		}
	}
}
