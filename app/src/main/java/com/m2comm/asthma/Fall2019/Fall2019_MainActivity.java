package com.m2comm.asthma.Fall2019;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.m2comm.asthma.MainActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.Global;
import com.m2comm.voting.Voting;

public class Fall2019_MainActivity extends Activity implements View.OnClickListener {

    ImageView menu,menu1,menu2,menu3,menu4,menu5,menu6,voting,search,bg,home,glance;
    SharedPreferences prefs;
    String sid,gubun;
    String code = "all2019f";

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2019_main);
        getWindow().setWindowAnimations(0);
        menu = (ImageView) findViewById(R.id.menu);
        menu1 = (ImageView) findViewById(R.id.menu1);
        menu2 = (ImageView) findViewById(R.id.menu2);
        menu3 = (ImageView) findViewById(R.id.menu3);
        menu4 = (ImageView) findViewById(R.id.menu4);
        menu5 = (ImageView) findViewById(R.id.menu5);
        menu6 = (ImageView) findViewById(R.id.menu6);
        search = (ImageView) findViewById(R.id.search);
        voting = (ImageView) findViewById(R.id.voting);
        glance = (ImageView) findViewById(R.id.glance);

        menu.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
        menu6.setOnClickListener(this);
        search.setOnClickListener(this);
        voting.setOnClickListener(this);
        glance.setOnClickListener(this);

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
                intent = new Intent(Fall2019_MainActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_SideMenu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.menu1 :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", Global.Fall2019_URL+"info/index.php");
                startActivity(intent);
                break;
            case R.id.menu2 :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+this.code+"&deviceid"+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                startActivity(intent);

                break;
            case R.id.menu3 :
                if(!prefs.getString("Fall2019_gubun","").equals("")) {
                    intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                    intent.putExtra("page", Global.Fall2019_URL+"regist/index.php?Fall2019_gubun=" + prefs.getString("Fall2019_gubun","") + "&Fall2019_sid=" + prefs.getString("Fall2019_sid",""));
                    startActivity(intent);
                }
                else
                {

                    new AlertDialog.Builder(Fall2019_MainActivity.this,R.style.AlertDialogCustom)
                            .setTitle("대한천식알레르기학회")
                            .setMessage("Please login")
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Fall2019_MainActivity.this, Fall2019_LoginActivity.class);
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
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", Global.Fall2019_URL+"map.php");
                startActivity(intent);
                break;
            case R.id.menu5 :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", Global.Fall2019_URL+"venue.php");
                startActivity(intent);
                break;

            case R.id.glance :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", "http://ezv.kr/voting/php/session/glance.php?code="+this.code);
                startActivity(intent);
                break;
            case R.id.menu6 :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.putExtra("page", "http://ezv.kr/voting/php/bbs/list.php?code="+this.code);
                startActivity(intent);
                break;
            case R.id.search :
                intent = new Intent(Fall2019_MainActivity.this, Fall2019_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?tab=-3&code="+this.code+"&deviceid"+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
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
                intent = new Intent(Fall2019_MainActivity.this, Voting.class);
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
