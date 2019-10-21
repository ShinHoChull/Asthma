package com.m2comm.asthma.Spring2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.m2comm.asthma.R;
import com.m2comm.asthma.Spring2017.Spring2017_LoginActivity;
import com.m2comm.asthma.Spring2017.Spring2017_MainActivity;
import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;

import org.json.JSONException;
import org.json.JSONObject;

public class Spring2018_LoginActivity extends Activity implements View.OnClickListener {


    LinearLayout text1,ll1,ll2,yes,no,bg;
    EditText name,email;
    SharedPreferences prefs;
    TextView ok;
    Intent intent;
    int check;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.spring2018_login);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        text1 = (LinearLayout) findViewById(R.id.text1);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ok = (TextView) findViewById(R.id.ok);
        yes = (LinearLayout) findViewById(R.id.yes);
        no = (LinearLayout) findViewById(R.id.no);
        back = (ImageView) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        bg = (LinearLayout) findViewById(R.id.bg);
        bg.setOnClickListener(this);
        intent = getIntent();
        ok.setOnClickListener(this);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        back.setOnClickListener(this);
        check=intent.getIntExtra("check",0);
        if(check==1)
        {
            text1.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
        }

    }

    public final Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("hgkim","Handler : " );
            if(msg.what==1)
            {
                String info = (String) msg.obj;

                try {
                    JSONObject order = new JSONObject(info);
                    JSONObject resultList = order.getJSONObject("resultList");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Spring2018_sid", resultList.getString("sid"));
                    editor.putString("Spring2018_gubun", resultList.getString("gubun"));
                    editor.putString("Spring2018_name", resultList.getString("name"));
                    editor.putString("Spring2018_email", resultList.getString("email"));
                    editor.commit();
                    Intent intent = new Intent(Spring2018_LoginActivity.this, Spring2018_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    Toast.makeText(Spring2018_LoginActivity.this,info,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }
    };
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.bg:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                break;
            case R.id.back:
                if (check == 1) {
                    finish();
                } else {
                    back.setVisibility(View.INVISIBLE);
                    text1.setBackgroundResource(R.drawable.txt_q1);
                    text1.setVisibility(View.VISIBLE);
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.GONE);
                }
                break;
            case R.id.ok:
                if (name.getText().toString().length() == 0) {
                    Toast.makeText(Spring2018_LoginActivity.this,"Enter your Name",Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().length() == 0) {
                    Toast.makeText(Spring2018_LoginActivity.this,"Enter your Phone Number",Toast.LENGTH_SHORT).show();

                } else {

                    HttpAsyncTask question = new HttpAsyncTask(Spring2018_LoginActivity.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            Message msg = Message.obtain();
                            msg.obj = result;
                            msg.what = 1;
                            handle.sendMessage(msg);
                        }

                    });
                    question.execute(new HttpParam("url", Global.Spring2018_URL + "login.php"),
                    new HttpParam("name", "" + name.getText().toString()),
                    new HttpParam("phone", email.getText().toString()));
                }
                break;
            case R.id.yes :
                text1.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                break;

            case R.id.no :
                String phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Spring2018_name", phoneNumber);
                editor.commit();
                intent = new Intent(Spring2018_LoginActivity.this, Spring2018_MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }

}
