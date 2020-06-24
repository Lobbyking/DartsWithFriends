package com.example.dartswithfriends;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Telephony;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dartswithfriends.activities.FriendInvites;

import java.util.ArrayList;
import java.util.List;

public class Notification extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private List<SMS> list;

    public Notification() {
        super("Notification");
        list = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        for(int i = 0; i < FriendInvites.SMS_invites.size(); ++i){
            if(FriendInvites.SMS_invites.get(i).isNote()){
                list.add(FriendInvites.SMS_invites.get(i));
                FriendInvites.SMS_invites.get(i).setNote(false);
            }
        }

        if(list.size() > 1){
            CharSequence name = "foo";
            String description = "bar";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("10", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, "10")
                    .setSmallIcon(android.R.drawable.btn_star_big_on)
                    .setColor(Color.GREEN)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Sie haben mehrere Einladung bekommen")
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(10, builder.build());
            list.clear();
        }
        else if(list.size() == 1){
            CharSequence name = "foo";
            String description = "bar";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("10", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, "10")
                    .setSmallIcon(android.R.drawable.btn_star_big_on)
                    .setColor(Color.GREEN)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Sie haben eine Einladung bekommen")
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(10, builder.build());
            list.clear();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
