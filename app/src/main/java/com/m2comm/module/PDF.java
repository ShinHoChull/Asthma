package com.m2comm.module;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class PDF {
    Activity context;
    String url;
    private LoadingDialog dialog;
    private String m_url;
    boolean pdfReadYN = false;
    boolean downYN=false;
    String Path;
    String strRes;
    public PDF(Activity context, String url)
    {
        this.context = context;
        this.url = url;

        if(!pdfReadYN) {
            pdfReadYN = true;
            m_url = url;
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = null;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ApplicationInfo ai = pi.applicationInfo;

            String filePath = ai.dataDir + "/IFOS/";

            String filenames[] = url.split("/");
            filenames[filenames.length-1] = filenames[filenames.length-1].replace(" ", "_");
            try {
                filenames[filenames.length-1] = URLDecoder.decode(filenames[filenames.length-1], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Path = filePath + filenames[filenames.length-1];


            dialog = new LoadingDialog(context);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            Thread th = new Thread(new PDFDownThread());
            th.start();
        }
    }


    class PDFDownThread extends Thread
    {
        public void run() {
            if (downYN || new File(Path).exists() == false) {
                downYN = false;
                try {
                    HttpClient client = new DefaultHttpClient();
                    String url = m_url;
                    HttpGet get = new HttpGet(url);
                    HttpResponse res = null;
                    PackageManager pm = context.getPackageManager();
                    PackageInfo pi = null;
                    try {
                        pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ApplicationInfo ai = pi.applicationInfo;
                    String filePath = ai.dataDir + "/IFOS/";

                    res = client.execute(get);
                    strRes = "1";
                    if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        strRes = "Can not find Abstract.";
                        handle.sendEmptyMessage(0);
                        return;
                    }

                    HttpEntity resEntity = res.getEntity();
                    if (resEntity != null) {
                        if (new File(filePath).exists() == false) {
                            new File(filePath).mkdirs();
                        }
                        InputStream is = null;
                        is = resEntity.getContent();
                        File file = new File(Path);
                        FileOutputStream fs = new FileOutputStream(file);

                        byte[] buffer = new byte[2048000];
                        int bufferLen = 0;
                        while ((bufferLen = is.read(buffer)) > 0) {
                            fs.write(buffer, 0, bufferLen);
                        }
                        fs.flush();
                        fs.close();

                    } else {
                        strRes = "File open error.";
                        handle.sendEmptyMessage(0);
                        return;
                    }

                    handle.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    strRes = "Can not find Abstract.";
                    handle.sendEmptyMessage(0);
                }
            } else {
                handle.sendEmptyMessage(1);
            }
        }
    }
    final Handler handle = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0:
                    Toast.makeText(context, strRes, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    pdfReadYN = false;
                    break;
                case 1:

                    Intent i = new Intent(context, PdfActivity.class);
                    i.putExtra("url", Path);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                    dialog.dismiss();
                    pdfReadYN = false;
                    break;

            }
        }
    };
}
