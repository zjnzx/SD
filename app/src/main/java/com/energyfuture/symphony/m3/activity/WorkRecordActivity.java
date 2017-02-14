package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrConditionInfoDao;
import com.energyfuture.symphony.m3.dao.TrEquipmentDao;
import com.energyfuture.symphony.m3.dao.TrProjectDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.TrConditionInfo;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkRecordActivity extends ActionBarActivity{
    private Context context = WorkRecordActivity.this;
    //信息栏
    private TextView task_name,substation_name,task_group,task_state,toolbar_news_show,submit,toolbar_name;
    private LinearLayout toolbar_back;
    private ImageView toolbar_head_image,task_type;
    private TrTask trTask;
    private FrameLayout frameLayout;
    private List<TrProject> projectlist;  //根据项目ID查出来的项目列表

    private Button register;
    private RadioButton weather1,weather2,weather3;
    private SwipeRefreshLayout work_swipe;
    private TextView index_button;
    private EditText temperature,humidity,time,  //温度，湿度,记录时间
            transformer_name,transformer_number,transformer_label,transformer_model,
            transformer_phase,transformer_HZ,transformer_voltage,transformer_capacity,
            transformer_conditions,transformer_oil,transformer_cooling,transformer_pressure,
            transformer_Factory,transformer_make,transformer_commissioning,transformer_remould;
    private String userId;
    private int flag; //从不同activity跳转时的标记
    private PopupWindow window;
    private RelativeLayout relativeLayout,toolbar_head;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locale_work_record);

        userId = Constants.getLoginPerson(context).get("userId");

        initTaskInfo(); //初始化任务信息

        inioView();

    }


    private void initTaskInfo() {
        trTask= (TrTask)getIntent().getSerializableExtra("trTask");
        flag=getIntent().getIntExtra("flag",-1);

        TrProject trProject=new TrProject();
        trProject.setProjectid(trTask.getProjectid());
        TrProjectDao trProjectDao=new TrProjectDao(context);
        projectlist= trProjectDao.queryTrProjectsList(trProject);

        CardView include= (CardView) findViewById(R.id.work_record_row_name);

        task_type= (ImageView) include.findViewById(R.id.task_receive_type);
        task_name= (TextView) include.findViewById(R.id.task_receive_name);
        substation_name= (TextView) include.findViewById(R.id.substation_name);
        task_group= (TextView) include.findViewById(R.id.tack_group);
        task_state= (TextView) include.findViewById(R.id.task_receive_state);

        toolbar_back = (LinearLayout)findViewById(R.id.toolbar_back);
        toolbar_head_image = (ImageView)findViewById(R.id.toolbar_head_image);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeLayout);
        toolbar_head= (RelativeLayout) findViewById(R.id.toolbar_head);
        layout= (LinearLayout) findViewById(R.id.background);
        /*toolbar_news_show = (TextView)findViewById(R.id.toolbar_news_show);*/
        submit = (TextView)findViewById(R.id.submit);
        index_button = (TextView)findViewById(R.id.index_button);
        index_button.setVisibility(View.GONE);
        toolbar_name = (TextView)findViewById(R.id.toolbar_name);
        toolbar_name.setText("现场作业情况登记");

        frameLayout= (FrameLayout) include.findViewById(R.id.receive_progressBar_FrameLayout);

        task_name.setText(trTask.getTaskname());
        substation_name.setText(Constants.getStationName(context, projectlist));
        task_group.setText(Constants.getGroupName(context,projectlist));
        Constants.setState(trTask.getTaskstate(),frameLayout,task_type,task_state);

        String useid = Constants.getLoginPerson(this).get("userId");
        if(!Constants.getUsericon(context).equals("")){
            String imagepath=Constants.getUsericon(context);
            toolbar_head_image.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(useid);
            toolbar_head_image.setImageResource(userhead);
        }
        /*toolbar_news_show.bringToFront();*/

        toolbar_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(window==null){
                    window=Constants.createPopupWindow(userId,WorkRecordActivity.this,layout);
                }
                if(!window.isShowing()){
                    window.showAtLocation(relativeLayout,Gravity.TOP|Gravity.RIGHT,20,140);
                    if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getVisibility()==View.VISIBLE){
                    layout.setVisibility(View.GONE);
                    window.dismiss();
                    window=null;
                }
            }
        });

        menuClick();

        final Map<String, Object> condMap = Constants.getRequestParam(context,"EQUIPMENT",null,trTask.getTaskid() + "@@" + trTask.getEquipmentid());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1 && msg.obj != null){
                    setView();
                    work_swipe.setRefreshing(false);
                }
            }
        };
        new DataSyschronized(context).getDataFromWeb(condMap, handler);

        work_swipe = (SwipeRefreshLayout)findViewById(R.id.work_swipe);
        work_swipe.setColorSchemeColors(getResources().getColor(R.color.theme_accent));
        work_swipe.setRefreshing(true);
        //下拉刷新
        work_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new DataSyschronized(context).getDataFromWeb(condMap, handler);
            }
        });
    }

    private void inioView() {
        weather1= (RadioButton) findViewById(R.id.work_record_rb1);
        weather2= (RadioButton) findViewById(R.id.work_record_rb2);
        weather3= (RadioButton) findViewById(R.id.work_record_rb3);

        temperature= (EditText) findViewById(R.id.temperature);
        humidity= (EditText) findViewById(R.id.humidity);
        time= (EditText) findViewById(R.id.time);

        time.setText(Constants.dateformat4.format(new Date()));

        transformer_name= (EditText) findViewById(R.id.transformer_name);
        transformer_number= (EditText) findViewById(R.id.transformer_number);
        transformer_label= (EditText) findViewById(R.id.transformer_label);
        transformer_model= (EditText) findViewById(R.id.transformer_model);
        transformer_phase= (EditText) findViewById(R.id.transformer_phase);
        transformer_HZ= (EditText) findViewById(R.id.transformer_HZ);
        transformer_voltage= (EditText) findViewById(R.id.transformer_voltage);
        transformer_capacity= (EditText) findViewById(R.id.transformer_capacity);
        transformer_conditions= (EditText) findViewById(R.id.transformer_conditions);
        transformer_oil= (EditText) findViewById(R.id.transformer_oil);
        transformer_cooling= (EditText) findViewById(R.id.transformer_cooling);
        transformer_pressure= (EditText) findViewById(R.id.transformer_pressure);
        transformer_Factory= (EditText) findViewById(R.id.transformer_Factory);
        transformer_make= (EditText) findViewById(R.id.transformer_make);
        transformer_commissioning= (EditText) findViewById(R.id.transformer_commissioning);
        transformer_remould= (EditText) findViewById(R.id.transformer_remould);

        register= (Button) findViewById(R.id.work_record_submit);


        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            register.setVisibility(View.GONE);
        }else{
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = SavetoConditionInfo();
                    UpdateEquipment();
                    if(b){
                        if(flag!=-1){
                            finish();
                        }else{
                            Intent intent = new Intent(context, JobContentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("trTask",trTask);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(WorkRecordActivity.this);
                dataPicker.selectDateTimeDialog(time,null,2);
            }
        });

        transformer_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(WorkRecordActivity.this);
                dataPicker.selectNoDayDateDialog(transformer_make,2,null,null,1);
            }
        });

        transformer_commissioning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(WorkRecordActivity.this);
                dataPicker.selectNoDayDateDialog(transformer_commissioning,2,null,null,1);
            }
        });

        transformer_remould.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                DatePicker dataPicker = new DatePicker(WorkRecordActivity.this);
                dataPicker.selectNoDayDateDialog(transformer_remould,2,null,null,1);
            }
        });

        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            temperature.setEnabled(false);
            humidity.setEnabled(false);
            time.setEnabled(false);

            transformer_name.setEnabled(false);
            transformer_number.setEnabled(false);
            transformer_label.setEnabled(false);
            transformer_model.setEnabled(false);
            transformer_phase.setEnabled(false);
            transformer_HZ.setEnabled(false);
            transformer_voltage.setEnabled(false);
            transformer_capacity.setEnabled(false);
            transformer_conditions.setEnabled(false);
            transformer_oil.setEnabled(false);
            transformer_cooling.setEnabled(false);
            transformer_pressure.setEnabled(false);
            transformer_Factory.setEnabled(false);
            transformer_make.setEnabled(false);
            transformer_commissioning.setEnabled(false);
            transformer_remould.setEnabled(false);
        }
    }

    private void setView() {
        if(flag!=-1){
            //查询天气信息
            TrConditionInfoDao conditionInfoDao=new TrConditionInfoDao(context);
            TrConditionInfo conditionInfo=new TrConditionInfo();   //查询的实体
            conditionInfo.setTaskid(trTask.getTaskid());
            List<TrConditionInfo> weatherlist = conditionInfoDao.queryTrConditionInfoList(conditionInfo);

            if(weatherlist.size()>0){
                TrConditionInfo weatherinfo = weatherlist.get(0);
                if(weatherinfo.getWeather().equals("晴天")){
                    weather1.setChecked(true);
                    if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                        weather2.setEnabled(false);
                        weather3.setEnabled(false);
                    }
                }
                if(weatherinfo.getWeather().equals("阴天")){
                    weather2.setChecked(true);
                    if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                        weather1.setEnabled(false);
                        weather3.setEnabled(false);
                    }
                }
                if(weatherinfo.getWeather().equals("雨天")){
                    weather3.setChecked(true);
                    if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                        weather1.setEnabled(false);
                        weather2.setEnabled(false);
                    }
                }

                String recorddate="";
                if(!weatherinfo.getRecorddate().equals("") && weatherinfo.getRecorddate()!= null && !weatherinfo.getRecorddate().equals("null")){
                    if(weatherinfo.getRecorddate().contains("-")){
                        recorddate = weatherinfo.getRecorddate();
                    }else{
                        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Long l1=new Long(weatherinfo.getRecorddate());
                        recorddate = dateFormat.format(l1);
                    }
                }
                temperature.setText(weatherinfo.getTemperature());
                humidity.setText(weatherinfo.getHumidity());
                time.setText(recorddate);
            }
        }

        //查询变压器信息
        TrEquipmentDao equipmentDao=new TrEquipmentDao(context);
        TrEquipment equipment=new TrEquipment();
        equipment.setEquipmentid(trTask.getEquipmentid());
        List<TrEquipment> equipmentlist=equipmentDao.queryTrEquipmentList(equipment);

        if(equipmentlist.size()>0){
            TrEquipment equipmentdata = equipmentlist.get(0);

            String productionDate = "",useDate = "",updateTime = "";
            if(!equipmentdata.getProductiondate().equals("") && equipmentdata.getProductiondate() != null && !equipmentdata.getProductiondate().equals("null")){
                if(!equipmentdata.getProductiondate().contains("-")){
                    Long l1=new Long(equipmentdata.getProductiondate());
                    productionDate = Constants.dateformat6.format(l1);
                }else{
                    productionDate = equipmentdata.getProductiondate();
                }
            }
            if(!equipmentdata.getUsedate().equals("") && equipmentdata.getUsedate() != null && !equipmentdata.getUsedate().equals("null")){
                if(!equipmentdata.getUsedate().contains("-")){
                    Long l1=new Long(equipmentdata.getUsedate());
                    useDate = Constants.dateformat6.format(l1);
                }else{
                    useDate = equipmentdata.getUsedate();
                }
            }
            if(!equipmentdata.getUpdatetime().equals("") && equipmentdata.getUpdatetime() != null && !equipmentdata.getUpdatetime().equals("null")){
                if(!equipmentdata.getUpdatetime().contains("-")){
                    Long l1=new Long(equipmentdata.getUpdatetime());
                    updateTime = Constants.dateformat6.format(l1);
                }else{
                    updateTime = equipmentdata.getUpdatetime();
                }
            }

            transformer_name.setText(equipmentdata.getEquipmentname());
            transformer_number.setText(equipmentdata.getCcbh());
            transformer_label.setText(equipmentdata.getConnectnumber());
            transformer_model.setText(equipmentdata.getModel());

            transformer_phase.setText(equipmentdata.getPhasecount());
            transformer_HZ.setText(equipmentdata.getFrequency());
            transformer_voltage.setText(equipmentdata.getVoltage());
            transformer_capacity.setText(equipmentdata.getRatedcapacity());

            transformer_conditions.setText(equipmentdata.getUsecondition());
            transformer_oil.setText(equipmentdata.getOil());
            transformer_cooling.setText(equipmentdata.getCoolmethod()==null ? "" : equipmentdata.getCoolmethod());
            transformer_pressure.setText(equipmentdata.getVoltagesetmodel());

            transformer_Factory.setText(equipmentdata.getCompany());
            transformer_make.setText(productionDate);
            transformer_commissioning.setText(useDate);
            transformer_remould.setText(updateTime);
        }
    }

    private boolean SavetoConditionInfo() {  //保存天气情况到数据库
        boolean flag = false;
        List<TrConditionInfo> list=new ArrayList<>();
        TrConditionInfoDao conditionInfoDao=new TrConditionInfoDao(context);

        TrConditionInfo conditionInfo=new TrConditionInfo();   //插入的实体
        String temp = temperature.getText().toString();
        String hum = humidity.getText().toString();
        String recorddate=time.getText().toString();
        String id = Constants.getUuid();
        conditionInfo.setRecordid(id);
        conditionInfo.setTaskid(trTask.getTaskid());
        conditionInfo.setPersonid(userId);
        conditionInfo.setRecorddate(recorddate);
        conditionInfo.setTemperature(temp);
        conditionInfo.setHumidity(hum);
        if(weather1.isChecked()){
            conditionInfo.setWeather("晴天");
        }else if(weather2.isChecked()){
            conditionInfo.setWeather("阴天");
        }else if(weather3.isChecked()){
            conditionInfo.setWeather("雨天");
        }else{
            conditionInfo.setWeather("");
        }
        conditionInfo.setStationid(projectlist.get(0).getStationid());

        list.add(conditionInfo);

        TrConditionInfo conditionInfo1=new TrConditionInfo();   //查询的实体
        conditionInfo1.setTaskid(trTask.getTaskid());
        List<TrConditionInfo> listdata = conditionInfoDao.queryTrConditionInfoList(conditionInfo1);

        if(!temp.equals("") && !hum.equals("")){
            if(listdata.size()>0){
                Map<Object,Object> columnsMap=new HashMap<>();
                columnsMap.put("WEATHER",conditionInfo.getWeather());
                columnsMap.put("TEMPERATURE",conditionInfo.getTemperature());
                columnsMap.put("HUMIDITY",conditionInfo.getHumidity());
                columnsMap.put("RECORDDATE",conditionInfo.getRecorddate());

                Map<Object,Object> wheresMap=new HashMap<>();
                wheresMap.put("TASKID",trTask.getTaskid());

                conditionInfoDao.updateTrConditionInfo(columnsMap,wheresMap);
                conditionInfo.setRecordid(listdata.get(0).getRecordid());
                sendMessage1(conditionInfo, 1);
            }else{
                conditionInfoDao.insertListData(list);
                sendMessage1(conditionInfo, 2);
            }
            flag = true;
        }else{
            Toast.makeText(context,"请填写天气信息",Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    /**
     * 封装新增天气信息的消息
     */
    private void sendMessage1(TrConditionInfo conditionInfo,int flag){
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(this);
        TrConditionInfo trConditionInfo = new TrConditionInfo();
        trConditionInfo.setRecordid(conditionInfo.getRecordid());
        trConditionInfo.setTaskid(trTask.getTaskid());
        trConditionInfo.setPersonid(userId);
        trConditionInfo.setRecorddate(conditionInfo.getRecorddate());
        trConditionInfo.setTemperature(conditionInfo.getTemperature());
        trConditionInfo.setHumidity(conditionInfo.getHumidity());
        trConditionInfo.setWeather(conditionInfo.getWeather());
        trConditionInfo.setStationid(conditionInfo.getStationid());
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trConditionInfo);
        list1.add(list2);
        if(flag == 1){
            sympMessageRealDao.updateTextMessages(list1);
        }else if(flag == 2) {
            sympMessageRealDao.addTextMessages(list1);
        }
    }

    private void UpdateEquipment() { //修改变压器信息
        TrEquipmentDao equipmentDao=new TrEquipmentDao(context);

        Map<Object,Object> columnsMap=new HashMap<>();
        Map<Object,Object> wheresMap=new HashMap<>();

        columnsMap.put("Equipmentname",transformer_name.getText().toString());
        columnsMap.put("CCBH",transformer_number.getText().toString());
        columnsMap.put("CONNECTNUMBER",transformer_label.getText().toString());
        columnsMap.put("MODEL", transformer_model.getText().toString());

        columnsMap.put("PHASECOUNt",transformer_phase.getText().toString().toString());
        columnsMap.put("FREQUENCY",transformer_HZ.getText().toString());
        columnsMap.put("VOLTAGE",transformer_voltage.getText().toString());
        columnsMap.put("RATEDCAPACITY",transformer_capacity.getText().toString());

        columnsMap.put("USECONDITION",transformer_conditions.getText().toString());
        columnsMap.put("OIL",transformer_oil.getText().toString());
        columnsMap.put("COOLMETHOD",transformer_cooling.getText().toString());
        columnsMap.put("VOLTAGESETMODEL",transformer_pressure.getText().toString());

        columnsMap.put("COMPANY",transformer_Factory.getText().toString());
        columnsMap.put("PRODUCTIONDATE",transformer_make.getText().toString());
        columnsMap.put("USEDATE",transformer_commissioning.getText().toString());
        columnsMap.put("UPDATETIME",transformer_remould.getText().toString());

        wheresMap.put("EQUIPMENTID",trTask.getEquipmentid());

        equipmentDao.updateTrEquipmentList(columnsMap,wheresMap);

        sendMessage2();
    }

    /**
     * 封装变压器信息的消息
     */
    private void sendMessage2(){
        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
        TrEquipment trEquipment = new TrEquipment();

        trEquipment.setEquipmentname(transformer_name.getText().toString());
        trEquipment.setCcbh(transformer_number.getText().toString());
        trEquipment.setModel(transformer_model.getText().toString());
        trEquipment.setPhasecount(transformer_phase.getText().toString());
        trEquipment.setRatedcapacity(transformer_capacity.getText().toString());
        trEquipment.setCoolmethod(transformer_cooling.getText().toString());
        trEquipment.setVoltagesetmodel(transformer_pressure.getText().toString());
        trEquipment.setCompany(transformer_Factory.getText().toString());
        trEquipment.setProductiondate(transformer_make.getText().toString());
        trEquipment.setUsedate(transformer_commissioning.getText().toString());
        trEquipment.setUpdatetime(transformer_remould.getText().toString());
        trEquipment.setEquipmentid(trTask.getEquipmentid());

        trEquipment.setOil(transformer_oil.getText().toString());
        trEquipment.setUsecondition(transformer_conditions.getText().toString());
        trEquipment.setVoltage(transformer_voltage.getText().toString());
        trEquipment.setFrequency(transformer_HZ.getText().toString());
        trEquipment.setConnectnumber(transformer_label.getText().toString());

        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trEquipment);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    private void menuClick() {
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }
}
