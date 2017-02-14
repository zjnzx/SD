package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class IndexAdapter extends BaseExpandableListAdapter{
    private List<TrDetectiontypEtempletObj> data1;   //上层显示的数据(Group)
    private List<List<TrDetectiontypEtempletObj>> data2; //下层显示的数据(Child)
    private Context context;
    ExpandableListView select_condition;

    public IndexAdapter(List<TrDetectiontypEtempletObj> data1, List<List<TrDetectiontypEtempletObj>> data2, Context context,ExpandableListView select_condition) {
        this.data1 = data1;
        this.data2 = data2;
        this.context = context;
        this.select_condition=select_condition;
    }

    @Override
    public int getGroupCount() {
        return data1.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data2.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data1.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data2.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TrDetectiontypEtempletObj obj = data1.get(groupPosition);
        myViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.index_first_item,null);
            viewHolder=new myViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (myViewHolder) convertView.getTag();
        }
            viewHolder.text.setText(obj.getDetectionname()!=null?obj.getDetectionname():"");
        select_condition.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TrDetectiontypEtempletObj obj = data2.get(groupPosition).get(childPosition);
        myViewHolder viewHolder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.index_second_item,null);
            viewHolder=new myViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (myViewHolder) convertView.getTag();
        }
            viewHolder.text.setText(obj.getDetectionname()!=null?obj.getDetectionname():"");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class myViewHolder{
        ImageView image;
        TextView text;

        myViewHolder(View v) {
               image= (ImageView) v.findViewById(R.id.job_coutent_pic3);
               text= (TextView) v.findViewById(R.id.job_coutent_name);
        }
    }
}
