package com.m2comm.asthma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.m2comm.asthma.Fall2017.Fall2017_IntroActivity;
import com.m2comm.asthma.Fall2017.Fall2017_MainActivity;
import com.m2comm.asthma.Fall2018.Fall2018_LoginActivity;
import com.m2comm.asthma.Fall2018.Fall2018_MainActivity;
import com.m2comm.asthma.Fall2019.Fall2019_LoginActivity;
import com.m2comm.asthma.Fall2019.Fall2019_MainActivity;
import com.m2comm.asthma.Spring2018.Spring2018_IntroActivity;
import com.m2comm.asthma.Spring2018.Spring2018_LoginActivity;
import com.m2comm.asthma.Spring2018.Spring2018_MainActivity;
import com.m2comm.asthma.Spring2019.Spring2019_LoginActivity;
import com.m2comm.asthma.Spring2019.Spring2019_MainActivity;
import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.m2comm.asthma.R.id.pager;

public class MainActivity extends Activity implements View.OnClickListener {

    LinearLayout menu, menu1, menu2, menu3, menu4, menu5, menu6, menu7, menu8, menu9,rolling,bg,bg2;
    SharedPreferences prefs;
    String sid, gubun;
    ViewPager mPager;
    PagerAdapterClass mPagerAdapter;
    LoadingDialog loadingDialog;
    boolean isFinish = true;
    Thread mThread;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu = (LinearLayout) findViewById(R.id.menu);
        menu1 = (LinearLayout) findViewById(R.id.menu1);
        menu2 = (LinearLayout) findViewById(R.id.menu2);
        menu3 = (LinearLayout) findViewById(R.id.menu3);
        menu4 = (LinearLayout) findViewById(R.id.menu4);
        menu5 = (LinearLayout) findViewById(R.id.menu5);
        menu6 = (LinearLayout) findViewById(R.id.menu6);
        menu7 = (LinearLayout) findViewById(R.id.menu7);
        menu8 = (LinearLayout) findViewById(R.id.menu8);
        menu9 = (LinearLayout) findViewById(R.id.menu9);

        menu.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
        menu6.setOnClickListener(this);
        menu7.setOnClickListener(this);
        menu8.setOnClickListener(this);
        menu9.setOnClickListener(this);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        id = prefs.getString("id", null);


        bg = (LinearLayout) findViewById(R.id.bg);
        bg.setOnClickListener(this);
        bg2 = (LinearLayout) findViewById(R.id.bg2);
        bg2.setOnClickListener(this);


        mPager = (ViewPager) findViewById(pager);
        mPager.setOffscreenPageLimit(6);

        rolling = (LinearLayout) findViewById(R.id.rolling);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                for(int i=0;rolling.getChildCount()>i;i=i+2) {
                    ImageView a = (ImageView) rolling.getChildAt(i);
                    a.setImageResource(R.drawable.dot_off);
                }
                ImageView a = (ImageView) rolling.getChildAt(position * 2);
                a.setImageResource(R.drawable.dot_on);
            }
            @Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
            @Override public void onPageScrollStateChanged(int state) {}
        });

        mPagerAdapter = new PagerAdapterClass(this);
        mPager.setAdapter(mPagerAdapter);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        HttpAsyncTask question = new HttpAsyncTask(MainActivity.this, new HttpInterface() {
            @Override
            public void onResult(String result) {
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = 1;
                handle.sendMessage(msg);
            }
        });

        question.execute(new HttpParam("url", com.m2comm.module.Global.URL + "app/php/get_main_banner.php"));
    }


    public class PagerAdapterClass extends PagerAdapter {

        ArrayList<pagerInfo> mItems = new ArrayList<pagerInfo>();
        String fileDir;
        Context context;

        public void add(pagerInfo vi) {
            mItems.add(vi);
        }

        public PagerAdapterClass(Context c) {
            super();
            context = c;
            fileDir = c.getFilesDir().getPath() + "/";
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object instantiateItem(View pager, final int position) {
            View v;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.inflate_one, null);
            final pagerInfo vi = mItems.get(position);
            if (vi != null) {

                ImageView image = (ImageView) v.findViewById(R.id.image);

                Glide.with(MainActivity.this).load(Global.URL+"upload/"+vi.imgURL).thumbnail(0.4f).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loadingDialog.dismiss();


                        return false;
                    }
                }).into(image);



                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

