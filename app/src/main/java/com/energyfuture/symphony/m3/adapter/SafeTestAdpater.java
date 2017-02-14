package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
public class SafeTestAdpater extends BaseAdapter{
    private Context context;
    List<String> list;

    public SafeTestAdpater(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.safety_list_item,null) ;
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.safety_order.setText((position+1)+".");
        viewHolder.safety_content.setText(list.get(position)+"。");
        return convertView;
    }

    public class ViewHolder{
        public TextView safety_order,safety_content;
        public ViewHolder(View view){
            safety_order = (TextView)view.findViewById(R.id.safety_order);
            safety_content = (TextView)view.findViewById(R.id.safety_content);
        }
    }
}
