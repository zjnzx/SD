package com.energyfuture.symphony.m3.mqttservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.energyfuture.symphony.m3.activity.CommunionThemeReplyActivity;
import com.energyfuture.symphony.m3.activity.ProjectListActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.analysis.DataAnalysis;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.AppException;
import com.energyfuture.symphony.m3.common.JsonHelper;
import com.energyfuture.symphony.m3.common.Result;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.util.AlarmreceiverReceiver;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PushService extends Service {
    /* 最重要的三个部分host 地址，client_name,主题：Topic */
    String client_name = "null";
    private String topic = "message/null"; /* 主题 */
    private MqttClient client;
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;
    private static int NOTIF_CONNECTED = 0;
    private  String id;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        startReconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    private void set_notification(String msg,Object obj) {
        NotificationManager notificationManager = (NotificationManager) PushService.this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.ic_launcher,
                "您有一条新的消息，请注意查收！", System.currentTimeMillis());

        // Hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Intent intent;
        if(obj!=null&&obj.equals("trProject")){
            intent = new Intent(PushService.this, ProjectListActivity.class);
        }else{

            intent = new Intent(PushService.this, CommunionThemeReplyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("theme",(SympCommunicationTheme)obj);
            intent.putExtras(bundle);
        }
        PendingIntent activity = PendingIntent.getActivity(getBaseContext(), 0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(PushService.this, "新消息",
                msg, activity);
        notification.number += 1;
        if(obj.equals("trProject")){
            notificationManager.notify(NOTIF_CONNECTED++, notification);
        }else if(obj instanceof SympCommunicationTheme&&!((SympCommunicationTheme) obj).getId().equals(Utils.themeid)){

            notificationManager.notify(NOTIF_CONNECTED++, notification);
        }


    }
    Thread threadSubmit=new Thread(){
        public void run() {
            Looper.prepare();
            boolean flag=true;
            Map<Object,Object> map;
            Log.i("消息服务","提交数据到云平台");
            String message="";
            try {
                String  messageObj = ApiClient.getMessage(null, id, id);// 获取到云平台中的数据信息
                System.out.print("messageObj="+messageObj.toString());
                DataAnalysis dataAnalysis=new DataAnalysis(getBaseContext());
                List<String> list=new ArrayList<String>();
                List<Object> objlist=new ArrayList<Object>();
                map=dataAnalysis.JsonDataAnalysis(messageObj,list);
                Set<Object> set=map.keySet();
                for(Object obj:set){

                    flag=(boolean)map.get(obj);
                }
                for(Object obj:set){
                    if(obj==null||obj.equals("")){
                        set.remove(obj);
                    }
                }
                for(Object obj:set){
                    objlist.add(obj);
                }
                for (int i = 0; i < objlist.size(); i++) {
                    set_notification(list.get(i).toString(),objlist.get(i));
                }
            } catch (AppException e) {
                flag=false;
                e.printStackTrace();
                Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
            }
            if(flag==true)
            {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("DID", id);
                params.put("STATE","1");
                try {
                    Result result = ApiClient.pubPostMessageReturnState(null, params, id);// 获取到云平台中的数据信息
                    System.out.print(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
        try {
            if(client.isConnected()) client.disconnect();
        } catch (org.eclipse.paho.client.mqttv3.MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            //获取本机的 MAC 地址，当值clientid
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
//            client_name = info.getMacAddress();
            client_name = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            //获取本机登录的用户名，当做订阅的主题，如果登录用户没有，则使用 message/null
            SharedPreferences sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String userName = sharedPreferences.getString("userId","");
            if(userName != null && !userName.equals(""))
                topic = "message." + userName;
            else
                topic = "message.null";
            client = new MqttClient(URLs.MQTTHOST, client_name, new MemoryPersistence());


            options = new MqttConnectOptions();
            options.setCleanSession(true);
//            options.setUserName("admin");
//            options.setPassword("admin".toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {}

                @Override
                public void messageArrived(MqttTopic mqttTopic, MqttMessage mqttMessage) throws Exception {
                    System.out.println("messageArrived----------");
                    String msg_get = new String(mqttMessage.getPayload());/* message里面是byte [],要转换一下*/
//                    Log.d("mqtt", "收到 " + mqttTopic.getName() + ":" + msg_get);
                    Map resultMap = JsonHelper.toMap(msg_get);
                    id = resultMap.get("UUID").toString();
//                    id = msg_get;
//                    threadSubmit.start();
                    new Thread() {
                        public void run() {
                            Looper.prepare();
                            boolean flag = true;
                            Map<Object,Object> map=new ConcurrentHashMap<Object, Object>();
                            Set<Object> set=new ConcurrentSkipListSet<Object>();
                            List<Object> objlist=new ArrayList<Object>();
                            Log.i("111", "1111111111");
                            String message = "";//提醒信息
                            List<String> list = new ArrayList<String>();
                            try {
                                if(id != null &&  !id.equals("")){
                                    String messageObj = ApiClient.getMessage(null, id, id);// 获取到云平台中的数据信息
                                    if(messageObj != null && !messageObj.equals("")){
                                        DataAnalysis dataAnalysis = new DataAnalysis(getBaseContext());

                                        map = dataAnalysis.JsonDataAnalysis(messageObj, list);

                                        List<Map<Object, Object>> listData = JsonHelper.converMessageFormString(messageObj);
                                        Map map2 = listData.get(0);
                                        message = (String) map2.get("MESSAGE");
                                        set=map.keySet();
                                        for(Object obj:set){
                                            flag=(boolean)map.get(obj);
                                        }
                                        for(Object obj:set){
                                            objlist.add(obj);
                                        }
                                    }
                                }
                            } catch (AppException e) {
                                flag = false;
                                e.printStackTrace();
                                Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
                            }catch (JSONException e) {
                                e.printStackTrace();
                                Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
                            }
                            if (flag == true) {
                                Map<String, Object> params = new HashMap<String, Object>();
                                if(id != null && !id.equals("")){
                                    params.put("DID", id);
                                    params.put("STATE", "1");
                                    try {
                                        Result result = ApiClient.pubPostMessageReturnState(null, params, id);// 获取到云平台中的数据信息
                                        System.out.print(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
                                    }
                                }
                                for (int i = 0; i < objlist.size(); i++) {
                                    if(objlist.get(i)!=null&&!objlist.get(i).equals("")){
                                        set_notification(message,objlist.get(i));
                                    }
                                }

                            }
                        }
                        //showNotification();
                    }.run();
                }

                @Override
                public void deliveryComplete(MqttDeliveryToken mqttDeliveryToken) {}
            });
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
        }
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        /*
         * 0秒后开始，每隔10秒后等任务结束开始第二个
         * ，不会同时执行
         */
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                AlarmreceiverReceiver.showNotification(getBaseContext());
                if (!client.isConnected()) {
//                    connect(host, client_name, false);
                    connect(); /* 这里进行连接 */
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    client.connect();
                    client.subscribe(topic, Integer.parseInt(System.getProperty("QoS","2"))); /* 这里订阅了主题 */
                } catch (Exception e) {
                    Log.d("mqtt", "连接失败");
                    Constants.recordExceptionInfo(e, PushService.this, PushService.this.toString());
                }
            }
        }).start();
    }
}
