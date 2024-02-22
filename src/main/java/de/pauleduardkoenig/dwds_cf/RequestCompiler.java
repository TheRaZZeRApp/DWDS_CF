package de.pauleduardkoenig.dwds_cf;

import com.google.gson.JsonElement;
import com.therazzerapp.milorazlib.CollectionUtils;
import com.therazzerapp.milorazlib.DateUtils;
import com.therazzerapp.milorazlib.container.Trio;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVFile;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVRow;
import com.therazzerapp.milorazlib.files.FileUtils;
import com.therazzerapp.milorazlib.json.JSONConfigSection;
import com.therazzerapp.milorazlib.json.JSONSaver;
import com.therazzerapp.milorazlib.logger.Logging;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.0.0
 */
public class RequestCompiler{

	protected static final Set<String> CORPORA_TO_SKIP = ConcurrentHashMap.newKeySet();
	public static volatile boolean compileThreadRunning = true;

	public static boolean compile(DWDSRequestBuilder requestBuilder, final String wordlist, Set<String> corpora, final boolean combineCorpora, final String path, final String customRows){
		Logging.log(DWDS_CF.logger, "(Fetcher) Starting new DWDS fetch in " + corpora.size() + " corpora.");
		CORPORA_TO_SKIP.clear();
		int counterWord = 0;
		int validCounter = 0;
		String dcc = requestBuilder.getDdc();
		boolean multiwordrequest = dcc.contains("%WORD%");
		Set<String> wordList = new LinkedHashSet<>(multiwordrequest ? Arrays.stream(wordlist.split("\n")).map(String::trim).toList() : Set.of("dcc"));
		Logging.log(DWDS_CF.logger, multiwordrequest ? ("(Fetcher) Start fetch for " + wordList.size() + " words" + (wordList.size() != 1 ? "!" : "")) : "(Fetcher) Fetching with DCC \"" + dcc + "\"");
		final int threads = DWDS_CF.config.getAsInt(ConfigType.THREADS);
		final DWDSRequestBuilder.ViewType viewType = requestBuilder.getView();

		if ((viewType == DWDSRequestBuilder.ViewType.CSV || viewType == DWDSRequestBuilder.ViewType.TSV) && DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG)){
			Tagger.load();
		}

