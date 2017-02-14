package com.energyfuture.symphony.m3.analysis;

/**
 * Created by Administrator on 2015/4/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, GuardService.class);
        context.startService(newIntent);
    }
}
