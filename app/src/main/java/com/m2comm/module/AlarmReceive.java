package com.m2comm.module;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.m2comm.asthma.R;
import com.m2comm.asthma.Spring2019.Spring2019_WebPage;


public class AlarmReceive extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub



		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent2 = new Intent(context, Spring2019_WebPage.class);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		intent2.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code=allergy2019s&tab=-2");

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			Notification.Builder builder = new Notification.Builder(context, "all")
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("["+intent.getStringExtra("msg")+"] 10분 후 시작됩니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
			builder.setContentIntent(contentIntent);
			mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
		}else {



			NotificationCompat.Builder builder =
					new NotificationCompat.Builder(context)
							.setSmallIcon(R.mipmap.ic_launcher)
							.setContentTitle(context.getString(R.string.app_name))
							.setAutoCancel(true)
							.setStyle(new NotificationCompat.BigTextStyle()
									.bigText(intent.getStringExtra("msg")))
							.setContentText(intent.getStringExtra("msg"));
			builder.setContentIntent(contentIntent);
			mNotificationManager.notify((int) (Math.random() * 99999999), builder.build());
		}


	}

}
