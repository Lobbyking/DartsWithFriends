package com.example.dartswithfriends;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SDCard_Save {

    static MainActivity main;

    public SDCard_Save(){
        main = MainActivity.getInstance();
    }

    public static void writePlayers(List<Player> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = main.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Players.txt";
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fullPath)));
            for(int i = 0; i < list.size(); ++i){
                out.write(list.get(i).toString()+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public static void writeSMSinvites(List<String> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = main.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";
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

    public static void writeScores(List<String> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = main.getExternalFilesDir(null);
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

    public static ArrayList<Player> readPlayers(){
        ArrayList<Player> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = main.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Players.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                String[] arr = line.split(";");
                Player p = new Player(arr[0],Integer.valueOf(arr[2]));
                p.setAverage(Integer.valueOf(arr[1]));
                list.add(p);
                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
    }

    public static ArrayList<String> readSMS(){
        ArrayList<String> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = main.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                list.add(line);
            }
        } catch (Exception e) {
        }

        return list;
    }

    public static ArrayList<Player> readScores(){
        ArrayList<Player> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = main.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Scores.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                String[] arr = line.split(";");
                Player p = new Player(arr[0],Integer.valueOf(arr[2]));
                p.setAverage(Integer.valueOf(arr[1]));
                list.add(p);
                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
    }
}
