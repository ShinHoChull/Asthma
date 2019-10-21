package com.m2comm.asthma.Fail2016;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m2comm.asthma.R;
import com.m2comm.module.AnimatedExpandableListView;
import com.m2comm.module.SideMenuClass;
import com.m2comm.module.SubSideMenuClass;

import java.util.ArrayList;

public class Fail2016_SideMenuAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

        private LayoutInflater inflater = null;
        private ViewHolder viewHolder = null;
        Context context;
        public ArrayList<SideMenuClass> SideMenuList = new ArrayList();

        public Fail2016_SideMenuAdapter(Context context, int textViewResourceId) {
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
            row = inflater.inflate(R.layout.fail2016_side_menu_item, parent, false);
            TextView name = (TextView) row.findViewById(R.id.name);
            name.setText(MenuInfo.info);

            return row;
        }

        // 차일드뷰를 반환한다.
        @Override
        public SubSideMenuClass getChild(int groupPosition, int childPosition) {
            return SideMenuList.get(groupPosition).SubSideMenuList.get(childPosition);
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
            v = inflater.inflate(R.layout.sub_sidemenu_item, null);
            viewHolder.info = (TextView) v.findViewById(R.id.info);
            viewHolder.info.setText(SideMenuList.get(groupPosition).SubSideMenuList.get(childPosition).info);

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