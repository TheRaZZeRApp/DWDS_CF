package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVRow;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVWriter;
import com.therazzerapp.milorazlib.logger.Logging;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class CSVConverter{

	private static final Pattern CUSTOM_ROW_PATTERN = Pattern.compile("(\\d*);(.*)");

	private static int getHeaderVal(String[] header, String head){
		for (int i = 0; i < header.length; i++){
			if (head.equalsIgnoreCase(header[i])){
				return i;
			}
		}
		return -1;
	}

	public static void formatCSVFile(DWDSRequestBuilder.ViewType viewType, Queue<ConcurrentCSVRow> lines, File export, String[] header, Map<Integer, String> customRows){
		ConcurrentCSVWriter csvWriter = new ConcurrentCSVWriter(export, viewType == DWDSRequestBuilder.ViewType.CSV ? DWDS_CF.config.getAsString(ConfigType.CSV_SEPARATOR).charAt(0) : '\t', DWDS_CF.config.getAsString(ConfigType.CSV_ESCAPE).charAt(0), "\n", DWDS_CF.config.getAsBoolean(ConfigType.CSV_ESCAPE_HTML), StandardCharsets.UTF_8, DWDS_CF.logger);
		csvWriter.addNextLine(mergeRows(header, customRows, true));
		int blankLines = DWDS_CF.config.getAsInt(ConfigType.CSV_SPACES);
		Set<Trio<String, String, String>> sentences = new HashSet<>();
		if (blankLines > 0){
			if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG)){
				if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_REMOVE_DUPLICATES)){
					lines.forEach(l -> {
						Trio<String, String, String> sentence = new Trio<>(l.getRow()[getHeaderVal(header, "contextbefore")], l.getRow()[getHeaderVal(header, "hit")], l.getRow()[getHeaderVal(header, "contextafter")]);
						if (!sentences.contains(sentence)){
							sentences.add(sentence);
							csvWriter.addEmptyLine(blankLines);
							csvWriter.addNextLine(Tagger.tagRow(mergeRows(l.getRow(), customRows, false)));
						}
					});
				} else {
					lines.forEach(l -> {
						csvWriter.addEmptyLine(blankLines);
						csvWriter.addNextLine(Tagger.tagRow(mergeRows(l.getRow(), customRows, false)));
					});
				}
			} else {
				if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_REMOVE_DUPLICATES)){
					lines.forEach(l -> {
						Trio<String, String, String> sentence = new Trio<>(l.getRow()[getHeaderVal(header, "contextbefore")], l.getRow()[getHeaderVal(header, "hit")], l.getRow()[getHeaderVal(header, "contextafter")]);
						if (!sentences.contains(sentence)){
							sentences.add(sentence);
							csvWriter.addEmptyLine(blankLines);
							csvWriter.addNextLine(mergeRows(l.getRow(), customRows, false));
						}
					});
				} else {
					lines.forEach(l -> {
						csvWriter.addEmptyLine(blankLines);
						csvWriter.addNextLine(mergeRows(l.getRow(), customRows, false));
					});
				}

			}
		} else {
			if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG)){
				if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_REMOVE_DUPLICATES)){
					lines.forEach(l -> {
						Trio<String, String, String> sentence = new Trio<>(l.getRow()[getHeaderVal(header, "contextbefore")], l.getRow()[getHeaderVal(header, "hit")], l.getRow()[getHeaderVal(header, "contextafter")]);
						if (!sentences.contains(sentence)){
							sentences.add(sentence);
							csvWriter.addEmptyLine(blankLines);
							csvWriter.addNextLine(Tagger.tagRow(mergeRows(l.getRow(), customRows, false)));
						}
					});
				} else {
					lines.forEach(l -> csvWriter.addNextLine(Tagger.tagRow(mergeRows(l.getRow(), customRows, false))));
				}

			} else {
				if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_REMOVE_DUPLICATES)){
					lines.forEach(l -> {
						Trio<String, String, String> sentence = new Trio<>(l.getRow()[getHeaderVal(header, "contextbefore")], l.getRow()[getHeaderVal(header, "hit")], l.getRow()[getHeaderVal(header, "contextafter")]);
						if (!sentences.contains(sentence)){
							sentences.add(sentence);
							csvWriter.addEmptyLine(blankLines);
							csvWriter.addNextLine(mergeRows(l.getRow(), customRows, false));
						}
					});
				} else {
					lines.forEach(l -> csvWriter.addNextLine(mergeRows(l.getRow(), customRows, false)));
				}

			}
		}
		csvWriter.export();
	}

	private static String[] mergeRows(String[] originalRow, Map<Integer, String> customRows, boolean header){
		String[] newRow = new String[originalRow.length + customRows.size()];
		customRows.forEach((k, v) -> newRow[k] = header ? v : "");
		for (String s : originalRow){
			for (int i = 0; i < newRow.length; i++){
				if (newRow[i] == null){
					newRow[i] = s;
					break;
				}
			}
		}
		return newRow;
	}

	public static Map<Integer, String> compileCustomRows(String customRowsRaw){
		Map<Integer, String> customRows = new ConcurrentHashMap<>();
		for (String s : customRowsRaw.split("\n")){
			if (s == null || s.isBlank()){
				continue;
			}
			Matcher m = CUSTOM_ROW_PATTERN.matcher(s.trim());
			if (m.find()){
				try{
					int rowNumber = Integer.parseInt(m.group(1)) - 1;
					if (rowNumber <= 0){
						Logging.warning(DWDS_CF.logger, "(Fetcher) Custom row number 0 or below. Needs to start at 1!");
						continue;
					}
					customRows.put(rowNumber, m.group(2));
				} catch (NumberFormatException e){
					Logging.stackTrace(DWDS_CF.logger, e);
				}
			}
		}
		if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG)){
			customRows.put(DWDS_CF.config.getAsInt(ConfigType.CSV_AUTOTAG_COLUMN)-1, DWDS_CF.config.getAsString(ConfigType.CSV_AUTOTAG_COLUMN_NAME));
		}
		return customRows;
	}
}
