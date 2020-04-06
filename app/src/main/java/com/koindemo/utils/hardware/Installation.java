package com.koindemo.utils.hardware;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Allows to identify app installations.
 * http://android-developers.blogspot.ie/2011/03/identifying-app-installations.html
 */

public class Installation {

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    /**
     * Writes an UUID as the app's identifier the first time the app runs after installation.
     *
     * @param context
     * @return
     */
    public synchronized static String id(Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);

            try {
                if (!installation.exists())
                    writeUUID(installation);

                sID = readUUID(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return sID;
    }

    private static String readUUID(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeUUID(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}

