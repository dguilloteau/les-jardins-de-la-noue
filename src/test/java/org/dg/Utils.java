package org.dg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.quarkus.logging.Log;

public class Utils {

    public static String getJsonFromFile(String pathFile) {
        File file = new File(pathFile);
        try (InputStream is = new FileInputStream(file)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.error("Erreur = " + e.getMessage());
        }
        return null;
    }

}
