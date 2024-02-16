package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.FunctionUtils;
import com.therazzerapp.milorazlib.container.Pair;
import com.therazzerapp.milorazlib.excel.csv.CSVFile;
import com.therazzerapp.milorazlib.excel.csv.CSVLoader;
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

    private static Object fetchContent(DWDSRequestBuilder requestBuilder){
        List<String> content = HTTPUtils.getHTTPRequestContentList(requestBuilder.build(), getHTTPSettings(), DWDS_CF.logger);
        if (!content.isEmpty() && content.get(1).startsWith("<!DOCTYPE html>")){
            String wholeContent = String.join("\n", content);
            if (wholeContent.contains("Zugriff auf diese Seite erhalten Sie, wenn Sie im DWDS eingeloggt sind")){
                Logging.warning(DWDS_CF.logger, "(Fetcher) No access to corpus: \"" + requestBuilder.getCorpus() + "\". To session token!");
                return null;
            } else if (wholeContent.contains("timeout elapsed</pre>")){
                Logging.warning(DWDS_CF.logger, "(Fetcher) Can't connect to corpus: \"" + requestBuilder.getCorpus() + "\". Timeout!");
                return null;
            }
            Logging.warning(DWDS_CF.logger, "(Fetcher) Can't connect to corpus: \"" + requestBuilder.getCorpus() + "\". Reason unknown!");
            return null;
        }
        if (content.size() == 2 && content.get(1).startsWith("\"1\",\"\",\"\",\"\",\"\"")){
            Logging.detail(DWDS_CF.logger, "(Fetcher) No matches in corpus: \"" + requestBuilder.getCorpus() + "\".");
            return null;
        }

        if (requestBuilder.getView() == DWDSRequestBuilder.ViewType.CSV || requestBuilder.getView() == DWDSRequestBuilder.ViewType.TSV){
            return content;
        }
        return StringUtils.join(content, "\n");
    }

    public static CSVFile fetchCSV(DWDSRequestBuilder requestBuilder, char separator){
        List<String> content = FunctionUtils.doWithRetries(() -> {
            try{
                //noinspection unchecked
                return (List<String>) fetchContent(requestBuilder);
            } catch (ClassCastException ignored){
                return null;
            }
        }, DWDS_CF.config.getAsInt(ConfigType.FETCH_TRIES));
        if (content == null || content.size() <= 1){
            return null;
        }
        return CSVLoader.load(requestBuilder.build(), content, separator, '\"', StandardCharsets.UTF_8, DWDS_CF.logger);
    }

    public static JSONConfigSection fetchJSON(DWDSRequestBuilder requestBuilder){
        String content = FunctionUtils.doWithRetries(() -> {
            try{
                return (String) fetchContent(requestBuilder);
            } catch (ClassCastException ignored){
                return null;
            }
        }, DWDS_CF.config.getAsInt(ConfigType.FETCH_TRIES));
        return content == null ? null : JSONLoader.load(content, true, DWDS_CF.logger);
    }

    public static String fetchTCF(DWDSRequestBuilder requestBuilder){
        String content = FunctionUtils.doWithRetries(() -> {
            try{
                return (String) fetchContent(requestBuilder);
            } catch (ClassCastException ignored){
                return null;
            }
        }, DWDS_CF.config.getAsInt(ConfigType.FETCH_TRIES));
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
