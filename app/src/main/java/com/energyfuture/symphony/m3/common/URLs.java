package com.energyfuture.symphony.m3.common;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

/**
 * 接口URL实体�? (http://my.oschina.net/liux)
 * 
 * @version 1.0
 * @created 2012-3-21
 */
public class URLs implements Serializable {

//  消息数据上传IP+端口地址  本地  117
//    public final static String HOST = "117.78.32.59:80";
//    //平台消息获取IP+端口地址
//    public final static String HOSTA = "117.78.32.59:80";
//    //mqtt消息获取IP+端口地址
//    public final static String MQTTHOST = "tcp://117.78.32.59:1883";


        //消息数据上传IP+端口地址
    public final static String HOST = "115.28.34.4:81";
    //平台消息获取IP+端口地址
    public final static String HOSTA = "115.28.34.4:81";
    //mqtt消息获取IP+端口地址
    public final static String MQTTHOST = "tcp://115.28.34.4:1883";

//    public final static String HOST = "192.168.2.169:80";
//    //平台消息获取IP+端口地址
//    public final static String HOSTA = "192.168.2.169:80";
//    //mqtt消息获取IP+端口地址
//    public final static String MQTTHOST = "tcp://192.168.2.169:1883";


//    public final static String HOST = "192.168.2.59:8080";// 192.168.1.213  115.28.163.240:83
//    //平台消息获取IP+端口地址
//    public final static String HOSTA = "192.168.2.59:8080";// 192.168.1.213   115.28.163.240:83
//    //mqtt消息获取IP+端口地址
//    public final static String MQTTHOST = "tcp://192.168.2.59:1883";


    //消息数据上传IP+端口地址
//    public final static String HOST = "192.168.2.69:8080";
//    //平台消息获取IP+端口地址
//    public final static String HOSTA = "192.168.2.69:8080";
//    //mqtt消息获取IP+端口地址
//    public final static String MQTTHOST = "tcp://192.168.2.69:1883";


    //图片地址
    public final static String IMAGEPATH = "/INSP/IMG/";
    public final static String PROJECT = "Azzandroid";
    public static Map<String,Float> flowMap = new HashMap<String, Float>();
	public final static String PROJECTA = "inspectfuture-dataService";
    public final static String GETDATAFROMWEB = "inspectfuture-dataAcquisitionService";
    public final static String SENDMESSAGE = "recevieDeviceService";
	public final static String HTTP = "http://";
	public final static String HTTPS = "http://";

	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";

	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	private final static String URL_API_URL = HTTP + HOST + URL_SPLITTER
			+ PROJECT + URL_SPLITTER;
	private final static String URL_API_URLA = HTTP + HOSTA + URL_SPLITTER


			+ PROJECTA + URL_SPLITTER;

	public final static String LOGIN_VALIDATE_HTTP = HTTP + HOST + URL_SPLITTER
			+ "action/api/login_validate";
	public final static String LOGIN_VALIDATE_HTTPS = HTTPS + HOST
			+ URL_SPLITTER + "action/api/login_validate";
	public final static String PORTRAIT_UPDATE = URL_API_HOST
			+ "action/api/portrait_update";
    //版本更新地址
    public final static String UPDATE_VERSION = URL_API_HOST + "versionUpdate/update.xml";

    /*******红外温度越限阀值**********/
    public static float INFRARED_UP_THRESHOLD_VALUE = 300;  //红外越限上限值

    public static float INFRARED_DN_THRESHOLD_VALUE = -30;  //红外越限下限值


    /************超高频局放幅值越限阀值********************/
    public static float UHFPD_UP_THRESHOLD_VALUE = 4000; //幅值上限值

    public static float UHFPD_DN_THRESHOLD_VALUE = 4000;  //幅值下限值

    public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 执行过程上传图片保存地址
	public final static String UPLOAD_PICTURE_ADD = "/storage/emulated/0/PMW/picture/";
	// 拍照图片保存地址
	public final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory() + "/PMW/picture/";
	// 获取最新的任务条数
	public final static String GETNEWTASKCOUNT = URL_API_URLA
			+ "getNewTaskCount.action";
	// 获取最新的任务内容
	public final static String GETMESSAGE = URL_API_URLA + "getMessageInfo.action";
	// 提交数据到云平台
	public final static String RECEIVEMESSAGEINFO = URL_API_URLA
			+ "receiveMessageInfo.action";

    //将消息表中的数据提交到云平台上
    public final static String MESSAGESEND = "receiveUploadData.action";

    public final static String GETDATAFROMWEBEND = "getdataAcquisition.action";
    public final static String ADDPROJECT = "addProjet.action";
    public final static String ADDTASK = "addTask.action";
    public final static String ADDSTATION = "saveStation.action";
    public final static String ADDEQUIPMENT = "saveEquipment.action";
    public final static String PROJECTCONTRAST = "getProjectByNotId.action";
    public final static String PROJECTDOWNLOAD = "getTaskByProjectId.action";
    public final static String REPORTDOWNLOAD = "getReportList.action";
    public final static String BUREAUL = "getBureauL.action";
    public final static String COMMONTOOLSDOWNLOAD = "getPhoneCommontools.action";
    public final static String DELETEPRO = "deleteProjet.action";
    public final static String DELETETASK = "deleteTaskInfo.action";

    public final static String UPDATEUSER = "updateUser.action";
    public final static String UPDATEUSERICON = "uploadUserImage.action";


    public final static String GETAPPUPTODATEVERSION="getAppUpToDateVersion.action";

    public final static String IDENTIFYING = "getApplyempowerInfo.action";

	public final static String SAVEATTACH = URL_API_URLA
			+ "saveAttach.action";

