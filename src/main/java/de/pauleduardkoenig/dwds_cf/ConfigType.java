package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.config.ConfigEnum;
import org.jetbrains.annotations.NotNull;

import java.time.Year;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public enum ConfigType implements ConfigEnum<ConfigType>{
    VERSION(Constants.VERSION),
    SESSION_TOKEN(""),
    LOG_LEVEL("1"),
    FETCH_TRIES("3"),
    CONNECT_TIMEOUT("5000"),
    READ_TIMEOUT("20000"),
    WORDLIST("./data/words.txt"),
    REQ_DCC("$p=ADJA %WORD%"),
    REQ_VIEW("CSV"),
    REQ_SORT("RANDOM"),
    REQ_FORMAT("KWIC"),
    REQ_LIMIT("50"),
    REQ_DATE_START("1900"),
    REQ_DATE_END(Year.now().toString()),
    REQ_PAGE("1"),
    REQ_PAGE_ENABLED(false),
    REQ_YEAR_START_ENABLED(false),
    REQ_YEAR_END_ENABLED(false),
    REQ_SORT_ENABLED(false),
    REQ_LIMIT_ENABLED(false),
    JSON_COLLAPSE_LEVEL("3"),
    JSON_COMBINE(true),
    TCF_INDENT_LEVEL("2"),
    TCF_INDENT(" "),
    CSV_SPACES("0"),
    CSV_EXPORT_COMBINE_CORPORA(true),
    CSV_AUTOTAG(true),
    CSV_SEPARATOR(";"),
    CSV_ESCAPE_HTML(false),
    CSV_ESCAPE("\""),
    CSV_CUSTOM_ROWS("./data/custom_rows.txt"),
    CSV_SHOW_CORPUS(true),
    CORPORA_DTA(false),
    CORPORA_DTAXL(false),
    CORPORA_PUBLIC(false),
    CORPORA_BZ(false),
    CORPORA_TSP(false),
    CORPORA_BLOGS(false),
    CORPORA_DTAE(false),
    CORPORA_ADG(false),
    CORPORA_DINGLER(false),
    CORPORA_UNTERTITEL(false),
    CORPORA_SPK(false),
    CORPORA_DDR(false),
    CORPORA_POLITISCHEREDEN(false),
    CORPORA_BUNDESTAG(false),
    CORPORA_SOLDATENBRIEFE(false),
    CORPORA_COPADOCS(false),
    CORPORA_AVHBERN(false),
    CORPORA_BRUEDERGEMEINE(false),
    CORPORA_PITAVAL(false),
    CORPORA_JEANPAUL(false),
    CORPORA_DEKUDE(false),
    CORPORA_NSCHATZDEU(false),
    CORPORA_STIMMLOS(false),
    CORPORA_WIKIBOOKS(false),
    CORPORA_WIKIPEDIA(false),
    CORPORA_WIKIVOYAGE(false),
    CORPORA_GESETZE(false),
    CORPORA_KERN(true),
    CORPORA_DTAK(false),
    CORPORA_CPREGIONAL(false),
    CORPORA_CPWEBXL(false),
    CORPORA_CPBZ_PP(false),
    CORPORA_CPFAZ(false),
    CORPORA_CPND(false),
    CORPORA_CPZEIT(false),
    CORPORA_CPWEB(false),
    CORPORA_CPWEBMONITOR(false),
    CORPORA_CPBALLSPORT(false),
    CORPORA_CPJURA(false),
    CORPORA_CPMEDIZIN(false),
    CORPORA_CPCORONA(false),
    CORPORA_CPMODEBLOGS(false),
    CORPORA_CPIT_BLOGS(false),
    CORPORA_CPIBK_DCHAT(false),
    CORPORA_CPTEXTBERG(false),
    CORPORA_CPWENDE(false),
    CORPORA_KORPUS21(false);

    private final String def;

    ConfigType(String def){
        this.def = def;
    }

    ConfigType(boolean def){
        this.def = def ? "true" : "false";
    }

    @Override
    public @NotNull String getDefaultValue(){
        return def;
    }
}
