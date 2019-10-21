package com.m2comm.module;

import android.app.Application;

public class Global extends Application {
    static public String CODE = "allergy";
    public static String URL = "http://2017spring.allergy.or.kr/";
    public static String Spring2018_URL = "http://2018spring.allergy.or.kr/app/php/";
    public static String Spring2019_URL = "http://2019spring.allergy.or.kr/app/php/";
    public static String Fall2019_URL = "http://2019fall.allergy.or.kr/app/php/";
    public static String Fall2017_URL = "http://2017fall.allergy.or.kr/app/php/";
    public static String Spring2017_URL = "http://2017spring.allergy.or.kr/workshop/2017_Spring/php/";
    public static String Fall2018_URL = "http://2018fall.allergy.or.kr/app/php/";
    public static String Fail2016_URL = "http://2017spring.allergy.or.kr/workshop/2016_Fall/php/";
    public static String FEED_BACK_URL = "http://kpho.m2comm.co.kr/feedback/allergy/feedback.asp?id=";
    public static String QUESTION_URL = "http://121.254.129.104/voting_0523/insert_qna_ksoug.asp";
    @Override
    public void onCreate() {
        super.onCreate();
    }

}
