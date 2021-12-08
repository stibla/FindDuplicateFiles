package com.stibla.findduplicatefiles;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileChecksum {

    public static byte[] createChecksum(String filename) throws Exception {
        InputStream fis = new java.io.FileInputStream(filename);

        byte[] buffer = new byte['a'];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            try {
                numRead = fis.read(buffer);
            } catch (IOException e) {
                System.out.println("Chyba numRead = fis.read(buffer);" + filename);
                numRead = -1;
            }

            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String filename) throws Exception {
        byte[] b = createChecksum(filename);
        String result = "";

        for (int i = 0; i < b.length; i++) {
            result = result + Integer.toString((b[i] & 0xFF) + 256, 16).substring(1);
        }
        return result;
    }
}
