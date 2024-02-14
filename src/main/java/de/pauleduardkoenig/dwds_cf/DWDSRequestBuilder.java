package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.basictypes.NameTypeEnum;
import com.therazzerapp.milorazlib.logger.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 0.1.0
 */
public class DWDSRequestBuilder {

    private String ddc;
    private String corpus;
    private int dateStart = -1;
    private int dateEnd = -1;
    private Set<String> genre;
    private FormatType format;
    private SortType sort;
    private int limit = 50;
    private int page = -1;
    private ViewType view = ViewType.CSV;

    public DWDSRequestBuilder() {
        // empty
    }

    public String getDdc() {
        return ddc;
    }

    public DWDSRequestBuilder setDdc(String ddc) {
        this.ddc = ddc.replace(" ", "%20")
                .replace("$", "%24")
                .replace("=", "%3D")
                .replace("@", "%40")
                .replace("-", "%2D")
                .replace("#", "%23")
                .replace(",", "%2C")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace("+", "%2B")
                .replace("\\", "%5C")
                .replace("|", "%7C")
                .replace(";", "%3B")
                .replace(":", "%3A")
                .replace(".", "%2E")
                .replace("*", "%2A");
        return this;
    }

    public ViewType getView() {
        return view;
    }

    public DWDSRequestBuilder setView(ViewType view) {
        this.view = view;
        return this;
    }

    public DWDSRequestBuilder setCorpus(String corpus) {
        this.corpus = corpus;
        return this;
    }

    public DWDSRequestBuilder setDateStart(int dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public DWDSRequestBuilder setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public DWDSRequestBuilder setGenre(@Nullable Set<String> genre) {
        this.genre = genre == null ? new HashSet<>() : genre;
        return this;
    }

    public DWDSRequestBuilder setGenre(String genre) {
        this.genre = new HashSet<>();
        this.genre.add(genre);
        return this;
    }

    public DWDSRequestBuilder addGenre(String genre) {
        if (this.genre == null) {
            this.genre = new HashSet<>();
        }
        this.genre.add(genre);
        return this;
    }

    public DWDSRequestBuilder setFormat(FormatType format) {
        this.format = format;
        return this;
    }

    public DWDSRequestBuilder setSort(SortType sort) {
        this.sort = sort;
        return this;
    }

    public DWDSRequestBuilder setPage(int page) {
        if (page < 1) {
            Logging.error(DWDS_CF.logger, "Min. page is 1!");
            this.page = 1;
        } else {
            this.page = page;
        }
        return this;
    }

    public DWDSRequestBuilder setLimit(int limit) {
        if (limit > 5000) {
            Logging.error(DWDS_CF.logger, "Max. limit is 5000!");
            this.limit = 5000;
        } else if (limit < 1) {
            Logging.error(DWDS_CF.logger, "Min. limit is 1!");
            this.limit = 1;
        } else {
            this.limit = limit;
        }
        return this;
    }

    public @NotNull String build() {
        if (ddc == null || ddc.isBlank()) {
            Logging.error(DWDS_CF.logger, "Can't create request without DDC!");
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://www.dwds.de/r/?q=");
        stringBuilder.append(ddc);
        if (corpus != null && !corpus.isBlank()) {
            stringBuilder.append("&corpus=");
            stringBuilder.append(corpus);
        }
        if (dateStart != -1) {
            stringBuilder.append("&date-start=");
            stringBuilder.append(dateStart);
        }
        if (dateEnd != -1) {
            stringBuilder.append("&date-end=");
            stringBuilder.append(dateEnd);
        }
        if (genre != null) {
            for (String g : genre) {
                stringBuilder.append("&genre=");
                stringBuilder.append(g);
            }
        }
        if (format != null) {
            stringBuilder.append("&format=");
            stringBuilder.append(format.toString().toLowerCase());
        }
        if (sort != null) {
            stringBuilder.append("&sort=");
            stringBuilder.append(sort.toString().toLowerCase());
        }
        stringBuilder.append("&limit=");
        stringBuilder.append(limit);
        if (page != -1) {
            stringBuilder.append("&page=");
            stringBuilder.append(page);
        }
        stringBuilder.append("&view=");
        stringBuilder.append(view.toString().toLowerCase());
        return stringBuilder.toString();
    }

    public enum FormatType implements NameTypeEnum<FormatType> {
        KWIC("KWIC (keyword in context)"),
        FULL("FULL (whole sentence)"),
        MAX("MAX (full sentence + 1 sentence of context)");

        private final String name;

        FormatType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ViewType implements NameTypeEnum<ViewType> {
        CSV("CSV (Comma-Separated Values)"),
        JSON("JSON (JavaScript Object Notation)"),
        TCF("TCF (Text Corpus Format)"),
        TSV("TSV (Tab-Separated Values)");

        private final String name;

        ViewType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public enum SortType implements NameTypeEnum<SortType> {
        DATE_ASC("Date Ascending"),
        DATE_DESC("Date Descending"),
        LEFT_ASC("Left Ascending"),
        LEFT_DESC("Left Descending"),
        MID_ASC("Hit Ascending"),
        MID_DESC("Hit Descending"),
        RIGHT_ASC("Right Ascending"),
        RIGHT_DESC("Right Descending"),
        LENGTH_ASC("Length Of Cover Ascending"),
        LENGTH_DESC("Length Of Cover Descending"),
        RANDOM("Random");

        private final String name;

        SortType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
