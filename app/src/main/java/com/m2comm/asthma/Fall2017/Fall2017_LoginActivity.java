package com.m2comm.asthma.Fall2017;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Fall2017_LoginActivity extends Activity implements View.OnClickListener {

    LinearLayout bg;
    ImageView back;
    EditText name,email;
    SharedPreferences prefs;
    TextView ok;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2017_login);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        ok = (TextView) findViewById(R.id.ok);
        back = (ImageView) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        bg = (LinearLayout) findViewById(R.id.bg);
        bg.setOnClickListener(this);


        ok.setOnClickListener(this);
        back.setOnClickListener(this);

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
                    editor.putString("Fall2017_sid", resultList.getString("sid"));
                    editor.putString("Fall2017_gubun", resultList.getString("gubun"));
                    editor.putString("Fall2017_name", resultList.getString("name"));
                    editor.commit();
                    Intent intent = new Intent(Fall2017_LoginActivity.this, Fall2017_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                } catch (JSONException e) {
                    Toast.makeText(Fall2017_LoginActivity.this,info,Toast.LENGTH_SHORT).show();
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

                 HttpAsyncTask question = new HttpAsyncTask(Fall2017_LoginActivity.this, new HttpInterface() {
                    @Override
                    public void onResult(String result) {
                        Message msg = Message.obtain();
                        msg.obj = result;
                        msg.what = 1;
                        handle.sendMessage(msg);
                    }

                });
                question.execute(new HttpParam("url", Global.Fall2017_URL + "login.php"),
                        new HttpParam("name","" + name.getText().toString()),
                        new HttpParam("phone",email.getText().toString()));


                break;
        }
    }

}
