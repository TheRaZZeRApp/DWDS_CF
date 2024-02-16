package de.pauleduardkoenig.dwds_cf;

import com.google.gson.JsonElement;
import com.therazzerapp.milorazlib.DateUtils;
import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.csv.CSVFile;
import com.therazzerapp.milorazlib.excel.csv.CSVRow;
import com.therazzerapp.milorazlib.files.FileUtils;
import com.therazzerapp.milorazlib.json.JSONConfigSection;
import com.therazzerapp.milorazlib.json.JSONSaver;
import com.therazzerapp.milorazlib.logger.Logging;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.0.0
 */

public class RequestCompiler{

    public static boolean compileThreadRunning = true;

    public static boolean compile(DWDSRequestBuilder requestBuilder, String wordlist, Set<String> corpora, boolean combineCorpora, String path, String customRows){
        Logging.log(DWDS_CF.logger, "(Fetcher) Starting new DWDS fetch in " + corpora.size() + " corpora.");

        int counterWord = 0;
        int validCounter = 0;
        String dcc = requestBuilder.getDdc();
        boolean multiwordrequest = dcc.contains("%WORD%");
        Set<String> wordList = new LinkedHashSet<>(multiwordrequest ? Arrays.stream(wordlist.split("\n")).map(String::trim).toList() : Set.of("dcc"));
        Logging.log(DWDS_CF.logger, multiwordrequest ? ("(Fetcher) Start fetch for " + wordList.size() + " words" + (wordList.size() != 1 ? "!" : "")) : "(Fetcher) Fetching with DCC \"" + dcc + "\"");
        for (String word : wordList){
            if (!compileThreadRunning){
                break;
            }
            counterWord++;
            if (word.isBlank()){
                Logging.warning(DWDS_CF.logger, "(Fetcher) Can't fetch word " + counterWord + "/" + wordList.size() + ". Empty string!");
                continue;
            }

            List<Trio<String[], String, List<CSVRow>>> entriesCombinedCSV = new LinkedList<>();
            List<JsonElement> entriesCombinedJSON = new LinkedList<>();

            if (multiwordrequest){
                Logging.log(DWDS_CF.logger, "(Fetcher) Fetching word " + counterWord + "/" + wordList.size() + " (" + word + ")");
                requestBuilder.setDdc(dcc.trim().replace("%WORD%", word));
            } else {
                requestBuilder.setDdc(dcc.trim());
            }

            int counterCorpus = 0;
            for (String corpus : corpora){
                if (!compileThreadRunning){
                    break;
                }
                Logging.detail(DWDS_CF.logger, "(Fetcher) Fetching corpora " + (counterCorpus++) + "/" + corpora.size() + " (" + corpus + ")");
                requestBuilder.setCorpus(corpus);
                switch (requestBuilder.getView()){
                    case CSV, TSV ->
                            exportCSVTSV(requestBuilder, path, corpus, word, entriesCombinedCSV, combineCorpora, customRows);
                    case JSON -> exportJSON(requestBuilder, path, corpus, word, entriesCombinedJSON, combineCorpora);
                    case TCF -> exportTCF(requestBuilder, path, corpus, word);
                }
            }

            if (combineCorpora){

                switch (requestBuilder.getView()){
                    case CSV, TSV ->
                            CSVConverter.formatCSVFile(CSVFormatter.transformCorpusCSVFiles(entriesCombinedCSV), new File(path + "dwds_combined_" + Utils.escapeWordToPath(word) + "_" + DateUtils.getCurrentDateTimePath() + ".csv"), Constants.CSV_KWIC_HEADER.toArray(new String[0]), CSVConverter.compileCustomRows(customRows));
                    case JSON ->
                            JSONSaver.save(new JSONConfigSection(entriesCombinedJSON), new File(path + "dwds_combined_" + Utils.escapeWordToPath(word) + "_" + DateUtils.getCurrentDateTimePath() + ".json"), StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.JSON_COLLAPSE_LEVEL), DWDS_CF.logger);
                }
            }
            validCounter++;
        }
        if (compileThreadRunning){
            Logging.log(DWDS_CF.logger, multiwordrequest ? "(Fetcher) Successfully fetched " + validCounter + "/" + wordList.size() + " word" + (wordList.size() != 1 ? "s!" : "!") : "(Fetcher) Successfully fetched!");
            return true;
        }
        return false;
    }

    private static void exportCSVTSV(DWDSRequestBuilder requestBuilder, String path, String corpus, String word, List<Trio<String[], String, List<CSVRow>>> masterRows, boolean combineCorpora, String customRows){
        CSVFile csvFile = requestBuilder.getView() == DWDSRequestBuilder.ViewType.TSV ? Fetcher.fetchCSV(requestBuilder, '\t') : Fetcher.fetchCSV(requestBuilder, ',');
        if (csvFile == null){
            return;
        }
        if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_EXPORT_COMBINE_CORPORA) && combineCorpora){
            if (csvFile.getLines().length != 0 && !csvFile.getLine(0).getCell(1).isBlank()){
                masterRows.add(new Trio<>(csvFile.getHeader(), corpus, new LinkedList<>(Arrays.stream(csvFile.getLines()).toList())));
            }
        } else {
            CSVConverter.formatCSVFile(new LinkedList<>(Arrays.stream(csvFile.getLines()).toList()), new File(path + "dwds_" + corpus + "_" + Utils.escapeWordToPath(word) + "_" + DateUtils.getCurrentDateTimePath() + ".csv"), csvFile.getHeader(), CSVConverter.compileCustomRows(customRows));
        }
    }

    private static void exportJSON(DWDSRequestBuilder requestBuilder, String path, String corpus, String word, List<JsonElement> jsonElements, boolean combineCorpora){
        JSONConfigSection jsonConfigSection = Fetcher.fetchJSON(requestBuilder);
        if (jsonConfigSection == null){
            return;
        }
        if (!combineCorpora){
            JSONSaver.save(jsonConfigSection, new File(path + "dwds_" + corpus + "_" + Utils.escapeWordToPath(word) + "_" + DateUtils.getCurrentDateTimePath() + ".json"), StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.JSON_COLLAPSE_LEVEL), DWDS_CF.logger);
        } else {
            for (int i = 0; i < jsonConfigSection.getObject().size(); i++){
                if (!compileThreadRunning){
                    break;
                }
                jsonElements.add(jsonConfigSection.getObject().get(String.valueOf(i)));
            }
        }
    }

    private static void exportTCF(DWDSRequestBuilder requestBuilder, String path, String corpus, String word){
        String tcf = Fetcher.fetchTCF(requestBuilder);
        if (tcf == null){
            return;
        }
        FileUtils.exportFile(tcf, new File(path + "dwds_" + corpus + "_" + Utils.escapeWordToPath(word) + "_" + DateUtils.getCurrentDateTimePath() + ".tcf"), StandardCharsets.UTF_8, DWDS_CF.logger);
    }
}
