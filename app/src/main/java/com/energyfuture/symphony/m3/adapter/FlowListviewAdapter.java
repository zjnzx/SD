package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.SympFlowInfo;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class FlowListviewAdapter extends BaseAdapter {

    private List<SympFlowInfo> listflow;
    private Context context;

    public FlowListviewAdapter(Context context, List<SympFlowInfo> listflow) {
        this.listflow = listflow;
        this.context = context;
    }
    public void onDateChange(List<SympFlowInfo> listflow) {
        this.listflow = listflow;
        this.notifyDataSetChanged();
    }
    public void clearApplications() {
        int size = this.listflow.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listflow.remove(0);
            }

//            this.notify
        }
    }
    public void addApplications(List<SympFlowInfo> appliction) {

        listflow.addAll(appliction);
//        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public int getCount() {
        return listflow.size();
    }

    @Override
    public Object getItem(int position) {
        return listflow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.flow_manage_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtUserid = (TextView) convertView.findViewById(R.id.txt_user_id);
            viewHolder.txtFlowtype = (TextView) convertView.findViewById(R.id.txt_flow_type);
            viewHolder.txtUsetype = (TextView) convertView.findViewById(R.id.txt_use_type);
            viewHolder.txtConnecttype = (TextView) convertView.findViewById(R.id.txt_connect_type);
            viewHolder.txtUsequantity = (TextView) convertView.findViewById(R.id.txt_use_quantity);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_flow_time);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        String uid = listflow.get(position).getUserid();

        List<String> listName= Constants.getUserNameById(uid, context);

        String userName = listName.get(0);
        viewHolder.txtUserid.setText(userName);

        String type = listflow.get(position).getFlowtype();

        viewHolder.txtFlowtype.setText(type);

        String usetype = listflow.get(position).getUsetype();

        viewHolder.txtUsetype.setText(usetype);


        String connecttype = listflow.get(position).getConnecttype();

        viewHolder.txtConnecttype.setText(connecttype);

        viewHolder.txtUsequantity.setText(listflow.get(position).getUsequantity());
        viewHolder.txtTime.setText(listflow.get(position).getRecordtime());

        return convertView;
    }

    static class ViewHolder {
        //用户ID
        TextView txtUserid;
        //流量类型
        TextView txtFlowtype;
        //使用类型
        TextView txtUsetype;
        //连接类型
        TextView txtConnecttype;
        //使用量
        TextView txtUsequantity;
        //时间
        TextView txtTime;

    }

}
