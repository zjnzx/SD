package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.TrEquipmentToolsDao;
import com.energyfuture.symphony.m3.domain.TrDetectionTools;
import com.energyfuture.symphony.m3.domain.TrEquipmentTools;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 */
public class TaskToolAdapter extends BaseAdapter{
    private Context context;
    private List<TrDetectionTools> toolslist;
    private String taskid;
    private TrTask trTask;

    public TaskToolAdapter(Context context, List<TrDetectionTools> toolslist,TrTask trTask) {
        this.context = context;
        this.toolslist = toolslist;
        this.trTask=trTask;
    }

    public void setTaskid(String taskid){
        this.taskid=taskid;

    }

    @Override
    public int getCount() {
        return toolslist == null ? 0 : toolslist.size();
    }

    @Override
    public Object getItem(int position) {
        return toolslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrDetectionTools tool = toolslist.get(position);
        MyViewholder myViewholder;

        if(convertView==null){
            if(tool.getToolstype().equals("instrument")){
                convertView= LayoutInflater.from(context).inflate(R.layout.instrument_list_item,null);
                myViewholder=new MyViewholder(convertView,1);
                convertView.setTag(myViewholder);
            }else{
                convertView= LayoutInflater.from(context).inflate(R.layout.tool_list_item,null);
                myViewholder=new MyViewholder(convertView,2);
                convertView.setTag(myViewholder);
            }
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }

        if(tool.getToolstype().equals("instrument")){
            String date = "";
            if(tool.getValiddate().contains("-")){
                date = tool.getValiddate().substring(0,tool.getValiddate().indexOf(" "));
            }else{
                Long time=new Long(tool.getValiddate());
                date = Constants.dateformat1.format(time);
            }

            myViewholder.instrumentid.setText(tool.getId());
            myViewholder.instrument_name.setText(tool.getEquipmentname());
            myViewholder.instrument_model.setText((tool.getModel() == null || tool.getModel().equals("null")) ? "" : tool.getModel());
            myViewholder.instrument_organization.setText(tool.getCorrecting());
            myViewholder.instrument_validity.setText(date);
            myViewholder.instrument_unit.setText("("+tool.getUnit()+")");
            if(taskid!=null){
                myViewholder.instrument_count.setText(getToolCount(tool.getId())==null?""+0:getToolCount(tool.getId()));
            }else{
                myViewholder.instrument_count.setText(""+0);
            }

            myViewholder.instrument_count_sub.setOnClickListener(new myClickListener(myViewholder));
            myViewholder.instrument_count_add.setOnClickListener(new myClickListener(myViewholder));

            final MyViewholder myViewholder1 = myViewholder;
            myViewholder.instrument_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        String instrumentid = myViewholder1.instrumentid.getText().toString();
                        int instrumentcount=Integer.parseInt(myViewholder1.instrument_count.getText().toString().equals("")?"0":myViewholder1.instrument_count.getText().toString());

                        myViewholder1.instrument_count.setText(String.valueOf(instrumentcount));
                        Utils.countMap.put(instrumentid,(String.valueOf(instrumentcount)));
                    }
                }
            });
        }else{
            myViewholder.toolid.setText(tool.getId());
            myViewholder.tool_name.setText(tool.getEquipmentname());
            myViewholder.tool_model.setText((tool.getModel() == null || tool.getModel().equals("null")) ? "" : tool.getModel());
            myViewholder.tool_unit.setText("("+tool.getUnit()+")");
            if(taskid!=null){
                myViewholder.tool_count.setText(getToolCount(tool.getId())==null?""+0:getToolCount(tool.getId()));
            }else{
                myViewholder.tool_count.setText(""+0);
            }


            myViewholder.tool_count_sub.setOnClickListener(new myClickListener(myViewholder));
            myViewholder.tool_count_add.setOnClickListener(new myClickListener(myViewholder));

            final MyViewholder myViewholder1 = myViewholder;
            myViewholder.tool_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        String toolid = myViewholder1.toolid.getText().toString();
                        int toolcount=Integer.parseInt(myViewholder1.tool_count.getText().toString().equals("")?"0":myViewholder1.tool_count.getText().toString());

                        myViewholder1.tool_count.setText(String.valueOf(toolcount));
                        Utils.countMap.put(toolid,(String.valueOf(toolcount)));
                    }
                }
            });
        }

        return convertView;
    }

    public class myClickListener implements View.OnClickListener{
        MyViewholder myViewholder;

        public myClickListener(MyViewholder myViewholder) {
            this.myViewholder = myViewholder;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.instrument_count_sub:
                    int count1=Integer.parseInt(myViewholder.instrument_count.getText().toString());
                    String instrumentid = myViewholder.instrumentid.getText().toString();
                    if(count1==0){
                        myViewholder.instrument_count.setText(0+"");
                    }else{
                        Utils.countMap.put(instrumentid,(String.valueOf(count1-1)));
                        myViewholder.instrument_count.setText((count1-1)+"");
                       /* if(Integer.parseInt(myViewholder.instrument_count.getText().toString()) == 0){
                            Utils.countMap.remove(instrumentid);
                        }*/
                    }
                    break;
                case R.id.instrument_count_add:
                    String instrumentid1 = myViewholder.instrumentid.getText().toString();
                    int count2=Integer.parseInt(myViewholder.instrument_count.getText().toString());
                    Utils.countMap.put(instrumentid1,(String.valueOf(count2+1)));
                    myViewholder.instrument_count.setText((count2+1)+"");
                    break;
                case R.id.tool_count_sub:
                    int count3=Integer.parseInt(myViewholder.tool_count.getText().toString());
                    String toolid = myViewholder.toolid.getText().toString();
                    if(count3==0){
                        myViewholder.tool_count.setText(0+"");
                    }else{
                        Utils.countMap.put(toolid,(String.valueOf(count3-1)));
                        myViewholder.tool_count.setText((count3-1)+"");
                        /*if(Integer.parseInt(myViewholder.tool_count.getText().toString()) == 0){
                            Utils.countMap.remove(toolid);
                        }*/
                    }
                    break;
                case R.id.tool_count_add:
                    String toolid1 = myViewholder.toolid.getText().toString();
                    int count4=Integer.parseInt(myViewholder.tool_count.getText().toString());
                    Utils.countMap.put(toolid1,(String.valueOf(count4+1)));
                    myViewholder.tool_count.setText((count4+1)+"");
                    break;
            }
        }
    }

    public class MyViewholder{
        //仪器
        TextView  instrument_name,instrument_model,instrument_organization,instrument_validity,instrumentid;
        ImageView instrument_count_sub,instrument_count_add;
        EditText instrument_count;

        //材料（工具）
        TextView  tool_name,tool_model,tool_unit,instrument_unit,toolid;
        ImageView tool_count_sub,tool_count_add;
        EditText tool_count;

        public MyViewholder(View v,int type) {
            if(type==1){
                instrument_name= (TextView) v.findViewById(R.id.instrument_name);
                instrument_model= (TextView) v.findViewById(R.id.instrument_model);
                instrument_organization= (TextView) v.findViewById(R.id.instrument_organization);
                instrument_validity= (TextView) v.findViewById(R.id.instrument_validity);
                instrument_count= (EditText) v.findViewById(R.id.instrument_count);
                instrument_count_sub= (ImageView) v.findViewById(R.id.instrument_count_sub);
                instrument_count_add= (ImageView) v.findViewById(R.id.instrument_count_add);
                instrument_unit= (TextView) v.findViewById(R.id.instrument_unit);
                instrumentid = (TextView) v.findViewById(R.id.instrumentid);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    instrument_count_sub.setEnabled(false);
                    instrument_count_add.setEnabled(false);
                    instrument_count.setEnabled(false);

                    instrument_count_sub.setVisibility(View.GONE);
                    instrument_count_add.setVisibility(View.GONE);
                }
            }else{
                tool_name= (TextView) v.findViewById(R.id.tool_name);
                tool_model= (TextView) v.findViewById(R.id.tool_model);
                tool_unit= (TextView) v.findViewById(R.id.tool_unit);
                tool_count= (EditText) v.findViewById(R.id.tool_count);
                tool_count_sub= (ImageView) v.findViewById(R.id.tool_count_sub);
                tool_count_add= (ImageView) v.findViewById(R.id.tool_count_add);
                toolid = (TextView) v.findViewById(R.id.toolid);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    tool_count_sub.setEnabled(false);
                    tool_count_add.setEnabled(false);
                    tool_count.setEnabled(false);

                    tool_count_sub.setVisibility(View.GONE);
                    tool_count_add.setVisibility(View.GONE);
                }
            }
        }
    }
    private String getToolCount(String toolid){ //获取工具的数量
        TrEquipmentTools trEquipmentTools=new TrEquipmentTools();
        trEquipmentTools.setTaskid(taskid);
        trEquipmentTools.setToolid(toolid);
        TrEquipmentToolsDao trEquipmentToolsDao=new TrEquipmentToolsDao(context);
        List<TrEquipmentTools> equipmentToolsList = trEquipmentToolsDao.queryTrEquipmentToolsList(trEquipmentTools);
        if(equipmentToolsList.size()!=0){
            return equipmentToolsList.get(0).getAmount();
        }
        return null;
    }
}
