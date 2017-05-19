package com.lanshon.strictmode;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lanshon on 18/05/2017.
 */

public class StorageUtils {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 文件系统的操作
     */
    public static void writeToExternalStorage(String file, String content) {
        File externalStorage = Environment.getExternalStorageDirectory();
        File mbFile = new File(externalStorage, file);
        if (!mbFile.getParentFile().exists()) {
            mbFile.getParentFile().mkdirs();
        }
        try {
            OutputStream output = new FileOutputStream(mbFile);
            output.write(content.getBytes());
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromExternalStorage(String file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte buf[] = new byte[inputStream.available()];
            inputStream.read(buf);
            inputStream.close();
            return new String(buf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
