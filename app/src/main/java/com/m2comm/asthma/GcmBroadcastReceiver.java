package com.m2comm.asthma;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.m2comm.asthma.Fall2018.Fall2018_WebPage;
import com.m2comm.asthma.Spring2017.Spring2017_WebPage;
import com.m2comm.asthma.Spring2019.Spring2019_WebPage;
import com.m2comm.module.Global;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("hgkim", "GcmBroadcastReceiver");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String linkurl = intent.getExtras().getString("linkurl");
        String body = intent.getExtras().getString("body");
        String title = intent.getExtras().getString("title");
        String code = intent.getExtras().getString("code");


        Intent intent2 = null;
        if (code.equals("allergy2019s")) {
            intent2 = new Intent(context, Spring2019_WebPage.class);
            intent2.putExtra("page", "http://ezv.kr/voting/php/bbs/view.php?code=allergy2019s&sid=" + linkurl);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Notification.Builder builder = new Notification.Builder(context, "all")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true);
                builder.setContentIntent(contentIntent);
                mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
            } else {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(body))
                                .setContentText(body);
                builder.setContentIntent(contentIntent);
                mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
            }
            return;
        } else if (code.equals("2018_Fall")) {
            intent2 = new Intent(context, Fall2018_WebPage.class);
            intent2.putExtra("page", Global.Fall2018_URL + "bbs/view.php?sid=" + linkurl);
        } else if (code.equals("2017_Fall")) {
            intent2 = new Intent(context, Spring2017_WebPage.class);
            intent2.putExtra("page", Global.Fall2017_URL + "bbs/view.php?sid=" + linkurl);
        } else if (code.equals("notice")) {
            intent2 = new Intent(context, WebPage.class);
            intent2.putExtra("page", Global.URL + "app/php/bbs_view.php?sid=" + linkurl);
        } else if (code.equals("guideline")) {

        } else if (code.equals("newsletter")) {

        } else {
            intent2 = new Intent(context, MainActivity.class);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(context, "all")
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(context.getString(R.string.app_name))
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(title))
                            .setContentText(title);
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
        }
    }
}