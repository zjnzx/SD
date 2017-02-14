package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrSpecialWorkDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrSpecialWork;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public class WorkListAdapter extends BaseAdapter{
    private OperationItemActivity context;
    private List<TrSpecialWork> workdatalist;
    String userId = "";
    private TrSpecialWorkDao trSpecialWorkDao;
    private SympMessageRealDao sympMessageRealDao;
    private String content; //作业情况采集卡的检查内容
    private String taskid,text1 = "";
    private TrDetectiontypEtempletObj trDetectiontypEtempletObj;
    private TrTask trTask;
    public WorkListAdapter(Context context, List<TrSpecialWork> workdatalist,TrDetectiontypEtempletObj trDetectiontypEtempletObj,TrTask trTask) {
        this.context = (OperationItemActivity) context;
        this.workdatalist = workdatalist;
        this.taskid=trTask.getTaskid();
        this.trTask=trTask;
        this.trDetectiontypEtempletObj = trDetectiontypEtempletObj;
        userId = Constants.getLoginPerson(context).get("userId");
        trSpecialWorkDao = new TrSpecialWorkDao(context);
        sympMessageRealDao = new SympMessageRealDao(context);
        Utils.condPersonMap.clear();
        Utils.cpersonMap.clear();
    }

    @Override
    public int getCount() {
        return workdatalist.size();
    }

    public void setName(String content){
        this.content=content;
    }

    @Override
    public Object getItem(int position) {
        return workdatalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final TrSpecialWork data = workdatalist.get(position);
        final Viewholder viewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.oper_cond_record_item,null);
            viewholder=new Viewholder(convertView);
            convertView.setTag(viewholder);
        }else{
            viewholder= (Viewholder) convertView.getTag();
        }
        viewholder.content.setText(content);

        String date = "";
        if(!data.getDetectiondate().equals("")){
            try{
                Long time=new Long(data.getDetectiondate());
                date = Constants.dateformat1.format(time);
            }catch (Exception e){
                date=data.getDetectiondate();
            }
        }

        viewholder.record_date.setText(date);
        //设置主键id
        viewholder.record_date.setTag(R.id.work_id, data.getId());
        viewholder.record_time.setText((data.getDetectiontime() == null || data.getDetectiontime().equals("null")) ? "" : data.getDetectiontime());
        viewholder.record_time.setTag(R.id.work_id, data.getId());
        viewholder.record_temp.setText(data.getTemp().equals("null") ? "" : data.getTemp());
        viewholder.record_cond.setText(data.getDetectioncondition().equals("null") ? "" : data.getDetectioncondition());
        viewholder.record_radiation.setText(data.getRadiation().equals("null") ? "" : data.getRadiation());
        viewholder.record_load.setText(data.getLoads().equals("null") ? "" : data.getLoads());
        viewholder.record_remark.setText(data.getRemarks().equals("null") ? " " : data.getRemarks());

        List<String> persons = new ArrayList<String>();
        List<Boolean> personsBool = new ArrayList<Boolean>();
        Map<String,Boolean> personsmap=getDetectiondeperson(data);
        for(Map.Entry<String,Boolean> entry: personsmap.entrySet()){
            persons.add(entry.getKey());
            personsBool.add(entry.getValue());
        }
        DetectiondapersonAdapter adapter = new DetectiondapersonAdapter(persons,personsBool,context,data,trTask,trDetectiontypEtempletObj);
        viewholder.gridView.setAdapter(adapter);

        final String temp = data.getTemp();
        final String condition = data.getDetectioncondition();
        final String radiation = data.getRadiation();
        final String loads = data.getLoads();
        final String remark = data.getRemarks();

        viewholder.record_date.setOnClickListener(new View.OnClickListener() {
            String id = viewholder.record_date.getTag(R.id.work_id).toString();
            @Override
            public void onClick(View v) {
                String[] data=new String[]{userId,id};
                context.overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(context);
                dataPicker.selectDateDialog(viewholder.record_date,3,data,null,1);
            }
        });

        viewholder.record_time.setOnClickListener(new View.OnClickListener() {
            String id = viewholder.record_time.getTag(R.id.work_id).toString();
            @Override
            public void onClick(View v) {
                String[] data=new String[]{userId,id};
                context.overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(context);
                dataPicker.selectTimeDialog(viewholder.record_time,data);
            }
        });

        //监听环境温度输入框
        viewholder.record_temp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateWorkInfo(viewholder.record_temp, temp, "TEMP", data.getId());
                }
            }
        });
        //监听检测条件输入框
        viewholder.record_cond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateWorkInfo(viewholder.record_cond,condition,"DETECTIONCONDITION",data.getId());
                }
            }
        });
        //监听辐射率输入框
        viewholder.record_radiation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateWorkInfo(viewholder.record_radiation,radiation,"RADIATION",data.getId());
                }
            }
        });
        //监听负荷输入框
        viewholder.record_load.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateWorkInfo(viewholder.record_load,loads,"LOADS",data.getId());
                }
            }
        });
        //监听备注输入框
        viewholder.record_remark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateWorkInfo(viewholder.record_remark,remark,"REMARKS",data.getId());
                }
            }
        });

        return convertView;
    }

    /**
     * 修改作业情况检测信息
     * @param editText
     * @param text
     * @param colum
     */
    private void updateWorkInfo(EditText editText,String text,String colum,String id){
        String date = Constants.dateformat2.format(new Date());
        String textNew =  editText.getText().toString();
        if(!textNew.equals("") && !text.equals(textNew) && !text1.equals(textNew)){
            text1 = textNew;
            Map<Object,Object> columnsMap = new HashMap<Object, Object>();
            columnsMap.put(colum,textNew);
            columnsMap.put("UPDATETIME",date);
            columnsMap.put("UPDATEPERSON",userId);
            Map<Object,Object> wheresMap = new HashMap<Object, Object>();
            wheresMap.put("ID",id);
            trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
            //发送消息
            SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
            TrSpecialWork trSpecialWork = new TrSpecialWork();
            if(colum.equals("TEMP")){
                trSpecialWork.setTemp(textNew);
            }else if(colum.equals("DETECTIONCONDITION")){
                trSpecialWork.setDetectioncondition(textNew);
            }else if(colum.equals("RADIATION")){
                trSpecialWork.setRadiation(textNew);
            }else if(colum.equals("LOADS")){
                trSpecialWork.setLoads(textNew);
            }else if(colum.equals("REMARKS")){
                trSpecialWork.setRemarks(textNew);
            }
            trSpecialWork.setUpdatetime(date);
            trSpecialWork.setUpdateperson(userId);
            trSpecialWork.setId(id);
            List<List<Object>> list1 = new ArrayList<List<Object>>();
            List<Object> list2 = new ArrayList<Object>();
            list2.add(trSpecialWork);
            list1.add(list2);
            sympMessageRealDao.updateTextMessages(list1);
        }
    }

    private Map<String,Boolean> getDetectiondeperson(TrSpecialWork date) {  //截取人员的名字
        Map<String,Boolean> personslist = new HashMap<String,Boolean>();
        String taskPersons=getTaskPerson();
        String wordPerson=workdatalist.get(0).getDetectiondeperson();
        if(!taskPersons.equals("")){
            String[] taskPer = taskPersons.split(";");
            String[] wordPer = wordPerson.split(";");
            for(int i = 0;i < taskPer.length;i++){
                personslist.put(taskPer[i],false);
                for(int j = 0; j < wordPer.length;j++){
                    if(taskPer[i].equals(wordPer[j])){
                        personslist.put(taskPer[i],true);
                    }
                }
            }
        }
        return personslist;
    }

