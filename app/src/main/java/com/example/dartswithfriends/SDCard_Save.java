package com.example.dartswithfriends;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SDCard_Save {

    private void writePlayers(List<String> list) {
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = MainActivity.this.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Players.txt";
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

    private void writeSMSinvites(List<String> list) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = Environment.getExternalStorageDirectory();
        String path = outFile.getAbsolutePath();
        String fullPath = path + File.separator + "SMS.txt";
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

    private void writeScores(List<String> list) {
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = getExternalFilesDir ( null );
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Scores.txt";
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

    private List<String> readPlayers(){
        List<String> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = getExternalFilesDir ( null );
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + FILENAMEJSON;

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

        return list;
    }
}
