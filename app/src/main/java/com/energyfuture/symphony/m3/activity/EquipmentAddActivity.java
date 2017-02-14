package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrBureauDao;
import com.energyfuture.symphony.m3.dao.TrEquipmentDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentAddActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context = EquipmentAddActivity.this;
    private String stationid;
    private EditText productiondate,usedate,equipmentname,ccbh,connectnumber,model,phasecount,frequency,
            voltage,ratedcapacity,usecondition,oil,coolmethod,voltagesetmodel,company,updatetime;
    private LinearLayout menu_back,menu_check;
    private TrEquipmentDao trEquipmentDao = new TrEquipmentDao(context);
    private TrStationDao trStationDao = new TrStationDao(context);
    private TrBureauDao trBureauDao = new TrBureauDao(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_add);

        initView();
    }

    private void initView() {
        stationid = getIntent().getStringExtra("stationId");
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_check = (LinearLayout)findViewById(R.id.menu_check);
        productiondate = (EditText)findViewById(R.id.productiondate);
        updatetime = (EditText)findViewById(R.id.updatetime);
        usedate = (EditText)findViewById(R.id.usedate);
        equipmentname = (EditText)findViewById(R.id.equipmentname);
        ccbh = (EditText)findViewById(R.id.ccbh);
        connectnumber = (EditText)findViewById(R.id.connectnumber);
        model = (EditText)findViewById(R.id.model);
        phasecount = (EditText)findViewById(R.id.phasecount);
        frequency = (EditText)findViewById(R.id.frequency);
        voltage = (EditText)findViewById(R.id.voltage);
        ratedcapacity = (EditText)findViewById(R.id.ratedcapacity);
        usecondition = (EditText)findViewById(R.id.usecondition);
        oil = (EditText)findViewById(R.id.oil);
        coolmethod = (EditText)findViewById(R.id.coolmethod);
        voltagesetmodel = (EditText)findViewById(R.id.voltagesetmodel);
        company = (EditText)findViewById(R.id.company);

        menu_back.setOnClickListener(this);
        menu_check.setOnClickListener(this);
        productiondate.setOnClickListener(this);
        updatetime.setOnClickListener(this);
        usedate.setOnClickListener(this);
    }


    //判断EditText是否为空
    private boolean isEditTextNull(String string,EditText editText){
        boolean isNull = false;
        if ("".equals(string)) {
            Toast.makeText(getBaseContext(), "请输入" + editText.getTag() + "！", Toast.LENGTH_SHORT).show();
            if(editText.getTag().equals("制造日期") || editText.getTag().equals("投运日期") || editText.getTag().equals("改造日期")){
                DatePicker dataPicke = new DatePicker(context);
                dataPicke.selectDateDialog(editText,2,null,null,1);
            }else{
                editText.setEnabled(true);
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
            }
            isNull = true;
        }
        return isNull;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.productiondate:
                DatePicker dataPickerProductiondate = new DatePicker(context);
                dataPickerProductiondate.selectNoDayDateDialog(productiondate,2,null,null,1);
            break;
            case R.id.usedate:
                DatePicker dataPickeUsedate = new DatePicker(context);
                dataPickeUsedate.selectNoDayDateDialog(usedate,2,null,null,1);
            break;
            case R.id.updatetime:
                DatePicker dataPickeupdate = new DatePicker(context);
                dataPickeupdate.selectNoDayDateDialog(updatetime,2,null,null,1);
            break;
            case R.id.menu_back:
                setResult(RESULT_CANCELED);
                finish();
            break;
            case R.id.menu_check:
                //判断EditText是否有值
                String equipmentnameString = equipmentname.getText().toString().trim();
                if(isEditTextNull(equipmentnameString,equipmentname) == true){
                    return;
                }
                String ccbhString = ccbh.getText().toString().trim();
                if(isEditTextNull(ccbhString,ccbh) == true){
                    return;
                }
                String connectnumberString = connectnumber.getText().toString().trim();
                if(isEditTextNull(connectnumberString,connectnumber) == true){
                    return;
                }
                String modelString = model.getText().toString().trim();
                if(isEditTextNull(modelString,model) == true){
                    return;
                }
                String phasecountString = phasecount.getText().toString().trim();
                if(isEditTextNull(phasecountString,phasecount) == true){
                    return;
                }
                String frequencyString = frequency.getText().toString().trim();
                if(isEditTextNull(frequencyString,frequency) == true){
                    return;
                }
                String voltageString = voltage.getText().toString().trim();
                if(isEditTextNull(voltageString,voltage) == true){
                    return;
                }
                String ratedcapacityString = ratedcapacity.getText().toString().trim();
                if(isEditTextNull(ratedcapacityString,ratedcapacity) == true){
                    return;
                }
                String useconditionString = usecondition.getText().toString().trim();
                if(isEditTextNull(useconditionString,usecondition) == true){
                    return;
                }
                String oilString = oil.getText().toString().trim();
                if(isEditTextNull(oilString,oil) == true){
                    return;
                }
                String coolmethodString = coolmethod.getText().toString().trim();
                if(isEditTextNull(coolmethodString,coolmethod) == true){
                    return;
                }
                String voltagesetmodelString = voltagesetmodel.getText().toString().trim();
                if(isEditTextNull(voltagesetmodelString,voltagesetmodel) == true){
                    return;
                }
                String companyString = company.getText().toString().trim();
                if(isEditTextNull(companyString,company) == true){
                    return;
                }
                String productiondateString = productiondate.getText().toString().trim();
                if(!productiondateString.equals("")){
                    productiondateString += "-01";
                }
                String usedateString = usedate.getText().toString().trim();
                if(!usedateString.equals("")){
                    usedateString += "-01";
                }
                String updatetimeeString = updatetime.getText().toString().trim();
                if(!updatetimeeString.equals("")){
                    updatetimeeString += "-01";
                }
                //查询变电站
                TrStation trStation = new TrStation();
                trStation.setStationid(stationid);
                List<TrStation> trStationList = trStationDao.queryTrStationList(trStation);
                //查询供电局
                TrBureau trBureau = new TrBureau();
                List<TrBureau> trBureauList = new ArrayList<TrBureau>();
                if(trStationList.size() > 0){
                    trBureau.setBureauid(trStationList.get(0).getBureauid());
                    trBureauList = trBureauDao.queryTrBureauList(trBureau);
                }
                //数据插入TrEquipment对象
                String equipmentid = Constants.getUuid();
                TrEquipment trEquipment = new TrEquipment();
                trEquipment.setEquipmentid(equipmentid);
                if(trStationList.size() > 0){
                    trEquipment.setStationid(trStationList.get(0).getStationid());
                    trEquipment.setStationname(trStationList.get(0).getStationname());
                }
                if(trBureauList.size() > 0){
                    trEquipment.setBureauid(trBureauList.get(0).getBureauid());
                    trEquipment.setBureauname(trBureauList.get(0).getBureauname());
                }
                trEquipment.setEquipmentname(equipmentnameString);
                trEquipment.setCcbh(ccbhString);
                trEquipment.setConnectnumber(connectnumberString);
                trEquipment.setModel(modelString);
                trEquipment.setPhasecount(phasecountString);
                trEquipment.setFrequency(frequencyString);
                trEquipment.setVoltage(voltageString);
                trEquipment.setRatedcapacity(ratedcapacityString);
                trEquipment.setUsecondition(useconditionString);
                trEquipment.setOil(oilString);
                trEquipment.setCoolmethod(coolmethodString);
                trEquipment.setVoltagesetmodel(voltagesetmodelString);
                trEquipment.setCompany(companyString);
                trEquipment.setProductiondate(productiondateString);
                trEquipment.setUsedate(usedateString);
                trEquipment.setUpdatetime(updatetimeeString);
                if(Network.isNetConnected(context)){
                    menu_check.setVisibility(View.GONE);
                    equipmentAdd(trEquipment);
                }else{
                    Toast.makeText(context,"网络未连接或信号不好",Toast.LENGTH_SHORT).show();
                    menu_check.setVisibility(View.VISIBLE);
                }
            break;
        }
    }


    //设备信息保存方法
    private void equipmentAdd(final TrEquipment trEquipment){
        final Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("equipmentid",trEquipment.getEquipmentid());
        dataMap.put("equipmentname",trEquipment.getEquipmentname());
        dataMap.put("ccbh",trEquipment.getCcbh());
        dataMap.put("connectnumber",trEquipment.getConnectnumber());
        dataMap.put("model",trEquipment.getModel());
        dataMap.put("phasecount",trEquipment.getPhasecount());
        dataMap.put("frequeecy",trEquipment.getFrequency());
        dataMap.put("voltage",trEquipment.getVoltage());
        dataMap.put("ratedcapacity",trEquipment.getRatedcapacity());
        dataMap.put("usecondition",trEquipment.getUsecondition());
        dataMap.put("oil",trEquipment.getOil());
        dataMap.put("coolmethod",trEquipment.getCoolmethod());
        dataMap.put("voltagesetmodel",trEquipment.getVoltagesetmodel());
        dataMap.put("company",trEquipment.getCompany());
        dataMap.put("productiondate",trEquipment.getProductiondate());
        dataMap.put("usedate",trEquipment.getUsedate());
        dataMap.put("stationid",trEquipment.getStationid());
        dataMap.put("stationname",trEquipment.getStationname());
        dataMap.put("bureauid",trEquipment.getBureauid());
        dataMap.put("bureauname",trEquipment.getBureauname());
        dataMap.put("updatetime",trEquipment.getUpdatetime());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.ADDEQUIPMENT;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                Log.i("==========resultMap=", resultMap + "");
                //新增成功
                if(resultMap != null && resultMap.get("ok").equals("true")){
                    Message msg = new Message();
                    msg.obj = trEquipment;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        };
        new Thread(runnable).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                List<TrEquipment> trEquipmentList = new ArrayList<TrEquipment>();
                trEquipmentList.add((TrEquipment)msg.obj);
                trEquipmentDao.insertListData(trEquipmentList);
                Intent intent = new Intent();
                intent.putExtra("result",(TrEquipment)msg.obj);
                setResult(RESULT_OK,intent);
                finish();
            }else{
                menu_check.setVisibility(View.VISIBLE);
                Toast.makeText(context,"新增变压器失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
