package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 */
public class FlowScreenAdapter extends BaseAdapter{

    public Context context;
    public List<UserInfo> userInfos;

    public  FlowScreenAdapter(Context context,List<UserInfo> userInfos){

        this.context = context;
        this.userInfos = userInfos;
    }

    @Override
    public int getCount() {
        return userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.child_list_item, null);
            viewHolder.mTxtInfo = (TextView) convertView.findViewById(R.id.txt_screen_info);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

            String userName = userInfos.get(position).getXm();
            viewHolder.mTxtInfo.setText(userName);

        return convertView;
    }

    static class ViewHolder {

        TextView mTxtInfo;

    }
}
