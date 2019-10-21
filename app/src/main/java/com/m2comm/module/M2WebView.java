package com.m2comm.module;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.m2comm.asthma.R;

import java.net.URLDecoder;

import static android.content.Context.DOWNLOAD_SERVICE;


public class M2WebView extends WebView {

    WebSettings Setting;
    Context context;

    public M2WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public M2WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void Setting(final Context context){
        Setting = getSettings();

        //Setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        Setting.setJavaScriptEnabled(true);
        Setting.setSupportZoom(true);
        Setting.setBuiltInZoomControls(true);
        Setting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        Setting.setJavaScriptEnabled(true);
        Setting.setDisplayZoomControls(true);
        Setting.setLoadWithOverviewMode(true);
        Setting.setUseWideViewPort(true);
        Setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
            {


                AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                        .setTitle(context.getString(R.string.app_name))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })

                        .create();
                dialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.getWindow().setAttributes(layoutParams);
                return true;


            };

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result){
                AlertDialog dialog = new AlertDialog.Builder(view.getContext(),R.style.AlertDialogCustom)
                        .setTitle(context.getString(R.string.app_name))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                })
                        .create();
                dialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                layoutParams.copyFrom(dialog.getWindow().getAttributes());

                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.getWindow().setAttributes(layoutParams);
                return true;

            };

        });

        final CharSequence[] popupItem = {"View", "Save", "Cancel"};
        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                WebView webView = (WebView) v;
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                final String url = hitTestResult.getExtra();
                if(url != null && (url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".jpg"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(context.getString(R.string.app_name))
                            .setItems(popupItem, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                                public void onClick(DialogInterface dialog, int index) {
                                    switch (index) {
                                        case 0:
                                            loadUrl(url);
                                            break;
                                        case 1:
                                            //long now = System.currentTimeMillis();
                                            String fileNames[] = url.split("/");
                                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                            request.allowScanningByMediaScanner();
                                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileNames[fileNames.length - 1]);
                                            request.setTitle(fileNames[fileNames.length - 1]);
                                            DownloadManager dm = (DownloadManager) v.getContext().getSystemService(v.getContext().DOWNLOAD_SERVICE);
                                            dm.enqueue(request);
                                            break;
                                        case 2:
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            });
                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기
                }
                return false;
            }
        });

        this.setDownloadListener(new DownloadListener() {

            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Log.d("hgkim","mimeType : " + mimeType);
                if(mimeType.contains(".pdf"))
                    return;
                try {
                    String fileName = contentDisposition.replace("attachment; filename=", "");
                    fileName = fileName.replaceAll("\"", "");
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    MimeTypeMap mimeTypeMape = MimeTypeMap.getSingleton();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toLowerCase();
                    Log.d("hgkim","fileExtension : " + fileExtension);
                    mimeType = mimeTypeMape.getMimeTypeFromExtension(fileExtension);
                    if(fileExtension.equals("hwp"))
                        mimeType="application/hwp";
                    else if(fileExtension.equals("pdf"))
                        return ;
                    else if(fileExtension.equals("doc") || fileExtension.equals("docx"))
                        mimeType="application/msword";
                    else if(fileExtension.equals("xls") || fileExtension.equals("xlsx") || fileExtension.equals("xlsm"))
                        mimeType="application/vnd.ms-excel";
                    else if(fileExtension.equals("ppt") || fileExtension.equals("pptx"))
                        mimeType="application/vnd.ms-powerpoint";

                    Log.d("hgkim","mimeType : " + mimeType);
                    request.setMimeType(mimeType);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file");
                    Log.d("hgkim","contentDisposition : " + contentDisposition);
                    request.setTitle(URLDecoder.decode(fileName, "utf-8"));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(context, "Downloading File", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    /*
                    if (ContextCompat.checkSelfPermission(WebPage.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(WebPage.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(WebPage.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        } else {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(WebPage.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }
                    */
                }


            }
        });


        NotificationManager nm =
                (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        nm.cancel(1);
    }
    public M2WebView(final Context context) {
        super(context);
        this.context = context;

    }

}
