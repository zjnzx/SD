package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrSafetytipsObj;

import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
public class SafeConfirmAdapter extends BaseAdapter{
    private Context context;
    private List<TrSafetytipsObj> list;

    public SafeConfirmAdapter(Context context, List<TrSafetytipsObj> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrSafetytipsObj obj = list.get(position);
        MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.safety_list_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        myViewholder.safety_order.setText((position+1)+".");
        myViewholder.safety_content.setText(obj.getEndangerrang());
        return convertView;
    }



    public class MyViewholder{
        public TextView safety_order,safety_content;
        MyViewholder(View v) {
            safety_order= (TextView) v.findViewById(R.id.safety_order);
            safety_content= (TextView) v.findViewById(R.id.safety_content);
        }
    }
}
