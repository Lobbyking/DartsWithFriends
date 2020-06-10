package com.example.dartswithfriends.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.dartswithfriends.R;

public class endScreen extends AppCompatActivity {

    public static TextView player1;
    public static TextView player1_avg;
    public static TextView player1_points;
    public static TextView player2;
    public static TextView player2_avg;
    public static TextView player2_points;
    public static TextView winner;
    public static TextView player3;
    public static TextView player3_avg;
    public static TextView player3_points;
    public static TextView player4;
    public static TextView player4_avg;
    public static TextView player4_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_game);

        winner = findViewById(R.id.winner_textView);

        player1 = findViewById(R.id.player1_textView);
        player1_avg = findViewById(R.id.player1_avg_textview);
        player1_points = findViewById(R.id.player1_points_textview);

        player2 = findViewById(R.id.player2_textView);
        player2_avg = findViewById(R.id.player2_avg_textview);
        player2_points = findViewById(R.id.player2_points_textview);

        player3 = findViewById(R.id.player3_textView);
        player3_avg = findViewById(R.id.player3_avg_textview);
        player3_points = findViewById(R.id.player3_points_textview);

        player4 = findViewById(R.id.player4_textView);
        player4_avg = findViewById(R.id.player4_avg_textview);
        player4_points = findViewById(R.id.player4_points_textview);
    }
}
