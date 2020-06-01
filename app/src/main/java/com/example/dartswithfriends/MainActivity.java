package com.example.dartswithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.activities.FriendInvites;
import com.example.dartswithfriends.activities.PlayDart;
import com.example.dartswithfriends.activities.Scoreboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button addPlayer;
    private Button startGame;
    public ArrayList<Player> players = new ArrayList<>();
    private ListView playersListView;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayAdapter<String> aa;
    private ArrayList<Player> takenPlayers = new ArrayList<>();
    private CheckBox cb501;
    private CheckBox cb301;

    private Button switchToFriendInvites;
    private Button switchToScoreboard;

    //    Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private static final int RQ_PREFERENCES = 8764;
    private boolean darkmode;
    private boolean notifications;
    private boolean gps;


    //    Darkmode
    TableLayout start_screen;
//    TableLayout play_screen;
//    TableLayout left_screen;
//    TableLayout right_screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPlayer= findViewById(R.id.addPlayer_btn);
        startGame = findViewById(R.id.startGame_btn);
        playersListView = findViewById(R.id.players_listView);
        players = einlesen();
        for(Player p : players){
            playerNames.add(p.getName());
        }
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerNames);
        playersListView.setAdapter(aa);
        addPlayer.setOnClickListener(this);
        startGame.setOnClickListener(this);
        cb301 = findViewById(R.id.cB301_checkBox);
        cb501 = findViewById(R.id.cB501_checkBox);

        cb301.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb501.setChecked(false);
                }
            }
        });

        cb501.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb301.setChecked(false);
                }
            }
        });

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String takenName = (String) (parent.getItemAtPosition(position));
                if(takenName.contains("--Taken--")){
                    takenName = takenName.replaceAll("--Taken--", "");
                    for(Player p: players){
                        if(p.getName().equals(takenName)){
                            takenPlayers.remove(p);
                        }
                    }
                }else{
                    takenName = takenName + "--Taken--";
                    takenPlayers.add(players.get(position));
                }
                ArrayList<String> temp = new ArrayList<>();
                for(int i = 0; i< playerNames.size(); i++){
                    if(i == position){
                        temp.add(takenName);
                    }else{
                        temp.add(playerNames.get(i));
                    }
                }
                playerNames = new ArrayList<>();
                playerNames = temp;
                setView(playerNames);
            }
        });

        //        Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
                MainActivity.this.preferenceChanged(sharedPrefs, key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        start_screen = findViewById(R.id.start_screen);
//        play_screen = findViewById(R.id.play_screen);
//        left_screen = findViewById(R.id.left_screen);
//        right_screen = findViewById(R.id.right_screen);

        darkmode = prefs.getBoolean("darkmode", false);
        notifications = prefs.getBoolean("notes", true);
        gps = prefs.getBoolean("gps", true);

        setDarkMode();

        switchToFriendInvites = findViewById(R.id.midToEinladungen_Button);
        switchToFriendInvites.setOnClickListener(this);
        switchToScoreboard = findViewById(R.id.midToSpiele_Button);
        switchToScoreboard.setOnClickListener(this);

        }


    @Override
    public void onClick(View v) {
        if(v.getId() == addPlayer.getId()){
            EditText playerName = findViewById(R.id.playerName_editText);
            String name = playerName.getText().toString();
            Player p = new Player(name, 0);
            players.add(p);
            playerNames.add(name);
            aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNames);
            schreiben(players);
        }else if(v.getId() == startGame.getId()) {
            if (cb501.isChecked()) {
                playGame(501, takenPlayers);
            } else if (cb301.isChecked()) {
                playGame(301, takenPlayers);
            } else {
                Toast.makeText(this, "Du musst eine der CheckBoxes auswählen.", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(this, PlayDart.class);
            startActivity(intent);
        }else if(v.getId() == switchToFriendInvites.getId()){
            Intent intent = new Intent(this, FriendInvites.class);
            startActivity(intent);
        }else if(v.getId() == switchToScoreboard.getId()){
            Intent intent = new Intent(this, Scoreboard.class);
            startActivity(intent);
        }
            else{

            }
    }

    public void playGame(int points, ArrayList<Player> takenPlayers){

    }

    private ArrayList<Player> einlesen(){
        ArrayList<Player> writeBack = new ArrayList<Player>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("players.txt"));
            String line;
            while((line = br.readLine()) != null){
                String[] splitted = line.split(";");
                Player temp = new Player(splitted[0], Integer.valueOf(splitted[2]));
                writeBack.add(temp);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("-Eilesn- fick dich");
            e.printStackTrace();
        }
        return writeBack;
    }

    private void schreiben(ArrayList<Player> p){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("players.txt"));
            for(Player singleOne : p){
                bw.write(singleOne.toString());
            }
            bw.flush();
            bw.close();
        }catch(IOException e){
            System.out.println("-Schreiben- fick dich");
            e.printStackTrace();
        }
    }


    private void setView(ArrayList<String> temp) {
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp);
        playersListView.setAdapter(aa);
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
            start_screen.setBackgroundColor(Color.GRAY);
//            play_screen.setBackgroundColor(Color.GRAY);
//            left_screen.setBackgroundColor(Color.GRAY);
//            right_screen.setBackgroundColor(Color.GRAY);
        }else{
            start_screen.setBackgroundColor(Color.parseColor("#20B451"));
//            play_screen.setBackgroundColor(Color.parseColor("#20B451"));
//            left_screen.setBackgroundColor(Color.parseColor("#20B451"));
//            right_screen.setBackgroundColor(Color.parseColor("#20B451"));
        }
    }
    }
