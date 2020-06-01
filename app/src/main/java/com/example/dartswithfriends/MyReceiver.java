package com.example.dartswithfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.dartswithfriends.activities.FriendInvites;

public class MyReceiver extends BroadcastReceiver {

    FriendInvites ma = FriendInvites.getInstance();
    String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String Tag = "SmsBroadcastReceiver";
    String msg = "";
    String phoneNo= "";

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
            MainActivity.SMS_Invites.add(msg);
        }
    }
}
