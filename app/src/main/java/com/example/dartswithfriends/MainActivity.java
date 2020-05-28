package com.example.dartswithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addPlayer;
    private Button startGame;
    public ArrayList<Player> players = new ArrayList<>();
    private ListView playersListView;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayAdapter<String> aa;

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
        final CheckBox cb301 = findViewById(R.id.cB301_checkBox);
        final CheckBox cb501 = findViewById(R.id.cB501_checkBox);

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
                if(takenName.contains("*")){
                    //takenName = takenName.replaceAll("*********", "");
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
        }else if(v.getId() == startGame.getId()){

        }else{

        }
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
    }