//                        if(prefs.getString("Fall2019_name","").equals("")) {
//                            Intent intent = new Intent(MainActivity.this, Fall2019_LoginActivity.class);
//                            startActivity(intent);
//                        }
//                        else
//                        {
//                            Intent intent = new Intent(MainActivity.this, Fall2019_MainActivity.class);
//                            startActivity(intent);
//                        }

                        if (vi.linkURL.contains("http://")) {

                            Intent intent = new Intent(MainActivity.this, WebPage.class);
                            intent.putExtra("page", vi.linkURL);
                            startActivity(intent);

                        } else if(vi.linkURL.contains("Spring2017")) {

                            if(prefs.getString("Fall2017_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Fall2017_IntroActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Fall2017_MainActivity.class);
                                startActivity(intent);
                            }
                        } else if(vi.linkURL.contains("Fall2017")) {

                            if(prefs.getString("Fall2017_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Fall2017_IntroActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Fall2017_MainActivity.class);
                                startActivity(intent);
                            }
                        } else if(vi.linkURL.contains("Spring2018")) {

                            if(prefs.getString("Spring2018_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Spring2018_LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Spring2018_MainActivity.class);
                                startActivity(intent);
                            }
                        }else if(vi.linkURL.contains("Fall2018")) {

                            if(prefs.getString("Fall2018_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Fall2018_LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Fall2018_MainActivity.class);
                                startActivity(intent);
                            }
                        }else if(vi.linkURL.contains("Spring2019")) {

                            if(prefs.getString("Spring2019_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Spring2019_LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Spring2019_MainActivity.class);
                                startActivity(intent);
                            }
                        } else if(vi.linkURL.contains("Fall2019")) {

                            if(prefs.getString("Fall2019_name","").equals("")) {
                                Intent intent = new Intent(MainActivity.this, Spring2019_LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(MainActivity.this, Spring2019_MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });


            }
            ((ViewPager) pager).addView(v, 0);
            return v;
        }


        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

    public class pagerInfo implements Serializable {
        public String imgURL;
        public String linkURL;
        public ImageView imageview;
        public Bitmap bm;

        public pagerInfo() {

        }

        public void set(JSONObject object) {
            try {

                if (object != null) {
                    imgURL = object.getString("userfile3");
                    linkURL = object.getString("link");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public final Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String info = (String) msg.obj;


                try {
                    JSONArray bannerList = new JSONArray(info);
                    for (int i = 0; i < bannerList.length(); i++) {
                        pagerInfo vi = new pagerInfo();
                        vi.set(bannerList.getJSONObject(i));
                        mPagerAdapter.add(vi);

                        LinearLayout margin = new LinearLayout(getApplicationContext());
                        margin.setPadding(2,2,2,2);
                        ImageView layout = new ImageView(getApplicationContext());
                        if(i==0)
                            layout.setImageResource(R.drawable.dot_on);
                        else
                            layout.setImageResource(R.drawable.dot_off);
                        layout.setId(i);
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("hgkim","view.getId() : " + view.getId());
                                mPager.setCurrentItem(view.getId());
                            }
                        });
                        rolling.addView(layout);
                        rolling.addView(margin);
                        layout.getLayoutParams().height=25;


                    }
                } catch (JSONException e) {

                }

                mPagerAdapter.notifyDataSetChanged();

                mThread = new Thread() {
                    public void run() {
                        while(true) {
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {

                            }
                            if(isFinish) {

                                Message msg = Message.obtain();
                                msg.what = 3;
                                handle.sendMessage(msg);


                            }
                        }
                    }
                };
                mThread.start();

            }
            else if(msg.what==3) {
                if(mPager.getCurrentItem()>=mPagerAdapter.getCount()-1)
                    mPager.setCurrentItem(0);
                else
                    mPager.setCurrentItem(mPager.getCurrentItem()+1);

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isFinish=false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        isFinish=true;

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Uri uri;
        switch (v.getId()) {
            case R.id.menu:
                intent = new Intent(MainActivity.this, SideMenu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.menu1:

                intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/message.php");
                startActivity(intent);

                /*
                if(prefs.getString("Fall2017_name","").equals("")) {
                    intent = new Intent(MainActivity.this, Fall2017_IntroActivity.class);
                    startActivity(intent);
                }
                else
                {
                    intent = new Intent(MainActivity.this, Fall2017_MainActivity.class);
                    startActivity(intent);
                }
                */
                break;
            case R.id.menu2:
                intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/bbs_list.php");
                startActivity(intent);

                break;
            case R.id.menu3:
                intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/guide.php");
                startActivity(intent);

                break;
            case R.id.menu4:
                intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/event.php");
                startActivity(intent);
                break;
            case R.id.menu5:
                if(id != null) {
                    intent = new Intent(MainActivity.this, WebPage.class);
                    intent.putExtra("page", Global.URL + "app/php/education.php");
                    startActivity(intent);
                }
                else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("check", 1);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"회원만 이용이 가능합니다",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu6:
                intent = new Intent(MainActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/newsletter_list.php");
                startActivity(intent);
                break;

            case R.id.menu7:
                uri = Uri.parse("http://www.aard.or.kr");
                intent  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;

            case R.id.menu8:
                uri = Uri.parse("http://www.e-aair.org");
                intent  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;

            case R.id.menu9:
                if(id != null) {
                    intent = new Intent(MainActivity.this, WebPage.class);
                    intent.putExtra("page", Global.URL + "app/php/pds.php");
                    startActivity(intent);
                }
                else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("check", 1);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"회원만 이용이 가능합니다",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
