package com.m2comm.asthma.Spring2018;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.m2comm.asthma.MainActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.Global;
import com.m2comm.voting.Voting;

public class Spring2018_MainActivity extends Activity implements View.OnClickListener {

    ImageView menu,menu1,menu2,menu3,menu4,menu5,menu6,voting,search,bg,home;
    SharedPreferences prefs;
    String sid,gubun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spring2018_main);
        menu = (ImageView) findViewById(R.id.menu);
        menu1 = (ImageView) findViewById(R.id.menu1);
        menu2 = (ImageView) findViewById(R.id.menu2);
        menu3 = (ImageView) findViewById(R.id.menu3);
        menu4 = (ImageView) findViewById(R.id.menu4);
        menu5 = (ImageView) findViewById(R.id.menu5);
        menu6 = (ImageView) findViewById(R.id.menu6);
        search = (ImageView) findViewById(R.id.search);
        voting = (ImageView) findViewById(R.id.voting);

        menu.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
        menu6.setOnClickListener(this);
        search.setOnClickListener(this);
        voting.setOnClickListener(this);


        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(this);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        checkPermission();


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home :
                intent = new Intent(Spring2018_MainActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_SideMenu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.menu1 :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.putExtra("page", Global.Spring2018_URL+"info/infoMess.php");
                startActivity(intent);
                break;
            case R.id.menu2 :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.putExtra("page", Global.Spring2018_URL+"program/list.php?Spring2018_sid="+prefs.getString("Spring2018_sid","")+"&Spring2018_gubun="+prefs.getString("Spring2018_gubun",""));
                startActivity(intent);

                break;
            case R.id.menu3 :
                if(!prefs.getString("Spring2018_gubun","").equals("")) {
                    intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                    intent.putExtra("page", Global.Spring2018_URL+"regist/index.php?Spring2018_gubun=" + prefs.getString("Spring2018_gubun","") + "&Spring2018_sid=" + prefs.getString("Spring2018_sid",""));
                    startActivity(intent);
                }
                else
                {

                    new AlertDialog.Builder(Spring2018_MainActivity.this)
                            .setTitle("대한천식알레르기학회")
                            .setMessage("Please login")
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Spring2018_MainActivity.this, Spring2018_LoginActivity.class);
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
                break;
            case R.id.menu4 :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.putExtra("page", Global.Spring2018_URL+"tran/incheon.php");
                startActivity(intent);
                break;
            case R.id.menu5 :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.putExtra("page", Global.Spring2018_URL+"venue.php");
                startActivity(intent);
                break;

            case R.id.menu6 :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.putExtra("page", Global.Spring2018_URL+"bbs/list.php");
                startActivity(intent);
                break;
            case R.id.search :
                intent = new Intent(Spring2018_MainActivity.this, Spring2018_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", Global.Spring2018_URL+"search.php?Spring2018_gubun=" + prefs.getString("Spring2018_gubun","") + "&Spring2018_sid=" + prefs.getString("Spring2018_sid",""));
                startActivity(intent);

                /*
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("sid", "");
                editor.putString("gubun", "");
                editor.putString("name","");
                editor.putString("email", "");
                editor.commit();
                finish();
                */
                break;
            case R.id.voting :
                intent = new Intent(Spring2018_MainActivity.this, Voting.class);
                startActivity(intent);
                break;
        }
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
    /*
    @Override
    protected void onPause() {
        super.onPause();
        bg.setBackgroundColor(Color.parseColor("#ffffff"));
    }
    @Override
    protected void onResume() {
        super.onResume();
        bg.setBackgroundResource(R.drawable.bg);
    }
    */
}
