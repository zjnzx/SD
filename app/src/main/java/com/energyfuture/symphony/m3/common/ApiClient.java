package com.energyfuture.symphony.m3.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.energyfuture.symphony.m3.dao.SympFlowInfoDao;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.domain.SympFlowInfo;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.version.Update;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API客户端接口：用于访问网络数据 (http://my.oschina.net/liux)
 *
 * @version 1.0
 * @created 2012-3-21
 */
public class ApiClient {

    public static final String UTF_8 = "UTF-8";
    public static final String DESC = "descend";

    public static final String ASC = "ascend";

    private final static int TIMEOUT_CONNECTION = 8000;
    private final static int TIMEOUT_SOCKET = 8000;
    private final static int TIMEOUT_SOCKETTWO = 90000;
    private final static int RETRY_TIME = 1;
    //设置消息超时时间
    private final static int TIMEOUT_CONNECTIONMESSAGE = 8000;
    private final static int TIMEOUT_CONNECTIONMESSAGETWO = 90000;

    private static String appCookie;
    private static String appUserAgent;
    private Context context;
    public static void cleanCookie() {
        appCookie = "";
    }
    private static String getCookie(AppContext appContext) {
        if (appCookie == null || appCookie == "") {
            appCookie = appContext.getProperty("cookie");
        }
        return appCookie;
    }

    private static String getUserAgent(AppContext appContext) {
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("OSChina.NET");
            ua.append('/' + appContext.getPackageInfo().versionName + '_'
                    + appContext.getPackageInfo().versionCode);// App版本
            ua.append("/Android");// 手机系统平台
            ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
            ua.append("/" + android.os.Build.MODEL); // 手机型号
            ua.append("/" + appContext.getAppId());// 客户端唯�?���?
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    private static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(TIMEOUT_CONNECTION);
        // 设置 读数据超时时
        httpClient.getHttpConnectionManager().getParams()
                .setSoTimeout(TIMEOUT_SOCKET);
        // 设置 字符
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static GetMethod getHttpGet(String url, String cookie,
                                        String userAgent) {
        GetMethod httpGet = new GetMethod(url);
        // 设置 请求超时时间
        httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
        httpGet.setRequestHeader("Host", URLs.HOST);
        httpGet.setRequestHeader("Connection", "Keep-Alive");
        httpGet.setRequestHeader("Cookie", cookie);
        httpGet.setRequestHeader("User-Agent", userAgent);
        return httpGet;
    }

    private static PostMethod getHttpPost(String url, String cookie,
                                          String userAgent) {
        PostMethod httpPost = new PostMethod(url);
        // 设置 请求超时时间
        httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
        httpPost.setRequestHeader("Host", URLs.HOST);
        httpPost.setRequestHeader("Connection", "Keep-Alive");
        httpPost.setRequestHeader("Cookie", cookie);
        httpPost.setRequestHeader("User-Agent", userAgent);
        return httpPost;
    }

    private static PostMethod getHttpPostTwo(String url, String cookie,
                                             String userAgent) {
        PostMethod httpPost = new PostMethod(url);
        // 设置 请求超时时间
        httpPost.getParams().setSoTimeout(TIMEOUT_SOCKETTWO);
        httpPost.setRequestHeader("Host", URLs.HOST);
        httpPost.setRequestHeader("Connection", "Keep-Alive");
        httpPost.setRequestHeader("Cookie", cookie);
        httpPost.setRequestHeader("User-Agent", userAgent);
        return httpPost;
    }

    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');

        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            // 不做URLEncoder处理
            // url.append(URLEncoder.encode(String.valueOf(params.get(name)),
            // UTF_8));
        }

        return url.toString().replace("?&", "?");
    }

    /**
     * get请求URL
     *
     * @param url
     * @throws AppException
     */
    public static InputStream http_get(String url, String type) throws AppException {
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        String responseBody ="";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url, null, null);
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode != HttpStatus.SC_OK) {
                    throw AppException.http(statusCode);
                }
                responseBody = httpGet.getResponseBodyAsString();
                break;
            } catch (HttpException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {}
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问�?
                e.printStackTrace();
                throw AppException.http(e);
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                Log.e("网络异常",e.getMessage());
//            e.printStackTrace();
                throw AppException.network(e);
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
        if (responseBody.contains("result")
                && responseBody.contains("errorCode")) {
            try {
                Result res = Result.parse(new ByteArrayInputStream(responseBody
                        .getBytes()));
                if (res.getCode() == 0) {
                }
            } catch (Exception e) {
                Log.e("网络异常",e.getMessage());
//            e.printStackTrace();
            }
        }
        recordFlow(type, responseBody.getBytes().length);
        return new ByteArrayInputStream(responseBody.getBytes());
    }

    /**
     * 记录使用了多少流量
     * @param type
     * @param flow
     */
    public static void recordFlow(String type, int flow){
        Float flows = URLs.flowMap.get(type);
        if(flows != null){
            flows = flows + (new Float(flow) / 1024);
        }else{
            flows = new Float((new Float(flow) / 1024));
        }
        URLs.flowMap.put(type, new Float(flows));
        float total = 0;
        for (String key : URLs.flowMap.keySet()){
            if(!"total".equals(key)) total += URLs.flowMap.get(key);
        }
        URLs.flowMap.put("total", total);
    }

    /**
     * 记录使用了多少流量
     * @param type
     * @param map
     */
    public static void recordFlow(String type, Map<String, Object> map){
        int size = 0;
        for (String key : map.keySet()){
            size += map.get(key).toString().getBytes().length;
        }
        recordFlow(type, size);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param files
     * @throws AppException
     */
    private static InputStream _post(AppContext appContext, String url,
                                     Map<String, Object> params, Map<String, File> files, String type)
            throws AppException {
        HttpClient httpClient = null;
        PostMethod httpPost = null;

        // post表单参数处理
        int length = (params == null ? 0 : params.size())
                + (files == null ? 0 : files.size());
        Part[] parts = new Part[length];
        int i = 0;
        NameValuePair[] nvp = new NameValuePair[params.size()];
        if (params != null)
            for (String name : params.keySet()) {
                nvp[i++] = new NameValuePair(name, String.valueOf(params.get(name)));
            }
        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpPost = getHttpPost(url, null, null);
                httpPost.setRequestBody(nvp);
                int statusCode = httpClient.executeMethod(httpPost);
                if (statusCode != HttpStatus.SC_OK) {
                    throw AppException.http(statusCode);
                } else if (statusCode == HttpStatus.SC_OK) {
                    Cookie[] cookies = httpClient.getState().getCookies();
                    String tmpcookies = "";
                    for (Cookie ck : cookies) {
                        tmpcookies += ck.toString() + ";";
                    }
                    // 保存cookie
                    if (appContext != null && tmpcookies != "") {
                        appContext.setProperty("cookie", tmpcookies);
                        appCookie = tmpcookies;
                    }
                }
                responseBody = httpPost.getResponseBodyAsString();
                // System.out.println("XMLDATA=====>"+responseBody);
                break;
            } catch (HttpException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问题
                e.printStackTrace();
                throw AppException.http(e);
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                    }
                    continue;
                }
                // 发生网络异常
                Log.e("网络异常",e.getMessage());
