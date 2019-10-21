package com.m2comm.asthma.Fail2016;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.asthma.R;
import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;

import org.json.JSONException;
import org.json.JSONObject;

public class Fail2016_LoginActivity extends Activity implements View.OnClickListener {

    LinearLayout back,bg;
    EditText name,email;
    SharedPreferences prefs;
    TextView ok;
    Intent intent;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fail2016_login);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        ok = (TextView) findViewById(R.id.ok);
        back = (LinearLayout) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        bg = (LinearLayout) findViewById(R.id.bg);
        bg.setOnClickListener(this);
        ok.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public final Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                String info = (String) msg.obj;


                try {
                    JSONObject order = new JSONObject(info);
                    JSONObject resultList = order.getJSONObject("resultList");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Fail2016_sid", resultList.getString("sid"));
                    editor.putString("Fail2016_gubun", resultList.getString("gubun"));
                    editor.putString("Fail2016_name", resultList.getString("name"));
                    editor.putString("Fail2016_email", resultList.getString("email"));
                    editor.commit();
                    Intent intent = new Intent(Fail2016_LoginActivity.this, Fail2016_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    Toast.makeText(Fail2016_LoginActivity.this,info,Toast.LENGTH_SHORT).show();
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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                break;
            case R.id.back :
                    finish();

                break;
            case R.id.ok :

                 HttpAsyncTask question = new HttpAsyncTask(Fail2016_LoginActivity.this, new HttpInterface() {
                    @Override
                    public void onResult(String result) {
                        Message msg = Message.obtain();
                        msg.obj = result;
                        msg.what = 1;
                        handle.sendMessage(msg);
                    }

                });
                question.execute(new HttpParam("url", Global.Fail2016_URL + "login.php"),
                        new HttpParam("name","" + name.getText().toString()),
                        new HttpParam("email",email.getText().toString()));
                break;
        }
    }

}
