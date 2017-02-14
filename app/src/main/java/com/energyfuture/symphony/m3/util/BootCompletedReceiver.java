package com.energyfuture.symphony.m3.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.energyfuture.symphony.m3.analysis.MessageHandlerService;

/**
 * 开机启动消息接收服务
 * Created by 超 on 2015/4/9.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent mintent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(mintent.getAction())) {
            // 启动完成
            //开启数据发送给云平台的服务
            Intent newIntent = new Intent(context, MessageHandlerService.class);
            context.startService(newIntent);
            //开启接收云平台消息的服务
            Intent intent = new Intent(context, AlarmreceiverReceiver.class);
            intent.setAction("arui.alarm.action");
            PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                    intent, 0);
            long firstime = SystemClock.elapsedRealtime();
            AlarmManager am = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            // 10秒一个周期，不停的发送广播
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
                    60 * 1000, sender);
        }
    }
}