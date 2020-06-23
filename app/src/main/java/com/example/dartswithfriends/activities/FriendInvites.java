package com.example.dartswithfriends.activities;

import android.Manifest;
import android.app.PendingIntent;
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
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.dartswithfriends.FriendInvitesLvAdapter;
import com.example.dartswithfriends.MainActivity;
import com.example.dartswithfriends.MyReceiver;
import com.example.dartswithfriends.OnSwipeTouchListener;
import com.example.dartswithfriends.Preferences.MySettingsActivity;
import com.example.dartswithfriends.R;
import com.example.dartswithfriends.SDCard_Save;
import com.example.dartswithfriends.SMS;

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
    private static final int RQ_READ = 84232;
    private static final int RQ_WRITE = 532;
    private static final int RQ_SENDSMS = 5432523;
    private static FriendInvites instance;
    ListView listView;
    public static List<SMS> SMS_invites;
    FriendInvitesLvAdapter adapter;

    boolean accept = false;
    MenuItem current = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left);
        screen = findViewById(R.id.left_screen);

        SMS_invites = new ArrayList<>();

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
            SMS_invites = readSMS();
        }

        MyReceiver myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,intentFilter);
        listView = findViewById(R.id.Einladungen_listView);
        adapter = new FriendInvitesLvAdapter(this,R.layout.listview_layout_friendinvites,SMS_invites);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);


        //ONSWIPELISTENER
        findViewById(R.id.FriendInvites_Layout).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.left_screen).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.header_row).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.header_textView).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.tableRow_underline).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.underlinement_textView).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });
        findViewById(R.id.tableRow_listview).setOnTouchListener(new OnSwipeTouchListener(FriendInvites.this) {
            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                switchScreen();
            }
        });

    }

//    Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    int viewId = v.getId();
    if (viewId == R.id.Einladungen_listView) {
        getMenuInflater().inflate(R.menu.friend_invites_menu, menu);
    }
    super.onCreateContextMenu(menu, v, menuInfo);
}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.accept) {
            accept = true;
            current = item;
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},RQ_SENDSMS);
            } else {
                sendSMS(accept, current);
            }
            return true;
        }
        if (item.getItemId() == R.id.deny) {
            accept = false;
            current = item;
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},RQ_SENDSMS);
            } else {
                sendSMS(accept, current);
            }

            return true;
        }
        if (item.getItemId() == R.id.delete) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String s = "";
            if (info != null) {
                long id = info.id;
                int pos = info.position;
                s = info != null ?
                        listView.getAdapter().getItem(pos).toString() :
                        "";
            }

            SMS sms = findSMS(s);

            SMS_invites.remove(sms);

            adapter = new FriendInvitesLvAdapter(this,R.layout.listview_layout_friendinvites,SMS_invites);
            listView.setAdapter(adapter);
            writeSMSinvites(SMS_invites);
            return true;
        }
        return super.onContextItemSelected(item);
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
                SMS_invites = readSMS();
            }
        }
        if (requestCode==RQ_WRITE) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
                writeSMSinvites(SMS_invites);
            }
        }if (requestCode==RQ_SENDSMS) {
            if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
                sendSMS(accept, current);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == switchToMid.getId()) {
            writeSMSinvites(SMS_invites);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void updateListView(){
        adapter = new FriendInvitesLvAdapter(this,R.layout.listview_layout_friendinvites,SMS_invites);
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
            screen.setBackgroundColor(Color.parseColor("#fffaaf"));
        }
    }

    public static FriendInvites getInstance(){
        return instance;
    }

    public void writeSMSinvites(List<SMS> list) {
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
                out.append(list.get(i).toString()+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public ArrayList<SMS> readSMS(){
        ArrayList<SMS> list = new ArrayList<>();

        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) ;
        File outFile = getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fullPath)));

            String line = br.readLine();

            while(line != null || !line.equals("")){
                String[] arr = line.split(";");
                list.add(new SMS(arr[0],arr[1]));
                line = br.readLine();
            }
        } catch (Exception e) {
        }

        return list;
    }

    private SMS findSMS(String s){
        String[]arr = s.split(";");
        String msg = arr[0];
        String number = arr[1];

        for(int i = 0; i < SMS_invites.size(); ++i){
            if(SMS_invites.get(i).getMsg().equals(msg)){
                if(SMS_invites.get(i).getNumber().equals(number)){
                    return SMS_invites.get(i);
                }
            }
        }
        return null;
    }


    private void sendSMS(boolean b, MenuItem item){
        if(b){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String s = "";
            if (info != null) {
                long id = info.id;
                int pos = info.position;
                s = info != null ?
                        listView.getAdapter().getItem(pos).toString() :
                        "";
            }

            SMS sms = findSMS(s);

            String number = sms.getNumber();
            String msg = "Ja gerne, lass uns treffen!";
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, SMS.class), 0);

            SmsManager SMSmanager = SmsManager.getDefault();
            SMSmanager.sendTextMessage(number, null, msg, pi, null);

            SMS_invites.remove(sms);

            adapter = new FriendInvitesLvAdapter(this,R.layout.listview_layout_friendinvites,SMS_invites);
            listView.setAdapter(adapter);
            writeSMSinvites(SMS_invites);
        }else{
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String s = "";
            if (info != null) {
                long id = info.id;
                int pos = info.position;
                s = info != null ?
                        listView.getAdapter().getItem(pos).toString() :
                        "";
            }

            SMS sms = findSMS(s);

            String number = sms.getNumber();
            String msg = "Vielleicht ein anderes mal.";
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, SMS.class), 0);

            SmsManager SMSmanager = SmsManager.getDefault();
            SMSmanager.sendTextMessage(number, null, msg, pi, null);

            SMS_invites.remove(sms);

            adapter = new FriendInvitesLvAdapter(this,R.layout.listview_layout_friendinvites,SMS_invites);
            listView.setAdapter(adapter);
            writeSMSinvites(SMS_invites);
        }
    }

    public void switchScreen()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

