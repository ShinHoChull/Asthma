package com.m2comm.voting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.asthma.R;

public class Voting extends Activity implements View.OnClickListener {

    private String phoneNumber;

    SharedPreferences prefs;

    private TextView btnVoting1, btnVoting2, btnVoting3, btnVoting4, btnVoting5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        getWindow().setWindowAnimations(0);
        prefs = getSharedPreferences("voting", MODE_PRIVATE);
        phoneNumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        BottomMenu bottommenu = (BottomMenu) findViewById(R.id.bottommenu);
        bottommenu.setActivity(this);
        bottommenu.setSelectNumber(2);

        btnVoting1 = (TextView)findViewById(R.id.VotingButton1);
        btnVoting2 = (TextView)findViewById(R.id.VotingButton2);
        btnVoting3 = (TextView)findViewById(R.id.VotingButton3);
        btnVoting4 = (TextView)findViewById(R.id.VotingButton4);
        btnVoting5 = (TextView)findViewById(R.id.VotingButton5);

        btnVoting1.setOnClickListener(this);
        btnVoting2.setOnClickListener(this);
        btnVoting3.setOnClickListener(this);
        btnVoting4.setOnClickListener(this);
        btnVoting5.setOnClickListener(this);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        switch(v.getId())
        {
            case R.id.VotingButton1:
                intent = new Intent(this, SelectedNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("selected num", 1);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;

            case R.id.VotingButton2:
                intent = new Intent(this, SelectedNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("selected num", 2);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;
            case R.id.VotingButton3:
                intent = new Intent(this, SelectedNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("selected num", 3);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;
            case R.id.VotingButton4:
                intent = new Intent(this, SelectedNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("selected num", 4);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;
            case R.id.VotingButton5:
                intent = new Intent(this, SelectedNumber.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("selected num", 5);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
                break;
        }
    }
}
