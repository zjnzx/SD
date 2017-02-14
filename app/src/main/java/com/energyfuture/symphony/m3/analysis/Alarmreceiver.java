package com.energyfuture.symphony.m3.analysis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.UplodaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/18.
 */
public class Alarmreceiver extends BroadcastReceiver {
    private Context context;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/";
    @Override
    public void onReceive(Context context, Intent intent) {
        Map<String,Object> condMap1 = new HashMap<String, Object>();
        condMap1.put("OBJ","LEDGER");
        condMap1.put("OPELIST",null);
        condMap1.put("TYPE",null);
        condMap1.put("SIZE",5);
        condMap1.put("USERID", Constants.getLoginPerson(context).get("userId"));
        new DataSyschronized(context).getDataFromWeb(condMap1,handler1);
        this.context = context;
    }

    final Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1 && msg.obj != null){
                final Map<String,Object> resMap = (Map<String,Object>)msg.obj;
                if("ok".equals(resMap.get("0"))) {
                    new Thread(){
                        @Override
                        public void run() {
                            List<TrDataSynchronization> syncData  = (ArrayList) resMap.get("data");
                            if(syncData.size()>0){
                                for(TrDataSynchronization sys : syncData){
                                    if(sys.getOpertype() != null && sys.getOpertype().equals("1")){
                                        String url = "";
//                                        try {
                                            //URLEncoder.encode(sys.getFileurl(), "UTF-8")   URLEncoder.encode(sys.getFilename(), "UTF-8")
                                            url = URLs.HTTP+ URLs.HOST+"/"+"INSP/"+ sys.getFileurl()+"/"+ sys.getFilename();
                                            UplodaFile.downFile(url, sys);
//                                        } catch (UnsupportedEncodingException e) {
//                                            e.printStackTrace();
//                                        };
                                    }else if(sys.getOpertype() != null && sys.getOpertype().equals("3")){
                                        File file = new File(FILE_SAVEPATH + "file/" + sys.getFileurl() + "/" + sys.getFilename());
                                        UplodaFile.deleteFile(file);
                                    }
                                }
                            }
                        }
                    }.start();

                }else{

                }
            }else{
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
