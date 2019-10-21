package com.m2comm.voting;

import java.util.ArrayList;

public class SideMenuClass
{
    public ArrayList<Lecture.submenu> SubSideMenuList = new ArrayList<Lecture.submenu>();
    public String info;
    public String room;
    public String sid;
    public SideMenuClass(String info, String room, String sid, ArrayList<Lecture.submenu> SubSideMenuList)
    {
        this.info = info;
        this.room = room;
        this.sid = sid;
        this.SubSideMenuList = SubSideMenuList;
    }
}