//            e.printStackTrace();
                throw AppException.network(e);
            } finally {
                // 释放连接
                httpPost.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_TIME);

        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
        if (responseBody.contains("result")
                && responseBody.contains("errorCode")
                && appContext.containsProperty("user.uid")) {
            try {
                Result res = Result.parse(new ByteArrayInputStream(responseBody
                        .getBytes()));
                if (res.getCode() == 0) {
                }
            } catch (Exception e) {
                Log.e("网络异常",e.getMessage());
//            e.printStackTrace();
                Constants.recordExceptionInfo(e, appContext, appContext.toString());
            }
        }
        recordFlow(type, responseBody.getBytes().length);
        return new ByteArrayInputStream(responseBody.getBytes());
    }

    /**
     * post请求URL
     *
     * @param url
     * @param params
     * @param files
     * @throws AppException
     * @throws java.io.IOException
     * @throws
     */
    private static Result http_post(AppContext appContext, String url,
                                    Map<String, Object> params, Map<String, File> files, String type)
            throws AppException, IOException {
        return Result.parse(_post(appContext, url, params, files, type));
    }

    /**
     * 检查版本更新
     * @return
     */
    public static Update checkVersion(AppContext appContext) throws AppException {
        try{
            return Update.parse(http_get(URLs.UPDATE_VERSION, "version"));
        }catch(Exception e){
            if(e instanceof AppException)
                throw (AppException)e;
            throw AppException.network(e);
        }
    }

    /**
     * 获取网络图片
     *
     * @param url
     * @return
     */
    public static Bitmap getNetBitmap(String url,Context context,String flowtype) throws AppException {
        // System.out.println("image_url==> "+url);
        Float totalRxFront = rxBytes(context);
        DecimalFormat df = new DecimalFormat("###,###,###,###.0000");
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        Bitmap bitmap = null;
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url, null, null);
                int statusCode = httpClient.executeMethod(httpGet);
                if (statusCode != HttpStatus.SC_OK) {
                    throw AppException.http(statusCode);
                }
                InputStream inStream = httpGet.getResponseBodyAsStream();
                bitmap = BitmapFactory.decodeStream(inStream);
                inStream.close();
                break;
            } catch (HttpException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        Constants.recordExceptionInfo(e1, context, context.toString()+"/ApiClient");
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问�?
                e.printStackTrace();
                throw AppException.http(e);
            } catch (IOException e) {
                time++;
                if (time < RETRY_TIME) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        Constants.recordExceptionInfo(e1, context, context.toString()+"/ApiClient");
                    }
                    continue;
                }
                // 发生网络异常
                e.printStackTrace();
                throw AppException.network(e);
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < RETRY_TIME);
        Float totalRxAfter = rxBytes(context);
        String totalRx = df.format(totalRxAfter - totalRxFront);
        download(context,flowtype,totalRx);
        return bitmap;
    }

