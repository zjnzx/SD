package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrSafetytipsObj;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/9/8.
 */
public class RiskTitleAdpater extends BaseAdapter{
    private Context context;
    List<TrSafetytipsObj> trSafetytipsObjs;

    public RiskTitleAdpater(Context context, List<TrSafetytipsObj> trSafetytipsObjs){
        this.context = context;
        this.trSafetytipsObjs = trSafetytipsObjs;
    }

    @Override
    public int getCount() {
        return trSafetytipsObjs == null ? 0 : trSafetytipsObjs.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.risk_list_title_item,null) ;
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        TrSafetytipsObj obj = trSafetytipsObjs.get(position);
        String num = Constants.numToUpper(position + 1);
        viewHolder.risk_title.setText("风险" + num);
        viewHolder.risk_content1.setText((obj.getEndangername() == null || obj.getEndangername().equals("null")) ? "" : obj.getEndangername());
        viewHolder.risk_content2.setText((obj.getEndangerrang() == null || obj.getEndangerrang().equals("null")) ? "" : obj.getEndangerrang());
        viewHolder.risk_content3.setText((obj.getRiiskrang() == null || obj.getRiiskrang().equals("null")) ? "" : obj.getRiiskrang());
        viewHolder.risk_content4.setText((obj.getResult() == null || obj.getResult().equals("null")) ? "" : obj.getResult());
        viewHolder.risk_content5.setText((obj.getGrade() == null || obj.getGrade().equals("null")) ? "" : obj.getGrade());
        viewHolder.risk_content6.setText((obj.getPrecontrolmethod() == null || obj.getPrecontrolmethod().equals("null")) ? "" : obj.getPrecontrolmethod());
        viewHolder.risk_content7.setText((obj.getMethod() == null || obj.getMethod().equals("null")) ? "" : obj.getMethod());
        viewHolder.risk_content8.setText((obj.getContinuelevel() == null || obj.getContinuelevel().equals("null")) ? "" : obj.getContinuelevel());
        viewHolder.risk_content9.setText((obj.getRemarks() == null || obj.getRemarks().equals("null")) ? "" : obj.getRemarks());
        return convertView;
    }

    public class ViewHolder{
        public TextView risk_title,risk_content1,risk_content2,risk_content3,risk_content4,risk_content5,risk_content6,risk_content7,risk_content8,risk_content9;
        public ListView risk_content_listview;
        public ViewHolder(View view){
            risk_title = (TextView)view.findViewById(R.id.risk_title);
            risk_content1 = (TextView)view.findViewById(R.id.risk_content1);
            risk_content2 = (TextView)view.findViewById(R.id.risk_content2);
            risk_content3 = (TextView)view.findViewById(R.id.risk_content3);
            risk_content4 = (TextView)view.findViewById(R.id.risk_content4);
            risk_content5 = (TextView)view.findViewById(R.id.risk_content5);
            risk_content6 = (TextView)view.findViewById(R.id.risk_content6);
            risk_content7 = (TextView)view.findViewById(R.id.risk_content7);
            risk_content8 = (TextView)view.findViewById(R.id.risk_content8);
            risk_content9 = (TextView)view.findViewById(R.id.risk_content9);
//            risk_content_listview = (ListView)view.findViewById(R.id.risk_content_listview);
        }
    }
}
