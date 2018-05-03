package com.gmail.paulovitormelila.donaldsmarket;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationService extends Service {
    public Handler handler = null;
    public static Runnable runnable = null;

    private final String CHANNEL_ID = "CHANNEL ID";
    private final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    // TODO: Find a way to restart service when device is rebooted
    @Override
    public void onCreate() {
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                // delayMillis has to be set for every 24 hours = 86400000

                if (isThirdThursday()) {
                    createNotification();
                }

                handler.postDelayed(runnable, 86400000);
            }
        };

        handler.postDelayed(runnable, 86400000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void createNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Specials", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.dm_logo_round)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private boolean isThirdThursday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3;
    }
}
