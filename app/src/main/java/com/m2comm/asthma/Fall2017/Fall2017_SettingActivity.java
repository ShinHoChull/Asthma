package com.m2comm.asthma.Fall2017;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.m2comm.asthma.R;
import com.m2comm.asthma.Fall2017.Fall2017_SettingActivity;
import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fall2017_SettingActivity extends Activity {

    ListView list;
    SetAdapter adapter;
    SharedPreferences prefs;
    LoadingDialog loadingDialog;
    LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        list = (ListView) findViewById(R.id.list);
        adapter = new SetAdapter(this, R.layout.setting_item);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        list.setAdapter(adapter);

        HttpAsyncTask question = new HttpAsyncTask(Fall2017_SettingActivity.this, new HttpInterface() {
            @Override
            public void onResult(String result) {

                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = 1;
                handle.sendMessage(msg);
            }

        });

        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        question.execute(new HttpParam("url", Global.Fall2017_URL + "get_pushYN.php"),
                new HttpParam("token", prefs.getString("registration_id", "")));
    }

    public final Handler handle = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what==1)
            {

                try {
                    JSONObject resultList = new JSONObject((String) msg.obj);
                    adapter.add(new SetClass("alarm","공지사항",resultList.getString("alarm")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

        }
    };


    public class SetAdapter extends ArrayAdapter {

        List SetList = new ArrayList();
        Context context;

        public SetAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.context = context;
        }

        public void add(SetClass object) {
            SetList.add(object);
            super.add(object);
        }

        public void reset() {
            SetList.clear();
        }

        @Override
        public int getCount() {
            return SetList.size();
        }

        @Override
        public SetClass getItem(int index) {
            return (SetClass) SetList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final SetClass SetInfo = (SetClass) SetList.get(position);
            View row = convertView;
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.setting_item, parent, false);

            TextView msgName = (TextView) row.findViewById(R.id.name);
            msgName.setText(SetInfo.name);
            // Inflater를 이용해서 생성한 View에, ChatMessage를 삽입한다.
            final ImageView btn = (ImageView) row.findViewById(R.id.btn);
            if(SetInfo.alarm.equals("Y"))
                btn.setImageResource(R.drawable.btn_on);
            else
                btn.setImageResource(R.drawable.btn_off);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(SetInfo.alarm.equals("Y")) {
                        SetInfo.alarm = "N";
                        btn.setImageResource(R.drawable.btn_off);
                    }
                    else {
                        SetInfo.alarm = "Y";
                        btn.setImageResource(R.drawable.btn_on);
                    }

                    HttpAsyncTask question = new HttpAsyncTask(Fall2017_SettingActivity.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            loadingDialog.dismiss();
                        }

                    });
                    question.execute(new HttpParam("url",Global.Fall2017_URL + "set_pushYN.php"),
                            new HttpParam("val", SetInfo.alarm),
                            new HttpParam("token", prefs.getString("registration_id", "")));

                    loadingDialog = new LoadingDialog(Fall2017_SettingActivity.this);
                    loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    loadingDialog.setCancelable(false);
                    loadingDialog.show();

                }
            });

            return row;
        }

    }

    public class SetClass
    {
        public String sid;
        public String name;
        public String alarm;
        public SetClass(String sid, String name, String alarm)
        {
            this.sid = sid;
            this.name = name;
            this.alarm = alarm;
        }
    }

}
