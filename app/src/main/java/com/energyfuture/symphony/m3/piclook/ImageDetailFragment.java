package com.energyfuture.symphony.m3.piclook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.piclook.uk.co.senab.photoview.PhotoViewAttacher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl,localurl,filename;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl,String localurl,String filename) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
        args.putString("localurl", localurl);
        args.putString("filename", filename);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        localurl = getArguments() != null ? getArguments().getString("localurl") : null;
        filename = getArguments() != null ? getArguments().getString("filename") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        File filePic = new File(localurl + "/" + filename);
        if (filePic.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePic.getPath());
            mImageView.setImageBitmap(bitmap);
            mImageView.setTag(filename);//表示已有图片
            //                   添加图片名称     代盼   2015/9/30

        } else {
		    ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
		    	@Override
		    	public void onLoadingStarted(String imageUri, View view) {
		    		progressBar.setVisibility(View.VISIBLE);
		    	}

		    	@Override
		    	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		    		String message = "";
		    		switch (failReason.getType()) {
//		    		case IO_ERROR:
//		    			message = "下载错误";
//		    			break;
		    		case DECODING_ERROR:
		    			message = "图片无法显示";
		    			break;
		    		case NETWORK_DENIED:
		    			message = "网络有问题，无法下载";
		    			break;
		    		case OUT_OF_MEMORY:
		    			message = "图片太大无法显示";
		    			break;
		    		case UNKNOWN:
		    			message = "未知的错误";
		    			break;
		    		}
		    		//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		    		progressBar.setVisibility(View.GONE);
		    	}

		    	@Override
		    	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Base.saveMyBitmap(filename, loadedImage, localurl);
		    		progressBar.setVisibility(View.GONE);
                    mImageView.setTag(filename);
		    		mAttacher.update();
		    	}
		    });
        }
	}
}
