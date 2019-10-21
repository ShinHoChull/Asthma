package com.m2comm.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class GCMPopupActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle bun = getIntent().getExtras();
        String title = bun.getString("title");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GCMPopupActivity.this);
        /*
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
    			Intent i = new Intent(GCMPopupActivity.this, NewCaseListActivity.class);
                i.putExtra("new", true);
    			startActivity(i);
                GCMPopupActivity.this.finish();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
                GCMPopupActivity.this.finish();
			}
		});
        alertDialog.setMessage(title);
        alertDialog.show();
        */
    }
}
