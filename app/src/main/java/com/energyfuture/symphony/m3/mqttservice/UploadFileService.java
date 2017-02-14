package com.energyfuture.symphony.m3.mqttservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.UplodaFile;

import java.util.List;

public class UploadFileService extends Service {
    public UploadFileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        List<TrDataSynchronization> data = (List<TrDataSynchronization>) bundle.getSerializable("data");
        for(TrDataSynchronization sys : data){
            String fileName = sys.getFilename();
            String url =URLs.HTTP+URLs.HOST+"/"+sys.getFileurl()+"/"+sys.getFilename();

            UplodaFile.downFile(url,sys);

        }

        return super.onStartCommand(intent, flags, startId);
    }
}
