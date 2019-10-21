package com.m2comm.voting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.asthma.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

public class SelectedNumber extends Activity {

    private int num;
    private TextView iv_num;
    private Timer timer;
    int count=0;
    private static final int ERROR_DADABASE = 0xF0000010;
    private static final int ERROR_QUIZ_STATE = 0xF000000B;
    private static final int ERROR_ALREADY_SUBMITTED = 0xF000000E;
    Bitmap bitmap = null;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_number);
        getWindow().setWindowAnimations(0);
        num = getIntent().getIntExtra("selected num", 0);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        iv_num = (TextView)findViewById(R.id.imageView1);
        iv_num.setText(""+num);

        //InitViewSize();

        Thread th = new Thread(new SendAnswer());
        th.start();

        /*
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inDither = true;

        options.inSampleSize = 2;
        switch(num)
        {
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voting_num1, options);
                iv_num.setImageBitmap(bitmap);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voting_num2, options);
                iv_num.setImageBitmap(bitmap);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voting_num3, options);
                iv_num.setImageBitmap(bitmap);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voting_num4, options);
                iv_num.setImageBitmap(bitmap);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voting_num5, options);
                iv_num.setImageBitmap(bitmap);
                break;
        }

        InitViewSize();

        for(int i=0;i<500;i++) {
            Thread th = new Thread(new SendAnswer());
            th.start();
        }
        */
    }


    @Override
    public void onDestroy() {
        if(bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        super.onDestroy();
    }


    public void InitViewSize()
    {
        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        float widthScale = (float) width / 720;
        float heightScale = (float) height / 1280;
        if (width != 720 || height != 1280)
        {
            iv_num.getLayoutParams().width = (int) Math.ceil(widthScale * iv_num.getLayoutParams().width);
            iv_num.getLayoutParams().height = (int) Math.ceil(heightScale * iv_num.getLayoutParams().height);
        }
        //iv_num.setTextSize((int) Math.ceil(widthScale * iv_num.getLayoutParams().width*2/9));
    }

    private TimerTask Switcher = new TimerTask()
    {
        @Override
        public void run() {
            EndActivity();
        }
    };

    public void EndActivity()
    {
        this.finish();
    }


    Handler handle = new Handler()
    {
        public void handleMessage(Message msg1)
        {

            switch(msg1.what)
            {
                case 1:
                    Toast.makeText(getApplicationContext(),getString(R.string.voting_no),Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(getApplicationContext(),getString(R.string.voting_overlap),Toast.LENGTH_SHORT).show();
                    break;

                case -1:
                    Toast.makeText(getApplicationContext(),getString(R.string.network_error),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public class SendAnswer extends Thread
    {
        boolean flag = true;
        int errorCount = 0;
        public void run() {

            while (flag) {
                try {
                    InetAddress serverAddr = InetAddress.getByName(Global.ip);
                    Socket socket = new Socket(serverAddr, 13001);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String loginMsg = "<voting><request><device_type>user</device_type><type>quiz_result</type><id>" + phoneNumber + "</id><lectureid>0</lectureid><quizid>0</quizid><selected>" + num + "</selected></request></voting>";
                    String sendMsg = new String(Base64.encode(loginMsg.getBytes(), 0));

                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(sendMsg);

                    String received;

                    String recvMsg = "";
                    String content = "";

                    int b = 0;

                    Thread.sleep(100);

                    socket.setSoTimeout(2500);

                    try {
                        do {
                            char[] buf = new char[64];
                            b = in.read(buf, 0, 64);

                            received = String.copyValueOf(buf);
                            received.split("\0");
                            content += received;
                            Thread.sleep(1);
                        } while (b > 63);
                    } catch (java.net.SocketTimeoutException e) {
                        errorCount++;
                        if (errorCount > 4) {
                            handle.sendEmptyMessage(-1);
                            flag = false;
                        }
                    }

                    //Log.d(tag, "receive complete : " + content.length());
                    socket.close();

                    recvMsg = new String(Base64.decode(content, 0));
                    // Log.d(tag, "decode message : " + recvMsg);
                    int s = recvMsg.indexOf("<rescode>") + 9;
                    int e = recvMsg.indexOf("</rescode>");
                    String strErrorCode = recvMsg.substring(s, e);
                    int errorCode = Integer.parseInt(strErrorCode);

                    if (errorCode == ERROR_QUIZ_STATE)
                        handle.sendEmptyMessage(1);
                    else if (errorCode == ERROR_ALREADY_SUBMITTED)
                        handle.sendEmptyMessage(2);

                    flag = false;

                } catch (SocketTimeoutException e) {
                    errorCount++;
                    if (errorCount > 4) {
                        handle.sendEmptyMessage(-1);
                        flag = false;
                    }
                } catch (Exception e) {
                    handle.sendEmptyMessage(-1);
                    flag = false;
                }
            }

            timer = new Timer();
            timer.schedule(Switcher, 0, 500);

        }
    }
}
