package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrSystemcode;

import java.util.List;

/**
 * 设备适配器
 */
public class VoltageAdapter extends BaseAdapter{
    private Context context;
    public List<TrSystemcode> trSystemcodeList;

    public VoltageAdapter(Context context, List<TrSystemcode> trSystemcodeList) {
        this.context = context;
        this.trSystemcodeList = trSystemcodeList;
    }

    @Override
    public int getCount() {
        return trSystemcodeList == null ? 0 : trSystemcodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return trSystemcodeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrSystemcode trSystemcode = trSystemcodeList.get(position);
        MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.popu_listview_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        myViewholder.popuItem.setText(trSystemcode.getCodevalue() + "kV");
        return convertView;
    }

    public class MyViewholder{
        TextView popuItem;

        public MyViewholder(View v) {
                popuItem= (TextView) v.findViewById(R.id.popu_item);
        }
    }
}