//	/**
//	 * 版本更新
//	 *
//	 * @param
//	 * @return
//	 */
//	public static Update checkVersion(AppContext appContext)
//			throws AppException {
//		try {
//			return Update.parse(http_get(URLs.UPDATE_VERSION));
//		} catch (Exception e) {
//			if (e instanceof AppException)
//				throw (AppException) e;
//			throw AppException.network(e);
//		}
//	}

//	/**
//	 * 获取最新的任务条数
//	 *
//	 * @param
//	 * @return
//	 */
//	public static List<Map> getNewTaskCount(AppContext appContext)
//			throws AppException {
//		try {
//			Map<String, Object> para = new HashMap<String, Object>();
//			para.put("userid", "卜雷");
//			InputStream resultIsm = _post(appContext, URLs.GETNEWTASKCOUNT,
//					para, null);
//			String resultStr = InputStreamUtils.InputStreamTOString(resultIsm);
//			List<Map> resultMap = JsonHelper.toListMap(resultStr);
//			int taskCount = resultMap.size();
////			taskCount = new Integer(resultMap.get("rows").toString());
//			return resultMap;
//		} catch (Exception e) {
//			if (e instanceof AppException)
//				throw (AppException) e;
//			throw AppException.network(e);
//		}
//	}

    /**
     * 获取最新的任务内容
     *
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String getMessage(AppContext appContext,String messageid, String type)
            throws AppException{
        try {

            Map<String, Object> para = new HashMap<String, Object>();
            para.put("DID", messageid);
            InputStream resultIsm = _post(appContext, URLs.GETMESSAGE, para,
                    null, type);
            String resultStr = InputStreamUtils.InputStreamTOString(resultIsm);
            Map resultMap = JsonHelper.toMap(resultStr);
            List<Map<Object,Object>> message = new ArrayList<Map<Object,Object>>();
            if(resultMap != null && resultMap.size() > 0){
                String map=resultMap.get("CONTENT").toString();

                return map;
            }else{
                return null;
            }

//            Map resultMap2 = JsonHelper.toMap(map);

//            message = JsonHelper.converMessageFormString(map);
//            Map<Object,Object> mapRes=  message.get(0);
//			return mapRes;
        } catch (Exception e) {
            if (e instanceof AppException)
                throw (AppException) e;
            throw AppException.network(e);
        }
    }


    /**
     * 提交任务到云平台
     *
     * @param
     * @return
     * @throws Exception
     */
    public static Result pubPostMessageReturnState(AppContext appContext,
                                                   Map<String, Object> params, String type) throws Exception {
        try {
            return http_post(appContext, URLs.RECEIVEMESSAGEINFO, params, null, type);
        } catch (Exception e) {
            if (e instanceof AppException)
                throw (AppException) e;
            throw AppException.network(e);
        }
    }

    /**
     * http提交消息到云平台
     *
     * @param
     * @return
     * @throws Exception
     */
    public static Map httpPostMessageData(AppContext appContext, Map<String, Object> params, String type,Context context,String flowtype)  {
        try {
            Float totalTxFront = txBytes(context);
            DecimalFormat df = new DecimalFormat("####.0000");
            String http = URLs.HTTP+URLs.HOST+"/"+ URLs.SENDMESSAGE+"/" +URLs.MESSAGESEND;
//            Log.e("--------------------------------------------",http);
            InputStream resultIsm = _postMessagetwo(appContext, http, params,
                    null,"0",context);
            String resultStr = InputStreamUtils.InputStreamTOString(resultIsm);
            if(resultStr != null && !resultStr.equals("")){
                Map resultMap = JsonHelper.toMap(resultStr);
                Float totalTxAfter = txBytes(context);
                String totalTx = df.format(totalTxAfter - totalTxFront);
                upload(context,flowtype,totalTx);
                return resultMap;
            }

            Map resultMap = JsonHelper.toMap(resultStr);
            recordFlow(type, resultStr.getBytes().length);
            recordFlow(type, params);
            Float totalTxAfter = txBytes(context);
            String totalTx = df.format(totalTxAfter - totalTxFront);
            upload(context,flowtype,totalTx);
//            boolean iswifi = isWifi(context);
            return resultMap;
        }catch (JSONException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString()+"/ApiClient");
        } catch (Exception e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString()+"/ApiClient");
        }
        return null;
    }

    /**
     * http提交消息到云平台(终端创建项目和任务,报告等等)
     *
     * @param
     * @return
     * @throws Exception
     */
    public static Map sendHttpPostMessageData(AppContext appContext, String http,Map<String, Object> params, String type,Context context,String flowtype)  {
        try {
            Float totalTxFront = txBytes(context);
            DecimalFormat df = new DecimalFormat("####.0000");
            InputStream resultIsm = _postMessagetwo(appContext, http, params,
                    null,"1",context);
            String resultStr = InputStreamUtils.InputStreamTOString(resultIsm);
            if(resultStr != null && !resultStr.equals("")){
                Map resultMap = JsonHelper.toMap(resultStr);
                Float totalTxAfter = txBytes(context);
                String totalTx = df.format(totalTxAfter - totalTxFront);
                upload(context,flowtype,totalTx);
                return resultMap;
            }

            Map resultMap = JsonHelper.toMap(resultStr);
            recordFlow(type, resultStr.getBytes().length);
            recordFlow(type, params);
            Float totalTxAfter = txBytes(context);
            String totalTx = df.format(totalTxAfter - totalTxFront);
            upload(context,flowtype,totalTx);
//            boolean iswifi = isWifi(context);
            return resultMap;
        }catch (JSONException e) {
            e.printStackTrace();;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http同步获取平台数据
     *
     * @param
     * @return
     * @throws Exception
     */
    public static Map httpGetDataFromWeb(AppContext appContext, Map<String, Object> params, String type,String queryType,Context context,String flowtype)  {
        try {
            Float totalRxFront = rxBytes(context);
            DecimalFormat df = new DecimalFormat("####.0000");
            String http = URLs.HTTP+URLs.HOST+"/"+ URLs.GETDATAFROMWEB+"/" +URLs.GETDATAFROMWEBEND;
            byte[] resultIsm = _postMessage(appContext, http, params,null,queryType,context);
            String resultStr = "";
            if(resultIsm !=null){
                resultStr = StringUtils.byteToString(resultIsm);
            }
            Map resultMap = new HashMap();
            resultMap.put("json",resultStr);
            recordFlow(type, resultStr.getBytes().length);
            recordFlow(type, params);
            Float totalRxAfter = rxBytes(context);
            String totalRx = df.format(totalRxAfter - totalRxFront);
            download(context,flowtype,totalRx);
            return resultMap;
        } catch (AppException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString()+"/ApiClient");
        }
        return null;
    }

    /**
     * http同步获取平台数据
     *
     * @param
     * @return
     * @throws Exception
     */
    public static Map httpProGetDataFromWeb(AppContext appContext, Map<String, Object> params, String type,String queryType,Context context,String flowtype)  {
        try {
            Float totalRxFront = rxBytes(context);
            DecimalFormat df = new DecimalFormat("####.0000");
            String http = URLs.HTTP+URLs.HOST+"/"+ URLs.GETDATAFROMWEB+"/" +URLs.PROJECTCONTRAST;
            byte[] resultIsm = _postMessage(appContext, http, params,null,queryType,context);
            String resultStr = "";
            if(resultIsm !=null){
                resultStr = StringUtils.byteToString(resultIsm);
            }
            Map resultMap = new HashMap();
            resultMap.put("json",resultStr);
            recordFlow(type, resultStr.getBytes().length);
            recordFlow(type, params);
            Float totalRxAfter = rxBytes(context);
            String totalRx = df.format(totalRxAfter - totalRxFront);
            download(context,flowtype,totalRx);
            return resultMap;
        } catch (AppException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString()+"/ApiClient");
        }
        return null;
    }

    public static void download(Context context,String flowtype,String totalRx){
        String providersName = getProvidersName(context);
        String strNetworkType = getNetworkType(context);
        String clientname = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateformat.format(new Date());
        String userId = Constants.getLoginPerson(context).get("userId");
        SympFlowInfo sympFlowInfo = new SympFlowInfo();
        sympFlowInfo.setFlowinfoid(Constants.getUuid());
        sympFlowInfo.setUserid(userId);
        sympFlowInfo.setFlowtype(flowtype);
        sympFlowInfo.setUsetype("下载");//下载
        if(strNetworkType.equals("WIFI")){
            sympFlowInfo.setConnecttype("WIFI");//wifi
        }else{
            sympFlowInfo.setConnecttype(strNetworkType);
        }
        sympFlowInfo.setWebtype(strNetworkType);
        sympFlowInfo.setEquipmentid(clientname);
        sympFlowInfo.setUsequantity(totalRx);
        sympFlowInfo.setRecordtime(date);

        addMessage(date,userId,totalRx,context,sympFlowInfo);
//        addFlow(context,sympFlowInfo);
    }

    public static void upload(Context context,String flowtype,String totalTx){

        String providersName = getProvidersName(context);//设备名称
        String strNetworkType = getNetworkType(context);//网络类型
        String clientname = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateformat.format(new Date());//上传时间
        String userId = Constants.getLoginPerson(context).get("userId");//用户ID
        SympFlowInfo sympFlowInfo = new SympFlowInfo();
        sympFlowInfo.setFlowinfoid(Constants.getUuid());
        sympFlowInfo.setUserid(userId);
        sympFlowInfo.setFlowtype(flowtype);
        sympFlowInfo.setUsetype("上传");//上传
        if(strNetworkType.equals("WIFI")){
            sympFlowInfo.setConnecttype("WIFI");//wifi
        }else{
            sympFlowInfo.setConnecttype(strNetworkType);
        }
        sympFlowInfo.setWebtype(strNetworkType);
        sympFlowInfo.setUsequantity(totalTx);
        sympFlowInfo.setEquipmentid(clientname);
        sympFlowInfo.setRecordtime(date);

        addMessage(date,userId,totalTx,context,sympFlowInfo);
    }

    public static void addMessage(String date,String userId,String total,Context context,SympFlowInfo sympFlowInfo){
        SympMessageReal messageRealObj=new SympMessageReal();
        messageRealObj.setMessageid(Constants.getUuid());
        messageRealObj.setPresonid(userId);
        messageRealObj.setMessagestate("1");
        messageRealObj.setMessagetype("1");
        messageRealObj.setFcomment("备注");
        messageRealObj.setSenddate(date);
        messageRealObj.setCreatedate(date);

        int dataType=39;
        int operationType=1;
        List<Map<String, Object>> listMessage=new ArrayList<Map<String, Object>>();
        Map<String, Object> messageContent = new HashMap<String, Object>();
        messageContent.put("FLOWINFOID", sympFlowInfo.getFlowinfoid());
        messageContent.put("USERID", sympFlowInfo.getUserid());
        messageContent.put("FLOWTYPE", sympFlowInfo.getFlowtype());
        messageContent.put("USETYPE", sympFlowInfo.getUsetype());
        messageContent.put("CONNECTTYPE", sympFlowInfo.getConnecttype());
        messageContent.put("WEBTYPE", sympFlowInfo.getWebtype());
        messageContent.put("RECORDTIME", sympFlowInfo.getRecordtime());
        messageContent.put("USEQUANTITY", sympFlowInfo.getUsequantity());
        messageContent.put("EQUIPMENTID", sympFlowInfo.getEquipmentid());
        messageContent.put("BUSINESSTYPE", (sympFlowInfo.getBusinesstype()==null? "":sympFlowInfo.getBusinesstype()));
        messageContent.put("REMARK", (sympFlowInfo.getRemark()==null? "":sympFlowInfo.getRemark()));
        listMessage.add(messageContent);
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        try {
            SympFlowInfoDao sympFlowInfoDao = new SympFlowInfoDao(context);
            sympFlowInfoDao.inserFlowInfo(userId,total,sympFlowInfo);
//            sympMessageRealDao.addMessage(messageRealObj,dataType,operationType,listMessage);
        } catch (Exception e) {
            Toast.makeText(context, "网络异常，提交失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Constants.recordExceptionInfo(e, context, context.toString()+"/ApiClient");
        }
    }

    /**
     * 判断是否连接WIFI
     */
//    public static boolean isWifi(Context mContext) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//        if (activeNetInfo != null
//                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 获取手机SIM卡运营商
     */
    public static String getProvidersName(Context mContext) {
        String providersName = null;
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI; // 返回唯一的用户ID;就是这张卡的编号神马的
        IMSI = telephonyManager.getSubscriberId();
        if (IMSI == null){
            return "unkwon";
        }else{
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                providersName = "移动";
            } else if (IMSI.startsWith("46001")) {
                providersName ="联通";
            } else if (IMSI.startsWith("46003")) {
                providersName = "电信";
            }
        }
        return providersName;
    }

    /**
     * 获取手机连接网络类型
     */
    public static String getNetworkType(Context mContext)
    {
        String strNetworkType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                strNetworkType = "WIFI";
            }else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                String _strSubTypeName = networkInfo.getSubtypeName();

                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                        {
                            strNetworkType = "3G";
                        }
                        else
                        {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }
            }
        }

        return strNetworkType;
    }

    /**
     * 获取APP下载的流量
     */
    public static Float rxBytes(Context mContext){
        int uid = 0;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = mContext.getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                uid = runningProcess.uid;
                break;
            }
        }

        Float rx = (Float.valueOf(TrafficStats.getUidRxBytes(uid)))/1024;
        return rx;
    }

    /**
     * 获取APP上传的流量
     */
    public static Float txBytes(Context mContext){
        int uid = 0;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ApplicationInfo appinfo = mContext.getApplicationInfo();
        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess : run) {
            if ((runningProcess.processName != null) && runningProcess.processName.equals(appinfo.processName)) {
                uid = runningProcess.uid;
                break;
            }
        }

        Float tx = (Float.valueOf(TrafficStats.getUidTxBytes(uid)))/1024;
        return tx;
    }

