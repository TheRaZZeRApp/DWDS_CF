package de.pauleduardkoenig.dwds_cf;

import com.therazzerapp.milorazlib.logger.Logging;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * <description>
 *
 * @author Paul Eduard Koenig <rezzer101@googlemail.com>
 * @since 1.0.0
 */
public class Utils{
    public static String escapeWordToPath(String word){
        if (word == null || word.isBlank()){
            return "UNKNOWN";
        }
        return word.trim().replace(" ", "_");
    }

    public static void openPathInDesktop(String path){
        try{
            Desktop.getDesktop().open(new File(path));
        } catch (IOException | IllegalArgumentException i){
            Logging.stackTrace(DWDS_CF.logger, i);
        }
    }
}
