package com.m2comm.asthma.Spring2019;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.m2comm.asthma.ImageViewActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.Alarm;
import com.m2comm.module.M2WebView;
import com.m2comm.module.PDF;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebPopupActivity extends Activity implements View.OnClickListener{
    M2WebView webview;

    SharedPreferences prefs;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s2019_popup_web);
        getWindow().setWindowAnimations(0);
        webview = (M2WebView) findViewById(R.id.webview);
        webview.Setting(this);

        Intent intent = getIntent();
        String add_parm = "";
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        webview.loadUrl(intent.getStringExtra("page") + add_parm);
        getWindow().setWindowAnimations(0);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webview != null && webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("back.php")) {
                    if (webview != null && webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
                } else if (url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg")) {
                    Intent intent = new Intent(WebPopupActivity.this, ImageViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("page", url);
                    startActivity(intent);
                    return true;
                } else if (url.endsWith(".doc") || url.endsWith(".dox")) {
                    webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + url);
                    return true;
                } else if (url.contains("glance.php")) {
            /*
            Intent intent = new Intent(WebActivity.this, Session_Excel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
            */
                } else if (url.contains("add_alarm.php")) {
                    String[] temp = url.split("&");
                    final String sid = temp[0].split("=")[1];
                    final String tab = temp[1].split("=")[1];
                    String time2 = temp[2].split("=")[1];
                    final String time = time2.split("-")[0];
                    final String subject = temp[3].split("=")[1];

                    Alarm alarm = new Alarm(WebPopupActivity.this);
                    int day = 10;

                    if (tab.equals("31")) {
                        day = 10;
                    } else if (tab.equals("32")) {
                        day = 11;
                    }

                    try {
                        if (Integer.parseInt(time.split(":")[1]) < 10) {
                            alarm.InsertAlarm(2019, 4, day, Integer.parseInt(time.split(":")[0]) - 1, Integer.parseInt(time.split(":")[1]) + 50, Integer.parseInt(sid), URLDecoder.decode(subject, "UTF-8"));
                        } else {
                            alarm.InsertAlarm(2019, 4, day, Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1]) - 10, Integer.parseInt(sid), URLDecoder.decode(subject, "UTF-8"));
                        }
                        Toast.makeText(WebPopupActivity.this, "Add Alarm", Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    return true;
                } else if (url.contains("remove_alarm.php")) {
                    String[] temp = url.split("&");
                    final String sid = temp[0].split("=")[1];
                    Alarm alarm = new Alarm(WebPopupActivity.this);
                    alarm.DelAlarm(Integer.parseInt(sid));

                    return true;
                } else if (url.contains("close.php")) {
                    finish();
                    return true;
                } else if (url.endsWith(".pdf")) {
                    if(!prefs.getString("Spring2019_gubun","").equals("")) {
                        new PDF(WebPopupActivity.this, url);
                    }else{
                        new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                                .setTitle(getString(R.string.app_name))
                                .setMessage("로그인하시겠습니까?")
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(WebPopupActivity.this, Spring2019_LoginActivity.class);
                                                intent.putExtra("check", 1);
                                                startActivity(intent);
                                            }
                                        })
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                .create()
                                .show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview != null && (keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        Thread mThread;
        Intent intent;
        switch (v.getId()) {

        }
    }
}
