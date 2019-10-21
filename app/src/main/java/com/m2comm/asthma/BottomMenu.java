package com.m2comm.asthma;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class BottomMenu extends LinearLayout implements View.OnClickListener
{
    Button btn1, btn2, btn3;
    Context context;
    OnMenuListener listener;
    public interface OnMenuListener {
        void onMenuClick(int i);
    }

    public void setOnMenuListener(OnMenuListener listener) {
        this.listener = listener;
    }


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
    public void setSelectNumber(int i)
    {
        switch(i) {
            case 1:
                btn1.setBackgroundResource(R.drawable.voting1_o);
                break;
            case 2:
                btn2.setBackgroundResource(R.drawable.voting2_o);
                break;
            case 3:
                btn3.setBackgroundResource(R.drawable.voting3_o);
                break;
        }
    }

    @SuppressLint("WrongViewCast")
    public void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.activity_bottom, this, false);
        addView(v);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(listener != null) {
            switch (v.getId()) {
                case R.id.btn1:
                    listener.onMenuClick(1);
                    break;
                case R.id.btn2:
                    listener.onMenuClick(2);
                    break;
                case R.id.btn3:
                    listener.onMenuClick(3);
                    break;
            }
        }
    }
}
