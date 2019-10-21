package com.m2comm.asthma;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.m2comm.module.Global;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;
import com.m2comm.module.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {

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
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new SetAdapter(this, R.layout.setting_item);
        prefs = getSharedPreferences("m2comm", MODE_PRIVATE);
        list.setAdapter(adapter);

        HttpAsyncTask question = new HttpAsyncTask(SettingActivity.this, new HttpInterface() {
            @Override
            public void onResult(String result) {
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = 1;
                handle.sendMessage(msg);
            }

        });

        question.execute(new HttpParam("url", Global.URL + "app/php/get_alram.php"),
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
                    adapter.add(new SetClass("alarm1","공지사항",resultList.getString("alarm1")));
                    adapter.add(new SetClass("alarm2","진료지침",resultList.getString("alarm2")));
                    adapter.add(new SetClass("alarm3","학회지",resultList.getString("alarm3")));
                    adapter.add(new SetClass("alarm4","학술행사",resultList.getString("alarm4")));
                    adapter.add(new SetClass("alarm5","뉴스레터",resultList.getString("alarm5")));

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

                    HttpAsyncTask question = new HttpAsyncTask(SettingActivity.this, new HttpInterface() {
                        @Override
                        public void onResult(String result) {
                            loadingDialog.dismiss();
                        }

                    });
                    question.execute(new HttpParam("url",com.m2comm.module.Global.URL + "app/php/set_alram.php"),
                            new HttpParam("gubun", SetInfo.sid),
                            new HttpParam("val", SetInfo.alarm),
                            new HttpParam("token", prefs.getString("registration_id", "")));

                    loadingDialog = new LoadingDialog(SettingActivity.this);
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
