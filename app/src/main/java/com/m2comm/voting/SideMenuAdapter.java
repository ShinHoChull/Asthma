package com.m2comm.voting;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.m2comm.asthma.R;

import java.util.ArrayList;

public class SideMenuAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    Context context;
    public ArrayList<SideMenuClass> SideMenuList = new ArrayList();

    public SideMenuAdapter(Context context, int textViewResourceId) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void add(SideMenuClass object) {
        SideMenuList.add(object);
    }

    // 그룹 포지션을 반환한다.
    @Override
    public SideMenuClass getGroup(int groupPosition) {
        return SideMenuList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return SideMenuList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public void reset() {
        SideMenuList.clear();
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View row = convertView;
        final SideMenuClass MenuInfo = (SideMenuClass) SideMenuList.get(groupPosition);
        row = inflater.inflate(R.layout.voting_menu_item, parent, false);
        TextView name = (TextView) row.findViewById(R.id.name);
        name.setText(MenuInfo.info);
        ImageView img = (ImageView) row.findViewById(R.id.img);
        if(isExpanded) {
            row.setBackgroundColor(Color.parseColor("#171668"));
            img.setImageResource(R.drawable.ico_up);
            name.setTextColor(Color.parseColor("#ffffff"));
        }else {
            row.setBackgroundColor(Color.parseColor("#ffffff"));
            img.setImageResource(R.drawable.ico_down);
            name.setTextColor(Color.parseColor("#1a1a1a"));
        }

        return row;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return SideMenuList.get(groupPosition).SubSideMenuList.get(childPosition).title;
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;


        viewHolder = new ViewHolder();
        v = inflater.inflate(R.layout.sub_voting_menu_item, null);
        viewHolder.info = (TextView) v.findViewById(R.id.info);
        viewHolder.info.setText(SideMenuList.get(groupPosition).SubSideMenuList.get(childPosition).title);

        return v;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return SideMenuList.get(groupPosition).SubSideMenuList.size();
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        public TextView info;
    }

}