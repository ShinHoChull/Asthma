package com.m2comm.module;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.m2comm.asthma.R;

public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
    }
}