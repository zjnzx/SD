package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/9/11.
 * 一级巡检任务适配器
 */
public class JobContentAdapter extends BaseAdapter{
    private Context context;
    private List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjList;
    private TrTask trTask;

    public JobContentAdapter(Context context, List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjList,TrTask trTask) {
        this.context = context;
        this.trDetectiontypEtempletObjList = trDetectiontypEtempletObjList;
        this.trTask = trTask;
    }

    @Override
    public int getCount() {
        return trDetectiontypEtempletObjList == null ? 0 : trDetectiontypEtempletObjList.size();
    }

    @Override
    public Object getItem(int position) {
        return trDetectiontypEtempletObjList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrDetectiontypEtempletObj trDetectiontypEtempletObj = trDetectiontypEtempletObjList.get(position);
        Myviewholdr myviewholdr;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.job_content_first_item,null);
            myviewholdr=new Myviewholdr(convertView);
            convertView.setTag(myviewholdr);
        }else{
            myviewholdr= (Myviewholdr) convertView.getTag();
        }

        if(!trDetectiontypEtempletObj.getTemplettype().equals("safe") && !trDetectiontypEtempletObj.getTemplettype().equals("tool") && !trDetectiontypEtempletObj.getTemplettype().equals("work")){
            if(trDetectiontypEtempletObj.getStatus().equals("")){
                myviewholdr.icon.setBackgroundResource(R.drawable.gray_round);
            }else if(trDetectiontypEtempletObj.getStatus().equals("1")){
                myviewholdr.icon.setBackgroundResource(R.drawable.green_round);
            }else if(trDetectiontypEtempletObj.getStatus().equals("0")){
                myviewholdr.icon.setBackgroundResource(R.drawable.orange_round);
            }
        }

        myviewholdr.name.setText(trDetectiontypEtempletObj.getDetectionname());
        myviewholdr.image.setImageResource(R.drawable.ic_keyboard_arrow_right_grey600_24dp);

        return convertView;
    }

    private List<TrDetectiontypEtempletObj> queryTrDetectiontypEtempletObj(String rid){
        List<TrDetectiontypEtempletObj> trDetectiontypEtempletObjs = new ArrayList<TrDetectiontypEtempletObj>();
        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj.setRid(rid);
        trDetectiontypEtempletObj.setTaskid(trTask.getTaskid());
        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        trDetectiontypEtempletObjs = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj);
        return trDetectiontypEtempletObjs;
    }

    class Myviewholdr{
        TextView name;
        ImageView image,icon;
        Myviewholdr(View v) {
            name= (TextView) v.findViewById(R.id.job_coutent_name);
            image= (ImageView) v.findViewById(R.id.job_coutent_pic2);
            icon = (ImageView)v.findViewById(R.id.job_coutent_pic3);
        }
    }
}
