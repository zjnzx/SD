package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.piclook.ImagePagerFeedBackActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2015/8/14 0014.
 */
public class TrFeedBackSendReplyPicAdapter extends BaseAdapter {
    private Context context;
    private String id;
    private List<String> list;
    private DisplayImageOptions options;
    private String picUrl,opicUrl,themeId;
    private ImageLoader imageLoader;
    public static String pathTemp = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static  String FILE_SAVEPATH = pathTemp.substring(0, pathTemp.length() - 1) + "legacy" + "/M3/transformer/";
    public LruCache<String,Bitmap> lruCache = new LruCache<String,Bitmap>(20);
    public TrFeedBackSendReplyPicAdapter(Context context, List<String> list, String id, String picUrl, String opicUrl, String themeId){
        this.context = context;
        this.list = list;
        this.id = id;
        this.picUrl = picUrl;
        this.opicUrl = opicUrl;
        this.themeId = themeId;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_loading)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new SimpleBitmapDisplayer())
                .build();
        imageLoader = ImageLoader.getInstance();
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.communion_reply_picture_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.send_reply_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(list.get(position) != null && !list.get(position).equals("")) {
            File file = new File(FILE_SAVEPATH+list.get(position));
            if (file.exists()) {
               /* imageLoader.displayImage("file://" +FILE_SAVEPATH+ list.get(position), holder.imageView, options);*/
                final String imageuri="file://" +FILE_SAVEPATH+ list.get(position);
                final ViewHolder finalHolder1 = holder;
                finalHolder1.imageView.setTag(imageuri);
                imageLoader.loadImage(imageuri,options,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if(imageuri.equals(finalHolder1.imageView.getTag())){
                            finalHolder1.imageView.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
            } else {
                final String url = URLs.HTTP + URLs.HOSTA + "/INSP/" + list.get(position);
                //显示图片的配置
                final ViewHolder finalHolder = holder;
                ImageLoader.getInstance().displayImage(url, holder.imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        //开始加载
                        finalHolder.imageView.setImageResource(R.drawable.pic_loading);
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        //加载成功
                        String fileName = list.get(position).substring(list.get(position).lastIndexOf("/") + 1);
                        Base.saveMyBitmap(fileName, arg2, picUrl);
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        //加载取消
                    }
                });
            }
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = URLs.HTTP+URLs.HOSTA+"/INSP/feedback/" + themeId +"/original/";
                Intent intent = new Intent(context, ImagePagerFeedBackActivity.class);
                intent.putExtra("image_index", position);
                intent.putExtra("url", url);
                intent.putExtra("localurl",opicUrl);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder {
       private ImageView imageView;
    }
}