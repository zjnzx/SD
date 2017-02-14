package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;

import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 */
public class FirstCaptureCardAdapter extends BaseAdapter{
    private Context context;
    List<TrDetectiontempletFileObj> filelist;
    private boolean flag;

    public FirstCaptureCardAdapter(Context context, List<TrDetectiontempletFileObj> filelist) {
        this.context = context;
        this.filelist = filelist;
    }

    public FirstCaptureCardAdapter(Context context) {
        this.context = context;
    }

    public void addNumber(){
        filelist.add(new TrDetectiontempletFileObj());
    }

    public void setFlag(){
        flag=true;
    }

    @Override
    public int getCount() {
        return filelist.size();
    }

    @Override
    public Object getItem(int position) {
        return filelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myviewholder myviewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.image_gridview_item, null);
            myviewholder=new Myviewholder(convertView);
            convertView.setTag(myviewholder);
        }else{
            myviewholder= (Myviewholder) convertView.getTag();
        }

        if(position==(filelist.size()-2)&&flag==true){   //用来将前一个位置添加图片还原为正常的显示图片的页面
            myviewholder.linearLayout.setVisibility(View.VISIBLE);
            myviewholder.image_show.setVisibility(View.VISIBLE);
            myviewholder.image_add.setVisibility(View.GONE);
        }

        if(position==(filelist.size()-1)){  //换为添加图片
            myviewholder.linearLayout.setVisibility(View.GONE);
            myviewholder.image_show.setVisibility(View.GONE);
            myviewholder.image_add.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class Myviewholder{
        LinearLayout linearLayout;
        EditText image_number;
        ImageView image_show,image_add;

        Myviewholder(View v) {
            linearLayout= (LinearLayout) v.findViewById(R.id.image_ll);
            image_number= (EditText) v.findViewById(R.id.image_number);
            image_show= (ImageView) v.findViewById(R.id.image_show);
            image_add= (ImageView) v.findViewById(R.id.image_add);
        }
    }
}
