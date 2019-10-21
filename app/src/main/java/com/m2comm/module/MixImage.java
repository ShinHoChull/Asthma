package com.m2comm.module;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.m2comm.asthma.R;


@SuppressLint("AppCompatCustomView")
public class MixImage extends ImageView {
    int parwidth;
    int parheight;
    Context context;
    public MixImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MixImage);
        int color = a.getColor(R.styleable.MixImage_mixcolor, Color.parseColor("#ffffff"));

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        parwidth = a.getInt(R.styleable.MixImage_w, 0) * width * 3 / 1080;
        parheight = a.getInt(R.styleable.MixImage_h, 0) * height * 3 / 1920;

        if(color != -1) {
            this.setColorFilter(color);
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