	private final static String URL_HOST = "oschina.net";
	private final static String URL_WWW_HOST = "www." + URL_HOST;
	private final static String URL_MY_HOST = "my." + URL_HOST;

	private final static String URL_TYPE_NEWS = URL_WWW_HOST + URL_SPLITTER
			+ "news" + URL_SPLITTER;
	private final static String URL_TYPE_SOFTWARE = URL_WWW_HOST + URL_SPLITTER
			+ "p" + URL_SPLITTER;
	private final static String URL_TYPE_QUESTION = URL_WWW_HOST + URL_SPLITTER
			+ "question" + URL_SPLITTER;
	private final static String URL_TYPE_BLOG = URL_SPLITTER + "blog"
			+ URL_SPLITTER;
	private final static String URL_TYPE_TWEET = URL_SPLITTER + "tweet"
			+ URL_SPLITTER;
	private final static String URL_TYPE_ZONE = URL_MY_HOST + URL_SPLITTER
			+ "u" + URL_SPLITTER;
	private final static String URL_TYPE_QUESTION_TAG = URL_TYPE_QUESTION
			+ "tag" + URL_SPLITTER;


	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_TWEET = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;

	public final static String CODE = "UTF-8";

	private int objId;
	private String objKey = "";
	private int objType;

	public int getObjId() {
		return objId;
	}

	public void setObjId(int objId) {
		this.objId = objId;
	}

	public String getObjKey() {
		return objKey;
	}

	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	/**
	 * 转化URL为URLs实体
	 *
	 * @param path
	 * @return 不能转化的链接返回null
	 */
	public final static URLs parseURL(String path) {
		if (StringUtils.isEmpty(path))
			return null;
		path = formatURL(path);
		URLs urls = null;
		String objId = "";
		try {
			URL url = new URL(path);
			// 站内链接
			if (url.getHost().contains(URL_HOST)) {
				urls = new URLs();
				// www
				if (path.contains(URL_WWW_HOST)) {
					// 新闻
					// www.oschina.net/news/27259/mobile-internet-market-is-small
					if (path.contains(URL_TYPE_NEWS)) {
						objId = parseObjId(path, URL_TYPE_NEWS);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_NEWS);
					}
					// 软件 www.oschina.net/p/jx
					else if (path.contains(URL_TYPE_SOFTWARE)) {
						urls.setObjKey(parseObjKey(path, URL_TYPE_SOFTWARE));
						urls.setObjType(URL_OBJ_TYPE_SOFTWARE);
					}
					// 问答
					else if (path.contains(URL_TYPE_QUESTION)) {
						// 问答-标签 http://www.oschina.net/question/tag/python
						if (path.contains(URL_TYPE_QUESTION_TAG)) {
							urls.setObjKey(parseObjKey(path,
									URL_TYPE_QUESTION_TAG));
							urls.setObjType(URL_OBJ_TYPE_QUESTION_TAG);
						}
						// 问答 www.oschina.net/question/12_45738
						else {
							objId = parseObjId(path, URL_TYPE_QUESTION);
							String[] _tmp = objId.split(URL_UNDERLINE);
							urls.setObjId(StringUtils.toInt(_tmp[1]));
							urls.setObjType(URL_OBJ_TYPE_QUESTION);
						}
					}
					// other
					else {
						urls.setObjKey(path);
						urls.setObjType(URL_OBJ_TYPE_OTHER);
					}
				}
				// my
				else if (path.contains(URL_MY_HOST)) {
					// 博客 my.oschina.net/szpengvictor/blog/50879
					if (path.contains(URL_TYPE_BLOG)) {
						objId = parseObjId(path, URL_TYPE_BLOG);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_BLOG);
					}
					// 动弹 my.oschina.net/dong706/tweet/612947
					else if (path.contains(URL_TYPE_TWEET)) {
						objId = parseObjId(path, URL_TYPE_TWEET);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_TWEET);
					}
					// 个人专页 my.oschina.net/u/12
					else if (path.contains(URL_TYPE_ZONE)) {
						objId = parseObjId(path, URL_TYPE_ZONE);
						urls.setObjId(StringUtils.toInt(objId));
						urls.setObjType(URL_OBJ_TYPE_ZONE);
					} else {
						// 另一种个人专�? my.oschina.net/dong706
						int p = path.indexOf(URL_MY_HOST + URL_SPLITTER)
								+ (URL_MY_HOST + URL_SPLITTER).length();
						String str = path.substring(p);
						if (!str.contains(URL_SPLITTER)) {
							urls.setObjKey(str);
							urls.setObjType(URL_OBJ_TYPE_ZONE);
						}
						// other
						else {
							urls.setObjKey(path);
							urls.setObjType(URL_OBJ_TYPE_OTHER);
						}
					}
				}
				// other
				else {
					urls.setObjKey(path);
					urls.setObjType(URL_OBJ_TYPE_OTHER);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			urls = null;
		}
		return urls;
	}

	/**
	 * 解析url获得objId
	 *
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjId(String path, String url_type) {
		String objId = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if (str.contains(URL_SPLITTER)) {
			tmp = str.split(URL_SPLITTER);
			objId = tmp[0];
		} else {
			objId = str;
		}
		return objId;
	}

	/**
	 * 解析url获得objKey
	 *
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjKey(String path, String url_type) {
		path = URLDecoder.decode(path);
		String objKey = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if (str.contains("?")) {
			tmp = str.split("?");
			objKey = tmp[0];
		} else {
			objKey = str;
		}
		return objKey;
	}

	/**
	 * 对URL进行格式处理
	 *
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if (path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}
}