//	/**
//	 * 提交任务到云平台
//	 *
//	 * @param post
//	 * @return
//	 * @throws Exception
//	 */
//	public static Result pubPost(AppContext appContext, PmTask task)
//			throws Exception {
//
//		Map<String, Object> params = new HashMap<String, Object>();
//		Map taskList = new HashMap<>();
//		taskList.put("rows", 1);
//		taskList.put("tasks", "[" + JsonHelper.toJSON(task) + "]");
//
//		params.put("json", JsonHelper.setJosn(taskList));
//		params.put("terminalid", 1);
//		params.put("userid", 1);
//
//		try {
//			return http_post(appContext, URLs.SAVETASKCONTENT, params, null);
//		} catch (Exception e) {
//			if (e instanceof AppException)
//				throw (AppException) e;
//			throw AppException.network(e);
//		}
//	}



    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }




    //TODO
    private static byte[] _postMessage(AppContext appContext, String url,
                                            Map<String, Object> params, Map<String, File> files,String queryType,Context context)
            throws AppException {
        String responseBody = "";
        byte[] responseBodya = new byte[0];
        if (Network.isNetworkConnected(context) == true){
            try {
                PostMethod httpPost = null;
                // 使用网络连接类HttpClient类对网络数据的提取
                HttpClient httpClient = null;

                // 获取HttpClient对象 （认证）
                // post表单参数处理
                int length = (params == null ? 0 : params.size())
                        + (files == null ? 0 : files.size());
                Part[] parts = new Part[length];
                int i = 0;
                NameValuePair[] nvp = new NameValuePair[params.size()];
                if (params != null)
                    for (String name : params.keySet()) {
                        nvp[i++] = new NameValuePair(name, String.valueOf(params.get(name)));
                    }
                int time = 0;
                do {
                    try {

                        // httpPost = getHttpPost(url, cookie, userAgent);
                        if (queryType.equals("1")) {
                            httpClient = getHttpClientMessageTwo();
                            httpPost = getHttpPostTwo(url, null, null);
                        } else {
                            httpClient = getHttpClientMessage();
                            httpPost = getHttpPost(url, null, null);
                        }

                        httpPost.setRequestBody(nvp);
                        int statusCode = httpClient.executeMethod(httpPost);
                        if (statusCode != HttpStatus.SC_OK) {
                            throw AppException.http(statusCode);
                        } else if (statusCode == HttpStatus.SC_OK) {
                            Cookie[] cookies = httpClient.getState().getCookies();
                            String tmpcookies = "";
                            for (Cookie ck : cookies) {
                                tmpcookies += ck.toString() + ";";
                            }
                            // 保存cookie
                            if (appContext != null && tmpcookies != "") {
                                appContext.setProperty("cookie", tmpcookies);
                                appCookie = tmpcookies;
                            }
                        }
                        responseBody = httpPost.getResponseBodyAsString();
                        responseBodya = httpPost.getResponseBody();
                        // System.out.println("XMLDATA=====>"+responseBody);
                        break;
                    } catch (HttpException e) {
                        time++;
                        if (time < RETRY_TIME) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                            }
                            continue;
                        }
                        // 发生致命的异常，可能是协议不对或者返回的内容有问题
                        Log.e("网络异常", e.getMessage());
                        //            e.printStackTrace();
                        throw AppException.http(e);
                    } catch (IOException e) {
                        time++;
                        if (time < RETRY_TIME) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                            }
                            continue;
                        }
                        throw AppException.io(e);
                    } finally {
                        // 释放连接
                        httpPost.releaseConnection();
                        httpClient = null;
                    }
                } while (time < RETRY_TIME);

                responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
                if (responseBody.contains("result")
                        && responseBody.contains("errorCode")
                        && appContext.containsProperty("user.uid")) {
                    Result res = null;
                    try {
                        res = Result.parse(new ByteArrayInputStream(responseBody
                                .getBytes()));
                    } catch (IOException e) {
                        throw AppException.io(e);
                    }
                    if (res.getCode() == 0) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                Log.e("网络异常", e.getMessage());
                throw AppException.http(e);
            }
        }else{
            throw AppException.http(1);
        }
        return responseBodya;
    }

    //TODO
    private static InputStream _postMessagetwo(AppContext appContext, String url,
                                       Map<String, Object> params, Map<String, File> files,String queryType,Context context)
            throws AppException {
        String responseBody = "";
        if (Network.isNetworkConnected(context) == true){
//            Log.i("message", "===================提交数据=map1=" + params);
            try {
                PostMethod httpPost = null;
                // 使用网络连接类HttpClient类对网络数据的提取
                HttpClient httpClient = null;

                // 获取HttpClient对象 （认证）
                // post表单参数处理
                int length = (params == null ? 0 : params.size())
                        + (files == null ? 0 : files.size());
                Part[] parts = new Part[length];
                int i = 0;
                NameValuePair[] nvp = new NameValuePair[params.size()];
                if (params != null)
                    for (String name : params.keySet()) {
                        nvp[i++] = new NameValuePair(name, String.valueOf(params.get(name)));
                    }
                int time = 0;
                do {
                    try {

                        // httpPost = getHttpPost(url, cookie, userAgent);
                        if (queryType.equals("1")) {
                            httpClient = getHttpClientMessageTwo();
                            httpPost = getHttpPostTwo(url, null, null);
                        } else {
                            httpClient = getHttpClientMessage();
                            httpPost = getHttpPost(url, null, null);
                        }

                        httpPost.setRequestBody(nvp);
                        int statusCode = httpClient.executeMethod(httpPost);
                        if (statusCode != HttpStatus.SC_OK) {
                            throw AppException.http(statusCode);
                        } else if (statusCode == HttpStatus.SC_OK) {
                            Cookie[] cookies = httpClient.getState().getCookies();
                            String tmpcookies = "";
                            for (Cookie ck : cookies) {
                                tmpcookies += ck.toString() + ";";
                            }
                            // 保存cookie
                            if (appContext != null && tmpcookies != "") {
                                appContext.setProperty("cookie", tmpcookies);
                                appCookie = tmpcookies;
                            }
                        }
                        responseBody = httpPost.getResponseBodyAsString();
                        // System.out.println("XMLDATA=====>"+responseBody);
                        break;
                    } catch (HttpException e) {
                        time++;
                        if (time < RETRY_TIME) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                            }
                            continue;
                        }
                        // 发生致命的异常，可能是协议不对或者返回的内容有问题
                        Log.e("网络异常", e.getMessage());
                        //            e.printStackTrace();
                        throw AppException.http(e);
                    } catch (IOException e) {
                        time++;
                        if (time < RETRY_TIME) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                Constants.recordExceptionInfo(e1, appContext, appContext.toString()+"/ApiClient");
                            }
                            continue;
                        }
                        throw AppException.io(e);
                    } finally {
                        // 释放连接
                        httpPost.releaseConnection();
                        httpClient = null;
                    }
                } while (time < RETRY_TIME);

                responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
                if (responseBody.contains("result")
                        && responseBody.contains("errorCode")
                        && appContext.containsProperty("user.uid")) {
                    Result res = null;
                    try {
                        res = Result.parse(new ByteArrayInputStream(responseBody
                                .getBytes()));
                    } catch (IOException e) {
                        throw AppException.io(e);
                    }
                    if (res.getCode() == 0) {
                    }
                }
            } catch (Exception e) {
//                Log.e("网络异常", e.getMessage());
                throw AppException.http(e);
            }

        }else{
            throw AppException.http(1);
        }
        return new ByteArrayInputStream(responseBody.getBytes());
    }

    private static String _postDataFromWebMessage(AppContext appContext, String url,
                                                  Map<String, Object> params, Map<String, File> files) {
        org.apache.http.client.methods.HttpPost post;
        //创建客户端对象
        org.apache.http.client.HttpClient cliet;
        //创建发送请求的对象
        HttpResponse response;
        UrlEncodedFormEntity urlEntity;
        //创建接收返回数据的对象
        HttpEntity entity;
        //创建流对象
        InputStream is;
        post=new HttpPost(url);
        //创建默认的客户端对象
        cliet= new DefaultHttpClient();
        //用list封装要向服务器端发送的参数
        List<BasicNameValuePair> pairs=new ArrayList<BasicNameValuePair>();
        if (params != null){
            for (String name : params.keySet()) {
                pairs.add(new BasicNameValuePair(name, String.valueOf(params.get(name))));
            }
        }
        try {
            //用UrlEncodedFormEntity来封装List对象
            urlEntity=new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
            //设置使用的Entity
            post.setEntity(urlEntity);
            try {
                //客户端开始向指定的网址发送请求
                response=cliet.execute(post);
                //获得请求的Entity
                entity=response.getEntity();
                is=entity.getContent();
                //下面是读取数据的过程
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line=null;
                StringBuffer sb=new StringBuffer();
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                System.out.println(sb.toString());
                return sb.toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, appContext, appContext.toString()+"/ApiClient");
            } catch (IOException e) {
                e.printStackTrace();
                Constants.recordExceptionInfo(e, appContext, appContext.toString()+"/ApiClient");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Constants.recordExceptionInfo(e, appContext, appContext.toString()+"/ApiClient");
        }

        return null;
    }

    private static HttpClient getHttpClientMessage() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(TIMEOUT_CONNECTIONMESSAGE);
        // 设置 读数据超时时
        httpClient.getHttpConnectionManager().getParams()
                .setSoTimeout(TIMEOUT_SOCKET);
        // 设置 字符
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static HttpClient getHttpClientMessageTwo() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(TIMEOUT_CONNECTIONMESSAGETWO);
        // 设置 读数据超时时
        httpClient.getHttpConnectionManager().getParams()
                .setSoTimeout(TIMEOUT_SOCKETTWO);
        // 设置 字符
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }


//	/**
//	 * 图片数据发送给云平台
//	 *
//	 * @param url
//	 * @param username
//	 * @param pwd
//	 * @return
//	 * @throws AppException
//	 */
//
//	public static Result uploadPicture(AppContext appContext, String pictureName, File picture) throws AppException {
//		Map<String,Object> params = new HashMap<String,Object>();
//		params.put("pictureName", pictureName);
//
//		Map<String, File> files = new HashMap<String, File>();
//		files.put("uploadPicture", picture);
//
//		try{
//			return http_post(appContext, URLs.SAVEATTACH, params, files);
//		}catch(Exception e){
//			if(e instanceof AppException)
//				throw (AppException)e;
//			throw AppException.network(e);
//		}
//	}



}
