package com.energyfuture.symphony.m3.version;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.AppException;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 应用程序更新工具
 * @author 
 * @version 1.1
 * @created 2015-2-12
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
	
    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL   = 1;
    
	private static UpdateManager updateManager;
	
	private Context mContext;
	//通知对话框
	private Dialog noticeDialog;
	//下载对话框
	private Dialog downloadDialog;
	//'已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
    //进度条
    private ProgressBar mProgress;
    //显示下载数值
    private TextView mProgressText;
    //查询动画
    private ProgressDialog mProDialog;
    //进度值
    private int progress;
    //下载线程
    private Thread downLoadThread;
    //终止标记
    private boolean interceptFlag;
	//提示语
	private String updateMsg = "";
	//返回的安装包url
	private String apkUrl = "";
	//下载包保存路径
    private String savePath = "";
	//apk保存完整路径
	private String apkFilePath = "";
	//临时下载文件路径
	private String tmpFilePath = "";
	//下载文件大小
	private String apkFileSize;
	//已下载文件大小
	private String tmpFileSize;
	
	private String curVersionName = "";
	private int curVersionCode;
	private Update mUpdate;
    private TextView versionUpdate;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				
				String a =apkFileSize;
				String b =tmpFileSize;
				
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
				break;
			}
    	};
    };
    
	public static UpdateManager getUpdateManager() {
		if(updateManager == null){
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}
	
	/**
	 * 检查App更新
	 * @param context
	 * @param isShowMsg 是否显示提示消息
	 */
	public void checkAppUpdate(final Context context,final boolean isShowMsg,TextView versionText){
		this.mContext = context;
        this.versionUpdate = versionText;
		getCurrentVersion();
        versionUpdate.setText(Html.fromHtml("版本&nbsp;&nbsp; " + curVersionName.split("-")[0]));
		if(isShowMsg){
            if(mProDialog == null)
                mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...", true, true);
            else if(mProDialog.isShowing() || (latestOrFailDialog!=null && latestOrFailDialog.isShowing()))
                return;
        }
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				//进度条对话框不显示 - 检测结果也不显示
				if(mProDialog != null && !mProDialog.isShowing()){
					return;
				}
				//关闭并释放释放进度条对话框
				if(isShowMsg && mProDialog != null){
					mProDialog.dismiss();
					mProDialog = null;
				}
				//显示检测结果
				if(msg.what == 1){
					mUpdate = (Update)msg.obj;
					if(mUpdate != null){
						if(curVersionCode < mUpdate.getVersionCode()){
							apkUrl = mUpdate.getDownloadUrl();
							updateMsg = mUpdate.getUpdateLog();
							showNoticeDialog();
						}else if(isShowMsg){
							showLatestOrFailDialog(DIALOG_TYPE_LATEST);
						}
					}
				}else if(isShowMsg){
					showLatestOrFailDialog(DIALOG_TYPE_FAIL);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
                        Update update = ApiClient.checkVersion(null);
                        msg.what = 1;
                        msg.obj = update;
				} catch (AppException e) {
					e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString() + "/UpdateManager");
				}
				handler.sendMessage(msg);
			}			
		}.start();		
	}

    /**
     * 检查App更新
     * @param versionText
     */
    public void checkAppUpdate_(final Context context, TextView versionText){
        this.mContext = context;
        this.versionUpdate = versionText;
        getCurrentVersion();
        final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                //显示检测结果
                if(msg.what == 1){
                    mUpdate = (Update)msg.obj;
                    if(mUpdate != null){
                        if(curVersionCode < mUpdate.getVersionCode()){
                            //调用弹出窗口
//
                            versionUpdate.getText();
                            versionUpdate.setText(Html.fromHtml("版本更新&nbsp;<font color='#ff0000'>●</font>"));
                            showNoticeDialog();


                        }
                    }
                }
            }
        };
        new Thread(){
            public void run() {
                Message msg = new Message();
                try {
                    Update update = ApiClient.checkVersion(null);
                    msg.what = 1;
                    msg.obj = update;
                } catch (AppException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString() + "/UpdateManager");
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			//关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		Builder builder = new Builder(mContext);
        builder.setTitle("系统提示");
        if (dialogType == DIALOG_TYPE_LATEST) {
            builder.setMessage("您当前已经是最新版本");
        } else if (dialogType == DIALOG_TYPE_FAIL) {
            builder.setMessage("无法获取版本更新信息");
        }
        builder.setPositiveButton("确定", null);
        latestOrFailDialog = builder.create();
        latestOrFailDialog.show();

	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion(){
        try {
        	PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        	curVersionName = info.versionName;
        	curVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
	}
	
	/**
	 * 显示版本更新通知对话框
	 */
	public void showNoticeDialog(){
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(Html.fromHtml(updateMsg));
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
//		builder.setNegativeButton("以后再说", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent i = new Intent(mContext, LoginActivity.class);
//                mContext.startActivity(i);
//
//			}
//		});
		noticeDialog = builder.create();
		noticeDialog.show();
        noticeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return true;
            }
        });
	}


	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog(){
		Builder builder = new Builder(mContext);
		builder.setTitle("正在下载新版本");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		
		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
        downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return true;
            }
        });
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				SimpleDateFormat simpledateformat=new SimpleDateFormat("yyyyMMddHHmm");
				String apkName = "M3App_"+mUpdate.getVersionName()+"_"+simpledateformat.format(new Date())+".apk";
				String tmpApk = "M3App_"+mUpdate.getVersionName()+"_"+simpledateformat.format(new Date())+".tmp";
				//判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/M3/update/";
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				
				//没有挂载SD卡，无法下载文件
				if(apkFilePath == null || apkFilePath == ""){
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				
				File ApkFile = new File(apkFilePath);

				//是否已下载更新文件
				if(ApkFile.exists()){
					downloadDialog.dismiss();
					installApk();
					return;
				}
				
				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//显示文件大小格式：2个小数点显示
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//进度条下面显示的总文件大小
		    	apkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    		//进度条下面显示的当前下载文件大小
		    		tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//当前进度值
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//下载完成 - 将临时下载文件转成APK文件
						if(tmpFile.renameTo(ApkFile)){
							//通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
                if(mContext!=null){
                    Constants.recordExceptionInfo(e, mContext, mContext.toString() + "/UpdateManager");
                }
			} catch(IOException e){
				e.printStackTrace();
                if(mContext!=null){
                    Constants.recordExceptionInfo(e, mContext, mContext.toString() + "/UpdateManager");
                }
			}
		}
	};
	
	/**
	* 下载apk
	*/
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	/**
    * 安装apk
    */
	private void installApk(){
		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
}
