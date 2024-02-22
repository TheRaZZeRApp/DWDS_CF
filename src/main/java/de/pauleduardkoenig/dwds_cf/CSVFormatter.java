package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVRow;
import com.therazzerapp.milorazlib.logger.Logging;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class CSVFormatter{

	public static final Map<String, Integer> HEADER_MAPPING = new TreeMap<>();

	static{
		HEADER_MAPPING.put("no.", 0);
		HEADER_MAPPING.put("date", 1);
		HEADER_MAPPING.put("genre", 2);
		HEADER_MAPPING.put("bible", 3);
		HEADER_MAPPING.put("contextbefore", 4);
		HEADER_MAPPING.put("hit", 5);
		HEADER_MAPPING.put("contextafter", 6);
	}

	public static Queue<ConcurrentCSVRow> transformCorpusCSVFiles(Queue<Trio<String[], String, Queue<ConcurrentCSVRow>>> rowsRaw){
		Queue<ConcurrentCSVRow> finalRows = new ConcurrentLinkedQueue<>();
		for (Trio<String[], String, Queue<ConcurrentCSVRow>> stringListPair : rowsRaw){
			String corpusInfo = DWDS_CF.config.getAsBoolean(ConfigType.CSV_SHOW_CORPUS) ? (":" + stringListPair.getValue()) : "";
			finalRows.addAll(convert(stringListPair.getThree(), corpusInfo, getMappingArray(stringListPair.getKey()), CSVFormatter::convertRow));
		}
		return finalRows;
	}

	private static Queue<ConcurrentCSVRow> convert(Queue<ConcurrentCSVRow> rows, String corpus, int[] index, TriFunction<int[], String[], String, String[]> mapper){
		Queue<ConcurrentCSVRow> convertedRows = new ConcurrentLinkedQueue<>();
		int counter = 1;
		for (ConcurrentCSVRow csvRow : rows){
			String[] formattedRow = mapper.apply(index, csvRow.getRow(), corpus);
			if (formattedRow == null){
				if (csvRow.getRawRow() != null && csvRow.getRawRow().equals("-->")){
					continue;
				}
				Logging.warning(DWDS_CF.logger, "Can't convert row: " + counter + " in corpus: " + corpus + ". Skipped!");
				continue;
			}
			counter++;
			convertedRows.add(new ConcurrentCSVRow(
					csvRow.getOrigin(),
					csvRow.getRowNumber(),
					csvRow.getRawRow(),
					formattedRow,
					csvRow.getLogger()
			));
		}
		return convertedRows;
	}

	private static int[] getMappingArray(String[] h){
		final String[] header = Arrays.copyOf(h, h.length);
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
