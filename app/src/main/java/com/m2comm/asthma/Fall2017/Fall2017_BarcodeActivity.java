package com.m2comm.asthma.Fall2017;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.m2comm.asthma.BarcodeAdapter;
import com.m2comm.asthma.BarcodeClass;
import com.m2comm.asthma.R;
import com.m2comm.module.Global;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fall2017_BarcodeActivity extends Activity implements View.OnClickListener {
    SharedPreferences prefs;
    LinearLayout menu;
    Thread mThread;
    ListView listview;
    BarcodeAdapter barcodeAdapter;
    boolean check =true;
    String data;
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back :
                finish();
                break;
            case R.id.menu:
                intent = new Intent(Fall2017_BarcodeActivity.this, Fall2017_SideMenu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        menu = (LinearLayout) findViewById(R.id.menu);
        menu.setOnClickListener(this);

        listview = (ListView) findViewById(R.id.list);
        barcodeAdapter = new BarcodeAdapter(this, R.layout.barcode_list);
        listview.setAdapter(barcodeAdapter);
    /*
        barcodeAdapter.add(new BarcodeClass("2016.10.13(목)","10:42","10:58","00:16"));
        barcodeAdapter.add(new BarcodeClass("2016.10.2","6544","2534","5166"));
        barcodeAdapter.add(new BarcodeClass("2016.10.3","6544","2534","5166"));
        */
        MultiFormatWriter gen = new MultiFormatWriter();

        TextView barcode_text = (TextView) findViewById(R.id.barcode_text);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        back.setColorFilter(getResources().getColor(R.color.css1));

        barcode_text.setText(prefs.getString("Fall2017_gubun","")+"-"+prefs.getString("Fall2017_sid","")+"A");
        try {
            final int WIDTH = 700;
            final int HEIGHT = 250;
            BitMatrix bytemap = gen.encode(prefs.getString("Fall2017_gubun","")+"-"+prefs.getString("Fall2017_sid","")+"A", BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < WIDTH; ++i)
                for (int j = 0; j < HEIGHT; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i, j) ? Color.BLACK : Color.WHITE);
                }

            ImageView view = (ImageView) findViewById(R.id.barcode_image);
            view.setImageBitmap(bitmap);
            view.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        check=false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        check=true;
        mThread = new Thread() {
            public void run() {
                String urlStr = Global.Fall2017_URL+"get_attendance.php?userid=" + prefs.getString("Fall2017_gubun","")+"-"+prefs.getString("Fall2017_sid","");
                while(check) {
                    try {
                        URL url = new URL(urlStr);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        if (conn != null) {
                            conn.setConnectTimeout(10000);
                            conn.setRequestMethod("GET");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            int resCode = conn.getResponseCode();
                            if (resCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                String line = null;
                                String json = "";
                                while (true) {
                                    line = reader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    json += line;
                                }
                                //Log.d("hgkim","json : " + json);
                                Message msg = Message.obtain();
                                msg.what = 1;
                                msg.obj = json;
                                handle.sendMessage(msg);
                                reader.close();
                                conn.disconnect();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        };
        mThread.start();
    }
    final Handler handle = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what==1)
            {
                try {
                    barcodeAdapter.reset();
                    JSONArray resultList = new JSONArray((String) msg.obj);
                    for(int i=0;i<resultList.length();i++)
                    {

                        barcodeAdapter.add(
                                new BarcodeClass(resultList.getJSONObject(i).getString("day"),
                                        resultList.getJSONObject(i).getString("in_time"),
                                        resultList.getJSONObject(i).getString("out_time"),
                                        resultList.getJSONObject(i).getString("residense_time")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
