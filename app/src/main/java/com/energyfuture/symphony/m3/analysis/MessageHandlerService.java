package com.energyfuture.symphony.m3.analysis;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.DualThreadService;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympMessage;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.domain.TrDataSynchronization;
import com.energyfuture.symphony.m3.util.BitmapUtils;
import com.energyfuture.symphony.m3.util.BringToFrontReceiver;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.DBHelper;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.util.UplodaFile;
import com.energyfuture.symphony.m3.util.Utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据发送操作类
 * @author   上海艾飞能源科技有限公司
 * @version  1.0
 * @since    2015-4-16
 */
public class MessageHandlerService extends Service {
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(this);
    private Context context = sympMessageRealDao.getContent();
    private DBHelper dbHelper = new DBHelper(context);
    public static int MessageCount;
    public MessageHandlerService(){
        super();
    }

    static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    //设置消息调用时间间隔
    private  int timeCount = 5000;
    //设置台账调用时间间隔
    private  int LedgertimeCount = 3600000;
    //标记线程是否在运行中，
    public static boolean flagRunnable = true;
    //标记图片上传线程是否在运行中，
    private static boolean flagUploadFileRunnableGeneral = true;
    //0:自动发送 1：wifi发送 2：手动发送
    public static String SENDMESSAGEFLAG = "0";


    // 用于判断进程是否运行
    private String Process_Name = "com.energyfuture.symphony.debug:guardService";

