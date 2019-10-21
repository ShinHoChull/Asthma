package com.m2comm.asthma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Check;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener {

    LinearLayout ll1,ll2,yes,no,bg;
    TextView login;
    EditText id,password;
    SharedPreferences prefs;
    Intent intent;
    ImageView banner;
    int check;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        login = (TextView) findViewById(R.id.login);
        yes = (LinearLayout) findViewById(R.id.yes);
        no = (LinearLayout) findViewById(R.id.no);
        id = (EditText) findViewById(R.id.idea);
        password = (EditText) findViewById(R.id.password);
        bg = (LinearLayout) findViewById(R.id.bg);
        bg.setOnClickListener(this);
        intent = getIntent();
        login.setOnClickListener(this);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        banner = (ImageView) findViewById(R.id.banner);
        banner.setOnClickListener(this);
        check=intent.getIntExtra("check",0);
        if(check==1)
        {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
        }

        Check Check = new Check(this);
        Check.PermissionCheck();
    }

    public final Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                String info = (String) msg.obj;
                loadingDialog.dismiss();
                try {
                    JSONObject order = new JSONObject(info);
                    if(order.getString("result").equals("10"))
                    {
                        Toast.makeText(LoginActivity.this,"ID를 확인해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else if(order.getString("result").equals("20"))
                    {
                        Toast.makeText(LoginActivity.this,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("id", id.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putString("isLogin", id.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,info,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.banner:

                if(prefs.getString("Fall2019_name","").equals("")) {
                    intent = new Intent(LoginActivity.this, com.m2comm.asthma.Fall2019.Fall2019_LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    intent = new Intent(LoginActivity.this, com.m2comm.asthma.Fall2019.Fall2019_MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;

            case R.id.bg:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(id.getWindowToken(), 0);
                break;
            case R.id.back :
                if(check==1)
                {
                    finish();
                }
                else
                {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.GONE);
                }
                break;
            case R.id.login :
                if(id.getText().toString().length()>0 && password.getText().toString().length()>0) {
                    HttpAsyncTask question = new HttpAsyncTask(LoginActivity.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            Log.d("hgkim", "result : " + result);
                            Message msg = Message.obtain();
                            msg.obj = result;
                            msg.what = 1;
                            handle.sendMessage(msg);
                        }

                    });
                    question.execute(new HttpParam("url", "http://www.allergy.or.kr/m2_connect.html"),
                            new HttpParam("id", id.getText().toString()),
                            new HttpParam("pwd", password.getText().toString()),
                            new HttpParam("key", "39e5c4bec8fa45d630cecf9362da608c")
                    );

                    loadingDialog = new LoadingDialog(this);
                    loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();

                }
                else
                {
                    Toast.makeText(LoginActivity.this,"ID, 비밀번호를 입력해 주세요",Toast.LENGTH_SHORT).show();
                }



                break;
            case R.id.yes :
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                break;

            case R.id.no :
                SharedPreferences.Editor editor = prefs.edit();
                String phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                editor.putString("isLogin", phoneNumber);
                editor.commit();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }

}
