package com.m2comm.asthma;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.m2comm.asthma.Fall2018.Fall2018_WebPage;
import com.m2comm.asthma.Spring2017.Spring2017_WebPage;
import com.m2comm.asthma.Spring2019.Spring2019_WebPage;
import com.m2comm.module.Global;

public class GcmIntentService extends IntentService {
    public static final String TAG = "hgkim";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String fileDir;
    public GcmIntentService() {
//        Used to name the worker thread, important only for debugging.
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.d("hgkim","onHandleIntent");
        if (!extras.isEmpty()) {
           if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
               sendNotification(extras.getString("body"), extras.getString("title"), extras.getString("code") , extras.getString("linkurl"));

           }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String body, String title,String code, String linkurl) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent =null;
        if(code.equals("allergy2019s")) {
            intent = new Intent(this, Spring2019_WebPage.class);
            intent.putExtra("page", "http://ezv.kr/voting/php/bbs/view.php?sid="+linkurl);
        }else if(code.equals("2018_Fall")) {
            intent = new Intent(this, Fall2018_WebPage.class);
            intent.putExtra("page", Global.Fall2018_URL+"bbs/view.php?sid="+linkurl);
        }
        else if(code.equals("2017_Fall")) {
            intent = new Intent(this, Spring2017_WebPage.class);
            intent.putExtra("page", Global.Fall2017_URL+"bbs/view.php?sid="+linkurl);
        }
        else if (code.equals("notice"))
        {
            intent = new Intent(this, WebPage.class);
            intent.putExtra("page", Global.URL+"app/php/bbs_view.php?sid="+linkurl);
        } else if (code.equals("guideline"))
        {

        } else if (code.equals("newsletter"))
        {

        } else {
            intent = new Intent(this, MainActivity.class);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d("hgkim","all");
            Notification.Builder builder = new Notification.Builder(this, "all")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
        } else {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name))
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(title))
                            .setContentText(title);
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
        }

    }
}