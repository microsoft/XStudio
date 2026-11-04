// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

package com.microsoft.hydralab.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipBombChecker {
    private static final long MAX_UNCOMPRESSED_SIZE = 1024 * 1024 * 1024; // 1024 MB
    private static final int MAX_ENTRIES = 20000;
    private static final int MAX_NESTING_DEPTH = 5;

    public static boolean isZipBomb(File file) {
        return isZipBomb(file, 0);
    }

    private static boolean isZipBomb(File file, int depth) {
        if (depth > MAX_NESTING_DEPTH) {
            return true;
        }

        long totalUncompressedSize = 0;
        int entryCount = 0;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry;
            byte[] buffer = new byte[8192];

            while ((entry = zis.getNextEntry()) != null) {
                entryCount++;
                if (entryCount > MAX_ENTRIES) {
                    return true;
                }

                if (!entry.isDirectory()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int read;
                    while ((read = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, read);
                        totalUncompressedSize += read;
                        if (totalUncompressedSize > MAX_UNCOMPRESSED_SIZE) {
                            return true;
                        }
                    }
                    // check if the entry is a nested zip file
                    if (entry.getName().toLowerCase().endsWith(".zip")) {
                        byte[] nestedZipBytes = baos.toByteArray();
                        File tempZip = File.createTempFile("nested", ".zip");
                        try (FileOutputStream fos = new FileOutputStream(tempZip)) {
                            fos.write(nestedZipBytes);
                        }
                        boolean nestedBomb = isZipBomb(tempZip, depth + 1);
                        tempZip.delete();
                        if (nestedBomb) {
                            return true;
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (Exception e) {
            return true; // If there's an error reading the zip, treat it as a potential zip bomb
        }
        return false;
    }
}
