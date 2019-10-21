package com.m2comm.asthma.Fall2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.m2comm.asthma.R;
import com.m2comm.module.AnimatedExpandableListView;
import com.m2comm.module.Global;
import com.m2comm.module.SideMenuAdapter2;
import com.m2comm.module.SideMenuClass;
import com.m2comm.module.SubSideMenuClass;

import java.util.ArrayList;

public class Fall2018_SideMenu extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.fall2018_sidemenu);
        //getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        getWindow().setWindowAnimations(0);
        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter2(this, R.layout.side_menu_item2);
        menu_list.setAdapter(sidemenuadapter);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        /*
        sidemenuadapter.add(new SideMenuClass("Information", Global.URL+"workshop/php/wsInfo.php"));
        sidemenuadapter.add(new SideMenuClass("Program", Global.URL+"workshop/php/program.php?gubun="+prefs.getString("Fall2018_gubun","")+"&user_sid="+prefs.getString("Fall2018_sid","")));
        sidemenuadapter.add(new SideMenuClass("Registration", Global.URL+"workshop/php/mypage.php?gubun="+prefs.getString("Fall2018_gubun","")+"&user_sid="+prefs.getString("Fall2018_sid","")));
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

        if(prefs.getString("Fall2018_gubun","").equals(""))
        {
            setting.setVisibility(View.INVISIBLE);
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
        subSideMenuClass.add(new SubSideMenuClass("- 행사안내", Global.Fall2018_URL + "info/index.php"));
        subSideMenuClass.add(new SubSideMenuClass("- 인사말", Global.Fall2018_URL + "info/infoMess.php"));

        sidemenuadapter.add(new SideMenuClass("행사안내", null, subSideMenuClass));
        sidemenuadapter.add(new SideMenuClass("프로그램", Global.Fall2018_URL + "program/list.php?Fall2018_gubun=" + prefs.getString("Fall2018_gubun","") + "&Fall2018_sid=" + prefs.getString("Fall2018_sid",""), null));
        sidemenuadapter.add(new SideMenuClass("등록", "regi", null));
        sidemenuadapter.add(new SideMenuClass("학회장 안내", Global.Fall2018_URL + "venue.php", null));
        sidemenuadapter.add(new SideMenuClass("오시는 길", Global.Fall2018_URL + "map.php", null));
        ArrayList<SubSideMenuClass> subSideMenuClass1 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass1.add(new SubSideMenuClass("- 스폰서", Global.Fall2018_URL + "sponsor.php"));
        subSideMenuClass1.add(new SubSideMenuClass("- 부스", Global.Fall2018_URL + "booth.php"));
        sidemenuadapter.add(new SideMenuClass("스폰서", Global.Fall2018_URL + null, subSideMenuClass1));
        sidemenuadapter.add(new SideMenuClass("공지사항", Global.Fall2018_URL + "bbs/list.php", null));


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
                        if(!prefs.getString("Fall2018_gubun","").equals("")) {
                            intent = new Intent(Fall2018_SideMenu.this, Fall2018_WebPage.class);
                            intent.putExtra("page", Global.Fall2018_URL+"regist/index.php?Fall2018_gubun=" + prefs.getString("Fall2018_gubun","") + "&Fall2018_sid=" + prefs.getString("Fall2018_sid",""));
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            AlertDialog dialog = new AlertDialog.Builder(Fall2018_SideMenu.this,R.style.AlertDialogCustom)
                                    .setTitle(getString(R.string.app_name))
                                    .setMessage("사전등록 확인을 위해 로그인이 필요합니다.\n로그인을 하시겠습니까?")
                                    .setPositiveButton(android.R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Fall2018_SideMenu.this, Fall2018_LoginActivity.class);
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

                        }
                    }
                    else
                    {
                        intent = new Intent(Fall2018_SideMenu.this,Fall2018_WebPage.class);
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
                    intent = new Intent(Fall2018_SideMenu.this, Fall2018_WebPage.class);
                    intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_BarcodeActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.regi :
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_WebPage.class);
                intent.putExtra("page", Global.Fall2018_URL+"regist/index.php?Fall2018_gubun=" + prefs.getString("Fall2018_gubun","") + "&Fall2018_sid=" + prefs.getString("Fall2018_sid",""));
                startActivity(intent);
                finish();
                break;

            case R.id.scrap :
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", Global.Fall2018_URL+"mypage/schedule.php?Fall2018_gubun="+prefs.getString("Fall2018_gubun","")+"&Fall2018_sid="+prefs.getString("Fall2018_sid",""));
                startActivity(intent);
                finish();
                break;
            case R.id.setting :
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.home :
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.login :
                intent = new Intent(Fall2018_SideMenu.this, Fall2018_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.close :
                finish();
                break;
            case R.id.logout :
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("Fall2018_sid", "");
                editor.putString("Fall2018_gubun", "");
                editor.putString("Fall2018_name","");
                editor.putString("Fall2018_email", "");
                editor.commit();

                intent = new Intent(Fall2018_SideMenu.this, Fall2018_LoginActivity.class);
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
