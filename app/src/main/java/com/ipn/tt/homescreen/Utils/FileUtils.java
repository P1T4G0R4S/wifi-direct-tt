package com.ipn.tt.homescreen.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by osvaldo on 8/27/17.
 */

public class FileUtils {
    protected String TAG = "FileUtils";
    private Context context;

    public FileUtils(){}

    public FileUtils(Context ctx) {
        context = ctx;
    }

    public File createFile(String fileName) {
        //File data = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
        File data = getStorageDir(TAG);
        File file = new File(data, fileName);
        file.setWritable(true);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void appendLine(File file, String content) {
        String string = content;
        FileWriter outputStream;

        try {
            outputStream = new FileWriter(file, true);
            outputStream.append(string);
            outputStream.append("\n");
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFile(File file) {
        String string = "";
        FileWriter outputStream;

        try {
            outputStream = new FileWriter(file);
            outputStream.write(string);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLine(File file, String content) {
        String string = content;
        FileWriter outputStream;

        try {
            outputStream = new FileWriter(file);
            outputStream.write(string);
            outputStream.append("\n");
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getStorageDir(String dirName) {
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);
        //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), dirName);
        File file = context.getExternalFilesDir(null);

        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(TAG, "Directory not created");
            }
        }
        Log.d(TAG, file.getAbsolutePath());
        return file;
    }

    public String readAllText(File file) {
        String ret = "";

        try {
            //InputStream inputStream = context.openFileInput("config.txt");

            if ( file != null ) {
                FileReader inputStream = new FileReader(file);
                //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStream);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString + "\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
