package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordValueDao;
import com.energyfuture.symphony.m3.domain.TrNoiseRecordValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class NoiseAdapter extends BaseAdapter {
    private Context context;
    private List<TrNoiseRecordValue> trNoiseRecordValues;
    private SympMessageRealDao sympMessageRealDao;
    private TrNoiseRecordValueDao trNoiseRecordValueDao;

    public NoiseAdapter(Context context,List<TrNoiseRecordValue> trNoiseRecordValues){
        this.context = context;
        this.trNoiseRecordValues = trNoiseRecordValues;
        sympMessageRealDao = new SympMessageRealDao(context);
        trNoiseRecordValueDao = new TrNoiseRecordValueDao(context);
    }

    @Override
    public int getCount() {
        return trNoiseRecordValues.size();
    }

    @Override
    public Object getItem(int position) {
        return trNoiseRecordValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrNoiseRecordValue data = trNoiseRecordValues.get(position);
        final Myviewholder myviewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.noise_gridview_item, null);
            myviewholder=new Myviewholder(convertView);
            convertView.setTag(myviewholder);
        }else{
            myviewholder= (Myviewholder) convertView.getTag();
        }
        myviewholder.noise_sight.setText(data.getSight().equals("null") ? "":data.getSight());
        myviewholder.noise_height13.setText(data.getHeight13().equals("null") ? "":data.getHeight13());
        myviewholder.noise_height23.setText(data.getHeight23().equals("null") ? "":data.getHeight23());
        //设置监听事件
        myviewholder.noise_height13.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String height13 = myviewholder.noise_height13.getText().toString();
                    if(data.getId() != null && !data.getId().equals("")){ //id不为空,修改
                        if(!data.getHeight13().equals(height13)){
                            updateData("HEIGHT13",height13,data.getId());
                        }
                    }
                }
            }
        });
        myviewholder.noise_height23.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String height23 = myviewholder.noise_height23.getText().toString();
                    if(data.getId() != null && !data.getId().equals("")){ //id不为空,修改
                        if(!data.getHeight23().equals(height23)){
                            updateData("HEIGHT23",height23,data.getId());
                        }
                    }
                }
            }
        });
        return convertView;
    }

    //修改数据并且发送消息
    private void updateData(String column,String text,String id){
        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
        columnsMap.put(column,text);
        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
        wheresMap.put("ID", id);
        trNoiseRecordValueDao.updateTrNoiseRecordValueInfo(columnsMap,wheresMap);
        //发送消息
        TrNoiseRecordValue trNoiseRecordValue = new TrNoiseRecordValue();
        trNoiseRecordValue.setId(id);
        if(column.equals("HEIGHT13")){
            trNoiseRecordValue.setHeight13(text);
        }else if(column.equals("HEIGHT23")){
            trNoiseRecordValue.setHeight23(text);
        }
        List list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trNoiseRecordValue);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    class Myviewholder{
        TextView noise_sight;
        EditText noise_height13,noise_height23;
        Myviewholder(View v) {
            noise_sight = (TextView)v.findViewById(R.id.noise_sight);
            noise_height13 = (EditText)v.findViewById(R.id.noise_height13);
            noise_height23 = (EditText)v.findViewById(R.id.noise_height23);
        }
    }
}
