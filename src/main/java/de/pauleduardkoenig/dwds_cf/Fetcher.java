package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.FunctionUtils;
import com.therazzerapp.milorazlib.container.Pair;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVFile;
import com.therazzerapp.milorazlib.excel.concurrent.ConcurrentCSVLoader;
import com.therazzerapp.milorazlib.http.HTTPSettings;
import com.therazzerapp.milorazlib.http.HTTPUtils;
import com.therazzerapp.milorazlib.json.JSONConfigSection;
import com.therazzerapp.milorazlib.json.JSONLoader;
import com.therazzerapp.milorazlib.logger.Logging;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class Fetcher{

	private static HTTPSettings getHTTPSettings(){
		List<Pair<String, String>> props = new LinkedList<>();
		props.add(HTTPSettings.DEFAULT_AGENT);
		props.add(new Pair<>("Cookie", "dwds_session=" + DWDS_CF.config.getAsString(ConfigType.SESSION_TOKEN)));
		return new HTTPSettings(props, DWDS_CF.config.getAsInt(ConfigType.CONNECT_TIMEOUT), DWDS_CF.config.getAsInt(ConfigType.READ_TIMEOUT), StandardCharsets.UTF_8);
	}

	private static Pair<Object, Boolean> fetchContent(String url, DWDSRequestBuilder.ViewType viewType, String corpus){
		Map<Integer, String> content = HTTPUtils.getHTTPRequestContentConcurrentMap(url, getHTTPSettings(), DWDS_CF.logger);
		if (!content.isEmpty() && content.size() > 1 && (content.get(1).startsWith("<!DOCTYPE html>") || content.get(0).startsWith("<!DOCTYPE HTML PUBLIC"))){
			String wholeContent = String.join("\n", content.values());
			if (wholeContent.contains("Zugriff auf diese Seite erhalten Sie, wenn Sie im DWDS eingeloggt sind")){
				Logging.warning(DWDS_CF.logger, "(Fetcher) Can't access corpus : \"" + corpus + "\". No session token, removed from current fetch!");
				RequestCompiler.CORPORA_TO_SKIP.add(corpus);
				return new Pair<>(null, false);
			} else if (wholeContent.contains("timeout elapsed</pre>")){
				Logging.warning(DWDS_CF.logger, "(Fetcher) Can't connect to corpus: \"" + corpus + "\". Timeout!");
				return new Pair<>(null, RequestCompiler.compileThreadRunning);
			}
			Logging.warning(DWDS_CF.logger, "(Fetcher) Can't connect to corpus: \"" + corpus + "\". Reason unknown!");
			return new Pair<>(null, RequestCompiler.compileThreadRunning);
		}
		if (viewType == DWDSRequestBuilder.ViewType.JSON && content.get(0).equals("[{\"meta_\":{}}]")){
			Logging.detail(DWDS_CF.logger, "(Fetcher) No matches in corpus: \"" + corpus + "\".");
			return new Pair<>(null, RequestCompiler.compileThreadRunning);
		}
		if (viewType == DWDSRequestBuilder.ViewType.CSV || viewType == DWDSRequestBuilder.ViewType.TSV){
			if (content.size() >= 2 && (content.get(1).startsWith("\"1\",\"\",\"\",\"\"") || content.get(1).startsWith("1\t\t\t\t\t"))){
				Logging.detail(DWDS_CF.logger, "(Fetcher) No matches in corpus: \"" + corpus + "\".");
				return new Pair<>(null, RequestCompiler.compileThreadRunning);
			}
			return new Pair<>(content, RequestCompiler.compileThreadRunning);
		}
		if (viewType == DWDSRequestBuilder.ViewType.TCF && content.size() == 23){ //23 = no tokens/sentences in tcf file
			return new Pair<>(null, false);
		}
		return new Pair<>(StringUtils.join(content.values(), "\n"), RequestCompiler.compileThreadRunning);
	}

	public static ConcurrentCSVFile fetchCSV(String url, DWDSRequestBuilder.ViewType viewType, String corpus, char separator){
		Map<Integer, String> content = FunctionUtils.doWithRetriesAndBreak(() -> {
			try{
				Pair<Object, Boolean> val = fetchContent(url, viewType, corpus);
				//noinspection unchecked
				return new Pair<>((Map<Integer, String>) val.getKey(), val.getValue());
			} catch (ClassCastException ignored){
				return null;
			}
		}, DWDS_CF.config.getAsInt(ConfigType.FETCH_TRIES));
		if (content == null || content.size() <= 1){
			return null;
		}
		return ConcurrentCSVLoader.load(url, content, separator, '\"', DWDS_CF.logger);
	}

	private static String fetchString(String url, DWDSRequestBuilder.ViewType viewType, String corpus){
		return FunctionUtils.doWithRetriesAndBreak(() -> {
			try{
				Pair<Object, Boolean> val = fetchContent(url, viewType, corpus);
				return new Pair<>((String) val.getKey(), val.getValue());
			} catch (ClassCastException ignored){
				return null;
			}
		}, DWDS_CF.config.getAsInt(ConfigType.FETCH_TRIES));
	}

	public static JSONConfigSection fetchJSON(String url, DWDSRequestBuilder.ViewType viewType, String corpus){
		String content = fetchString(url, viewType, corpus);
		return content == null ? null : JSONLoader.load(content, true, DWDS_CF.logger);
	}

	public static String fetchTCF(String url, DWDSRequestBuilder.ViewType viewType, String corpus){
		String content = fetchString(url, viewType, corpus);
		if (content == null){
			return null;
		}
		try{
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setIndentSize(DWDS_CF.config.getAsInt(ConfigType.TCF_INDENT_LEVEL));
			format.setSuppressDeclaration(false);
			format.setIndent(DWDS_CF.config.getAsString(ConfigType.TCF_INDENT));
			format.setEncoding("UTF-8");

			org.dom4j.Document document = DocumentHelper.parseText(content);
			StringWriter sw = new StringWriter();
			XMLWriter writer = new XMLWriter(sw, format);
			writer.write(document);
			return sw.toString();
		} catch (Exception e){
			Logging.stackTrace(DWDS_CF.logger, e);
		}
		return content;
	}
}
