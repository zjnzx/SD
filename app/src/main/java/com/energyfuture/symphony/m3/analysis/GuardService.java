package com.energyfuture.symphony.m3.analysis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.energyfuture.symphony.m3.activity.DualThreadService;
import com.energyfuture.symphony.m3.util.Constants;

/**
 * Created by Administrator on 2015/8/21.
 */
public class GuardService extends Service {
    // 用于判断进程是否运行
    private String Process_Name = "com.energyfuture.symphony.debug:messageHandlerService";

    /**
     *启动GuradService
     */
    private DualThreadService messageHandlerService = new DualThreadService.Stub(){
        @Override
        public void stopService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), MessageHandlerService.class);
            getBaseContext().stopService(i);
        }

        @Override
        public void startService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), MessageHandlerService.class);
            getBaseContext().startService(i);
        }
    };

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        keepMessageHandlerService();
    }

    /**
     * 判断MessageHandlerService是否还在运行，如果不是则启动MessageHandlerService
     */
    private  void keepMessageHandlerService(){
        boolean isRun = Constants.isProessRunning(GuardService.this, Process_Name);
        if (isRun == false) {
            try {
//                Toast.makeText(getBaseContext(), "重新启动 MessageHandlerService", Toast.LENGTH_SHORT).show();
                messageHandlerService.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, GuardService.this, GuardService.this.toString());
            }
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder)messageHandlerService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Toast.makeText(getBaseContext(),"服务启动",Toast.LENGTH_SHORT).show();
        keepMessageHandlerService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //重新开启服务
        Intent sevice = new Intent(this, GuardService.class);
        this.startService(sevice);
    }
}
