package com.example.dartswithfriends;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class SDCard_Save {

    private void writeToFile(String filename, List<String> list) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = Environment.getExternalStorageDirectory();
        String path = outFile.getAbsolutePath();
        String fullPath = path + File.separator + filename;
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fullPath)));
            for(int i = 0; i < list.size(); ++i){
                out.write(list.get(i)+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }
}
