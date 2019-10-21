package com.m2comm.asthma;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.m2comm.module.Global;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarActivity extends Activity implements View.OnClickListener {

    ListView calendar_list;
    LinearLayout prev,next, bt_bottom, week,top_list,back;
    private TextView tvDate;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    ScheduleAdapter scheduleAdapter;
    ImageView today;
    TextView noti, home;
    int group;
    ArrayList<Schedule> ScheduleList;
    private int year,month;
    private int today_year,today_month,today_day;
    int dayNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getWindow().setWindowAnimations(0);
        bt_bottom = (LinearLayout)findViewById(R.id.bt_bottom);
        bt_bottom.setOnClickListener(this);
        top_list = (LinearLayout)findViewById(R.id.top_list);
        top_list.setOnClickListener(this);
        week = (LinearLayout)findViewById(R.id.week);
        week.setOnClickListener(this);
        prev = (LinearLayout)findViewById(R.id.prev);
        prev.setOnClickListener(this);
        next = (LinearLayout)findViewById(R.id.next);
        next.setOnClickListener(this);
        today = (ImageView)findViewById(R.id.today);
        today.setOnClickListener(this);
        calendar_list = (ListView)findViewById(R.id.calendar_list);
        scheduleAdapter = new ScheduleAdapter(this, R.layout.schedule_list);
        calendar_list.setAdapter(scheduleAdapter);
        back = (LinearLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        noti = (TextView) findViewById(R.id.noti);
        noti.setOnClickListener(this);

        home = (TextView) findViewById(R.id.home);
        home.setOnClickListener(this);

        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView = (GridView)findViewById(R.id.gridview);
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        tvDate.setText(curYearFormat.format(date) + "." + curMonthFormat.format(date));
        dayList = new ArrayList<String>();
        ScheduleList = new ArrayList<Schedule>();

        mCal = Calendar.getInstance();
        today_year = year = Integer.parseInt(curYearFormat.format(date));
        today_month = month = Integer.parseInt(curMonthFormat.format(date));
        today_day = Integer.parseInt(curDayFormat.format(date));

        calendar_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL+"app/php/schedule_view.php?number="+ScheduleList.get(position).sid);
                startActivity(intent);
            }
        });

        Thread mThread = new Thread() {
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String url = Global.URL+"app/schedule_list.php?datetime="+curYearFormat.format(date) + "-" + curMonthFormat.format(date);
                    Log.d("hgkim","url : " + url);
                    HttpGet get = new HttpGet(url);
                    HttpResponse res = null;
                    res = client.execute(get);
                    HttpEntity resEntity = res.getEntity();
                    if (resEntity != null) {
                        Message msg = Message.obtain();
                        msg.obj = new String(EntityUtils.toByteArray(resEntity), "UTF-8");
                        msg.what = 1;
                        handle.sendMessage(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }
    public final Handler handle = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.what==1)
            {
                ScheduleList.clear();
                scheduleAdapter.reset();
                try {
                    JSONObject order = new JSONObject((String) msg.obj);
                    JSONArray resultList = order.getJSONArray("resultList");
                    for(int i=0;i<resultList.length();i++) {

                        String[] a =resultList.getJSONObject(i).getString("sdate").split("-");
                        Schedule mSchedule = new Schedule(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]),resultList.getJSONObject(i).getString("subject"),
                                resultList.getJSONObject(i).getString("content"),resultList.getJSONObject(i).getString("name"),resultList.getJSONObject(i).getString("place"),
                                resultList.getJSONObject(i).getString("id"),resultList.getJSONObject(i).getString("sdate"),resultList.getJSONObject(i).getString("stime"),
                                resultList.getJSONObject(i).getString("edate"),resultList.getJSONObject(i).getString("etime"),resultList.getJSONObject(i).getString("sid"));
                        ScheduleList.add(mSchedule);
                        scheduleAdapter.add(mSchedule);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                scheduleAdapter.notifyDataSetChanged();
                dayList.clear();
                mCal.set(year, month-1, 1);
                dayNum = mCal.get(Calendar.DAY_OF_WEEK);
                for (int i = 1; i <  dayNum; i++) {
                    dayList.add("");
                }

                setCalendarDate(mCal.get(Calendar.MONTH));
                gridAdapter = new GridAdapter(CalendarActivity.this, dayList);
                gridView.setAdapter(gridAdapter);
            }
        }
    };

    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    @Override
    public void onClick(View v) {
        Thread mThread;
        Intent intent;
        switch (v.getId()) {
            case R.id.home:
                intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            case R.id.back:
                finish();
                break;

            case R.id.noti:
                intent = new Intent(CalendarActivity.this, WebPage.class);
                intent.putExtra("page", Global.URL + "app/php/bbs_list.php");
                startActivity(intent);
                break;

            case R.id.today:
                month = today_month;
                year = today_year;
                if(month<10)
                    tvDate.setText(year + ".0" + month);
                else
                    tvDate.setText(year + "." + month);
                mThread = new Thread() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String url;
                            if(month<10)
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-0" + month;
                            else
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-" + month;
                            HttpGet get = new HttpGet(url);
                            HttpResponse res = null;
                            res = client.execute(get);
                            HttpEntity resEntity = res.getEntity();
                            if (resEntity != null) {
                                Message msg = Message.obtain();
                                msg.obj = new String(EntityUtils.toByteArray(resEntity), "UTF-8");
                                msg.what = 1;
                                handle.sendMessage(msg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();
                break;
            case R.id.prev:
                month--;
                if(month<1)
                {
                    year--;
                    month+=12;
                }
                if(month<10)
                    tvDate.setText(year + ".0" + month);
                else
                    tvDate.setText(year + "." + month);

                mThread = new Thread() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String url;
                            if(month<10)
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-0" + month;
                            else
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-" + month;
                            HttpGet get = new HttpGet(url);
                            HttpResponse res = null;
                            res = client.execute(get);
                            HttpEntity resEntity = res.getEntity();
                            if (resEntity != null) {
                                Message msg = Message.obtain();
                                msg.obj = new String(EntityUtils.toByteArray(resEntity), "UTF-8");
                                msg.what = 1;
                                handle.sendMessage(msg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();

                break;

            case R.id.next:
                month++;
                if(month>12)
                {
                    year++;
                    month-=12;
                }
                if(month<10)
                    tvDate.setText(year + ".0" + month);
                else
                    tvDate.setText(year + "." + month);

                mThread = new Thread() {
                    public void run() {
                        try {
                            HttpClient client = new DefaultHttpClient();
                            String url;
                            if(month<10)
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-0" + month;
                            else
                                url = Global.URL+"app/schedule_list.php?datetime="+year+ "-" + month;
                            HttpGet get = new HttpGet(url);
                            HttpResponse res = null;
                            res = client.execute(get);
                            HttpEntity resEntity = res.getEntity();
                            if (resEntity != null) {
                                Message msg = Message.obtain();
                                msg.obj = new String(EntityUtils.toByteArray(resEntity), "UTF-8");
                                msg.what = 1;
                                handle.sendMessage(msg);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mThread.start();
                break;

            case R.id.bt_bottom:
                if(gridView.getVisibility()== View.GONE) {
                    gridView.setVisibility(View.VISIBLE);
                    week.setVisibility(View.VISIBLE);
                    bt_bottom.setBackgroundResource(R.drawable.btn_close);
                }
                else {
                    gridView.setVisibility(View.GONE);
                    week.setVisibility(View.GONE);
                    bt_bottom.setBackgroundResource(R.drawable.btn_open);
                }
                break;

            case R.id.top_list:
                intent = new Intent(CalendarActivity.this, SideMenu.class);
                startActivity(intent);
                break;


        }
    }

    private class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.schedule_date, parent, false);
                holder = new ViewHolder();
                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);
                holder.layout = (LinearLayout)convertView.findViewById(R.id.tv_item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            boolean isSchedule = false;
            for(int i=0;i<ScheduleList.size();i++)
            {
                if(year == ScheduleList.get(i).year && month == ScheduleList.get(i).month && ((position - dayNum + 2) >= ScheduleList.get(i).day && (position - dayNum + 2) <= ScheduleList.get(i).eday))
                {
                    holder.tvItemGridView.setBackgroundResource(R.drawable.ic_today);
                    holder.tvItemGridView.setTextColor(Color.parseColor("#ffffff"));
                    final int finalI = i;
                    holder.tvItemGridView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            Intent intent = new Intent(CalendarActivity.this, ScheduleSub.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("day", ScheduleList.get(finalI).day);
                            intent.putExtra("info", ScheduleList.get(finalI).subject);
                            CalendarActivity.this.startActivity(intent);
                               */
                            Intent intent = new Intent(CalendarActivity.this, WebPage.class);
                            intent.putExtra("page", Global.URL+"app/php/schedule_view.php?number="+ScheduleList.get(finalI).sid);
                            startActivity(intent);
                        }
                    });
                    isSchedule = true;
                    break;
                }
            }

            if(!isSchedule) {
                switch (position % 7) {
                    case 0:
                        holder.tvItemGridView.setTextColor(Color.parseColor("#ff0000"));
                        break;
                    case 6:
                        holder.tvItemGridView.setTextColor(Color.parseColor("#0000ff"));
                        break;
                    default:
                        holder.tvItemGridView.setTextColor(Color.parseColor("#000000"));
                        break;
                }
            }
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
            if (today_year == year && today_month == month) {
                if ((dayNum + today_day - 2) / 7 == position / 7) {
                    if ((dayNum + today_day - 2) % 7 == position % 7)
                        holder.layout.setBackgroundColor(Color.parseColor("#dedede"));
                    else
                        holder.layout.setBackgroundColor(Color.parseColor("#f1f1f1"));
                }
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
        LinearLayout layout;
    }

    class Schedule {
        public int year;
        public int month;
        public int day;
        public int eday;
        public String subject;
        public String content;
        public String name;
        public String place;
        public String id;
        public String sdate;
        public String stime;
        public String edate;
        public String etime;
        public String sid;


        Schedule(int year, int month, int day, String subject, String content, String name, String place, String id, String sdate, String stime, String edate, String etime, String sid)
        {
            this.year = year;
            this.month = month;
            this.day = day;
            this.subject = subject;
            this.content = content;
            this.name = name;
            this.place = place;
            this.id = id;
            this.sdate = sdate;
            this.stime = stime;
            this.edate = edate;
            this.etime = etime;
            this.sid = sid;
            if(edate != null && edate != "" && edate != " ")
            {
                String[] a = edate.split("-");
                if(a.length>2) {
                    eday = Integer.parseInt(a[2]);
                }
                else
                {
                    eday=day;
                }
            }
            else
            {
                eday=day;
            }

        }
    }

    class ScheduleAdapter extends ArrayAdapter {

        List ScheduleList = new ArrayList();
        public ScheduleAdapter (Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        public void add(Schedule object) {
            ScheduleList.add(object);
            super.add(object);
        }

        public void reset() {
            ScheduleList.clear();
        }

        @Override
        public int getCount() {
            return ScheduleList.size();
        }

        @Override
        public Schedule getItem(int index) {
            return (Schedule) ScheduleList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Schedule schedule = (Schedule) ScheduleList.get(position);
            View row = convertView;


            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.schedule_list, parent, false);

            TextView day = (TextView) row.findViewById(R.id.day);
            day.setText(""+schedule.day);
            Log.d("hgkim","schedule.edate : " + schedule.edate);
            if(schedule.edate != null && schedule.edate != "" && schedule.edate != " ")
            {
                String[] a = schedule.edate.split("-");
                if(a.length>2)
                day.setText(""+schedule.day+"-"+a[2]);
            }
            // Inflater를 이용해서 생성한 View에, ChatMessage를 삽입한다.
            TextView info = (TextView) row.findViewById(R.id.info);
            info.setText(schedule.subject);

            return row;
        }
    }
}
