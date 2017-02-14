package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.adapter.EquipmentAdapter;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.ApiClient;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.TrEquipmentDao;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Network;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAddActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context = TaskAddActivity.this;
    private GridView mGridViewEqu;
    private LinearLayout menu_back,menu_check;
    private Button mEquipmentAdd;
    private List<TrEquipment> trEquipmentList = new ArrayList<TrEquipment>();
    private EquipmentAdapter equipmentAdapter;
    private TrProject trProject;
    private TextView mEquipmentPrompt;
    private MaterialDialog mMaterialDialog;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add);
        //禁止弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();

    }

    private void initView(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        trProject = (TrProject) bundle.get("trProject");

        mGridViewEqu = (GridView) findViewById(R.id.equipment_gridview_task);
        mEquipmentAdd = (Button) findViewById(R.id.equipment_add_task);
        menu_back = (LinearLayout)findViewById(R.id.menu_back);
        menu_check = (LinearLayout)findViewById(R.id.menu_check);
        mEquipmentPrompt = (TextView)findViewById(R.id.equipment_prompt);

        menu_back.setOnClickListener(this);
        menu_check.setOnClickListener(this);
        mEquipmentAdd.setOnClickListener(this);

        getEquipmentDataFromWeb();
    }

    /**
     * 向平台请求新数据
     */
    private void getEquipmentDataFromWeb(){
        final Map<String, Object> condMap = Constants.getRequestParam(context, "PROJECTADDEQUIPMENT", null, trProject.getStationid());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                    //查询设备
                    getEquipmentList();
            }
        };
        new DataSyschronized(context).getDataFromWeb(condMap, handler);
    }


    /**
     * 封装消息
     */
    private void senMessage(){
        StringBuffer equipmentIdStrBuf = new StringBuffer();
        for (String key : Utils.equipmentMap.keySet()) {
            equipmentIdStrBuf.append(key).append(",");
        }
        String equipment = equipmentIdStrBuf.toString();

        if(!equipment.equals("")){
            final Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("prj_tfs",equipment);
            dataMap.put("proId",trProject.getProjectid());

            mMaterialDialog = new MaterialDialog(context);
            mMaterialDialog.setCanceledOnTouchOutside(false);
            View view=LayoutInflater.from(context).inflate(R.layout.progressdialog, null);
            TextView text= (TextView) view.findViewById(R.id.text);
            bar= (ProgressBar) view.findViewById(R.id.progressBar2);
            ProgressBar barLogin= (ProgressBar) view.findViewById(R.id.progressBar_login);
            bar.setVisibility(View.VISIBLE);
            barLogin.setVisibility(View.GONE);
            text.setText("正在新增任务，请稍候...");
            mMaterialDialog.setView(view).show();

            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    //提交消息到云平台
                    String http = URLs.HTTP+URLs.HOST+"/"+ "inspectfuture/" +URLs.ADDTASK;
                    Map resultMap = ApiClient.sendHttpPostMessageData(null,http,dataMap,"",context,"文本");
                    //新增成功
                    if(resultMap != null && resultMap.get("ok").equals("true")){
                        handler.sendEmptyMessage(1);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            new Thread(runnable).start();
        }else{
            Toast.makeText(context,"请选择设备",Toast.LENGTH_SHORT).show();
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                mMaterialDialog.dismiss();
//                bar.setVisibility(View.GONE);
                Toast.makeText(context,"新增任务成功",Toast.LENGTH_SHORT).show();
                TaskAddActivity.this.onBackPressed();
            }else if(msg.what == 0){
                mMaterialDialog.dismiss();
//                bar.setVisibility(View.GONE);
                Toast.makeText(context,"新增任务失败,请重新尝试",Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 巡检设备
     */
    private void getEquipmentList(){
        TrEquipmentDao trEquipmentDao = new TrEquipmentDao(context);
        equipmentAdapter = new EquipmentAdapter(context,new ArrayList<TrEquipment>());
        trEquipmentList = trEquipmentDao.queryTrEquipment(trProject);
        equipmentAdapter.trEquipmentList.addAll(trEquipmentList);
        if(trEquipmentList.size() == 0){
            mEquipmentPrompt.setVisibility(View.VISIBLE);
            mGridViewEqu.setVisibility(View.GONE);
        }
        mGridViewEqu.setAdapter(equipmentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //新增变压器
            case R.id.equipment_add_task:
                Intent intentEquipment = new Intent(context,EquipmentAddActivity.class);
                intentEquipment.putExtra("stationId",trProject.getStationid());
                startActivityForResult(intentEquipment,2);
                break;
            //点击返回
            case R.id.menu_back:
                this.onBackPressed();
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
            getEquipmentList();
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            mEquipmentPrompt.setVisibility(View.GONE);
            mGridViewEqu.setVisibility(View.VISIBLE);
            TrEquipment trEquipment = (TrEquipment)data.getSerializableExtra("result");
            equipmentAdapter.trEquipmentList.add(trEquipment);
            equipmentAdapter.notifyDataSetChanged();
        }
    }
}
