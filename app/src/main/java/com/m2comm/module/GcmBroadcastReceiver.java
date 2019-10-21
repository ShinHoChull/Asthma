package com.m2comm.module;

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

import com.m2comm.asthma.R;
import com.m2comm.asthma.Spring2019.Spring2019_WebPage;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

        String sid = intent.getExtras().getString("sid");
        String title = intent.getExtras().getString("title");
        String body = intent.getExtras().getString("body");

        Log.d("hgkim","sid : " + sid);
        Log.d("hgkim","title : " + title);



        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent2 = new Intent(context, Spring2019_WebPage.class);
        intent2.putExtra("page", "http://ezv.kr/voting/php/bbs/view.php?code=allergy2019s&sid="+sid);
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



    }
}