		for (String word : wordList){
			if (!compileThreadRunning){
				break;
			}
			counterWord++;
			if (word.isBlank()){
				Logging.warning(DWDS_CF.logger, "(Fetcher) Can't fetch word " + counterWord + "/" + wordList.size() + ". Empty string!");
				continue;
			}

			Queue<Trio<String[], String, Queue<ConcurrentCSVRow>>> entriesCombinedCSV = new ConcurrentLinkedQueue<>();
			Queue<JsonElement> entriesCombinedJSON = new ConcurrentLinkedQueue<>();

			if (multiwordrequest){
				Logging.log(DWDS_CF.logger, "(Fetcher) Fetching word " + counterWord + "/" + wordList.size() + " (" + word + ")");
				requestBuilder.setDdc(dcc.trim().replace("%WORD%", word));
			} else {
				requestBuilder.setDdc(dcc.trim());
			}

			AtomicInteger counterCorpus = new AtomicInteger(0);
			requestBuilder.setCorpus("%CORPUS%");
			final String request = requestBuilder.build();
			ForkJoinPool forkJoinPool = null;
			try{
				forkJoinPool = new ForkJoinPool(threads);
				forkJoinPool.submit(() ->
						// Parallel task here, for example
						corpora.parallelStream().takeWhile(i -> compileThreadRunning).filter(Predicate.not(CORPORA_TO_SKIP::contains)).forEach(corpus -> {
							Logging.detail(DWDS_CF.logger, "(Fetcher) Fetching corpora " + (counterCorpus.incrementAndGet()) + "/" + corpora.size() + " (" + corpus + ")");
							String requestTemp = request.replace("%CORPUS%", corpus);
							switch (viewType){
								case CSV, TSV ->
										exportCSVTSV(requestTemp, viewType, corpus, compileFilename(path, corpus, word, viewType), entriesCombinedCSV, combineCorpora, customRows);
								case JSON ->
										exportJSON(requestTemp, viewType, corpus, compileFilename(path, corpus, word, DWDSRequestBuilder.ViewType.JSON), entriesCombinedJSON, combineCorpora);
								case TCF ->
										exportTCF(requestTemp, viewType, corpus, compileFilename(path, corpus, word, DWDSRequestBuilder.ViewType.TCF));
							}
						})
				).get();
			} catch (InterruptedException | ExecutionException e){
				Logging.stackTrace(DWDS_CF.logger, e);
			} finally {
				if (forkJoinPool != null){
					forkJoinPool.shutdown();
				}
			}

			if (combineCorpora){
				switch (requestBuilder.getView()){
					case CSV, TSV ->
							CSVConverter.formatCSVFile(viewType, CSVFormatter.transformCorpusCSVFiles(entriesCombinedCSV), compileFilename(path, "combined", word, requestBuilder.getView()), Constants.CSV_KWIC_HEADER.toArray(new String[0]), CSVConverter.compileCustomRows(customRows));
					case JSON ->
							JSONSaver.save(new JSONConfigSection(entriesCombinedJSON), compileFilename(path, "combined", word, DWDSRequestBuilder.ViewType.JSON), StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.JSON_COLLAPSE_LEVEL), DWDS_CF.logger);
				}
			}
			validCounter++;
		}
		if (compileThreadRunning){
			if ((viewType == DWDSRequestBuilder.ViewType.CSV || viewType == DWDSRequestBuilder.ViewType.TSV) && DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG) && DWDS_CF.config.getAsBoolean(ConfigType.CSV_AUTOTAG_LEMMATIZE)){
				List<String> missingLemmas = new LinkedList<>(Tagger.missing);
				missingLemmas.sort(String::compareTo);
				FileUtils.exportFile(CollectionUtils.listToString(missingLemmas, "\n"), new File(path + "missing_lemmas_" + DateUtils.getCurrentDateTimePath() + ".txt"), StandardCharsets.UTF_8, DWDS_CF.logger);
			}
			Logging.log(DWDS_CF.logger, multiwordrequest ? "(Fetcher) Successfully fetched " + validCounter + "/" + wordList.size() + " word" + (wordList.size() != 1 ? "s!" : "!") : "(Fetcher) Successfully fetched!");
			return true;
		}
		return false;
	}

	private static void exportCSVTSV(String url, DWDSRequestBuilder.ViewType viewType, String corpus, File export, Queue<Trio<String[], String, Queue<ConcurrentCSVRow>>> masterRows, boolean combineCorpora, String customRows){
		ConcurrentCSVFile csvFile = viewType == DWDSRequestBuilder.ViewType.TSV ? Fetcher.fetchCSV(url, viewType, corpus, '\t') : Fetcher.fetchCSV(url, viewType, corpus, ',');

		if (csvFile == null){
			return;
		}
		if (DWDS_CF.config.getAsBoolean(ConfigType.CSV_EXPORT_COMBINE_CORPORA) && combineCorpora){
			if (csvFile.getLines().length != 0 && !csvFile.isEmpty()){
				masterRows.add(new Trio<>(csvFile.getHeader(), corpus, new ConcurrentLinkedQueue<>(Arrays.asList(csvFile.getLines()))));
			}
		} else {
			CSVConverter.formatCSVFile(viewType, new ConcurrentLinkedQueue<>(Arrays.stream(csvFile.getLines()).toList()), export, csvFile.getHeader(), CSVConverter.compileCustomRows(customRows));
		}
	}

	private static void exportJSON(String url, DWDSRequestBuilder.ViewType viewType, String corpus, File export, Queue<JsonElement> jsonElements, boolean combineCorpora){
		JSONConfigSection jsonConfigSection = Fetcher.fetchJSON(url, viewType, corpus);
		if (jsonConfigSection == null){
			return;
		}
		if (!combineCorpora){
			JSONSaver.save(jsonConfigSection, export, StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.JSON_COLLAPSE_LEVEL), DWDS_CF.logger);
		} else {
			IntStream.range(0, jsonConfigSection.getObject().size()).takeWhile(i -> compileThreadRunning).forEach(i -> jsonElements.add(jsonConfigSection.getObject().get(String.valueOf(i))));
		}
	}

	private static void exportTCF(String url, DWDSRequestBuilder.ViewType viewType, String corpus, File export){
		String tcf = Fetcher.fetchTCF(url, viewType, corpus);
		if (tcf == null){
			return;
		}
		FileUtils.exportFile(tcf, export, StandardCharsets.UTF_8, DWDS_CF.logger);
	}

	private static File compileFilename(String path, String corpus, String word, DWDSRequestBuilder.ViewType viewType){
		return new File(path + "dwds_" + Utils.escapeWordToPath(word) + "_" + corpus + "_" + DateUtils.getCurrentDateTimePath() + viewType.getExtension());
	}
}
