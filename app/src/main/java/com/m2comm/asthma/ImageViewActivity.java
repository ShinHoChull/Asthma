package com.m2comm.asthma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageViewActivity extends Activity {

    SharedPreferences prefs;
    ImageView imgview;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);

        Intent intent = getIntent();
        String url = intent.getStringExtra("page");
        imgview = (ImageView) findViewById(R.id.imageview);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this).load(url).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                return false;
            }
            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable,
                                           String s, Target<GlideDrawable> target, boolean b,
                                           boolean b1) {

                photoViewAttacher = new PhotoViewAttacher(imgview);
                photoViewAttacher.setMaximumScale(10);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgview);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


}
