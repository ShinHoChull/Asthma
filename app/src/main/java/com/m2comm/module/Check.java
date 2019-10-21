package com.m2comm.module;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.m2comm.asthma.R;

import whdghks913.tistory.marketversionchecker.MarketVersionChecker;

public class Check {
    Activity context;

    public Check(Activity context) {
        this.context = context;
    }

    public boolean VerCheck() {

        HttpAsyncTask question = new HttpAsyncTask(context, new HttpInterface() {
            @Override
            public void onResult(String result) {
                PackageInfo packageInfo = null;
                try {
                    packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String P_VER = packageInfo.versionName;

                if(result == null || Double.valueOf(result).doubleValue() <= Double.valueOf(P_VER).doubleValue()) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);     // 여기서 this는 Activity의 this

                    // 여기서 부터는 알림창의 속성 설정
                    builder.setTitle("업데이트")        // 제목 설정
                            .setMessage("버전이 업데이트 되었습니다. 구글플레이에서 업데이트 해주세요.")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton){
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("market://details?id="+context.getPackageName()));
                                    context.startActivity(intent);
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();
                }

            }
        });
        question.execute(new HttpParam("url", "http://ezv.kr/version_check.php"),
                new HttpParam("package", context.getPackageName()));

        return true;
    }


    public boolean PermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            context.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //최초 거부를 선택하면 두번째부터 이벤트 발생 & 권한 획득이 필요한 이융를 설명
                if (context.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(context, "You are required to register your authority to save the image", Toast.LENGTH_SHORT).show();

                    //요청 팝업 팝업 선택시 onRequestPermissionsResult 이동
                    context.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);

                }
                //권한이 있는 경우
                else {
                }
            }
        }
        return true;
    }
}
