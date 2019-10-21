package com.m2comm.module;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.m2comm.asthma.R;


@SuppressLint("AppCompatCustomView")
public class MyEditTextView extends EditText {
    int parwidth;
    int parheight;
    Context context;
    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        WindowManager wm;
        wm =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        Log.d("hgkim","width : " + width);
        Log.d("hgkim","height : " + height);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MixImage);
        parwidth = a.getInt(R.styleable.MixImage_w, 0) * width * 3 / 1080;
        parheight = a.getInt(R.styleable.MixImage_h, 0) * height * 3 / 1920;
        int parsize = a.getInt(R.styleable.MixImage_parsize,0);
        if(parsize>0) {
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int) (parsize*height/600));
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = 0, h = 0;

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED://unspecified
                w = widthMeasureSpec;
                break;
            case MeasureSpec.EXACTLY://match_parent
                w = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                h = heightMeasureSpec;
                break;
            case MeasureSpec.EXACTLY:
                h = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }

        if (parwidth > 0) {
            w = parwidth;
        } if(parheight > 0) {
            h = parheight;
        }

        setMeasuredDimension(w, h);
    }
}
