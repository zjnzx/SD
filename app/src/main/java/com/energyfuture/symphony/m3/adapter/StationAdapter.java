package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrStation;

import java.util.List;

/**
 * 变电站适配器
 */
public class StationAdapter extends BaseAdapter{
    private Context context;
    public List<TrStation> trStationList;

    public StationAdapter(Context context, List<TrStation> trStationList) {
        this.context = context;
        this.trStationList = trStationList;
    }

    @Override
    public int getCount() {
        return trStationList == null ? 0 : trStationList.size();
    }

    @Override
    public Object getItem(int position) {
        return trStationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrStation trStation = trStationList.get(position);
        MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.popu_listview_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        myViewholder.popuItem.setText(trStation.getStationname());
        return convertView;
    }

    public class MyViewholder{
        TextView popuItem;

        public MyViewholder(View v) {
                popuItem= (TextView) v.findViewById(R.id.popu_item);
        }
    }
}
