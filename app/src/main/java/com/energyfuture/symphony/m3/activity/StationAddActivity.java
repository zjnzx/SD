package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.BureauAdapter;
import com.energyfuture.symphony.m3.adapter.VoltageAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrBureauDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrSystemcodeDao;
import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.domain.TrSystemcode;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationAddActivity extends ActionBarActivity implements View.OnClickListener,View.OnTouchListener {
    private Context context = StationAddActivity.this;
    private View popuLayout;
    private LinearLayout menu_back,menu_check;
    private EditText stationname,subcode,bureauid,voltage;
    private String visible = "1";
    private RadioGroup radioGroup;
    private RadioButton radioYes,radioNo;
    private TrBureauDao trBureauDao = new TrBureauDao(context);
    private List<TrBureau> trBureauList = new ArrayList<TrBureau>();
    private BureauAdapter bureauAdapter;
    private ListView mListView;
    private PopupWindow popupWindow;
    private boolean flag = false; //防止list越界标志
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_add);
        TrBureau trBureau = new TrBureau();
        trBureauList = trBureauDao.queryTrBureauList(trBureau);
        initView();
        if (Network.isNetworkConnected(context) == true){
            getData();
        }
    }

    private void initView() {
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_check = (LinearLayout)findViewById(R.id.menu_check);
        stationname = (EditText)findViewById(R.id.stationname);
        subcode = (EditText)findViewById(R.id.subcode);
        bureauid = (EditText)findViewById(R.id.bureauid);
        voltage = (EditText)findViewById(R.id.voltage);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioYes = (RadioButton)findViewById(R.id.radioYes);
        radioNo = (RadioButton)findViewById(R.id.radioNo);
        popuLayout= LayoutInflater.from(context).inflate(R.layout.popu_listview,null);
        mListView= (ListView) popuLayout.findViewById(R.id.popu_listview);
        popupWindow = new PopupWindow(popuLayout, 795, 500);

        bureauAdapter = new BureauAdapter(context,trBureauList);

        menu_back.setOnClickListener(this);
        menu_check.setOnClickListener(this);
        voltage.setOnClickListener(this);
        bureauid.setOnTouchListener(this);

        createPopuwindow();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();

                RadioButton rb = (RadioButton)StationAddActivity.this.findViewById(radioButtonId);

                if(rb.getText().equals("是")){
                    visible = "1";
                }else{
                    visible = "0";
                }
            }
        });
        //模糊查询
        bureauid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String bureauName = s + "";
                TrBureau trBureau = new TrBureau();
                trBureau.setBureauname(bureauName);
                if(!flag){
                    trBureauList.clear();
                    bureauAdapter.trBureauList.clear();
                    trBureauList = trBureauDao.queryTrBureauList(trBureau);
                }
                bureauAdapter.trBureauList.addAll(trBureauList);
                setListItemClick();
                mListView.setAdapter(bureauAdapter);
                popupWindow.showAsDropDown(bureauid, 0, 0);
                flag = false;
            }
        });
    }

    private void createPopuwindow(){
        //点击空白处消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击键盘不消失
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

    }

    /**
     * 电压等级
     */
    private PopupWindow getVoltageList(){
        final PopupWindow popupWindow = new PopupWindow(popuLayout, 795, 500);
        //点击空白处消失
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        TrSystemcodeDao trSystemcodeDao = new TrSystemcodeDao(context);
        TrSystemcode trSystemcode = new TrSystemcode();
        trSystemcode.setCodetype("VOLTAGE");
        final List<TrSystemcode> trSystemcodeList = trSystemcodeDao.queryTrSystemcodesList(trSystemcode);

        ListView mListView= (ListView) popuLayout.findViewById(R.id.popu_listview);
        VoltageAdapter voltageAdapter = new VoltageAdapter(context,trSystemcodeList);
        mListView.setAdapter(voltageAdapter);
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(voltage, 0, 0);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                voltage.setText(trSystemcodeList.get(position).getCodevalue() + "kV");
                voltage.setTag(R.id.voltage,trSystemcodeList.get(position).getCodevalue());
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    /**
     * 电力公司
     */
    private PopupWindow getBureauList(){
        mListView.setAdapter(bureauAdapter);
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(bureauid, 0, 0);
        }
        setListItemClick();
        return popupWindow;
    }

    /**
     * listview点击事件
     */
    private void setListItemClick(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag = true;
                String bureauName = trBureauList.get(position).getBureauname();
                bureauid.setText(bureauName);
                bureauid.setSelection(bureauName.length());
                bureauid.setTag(R.id.bureauid,trBureauList.get(position).getBureauid());
                popupWindow.dismiss();
            }
        });
    }

    //判断EditText是否为空
    private boolean isEditTextNull(String string,EditText editText){
        boolean isNull = false;
        if ("".equals(string)) {
            Toast.makeText(getBaseContext(), "请输入" + editText.getTag() + "！", Toast.LENGTH_SHORT).show();
            if(editText.getTag().equals("电压等级")){
                getVoltageList();
            }else if(editText.getTag().equals("电力公司")){
                Map<String, Object> condMap = new HashMap<String, Object>();
                condMap.put("OBJ","BUREAU");
                new DataSyschronized(StationAddActivity.this).getDataFromWeb(condMap, handler1);
                getBureauList();
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

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TrBureau trBureau = new TrBureau();
            trBureauList = trBureauDao.queryTrBureauList(trBureau);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_back:
                setResult(RESULT_CANCELED);
                finish();
            break;
            case R.id.voltage:
                getVoltageList();
            break;
            case R.id.menu_check:
                String stationnameString = stationname.getText().toString().trim();
                if(isEditTextNull(stationnameString,stationname) == true){
                    return;
                }
                String subcodeString = subcode.getText().toString().trim();
                if(isEditTextNull(subcodeString,subcode) == true){
                    return;
                }
                String bureauidString = bureauid.getText().toString().trim();
                if(isEditTextNull(bureauidString,bureauid) == true){
                    return;
                }
                String bureauidTag = bureauid.getTag(R.id.bureauid).toString();
                String voltageString = voltage.getText().toString().trim();
                if(isEditTextNull(voltageString,voltage) == true){
                    return;
                }
                String voltageTag = voltage.getTag(R.id.voltage).toString();
                //数据插入TrStation对象
                String stationid = Constants.getUuid();
                TrStation trStation = new TrStation();
                trStation.setStationid(stationid);
                trStation.setStationname(stationnameString);
                trStation.setSubcode(subcodeString);
                trStation.setBureauid(bureauidTag);
                trStation.setVoltage(voltageTag);
                trStation.setVisible(visible);
                if(Network.isNetConnected(context)){
                    menu_check.setVisibility(View.GONE);
                    StationAdd(trStation);
                }else{
                    Toast.makeText(context,"网络未连接或信号不好",Toast.LENGTH_SHORT).show();
                    menu_check.setVisibility(View.VISIBLE);
                }
            break;
        }
    }

    //获取平台供电局数据
    private void getData(){
        trBureauDao.deleteListData(trBureauList);
        Map<String, Object> condMap = new HashMap<String, Object>();
        condMap.put("OBJ","BUREAU");
        condMap.put("SIZE",20);
        new DataSyschronized(StationAddActivity.this).getDataFromWeb(condMap, handler1);
    }

    //保存到数据库
    private void StationAdd(final TrStation trStation){
        final Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("stationid",trStation.getStationid());
        dataMap.put("stationname",trStation.getStationname());
        dataMap.put("bureauid",trStation.getBureauid());
        dataMap.put("subcode",trStation.getSubcode());
        dataMap.put("voltage",trStation.getVoltage());
        dataMap.put("visible",trStation.getVisible());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.ADDSTATION;
                Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                Log.i("==========resultMap=", resultMap + "");
                //新增成功
                if(resultMap != null && resultMap.get("ok").equals("true")){
                    Message msg = new Message();
                    msg.obj = trStation;
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
                List<TrStation> trStationList = new ArrayList<TrStation>();
                trStationList.add((TrStation)msg.obj);
                TrStationDao trStationDao = new TrStationDao(context);
                trStationDao.insertListData(trStationList);
                Intent intent = new Intent();
                intent.putExtra("result",(TrStation)msg.obj);
                setResult(RESULT_OK,intent);
                finish();
            }else{
                menu_check.setVisibility(View.VISIBLE);
                Toast.makeText(context,"新增变电站失败！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            //电力公司
            case R.id.bureauid:
                getBureauList();
                break;
        }
        return false;
    }
}
