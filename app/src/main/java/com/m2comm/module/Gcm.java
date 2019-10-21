package com.m2comm.module;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class Gcm {
    Context context;
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    public String PROPERTY_REG_ID = "registration_id";
    public String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "1073814458743";
    String regid;
    public Gcm (Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences("m2comm", context.MODE_PRIVATE);
    }

    public void ConnectGCM()
    {
        if (checkPlayServices())
        {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId();
            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }

    private String getRegistrationId() {
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {
                    regid = gcm.register(SENDER_ID);
                    storeRegistrationId(regid);
                } catch (IOException ex) {

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }

        }.execute(null, null, null);
    }

    private void storeRegistrationId(final String regid) {
        int appVersion = getAppVersion();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

            }
            return false;
        }
        return true;
    }
}
