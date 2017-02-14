package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.DepartmentAdapter;
import com.energyfuture.symphony.m3.adapter.EquipmentAdapter;
import com.energyfuture.symphony.m3.adapter.StationAdapter;
import com.energyfuture.symphony.m3.adapter.UserInfoAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrEquipmentDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrUserInfoDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectAddActivity extends ActionBarActivity implements View.OnClickListener,View.OnTouchListener {
    private Context context = ProjectAddActivity.this;
    private RecyclerView mRecyclerViewMember;
    private GridView mGridViewEqu;
    private LinearLayout menu_back,menu_check;
    private EditText mProjectName,mProjectTeams,mStationName,mProjectStartTime,mProjectEndTime;
    private Button add_station_btn,add_voltage_btn;
    private View mPopuLayout;
    private String departmentId = "",stationId = "";
    private UserInfoAdapter userInfoAdapter;
    private List<TrEquipment> trEquipmentList = new ArrayList<TrEquipment>();
    private List<TrStation> trStationList = new ArrayList<TrStation>();
    private EquipmentAdapter equipmentAdapter;
    private TextView mEquipmentPrompt;
    private TrStationDao trStationDao;
    private StationAdapter stationAdapter;
    private PopupWindow popupWindow;
    private ListView mListView;
    private MaterialDialog mMaterialDialog;
    private boolean flag = false; //防止list越界标志
    private ProgressBar bar;
    private String projectName = "",stationName = "",startTime = "",endTime = "",teams = "",equipment = "",member = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_add);
        //禁止弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //向平台请求新数据
        getNewDataFromWeb("PROJECTADD", "");
    }

    private void initView(){
        trStationDao = new TrStationDao(context);
        mProjectName = (EditText) findViewById(R.id.project_name);
        mProjectStartTime = (EditText) findViewById(R.id.project_starttime);
        mProjectEndTime = (EditText) findViewById(R.id.project_endtime);
        mProjectTeams = (EditText) findViewById(R.id.project_teams);
        mStationName = (EditText) findViewById(R.id.project_station_name);
        mGridViewEqu = (GridView) findViewById(R.id.equipment_gridview);
        mRecyclerViewMember = (RecyclerView) findViewById(R.id.member_recycle);
        add_station_btn = (Button) findViewById(R.id.add_station_btn);
        add_voltage_btn = (Button) findViewById(R.id.add_voltage_btn);
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_check = (LinearLayout)findViewById(R.id.menu_check);
        mEquipmentPrompt = (TextView)findViewById(R.id.equipment_prompt_project);
        mPopuLayout= LayoutInflater.from(context).inflate(R.layout.popu_listview,null);
        mListView= (ListView) mPopuLayout.findViewById(R.id.popu_listview);

        Date date = new Date();
        mProjectStartTime.setText(Constants.dateformat1.format(date));
//        date.setMonth(date.getMonth() + 1);
        mProjectEndTime.setText(Constants.dateformat1.format(date));

        equipmentAdapter = new EquipmentAdapter(context,new ArrayList<TrEquipment>());
        stationAdapter = new StationAdapter(context,new ArrayList<TrStation>());

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context,3);
        mRecyclerViewMember.setLayoutManager(gridLayoutManager2);

        add_station_btn.setOnClickListener(this);
        add_voltage_btn.setOnClickListener(this);
        mProjectStartTime.setOnClickListener(this);
        mProjectEndTime.setOnClickListener(this);
        mStationName.setOnTouchListener(this);
        mProjectTeams.setOnClickListener(this);
        menu_back.setOnClickListener(this);
        menu_check.setOnClickListener(this);

        createPopuwindow();

        //模糊查询
        mStationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String stationName = s + "";
                TrStation trStation = new TrStation();
                trStation.setStationname(stationName);
                if(!flag){
                    trStationList.clear();
                    stationAdapter.trStationList.clear();
                    trStationList = trStationDao.queryTrStationList(trStation);
                    if(trStationList.size() > 0){
                        stationId = trStationList.get(0).getStationid();
                    }
                }
                stationAdapter.trStationList.addAll(trStationList);
                setListItemClick();
                mListView.setAdapter(stationAdapter);
                popupWindow.showAsDropDown(mStationName, 0, 0);
                flag = false;
            }
        });
    }

    private void createPopuwindow(){
        popupWindow = new PopupWindow(mPopuLayout, 580, 500);
        //点击空白处消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击键盘不消失
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }

    /**
     * 判断是否为空
     */
    private boolean isEmpty(){
        if(stationName.equals("")){
            Toast.makeText(context,"变电站不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(projectName.equals("")){
            Toast.makeText(context,"项目名称不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(equipment.equals("")){
            Toast.makeText(context,"巡检设备不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(startTime.equals("")){
            Toast.makeText(context,"开始时间不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(endTime.equals("")){
            Toast.makeText(context,"结束时间不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(teams.equals("")){
            Toast.makeText(context,"执行班组不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else if(member.equals("")){
            Toast.makeText(context,"成员及负责人不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }

    }

    /**
     * 封装消息
     */
    private void senMessage(){
        StringBuffer equipmentIdStrBuf = new StringBuffer();
        projectName = mProjectName.getText().toString();
        stationName = mStationName.getText().toString();
        startTime = mProjectStartTime.getText().toString();
        endTime = mProjectEndTime.getText().toString();
        teams = mProjectTeams.getText().toString();
        for (String key : Utils.equipmentMap.keySet()) {
            equipmentIdStrBuf.append(key).append(",");
        }
        equipment = equipmentIdStrBuf.toString();
        for (int key : Utils.radioButtonMap.keySet()) {
            if(Utils.radioButtonMap.get(key)){
                member = userInfoAdapter.userInfoList.get(key).getYhid();
                break;
            }
        }

        if(!isEmpty()) {
            final Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("prj_station_id", stationId);
            dataMap.put("prj_station", stationName);
            dataMap.put("prj_title", projectName);
            dataMap.put("prj_date", startTime + "/" + endTime);
            dataMap.put("prj_team", departmentId);
            dataMap.put("prj_tfs", equipment);
            dataMap.put("prj_manager", member);

            mMaterialDialog = new MaterialDialog(context);
            mMaterialDialog.setCanceledOnTouchOutside(false);
            View view = LayoutInflater.from(context).inflate(R.layout.progressdialog, null);
            TextView text = (TextView) view.findViewById(R.id.text);
            bar = (ProgressBar) view.findViewById(R.id.progressBar2);
            ProgressBar barLogin = (ProgressBar) view.findViewById(R.id.progressBar_login);
            text.setText("正在新增项目，请稍候...");
            bar.setVisibility(View.VISIBLE);
            barLogin.setVisibility(View.GONE);
            mMaterialDialog.setView(view).show();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //提交消息到云平台
                    String http = URLs.HTTP + URLs.HOST + "/" + "inspectfuture/" + URLs.ADDPROJECT;
                    Map resultMap = ApiClient.sendHttpPostMessageData(null, http, dataMap, "", context, "文本");
                    Log.i("=============resultMap=", resultMap + "");
                    //新增成功
                    if (resultMap != null && resultMap.get("ok").equals("true")) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            new Thread(runnable).start();
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                mMaterialDialog.dismiss();
                Toast.makeText(context,"新增项目成功",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                ProjectAddActivity.this.onBackPressed();
            }else if(msg.what == 0){
                mMaterialDialog.dismiss();
                Toast.makeText(context,"新增项目失败,请重新尝试",Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 巡检设备
     */
    private void getEquipmentList(String stationId){
        TrEquipmentDao trEquipmentDao = new TrEquipmentDao(context);
        TrEquipment trEquipment = new TrEquipment();
        trEquipment.setStationid(stationId);
        trEquipmentList.clear();
        equipmentAdapter.trEquipmentList.clear();
        trEquipmentList = trEquipmentDao.queryTrEquipmentList(trEquipment);
        if(trEquipmentList.size() == 0){
            mEquipmentPrompt.setVisibility(View.VISIBLE);
            mGridViewEqu.setVisibility(View.GONE);
        }else{
            mEquipmentPrompt.setVisibility(View.GONE);
            mGridViewEqu.setVisibility(View.VISIBLE);
            equipmentAdapter.trEquipmentList.addAll(trEquipmentList);
            mGridViewEqu.setAdapter(equipmentAdapter);
        }
    }
    /**
     * 成员及负责人
     */
    private void getMemberList(){
        if(!departmentId.equals("")){
            TrUserInfoDao trUserInfoDao = new TrUserInfoDao(context);
            UserInfo userInfo = new UserInfo();
            userInfo.setBmbh(departmentId);
            List<UserInfo> userInfoList = trUserInfoDao.queryUserInfoList(userInfo);
            for(int i = 0;i < userInfoList.size();i++){
                UserInfo user = userInfoList.get(i);
                if(user.getYhid().equals("guest") || user.getYhid().equals("admin")){
                    userInfoList.remove(user);
                }
            }
            userInfoAdapter = new UserInfoAdapter(context,userInfoList);
            mRecyclerViewMember.setAdapter(userInfoAdapter);
        }
    }

    /**
     * 变电站
     */
    private PopupWindow getTrStationList(){
        trStationList.clear();
        stationAdapter.trStationList.clear();
        trStationList =  trStationDao.queryTrStationList(new TrStation());
        stationAdapter.trStationList.addAll(trStationList);
        mListView.setAdapter(stationAdapter);
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(mStationName, 0, 0);
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
                String stationName = trStationList.get(position).getStationname();
                mStationName.setText(stationName);
                mStationName.setSelection(stationName.length());
                mProjectName.setText(stationName + "巡检项目");
                stationId = trStationList.get(position).getStationid();
                add_voltage_btn.setEnabled(true);
                add_voltage_btn.setTag(stationId);
                add_voltage_btn.setBackgroundResource(R.drawable.project_button);
                popupWindow.dismiss();

                //向平台请求新数据
                getNewDataFromWeb("PROJECTADDEQUIPMENT", stationId);

            }
        });
    }

    /**
     * 执行班组
     */
    private PopupWindow getTeamsList(){
        final PopupWindow popupWindow = new PopupWindow(mPopuLayout, 795,350);
        //点击空白处消失
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        TrDepartmentDao trDepartmentDao = new TrDepartmentDao(context);
        Department department = new Department();
        department.setSsbm("101000000000000000");
        final List<Department> departmentList =  trDepartmentDao.queryDepartmentList(department);

        ListView mListView= (ListView) mPopuLayout.findViewById(R.id.popu_listview);
        DepartmentAdapter departmentAdapter = new DepartmentAdapter(context,departmentList);
        mListView.setAdapter(departmentAdapter);
        if(!popupWindow.isShowing()){
            popupWindow.showAsDropDown(mProjectTeams, 0, 0);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                departmentId = departmentList.get(position).getBmid();
                mProjectTeams.setText(departmentList.get(position).getBmmc());
                popupWindow.dismiss();
                //成员及负责人
                getMemberList();
            }
        });
        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //新增变电站
            case R.id.add_station_btn:
                Intent intentStation = new Intent(context,StationAddActivity.class);
                startActivityForResult(intentStation, 1);
                break;
            //新增变压器
            case R.id.add_voltage_btn:
                String stationid = v.getTag().toString();
                Intent intentEquipment = new Intent(context,EquipmentAddActivity.class);
                intentEquipment.putExtra("stationId",stationid);
                startActivityForResult(intentEquipment,2);
                break;
            //开始时间
            case R.id.project_starttime:
                DatePicker dataPickerStart = new DatePicker(context);
                dataPickerStart.selectDateDialog(mProjectStartTime,2,null,mProjectEndTime,0);
                break;
            //结束时间
            case R.id.project_endtime:
                DatePicker dataPickerEnd = new DatePicker(context);
                dataPickerEnd.selectDateDialog(mProjectEndTime,2,null,null,1);
                break;
            //执行班组
            case R.id.project_teams:
                getTeamsList();
                break;
            //点击返回
            case R.id.menu_back:
                this.onBackPressed();
                setResult(RESULT_CANCELED);
                break;
            //点击对号
            case R.id.menu_check:
                if(Network.isNetConnected(context)){
                    senMessage();
                }else{
                    Toast.makeText(context,"网络未连接或信号不好",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            TrStation trStation = (TrStation)data.getSerializableExtra("result");
            mStationName.setText(trStation.getStationname());
            add_voltage_btn.setEnabled(true);
            add_voltage_btn.setTag(trStation.getStationid());
            mProjectName.setText(trStation.getStationname() + "巡检项目");
            add_voltage_btn.setBackgroundResource(R.drawable.project_button);
            getEquipmentList(trStation.getStationid());
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            mEquipmentPrompt.setVisibility(View.GONE);
            mGridViewEqu.setVisibility(View.VISIBLE);
            TrEquipment trEquipment = (TrEquipment)data.getSerializableExtra("result");
//            equipmentAdapter.trEquipmentList.add(trEquipment);
//            equipmentAdapter.notifyDataSetChanged();
            getEquipmentList(trEquipment.getStationid());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            //变电站
            case R.id.project_station_name:
                getTrStationList();
                break;
        }
        return false;
    }

    /**
     * 向平台请求新数据
     */
    private void getNewDataFromWeb(final String obj,String key){
        final Map<String, Object> condMap = Constants.getRequestParam(context,obj,null,key);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(obj.equals("PROJECTADDEQUIPMENT")){
                    //查询设备
                    getEquipmentList(stationId);
                }
            }
        };
        new DataSyschronized(context).getDataFromWeb(condMap, handler);
    }
}
