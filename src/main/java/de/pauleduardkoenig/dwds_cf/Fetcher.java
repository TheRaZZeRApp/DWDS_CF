package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.excel.csv.CSVFile;
import com.therazzerapp.milorazlib.http.HTTPUtils;
import com.therazzerapp.milorazlib.json.JSONConfigSection;
import com.therazzerapp.milorazlib.logger.Logging;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class Fetcher {


    public static CSVFile fetchCSV(DWDSRequestBuilder requestBuilder) {
        return HTTPUtils.getHTTPRequestAsCSV(requestBuilder.setView(DWDSRequestBuilder.ViewType.CSV).build(), ",", "\"", StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.CONNECT_TIMEOUT), DWDS_CF.config.getAsInt(ConfigType.READ_TIMEOUT), DWDS_CF.logger);
    }

    public static CSVFile fetchTSV(DWDSRequestBuilder requestBuilder) {
        return HTTPUtils.getHTTPRequestAsCSV(requestBuilder.setView(DWDSRequestBuilder.ViewType.TSV).build(), "\t", "\"", StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.CONNECT_TIMEOUT), DWDS_CF.config.getAsInt(ConfigType.READ_TIMEOUT), DWDS_CF.logger);
    }

    public static JSONConfigSection fetchJSON(DWDSRequestBuilder requestBuilder) {
        return HTTPUtils.getRequestAsJSON(requestBuilder.setView(DWDSRequestBuilder.ViewType.JSON).build(), true, StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.CONNECT_TIMEOUT), DWDS_CF.config.getAsInt(ConfigType.READ_TIMEOUT), DWDS_CF.logger);
    }

    public static String fetchTCF(DWDSRequestBuilder requestBuilder) {
        String rawXML = HTTPUtils.getHTTPRequestContent(requestBuilder.setView(DWDSRequestBuilder.ViewType.TCF).build(), StandardCharsets.UTF_8, DWDS_CF.config.getAsInt(ConfigType.CONNECT_TIMEOUT), DWDS_CF.config.getAsInt(ConfigType.READ_TIMEOUT), DWDS_CF.logger);
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndentSize(DWDS_CF.config.getAsInt(ConfigType.TCF_INDENT_LEVEL));
            format.setSuppressDeclaration(false);
            format.setIndent(DWDS_CF.config.getAsString(ConfigType.TCF_INDENT));
            format.setEncoding("UTF-8");

            org.dom4j.Document document = DocumentHelper.parseText(rawXML);
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            return sw.toString();
        } catch (Exception e) {
            Logging.stackTrace(DWDS_CF.logger, e);
        }
        return rawXML;
    }
}
