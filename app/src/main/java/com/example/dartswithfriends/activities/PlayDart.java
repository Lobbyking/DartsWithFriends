package com.example.dartswithfriends.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.Player;
import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.R;

import java.util.ArrayList;
import java.util.Map;

public class PlayDart extends AppCompatActivity  implements View.OnClickListener{

    private TableLayout screen;


    //    Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private static final int RQ_PREFERENCES = 8764;
    private boolean darkmode;
    private boolean notifications;
    private boolean gps;

    int playerTurn = 0;
    int dartsAvailable = 3;
    public ArrayList<Integer> punktestand = new ArrayList<>();
    TextView points;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.dartspiel);

        screen = findViewById(R.id.play_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //        Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
                PlayDart.this.preferenceChanged(sharedPrefs, key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        darkmode = prefs.getBoolean("darkmode", false);
        notifications = prefs.getBoolean("notes", true);
        gps = prefs.getBoolean("gps", true);
       // @SuppressLint("WrongViewCast") EditText et = findViewById(R.id.score_TextView);
        setDarkMode();

        points = findViewById(R.id.score_TextView);

        if(MainActivity.cb301.isChecked()){
            //et.setText("301");

            points.setText("301");
            startGame(301, MainActivity.takenPlayers);
        }else if(MainActivity.cb501.isChecked()){
           // et.setText("501");

            points.setText("501");
            startGame(501, MainActivity.takenPlayers);
        }
        Button one = findViewById(R.id.one_Button);
        one.setOnClickListener(this);
        Button two = findViewById(R.id.two_Button);
        two.setOnClickListener(this);
        Button three = findViewById(R.id.three_Button);
        three.setOnClickListener(this);
        Button four = findViewById(R.id.four_Button);
        four.setOnClickListener(this);
        Button five = findViewById(R.id.five_Button);
        five.setOnClickListener(this);
        Button six = findViewById(R.id.six_Button);
        six.setOnClickListener(this);
        Button seven = findViewById(R.id.seven_Button);
        seven.setOnClickListener(this);
        Button eight = findViewById(R.id.eight_Button);
        eight.setOnClickListener(this);
        Button nine = findViewById(R.id.nine_Button);
        nine.setOnClickListener(this);
        Button ten = findViewById(R.id.ten_Button);
        ten.setOnClickListener(this);
        Button eleven = findViewById(R.id.eleven_Button);
        eleven.setOnClickListener(this);
        Button twelve = findViewById(R.id.twelve_Button);
        twelve.setOnClickListener(this);
        Button thirteen = findViewById(R.id.thirteen_Button);
        thirteen.setOnClickListener(this);
        Button fourteen = findViewById(R.id.four_Button);
        fourteen.setOnClickListener(this);
        Button fifteen = findViewById(R.id.fifteen_Button);
        fifteen.setOnClickListener(this);
        Button sixteen = findViewById(R.id.sixteen_Button);
        sixteen.setOnClickListener(this);
        Button seventeen = findViewById(R.id.seventeen_Button);
        seventeen.setOnClickListener(this);
        Button eighteen = findViewById(R.id.eighteen_Button);
        eighteen.setOnClickListener(this);
        Button ninteen = findViewById(R.id.nineteen_Button);
        ninteen.setOnClickListener(this);
        Button twenty = findViewById(R.id.twenty_Button);
        twenty.setOnClickListener(this);
        Button bullsEye  = findViewById(R.id.bull_Button);
        bullsEye.setOnClickListener(this);
        Button zero = findViewById(R.id.null_Button);
        zero.setOnClickListener(this);
        Button showPoints = findViewById(R.id.showOtherScore_Button);
        showPoints.setOnClickListener(this);
        Button undo = findViewById(R.id.undo_Button);
        undo.setOnClickListener(this);


    }

    //    Preferences
    private void preferenceChanged(SharedPreferences sharedPrefs, String key) {
        Map<String, ?> allEntries = sharedPrefs.getAll();
        if(key.equals("darkmode")) {
            darkmode = sharedPrefs.getBoolean(key, false);
            setDarkMode();
        }else if(key.equals("notes")){
            notifications = sharedPrefs.getBoolean(key,true);
        }else if(key.equals("gps")){
            gps = sharedPrefs.getBoolean(key,true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pref:
                Intent intent = new Intent(this, MySettingsActivity.class);
                startActivityForResult(intent, RQ_PREFERENCES);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //    Darkmode
    private void setDarkMode(){
        if(darkmode) {
            screen.setBackgroundColor(Color.GRAY);
        }else{
            screen.setBackgroundColor(Color.parseColor("#20B451"));
        }
    }

    public void startGame(int points, ArrayList<Player> takenPlayers){

        for(int i = 0; i<takenPlayers.size(); i++){
            punktestand.add(points);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.one_Button || v.getId() == R.id.two_Button || v.getId() == R.id.three_Button || v.getId() == R.id.four_Button || v.getId() == R.id.five_Button|| v.getId() == R.id.six_Button || v.getId() == R.id.seven_Button || v.getId() == R.id.eight_Button || v.getId() == R.id.nine_Button || v.getId() == R.id.ten_Button || v.getId() == R.id.eleven_Button || v.getId() == R.id.twelve_Button || v.getId() == R.id.thirteen_Button || v.getId() == R.id.fourteen_Button || v.getId() == R.id.fifteen_Button || v.getId() == R.id.sixteen_Button || v.getId() == R.id.seven_Button || v.getId() == R.id.eighteen_Button || v.getId() == R.id.nineteen_Button || v.getId() == R.id.twenty_Button){
           Button pushed = (Button) v;
           final String number = pushed.getText().toString();
            AlertDialog.Builder ad = new AlertDialog.Builder(this).setTitle("Auswählen:")
                    .setNegativeButton("D"+number, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            int punkte = punktestand.get(playerTurn);
                            if(punkte +1 > Integer.parseInt(number)*2){
                                punkte = punkte - Integer.parseInt(number)*2;
                                points.setText(String.valueOf(punkte));
                                dartsAvailable--;
                                punktestand.set(playerTurn, punkte);
                                if(dartsAvailable == 0){
                                    playerTurn++;
                                    dartsAvailable = 3;
                                    Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                                }else{

                                }
                            }else{
                                Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNeutralButton(number, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            int punkte = punktestand.get(playerTurn);
                            if(punkte +1 > Integer.parseInt(number)){
                                punkte = punkte - Integer.parseInt(number);
                                points.setText(String.valueOf(punkte));
                                punktestand.set(playerTurn, punkte);
                                dartsAvailable--;
                                if(dartsAvailable == 0){
                                    playerTurn++;
                                    dartsAvailable = 3;
                                    Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                                }else{

                                }
                            }else{
                                Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setPositiveButton("T"+number, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            int punkte = punktestand.get(playerTurn);
                            if(punkte +1 > Integer.parseInt(number)*3){
                                punkte = punkte - Integer.parseInt(number)*3;
                                points.setText(String.valueOf(punkte));
                                dartsAvailable--;
                                punktestand.set(playerTurn, punkte);
                                if(dartsAvailable == 0){
                                    playerTurn++;
                                    dartsAvailable = 3;
                                    Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                                }else{

                                }
                            }else{
                                Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            ad.show();
        }else if(v.getId() == R.id.undo_Button){

        }else if(v.getId() == R.id.bull_Button){
            AlertDialog.Builder ad = new AlertDialog.Builder(this).setPositiveButton("Single Bull", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){

                }
            }).setNegativeButton("Double Bull", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad.show();
        }else if(v.getId() == R.id.null_Button){

        }else if(v.getId() == R.id.showOtherScore_Button){

        }
    }
}
