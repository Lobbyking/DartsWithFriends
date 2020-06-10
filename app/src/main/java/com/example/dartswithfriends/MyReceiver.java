package com.example.dartswithfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;

import com.example.dartswithfriends.activities.FriendInvites;
import com.example.dartswithfriends.activities.Scoreboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyReceiver extends BroadcastReceiver {

    FriendInvites fi = FriendInvites.getInstance();
    String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String Tag = "SmsBroadcastReceiver";
    String msg = "";
    String phoneNo= "";

    List<SMS> arr = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent ) {
        if(intent.getAction()==SMS_RECEIVED){
            Bundle dataBundle = intent.getExtras();
            if(dataBundle!=null){
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for(int i = 0; i < mypdu.length; ++i){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i],format);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }
            }
        }
        if(msg.contains("Dart")){
            arr.add(new SMS(msg,phoneNo));
            fi.SMS_invites.add(new SMS(msg,phoneNo));
            fi.updateListView();
            writeSMSinvites(arr);
        }
    }

    public void writeSMSinvites(List<SMS> list) {
        if(list == null){
            return;
        }
        String state = Environment.getExternalStorageState();
        if (! state . equals(Environment.MEDIA_MOUNTED)) return;
        File outFile = fi.getExternalFilesDir(null);
        String path = outFile.getAbsolutePath();
        String fullPath = path + File. separator + "SMS.txt";
        try {
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(new File(fullPath),true)));
            for(int i = 0; i < list.size(); ++i){
                out.append(list.get(i).toString()+"\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

}
