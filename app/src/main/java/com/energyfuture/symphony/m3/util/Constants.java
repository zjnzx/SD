package com.energyfuture.symphony.m3.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.CommToolsActivity;
import com.energyfuture.symphony.m3.activity.LoginActivity;
import com.energyfuture.symphony.m3.activity.MessageMagActivity;
import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.SettingNewActivity;
import com.energyfuture.symphony.m3.activity.UserInfoActivity;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.SympUserInfoDao;
import com.energyfuture.symphony.m3.dao.TrCommonToolsDao;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrUserInfoDao;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.domain.SympMessageReal;
import com.energyfuture.symphony.m3.domain.TrCommonTools;
import com.energyfuture.symphony.m3.domain.TrExceptionInfo;
import com.energyfuture.symphony.m3.domain.TrFeedBackAccessory;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.wifi.ui.WifiListActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public final class Constants {
    //	public static final Logger logger = Logger.getLogger(Constants.class);
    public static int pageSize=4;
    public static String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    public static String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    public static String apkPath = pathTemp.substring(0, Constants.pathTemp.length() - 1) + "legacy" + "/temp/";
    public static Map<String,String> mapUrl = new HashMap<String,String>();//保存选中的图片信息
    public static SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateformat3 = new SimpleDateFormat("HH:mm");
    public static SharedPreferences  M3_SETTNG;
    public static SimpleDateFormat dateformat5 = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat dateformat6 = new SimpleDateFormat("yyyy-MM");

    public static Handler generalhandler;  //普通采集卡
    public static Handler sechandler; //第二级的采集卡
    public static Handler bushinghandler;

    public static Handler getImageHandler; //获取图片

    public static boolean isDownLoad = true; //工具是否下载
    public static Map<String,Map<String,Object>> commToolMap = new HashMap<String,Map<String,Object>>();//保存工具下载的相关控件

    public static Handler takephotohandler; //取消拍照
    public static boolean flag=false;  //用于标记是否为重新获取图片
    public static int successcount; //成功的次数

    public static String SENDMESSAGEFLAG = "0";
    public static int getcount; //通过wifi获取图片的次数
    public static int state=2;  //记录拍照或从wifi获取图片,默认为拍照
    public static List<String> imagelist=new ArrayList<String>(); //第一次批量导入时存放输入的图号和id
    public static Map<String,String> datamap=new LinkedHashMap<>(); //成功获取图片后存放图号和名字
    public static String wifiid=""; //用来记录当前的wifi的networkid
    public static SimpleDateFormat dateformat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String pageBack(String txt) {
        return "<script type='text/javascript'>alert('" + txt
                + "'); window.history.go(-1);</script>";
    }
    /**
     *
     * @Description:获取生成的有规律的主键
     * @since  2013-7-9
     * @author zhangshuang
     * @param @return
     * @return String
     */
    public static String getOrderNo(){

        String datetime=new SimpleDateFormat("hhmmss").format(new Date());
        String orderno="DH-"+datetime+"-"+sn();
        return orderno;

    }
    //	public static void main(String[] args) {
//		System.out.println(getOrderNo());
//	}
    public static int   sn() {
        Random random = new Random();
        int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;

        int b = 0;
        while (true) {
            b = random.nextInt(9999);
            if (b < 1000) {
                continue;
            } else
                break;
        }

        return b;
    }

    public static int getOrderSYS(){
        int orderno=sn();
        return orderno;

    }
    /**
     *
     * @Description:获取客户端真实IP
     * @since  2013-7-22
     * @author zhangshuang
     * @param @param request
     * @param @return
     * @return String
     */
//	public static String getIpAddr(HttpServletRequest request) {
//	    String ip = request.getHeader("x-forwarded-for");
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	        ip = request.getHeader("PRoxy-Client-IP");
//	    }
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	        ip = request.getHeader("WL-Proxy-Client-IP");
//	    }
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//	        ip = request.getRemoteAddr();
//	    }
//	    return ip;
//	}

    public static String getUuid(){

        String uuid =java.util.UUID.randomUUID().toString().replaceAll("-","");
        return uuid;

    }

    public static final String CharsetDecodeToUTF8(String str) {
        if (str == null)
            return "";
        String result = "";
        try {
            result = URLDecoder.decode(str, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串转换成时间格式
     * @Description:
     * @since  2013-9-13
     * @author zhangshuang
     * @param @param time
     * @param @return
     * @return Date
     */
    public static Date stringToDate(String time){
        Date date=new Date();
        if (null!=time && !time.equals("")) {
            SimpleDateFormat faDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            try {
                date= faDateFormat.parse(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            date=new Date();
        }
        return date;

    }
    /**
     * 时间转换成字符串格式
     * @Description:
     * @since  2013-9-13
     * @author zhangshuang
     * @param @param time
     * @param @return
     * @return Date
     */
    public static String dateToString(Date date){
        String time="";
        if (null!=date) {
            SimpleDateFormat faDateFormat=new SimpleDateFormat("yyyy-MM-dd");
            try {
                time= faDateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;

    }
    /**
     * 判断是否是数字
     * @Description:
     * @since  2014-11-3
     * @author zhangshuang
     * @param @param str
     * @param @return
     * @return boolean
     */
    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    /**
     * 格式化字符为quartz表达式
     * @designer
     * @since  2015-1-20
     * @author zhaogr
     * @param @param val 取值
     * @param @param type  时间类型
     * @param @return 拼接好的quartz表达式
     * @return String
     */
    public static String quartzFormatData(String val, String type){
        String quartz = null;
        if((val != null && !val.equals("")) && (type != null && !type.equals(""))){
            //秒
            if(type.equals("ss")){
                quartz = "0/"+val+" * * * * ?";
            }
            //分
            else if(type.equals("mm")){
                quartz = "0 */"+val+" * * * ?";
            }
            //时
            else if(type.equals("HH")){
                quartz = "0 0 */"+val+" * * ?";
            }
            //日
            else if(type.equals("dd")){
                quartz = "0 0 0 */"+val+" * ?";
            }
            //月
            else if(type.equals("MM")){
                quartz = "0 0 0 1 */"+val+" ?";
            }

        }

        return quartz;

    }

    /**
     * 拆分拼接好的quartz表达式
     * @designer
     * @since  2015-1-20
     * @author zhaogr
     * @param @return  拆分时间取值和时间类型;格式 1;ss(1秒读取一次)
     * @return String
     */
    public static String formatDataQuartz(String quartz){
        String returnCode = null;
//		0/10 * * * * ?
        if(quartz != null && !quartz.equals("")){
//			String[] temp  = quartz.split(" ");
            int count = 0; String val = "";
//			for (int i = 0; i < temp.length; i++) {
//				if(!temp[i].equals("*")){
//					val = temp[i];
//					break;
//				}else{
//					count ++;
//				}
//			}

            count = charCount(quartz, " ");
            val = quartz.substring(quartz.indexOf("/")-1,quartz.indexOf("/")+2);
            //秒
            if(count == 0){
                returnCode = "ss;"+val.substring(val.indexOf("/")+1);
            }
            //分
            else if(count == 1){
                returnCode = "mm;"+val.substring(val.indexOf("/")+1);
            }
            //时
            else if(count == 2){
                returnCode = "HH;"+val.substring(val.indexOf("/")+1);
            }
            //日
            else if(count == 3){
                returnCode = "dd;"+val.substring(val.indexOf("/")+1);
            }
            //月
            else if(count == 4){
                returnCode =  "MM;"+val.substring(val.indexOf("/")+1);
            }

        }
        return returnCode;

    }

    /**
     * 在某一段字符中获取特定字符出现的数量
     * @designer
     * @since  2015-3-10
     * @author zhaogr
     * @param @param charstring 某一段字符
     * @param @param flag 特定字符
     * @param @return
     * @return int
     */
    public static int charCount(String charstring, String flag){
        String s0 = charstring.substring(0,charstring.indexOf("/"));
        String ma = flag;
        int p = s0.indexOf(ma);
        int times = 0;

        while (p > -1) {
            times++;
            s0 = s0.substring(p + ma.length() + 1);
            p = s0.indexOf(ma);
        }
        return times;
    }
    /**
     * 时间计算
     * Constants.java
     * @Description:
     * @since  2015-3-5
     * @author zhangshuang
     * @param @param startTime
     * @param @param endTime
     * @param @return
     * @return String
     */
    public static String dateDiff(Date startTime, Date endTime) {
        String time = "";
        long diff;
        // 获得两个时间的毫秒时间差异
        diff = endTime.getTime() - startTime.getTime();
        long between = diff / 1000;// 除以1000是为了转换成秒

        if (between / (24 * 3600) > 0) {
            long days = between / (24 * 3600);
            long weeks = days / 7;

            if (weeks == 0) {
                time = days + "天前";
            } else if (weeks >= 1 && weeks <= 3) {
                time = weeks + "周前";
            } else {
                time = "更早";
            }
        } else if (between / 3600 > 0) {
            time = between / 3600 + "小时前";
        } else if (between / 60 > 0) {
            time = between / 60 + "分钟前";
        } else {
            time = "刚刚";
        }
        return time;

    }

    /**
     * 判断进程是否运行
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static void main(String[] args) {

        String  path = "E:\\work\\project\\基于云平台\\m3-sys-demo20150330\\img";
        boolean result = Constants.DeleteFolder(path);
        System.out.println(result);
    }

    File file=null;
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag=false;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File  file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /*
    zhangshuang
    获取头像
     */
    public  static int userHead(String userId){
        int userhead=R.drawable.handong;
        if (userId.equals("handong")){
            userhead= R.drawable.handong;
        }else if (userId.equals("zhangjia")){
            userhead=R.drawable.zhangjia;
        }else if (userId.equals("zhangming")){
            userhead=R.drawable.zhangming;
        }else if (userId.equals("zhangshuang")){
            userhead=R.drawable.zhangshuang;
        }else if (userId.equals("zhaogr")){
            userhead=R.drawable.zhaogr;
        }else if (userId.equals("zhaoxun")){
            userhead=R.drawable.zhaoxun;
        }else if (userId.equals("mengqian")){
            userhead=R.drawable.mengqian;
        }else if (userId.equals("wanxin")){
            userhead=R.drawable.wanxin;
        }else if (userId.equals("wangchao")){
            userhead=R.drawable.wangchao;
        }else if (userId.equals("dingwj")){
            userhead=R.drawable.dingwj;
        }else if (userId.equals("sujun")){
            userhead=R.drawable.sujun;
        }else if (userId.equals("yoyee")){
            userhead=R.drawable.yoyee;
        }else{
            userhead=R.drawable.nh;
        }
        return userhead;
    }

    public static String getUsericon(Context context){
        String userId = Constants.getLoginPerson(context).get("userId");
        String pic_route=FILE_SAVEPATH+"head/USER/";
        TrUserInfoDao userInfoDao=new TrUserInfoDao(context);
        UserInfo userInfo=new UserInfo();
        userInfo.setYhid(userId);
        List<UserInfo> userInfolist = userInfoDao.queryUserInfoList(userInfo);

        if(userInfolist.get(0).getSsxt()!=null&&!userInfolist.get(0).getSsxt().equals("null")&&!userInfolist.get(0).getSsxt().equals("")){
            String yhtx=userInfolist.get(0).getSsxt();
            String imagename=yhtx.substring(yhtx.lastIndexOf("\\")+1);
            File file=new File(pic_route+"small/"+imagename);
            if(file.exists()){
                return pic_route+"small/"+imagename;
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    public static String getWorkicon(Context context,String workperson){
        String pic_route=FILE_SAVEPATH+"head/USER/";
        TrUserInfoDao userInfoDao=new TrUserInfoDao(context);
        UserInfo userInfo=new UserInfo();
        userInfo.setYhid(workperson);
        List<UserInfo> userInfolist = userInfoDao.queryUserInfoList(userInfo);

        if(userInfolist.get(0).getSsxt()!=null&&!userInfolist.get(0).getSsxt().equals("null")&&!userInfolist.get(0).getSsxt().equals("")){
            String yhtx=userInfolist.get(0).getSsxt();
            String imagename=yhtx.substring(yhtx.lastIndexOf("\\")+1);
            File file=new File(pic_route+"small/"+imagename);
            if(file.exists()){
                return pic_route+"small/"+imagename;
            }else{
                return "";
            }
        }else{
            return "";
        }
    }

    /**
     * 查询当前登录人信息
     * @param context
     * @return
     */
    public static Map<String,String> getLoginPerson(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", context.getApplicationContext().MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        String userName = sharedPreferences.getString("userName","");
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId",userId);
        map.put("userName",userName);
        return map;
    }

    /**
     * 根据id查询用户
     * @param id
     * @param context
     * @return
     */
    public static List<String> getUserNameById(String id,Context context){
        SympUserInfoDao sympUserInfoDao = new SympUserInfoDao(context);
        UserInfo userInfo = new UserInfo();
        if(id != null && !id.equals("")){
            userInfo.setYhid(id);
        }
        List<UserInfo> userList = sympUserInfoDao.queryUserInfoList(userInfo);
        List<String> nameList = new ArrayList<String>();
        if(userList != null){
            for (int i = 0;i < userList.size();i++){
                String userName = userList.get(i).getXm();
                nameList.add(userName);
            }
        }
        return nameList;
    }

    /**
     * //保存图片到sd卡中文件夹
     * @param fileSrc
     * @param saveSrc
     * @param path
     * @param type 1,非沟通交流图片,2,沟通交流图片和反馈图片
     */
    public static void saveFile(String fileSrc,String saveSrc,String path,String type){
        if(!path.equals("")){
            Base.mardDir(path);
        }
        InputStream is = null;
        OutputStream fos = null;
        File saveFile=null;
        try {
            saveFile=new File(saveSrc);
//            if(saveFile.exists()){
//                saveFile.delete();
//            }
            String fileName = fileSrc.substring(fileSrc.lastIndexOf("/")+1);
            String fileUrl = saveFile.getAbsolutePath(); //+"/"+fileName;

            is = new FileInputStream(fileSrc);
            fos = new FileOutputStream(saveFile);

            //文件路径和名称存放到mapUrl中
            if("2".equals(type)){
                mapUrl.put(fileUrl,fileName);
            }


            // 写入SD卡
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            fos.write(buffer,0,buffer.length);
            if(path.contains("small")){//如果保存的是小图就压缩
                Base.revitionFileImageSize(saveFile,500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }
                if(fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 封装消息对象
     * @param context
     * @param type
     * @return
     */
    public static SympMessageReal getSympMessageReal(Context context,String type,String content){
        SympMessageReal sympMessageReal = new SympMessageReal();
        String date = dateformat2.format(new Date());
        sympMessageReal.setMessageid(Constants.getUuid());
        sympMessageReal.setPresonid(Constants.getLoginPerson(context).get("userId"));
        sympMessageReal.setCreatedate(date);
        sympMessageReal.setMessagestate("1");//消息状态(1.未发送、2.已发送、3.发送成功)
        sympMessageReal.setMessagetype(type);//消息类型 (1.文字、2.图片、3.文件)
        sympMessageReal.setSenddate(date);
        sympMessageReal.setFcomment("");
        sympMessageReal.setContent(content);
        return sympMessageReal;
    }

    //保留两位小数,不够两位补0
    public void formatValue(String value, EditText edt){
        if(value != null && ! value.equals("")){
            if(!value.contains(".") && !value.equals("-")){
                edt.setText(value+".00");
            }else if(value.contains(".") && value.substring(value.indexOf(".")).length()<3){
                edt.setText(value+"0");
            }else{
                edt.setText(value);
            }
        }
    }

    //控制输入保留两位小数
    public void format(Editable s){
        String temp = s.toString();
        if(temp.contains(".")){
            int posDot = temp.indexOf(".");
            if (posDot <= 0)
                return;
            if (temp.length() - posDot - 1 > 2) {
                s.delete(posDot + 3, posDot + 4);
            }
        }
    }
    // 将数字转化为大写
    public static String numToUpper(int num) {
        String u[] = {"〇","一", "二", "三", "四", "五", "六", "七", "八", "九" };
        char[] str = String.valueOf(num).toCharArray();
        String rstr = "";
        for (int i = 0; i < str.length; i++) {
            rstr = rstr + u[Integer.parseInt(str[i] + "")];
        }
        return rstr;
    }

    //查询变电站
    public static String getStationName(Context context,List<TrProject> list) {
        String stationName = "",stationVoltage = "";
        TrStation stationInfo = null;
        TrStation trStation=new TrStation();
        trStation.setStationid(list.get(0).getStationid());

        TrStationDao trStationDao=new TrStationDao(context);
        List<TrStation> stationList = trStationDao.queryTrStationList(trStation);
        if(stationList != null && stationList.size() > 0){
            stationInfo = stationList.get(0);
            stationName = stationInfo.getStationname();
            stationVoltage = stationInfo.getVoltage();
        }

        return stationName;
    }

    //查询小组名称
    public static String getGroupName(Context context,List<TrProject> list) {
        String bmmc = "";
        Department department=new Department();
        department.setBmid(list.get(0).getDepartment());

        List<Department> dataList = new TrDepartmentDao(context).queryDepartmentList(department);
        if(dataList != null && dataList.size() > 0){
            bmmc = dataList.get(0).getBmmc();
        }
       return bmmc;
    }

    //根据状态代码设置任务状态
    public static void setState(String taskstate,FrameLayout frameLayout,ImageView type,TextView state) {
        if(taskstate.equals("1")){
            frameLayout.setVisibility(View.GONE);
            type.setImageResource(R.drawable.orange);
            state.setText("未开始");
        }
        if(taskstate.equals("2")){
            type.setImageResource(R.drawable.task_state2);
            state.setText("进行中");
        }
        if(taskstate.equals("3")){
            frameLayout.setVisibility(View.GONE);
            type.setImageResource(R.drawable.green);
            state.setText("待补录");
        }
        if(taskstate.equals("4")){
            frameLayout.setVisibility(View.GONE);
            type.setImageResource(R.drawable.task_state4);
            state.setText("待总结");
        }
        if(taskstate.equals("5")){
            frameLayout.setVisibility(View.GONE);
            type.setImageResource(R.drawable.task_state5);
            state.setText("待审核");
        }
        if(taskstate.equals("6")){
            frameLayout.setVisibility(View.GONE);
            type.setImageResource(R.drawable.blue);
            state.setText("已完成");
        }
    }

    /**
     * 获取请求参数
     */
    public static Map<String, Object> getRequestParam(Context context,String obj,String type,String pid){
        String userId = getLoginPerson(context).get("userId");
        final Map<String, Object> condMap = new HashMap<String, Object>();
        condMap.put("OBJ",obj);
        condMap.put("TYPE",type);
        condMap.put("SIZE",5);
        condMap.put("USERID",userId);
        condMap.put("KEY",pid);
        return condMap;
    }

    /**
     * 数字加一
     * @return
     */
    public static String formatNum(){
        int strLength= Utils.number.length();
        String format="";
        String numberStr="";
        if(strLength < 12) {
            for (int k = 0; k < strLength; k++) {
                format += "0";
            }
            DecimalFormat df = new DecimalFormat(format);
            if (!Utils.number.equals("") && isNumeric2(Utils.number)) {
                int number = Integer.parseInt(Utils.number) + 1;
                numberStr = df.format(number);
            }
        }
        Utils.number = numberStr;
        return numberStr;
    }
    public static PopupWindow createPopupWindow(String userId, final Context context, final LinearLayout layout){  //点击用户头像弹框
        View view= LayoutInflater.from(context).inflate(R.layout.user_menu,null);
        final PopupWindow window = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        /*window.setBackgroundDrawable(new BitmapDrawable());  //点击空白出消失*/


        TextView name= (TextView) view.findViewById(R.id.user_menu_name);
        ImageView image= (ImageView) view.findViewById(R.id.user_menu_image);

        RelativeLayout user= (RelativeLayout) view.findViewById(R.id.user_menu_user);  //用户
        LinearLayout user_menu_message= (LinearLayout) view.findViewById(R.id.user_menu_message); //消息
        LinearLayout setting= (LinearLayout) view.findViewById(R.id.user_menu_setting);  //设置
        LinearLayout switchuser= (LinearLayout) view.findViewById(R.id.user_menu_switch_user); //切换用户
        TextView user_menu_menssage_count= (TextView) view.findViewById(R.id.user_menu_menssage_count); //消息数量

        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setMessagetype("1");
        List<Map<String,String>> textdataList = sympMessageRealDao.querySympMessageReal(sympMessageReal);
        sympMessageReal.setMessagetype("2");
        List<Map<String,String>> imagedataList = sympMessageRealDao.querySympMessageReal(sympMessageReal);
        int messagecount=textdataList.size()+imagedataList.size();

        user_menu_menssage_count.setText(String.valueOf(messagecount));

        if(Constants.getUserNameById(userId,context).size()!=0){
            name.setText(Constants.getUserNameById(userId,context).get(0));
        }else{
            name.setText("未知用户");
        }

        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(userId);
            image.setImageResource(userhead);
        }

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserInfoActivity.class);
                context.startActivity(intent);
                window.dismiss();
                layout.setVisibility(View.GONE);
            }
        });

        user_menu_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessageMagActivity.class);
                context.startActivity(intent);
                window.dismiss();
                layout.setVisibility(View.GONE);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SettingNewActivity.class);
                context.startActivity(intent);
                window.dismiss();
                layout.setVisibility(View.GONE);
            }
        });
        switchuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, LoginActivity.class);
                intent.putExtra("deletePassword", 1);
                context.startActivity(intent);
            }
        });
        return window;
    }

    /**
     * 获取所有用户的姓名和id用于多选操作
     * @param context
     * @return
     */
    public static List<Map<String, Object>> getMulSpinnerUser(Context context){
        SympUserInfoDao sympUserInfoDao = new SympUserInfoDao(context);
        List<UserInfo> userInfos = sympUserInfoDao.queryUserInfoList(null);
        //回复中的@,删除管理员和超级管理员
        for(int i = 0;i < userInfos.size();i++){
            UserInfo user = userInfos.get(i);
            if(user.getYhid().equals("guest") || user.getYhid().equals("admin")){
                userInfos.remove(user);
            }
        }

        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        for(int i = 0;i < userInfos.size();i++){
            Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("name",userInfos.get(i).getXm());
            userMap.put("id",userInfos.get(i).getYhid());
            userList.add(userMap);
        }
        return userList;
    }

    public static void communionImage(SympCommunicationAccessory bean,String fileUrl,Context context,String userId){
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        SympCommunicationAccessory sympCommunicationAccessory = new SympCommunicationAccessory();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        sympCommunicationAccessory.setId(bean.getId());
        sympCommunicationAccessory.setThemeid(bean.getThemeid());
        sympCommunicationAccessory.setReplyid(bean.getReplyid());
        sympCommunicationAccessory.setFileurl(fileUrl);
        sympCommunicationAccessory.setFilename(bean.getFilename());
        sympCommunicationAccessory.setRecordtime(bean.getRecordtime());
        sympCommunicationAccessory.setAccessorytype(bean.getAccessorytype());
        sympCommunicationAccessory.setRemarks(bean.getRemarks());
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(sympCommunicationAccessory);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(userId);
        sympMessageReal.setContent(sympCommunicationAccessory.TABLENAME + "," + bean.getId());
        sympMessageReal.setMessagestate("2");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(date);
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);
    }

    /**
     * 反馈图片 发送消息
     * @param bean
     * @param fileUrl
     * @param context
     * @param userId
     */
    public static void feedBackImage(TrFeedBackAccessory bean,String fileUrl,Context context,String userId){
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        TrFeedBackAccessory trFeedBackAccessory = new TrFeedBackAccessory();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        trFeedBackAccessory.setId(bean.getId());
        trFeedBackAccessory.setThemeid(bean.getThemeid());
        trFeedBackAccessory.setReplyid(bean.getReplyid());
        trFeedBackAccessory.setFileurl(fileUrl);
        trFeedBackAccessory.setFilename(bean.getFilename());
        trFeedBackAccessory.setRecordtime(bean.getRecordtime());
        trFeedBackAccessory.setAccessorytype(bean.getAccessorytype());
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trFeedBackAccessory);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
        List<SympMessageReal> sympMessageReals = new ArrayList<SympMessageReal>();
        SympMessageReal sympMessageReal = new SympMessageReal();
        sympMessageReal.setPresonid(userId);
        sympMessageReal.setContent(trFeedBackAccessory.TABLENAME + "," + bean.getId());
        sympMessageReal.setMessagestate("1");
        sympMessageReal.setMessagetype("2");
        sympMessageReal.setCreatedate(date);
        sympMessageReals.add(sympMessageReal);
        sympMessageRealDao.insertListData(sympMessageReals);
    }


    /**
     * 判断是否连接sd卡网络
     */
    public static boolean isConnNet(){
        if(GetPicFromWifiSDScard.wifiManager.isWifiEnabled()) {
            //获取连接wifi的信息
            WifiInfo wifiInfo= GetPicFromWifiSDScard.wifiManager.getConnectionInfo();
            //连接sd卡
            if(wifiInfo!=null&&wifiInfo.getSSID()!=null&&!wifiInfo.getSSID().equals("")&&(wifiInfo.getSSID().toLowerCase().contains("flashair")||wifiInfo.getSSID().toLowerCase().contains("efwifisd"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打开sd卡wifi列表
     */
    public static void openWifi(Context context,TrTask trTask,int i){
        OperationItemActivity act = (OperationItemActivity) context;
        Intent intentWifi = new Intent(context,WifiListActivity.class);
        Bundle bundleWifi = new Bundle();
        bundleWifi.putSerializable("count","1");
        bundleWifi.putSerializable("TrTask",trTask);
        bundleWifi.putInt("flag",i);
        intentWifi.putExtras(bundleWifi);
        act.startActivity(intentWifi);
        /*act.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);*/
    }

    /**
     * 图片是否存在
     * @return
     */
    public static boolean isExistPic(String route,String fileNumber,Context context){
        if(M3_SETTNG == null){
            M3_SETTNG = context.getSharedPreferences("M3_SETTNG", context.getApplicationContext().MODE_PRIVATE);
        }
        String file_prefix  = M3_SETTNG.getString("rh_file_prefix","");//前缀
        String file_postfix  = M3_SETTNG.getString("rh_file_postfix","");//后缀if

        String[] imgType = {".jpg", ".png", ".jpeg"};
        //拍照图片
        if(fileNumber.length() > 10){
            return true;
        }
        //图片类型
        for (int img = 0; img < imgType.length; img++) {
            //前缀包含;
            if(file_prefix.contains(";")){
                String prefixArray[] = file_prefix.split(";");
                for (int i = 0; i < prefixArray.length; i++) {
                    //后缀包含;
                    if(file_postfix.contains(";")) {
                        String postfixArray[] = file_postfix.split(";");
                        for(int j = 0;j < postfixArray.length;j++) {
                            String url = route + prefixArray[i] + fileNumber + postfixArray[j] + imgType[img];
                            File file = new File(url);
                            if(file.exists()){
                                return true;
                            }
                        }
                    }else{//后缀不包含;
                        String url = route + prefixArray[i] + fileNumber + file_postfix + imgType[img];
                        File file = new File(url);
                        if(file.exists()){
                            return true;
                        }
                    }
                }
            }else{//前缀不包含;
                String url = route + file_prefix + fileNumber + imgType[img];
                File file = new File(url);
                if(file.exists()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取word的intent
     * @param param
     * @return
     */
    public static Intent getWordFileIntent( String param ){
        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(param ));

        intent.setDataAndType(uri, "application/msword");

        return intent;

    }

    /**
     * 截取图号
     * @return
     */
    public static String getFileNumber(String fileName){
        String fileNumber = "";
        if(fileName != null && !("null").equals(fileName)){
            if(fileName.contains("_")){
                fileNumber = fileName.substring(fileName.indexOf("_") + 1,fileName.lastIndexOf("."));
            }else{
                fileNumber = fileName.substring(0,fileName.lastIndexOf("."));
            }
        }
        return fileNumber;
    }

    /**
     * 检查要打开的文件的后缀是否在遍历后缀数组中
     * @param checkItsEnd
     * @param fileEndings
     * @return
     */
    private static boolean checkEndsWithInStringArray(String checkItsEnd,String[] fileEndings){
        for(String aEnd : fileEndings){
            if(checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    /**
     * 根据文件的类型打开不同文件
     * @param context
     */
    public static void openFile(Context context,File currentPath){
        if(currentPath!=null&&currentPath.isFile()){
            String filePath = currentPath.toString();
            Intent intent = null;
            if(checkEndsWithInStringArray(filePath, context.getResources().
                    getStringArray(R.array.fileEndingImage))){
                intent = OpenFiles.getImageFileIntent(currentPath);
            }else if(checkEndsWithInStringArray(filePath, context.getResources().
                    getStringArray(R.array.fileEndingPackage))){
                intent = OpenFiles.getApkFileIntent(currentPath);
            }else if(checkEndsWithInStringArray(filePath, context.getResources().
                    getStringArray(R.array.fileEndingPdf))){
                intent = OpenFiles.getPdfFileIntent(currentPath);
            }else if(checkEndsWithInStringArray(filePath, context.getResources().
                    getStringArray(R.array.fileEndingWord))){
                intent = OpenFiles.getWordFileIntent(currentPath);
            }
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.GET_ACTIVITIES);
            if(list.size() > 0){
                context.startActivity(intent);
            }else{//未安装
                TrCommonTools trCommonTools = new TrCommonTools();
                String opt = "";
                if(filePath.endsWith("pdf")){
                    trCommonTools.setFilename("PDF阅读器.apk");
                    String url = apkPath + "PDF阅读器.apk";
                    final File file = new File(url);
                    opt = isDownLoadApk(trCommonTools,context,file);

                    dialog(context,"未安装PDF阅读器",opt,file,1);
                }else if(filePath.endsWith("doc") || filePath.endsWith("docx")){
                    trCommonTools.setFilename("WORD.apk");
                    String url = apkPath + "WORD.apk";
                    final File file = new File(url);
                    opt = isDownLoadApk(trCommonTools,context,file);

                    dialog(context,"未安装WORD",opt,file,2);
                }
            }
        }
    }

    /**
     * 判断是下载还是安装
     * @param trCommonTools
     * @param context
     * @param file
     * @return
     */
    private static String isDownLoadApk(TrCommonTools trCommonTools, Context context, File file){
        String opt = "",fileSize = "",fileSize2 = "";
        TrCommonToolsDao trCommonToolsDao = new TrCommonToolsDao(context);
        List<TrCommonTools> trCommonToolsList = trCommonToolsDao.queryTrCommonToolsList(trCommonTools);
        if(trCommonToolsList.size() > 0){
            trCommonTools = trCommonToolsList.get(0);
            fileSize = trCommonTools.getFilesize();
        }
        //本地文件大小
        Long length = file.length();
        if(length != null){
            fileSize2 = String.valueOf(length);
        }
        //文件不存在或者不完整就下载
        if(!file.exists() || !fileSize.equals(fileSize2)){
            opt = "下载";
        }else{
            opt = "安装";
        }
        return opt;
    }

    private static void dialog(final Context context,String title, final String opt, final File file, final int type){
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title).setMessage("").setPositiveButton(opt,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opt.endsWith("安装")){
                    openFile(context, file);
                }else if(opt.endsWith("下载")){
                    Intent intent = new Intent(context, CommToolsActivity.class);
                    intent.setFlags(type);
                    context.startActivity(intent);
                }
                mMaterialDialog.dismiss();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        }).setCanceledOnTouchOutside(false).show();
    }
    /**
     * 自动获得焦点并弹出软键盘
     * @return
     */
    public static void showSoftKeyboard(View view){
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 判断app是否已安装
     * @param context
     * @param uri
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * @Description long转文件大小M单位方法
     * @author temdy
     * @param bytes
     * @return
     */
    public static String byteToM(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        return returnValue + "";
    }

    /**
     * 记录异常信息
     */
    public static void recordExceptionInfo(Exception e,Context context,String exceptionclass){
        String id= Constants.getUuid();
        String time=Constants.dateformat2.format(new Date());
        String userId = Constants.getLoginPerson(context).get("userId");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        String exceptionmessage = sw.toString();

        TrExceptionInfo exceptionInfo=new TrExceptionInfo(id,exceptionclass,exceptionmessage,userId,"1",time); //1表示移动端的异常
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(exceptionInfo);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
    }
}
