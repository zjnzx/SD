package com.energyfuture.symphony.m3.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * @author dingwujun
 * @blog http://blog.csdn.net/coolszy
 */
public class ImageViewActivity extends Activity {
	private ImageView imageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	   
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.imageview);
		imageView = (ImageView) findViewById(R.id.imageView);
		Intent intent = getIntent();
		String url_pic = intent.getStringExtra("url");
		File file = new File( url_pic);
		Picasso.with(ImageViewActivity.this).load(file)
				.placeholder(R.drawable.ic_switch_camera_grey600_36dp).error(R.drawable.ic_switch_camera_grey600_36dp)
				.into(imageView);
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) // 如果SD卡存在，则获取跟目录
		{
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}
}
