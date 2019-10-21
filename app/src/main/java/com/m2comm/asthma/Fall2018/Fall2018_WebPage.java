package com.m2comm.asthma.Fall2018;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.asthma.R;
import com.m2comm.module.M2WebView;
import com.m2comm.module.PDF;
import com.m2comm.voting.Voting;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Fall2018_WebPage extends Activity {
    M2WebView webview;
    LinearLayout menu;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2018_web);
        webview = (M2WebView) findViewById(R.id.webview);
        menu = (LinearLayout) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fall2018_WebPage.this, Fall2018_SideMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        Intent intent = getIntent();
        webview.loadUrl(intent.getStringExtra("page"));
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        TextView home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fall2018_WebPage.this, Fall2018_MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        checkPermission();

        NotificationManager nm =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        nm.cancel(1);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("hgkim","url : " + url);
                if(url.contains("voting.php"))
                {
                    Intent intent = new Intent(Fall2018_WebPage.this, Voting.class);
                    startActivity(intent);
                    return true;
                }
                else if(url.contains("back.php"))
                {
                    if (webview != null && webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        Intent intent = new Intent(Fall2018_WebPage.this, Fall2018_MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                } else if (url.contains("confirm.php"))
                {
                    String a[] = url.split("confirm.php");
                    String file_url = a[1];
                    file_url = file_url.substring(1);
                    try {
                        file_url = URLDecoder.decode(file_url,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                            .setTitle(getString(R.string.app_name))
                            .setMessage(file_url)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Fall2018_WebPage.this, Fall2018_LoginActivity.class);
                                            intent.putExtra("check", 1);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                            .create();
                    dialog.show();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                    layoutParams.copyFrom(dialog.getWindow().getAttributes());

                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                    dialog.getWindow().setAttributes(layoutParams);
                    return true;

                    /*
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                            .setTitle(getString(R.string.app_name))
                            .setMessage(file_url)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Fall2018_WebPage.this, Fall2018_LoginActivity.class);
                                            intent.putExtra("check", 1);
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                            .create();
                    dialog.show();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog.getWindow().setAttributes(layoutParams);
                    return true;
                    */
                }
                else if(url.contains("close.php"))
                {
                    finish();
                    return true;
                }
                else if(!url.contains("allergy.or.kr"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                else if (url.endsWith(".pdf"))
                {
                    new PDF(Fall2018_WebPage.this, url);
                }
                return false;
            }
        });
        final Context myApp = this;


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
            {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })

                        .create();
                dialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.getWindow().setAttributes(layoutParams);
                return true;

            };

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
                AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .create();
                dialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.getWindow().setAttributes(layoutParams);
                return true;
            };

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
