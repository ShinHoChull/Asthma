package com.m2comm.asthma;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.m2comm.asthma.Fall2017.Fall2017_MainActivity;
import com.m2comm.asthma.Fall2018.Fall2018_MainActivity;
import com.m2comm.asthma.Spring2019.Spring2019_IntroActivity;
import com.m2comm.asthma.Spring2019.Spring2019_LoginActivity;
import com.m2comm.asthma.Spring2019.Spring2019_MainActivity;
import com.m2comm.module.Check;
import com.m2comm.module.Gcm;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;

public class IntroActivity extends Activity  {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("all", "all", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        Gcm gcm = new Gcm(this);
        gcm.ConnectGCM();
        Check Check = new Check(this);
        if (Check.VerCheck()) {
            Handler handler = new Handler();
            handler.postDelayed(new goMain(), 1000);
        }

    }


    private class goMain implements Runnable {
        @Override
        public void run() {

            String phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            HttpAsyncTask question = new HttpAsyncTask(IntroActivity.this, new HttpInterface() {
                @Override
                public void onResult(String result) {


                    Intent intent;
                    /*
                    if(prefs.getString("isLogin","").equals(""))
                        intent = new Intent(IntroActivity.this, LoginActivity.class);
                    else
                        intent = new Intent(IntroActivity.this, MainActivity.class);
                    */
                    intent = new Intent(IntroActivity.this, MainActivity.class);


                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();



                }

            });

            Log.d("hgkim",prefs.getString("registration_id", ""));
            question.execute(new HttpParam("url",com.m2comm.module.Global.URL + "app/add_push.php"),
                    new HttpParam("device","android"),
                    new HttpParam("id","" + phoneNumber),
                    new HttpParam("token",prefs.getString("registration_id", "")));
        };
    }
}
