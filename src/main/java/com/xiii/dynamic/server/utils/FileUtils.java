package com.xiii.dynamic.server.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void deleteDirectory(final File file) throws IOException {
        if (file.isDirectory()) {
            final File[] entries = file.listFiles();
            if (entries != null) {
                for (final File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        if (!file.delete()) throw new IOException("Failed to delete " + file);
    }
}
