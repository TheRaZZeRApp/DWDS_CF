package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.csv.CSVRow;
import com.therazzerapp.milorazlib.logger.Logging;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class CSVFormatter{

	private static final Map<String, Integer> HEADER_MAPPING = new TreeMap<>();

	static{
		HEADER_MAPPING.put("no.", 0);
		HEADER_MAPPING.put("date", 1);
		HEADER_MAPPING.put("genre", 2);
		HEADER_MAPPING.put("bible", 3);
		HEADER_MAPPING.put("contextbefore", 4);
		HEADER_MAPPING.put("hit", 5);
		HEADER_MAPPING.put("contextafter", 6);
	}

	public static List<CSVRow> transformCorpusCSVFiles(List<Trio<String[], String, List<CSVRow>>> rowsRaw){
		List<CSVRow> finalRows = new LinkedList<>();
		for (Trio<String[], String, List<CSVRow>> stringListPair : rowsRaw){
			String corpusInfo = "";
			if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_SHOW_CORPUS)){
				corpusInfo = ":" + stringListPair.getValue();
			}
			finalRows.addAll(convert(stringListPair.getThree(), corpusInfo, getMappingArray(stringListPair.getKey()), CSVFormatter::convertRow));
		}
		return finalRows;
	}

	private static List<CSVRow> convert(List<CSVRow> rows, String corpusInfo, int[] index, TriFunction<int[], String[], String, String[]> mapper){
		List<CSVRow> convertedRows = new LinkedList<>();
		int counter = 1;
		for (CSVRow csvRow : rows){
			String[] formattedRow = mapper.apply(index, csvRow.getRow(), corpusInfo);
			if (formattedRow == null){
				if (csvRow.getRawRow() != null && csvRow.getRawRow().equals("-->")){
					continue;
				}
				Logging.warning(DWDS_CF.logger, "Can't convert row: " + counter + " in corpus: " + corpusInfo + ". Skipped!");
				continue;
			}
			counter++;
			convertedRows.add(new CSVRow(
					csvRow.getCsvFile() == null ? "" : csvRow.getCsvFile().getAbsolutePath(),
					csvRow.getRowNumber(),
					csvRow.getRawRow(),
					formattedRow,
					csvRow.getLogger()
			));
		}
		return convertedRows;
	}

	private static int[] getMappingArray(String[] header){
		int[] index = new int[7];
		for (int i = 0; i < header.length; i++){
			if (HEADER_MAPPING.containsKey(header[i].toLowerCase())){
				index[HEADER_MAPPING.get(header[i].toLowerCase())] = i;
			}
		}
		return index;
	}

	private static @Nullable String[] convertRow(int[] index, String[] rawRow, String corpus){

		try{
			return new String[]{rawRow[index[0]].concat(corpus), rawRow[index[1]], rawRow[index[2]], rawRow[index[3]], rawRow[index[4]], rawRow[index[5]], rawRow[index[6]]};
		} catch (ArrayIndexOutOfBoundsException e){
			Logging.stackTrace(DWDS_CF.logger, e);
			return null;
		}
	}
}
