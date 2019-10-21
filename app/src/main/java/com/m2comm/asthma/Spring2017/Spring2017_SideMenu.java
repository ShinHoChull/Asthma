package com.m2comm.asthma.Spring2017;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.m2comm.asthma.Barcode;
import com.m2comm.asthma.R;
import com.m2comm.module.AnimatedExpandableListView;
import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.PDF;
import com.m2comm.module.SideMenuAdapter2;
import com.m2comm.module.SideMenuClass;
import com.m2comm.module.SubSideMenuClass;

import java.util.ArrayList;

public class Spring2017_SideMenu extends Activity implements View.OnClickListener {

    int lastClickedPosition;
    SideMenuAdapter2 sidemenuadapter;
    AnimatedExpandableListView menu_list;
    LinearLayout regi,scrap,setting,home,login,logout,layout1,layout2,barcode;
    String name;
    SharedPreferences prefs;
    ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spring2017_sidemenu);
        //getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        getWindow().setWindowAnimations(0);
        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter2(this, R.layout.side_menu_item2);
        menu_list.setAdapter(sidemenuadapter);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        /*
        sidemenuadapter.add(new SideMenuClass("Information", Global.URL+"workshop/php/wsInfo.php"));
        sidemenuadapter.add(new SideMenuClass("Program", Global.URL+"workshop/php/program.php?gubun="+prefs.getString("Spring2017_gubun","")+"&user_sid="+prefs.getString("Spring2017_sid","")));
        sidemenuadapter.add(new SideMenuClass("Registration", Global.URL+"workshop/php/mypage.php?gubun="+prefs.getString("Spring2017_gubun","")+"&user_sid="+prefs.getString("Spring2017_sid","")));
        sidemenuadapter.add(new SideMenuClass("Abstract", Global.URL+"workshop/php/venue.php"));
        sidemenuadapter.add(new SideMenuClass("Venue/Transportation", Global.URL+"workshop/php/location_01.php"));
        sidemenuadapter.add(new SideMenuClass("Sponsorship", Global.URL+"workshop/php/sponsor.php"));
        sidemenuadapter.add(new SideMenuClass("Notice", Global.URL+"workshop/php/bbs/list.php"));
        */

        regi = (LinearLayout) findViewById(R.id.regi);
        scrap = (LinearLayout) findViewById(R.id.scrap);
        setting = (LinearLayout) findViewById(R.id.setting);
        home = (LinearLayout) findViewById(R.id.home);
        login = (LinearLayout) findViewById(R.id.login);
        close = (ImageView) findViewById(R.id.close);
        logout = (LinearLayout) findViewById(R.id.logout);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        logout.setOnClickListener(this);
        close.setOnClickListener(this);
        regi.setOnClickListener(this);
        setting.setOnClickListener(this);
        home.setOnClickListener(this);
        login.setOnClickListener(this);
        scrap.setOnClickListener(this);
        barcode = (LinearLayout) findViewById(R.id.barcode);
        barcode.setOnClickListener(this);

        if(prefs.getString("Spring2017_gubun","").equals(""))
        {
            logout.setVisibility(View.INVISIBLE);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
        else
        {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }

        String device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ArrayList<SubSideMenuClass> subSideMenuClass = new ArrayList<SubSideMenuClass>();
        subSideMenuClass.add(new SubSideMenuClass("· Information", Global.Spring2017_URL + "wsInfo.php"));
        subSideMenuClass.add(new SubSideMenuClass("· Invitation", Global.Spring2017_URL + "message.php"));
        sidemenuadapter.add(new SideMenuClass("Information",null,subSideMenuClass));

        ArrayList<SubSideMenuClass> subSideMenuClass4 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass4.add(new SubSideMenuClass("· May 11 (Thu)",Global.Spring2017_URL + "program.php?tab=1&spring2017_gubun="+prefs.getString("Spring2017_gubun","")+"&spring2017_sid="+prefs.getString("Spring2017_sid","")));
        subSideMenuClass4.add(new SubSideMenuClass("· May 12 (Fri)",Global.Spring2017_URL + "program.php?tab=2&spring2017_gubun="+prefs.getString("Spring2017_gubun","")+"&spring2017_sid="+prefs.getString("Spring2017_sid","")));
        subSideMenuClass4.add(new SubSideMenuClass("· May 13 (Sat)",Global.Spring2017_URL + "program.php?tab=3&spring2017_gubun="+prefs.getString("Spring2017_gubun","")+"&spring2017_sid="+prefs.getString("Spring2017_sid","")));

        sidemenuadapter.add(new SideMenuClass("Program",null,subSideMenuClass4));
        sidemenuadapter.add(new SideMenuClass("Program at a glance","etc",null));
        sidemenuadapter.add(new SideMenuClass("Registration","regi",null));

        ArrayList<SubSideMenuClass> subSideMenuClass2 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass2.add(new SubSideMenuClass("· Incheon Airport",Global.Spring2017_URL+"location_01.php"));
        subSideMenuClass2.add(new SubSideMenuClass("· Gimpo Airport",Global.Spring2017_URL + "location_02.php"));
        sidemenuadapter.add(new SideMenuClass("Transportation",null,subSideMenuClass2));
        sidemenuadapter.add(new SideMenuClass("Venue",Global.Spring2017_URL + "venue.php",null));

        ArrayList<SubSideMenuClass> subSideMenuClass3 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass3.add(new SubSideMenuClass("· Sponsorship",Global.Spring2017_URL+"sponsor.php"));
        subSideMenuClass3.add(new SubSideMenuClass("· Booth",Global.Spring2017_URL + "booth.php"));
        sidemenuadapter.add(new SideMenuClass("Sponsorship",null,subSideMenuClass3));


        sidemenuadapter.add(new SideMenuClass("Notice",Global.Spring2017_URL + "bbs/list.php",null));

        menu_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                Boolean isExpand = (!menu_list.isGroupExpanded(groupPosition));
                if(menu_list.isGroupExpanded(lastClickedPosition))
                    menu_list.collapseGroupWithAnimation(lastClickedPosition);
                menu_list.setSelection(groupPosition);
                if (sidemenuadapter.SideMenuList.get(groupPosition).SubSideMenuList == null) {
                    Intent intent;
                    if(sidemenuadapter.getGroup(groupPosition).url.equals("regi"))
                    {
                        if(!prefs.getString("Spring2017_gubun","").equals("")) {
                            intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                            intent.putExtra("page", Global.Spring2017_URL+"mypage.php?spring2017_gubun=" + prefs.getString("Spring2017_gubun","") + "&spring2017_sid=" + prefs.getString("Spring2017_sid",""));
                            startActivity(intent);
                        }
                        else
                        {
                            intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                            intent.putExtra("page", Global.Spring2017_URL+"regist/regist.php?spring2017_gubun=" + prefs.getString("Spring2017_gubun","") + "&spring2017_sid=" + prefs.getString("Spring2017_sid",""));
                            startActivity(intent);
                        }
                    }
                    else if(!sidemenuadapter.getGroup(groupPosition).url.equals("etc"))
                    {
                        intent = new Intent(Spring2017_SideMenu.this,Spring2017_WebPage.class);
                        intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        HttpAsyncTask question = new HttpAsyncTask(Spring2017_SideMenu.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            Log.d("hgkim","result : " + result);
                            new PDF(Spring2017_SideMenu.this, result);
                        }

                    });
                        Log.d("hgkim",Global.Spring2017_URL + "php/program_glance.php");
                        question.execute(new HttpParam("url",Global.Spring2017_URL + "program_glance.php"));
                    }

                    return true;
                }
                if (isExpand) {
                    menu_list.expandGroupWithAnimation(groupPosition);
                }
                lastClickedPosition = groupPosition;
                menu_list.setSelection(groupPosition);
                return true;

            }
        });

        menu_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent;
                if(!sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url.equals("etc"))
                {
                    intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                    intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.barcode :
                intent = new Intent(Spring2017_SideMenu.this, Barcode.class);
                startActivity(intent);
                break;

            case R.id.regi :

                if(!prefs.getString("Spring2017_gubun","").equals("")) {
                    intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                    intent.putExtra("page", Global.Spring2017_URL+"mypage.php?spring2017_gubun=" + prefs.getString("Spring2017_gubun","") + "&spring2017_sid=" + prefs.getString("Spring2017_sid",""));
                    startActivity(intent);
                }
                else
                {
                    intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                    intent.putExtra("page", Global.Spring2017_URL+"regist/regist.php?spring2017_gubun=" + prefs.getString("Spring2017_gubun","") + "&spring2017_sid=" + prefs.getString("Spring2017_sid",""));
                    startActivity(intent);
                }
                break;

            case R.id.scrap :
                intent = new Intent(Spring2017_SideMenu.this, Spring2017_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", Global.Spring2017_URL+"my_schedule.php?spring2017_gubun="+prefs.getString("Spring2017_gubun","")+"&spring2017_sid="+prefs.getString("Spring2017_sid",""));
                startActivity(intent);
                break;
            case R.id.setting :
                intent = new Intent(Spring2017_SideMenu.this, Spring2017_SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.home :
                intent = new Intent(Spring2017_SideMenu.this, Spring2017_MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.login :
                intent = new Intent(Spring2017_SideMenu.this, Spring2017_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                break;
            case R.id.close :
                finish();
                break;
            case R.id.logout :
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("Spring2017_sid", "");
                editor.putString("Spring2017_gubun", "");
                editor.putString("Spring2017_name","");
                editor.putString("Spring2017_email", "");
                editor.commit();

                intent = new Intent(Spring2017_SideMenu.this, Spring2017_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
        //overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
    }

}
