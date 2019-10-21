package com.m2comm.asthma.Fall2019;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.asthma.ImageViewActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.Alarm;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.M2WebView;
import com.m2comm.module.PDF;
import com.m2comm.voting.Voting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Fall2019_WebPage extends Activity {
    M2WebView webview;
    LinearLayout menu, top_menu, days, day_type1, day_type2, bottom_layout;
    TextView top_menu_txt, month;
    ImageView voting, back;
    int day_type = 0;
    int bottom_menu = 0;
    int tab=0;
    LinearLayout glance_overlay;
    boolean glance = false;
    String device;
    SharedPreferences prefs;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2019_web);
        getWindow().setWindowAnimations(0);
        webview = (M2WebView) findViewById(R.id.webview);
        menu = (LinearLayout) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_SideMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });


        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        Intent intent = getIntent();
        String add_parm="";
        device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if(intent.getStringExtra("page").contains("?")){
            add_parm="&deviceid="+device;
        }else{
            add_parm="?deviceid="+device;
        }
        webview.loadUrl(intent.getStringExtra("page")+add_parm);
        Log.d("hgkim",intent.getStringExtra("page")+add_parm);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview != null  && webview.canGoBack()) {
                    webview.goBack();
                }else{
                    finish();
                }
            }
        });
        glance_overlay = (LinearLayout) findViewById(R.id.glance_overlay);
        glance_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glance_overlay.setVisibility(View.GONE);
            }
        });
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        top_menu = (LinearLayout) findViewById(R.id.top_menu);
        days = (LinearLayout) findViewById(R.id.days);
        top_menu_txt = (TextView) findViewById(R.id.top_menu_txt);
        month = (TextView) findViewById(R.id.month);

        day_type1 = (LinearLayout) findViewById(R.id.day_type1);
        day_type2 = (LinearLayout) findViewById(R.id.day_type2);
        tab = intent.getIntExtra("tab",0);
        if(intent.getStringExtra("page").contains("session/glance"))
            glance = true;


        TextView home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        voting = (ImageView) findViewById(R.id.voting);
        voting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fall2019_WebPage.this, Voting.class);
                startActivity(intent);
            }
        });

        checkPermission();

        HttpAsyncTask question = new HttpAsyncTask(Fall2019_WebPage.this, new HttpInterface() {
            @Override
            public void onResult(String result) {

                try {
                    JSONObject resultList = new JSONObject(result);

                    top_menu.setBackgroundColor(Color.parseColor("#"+resultList.getString("session_topmenu_bg")));
                    top_menu_txt.setTextColor(Color.parseColor("#"+resultList.getString("session_topmenu_font")));
                    top_menu.setVisibility(View.VISIBLE);
                    month.setText(resultList.getString("mon"));
                    day_type = resultList.getInt("day_type");
                    bottom_menu = resultList.getInt("bottom_menu");
                    LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);
                    lparam.setMargins(1, 0, 0, 0);
                    if(bottom_menu==1 || bottom_menu==2) {
                        bottom_layout.setVisibility(View.VISIBLE);
                        ImageView sub_layout = new ImageView(getApplicationContext());
                        bottom_layout.addView(sub_layout);
                        sub_layout.setLayoutParams(lparam);
                        sub_layout.setImageResource(R.drawable.bottom_menu01);
                        sub_layout.setBackgroundColor(Color.parseColor("#45455c"));
                        sub_layout.setPadding(50,5,50,5);

                        sub_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code=allergy2019s&tab=-1&deviceid=" + device);
                                startActivity(intent);
                                finish();
                            }
                        });


                        ImageView sub_layout2 = new ImageView(getApplicationContext());
                        bottom_layout.addView(sub_layout2);
                        sub_layout2.setLayoutParams(lparam);
                        sub_layout2.setImageResource(R.drawable.bottom_menu02);
                        sub_layout2.setBackgroundColor(Color.parseColor("#45455c"));
                        sub_layout2.setPadding(50,5,50,5);

                        sub_layout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code=allergy2019s&deviceid=" + device);
                                startActivity(intent);
                                finish();
                            }
                        });


                        ImageView sub_layout3 = new ImageView(getApplicationContext());
                        bottom_layout.addView(sub_layout3);
                        sub_layout3.setLayoutParams(lparam);
                        sub_layout3.setImageResource(R.drawable.bottom_menu03);
                        sub_layout3.setBackgroundColor(Color.parseColor("#45455c"));
                        sub_layout3.setPadding(50,5,50,5);

                        sub_layout3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code=allergy2019s&tab=-2&deviceid=" + device);
                                startActivity(intent);
                                finish();
                            }
                        });

                        if(bottom_menu==2) {

                            ImageView sub_layout4 = new ImageView(getApplicationContext());
                            bottom_layout.addView(sub_layout4);
                            sub_layout4.setLayoutParams(lparam);
                            sub_layout4.setImageResource(R.drawable.bottom_menu02);
                            sub_layout4.setBackgroundColor(Color.parseColor("#45455c"));
                            sub_layout4.setPadding(50,5,50,5);

                            sub_layout4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                    intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code=allergy2019s&tab=-5&deviceid=" + device);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        }
                    }

                    final JSONArray day_info = new JSONArray(resultList.getString("day"));

                    if(day_type==1) {
                        day_type1.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams lparam2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);


                        for(int i=0;i<day_info.length();i++) {
                            final int finalI = i;
                            TextView day_text = new TextView(getApplicationContext());
                            day_type1.addView(day_text);
                            day_text.setLayoutParams(lparam2);
                            day_text.setTextSize(16);


                            if(day_info.getJSONObject(i).getInt("tab") == resultList.getInt("tab")) {
                                day_text.setTextColor(Color.parseColor("#"+resultList.getString("menu_font_on")));
                                day_text.setBackgroundColor(Color.parseColor("#"+resultList.getString("menu_bg_on")));
                            }else{
                                day_text.setTextColor(Color.parseColor("#"+resultList.getString("menu_font")));
                                day_text.setBackgroundColor(Color.parseColor("#"+resultList.getString("menu_bg")));
                            }
                            day_text.setText(day_info.getJSONObject(i).getString("name"));
                            day_text.setGravity(Gravity.CENTER);

                            day_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                    try {
                                        intent.putExtra("tab", day_info.getJSONObject(finalI).getInt("tab"));
                                        intent.putExtra("page", "http://ezv.kr/voting/php/session/glance.php?code=allergy2019s&tab="+day_info.getJSONObject(finalI).getInt("tab")+"&deviceid=" + device);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }

                    else if(day_type==2) {
                        day_type2.setVisibility(View.VISIBLE);
                        for(int i=0;i<day_info.length();i++) {

                            LinearLayout day_layout = new LinearLayout(getApplicationContext());
                            days.addView(day_layout);

                            day_layout.getLayoutParams().width = dpToPx(40);
                            day_layout.getLayoutParams().height = dpToPx(42);
                            day_layout.setGravity(Gravity.CENTER);
                            day_layout.setOrientation(LinearLayout.VERTICAL);




                            TextView day_text = new TextView(getApplicationContext());
                            day_layout.addView(day_text);

                            day_text.setGravity(Gravity.CENTER);
                            day_text.setTextSize(10);
                            day_text.setText(day_info.getJSONObject(i).getString("week"));
                            day_text.setTypeface(null, Typeface.BOLD);
                            day_text.setTextColor(Color.parseColor("#282828"));

                            FrameLayout temp = new FrameLayout(getApplicationContext());
                            day_layout.addView(temp);
                            temp.getLayoutParams().width = dpToPx(25);
                            temp.setPadding(0,5,0,0);

                            ImageView day_bg = new ImageView(getApplicationContext());
                            temp.addView(day_bg);

                            day_bg.getLayoutParams().width = dpToPx(25);
                            day_bg.getLayoutParams().height = dpToPx(25);
                            day_bg.setImageResource(R.drawable.cir);
                            day_bg.setColorFilter(Color.parseColor("#e3e5ec"));




                            TextView day_text2 = new TextView(getApplicationContext());
                            temp.addView(day_text2);

                            day_text2.getLayoutParams().width = dpToPx(25);
                            day_text2.getLayoutParams().height = dpToPx(25);
                            day_text2.setGravity(Gravity.CENTER);
                            day_text2.setTextSize(13);
                            day_text2.setText(day_info.getJSONObject(i).getString("day"));
                            if(day_info.getJSONObject(i).getInt("tab")==-1){
                                day_text2.setTextColor(Color.parseColor("#9fa1a9"));
                            }else {


                                final int finalI = i;
                                day_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPage.class);
                                        try {
                                            Log.d("hgkim","http://ezv.kr/voting/php/session/glance.php?code=allergy2019s&tab="+day_info.getJSONObject(finalI).getInt("tab")+"&deviceid=" + device);
                                            intent.putExtra("page", "http://ezv.kr/voting/php/session/glance.php?code=allergy2019s&tab="+day_info.getJSONObject(finalI).getInt("tab")+"&deviceid=" + device);
                                            intent.putExtra("tab", day_info.getJSONObject(finalI).getInt("tab"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(intent);
                                        finish();
                                    }
                                });




                                day_text2.setTypeface(null, Typeface.BOLD);
                                if(day_info.getJSONObject(i).getInt("tab") == resultList.getInt("tab")) {
                                    day_text2.setTextColor(Color.parseColor("#ffffff"));
                                    day_bg.setColorFilter(Color.parseColor("#" + resultList.getString("session_day_bg")));
                                }else {
                                    day_text2.setTextColor(Color.parseColor("#282828"));
                                }
                            }







                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }

        });
        if(glance) {
            glance_overlay.setVisibility(View.VISIBLE);
            voting.setVisibility(View.GONE);
            question.execute(new HttpParam("url", "http://ezv.kr/voting/php/session/get_set.php"),
                    new HttpParam("tab",""+tab),
                    new HttpParam("code", "allergy2019s"));
        }

        NotificationManager nm =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        nm.cancel(1);

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:document.body.style.marginBottom=\"8%\"; void 0");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("hgkim","url : " + url);
                if(url.contains("voting.php"))
                {
                    Intent intent = new Intent(Fall2019_WebPage.this, Voting.class);
                    startActivity(intent);
                    return false;
                }
                else if(url.contains("back.php"))
                {
                    if (webview != null && webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    return true;
                }else if(url.contains("glance_sub.php")){
                    Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_WebPopupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("page",url);
                    startActivity(intent);

                    return true;
                } else if (url.contains("confirm.php"))
                {
                    String a[] = url.split("confirm.php");
                    String file_url = a[1];
                    file_url = file_url.substring(1);
                    file_url = file_url.replace("%20"," ");
                    new AlertDialog.Builder(view.getContext())
                            .setTitle(getString(R.string.app_name))
                            .setMessage(file_url)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_LoginActivity.class);
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
                    return true;
                }
                else if(url.contains("close.php"))
                {
                    finish();
                    return true;
                }else if(url.contains("add_alarm.php")){
                    String[] temp= url.split("&");
                    final String sid = temp[0].split("=")[1];
                    final String tab = temp[1].split("=")[1];
                    String time2 = temp[2].split("=")[1];
                    final String time = time2.split("-")[0];
                    final String subject = temp[3].split("=")[1];

                    Alarm alarm = new Alarm(Fall2019_WebPage.this);
                    int day = 10;

                    if(tab.equals("31")){
                        day=10;
                    } else if(tab.equals("32")) {
                        day = 11;
                    }

                    try {
                        if(Integer.parseInt(time.split(":")[1])<10){
                            alarm.InsertAlarm(2019, 4, day, Integer.parseInt(time.split(":")[0])-1, Integer.parseInt(time.split(":")[1])+50, Integer.parseInt(sid), URLDecoder.decode(subject,"UTF-8"));
                        } else {
                            alarm.InsertAlarm(2019, 4, day, Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1])-10, Integer.parseInt(sid), URLDecoder.decode(subject,"UTF-8"));
                        }
                        Toast.makeText(Fall2019_WebPage.this,"Add Alarm",Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    return true;
                }else if(url.contains("remove_alarm.php")){
                    String[] temp= url.split("&");
                    final String sid = temp[0].split("=")[1];
                    Alarm alarm = new Alarm(Fall2019_WebPage.this);
                    alarm.DelAlarm(Integer.parseInt(sid));

                    return true;
                }else if(!url.contains("allergy.or.kr") && !url.contains("ezv.kr")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }else if(url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".jpeg")){
                    Intent intent = new Intent(Fall2019_WebPage.this, ImageViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("page",url);
                    startActivity(intent);
                    return true;
                }
                /*
                else if(!url.contains("allergy.or.kr"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }*/
                else if (url.endsWith(".pdf"))
                {
                    if(!prefs.getString("Spring2019_gubun","").equals("")) {
                        new PDF(Fall2019_WebPage.this, url);
                    }else{
                        new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                                .setTitle(getString(R.string.app_name))
                                .setMessage("로그인하시겠습니까?")
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Fall2019_WebPage.this, Fall2019_LoginActivity.class);
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
        final Context myApp = this;


        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
            {
                new AlertDialog.Builder(myApp,R.style.AlertDialogCustom)
                        .setTitle("대한천식알레르기학회")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            };

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
                new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                        .setTitle("대한천식알레르기학회")
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
                        .create()
                        .show();
                return true;
            };

        });
    }

    private int dpToPx(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
        return px;
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
