package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrBureau;

import java.util.List;

/**
 * 设备适配器
 */
public class BureauAdapter extends BaseAdapter{
    private Context context;
    public List<TrBureau> trBureauList;

    public BureauAdapter(Context context, List<TrBureau> trBureauList) {
        this.context = context;
        this.trBureauList = trBureauList;
    }

    @Override
    public int getCount() {
        return trBureauList == null ? 0 : trBureauList.size();
    }

    @Override
    public Object getItem(int position) {
        return trBureauList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrBureau trBureau = trBureauList.get(position);
        MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.popu_listview_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        myViewholder.popuItem.setText(trBureau.getBureauname());
        return convertView;
    }

    public class MyViewholder{
        TextView popuItem;

        public MyViewholder(View v) {
                popuItem= (TextView) v.findViewById(R.id.popu_item);
        }
    }
}
