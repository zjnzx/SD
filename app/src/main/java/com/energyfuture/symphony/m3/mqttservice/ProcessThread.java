//package com.energyfuture.symphony.m3.mqttservice;
//
//import android.os.Looper;
//import android.util.Log;
//
//import com.energyfuture.symphony.m3.analysis.DataAnalysis;
//import com.energyfuture.symphony.m3.common.ApiClient;
//import com.energyfuture.symphony.m3.common.AppException;
//import com.energyfuture.symphony.m3.common.Result;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Administrator on 2015/4/10.
// */
//public class ProcessThread extends Thread {
//
//    @Override
//    public void run() {
//        Looper.prepare();
//        boolean flag=true;
//        Log.i("111", "1111111111");
//        String message="";
//        try {
//            String  messageObj = ApiClient.getMessage(null, id);// 获取到云平台中的数据信息
//            System.out.print("messageObj="+messageObj.toString());
//            DataAnalysis dataAnalysis=new DataAnalysis(getBaseContext());
//            List<String> list=new ArrayList<String>();
//            flag=dataAnalysis.JsonDataAnalysis(messageObj,list);
//            for(int i=0;i<list.size();i++) {
//                set_notification(list.get(i).toString());
//            }
//        } catch (AppException e) {
//            flag=false;
//            e.printStackTrace();
//        }
//        if(flag==true)
//        {
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("DID", id);
//            params.put("STATE","1");
//            try {
//                Result result = ApiClient.pubPostMessageReturnState(null, params);// 获取到云平台中的数据信息
//                System.out.print(result);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
