package de.pauleduardkoenig.dwds_cf;

import java.awt.*;
import java.util.List;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.0.0
 */
public class Constants {
    public static final String VERSION = "1.2.2";
    public static final String PROGRAMM_NAME = "DWDS-SF";
    public static final Image PROGRAMM_ICON = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("dwds-sf_logo_256.png"));

    public static final List<String> CSV_KWIC_HEADER = List.of("No.", "Date", "Genre", "Bibl", "ContextBefore", "Hit", "ContextAfter");
}
