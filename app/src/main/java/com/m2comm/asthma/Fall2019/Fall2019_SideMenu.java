package com.m2comm.asthma.Fall2019;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class Fall2019_SideMenu extends Activity implements View.OnClickListener {

    int lastClickedPosition;
    SideMenuAdapter2 sidemenuadapter;
    AnimatedExpandableListView menu_list;
    LinearLayout regi,scrap,setting,home,login,logout,layout1,layout2,barcode;
    String name;
    SharedPreferences prefs;
    ImageView close;
    String code = "all2019f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2019_sidemenu);
        //getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        getWindow().setWindowAnimations(0);
        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter2(this, R.layout.side_menu_item2);
        menu_list.setAdapter(sidemenuadapter);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        /*
        sidemenuadapter.add(new SideMenuClass("Information", Global.URL+"workshop/php/wsInfo.php"));
        sidemenuadapter.add(new SideMenuClass("Program", Global.URL+"workshop/php/program.php?gubun="+prefs.getString("Spring2019_gubun","")+"&user_sid="+prefs.getString("Spring2019_sid","")));
        sidemenuadapter.add(new SideMenuClass("Registration", Global.URL+"workshop/php/mypage.php?gubun="+prefs.getString("Spring2019_gubun","")+"&user_sid="+prefs.getString("Spring2019_sid","")));
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

        if(prefs.getString("Fall2019_gubun","").equals(""))
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
        setting.setVisibility(View.VISIBLE);
        String device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        ArrayList<SubSideMenuClass> subSideMenuClass = new ArrayList<SubSideMenuClass>();
        subSideMenuClass.add(new SubSideMenuClass("- 행사안내", Global.Fall2019_URL + "info/index.php"));
        subSideMenuClass.add(new SubSideMenuClass("- 인사말", Global.Fall2019_URL + "info/infoMess.php"));

        sidemenuadapter.add(new SideMenuClass("행사안내", null, subSideMenuClass));

        subSideMenuClass = new ArrayList<SubSideMenuClass>();
        subSideMenuClass.add(new SubSideMenuClass("- 11월02일 (토)", "http://ezv.kr/voting/php/session/list.php?code="+this.code+"&tab=125&deviceid=" + device));
        subSideMenuClass.add(new SubSideMenuClass("- Program at a Glance", "http://ezv.kr/voting/php/session/glance.php?code="+this.code+"&deviceid=" + device));

        sidemenuadapter.add(new SideMenuClass("프로그램", null, subSideMenuClass));
        sidemenuadapter.add(new SideMenuClass("등록", "regi", null));

        subSideMenuClass = new ArrayList<SubSideMenuClass>();
//        subSideMenuClass.add(new SubSideMenuClass("- 인천 공항", Global.Fall2019_URL + "tran/incheon.php"));
//        subSideMenuClass.add(new SubSideMenuClass("- 김포 공항", Global.Fall2019_URL + "tran/gimpo.php"));
        sidemenuadapter.add(new SideMenuClass("오시는 길", Global.Fall2019_URL + "map.php", null));

        sidemenuadapter.add(new SideMenuClass("학회장 안내", Global.Fall2019_URL + "venue.php", null));

        subSideMenuClass = new ArrayList<SubSideMenuClass>();
        subSideMenuClass.add(new SubSideMenuClass("- 스폰서", Global.Fall2019_URL + "sponsor.php"));
        subSideMenuClass.add(new SubSideMenuClass("- 부스", Global.Fall2019_URL + "booth.php"));
        sidemenuadapter.add(new SideMenuClass("스폰서", Global.Fall2019_URL + null, subSideMenuClass));
        sidemenuadapter.add(new SideMenuClass("공지사항", "http://ezv.kr/voting/php/bbs/list.php?code="+this.code, null));

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
                        if(!prefs.getString("Fall2019_gubun","").equals("")) {
                            intent = new Intent(Fall2019_SideMenu.this, Fall2019_WebPage.class);
                            intent.putExtra("page", Global.Fall2019_URL+"regist/index.php?Fall2019_gubun=" + prefs.getString("Fall2019_gubun","") + "&Fall2019_sid=" + prefs.getString("Fall2019_sid",""));
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            new AlertDialog.Builder(Fall2019_SideMenu.this,R.style.AlertDialogCustom)
                                    .setTitle("대한천식알레르기학회")
                                    .setMessage("Please login")
                                    .setPositiveButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Fall2019_SideMenu.this, Fall2019_LoginActivity.class);
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
                    }
                    else
                    {
                        intent = new Intent(Fall2019_SideMenu.this, Fall2019_WebPage.class);
                        intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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
                    intent = new Intent(Fall2019_SideMenu.this, Fall2019_WebPage.class);
                    intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    HttpAsyncTask question = new HttpAsyncTask(Fall2019_SideMenu.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            Log.d("hgkim","result : " + result);
                            new PDF(Fall2019_SideMenu.this, result);
                        }

                    });
                    Log.d("hgkim",Global.Spring2017_URL + "program_glance.php");
                    question.execute(new HttpParam("url",Global.Fall2019_URL + "program_glance.php"));
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
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_BarcodeActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.regi :
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_WebPage.class);
                intent.putExtra("page", Global.Fall2019_URL+"regist/index.php?Fall2019_gubun=" + prefs.getString("Fall2019_gubun","") + "&Fall2019_sid=" + prefs.getString("Fall2019_sid",""));
                startActivity(intent);
                finish();
                break;

            case R.id.scrap :
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+this.code+"&tab=-2");
                startActivity(intent);
                finish();
                break;

            case R.id.setting :
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.home :
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.login :
                intent = new Intent(Fall2019_SideMenu.this, Fall2019_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.close :
                finish();
                break;
            case R.id.logout :
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("Fall2019_sid", "");
                editor.putString("Fall2019_gubun", "");
                editor.putString("Fall2019_name","");
                editor.putString("Fall2019_email", "");
                editor.commit();

                intent = new Intent(Fall2019_SideMenu.this, Fall2019_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void finish() {
        super.finish();
        //if(aniYN)
        overridePendingTransition(R.anim.stay, R.anim.slide_out_left);

    }

}
