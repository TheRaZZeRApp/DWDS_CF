package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.excel.csv.CSVFile;
import com.therazzerapp.milorazlib.excel.csv.CSVLoader;
import com.therazzerapp.milorazlib.excel.csv.CSVRow;
import com.therazzerapp.milorazlib.excel.csv.CSVWriter;
import com.therazzerapp.milorazlib.logger.Logging;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.de.GermanTagger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.3.5
 */
public class Tagger{

	private static final Map<String, String> tags = new ConcurrentHashMap<>();
	private static int source = -1;
	private static int destination = -1;
	private static int max = -1;
	public static final Set<String> missing = ConcurrentHashMap.newKeySet();

	private static final Map<String, String> lemmas = new ConcurrentHashMap<>();

	public static synchronized void load(){
		missing.clear();
		tags.clear();
		CSVFile taglist = CSVLoader.load(new File(DWDS_CF.config.getAsString(ConfigType.CSV_AUTOTAG_LIST)), ';', '\"', StandardCharsets.UTF_8, DWDS_CF.logger);
		for (CSVRow line : taglist.getLines()){
			tags.put(line.getCell(0), line.getCell(1).toUpperCase());
		}
		source = (DWDS_CF.config.getAsInt(ConfigType.CSV_AUTOTAG_SOURCE_COLUMN)-1);
		destination = (DWDS_CF.config.getAsInt(ConfigType.CSV_AUTOTAG_COLUMN)-1);
		max = Math.max(source, destination);
	}

	public static String[] tagRow(String[] originalRow){
		if (tags.isEmpty() || originalRow == null || originalRow.length == 0 || originalRow.length < max){
			return originalRow;
		}
		String[] taggedRow = Arrays.copyOf(originalRow, originalRow.length);
		String token = taggedRow[source].toLowerCase();
		if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG_LEMMATIZE)){
			if (tags.containsKey(token)){
				taggedRow[destination] = tags.get(token);
			}
			String lemma = lemmas.getOrDefault(token, null);
			if (lemma == null){
				try{
					AnalyzedTokenReadings analyzedTokens = new GermanTagger().lookup(token);
					if (analyzedTokens != null && !analyzedTokens.getReadings().isEmpty()){
						lemma = analyzedTokens.getAnalyzedToken(0).getLemma().toLowerCase();
					} else {
						lemma = token;
					}
					lemmas.put(token, lemma);
				} catch (IOException e){
					lemma = token;
					Logging.stackTrace(DWDS_CF.logger, e);
				}
			}
			if (!tags.containsKey(lemma)){
				missing.add(lemma);
			}
			taggedRow[destination] = tags.getOrDefault(lemma, "");
		} else {
			taggedRow[destination] = tags.getOrDefault(taggedRow[source], "");
		}

		return taggedRow;
	}

	public static void tagCSVFile(File input, File output, char separator, char encloser, Charset charset){
		CSVFile inputCSVFile = CSVLoader.load(input, separator, encloser, charset, DWDS_CF.logger);
		CSVWriter csvWriter = new CSVWriter(separator, charset, "\n", output, true, encloser, DWDS_CF.logger);
		csvWriter.addNextLine(inputCSVFile.getHeader());
		for (CSVRow line : inputCSVFile.getLines()){
			csvWriter.addNextLine(tagRow(line.getRow()));
		}
		csvWriter.export();
	}
}
