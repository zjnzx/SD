package com.energyfuture.symphony.m3.util;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.SympVersion;
import com.energyfuture.symphony.m3.domain.TrVersionUpdate;

import java.util.List;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class VersionUpdateAdapter extends BaseAdapter {
    private Context context;
    private List<TrVersionUpdate> datalist = null;
    private TrVersionUpdate versionUpdate;
    public VersionUpdateAdapter(List<TrVersionUpdate> list,Context context){

        this.context = context;
        this.datalist = list;
    }
    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.versionupdate_log_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        versionUpdate = datalist.get(position);

        holder.versioncode.setText(versionUpdate.getVersionname());
        holder.appinfo.setText(versionUpdate.getAppinfo());
        holder.updateinfo.setText(versionUpdate.getUpdateinfo());
        holder.deleteinfo.setText(versionUpdate.getDeleteinfo());
        holder.updatetime.setText(versionUpdate.getUpdatetime());
        return convertView;
    }

    private  class ViewHolder{
        public TextView versioncode,appinfo,updateinfo,deleteinfo,updatetime;
        public ViewHolder(View view){
           versioncode= (TextView) view.findViewById(R.id.versioncode);
           appinfo= (TextView) view.findViewById(R.id.appinfo);
           updateinfo= (TextView) view.findViewById(R.id.updateinfo);
           deleteinfo= (TextView) view.findViewById(R.id.deleteinfo);
           updatetime= (TextView) view.findViewById(R.id.updatetime);
        }
    }
}
