package com.m2comm.voting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2comm.asthma.R;


public class BottomMenu extends LinearLayout implements View.OnClickListener
{
    TextView btn1, btn2, btn3, btn4;
    Context context;
    Activity activity;


    public BottomMenu(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BottomMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public BottomMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public void setSelectNumber(int i)
    {
        switch(i) {
            case 1:
                btn1.setBackgroundResource(R.drawable.bottom_on);
                break;
            case 2:
                btn2.setBackgroundResource(R.drawable.bottom_on);
                break;
            case 3:
                btn3.setBackgroundResource(R.drawable.bottom_on);
                break;
            case 4:
                btn4.setBackgroundResource(R.drawable.bottom_on);
                break;
        }
    }

    public void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.activity_bottom, this, false);
        addView(v);
        btn1 = (TextView) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (TextView) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (TextView) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = (TextView) findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.btn1:
                intent = new Intent(activity, Agenda.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
                break;
            case R.id.btn2:
                intent = new Intent(activity, Voting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
                break;
            case R.id.btn3:
                if(Global.ISLECTURE)
                    intent = new Intent(activity, Question.class);
                else
                    intent = new Intent(activity, Question2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
                break;
            case R.id.btn4:
                intent = new Intent(activity, Feedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.finish();
                break;
        }
        activity.overridePendingTransition(0,0);
    }
}
