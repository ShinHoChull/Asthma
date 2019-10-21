package com.m2comm.asthma.Fall2017;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.asthma.R;

public class Fall2017_IntroActivity extends Activity implements View.OnClickListener {

    ImageView yes,no,back;
    SharedPreferences prefs;
    TextView ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fall2017_intro);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);

        yes = (ImageView) findViewById(R.id.yes);
        no = (ImageView) findViewById(R.id.no);
        back = (ImageView) findViewById(R.id.back);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.back :
                finish();
                break;

            case R.id.yes :
                intent = new Intent(Fall2017_IntroActivity.this, Fall2017_LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.no :
                String phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Fall2017_name", phoneNumber);
                editor.commit();
                intent = new Intent(Fall2017_IntroActivity.this, Fall2017_MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
