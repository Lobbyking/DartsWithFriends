package com.example.dartswithfriends.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.Match;
import com.example.dartswithfriends.Player;
import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    public static ArrayList<Integer> punktestand;
    public static ArrayList<String> platzierungen = new ArrayList<>();
    TextView points;
    TextView playerTextView;
    //private ArrayList<Double> gameAverage = new ArrayList<>();
    private TextView thrownAmount;
    private ArrayList<Double> averages = new ArrayList<>();
    private ArrayList<Integer> lastDarts = new ArrayList<>();
    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;
    String lat;
    String lon;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n","NewApi"})
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

        thrownAmount = findViewById(R.id.thrown_textView);
        points = findViewById(R.id.score_TextView);
        playerTextView = findViewById(R.id.player_TextView);
        if(MainActivity.cb301.isChecked()){
            //et.setText("301");
            String name = MainActivity.takenPlayers.get(0).getName();
            playerTextView.setText(name);
            points.setText("301");
            startGame(301, MainActivity.takenPlayers);
        }else if(MainActivity.cb501.isChecked()){
           // et.setText("501");
            String name = MainActivity.takenPlayers.get(0).getName();
            playerTextView.setText(name);
            points.setText("501");
            startGame(501, MainActivity.takenPlayers);
        }
        Button one = findViewById(R.id.one_Button);
        one.setOnClickListener(this);
        Button two = findViewById(R.id.two_Button);
        two.setOnClickListener(this);
        Button three = findViewById(R.id.three_Button);
        three.setOnClickListener(this);
        @SuppressLint("CutPasteId") Button four = findViewById(R.id.four_Button);
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
        Button fourteen = findViewById(R.id.fourteen_Button);
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

        thrownAmount = findViewById(R.id.thrown_textView);

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
            screen.setBackgroundColor(Color.parseColor("#fffaaf"));
        }
    }

    public void startGame(int points, ArrayList<Player> takenPlayers){
        punktestand = new ArrayList<>();
        for(int i = 0; i<takenPlayers.size(); i++){
            punktestand.add(points);
            averages.add(0.0);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.one_Button || v.getId() == R.id.two_Button || v.getId() == R.id.three_Button || v.getId() == R.id.four_Button || v.getId() == R.id.five_Button|| v.getId() == R.id.six_Button || v.getId() == R.id.seven_Button || v.getId() == R.id.eight_Button || v.getId() == R.id.nine_Button || v.getId() == R.id.ten_Button || v.getId() == R.id.eleven_Button || v.getId() == R.id.twelve_Button || v.getId() == R.id.thirteen_Button || v.getId() == R.id.fourteen_Button || v.getId() == R.id.fifteen_Button || v.getId() == R.id.sixteen_Button || v.getId() == R.id.seventeen_Button || v.getId() == R.id.eighteen_Button || v.getId() == R.id.nineteen_Button || v.getId() == R.id.twenty_Button){
           Button pushed = (Button) v;
           final String number = pushed.getText().toString();
           AlertDialog alterDialog = new AlertDialog.Builder(this).create();

           alterDialog.setButton(AlertDialog.BUTTON_NEUTRAL, String.valueOf(number), new DialogInterface.OnClickListener() {
               @SuppressLint("SetTextI18n")
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   int punkte = punktestand.get(playerTurn);
                   if(punkte +1 > Integer.parseInt(number)){
                       if(punkte-Integer.parseInt(number) <2){
                           Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist dran!", Toast.LENGTH_LONG).show();
                       }else {
                           punkte = punkte - Integer.parseInt(number) ;
                           points.setText(String.valueOf(punkte));
                           punktestand.set(playerTurn, punkte);
                           dartsAvailable--;

                            int alreadyThrown = Integer.parseInt(thrownAmount.getText().toString());
                            alreadyThrown = alreadyThrown + Integer.parseInt(number);
                            thrownAmount.setText(String.valueOf(alreadyThrown));
                            lastDarts.add( Integer.parseInt(number));

                           if (dartsAvailable == 0) {
                               playerTurn++;
                               if (playerTurn >= MainActivity.takenPlayers.size()) {
                                   playerTurn = 0;
                               }
                               dartsAvailable = 3;
                               playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                               points.setText(punktestand.get(playerTurn).toString());
                               Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                               thrownAmount.setText("0");
                           } else {

                           }
                       }
                   }else{
                       Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                       playerTurn++;
                       if (playerTurn >= MainActivity.takenPlayers.size()) {
                           playerTurn = 0;
                       }
                       dartsAvailable = 3;
                       playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                       points.setText(punktestand.get(playerTurn).toString());
                       thrownAmount.setText("0");
                   }
               }
           });
           alterDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Double"+number, new DialogInterface.OnClickListener() {
               @SuppressLint("ShowToast")
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   int punkte = punktestand.get(playerTurn);
                   if(punkte +1 > Integer.parseInt(number)*2){
                       if(punkte-Integer.parseInt(number)*2 == 1 ||punkte-Integer.parseInt(number)*2 < 0 ){
                           Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist dran!", Toast.LENGTH_LONG).show();

                       }else {
                           punkte = punkte - Integer.parseInt(number) * 2;
                           points.setText(String.valueOf(punkte));
                           punktestand.set(playerTurn, punkte);
                           dartsAvailable--;

                           int alreadyThrown = Integer.parseInt(thrownAmount.getText().toString());
                           alreadyThrown = alreadyThrown + Integer.parseInt(number)*2;
                           thrownAmount.setText(String.valueOf(alreadyThrown));
                           lastDarts.add(Integer.parseInt(number)*2);

                           if (punkte == 0) {
                               writeScores();
                               endGame(MainActivity.takenPlayers.get(playerTurn).getName());
                               return;
                           }

                           if (dartsAvailable == 0) {
                               playerTurn++;
                               if (playerTurn >= MainActivity.takenPlayers.size()) {
                                   playerTurn = 0;
                               }
                               dartsAvailable = 3;
                               playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                               points.setText(punktestand.get(playerTurn).toString());
                               Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                               thrownAmount.setText("0");
                           } else {

                           }
                       }
                   }else{
                       Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                       playerTurn++;
                       if (playerTurn >= MainActivity.takenPlayers.size()) {
                           playerTurn = 0;
                       }
                       dartsAvailable = 3;
                       playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                       points.setText(punktestand.get(playerTurn).toString());
                       thrownAmount.setText("0");
                   }
               }
           });
           alterDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Triple"+number, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   int punkte = punktestand.get(playerTurn);
                   if ((punkte + 1) > Integer.parseInt(number) * 3) {
                       if ((punkte - Integer.parseInt(number) * 3) < 2) {
                           Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist dran!", Toast.LENGTH_LONG).show();
                           playerTurn++;
                           if (playerTurn >= MainActivity.takenPlayers.size()) {
                               playerTurn = 0;
                           }
                           dartsAvailable = 3;
                           playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                           points.setText(punktestand.get(playerTurn).toString());
                       } else {
                           punkte = punkte - Integer.parseInt(number) * 3;
                           points.setText(String.valueOf(punkte));
                           punktestand.set(playerTurn, punkte);
                           dartsAvailable--;

                           int alreadyThrown = Integer.parseInt(thrownAmount.getText().toString());
                           alreadyThrown = alreadyThrown + Integer.parseInt(number)*3;
                           thrownAmount.setText(String.valueOf(alreadyThrown));
                           lastDarts.add(Integer.parseInt(number)*3);

                           if (dartsAvailable == 0) {
                               playerTurn++;
                               if (playerTurn >= MainActivity.takenPlayers.size()) {
                                   playerTurn = 0;
                               }
                               dartsAvailable = 3;
                               Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                               playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName().toString());
                               points.setText(punktestand.get(playerTurn).toString());
                               thrownAmount.setText("0");
                           } else {

                           }
                       }
                   } else {
                       Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                       playerTurn++;
                       if (playerTurn >= MainActivity.takenPlayers.size()) {
                           playerTurn = 0;
                       }
                       dartsAvailable = 3;
                       playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                       points.setText(punktestand.get(playerTurn).toString());
                       thrownAmount.setText("0");
                   }
               }

        });
           alterDialog.show();

        }else if(v.getId() == R.id.undo_Button){
            if(dartsAvailable == 3){
                dartsAvailable = 1;
                if(playerTurn== 0){
                    playerTurn= punktestand.size()-1;
                }else {
                    playerTurn--;
                }
                playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName());
                if(lastDarts.size() > 0) {
                    points.setText(String.valueOf(punktestand.get(playerTurn) + lastDarts.get(lastDarts.size()-1)));
                    thrownAmount.setText(String.valueOf(lastDarts.get(lastDarts.size() - 2) + lastDarts.get(lastDarts.size() - 3)));
                    lastDarts.remove(lastDarts.size()-1);
                    punktestand.set(playerTurn, punktestand.get(playerTurn)+lastDarts.get(lastDarts.size()-1));
                }else{
                    Toast.makeText(this, "Es wurde noch kein Dart geworfen!", Toast.LENGTH_LONG).show();
                }
            }else{
                dartsAvailable++;
                points.setText(String.valueOf(Integer.parseInt(points.getText().toString()) +lastDarts.get(lastDarts.size()-1)));
                punktestand.set(playerTurn, punktestand.get(playerTurn)+lastDarts.get(lastDarts.size()-1));
                if(dartsAvailable == 3){
                    thrownAmount.setText("0");
                }else if(dartsAvailable == 2){
                    thrownAmount.setText(String.valueOf(lastDarts.get(lastDarts.size()-2)));
                }
                lastDarts.remove(lastDarts.size()-1);
            }
            Toast.makeText(this, "Der letzte Dart wurde rückgängig gemacht", Toast.LENGTH_LONG).show();

        }else if(v.getId() == R.id.bull_Button){
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setButton(AlertDialog.BUTTON_NEGATIVE,"Single Bull", new DialogInterface.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int punkte = punktestand.get(playerTurn);
                    if(punkte > 26){
                        punkte = punkte - 25;
                        points.setText(String.valueOf(punkte));
                        punktestand.set(playerTurn, punkte);
                        dartsAvailable--;

                        int alreadyThrown = Integer.parseInt(thrownAmount.getText().toString());
                        alreadyThrown = alreadyThrown + 25;
                        thrownAmount.setText(String.valueOf(alreadyThrown));
                        lastDarts.add(25);

                        if(dartsAvailable == 0){
                            playerTurn++;
                            if(playerTurn >=MainActivity.takenPlayers.size()){
                                playerTurn = 0;
                            }
                            dartsAvailable = 3;
                            Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                            playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName().toString());
                            points.setText(punktestand.get(playerTurn).toString());
                            thrownAmount.setText("0");
                        }
                    }else{
                        Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                        thrownAmount.setText("0");
                    }
                }
            });
            ad.setButton(AlertDialog.BUTTON_POSITIVE, "Double Bull",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int punkte = punktestand.get(playerTurn);
                    if(punkte > 49){
                        punkte = punkte - 50;
                        points.setText(String.valueOf(punkte));
                        punktestand.set(playerTurn, punkte);
                        dartsAvailable--;

                        int alreadyThrown = Integer.parseInt(thrownAmount.getText().toString());
                        alreadyThrown = alreadyThrown + 50;
                        thrownAmount.setText(String.valueOf(alreadyThrown));
                        lastDarts.add(50);

                        if (punkte == 0) {
                            writeScores();
                            endGame(MainActivity.takenPlayers.get(playerTurn).getName());
                        }


                        if(dartsAvailable == 0){
                            playerTurn++;
                            if(playerTurn >=MainActivity.takenPlayers.size()){
                                playerTurn = 0;
                            }
                            dartsAvailable = 3;
                            Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                            playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName().toString());
                            points.setText(punktestand.get(playerTurn).toString());
                            thrownAmount.setText("0");
                        }else{

                        }
                    }else{
                        Toast.makeText(PlayDart.this, "Leider haben Sie sich überworfen, der nächste ist an der Reihe!", Toast.LENGTH_LONG).show();
                        thrownAmount.setText("0");
                    }
                }
            });
            ad.show();
        }else if(v.getId() == R.id.null_Button){
            lastDarts.add(0);
            dartsAvailable--;

            if(dartsAvailable == 0){
                playerTurn++;
                if(playerTurn >=MainActivity.takenPlayers.size()){
                    playerTurn = 0;
                }
                dartsAvailable = 3;
                Toast.makeText(PlayDart.this, "Der nächste Spieler ist an der Reihe", Toast.LENGTH_LONG).show();
                playerTextView.setText(MainActivity.takenPlayers.get(playerTurn).getName().toString());
                points.setText(punktestand.get(playerTurn).toString());
                thrownAmount.setText("0");
            }else{

            }

        }else if(v.getId() == R.id.showOtherScore_Button){

        }
    }

    @SuppressLint("SetTextI18n")
    public void endGame(String winnerName){
        //ArrayList<String> placements = new ArrayList<>();
        int[] temp = new int[punktestand.size()];
        String[] names = new String[MainActivity.takenPlayers.size()];
        for(int i = 0; i<punktestand.size(); i++){
            temp[i] = punktestand.get(i);
            names[i] = MainActivity.takenPlayers.get(i).getName();
        }

        String ersetzen = names[0];
        int x = temp[0];
        names[0] = MainActivity.takenPlayers.get(playerTurn).getName();
        temp[0] = temp[playerTurn];
        names[playerTurn] = ersetzen;
        temp[playerTurn] = x;
        for(int i = 1; i< punktestand.size(); i++){
            if(temp[i]<temp[i-1]){
                ersetzen = names[i];
                int y = temp[i];
                names[i] = names[i-1];
                temp[i] = temp[i-1];
                names[i-1] = ersetzen;
                temp[i-1] = y;
                i= 1;
            }
        }

        for(int i = 0; i<names.length; i++){
            platzierungen.add(names[i]);
        }
        punktestand.clear();
        for(int i = 0; i<temp.length; i++){
            punktestand.add(temp[i]);
        }

        Toast.makeText(PlayDart.this, MainActivity.takenPlayers.get(playerTurn).getName() + " hat gewonnen!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PlayDart.this, endScreen.class);
        startActivity(intent);

        /* endScreen.winner.setText(winnerName + " hat gewonnen!");

        for(int i = 0; i < punktestand.size(); i++) {
            if(i==0) {

                endScreen.player1.setText(winnerName);
                endScreen.player1_avg.setText(String.valueOf(averages.get(0)));
                endScreen.player1_points.setText("0");
            }else if(i == 1) {

                endScreen.player2.setText(names[1]);
                endScreen.player2_avg.setText(String.valueOf(averages.get(1)));
                endScreen.player2_points.setText(String.valueOf(temp[1]));
            }else if(i == 2) {

                endScreen.player3.setText(names[2]);
                endScreen.player3_avg.setText(String.valueOf(averages.get(2)));
                endScreen.player3_points.setText(String.valueOf(temp[2]));
            }else if(i == 3){

                endScreen.player4.setText(names[3]);
                endScreen.player4_avg.setText(String.valueOf(averages.get(3)));
                endScreen.player4_points.setText(String.valueOf(temp[3]));
            }
        } */
    }

    public void writeScores() {
        ArrayList list = MainActivity.takenPlayers;

        //AVERAGES MÜSSEN NU EINEEEE

        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Scores.txt";
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fullPath,true)));
            for(int i = 0; i < list.size(); ++i){
                out.append(list.get(i).toString()+"&");
            }
            getlatlon();
            out.append(lat);
            out.append(" ; ");
            out.append(lon);
            out.append("\n");
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public void getlatlon()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        // user defines the criteria

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);

        // the last known location of this provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        lat=""+location.getLatitude();

        lon=""+location.getLongitude();
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(PlayDart.this, provider + "'s status changed to "+status +"!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(PlayDart.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(PlayDart.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