//    private String getWordPerson(TrSpecialWork date){
//        List<TrSpecialWork> trSpecialWorks = trSpecialWorkDao.queryTrSpecialWorkList(date);
//        if(trSpecialWorks.size()>0){
//            return trSpecialWorks.get(0).getDetectiondeperson();
//        }
//        return "";
//    }

    private String getTaskPerson() {  //从任务表中获取小组成员
        TrTaskDao taskDao=new TrTaskDao(context);
        TrTask trTaskParam=new TrTask();
        trTaskParam.setTaskid(taskid);
        List<TrTask> datalist = taskDao.queryTrTaskList(trTaskParam);
        if(datalist.size()>0){
            return datalist.get(0).getWorkperson();
        }
        return "";
    }

    class Viewholder{
        EditText record_date,record_time,record_temp,record_cond,record_radiation,record_load;
        EditText record_remark;
        TextView content;
        GridView gridView;
        Viewholder(View v) {
            record_date= (EditText) v.findViewById(R.id.record_date);
            record_time= (EditText) v.findViewById(R.id.record_time);
            record_temp= (EditText) v.findViewById(R.id.record_cond_temp);
            record_cond= (EditText) v.findViewById(R.id.record_cond);
            record_radiation= (EditText) v.findViewById(R.id.record_radiation);
            record_load= (EditText) v.findViewById(R.id.record_load);
            gridView= (GridView) v.findViewById(R.id.record_person_gv);

            content= (TextView) v.findViewById(R.id.cond_item_content);

            record_remark= (EditText) v.findViewById(R.id.cond_item_remarkinput);

            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                record_date.setEnabled(false);
                record_time.setEnabled(false);
                record_temp.setEnabled(false);
                record_cond.setEnabled(false);
                record_radiation.setEnabled(false);
                record_load.setEnabled(false);
                record_remark.setEnabled(false);
            }
        }
    }
    private void updateRemarks(TrSpecialWork data, String remark){
        TrSpecialWorkDao workDao=new TrSpecialWorkDao(context);
        Map<Object,Object> columnsMap1=new HashMap<>();
        Map<Object,Object> wheresMap1=new HashMap<>();
        columnsMap1.put("REMARKS",remark);
        wheresMap1.put("ID",data.getId());
        workDao.updateTrSpecialWorkInfo(columnsMap1,wheresMap1);
    }
}
