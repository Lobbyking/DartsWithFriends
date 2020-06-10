package com.example.dartswithfriends.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.MyReceiver;
import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.R;
import com.example.dartswithfriends.SDCard_Save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendInvites extends AppCompatActivity implements View.OnClickListener{

    private static final int RQ_READ = 84232;
    private static final int RQ_WRITE = 532;
    private Button switchToMid;
    private TableLayout screen;

    //    Preferences
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;
    private static final int RQ_PREFERENCES = 8764;
    private boolean darkmode;
    private boolean notifications;
    private boolean gps;

    private static final int RQ_READSMS = 55675;
    private static final int RQ_RECEIVESMS = 12343;
    private static FriendInvites instance;
    ListView listView;
    public static List list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left);

        switchToMid = findViewById(R.id.leftBackToMid_Button);
        switchToMid.setOnClickListener(this);
        screen = findViewById(R.id.left_screen);
        list = new ArrayList<>();

        //        Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
                FriendInvites .this.preferenceChanged(sharedPrefs, key);
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        darkmode = prefs.getBoolean("darkmode", false);
        notifications = prefs.getBoolean("notes", true);
        gps = prefs.getBoolean("gps", true);

        setDarkMode();

        instance = this;
        if (checkSelfPermission(Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS},RQ_READSMS);
        } else {
//            doIt();
        }
        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},RQ_RECEIVESMS);
        } else {
//            doIt();
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RQ_READ);
        } else {
            list = readSMS();
        }
        MyReceiver myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,intentFilter);
        listView = findViewById(R.id.Einladungen_listView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

//    Permissions

    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults ) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode==RQ_READSMS) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
//                doIt();
            }
        }
        if (requestCode==RQ_RECEIVESMS) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
//                doIt();
            }
        }
        if (requestCode==RQ_READ) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
                list = readSMS();
            }
        }
        if (requestCode==RQ_WRITE) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
                writeSMSinvites(list);
            }
        }
    }

    public static FriendInvites getInstance(){
        return instance;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == switchToMid.getId()) {
            writeSMSinvites(list);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void updateListView(){
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
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

    public void writeSMSinvites(List<String> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(new File(fullPath))));
            for(int i = 0; i < list.size(); ++i){
                out.append(list.get(i)+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public ArrayList<String> readSMS(){
        ArrayList<String> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                list.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
    }
}

