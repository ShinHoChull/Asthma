package com.m2comm.voting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.asthma.R;
import com.m2comm.module.HttpAsyncTask;
import com.m2comm.module.HttpInterface;
import com.m2comm.module.HttpParam;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.m2comm.voting.Global.LECTURE_LIST;


public class Lecture extends Activity implements View.OnClickListener {

    LectureAdapter lectureAdapter;
    Intent intent;
    AnimatedExpandableListView menu_list;
    SideMenuAdapter sidemenuadapter;
    int lastClickedPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        getWindow().setWindowAnimations(0);

        intent = getIntent();

        ImageView close = (ImageView)findViewById(R.id.close);
        close.setOnClickListener(this);

        lectureAdapter = new LectureAdapter(this, R.layout.lecture_item);

        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter(this, R.layout.voting_menu_item);
        menu_list.setAdapter(sidemenuadapter);


        HttpAsyncTask question = new HttpAsyncTask(Lecture.this, new HttpInterface() {
            @Override
            public void onResult(String result) {
                Log.d("hgkim",result);
                Message msg = Message.obtain();
                msg.obj = result;
                msg.what = 1;
                handle.sendMessage(msg);
            }
        });
        question.execute(new HttpParam("url", LECTURE_LIST));

        menu_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long ids) {

                v.setBackgroundColor(Color.parseColor("#f8f8f8"));
                Boolean isExpand = (!menu_list.isGroupExpanded(groupPosition));
                if (menu_list.isGroupExpanded(lastClickedPosition))
                    menu_list.collapseGroupWithAnimation(lastClickedPosition);
                menu_list.setSelection(groupPosition);
                if (sidemenuadapter.SideMenuList.get(groupPosition).SubSideMenuList == null) {
                    return true;
                }
                if (isExpand) {
                    menu_list.expandGroupWithAnimation(groupPosition);
                    v.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }
                lastClickedPosition = groupPosition;
                menu_list.setSelection(groupPosition);
                return true;

            }
        });

        menu_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long ids) {

                v.setBackgroundColor(Color.parseColor("#aaaaaa"));
                ImageView btn = (ImageView) v.findViewById(R.id.btn);
                btn.setColorFilter(getResources().getColor(R.color.select_border));


                ImageView input_check = (ImageView) v.findViewById(R.id.input_check);
                input_check.setVisibility(View.VISIBLE);


                intent.putExtra("room",sidemenuadapter.getGroup(groupPosition).room);
                intent.putExtra("session_sid",sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).sid);
                intent.putExtra("return",sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).title);
                intent.putExtra("index", 1);
                setResult(RESULT_OK, intent);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        finish();
                    }
                }, 100);

                return false;
            }
        });

    }


    public final Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                try {
                    JSONArray resultList = new JSONArray((String)msg.obj);
                    for(int i=0;i<resultList.length();i++)
                    {ArrayList<submenu> subSideMenuClass = new ArrayList<submenu>();
                        try {
                            JSONArray subList = resultList.getJSONObject(i).getJSONArray("sub");

                            for(int j=0;j<subList.length();j++)
                            {
                                subSideMenuClass.add(new submenu(subList.getJSONObject(j).getString("title"), subList.getJSONObject(j).getString("sid")));
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        sidemenuadapter.add(new SideMenuClass(resultList.getJSONObject(i).getString("theme"), resultList.getJSONObject(i).getString("room"), resultList.getJSONObject(i).getString("sid"),  subSideMenuClass));
                    }
                    sidemenuadapter.notifyDataSetChanged();
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }
    };

    class submenu {
        public String title;
        public String sid;

        submenu(String title, String sid)
        {
            this.title = title;
            this.sid = sid;
        }
    }

    class LectureItem {
        public String name;
        public int type;

        LectureItem(String name, int type)
        {
            this.name = name;
            this.type = type;
        }
    }

    class LectureAdapter extends ArrayAdapter {

        List lectureList = new ArrayList();
        public LectureAdapter (Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        public void add(LectureItem object) {
            lectureList.add(object);
            super.add(object);
        }


        @Override
        public int getCount() {
            return lectureList.size();
        }

        @Override
        public LectureItem getItem(int index) {
            return (LectureItem) lectureList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LectureItem LectureItem = (LectureItem) lectureList.get(position);
            View row = convertView;

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.lecture_item, parent, false);
            TextView name = (TextView) row.findViewById(R.id.name);
            name.setText(LectureItem.name);
            /*
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.setBackgroundColor(Color.parseColor("#aaaaaa"));
                    LinearLayout btn = (LinearLayout) v.findViewById(R.id.btn);
                    btn.setBackgroundResource(R.drawable.btn_o);
                    intent.putExtra("return", LectureItem. LectureItem.name);
                    intent.putExtra("index", 1);
                    setResult(RESULT_OK, intent);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finish();
                        }
                    }, 100);
                }
            });
            */
            return row;
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.close :
                finish();
                break;
        }

    }
}
