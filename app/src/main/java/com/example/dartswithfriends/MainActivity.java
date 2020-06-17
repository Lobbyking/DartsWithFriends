package com.example.dartswithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int RQ_READ = 5423;
    private static final int RQ_WRITE = 23322;
    private Button addPlayer;
    private Button startGame;
    public ArrayList<Player> players = new ArrayList<>();
    private ListView playersListView;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayAdapter<String> aa;
    public static ArrayList<Player> takenPlayers = new ArrayList<>();
    public static CheckBox cb501;
    public static CheckBox cb301;

    private Button switchToFriendInvites;
    private Button switchToScoreboard;

    public static List<String> SMS_Invites;

    static MainActivity instance;

    //    Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private static final int RQ_PREFERENCES = 8764;
    private boolean darkmode;
    private boolean notifications;
    private boolean gps;


    //    Darkmode
    TableLayout start_screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addPlayer= findViewById(R.id.addPlayer_btn);
        startGame = findViewById(R.id.startGame_btn);
        playersListView = findViewById(R.id.players_listView);
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


        darkmode = prefs.getBoolean("darkmode", false);
        notifications = prefs.getBoolean("notes", true);
        gps = prefs.getBoolean("gps", true);

        setDarkMode();

        SMS_Invites = new ArrayList<>();

        instance = MainActivity.this;

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RQ_READ);
        } else {
            players = readPlayers();
        }

        for(int i = 0; i < players.size(); ++i){
            playerNames.add(players.get(i).getName());
        }
        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playerNames);
        playersListView.setAdapter(aa);


        //SWIPELISTENER
        findViewById(R.id.Main_Layout).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.start_screen).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.header_row).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.header_textView).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_Underline).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.underlinement_textView).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_TextView_PlayerSelect).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.spieler_textView).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_NewPlayer).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.name_textView).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_HinzufügenButton).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_listViewSelectPlayer).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.players_listView).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_SpielmodiWählen).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.textView4).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_Checkboxen).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        findViewById(R.id.tableRow_startButton).setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                switchScreenRight();
            }

            public void onSwipeLeft() {
                switchScreenLeft();
            }
        });
        }


    @Override
    public void onClick(View v) {
        if(v.getId() == addPlayer.getId()){
            EditText playerName = findViewById(R.id.playerName_editText);
            String name = playerName.getText().toString();
            if(!name.equals("")) {
                Player p = new Player(name, 0);
                players.add(p);
                playerNames.add(name);
                aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNames);
                playerName.setText("");
            }
            else
            {
                Toast.makeText(this, "Bitte geben Sie einen Namen ein", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId() == startGame.getId()) {

            if (cb501.isChecked()) {
                try{
                    writePlayers(players);
                    takenPlayers.get(0);
                    Intent intent = new Intent(this, PlayDart.class);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(this, "Sie müssen einen oder mehrere Spieler auswählen", Toast.LENGTH_LONG).show();
                }

            } else if (cb301.isChecked()) {
                try{
                    writePlayers(players);
                    takenPlayers.get(0);
                    Intent intent = new Intent(this, PlayDart.class);
                    startActivity(intent);

                }catch(Exception e){
                    Toast.makeText(this, "Sie müssen einen oder mehrere Spieler auswählen", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Du musst eine der CheckBoxes auswählen.", Toast.LENGTH_LONG).show();
            }
        }else if(v.getId() == switchToFriendInvites.getId()){
            writePlayers(players);
            Intent intent = new Intent(this, FriendInvites.class);
            startActivity(intent);
        }else if(v.getId() == switchToScoreboard.getId()){
            writePlayers(players);
            Intent intent = new Intent(this, Scoreboard.class);
            startActivity(intent);
        }
        else{

        }
    }

    private void setView(ArrayList<String> temp) {
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, temp);
        playersListView.setAdapter(aa);
    }

    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode==RQ_READ) {
            if (requestCode==RQ_READ) {
                if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                    //user does not allow
                } else {
                    players = readPlayers();
                }
            }
            if (requestCode==RQ_WRITE) {
                if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                    //user does not allow
                } else {
                    writePlayers(players);
                }
            }
        }
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


    private void setDarkMode(){
        if(darkmode) {
            start_screen.setBackgroundColor(Color.GRAY);

        }else{
            start_screen.setBackgroundColor(Color.parseColor("#fffaaf"));

        }
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public void writePlayers(List<Player> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Players.txt";
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fullPath)));
            for(int i = 0; i < list.size(); ++i){
                out.append(list.get(i).toString()+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public  ArrayList<Player> readPlayers(){
        ArrayList<Player> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "Players.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                String[] arr = line.split(";");
                Player p = new Player(arr[0],Integer.valueOf(arr[2]));
                p.setAverage(Double.valueOf(arr[1]));
                list.add(p);
                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
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

    public void switchScreenRight()
    {
        Intent intent = new Intent(getApplicationContext(), FriendInvites.class);
        startActivity(intent);
    }
    public void switchScreenLeft()
    {
        Intent intent = new Intent(getApplicationContext(), Scoreboard.class);
        startActivity(intent);
    }
}
