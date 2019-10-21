package com.m2comm.asthma.Fail2016;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.m2comm.asthma.MainActivity;
import com.m2comm.asthma.R;
import com.m2comm.module.Global;

import static com.m2comm.asthma.R.id.search;

public class Fail2016_MainActivity extends Activity implements View.OnClickListener {

    LinearLayout menu,menu1,menu2,menu3,menu4,menu5,menu6,voting,bg,home;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fail2016_main);
        bg = (LinearLayout) findViewById(R.id.bg);
        menu = (LinearLayout) findViewById(R.id.menu);
        menu1 = (LinearLayout) findViewById(R.id.menu1);
        menu2 = (LinearLayout) findViewById(R.id.menu2);
        menu3 = (LinearLayout) findViewById(R.id.menu3);
        menu4 = (LinearLayout) findViewById(R.id.menu4);
        menu5 = (LinearLayout) findViewById(R.id.menu5);
        menu6 = (LinearLayout) findViewById(R.id.menu6);
        voting = (LinearLayout) findViewById(R.id.voting);
        menu.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
        menu6.setOnClickListener(this);
        voting.setOnClickListener(this);

        home = (LinearLayout) findViewById(R.id.home);
        home.setOnClickListener(this);
        LinearLayout search = (LinearLayout) findViewById(R.id.search);
        search.setOnClickListener(this);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        checkPermission();

        voting.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.home :
                intent = new Intent(Fail2016_MainActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_SideMenu.class);
                startActivity(intent);
                break;
            case R.id.menu1 :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL+"wslnfo.php");
                startActivity(intent);
                break;
            case R.id.menu2 :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL+"program.php?sid="+prefs.getString("Fail2016_sid","")+"&gubun="+prefs.getString("Fail2016_gubun",""));
                startActivity(intent);

                break;
            case R.id.menu3 :
                new AlertDialog.Builder(this)
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
            case R.id.menu4 :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL+"location.php");
                startActivity(intent);
                break;
            case R.id.menu5 :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL+"venue.php");
                startActivity(intent);
                break;

            case R.id.menu6 :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.putExtra("page", Global.Fail2016_URL+"bbs_list.php");
                startActivity(intent);
                break;
            case search :
                intent = new Intent(Fail2016_MainActivity.this, Fail2016_WebPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", Global.Fail2016_URL+"search.php?gubun=" + prefs.getString("Fail2016_gubun","") + "&sid=" + prefs.getString("Fail2016_sid",""));
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
                new AlertDialog.Builder(this)
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
