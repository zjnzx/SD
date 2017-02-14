package com.energyfuture.symphony.m3.common;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.ProjectListActivity;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 *  (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	public static int TASK_NEW_COUNT = 0;
	
	//屏幕的高度像素点
	public static int Screen_Point_Height = 1920;
	//屏幕的宽度像素点
	public static int Screen_Point_Width = 1280;
	
	/** 表情图片匹配 */
	private static Pattern facePattern = Pattern.compile("\\[{1}([0-9]\\d*)\\]{1}");
	
	/** 全局web样式 */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
			"img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
			"pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
			"a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	/**
	 * 显示首页
	 * @param activity
	 */
	public static void showHome(Activity activity)
	{
		/*//Intent intent = new Intent(activity,Main.class);
		activity.startActivity(intent);
		activity.finish();*/
	}
	
	/**
	 * 显示登录页面
	 * @param activity
	 */
//	public static void showLoginDialog(Activity context)
//	{
//		Intent intent = new Intent(context,LoginDialog.class);
//		/*if(context instanceof Main)
//			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
//		else if(context instanceof Setting)
//			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
//		else*/
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
//	}
	
	/**
	 * 显示任务详情页面
	 * @param activity
	 */
//	public static void showTaskInfo(Activity context,String taskid)
//	{
//		Intent intent = new Intent(context,JobInfoActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("TASK_ID", taskid);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
//	}
	
	
	/**
	 * 显示列表页面
	 * @param
	 */
	public static void showRwgl(Activity context)
	{
		Intent intent = new Intent(context,ProjectListActivity.class);
		
		//Intent intent = new Intent(context,AtyTemperature.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	
	
	/**
	 * 显示设置画面
	 * @param activity
	 */
//	public static void showSettingActivity(Activity context)
//	{
//		Intent intent = new Intent(context,LoginDialog.class);
//		/*if(context instanceof Main)
//			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
//		else if(context instanceof Setting)
//			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
//		else*/
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
//	}
	
	
	/**
	 * 显示系统设置界面
	 * @param context
	 */
//	public static void showSetting(Activity context)
//	{
//		Intent intent = new Intent(context, AtySetting.class);
//		context.startActivity(intent);
//	}
	
	/**
	 * 显示在线监测界面
	 * @param context
	 */
//	public static void showZxjc(Activity context)
//	{
//		//Intent intent = new Intent(context, WebViewDemo1.class);
//		Intent intent = new Intent(context, AtyZxjc.class);
//
//		context.startActivity(intent);
//	}
	
	
	/**
	 * 显示分析报告
	 * @param context
	 */
//	public static void showFxbg(Activity context)
//	{
//		//Intent intent = new Intent(context, WebViewDemo1.class);
//		Intent intent = new Intent(context, WebViewDemo1.class);
//
//		context.startActivity(intent);
//	}
	/**
	 * 显示检验报告
	 * @param context
	 */
//	public static void showJybg(Activity context)
//	{
//		//Intent intent = new Intent(context, WebViewDemo1.class);
//		Intent intent = new Intent(context, WebViewDemo.class);
//
//		context.startActivity(intent);
//	}
	
	/**
	 * 显示例行检修界面
	 * @param context
	 */
//	public static void showByqysplxjx(Activity context)
//	{
//		Intent intent = new Intent(context, AtyPrcocessYsplxjx.class);
//		context.startActivity(intent);
//	}
	
	/**
	 * 显示智能诊断界面
	 * @param context
	 */
//	public static void showZnzd(Activity context)
//	{
//		Intent intent = new Intent(context, AtyZnzd.class);
//		context.startActivity(intent);
//	}
	
	/**
	 * 显示登录页面
	 * @param context
	 */
//	public static void showLogin(Activity context)
//	{
//		Intent intent = new Intent(context, AtyLogin.class);
//		context.startActivity(intent);
//	}
	
	
	/**
	 * 登录主界面
	 * @param context
	 */
//	public static void showMainActivity(Activity context)
//	{
//		Intent intent = new Intent(context, AtyMain.class);
//		context.startActivity(intent);
//	}
	
	
	/**
	 * 启动图片放大查看页面
	 * @designer
	 * @since  2014-11-27
	 * @author heqiang
	 * @param @param context
	 * @param @param intent 
	 * @return void
	 */
	public static void showImageView(Activity context,Intent intent)
	{
		context.startActivity(intent);
	}
	
	
	/**
	 * 点击返回监听事件
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity)
	{
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}	
	
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Activity cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Activity cont,int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Activity cont,String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
	
	
	/**
	 * 编辑器显示保存的草稿
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
//	public static void showTempEditContent(Activity context, EditText editer, String temlKey) {
//		//
//		String tempContent = "";
//		if(!StringUtils.isEmpty(tempContent)) {
//			SpannableStringBuilder builder = parseFaceByText(context, tempContent);
//			editer.setText(builder);
//			editer.setSelection(tempContent.length());//设置光标位置
//		}
//	}
	
	/**
	 * 将[12]之类的字符串替换为表情
	 * @param context
	 * @param content
	 */
//	public static SpannableStringBuilder parseFaceByText(Activity context, String content) {
//		SpannableStringBuilder builder = new SpannableStringBuilder(content);
//		Matcher matcher = facePattern.matcher(content);
//		while (matcher.find()) {
//			//使用正则表达式找出其中的数字
//			int position = StringUtils.toInt(matcher.group(1));
//			int resId = 0;
//			try {
//				if(position > 65 && position < 102)
//					position = position-1;
//				else if(position > 102)
//					position = position-2;
//				Drawable d = context.getResources().getDrawable(resId);
//				d.setBounds(0, 0, 35, 35);//设置表情图片的显示大小
//				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
//				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			} catch (Exception e) {
//			}
//		}
//		return builder;
//	}
	
	/**
	 * 清除文字
	 * @param cont
	 * @param editer
	 */
//	public static void showClearWordsDialog(final Context cont, final EditText editer, final TextView numwords)
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
//		builder.setTitle("清除文字吗？");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				//清除文字
//				editer.setText("");
//				numwords.setText("160");
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.show();
//	}
	
	/**
	 * 发送广播-发布动弹
	 * @param context
	 * @param notice
	 */
//	public static void sendBroadCastTweet(Activity context, int what, Result res, Tweet tweet){
//		if(res==null && tweet==null) return;
//		Intent intent = new Intent("net.oschina.app.action.APP_TWEETPUB");
//		intent.putExtra("MSG_WHAT", what);
//		if(what == 1)
//			intent.putExtra("RESULT", res);
//		else
//			intent.putExtra("TWEET", tweet);
//		context.sendBroadcast(intent);
//	}
	
	/**
	 * 获取控件实例
	 * @param context
	 * @param text 提示消息
	 * @param isSound 是否播放声音
	 * @return
	 */
//	public static NewDataToast makeText(Activity context, CharSequence text, boolean isSound) {
//		NewDataToast result = new NewDataToast(context, isSound);
//
//        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//
//        View v = inflate.inflate(R.layout.new_data_toast, null);
//        v.setMinimumWidth(dm.widthPixels);//设置控件最小宽度为手机屏幕宽度
//
//        TextView tv = (TextView)v.findViewById(R.id.new_data_toast_message);
//        tv.setText(text);
//
//        result.setView(v);
//        result.setDuration(600);
//        result.setGravity(Gravity.TOP, 0, (int)(dm.density*75));
//
//        return result;
//    }
	
	/**
	 * 查看图片
	 * @param context
	 * @param imgUrl
	 */
//	public static void showImageZoomDialog(Context context, String imgUrl)
//	{
//		Intent intent = new Intent(context, ImageZoomDialog.class);
//		intent.putExtra("img_url", imgUrl);
//		context.startActivity(intent);
//	}
	
	
}
