package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialWorkDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
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
 * Created by Administrator on 2015/9/16.
 */
public class DetectiondapersonAdapter extends BaseAdapter{
    private List<String> personslist;
    private List<Boolean> personsBool;
    private OperationItemActivity context;
    private TrSpecialWork data;
    private Map<String,Boolean> personMap;
    private SympMessageRealDao sympMessageRealDao;
    private TrDetectiontypEtempletObj trDetectiontypEtempletObj;
    private String userid;
    private TrTask trTask;
    public DetectiondapersonAdapter(List<String> personslist,List<Boolean> personsBool, Context context,TrSpecialWork data,TrTask trTask,TrDetectiontypEtempletObj trDetectiontypEtempletObj) {
        this.personslist = personslist;
        this.personsBool = personsBool;
        this.context = (OperationItemActivity)context;
        this.data = data;
        this.trTask=trTask;
        personMap = new HashMap<String,Boolean>();
        this.trDetectiontypEtempletObj = trDetectiontypEtempletObj;
        userid = Constants.getLoginPerson(context).get("userId");
        sympMessageRealDao = new SympMessageRealDao(context);
    }

    @Override
    public int getCount() {
        return personslist.size();
    }

    @Override
    public Object getItem(int position) {
        return personslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final String name = personslist.get(position);
        final Boolean bool = personsBool.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.safe_list_person_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        List<String> namelist = Constants.getUserNameById(name, context);
        String personname;
        if(namelist.size()==0){
            personname="";
        }else{
            personname=namelist.get(0);
        }
        viewHolder.checkBox.setText(personname);
        viewHolder.checkBox.setChecked(bool);

        personMap.put(name, bool);
        Utils.cpersonMap.put(data.getId(),personMap);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String date = Constants.dateformat2.format(new Date());
                personMap.put(name,isChecked);
                Utils.cpersonMap.put(data.getId(),personMap);
                Map<Object,Object> columnsMap = new HashMap<Object,Object>();
                Map<Object,Object> wheresMap = new HashMap<Object,Object>();
                StringBuffer sbf = new StringBuffer();
                for(Map.Entry<String, Map<String,Boolean>> entry: Utils.cpersonMap.entrySet()){
                    if(entry.getKey().toString().equals(data.getId())){
                        for(Map.Entry<String,Boolean> entry1:entry.getValue().entrySet()){
                            if(entry1.getValue() == true){
                                sbf.append(entry1.getKey()).append(";");
                            }
                        }
                    }
                }
                if(!sbf.toString().equals("")){
                    columnsMap.put("DETECTIONDEPERSON",sbf.toString());
                }else{
                    columnsMap.put("DETECTIONDEPERSON","");
                }
                columnsMap.put("UPDATEPERSON",userid);
                columnsMap.put("UPDATETIME",date);
                wheresMap.put("ID",data.getId());
                TrSpecialWorkDao trSpecialWorkDao = new TrSpecialWorkDao(context);
                trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
                TrSpecialWork trSpecialWork = new TrSpecialWork();
                if(!sbf.toString().equals("")){
                    trSpecialWork.setDetectiondeperson(sbf.toString());
                }else{
                    trSpecialWork.setDetectiondeperson("");
                }

                trSpecialWork.setUpdateperson(userid);
                trSpecialWork.setUpdatetime(date);
                trSpecialWork.setId(data.getId());
                List<List<Object>> list1 = new ArrayList<List<Object>>();
                List<Object> list2 = new ArrayList<Object>();
                list2.add(trSpecialWork);
                list1.add(list2);
                sympMessageRealDao.updateTextMessages(list1);
                workStatusUpdate(trDetectiontypEtempletObj);
            }
        });
        return convertView;
    }

    //作业情况记录状态改变
    private void workStatusUpdate(TrDetectiontypEtempletObj trDetectiontypEtempletObj){
        boolean isshow = false;
        List<TrSpecialWork> workdatalist = getWorkrecord(trDetectiontypEtempletObj);
        for(TrSpecialWork trSpecialWork : workdatalist){
            Map<String,Boolean> personsmap=getDetectiondeperson(trSpecialWork);
            for(Map.Entry<String,Boolean> entry: personsmap.entrySet()){
                if(entry.getValue() == true){
                    isshow = true;
                }
            }
        }
        if(isshow){
            updateStatus(trDetectiontypEtempletObj, "1");
        }else{
            updateStatus(trDetectiontypEtempletObj,"");
        }
    }

    private void updateStatus(TrDetectiontypEtempletObj trDetectiontypEtempletObj,String status){
        Map<Object, Object> columnsMap = new HashMap<Object, Object>();
        columnsMap.put("STATUS", status);
        Map<Object, Object> wheresMap = new HashMap<Object, Object>();
        wheresMap.put("ID", trDetectiontypEtempletObj.getId());
        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);
        TrDetectiontypEtempletObj trDetectiontypEtempletObj1 = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj1.setStatus(status);
        trDetectiontypEtempletObj1.setId(trDetectiontypEtempletObj.getId());
        List list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trDetectiontypEtempletObj1);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        context.setStatus(trTask.getTaskid(),trDetectiontypEtempletObj.getPid());
    }

    //获取作业情况记录
    public List<TrSpecialWork> getWorkrecord(TrDetectiontypEtempletObj data){
        TrSpecialWorkDao workDao=new TrSpecialWorkDao(context);
        TrSpecialWork trSpecialWork=new TrSpecialWork();
        trSpecialWork.setDetectionprojectid(data.getId());
        List<TrSpecialWork> workdata = workDao.queryTrSpecialWorkList(trSpecialWork);
        return workdata;
    }

    private Map<String,Boolean> getDetectiondeperson(TrSpecialWork date) {  //截取人员的名字
        Map<String,Boolean> personslist = new HashMap<String,Boolean>();
        String taskPersons=getTaskPerson();
        String wordPerson=date.getDetectiondeperson();
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

    private String getTaskPerson() {  //从任务表中获取小组成员
        TrTaskDao taskDao=new TrTaskDao(context);
        TrTask trTaskParam=new TrTask();
        trTaskParam.setTaskid(trTask.getTaskid());
        List<TrTask> datalist = taskDao.queryTrTaskList(trTaskParam);
        if(datalist.size()>0){
            return datalist.get(0).getWorkperson();
        }
        return "";
    }

    class ViewHolder{
        CheckBox checkBox;
        TextView person;
        public ViewHolder(View itemView) {
            checkBox= (CheckBox) itemView.findViewById(R.id.confirm_checkbox);
            person= (TextView) itemView.findViewById(R.id.person_id);
            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                checkBox.setEnabled(false);
            }
        }
    }
}
