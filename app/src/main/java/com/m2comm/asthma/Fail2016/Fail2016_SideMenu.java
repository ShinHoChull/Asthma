package com.m2comm.asthma.Fail2016;

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

import com.m2comm.asthma.Barcode;
import com.m2comm.asthma.MainActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.AnimatedExpandableListView;
import com.m2comm.module.Global;
import com.m2comm.module.SideMenuAdapter2;
import com.m2comm.module.SideMenuClass;
import com.m2comm.module.SubSideMenuClass;

import java.util.ArrayList;

public class Fail2016_SideMenu extends Activity implements View.OnClickListener {

    int lastClickedPosition;
    SideMenuAdapter2 sidemenuadapter;
    AnimatedExpandableListView menu_list;
    LinearLayout regi,scrap,setting,home,login,layout1,layout2,barcode;
    String name;
    SharedPreferences prefs;
    ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fail2016_sidemenu);
        //getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        getWindow().setWindowAnimations(0);
        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter2(this, R.layout.fail2016_side_menu_item);
        menu_list.setAdapter(sidemenuadapter);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        regi = (LinearLayout) findViewById(R.id.regi);
        scrap = (LinearLayout) findViewById(R.id.scrap);
        scrap.setOnClickListener(this);
        regi.setOnClickListener(this);
        setting = (LinearLayout) findViewById(R.id.setting);
        home = (LinearLayout) findViewById(R.id.home);
        login = (LinearLayout) findViewById(R.id.login);
        close = (ImageView) findViewById(R.id.close);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        close.setOnClickListener(this);
        home.setOnClickListener(this);
        login.setOnClickListener(this);

        if(prefs.getString("Fail2016_gubun","").equals(""))
        {
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
        sidemenuadapter.add(new SideMenuClass("행사안내",Global.Fail2016_URL + "wslnfo.php",null));
        sidemenuadapter.add(new SideMenuClass("프로그램",Global.Fail2016_URL + "program.php?gubun=" + prefs.getString("Fail2016_gubun","") + "&sid=" + prefs.getString("Fail2016_sid",""),null));
        sidemenuadapter.add(new SideMenuClass("등록","etc",null));
        sidemenuadapter.add(new SideMenuClass("학회장 안내",Global.Fail2016_URL + "venue.php",null));
        sidemenuadapter.add(new SideMenuClass("오시는 길",Global.Fail2016_URL + "location.php",null));
        sidemenuadapter.add(new SideMenuClass("스폰서",Global.Fail2016_URL + "sponsor.php",null));
        sidemenuadapter.add(new SideMenuClass("공지사항",Global.Fail2016_URL + "bbs_list.php",null));



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
                    if(!sidemenuadapter.getGroup(groupPosition).url.equals("etc"))
                    {
                        intent = new Intent(Fail2016_SideMenu.this,Fail2016_WebPage.class);
                        intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        new AlertDialog.Builder(Fail2016_SideMenu.this)
                            .setTitle(getString(R.string.app_name))
                            .setMessage("행사가 끝났습니다.")
                            .setPositiveButton(android.R.string.ok,
                                    new AlertDialog.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    })
                            .setCancelable(false)
                            .create()
                            .show();
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
                    intent = new Intent(Fail2016_SideMenu.this, Fail2016_WebPage.class);
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
                intent = new Intent(Fail2016_SideMenu.this, Barcode.class);
                startActivity(intent);
                break;

            case R.id.regi :

                new AlertDialog.Builder(Fail2016_SideMenu.this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage("행사가 끝났습니다.")
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();
                break;

            case R.id.scrap :
                intent = new Intent(Fail2016_SideMenu.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL + "myfavor.php");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.setting :
                break;
            case R.id.home :
                intent = new Intent(Fail2016_SideMenu.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.login :
                intent = new Intent(Fail2016_SideMenu.this, Fail2016_LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);
                break;
            case R.id.close :
                finish();
                break;
            case R.id.logout :
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("Fail2016_sid", "");
                editor.putString("Fail2016_gubun", "");
                editor.putString("Fail2016_name","");
                editor.putString("Fail2016_email", "");
                editor.commit();

                intent = new Intent(Fail2016_SideMenu.this, Fail2016_LoginActivity.class);
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
