package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.csv.CSVRow;
import com.therazzerapp.milorazlib.logger.Logging;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class CSVFormatter {
    public static List<CSVRow> transformCorpusCSVFiles(List<Trio<String[], String, List<CSVRow>>> rowsRaw) {

        List<CSVRow> finalRows = new LinkedList<>();
        for (Trio<String[], String, List<CSVRow>> stringListPair : rowsRaw) {
            String corpusInfo = "";
            if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_SHOW_CORPUS)) {
                corpusInfo = ":" + stringListPair.getValue();
            }
            finalRows.addAll(convert(stringListPair.getThree(), corpusInfo, getMappingArray(stringListPair.getKey()), CSVFormatter::convertRow));
        }
        return finalRows;
    }

    private static List<CSVRow> convert(List<CSVRow> rows, String corpusInfo, int[] index, TriFunction<int[], String[], String, String[]> mapper) {
        List<CSVRow> convertedRows = new LinkedList<>();
        int counter = 1;
        for (CSVRow csvRow : rows) {
            String[] formattedRow = mapper.apply(index, csvRow.getRow(), corpusInfo);
            if (formattedRow == null) {
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

    //"No.","Date","Genre","Bibl","ContextBefore","Hit","ContextAfter"
    private static int[] getMappingArray(String[] header) {
        int i = 0;
        int[] index = new int[7];
        for (String s : header) {
            if (s.equalsIgnoreCase("No.")) {
                index[0] = i;
            } else if (s.equalsIgnoreCase("Date")) {
                index[1] = i;
            } else if (s.equalsIgnoreCase("Genre")) {
                index[2] = i;
            } else if (s.equalsIgnoreCase("Bibl")) {
                index[3] = i;
            } else if (s.equalsIgnoreCase("ContextBefore")) {
                index[4] = i;
            } else if (s.equalsIgnoreCase("Hit")) {
                index[5] = i;
            } else if (s.equalsIgnoreCase("ContextAfter")) {
                index[6] = i;
            }
            i++;

        }
        return index;
    }

    private static @Nullable String[] convertRow(int[] index, String[] rawRow, String corpus) {
        try {
            return new String[]{rawRow[index[0]].concat(corpus), rawRow[index[1]], rawRow[index[2]], rawRow[index[3]], rawRow[index[4]], rawRow[index[5]], rawRow[index[6]]};
        } catch (ArrayIndexOutOfBoundsException e) {
            Logging.stackTrace(DWDS_CF.logger, e);
            return null;
        }
    }
}