    /**
     *启动GuradService
     */
    private DualThreadService guradService = new DualThreadService.Stub(){
        @Override
        public void stopService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), GuardService.class);
            getBaseContext().stopService(i);
        }

        @Override
        public void startService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), GuardService.class);
            getBaseContext().startService(i);
        }
    };

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        keepGuradService();
    }

    /**
     * 判断GuradService是否还在运行，如果不是则启动GuradService
     */
    private  void keepGuradService(){
        boolean isRun = Constants.isProessRunning(MessageHandlerService.this, Process_Name);
        if (isRun == false) {
            try {
//                Toast.makeText(getBaseContext(), "重新启动 GuradService", Toast.LENGTH_SHORT).show();
                guradService.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, MessageHandlerService.this, MessageHandlerService.this.toString());
            }
        }
    }

    //设置请求访问action地址
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder)guradService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        keepGuradService();
        SharedPreferences sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        department = sharedPreferences.getString("department","");
        //启动消息轮训
        this.startMessage();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("MESSAGE");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        //重新开启服务
        Intent sevice = new Intent(this, MessageHandlerService.class);
        this.startService(sevice);
        //启动消息轮训
        this.stopMessage();
    }

    private static void insertOrDelete( List<Map> result,   List<String> listUUID,Context context){
        //组装数据将消息信息插入历史表中
        List<SympMessage> messageList = new ArrayList<SympMessage>();
        if(result != null && result.size() > 0){
            for(Map map : result){
                SympMessage message = new SympMessage();
                message.setContent(map.get("CONTENT").toString());
                try {
                    message.setCreatedate(dateformat.parse(map.get("CREATEDATE").toString()));
                    message.setSenddate(dateformat.parse(dateformat.format(new Date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }
                message.setFcomment(map.get("FCOMMENT").toString());
                message.setMessageid(map.get("MESSAGEID").toString());
                message.setMessagestate("3");
                message.setMessagetype(map.get("MESSAGETYPE").toString());
                message.setPresonid(map.get("PRESONID").toString());
                messageList.add(message);
            }
        }

        //删除实时表消息，同步到历史记录表中
        if( messageList.size() > 0 && listUUID.size() > 0 && messageList.size() == listUUID.size()){
            SympMessageRealDao messageRealDao = new SympMessageRealDao(context);
            messageRealDao.deleteMessageData(listUUID);
            messageRealDao.insertMessageListData(messageList);
            listUUID.clear();
            messageList.clear();
        }

    }

    //    List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
    //获取服务绝对路径
    private static final String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    //取得图片绝对路径
    private static final String FILE_SAVEPATH = pathTemp.substring(0,pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private static final String FILE_SAVEPATH_COMM = pathTemp.substring(0,pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/";
    private static String department = "";

    public static  void queryMessage(final List<Map<String,String>> datas,final Handler handler, final Context context){
        //请求http方法
        MessageCount=0;

        Runnable downloadRun = new Runnable(){
//        new Thread(){

            @Override
            public void run() {
                try {
                    flagRunnable = false;
//                Looper.prepare();//屏蔽异常
                    //先获取数据
//                List<Map<String,String>> datas = quertDatas();
                    if (datas != null && datas.size() > 0) {
                        Log.i("message", "开始发送文本消息，共"+ datas.size()+"条");
                        for (int i = 0; i < datas.size(); i++) {
                            Message msg=new Message();
                            Map<String, String> bean = datas.get(i);
                            if (bean != null && bean.size() > 0) {
                                //现将当前map对象转成json对象
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("MESSAGEID", bean.get("MESSAGEID"));
                                params.put("MESSAGETYPE", bean.get("MESSAGETYPE"));
                                params.put("PRESONID", bean.get("PRESONID"));
                                SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                department = sharedPreferences.getString("department", "");
                                Log.i("message", "发送消息获取到的值是：" + department);
                                if (department == null || "".equals(department)) break;
                                params.put("DEPARTMENT", "800000000000000001");
                                //判断如果是图片，需要将图片信息封装到json数据中
                                params.put("CONTENT", bean.get("CONTENT"));
                                Map map = null;
                                Log.i("message", "开始发送第" + (i + 1) + "条文本消息，messageID=" + bean.get("MESSAGEID"));
                                try {

                                    map = ApiClient.httpPostMessageData(null, params, "send to web：" + bean.get("MESSAGEID"), context, "文本");
                                    //记录当前发送成功的消息信息，用于删除操作
                                    if (map != null && map.size()!=0) {
//                                        Log.i("message", "上传数据接收到服务端返回状态:" + map.get("STATE"));
//                                        Log.i("message", "上传数据接收到服务端返回ID:" + map.get("MESSAGEID"));

                                        MessageCount++;
                                        Log.i("message", "第" + (i + 1) + "条文本上传成功，接收到web端反馈，messageID=" + bean.get("MESSAGEID") + "返回");
                                        baseDataInserOrUpdate(bean, bean.get("MESSAGEID"), context);
                                    } else {
                                        Log.i("message", "第" + (i + 1) + "条文本消息发送失败，=====失败===== messageID=" + bean.get("MESSAGEID"));
                                    }

                                    msg.obj = map;
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                  map=null;
                                    Constants.recordExceptionInfo(e, context, context.toString());
                                }
                            }
                            if (Utils.isstop){
                                break;
                            }
                            handler.sendMessage(msg);
                        }
                    }

                } catch (Exception e) {
                    Log.e("queryMessage","文本消息发送失败！"+e.getMessage());
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
//                  map=null;
                }finally {
                    flagRunnable = true; //标记线程可执行

                }
            }

        };
        new Thread(downloadRun).start();
        Utils.isstop=false;

    }


    /**
     * 图片上传
     * @param
     */
    public static void uploadFile(final List<Map<String,String>> datas,final Handler handler,final Context context){
        //请求http方法
        Runnable downloadRun = new Runnable(){
            @Override
            public void run() {
                try {
                    flagUploadFileRunnableGeneral = false;
                    if(datas != null && datas.size() > 0){
                        Log.i("message", "开始发送文件(普通图片)消息，共"+ datas.size()+"条");
                        for (int i = 0; i < datas.size(); i++){
                            Message msg = new Message();
                            Map<String,String> bean = datas.get(i);
                            if(bean != null){
                                //现将当前bean对象转成json对象
                                Map<String, Object> params = new HashMap<String, Object>();
                                params.put("PROJECTID", bean.get("PROJECTID"));
                                params.put("TASKID", bean.get("TASKID"));
                                params.put("MESSAGETYPE", bean.get("MESSAGETYPE"));
                                if(bean.get("PROJECTID") != null && bean.get("TASKID") != null) {
                                    params.put("TYPE", "IMG");
                                }else if(bean.get("MESSAGESTATE").equals("2")){//沟通交流
                                    params.put("TYPE", "COMM");
                                    params.put("ID", bean.get("THEMEID"));
                                }else if(bean.get("MESSAGESTATE").equals("1")){ //反馈
                                    params.put("TYPE", "FEEDBACK");
                                    params.put("ID", bean.get("THEMEID"));
                                }
                                params.put("CUSTOMER", "");
//                                if (department == null || "".equals(department))
//                                    break;
                                params.put("DEPARTMENT", "800000000000000001");
                                params.put("FILENAME", bean.get("FILENAME"));
                                params.put("FILEID", "1");
                                //判断如果是图片，需要将图片信息封装到json数据中
                                String filepath = "";
                                if(bean.get("PROJECTID") != null && bean.get("TASKID") != null) {//其他图片
                                    filepath = FILE_SAVEPATH + bean.get("PROJECTID") + "/" + bean.get("TASKID") + "/original/" + bean.get("FILENAME");
                                }else if(bean.get("MESSAGESTATE").equals("2")){//沟通交流图片
                                    filepath = FILE_SAVEPATH_COMM + "comm/" + bean.get("THEMEID") + "/original/" + bean.get("FILENAME");
                                }else if(bean.get("MESSAGESTATE").equals("1")) {//反馈图片
                                    filepath = FILE_SAVEPATH_COMM + "feedback/" + bean.get("THEMEID") + "/original/" + bean.get("FILENAME");
                                }
                                Log.i("message", bean.get("MESSAGETYPE") + "==" + bean.get("MESSAGESTATE") + "===路径=" + filepath);
                                File file = new File(filepath);

                                //判断文件是否存在
                                if (file.exists()) {
                                    try {
                                        if (BitmapUtils.getFileSizes(file) >= 1024 * 1024) {
                                            BitmapUtils.getThumbUploadPath(filepath, 480, filepath);
                                            file = new File(filepath);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Constants.recordExceptionInfo(e, context, context.toString());
                                    }
                                    try {
                                        //将文件转成二进制字符
                                        String imageByte = ImageUtil.image2String(file);
                                        //然后进行压缩处理
                                        //将压缩后的字符封装到对象中
                                        params.put("FILEINFO", imageByte);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Constants.recordExceptionInfo(e, context, context.toString());
                                    }
                                } else {
                                    continue;
                                }
                                Map map = null;
                                Log.i("message", "开始发送第" + (i + 1) + "条文件(图片)消息，图片名称：" + bean.get("FILENAME") + "--" + bean.get("MESSAGEID"));
                                Log.i("message", "开始发送第" + params);
                                map = ApiClient.httpPostMessageData(null, params, "send file to web：" + bean.get("FILENAME"), context, "文件");
//                                Log.i("message", "====================map=" + map);
                                //记录当前发送成功的消息信息，用于删除操作
                                if (map != null && map.get("STATE").equals("1")) {
                                    MessageCount++;
                                    Log.i("message", "第" + (i + 1) + "条文件(图片)上传成功，接收到web端反馈，图片名称：" + bean.get("FILENAME"));
                                    SympMessageRealDao messageRealDao = new SympMessageRealDao(context);
                                    SympMessageReal sympMessageReal = new SympMessageReal();
                                    sympMessageReal.setMessageid(bean.get("MESSAGEID"));
//                                    Log.i("====发送成功后=============消息表中上传的图片", bean.get("MESSAGEID") + "--" + bean.get("FILENAME"));
                                    List<Map<String, String>> messageList = messageRealDao.querySympMessageRealByParameter(sympMessageReal);
                                    Map messageMap = new HashMap();
                                    if (messageList.size() > 0) {
                                        messageMap = messageList.get(0);
                                    }
                                    //删除消息表数据并且往历史表中插入本条数据
                                    baseDataInserOrUpdate(messageMap, bean.get("MESSAGEID"), context);
                                } else {
                                    Log.i("message", "第" + (i + 1) + "条文件(图片)上传失败，=====失败===== 接收到web端反馈，图片名称：" + bean.get("FILENAME"));
                                }
                                msg.obj=map;
                            }
                            if (Utils.isstop){
                                break;
                            }
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    Log.e("uploadFIle","文件(图片)消息发送失败"+e.getMessage());
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString());
                }finally {
                     flagUploadFileRunnableGeneral = true; //标记线程可执行
                }
            }
        };
        new Thread(downloadRun).start();
        Utils.isstop=false;
    }


    private static void baseDataInserOrUpdate(  Map bean,  String uuid,Context context){
        List<Map> result = new ArrayList();
        List<String> listUUID = new ArrayList<String>();
        if(bean != null && bean.size() > 0 && uuid != null && !uuid.equals("")){
            result.add((Map) bean);
            listUUID.add(String.valueOf(uuid));
        }
        //插入数据和删除数据
        insertOrDelete(result,listUUID,context);
    }


    /**
     * 消息发送方法
     * @designer
     * @author   zhaogr
     * @version  1.0
     * @since    2015-4-16
     */
    private void MessageSend(){
        //有网络连接
        if (SENDMESSAGEFLAG.equals("0") && Network.isNetworkConnected(context)){
            Log.i("*************网络连接*************","**"+SENDMESSAGEFLAG);
            upLoadMessage();
        }else if(SENDMESSAGEFLAG.equals("1") && Network.isWifiConnected(context)){ //WiFi连接情况下
            Log.i("*************wifi连接*************","**"+SENDMESSAGEFLAG);
            upLoadMessage();
        }

//        Log.i("*************测试打开或停止服务*************","**"+SENDMESSAGEFLAG);
    }

    private void upLoadMessage(){
        if (flagRunnable) {
            List<Map<String, String>> datas = quertDatas("1");
            Handler handler2=new Handler();
            if (datas != null && datas.size() > 0) {
                queryMessage(datas,handler2,this);
                flagRunnable = false;
            }
        }
        if (flagUploadFileRunnableGeneral) {
            //查询消息表待上传图片
            List<Map<String, String>> messageList = quertDatas("2");
            Handler handler2=new Handler();
            if (messageList != null && messageList.size() > 0) {
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for(int i = 0;i < messageList.size();i++){
                    String content = messageList.get(i).get("CONTENT");
                    String messageid = messageList.get(i).get("MESSAGEID");
                    String messagetype = messageList.get(i).get("MESSAGETYPE");
                    String messageState = messageList.get(i).get("MESSAGESTATE");
                    String[] str = content.split(",");
                    Log.i("=========Messageid========map1111=",messageList.get(i).get("MESSAGESTATE") + "====id="+str[1]);
                    Map<String,String> map = queryFileByTableName(str[0],str[1]);
                    if(map != null){
                        map.put("MESSAGEID",messageid);
                        map.put("MESSAGETYPE",messagetype);
                        map.put("MESSAGESTATE",messageState);
                        list.add(map);
                    }
                }
//                    Log.i("=========Messageid========list222=",list.size()+"");
                uploadFile(list,handler2,this);
                flagUploadFileRunnableGeneral = false;
            }

        }
    }

    /**
     * 根据表名和id查询待上传的图片
     * @return
     */
    private Map<String,String> queryFileByTableName(String tableName,String id){
        String sql = "";
        if(!tableName.equals("")) {
            sql = "select * from " + tableName + " where id = '" + id + "'";
        }
//        Log.i("=========queryFileByTableName=========",sql);
        List<Map<String,String>> list = dbHelper.selectSQL(sql,null);
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取当前消息数据
     * @designer
     * @author   zhaogr
     * @version  1.0
     * @since    2015-4-16
     */
    private List<Map<String,String>> quertDatas(String messageType){
        SympMessageRealDao messageRealDao = new SympMessageRealDao(getBaseContext());
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setMessagetype(messageType);
        List<Map<String,String>> datas  = messageRealDao.querySympMessageRealByParameter(sympMessageReal);
        return datas;
    }

    //定时器操作类
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //判断如果当前消息表中没有数据，则先将调度停掉,如果有数据，则将数据交给发送程序进行平台同步
            MessageSend();

            handler.postDelayed(this, timeCount);
        }
    };

    /**
     * 停止消息扫描发送
     * @designer
     * @author   zhaogr
     * @version  1.0
     * @since    2015-4-16
     */
    public  void stopMessage(){
        handler.removeCallbacks(runnable);
    }

    /**
     * 开始消息扫描发送
     * @designer
     * @author   zhaogr
     * @version  1.0
     * @since    2015-4-16
     */
    public  void startMessage(){
        handler.postDelayed(runnable, timeCount);//每"timeCount"秒执行一次runnable.
//        台账发送
        handler1.postDelayed(runnable1, LedgertimeCount);//每"LedgertimeCount"秒执行一次runnable1.
    }

//台账数据定时请求刷新接收文件下载保存
//    SharedPreferences  sharedPreferences =this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//    String userId = sharedPreferences.getString("userId","");

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
                                    String url = URLs.HTTP+ URLs.HOST+"/"+sys.getFileurl()+"/"+sys.getFilename();
                                    UplodaFile.downFile(url, sys);
                                }
                            }
                        }
                    }.start();

                }else{

                }
            }else{
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //定时器操作类台账
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            LedgerDataSend();

            handler1.postDelayed(this, LedgertimeCount);
        }
    };
    /**
     * 台账数据定时请求刷新
     *
     * 2015/8/13
     */
    private void LedgerDataSend(){

        Map<String,Object> condMap1 = new HashMap<>();
        condMap1.put("OBJ","LEDGER");
        condMap1.put("OPELIST",null);
        condMap1.put("TYPE",null);
        condMap1.put("SIZE",5);
        condMap1.put("USERID","guest");
//        new DataSyschronized(getBaseContext()).getDataFromWeb(condMap1,handler1);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("MESSAGE")){
                MessageHandlerService.SENDMESSAGEFLAG = intent.getStringExtra("type");
            }
        }

    };
}
