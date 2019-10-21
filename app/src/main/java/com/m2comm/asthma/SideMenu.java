package com.m2comm.asthma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.AnimatedExpandableListView;
import com.m2comm.module.Global;
import com.m2comm.module.SideMenuAdapter;
import com.m2comm.module.SideMenuClass;
import com.m2comm.module.SubSideMenuClass;

import java.util.ArrayList;

public class SideMenu extends Activity implements View.OnClickListener {

    int lastClickedPosition;
    SideMenuAdapter sidemenuadapter;
    AnimatedExpandableListView menu_list;
    ImageView home,setting,back,login;
    TextView home1;
    SharedPreferences prefs;
    String id;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);

    }


    @Override
    public void onClick(View v) {
        Intent intent;;
        switch (v.getId()) {
            case R.id.home:
            case R.id.home2:
                intent = new Intent(SideMenu.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.close:
                finish();
                break;

            case R.id.login:

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("id", null);
                editor.putString("password", null);
                editor.commit();


                intent = new Intent(SideMenu.this, LoginActivity.class);
                intent.putExtra("check", 1);
                startActivity(intent);

                break;

            case R.id.setting:
                intent = new Intent(SideMenu.this, SettingActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidemenu);
        getWindow().setWindowAnimations(0);

        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter(this, R.layout.side_menu_item);
        menu_list.setAdapter(sidemenuadapter);

        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(this);
        home1 = (TextView) findViewById(R.id.home2);
        home1.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.close);
        back.setOnClickListener(this);
        setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(this);
        login = (ImageView) findViewById(R.id.login);
        login.setOnClickListener(this);

        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        id = prefs.getString("id", null);
        if(id == null)
            login.setImageResource(R.drawable.btn_logout);
        else
            login.setImageResource(R.drawable.btn_login);


        String device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ArrayList<SubSideMenuClass> subSideMenuClass = new ArrayList<SubSideMenuClass>();
        subSideMenuClass.add(new SubSideMenuClass("· 인사말", Global.URL + "app/php/message.php" ));
        subSideMenuClass.add(new SubSideMenuClass("· 연혁", Global.URL + "app/php/history.php"));
        subSideMenuClass.add(new SubSideMenuClass("· 임원진 명단", Global.URL + "app/php/board.php"));

        sidemenuadapter.add(new SideMenuClass("학회소개",null,subSideMenuClass));

        ArrayList<SubSideMenuClass> subSideMenuClass4 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass4.add(new SubSideMenuClass("· 공지사항",Global.URL + "app/php/bbs_list.php"));
        subSideMenuClass4.add(new SubSideMenuClass("· 연중행사 일정","etc"));

        sidemenuadapter.add(new SideMenuClass("공지사항",null,subSideMenuClass4));
        sidemenuadapter.add(new SideMenuClass("진료지침", Global.URL + "app/php/guide.php",null));
        sidemenuadapter.add(new SideMenuClass("학술대회", Global.URL + "app/php/event.php",null));
        sidemenuadapter.add(new SideMenuClass("심포지엄 및 교육강좌",Global.URL + "app/php/education.php",null));
        sidemenuadapter.add(new SideMenuClass("뉴스레터",Global.URL + "app/php/newsletter_list.php",null));

        ArrayList<SubSideMenuClass> subSideMenuClass2 = new ArrayList<SubSideMenuClass>();
        subSideMenuClass2.add(new SubSideMenuClass("· AAIR","http://www.aard.or.kr"));
        subSideMenuClass2.add(new SubSideMenuClass("· AARD","http://www.e-aair.org"));
        sidemenuadapter.add(new SideMenuClass("학회지",null,subSideMenuClass2));
        sidemenuadapter.add(new SideMenuClass("자료실",Global.URL + "app/php/pds.php",null));


        menu_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long ids) {

                Boolean isExpand = (!menu_list.isGroupExpanded(groupPosition));
                if(menu_list.isGroupExpanded(lastClickedPosition))
                    menu_list.collapseGroupWithAnimation(lastClickedPosition);
                menu_list.setSelection(groupPosition);
                if (sidemenuadapter.SideMenuList.get(groupPosition).SubSideMenuList == null) {
                    Intent intent;
                    if(!sidemenuadapter.getGroup(groupPosition).url.equals("etc"))
                    {
                        if(sidemenuadapter.getGroup(groupPosition).url.contains("app/php/pds.php") && id==null)
                        {
                            Toast.makeText(SideMenu.this,"회원만 이용이 가능합니다",Toast.LENGTH_SHORT).show();
                            intent = new Intent(SideMenu.this, LoginActivity.class);
                            intent.putExtra("check", 1);
                            startActivity(intent);
                            return true;
                        } else if(sidemenuadapter.getGroup(groupPosition).url.contains("app/php/education.php") && id==null)
                        {
                            Toast.makeText(SideMenu.this,"회원만 이용이 가능합니다",Toast.LENGTH_SHORT).show();
                            intent = new Intent(SideMenu.this, LoginActivity.class);
                            intent.putExtra("check", 1);
                            startActivity(intent);
                            return true;
                        }

                        intent = new Intent(SideMenu. this,WebPage.class);
                        intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    finish();
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
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long ids) {
                Intent intent;
                if(!sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url.equals("etc"))
                {

                    if(sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url.contains(Global.URL))
                    {
                        intent = new Intent(SideMenu.this, WebPage.class);
                        intent.putExtra("page", sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Uri uri = Uri.parse(sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).url);
                        intent  = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }

                    finish();
                }
                else
                {
                    intent = new Intent(SideMenu.this, CalendarActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }


}
