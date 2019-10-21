package com.m2comm.voting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.m2comm.asthma.R;


public class Feedback extends Activity {

    String phoneNumber;
    String name, hospital;
    private WebView webView;
    SharedPreferences prefs;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        getWindow().setWindowAnimations(0);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        name = prefs.getString("id", null);
        hospital = prefs.getString("office", null);
        phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        BottomMenu bottommenu = (BottomMenu) findViewById(R.id.bottommenu);
        bottommenu.setActivity(this);
        bottommenu.setSelectNumber(4);


        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        url = Global.FEED_BACK_URL+phoneNumber;
        if(hospital != null)
            url += "&hospital="+hospital;
        if(name != null)
            url += "&name="+name;

        url += "&language="+prefs.getString("language", "");
        webView.loadUrl(url);


        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String urls,
                                     String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                        webView.loadUrl(url);
                                    }
                                }).setCancelable(false).create().show();

                return true;
            };
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(Feedback.this)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.cancel();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }
        });
    }
}
