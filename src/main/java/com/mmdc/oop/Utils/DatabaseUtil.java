package com.mmdc.oop.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class DatabaseUtil {

    public static File copyDatabaseToTemp() {
        try (InputStream is = DatabaseUtil.class.getResourceAsStream("/myapp.db")) { // Adjust the path
            if (is == null) {
                throw new RuntimeException("Cannot find database in resources.");
            }
            File tempFile = Files.createTempFile("db_", ".db").toFile();
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy database from resources.", e);
        }
    }

    public static String connectToDatabase() {
        try {
            File dbFile = copyDatabaseToTemp();
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to database.", e);
        }
    }
}

