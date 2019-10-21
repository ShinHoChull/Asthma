package com.m2comm.module;

import java.util.ArrayList;

public class SideMenuClass
    {
        public ArrayList<SubSideMenuClass> SubSideMenuList = new ArrayList<SubSideMenuClass>();
        public String info;
        public String url;
        public SideMenuClass(String info, String url ,ArrayList<SubSideMenuClass> SubSideMenuList)
        {
            this.info = info;
            this.url = url;
            this.SubSideMenuList = SubSideMenuList;
        }
    }