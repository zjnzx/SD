package com.energyfuture.symphony.m3.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;

import java.util.ArrayList;
import java.util.List;

//使用BaseAdapter来存储取得的文件
public class IconifiedTextListAdapter extends BaseAdapter {
    private Context mContext = null;
    private String s=null;
    // 用于显示文件的列表
    private List<IconifiedText> mItems = new ArrayList<IconifiedText>();

    public IconifiedTextListAdapter(Context context) {
        mContext = context;
    }


    // 设置文件列表
    public void setListItems(List<IconifiedText> lit) {
        mItems = lit;
    }

    // 得到文件的数目,列表的个数
    public int getCount() {
        return mItems.size();
    }

    // 得到一个文件
    public Object getItem(int position) {
        return mItems.get(position);
    }

    // 能否全部选中
    public boolean areAllItemsSelectable() {
        return false;
    }

    // 判断指定文件是否被选中
    public boolean isSelectable(int position) {
        return mItems.get(position).isSelectable();
    }

    // 得到一个文件的ID
    public long getItemId(int position) {
        return position;
    }

    // 重写getView方法来返回一个IconifiedTextView（我们自定义的文件布局）对象
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        TextView textView = null;
        if (convertView == null) {
            //根据自定义的Item布局加载布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        } else {

        }
        imageView=(ImageView) convertView.findViewById(R.id.imageView);
        textView=(TextView) convertView.findViewById(R.id.textView);
        imageView.setImageDrawable(mItems.get(position).getIcon());
        s=mItems.get(position).getText();
        s=s.substring(1);
        textView.setText(s);
        return convertView;
    }
}
