package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordGroupDao;
import com.energyfuture.symphony.m3.domain.TrNoiseRecordGroup;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MyNoiseDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/9.
 */
public class NoiseGroupAdapter extends BaseAdapter {
    private Context context;
    private List<TrNoiseRecordGroup> trNoiseRecordGroups;
    private SympMessageRealDao sympMessageRealDao;
    private TrNoiseRecordGroupDao trNoiseRecordGroupDao;
    private TrTask trTask;
    private int flag;
    public NoiseGroupAdapter(Context context, List<TrNoiseRecordGroup> trNoiseRecordGroups,TrTask trTask,int flag){
        this.context = context;
        this.trNoiseRecordGroups = trNoiseRecordGroups;
        this.trTask=trTask;
        sympMessageRealDao = new SympMessageRealDao(context);
        trNoiseRecordGroupDao = new TrNoiseRecordGroupDao(context);
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return trNoiseRecordGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return trNoiseRecordGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrNoiseRecordGroup data = trNoiseRecordGroups.get(position);
        final Myviewholder myviewholder;
        final String groupName;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.noise_group_item, null);
            myviewholder=new Myviewholder(convertView);
            convertView.setTag(myviewholder);
        }else{
            myviewholder= (Myviewholder) convertView.getTag();
        }

        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            groupName= data.getGroupname().equals("null") ? " ":data.getGroupname();
        }else{
            groupName = data.getGroupname().equals("null") ? "":data.getGroupname();
        }

        myviewholder.noise_distance.setText(groupName);
        //设置监听事件
        myviewholder.noise_distance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String noise_distance = myviewholder.noise_distance.getText().toString();
                    if(data.getId() != null & !data.getId().equals("")){ //id不为空,修改
                        if(!data.getGroupname().equals(noise_distance)){
                            updateData(noise_distance,data);
                        }
                    }
                }
            }
        });
        myviewholder.noise_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNoiseDialog myNoiseDialog = new MyNoiseDialog(context,myviewholder.noise_distance.getText().toString(),data);
                myNoiseDialog.show();
            }
        });

        if(flag == 0){
            myviewholder.noise_distance.setEnabled(false);
            myviewholder.noise_layout.setEnabled(false);
        }
        return convertView;
    }

    //修改数据并且发送消息
    private void updateData(String text,TrNoiseRecordGroup data){
        Map<Object, Object> columnsMap = new HashMap<Object, Object>();
        columnsMap.put("GROUPNAME", text);
        Map<Object, Object> wheresMap = new HashMap<Object, Object>();
        wheresMap.put("ID", data.getId());
        trNoiseRecordGroupDao.updateTrNoiseRecordGroupInfo(columnsMap, wheresMap);
        //发送消息
        TrNoiseRecordGroup trNoiseRecordGroup = new TrNoiseRecordGroup();
        trNoiseRecordGroup.setId(data.getId());
        trNoiseRecordGroup.setGroupname(text);
        List list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trNoiseRecordGroup);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    class Myviewholder{
        EditText noise_distance;
        TextView noise_text;
        LinearLayout noise_layout;
        Myviewholder(View v) {
            noise_distance = (EditText)v.findViewById(R.id.noise_distance);
            noise_text = (TextView)v.findViewById(R.id.noise_text);
            noise_layout = (LinearLayout)v.findViewById(R.id.noise_layout);
            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                noise_distance.setEnabled(false);
            }
        }
    }
}
