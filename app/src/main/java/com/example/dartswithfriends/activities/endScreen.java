package com.example.dartswithfriends.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class endScreen extends AppCompatActivity implements View.OnClickListener{

    @SuppressLint("StaticFieldLeak")
    public static TextView player1;
    @SuppressLint("StaticFieldLeak")
    public static TextView player1_avg;
    @SuppressLint("StaticFieldLeak")
    public static TextView player1_points;
    @SuppressLint("StaticFieldLeak")
    public static TextView player2;
    @SuppressLint("StaticFieldLeak")
    public static TextView player2_avg;
    @SuppressLint("StaticFieldLeak")
    public static TextView player2_points;
    @SuppressLint("StaticFieldLeak")
    public static TextView winner;
    @SuppressLint("StaticFieldLeak")
    public static TextView player3;
    @SuppressLint("StaticFieldLeak")
    public static TextView player3_avg;
    @SuppressLint("StaticFieldLeak")
    public static TextView player3_points;
    @SuppressLint("StaticFieldLeak")
    public static TextView player4;
    @SuppressLint("StaticFieldLeak")
    public static TextView player4_avg;
    @SuppressLint("StaticFieldLeak")
    public static TextView player4_points;

    public ArrayList<Integer> avges;
    public ArrayList<Integer> points;
    public ArrayList<String> playernames;

   public endScreen(ArrayList<Integer> averages, ArrayList<Integer> points, ArrayList<String> names){

       avges = averages;
       this.points = points;
       playernames = names;
    }

    public endScreen(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_game);

        Button mainButton = findViewById(R.id.mainactivity_button);
        Button playAgain = findViewById(R.id.playagain_button);

        winner = findViewById(R.id.winner_textView);

        player1 = findViewById(R.id.player1_textView);
        //player1_avg = findViewById(R.id.player1_avg_textview);
        player1_points = findViewById(R.id.player1_points_textview);

        player2 = findViewById(R.id.player2_textView);
        //player2_avg = findViewById(R.id.player2_avg_textview);
        player2_points = findViewById(R.id.player2_points_textview);

        player3 = findViewById(R.id.player3_textView);
        //player3_avg = findViewById(R.id.player3_avg_textview);
        player3_points = findViewById(R.id.player3_points_textview);

        player4 = findViewById(R.id.player4_textView);
       // player4_avg = findViewById(R.id.player4_avg_textview);
        player4_points = findViewById(R.id.player4_points_textview);

        Button playAgainBtn = findViewById(R.id.playagain_button);
        playAgainBtn.setOnClickListener(this);
        Button mainActivity = findViewById(R.id.mainactivity_button);
        mainActivity.setOnClickListener(this);

        fillFields();
    }

    public void fillFields(){
       winner.setText(PlayDart.platzierungen.get(0) +" hat gewonnen !!!");
       player1.setText("1. Platz " + PlayDart.platzierungen.get(0));
       //player1_avg.setText(avges.get(0));
       //player1_points.setText(PlayDart.punktestand.get(0).toString());
        if(MainActivity.takenPlayers.size()>1) {
            player2.setText("2. Platz " + PlayDart.platzierungen.get(1));
            //player2_avg.setText(avges.get(1));
            player2_points.setText(PlayDart.punktestand.get(1).toString());
        }
        if(MainActivity.takenPlayers.size()>2) {
            player3.setText("3. Platz " +PlayDart.platzierungen.get(2));
            //player3_avg.setText(avges.get(2));
            player3_points.setText(PlayDart.punktestand.get(2).toString());
        }
        if(MainActivity.takenPlayers.size()>3) {
            player4.setText("4.Platz " + PlayDart.platzierungen.get(3));
            //player4_avg.setText(avges.get(3));
            player4_points.setText(PlayDart.punktestand.get(3).toString());
        }
        PlayDart.platzierungen.clear();
        PlayDart.punktestand.clear();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.mainactivity_button){
            MainActivity.takenPlayers.clear();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.playagain_button){
            Intent intent = new Intent(getApplicationContext(), PlayDart.class);
            startActivity(intent);
        }
    }
}
