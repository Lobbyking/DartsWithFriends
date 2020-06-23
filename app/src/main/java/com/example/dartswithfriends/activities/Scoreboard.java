package com.example.dartswithfriends.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dartswithfriends.LocationAsyncTask;
import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.Match;
import com.example.dartswithfriends.OnSwipeTouchListener;
import com.example.dartswithfriends.Player;
import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.R;
import com.example.dartswithfriends.ScoreBoardLvAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;

public class Scoreboard extends AppCompatActivity implements View.OnClickListener{

    private Button switchToMid;
    private TableLayout screen;

    //    Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private static final int RQ_PREFERENCES = 8764;
    private boolean darkmode;
    private boolean notifications;
    private boolean gps;
    private List<Match> matches = new ArrayList<>();
    private ListView scoreboard;
    private ScoreBoardLvAdapter lvAdapter;

    public static Scoreboard instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.right);

        instance = this;
        screen = findViewById(R.id.right_screen);
        scoreboard = findViewById(R.id.Spiele_listView);

        matches = readScores();
        writeToLv();


        //        Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
                Scoreboard.this.preferenceChanged(sharedPrefs, key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        darkmode = prefs.getBoolean("darkmode", false);
        notifications = prefs.getBoolean("notes", true);
        gps = prefs.getBoolean("gps", true);

        setDarkMode();

        lvAdapter = new ScoreBoardLvAdapter(this, R.layout.listview_layout_scoreboard, matches);
        scoreboard.setAdapter(lvAdapter);

        scoreboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Double longitude = matches.get(pos).getLongitude();
                Double latitude = matches.get(pos).getLatitude();
                String poss="geo:"+latitude+","+longitude+"?z=15";
                Uri uri = Uri.parse(poss);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        //SWIPE listeners

        findViewById(R.id.Scoreboard_screen).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.right_screen).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.header_row).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.header_textView).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.tableRow_underline).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.underlinement_textView).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.tableRow_listView).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });
        findViewById(R.id.Spiele_listView).setOnTouchListener(new OnSwipeTouchListener(Scoreboard.this) {
            public void onSwipeRight() {
                switchScreen();
            }

            public void onSwipeLeft() {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == switchToMid.getId()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public static Scoreboard getInstance(){
        return instance;
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


    public ArrayList<Match> readScores(){
        ArrayList<Match> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Scores.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                HashMap<Player, Double> game = new HashMap<>();

                String[] arr = line.split("-");

                int c=0;
                for(int i = 0; i < arr.length-1; ++i){

                    String[] einzPlayer = arr[i].split(";");

                        Player p = new Player(einzPlayer[0],Integer.valueOf(einzPlayer[2]));
                        Double d = new Double(einzPlayer[1]);
                        game.put(p,d);
                        c++;
                }

                Double lat = Double.valueOf(arr[c].split(";")[0]);
                Double lon = Double.valueOf(arr[c].split(";")[1]);;

                list.add(new Match(game,lon,lat));

                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
    }

    public void switchScreen()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void writeToLv()
    {
        List<Match> inLv = new ArrayList<>();
        for(int i = matches.size(); i > 0;--i)
        {
            inLv.add(matches.get(i-1));
        }

        lvAdapter = new ScoreBoardLvAdapter(this, R.layout.listview_layout_scoreboard , inLv);
        scoreboard.setAdapter(lvAdapter);
    }

}
