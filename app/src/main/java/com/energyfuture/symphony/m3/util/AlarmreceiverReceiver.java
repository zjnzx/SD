package com.energyfuture.symphony.m3.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.mqttservice.PushService;

import java.text.DecimalFormat;

/**
 * 启动消息接收服务，在标题栏显示系统，点击后可进入系统
 * Created by 超 on 2015/4/9.
 */
public class AlarmreceiverReceiver extends BroadcastReceiver {

    private Context context;
    private static DecimalFormat df = new DecimalFormat("###,###,###,###.0000");
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("arui.alarm.action"))
        {
            Intent newIntent = new Intent(context, PushService.class);
            context.startService(newIntent);
            Log.i("TAG", "启动消息服务...");
            this.context = context;
            showNotification(context);
        }
    }
    public static void showNotification(Context context) {
        Float val = URLs.flowMap.get("total");
        String xs = "";
        if(Base.flowStatus) {
            if (val != null) {
                xs = "  流量(" + df.format(val) + " KB)";
            }
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("变压器专业巡检综合数据应用平台" + xs)
                        .setContentText("系统正在运行中，请放心使用！")
                        .setWhen(System.currentTimeMillis())
                        .setOngoing(true)
                        .setAutoCancel(true);
        Intent intent = new Intent(BringToFrontReceiver.ACTION_BRING_TO_FRONT);
        mBuilder.setContentIntent(PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(99999990, mBuilder.build());

    }

}
