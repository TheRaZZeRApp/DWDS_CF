package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.excel.csv.CSVRow;
import com.therazzerapp.milorazlib.excel.csv.CSVWriter;
import com.therazzerapp.milorazlib.logger.Logging;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

	public static void formatCSVFile(DWDSRequestBuilder requestBuilder, List<CSVRow> lines, File export, String[] header, Map<Integer, String> customRows){
		CSVWriter csvWriter = new CSVWriter(requestBuilder.getView() == DWDSRequestBuilder.ViewType.CSV ? DWDS_CF.config.getAsString(ConfigType.CSV_SEPARATOR).charAt(0) : '\t', StandardCharsets.UTF_8, "\n", export, DWDS_CF.config.getAsBoolean(ConfigType.CSV_ESCAPE_HTML), DWDS_CF.config.getAsString(ConfigType.CSV_ESCAPE).charAt(0), DWDS_CF.logger);
		int blankLines = DWDS_CF.config.getAsInt(ConfigType.CSV_SPACES);
		csvWriter.addNextLine(mergeRows(header, customRows, true));
		addBlankLines(csvWriter, blankLines);
		for (CSVRow line : lines){
			csvWriter.addNextLine(mergeRows(line.getRow(), customRows, false));
			addBlankLines(csvWriter, blankLines);
		}
		csvWriter.export();
	}

	private static String[] mergeRows(String[] originalRow, Map<Integer, String> customRows, boolean header){
		String[] newRow = new String[originalRow.length + customRows.size()];
		for (Map.Entry<Integer, String> integerStringEntry : customRows.entrySet()){
			newRow[integerStringEntry.getKey()] = header ? integerStringEntry.getValue() : "";
		}
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
		Map<Integer, String> customRows = new TreeMap<>();
		for (String s : customRowsRaw.split("\n")){
			if (s == null || s.isBlank()){
				continue;
			}
			Matcher m = CUSTOM_ROW_PATTERN.matcher(s.trim());
			if (m.find()){
				try{
					int rowNumber = Integer.parseInt(m.group(1))-1;
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
		return customRows;
	}

	private static void addBlankLines(CSVWriter writer, int amount){
		for (int i = 0; i < amount; i++){
			writer.addEmptyLine();
		}
	}
}
