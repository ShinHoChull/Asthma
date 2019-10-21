package com.m2comm.asthma;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Global;
import com.m2comm.module.M2WebView;
import com.m2comm.module.PDF;
import com.m2comm.voting.Voting;

public class WebPage extends Activity {
    M2WebView webview;
    LinearLayout menu, back;
    SharedPreferences prefs;
    TextView home;
    LinearLayout letter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webview = (M2WebView) findViewById(R.id.webview);
        menu = (LinearLayout) findViewById(R.id.menu);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview != null && webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
        letter = (LinearLayout) findViewById(R.id.letter);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebPage.this, SideMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        webview.loadUrl(intent.getStringExtra("page"));
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        checkPermission();
        getWindow().setWindowAnimations(0);
        NotificationManager nm =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        webview.Setting(this);
        nm.cancel(1);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                if(url.contains("home.html"))
                {
                    Intent intent = new Intent(WebPage.this, Voting.class);
                    startActivity(intent);
                    return true;
                }


                return false;
            }
        });


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("/e_letter/")) {
                    letter.setVisibility(View.VISIBLE);
                }
                else
                    letter.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                letter.setVisibility(View.GONE);
                if(url.contains("voting.php"))
                {
                    Intent intent = new Intent(WebPage.this, Voting.class);
                    startActivity(intent);
                    return true;
                }
                else if(url.contains("back.php"))
                {
                    if (webview != null && webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
                } else if (url.endsWith("close.php"))
                {
                    finish();
                }
                else if(url.contains("2017Spring"))
                {

                    if(prefs.getString("Spring2017_name","").equals("")) {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2017.Spring2017_LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2017.Spring2017_MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if(url.contains("2017Fall"))
                {

                    if(prefs.getString("Fall2017_name","").equals("")) {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Fall2017.Fall2017_IntroActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Fall2017.Fall2017_MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if(url.contains("2018Spring"))
                {

                    if(prefs.getString("Spring2018_name","").equals("")) {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2018.Spring2018_LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2018.Spring2018_MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if(url.contains("2019Spring"))
                {

                    if(prefs.getString("Spring2019_name","").equals("")) {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2019.Spring2019_LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Spring2019.Spring2019_MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if(url.contains("2018Fall"))
                {
                    if(prefs.getString("Fall2018_name","").equals("")) {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Fall2018.Fall2018_LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Fall2018.Fall2018_MainActivity.class);
                        startActivity(intent);
                    }
                    return true;
                } else if(url.contains("2016Fall"))
                {
                    Intent intent = new Intent(WebPage.this, com.m2comm.asthma.Fail2016.Fail2016_MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(url.contains("pdf.php"))
                {
                    String a[] = url.split("file=");
                    String file_url = a[1];

                    new PDF(WebPage.this, Global.URL+file_url);

                    return true;
                }
                else if(url.contains("calendar.php"))
                {

                    Intent intent = new Intent(WebPage.this, CalendarActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if(!url.contains("allergy.or.kr"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                else if ( url.endsWith(".pdf"))
                {
                    if(url.contains("allergy.or.kr/"))
                        new PDF(WebPage.this, url);
                    else
                        new PDF(WebPage.this, Global.URL+url);
                }
                return false;
            }
        });
        final Context myApp = this;


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (webview.getUrl().contains("/e_letter/")) {
            letter.setVisibility(View.VISIBLE);
        }
        else
            letter.setVisibility(View.GONE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview != null && (keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void checkPermission(){

        //권한이 없는 경우
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                //최초 거부를 선택하면 두번째부터 이벤트 발생 & 권한 획득이 필요한 이융를 설명
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }

                //요청 팝업 팝업 선택시 onRequestPermissionsResult 이동
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

            }
            //권한이 있는 경우
            else{

            }
        }
    }
}
