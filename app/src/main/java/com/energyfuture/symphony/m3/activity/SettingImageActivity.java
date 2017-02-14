package com.energyfuture.symphony.m3.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrParameterDao;
import com.energyfuture.symphony.m3.domain.TrParameter;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingImageActivity extends ActionBarActivity {
    private Context context = SettingImageActivity.this;
    SharedPreferences sharedPreferences ;
    private Spinner dc_spinner,rh_spinner;
    private EditText dc_company,dc_model,dc_sd_route,dc_file_prefix,dc_file_postfix,rh_company,rh_model,rh_sd_route,rh_file_prefix,rh_file_postfix;
    private Button dc_sure,rh_sure,dc_button_add,dc_button_update,dc_button_delete,rh_button_add,rh_button_update,rh_button_delete,dc_cancel,rh_cancel;
    private LinearLayout theme_back;
    private TrParameterDao trParameterDao = new TrParameterDao(context);
    private List<TrParameter> dc_list;
    private List<TrParameter> rh_list;
    private TextView dc_hint,rh_hint,dc_postion,rh_postion;
    private int dc_flag,rh_flag;
    private ArrayAdapter dc_adapter,rh_adapter;
    private List<String> dc_name = new ArrayList<String>();
    private List<String> rh_name = new ArrayList<String>();
    private String userid;
    private SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_image);
        //禁止弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences = context.getSharedPreferences("M3_SETTNG", context.getApplicationContext().MODE_PRIVATE);
        userid = Constants.getLoginPerson(context).get("userId");
        dc_spinner = (Spinner)findViewById(R.id.dc_spinner);
        rh_spinner = (Spinner)findViewById(R.id.rh_spinner);
        dc_hint = (TextView)findViewById(R.id.dc_hint);
        rh_hint = (TextView)findViewById(R.id.rh_hint);
        dc_sure = (Button)findViewById(R.id.dc_sure);
        rh_sure = (Button)findViewById(R.id.rh_sure);
        dc_cancel = (Button)findViewById(R.id.dc_cancel);
        rh_cancel = (Button)findViewById(R.id.rh_cancel);
        dc_postion = (TextView)findViewById(R.id.dc_postion);
        rh_postion = (TextView)findViewById(R.id.rh_postion);
        dc_button_add = (Button)findViewById(R.id.dc_button_add);
        dc_button_update = (Button)findViewById(R.id.dc_button_update);
        dc_button_delete = (Button)findViewById(R.id.dc_button_delete);
        rh_button_add = (Button)findViewById(R.id.rh_button_add);
        rh_button_update = (Button)findViewById(R.id.rh_button_update);
        rh_button_delete = (Button)findViewById(R.id.rh_button_delete);
        dc_company = (EditText)findViewById(R.id.dc_company);
        dc_model = (EditText)findViewById(R.id.dc_model);
        dc_sd_route = (EditText)findViewById(R.id.dc_sd_route);
        dc_file_prefix = (EditText)findViewById(R.id.dc_file_prefix);
        dc_file_postfix = (EditText)findViewById(R.id.dc_file_postfix);
        rh_company = (EditText)findViewById(R.id.rh_company);
        rh_model = (EditText)findViewById(R.id.rh_model);
        rh_sd_route = (EditText)findViewById(R.id.rh_sd_route);
        rh_file_prefix = (EditText)findViewById(R.id.rh_file_prefix);
        rh_file_postfix = (EditText)findViewById(R.id.rh_file_postfix);
        theme_back = (LinearLayout)findViewById(R.id.theme_back);
        getData();
        if(dc_list.size() > 0){
            trParameterDao.deleteListData(dc_list);
        }
        if(rh_list.size() > 0){
            trParameterDao.deleteListData(rh_list);
        }
        getDataWeb();
        dc_adapter = new ArrayAdapter(this,R.layout.spinner, dc_name){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
                }
                TextView label = (TextView) convertView.findViewById(R.id.label);
                label.setText(getItem(position).toString());
                return convertView;
            }
        };
        dc_spinner.setAdapter(dc_adapter);

        dc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!dc_name.get(position).equals("无") && !dc_name.get(position).equals("新增")) {
                    dc_company.setText(dc_list.get(position).getCompany());
                    dc_model.setText(dc_list.get(position).getModel());
                    dc_sd_route.setText(dc_list.get(position).getSdfileurl());
                    String fileprefix = (dc_list.get(position).getFileprefix() == null || dc_list.get(position).getFileprefix().equals("null")) ? "" : dc_list.get(position).getFileprefix();
                    String filesuffix = (dc_list.get(position).getFilesuffix() == null || dc_list.get(position).getFilesuffix().equals("null")) ? "" : dc_list.get(position).getFilesuffix();
                    dc_file_prefix.setText(fileprefix);
                    dc_file_postfix.setText(filesuffix);
                    dc_hint.setText(dc_list.get(position).getId());
                    dc_postion.setText(position + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rh_adapter = new ArrayAdapter(this,R.layout.spinner, rh_name){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView = getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
                }
                TextView label = (TextView) convertView.findViewById(R.id.label);
                label.setText(getItem(position).toString());
                return convertView;
            }
        };
        rh_spinner.setAdapter(rh_adapter);

        rh_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!rh_name.get(position).equals("无") && !rh_name.get(position).equals("新增")){
                    rh_company.setText(rh_list.get(position).getCompany());
                    rh_model.setText(rh_list.get(position).getModel());
                    rh_sd_route.setText(rh_list.get(position).getSdfileurl());
                    String fileprefix = (rh_list.get(position).getFileprefix() == null || rh_list.get(position).getFileprefix().equals("null")) ? "" : rh_list.get(position).getFileprefix();
                    String filesuffix = (rh_list.get(position).getFilesuffix() == null || rh_list.get(position).getFilesuffix().equals("null")) ? "" : rh_list.get(position).getFilesuffix();
                    rh_file_prefix.setText(fileprefix);
                    rh_file_postfix.setText(filesuffix);
                    rh_hint.setText(rh_list.get(position).getId());
                    rh_postion.setText(position + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dc_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String company = dc_company.getText().toString();
                String model = dc_model.getText().toString();
                String sd_route = dc_sd_route.getText().toString();
                String file_prefix = dc_file_prefix.getText().toString();
                String file_postfix = dc_file_postfix.getText().toString();
                if(isEditTextNull(company,dc_company) == true){
                    return;
                }
                if(isEditTextNull(model,dc_model) == true){
                    return;
                }
                if(isEditTextNull(sd_route,dc_sd_route) == true){
                    return;
                }
                String date = Constants.dateformat2.format(new Date());
                dc_false();
                if(dc_flag == 0){
                    String id = Constants.getUuid();
                    int selection = dc_list.size();
                    TrParameter trParameter = new TrParameter();
                    trParameter.setId(id);
                    trParameter.setParametertype("1");
                    trParameter.setCompany(company);
                    trParameter.setModel(model);
                    trParameter.setSdfileurl(sd_route);
                    trParameter.setFileprefix(file_prefix);
                    trParameter.setFilesuffix(file_postfix);
                    trParameter.setUpdateperson(userid);
                    trParameter.setUpdatetime(date);
                    List<TrParameter> trParameters = new ArrayList<TrParameter>();
                    trParameters.add(trParameter);
                    trParameterDao.insertListData(trParameters);
                    getData();
                    dc_adapter.notifyDataSetChanged();
                    dc_spinner.setSelection(selection);
                    addData(trParameter);
                }else if(dc_flag == 1){
                    String id = dc_hint.getText().toString();
                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("COMPANY", company);
                    columnsMap.put("MODEL", model);
                    columnsMap.put("SDFILEURL", sd_route);
                    columnsMap.put("FILEPREFIX", file_prefix);
                    columnsMap.put("FILESUFFIX", file_postfix);
                    columnsMap.put("UPDATEPERSON", userid);
                    columnsMap.put("UPDATETIME", date);
                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID", id);
                    trParameterDao.updateTrParameterInfo(columnsMap,wheresMap);
                    getData();
                    dc_adapter.notifyDataSetChanged();
                    updateData(id,company,model,sd_route,file_prefix,file_postfix,userid,date,"1");
                }
            }
        });
        dc_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc_false();
                if(dc_flag == 0){
                    getData();
                    dc_adapter.notifyDataSetChanged();
                    dc_company.setText(dc_list.get(0).getCompany());
                    dc_model.setText(dc_list.get(0).getModel());
                    dc_sd_route.setText(dc_list.get(0).getSdfileurl());
                    String fileprefix = (dc_list.get(0).getFileprefix() == null || dc_list.get(0).getFileprefix().equals("null")) ? "" : dc_list.get(0).getFileprefix();
                    String filesuffix = (dc_list.get(0).getFilesuffix() == null || dc_list.get(0).getFilesuffix().equals("null")) ? "" : dc_list.get(0).getFilesuffix();
                    dc_file_prefix.setText(fileprefix);
                    dc_file_postfix.setText(filesuffix);
                    dc_hint.setText(dc_list.get(0).getId());
                    dc_postion.setText(0 + "");
                }
            }
        });
        rh_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String company = rh_company.getText().toString();
                String model = rh_model.getText().toString();
                String sd_route = rh_sd_route.getText().toString();
                String file_prefix = rh_file_prefix.getText().toString();
                String file_postfix = rh_file_postfix.getText().toString();
                if(isEditTextNull(company,rh_company) == true){
                    return;
                }
                if(isEditTextNull(model,rh_model) == true){
                    return;
                }
                if(isEditTextNull(sd_route,rh_sd_route) == true){
                    return;
                }
                String date = Constants.dateformat2.format(new Date());
                rh_false();
                if(rh_flag == 0){
                    String id = Constants.getUuid();
                    int selection = rh_list.size();
                    TrParameter trParameter = new TrParameter();
                    trParameter.setId(id);
                    trParameter.setParametertype("2");
                    trParameter.setCompany(company);
                    trParameter.setModel(model);
                    trParameter.setSdfileurl(sd_route);
                    trParameter.setFileprefix(file_prefix);
                    trParameter.setFilesuffix(file_postfix);
                    trParameter.setUpdateperson(userid);
                    trParameter.setUpdatetime(date);
                    List<TrParameter> trParameters = new ArrayList<TrParameter>();
                    trParameters.add(trParameter);
                    trParameterDao.insertListData(trParameters);
                    getData();
                    rh_adapter.notifyDataSetChanged();
                    rh_spinner.setSelection(selection);
                    addData(trParameter);
                }else if(rh_flag == 1){
                    String id = rh_hint.getText().toString();
                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("COMPANY", company);
                    columnsMap.put("MODEL", model);
                    columnsMap.put("SDFILEURL", sd_route);
                    columnsMap.put("FILEPREFIX", file_prefix);
                    columnsMap.put("FILESUFFIX", file_postfix);
                    columnsMap.put("UPDATEPERSON", userid);
                    columnsMap.put("UPDATETIME", date);
                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID", id);
                    trParameterDao.updateTrParameterInfo(columnsMap,wheresMap);
                    getData();
                    rh_adapter.notifyDataSetChanged();
                    updateData(id,company,model,sd_route,file_prefix,file_postfix,userid,date,"2");
                }
            }
        });
        rh_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rh_false();
                if(rh_flag == 0){
                    getData();
                    rh_adapter.notifyDataSetChanged();
                    rh_company.setText(rh_list.get(0).getCompany());
                    rh_model.setText(rh_list.get(0).getModel());
                    rh_sd_route.setText(rh_list.get(0).getSdfileurl());
                    String fileprefix = (rh_list.get(0).getFileprefix() == null || rh_list.get(0).getFileprefix().equals("null")) ? "" : rh_list.get(0).getFileprefix();
                    String filesuffix = (rh_list.get(0).getFilesuffix() == null || rh_list.get(0).getFilesuffix().equals("null")) ? "" : rh_list.get(0).getFilesuffix();
                    rh_file_prefix.setText(fileprefix);
                    rh_file_postfix.setText(filesuffix);
                    rh_hint.setText(rh_list.get(0).getId());
                    rh_postion.setText(0 + "");
                }
            }
        });
        dc_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc_flag = 0;
                dc_true();
                dc_name.clear();
                dc_name.add(0,"新增");
                dc_adapter.notifyDataSetChanged();
                dc_company.setText("");
                dc_model.setText("");
                dc_sd_route.setText("");
                dc_file_prefix.setText("");
                dc_file_postfix.setText("");
            }
        });
        dc_button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dc_flag = 1;
                dc_true();
            }
        });
        dc_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = dc_hint.getText().toString();
                TrParameter trParameter = new TrParameter();
                trParameter.setId(id);
                List<TrParameter> trParameters = new ArrayList<TrParameter>();
                trParameters.add(trParameter);
                trParameterDao.deleteListData(trParameters);
                getData();
                dc_adapter.notifyDataSetChanged();
                int i = 0;
                if(!dc_postion.getText().toString().equals("")){
                    i = Integer.valueOf(dc_postion.getText().toString()).intValue();
                }
                if(i != dc_list.size()){
                    dc_company.setText(dc_list.get(i).getCompany());
                    dc_model.setText(dc_list.get(i).getModel());
                    dc_sd_route.setText(dc_list.get(i).getSdfileurl());
                    String fileprefix = (dc_list.get(i).getFileprefix() == null || dc_list.get(i).getFileprefix().equals("null")) ? "" : dc_list.get(i).getFileprefix();
                    String filesuffix = (dc_list.get(i).getFilesuffix() == null || dc_list.get(i).getFilesuffix().equals("null")) ? "" : dc_list.get(i).getFilesuffix();
                    dc_file_prefix.setText(fileprefix);
                    dc_file_postfix.setText(filesuffix);
                    dc_hint.setText(dc_list.get(i).getId());
                }
                deleteData(trParameter);
            }
        });
        rh_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rh_flag = 0;
                rh_true();
                rh_name.clear();
                rh_name.add(0,"新增");
                rh_adapter.notifyDataSetChanged();
                rh_company.setText("");
                rh_model.setText("");
                rh_sd_route.setText("");
                rh_file_prefix.setText("");
                rh_file_postfix.setText("");
            }
        });
        rh_button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rh_flag = 1;
                rh_true();
            }
        });
        rh_button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = rh_hint.getText().toString();
                TrParameter trParameter = new TrParameter();
                trParameter.setId(id);
                List<TrParameter> trParameters = new ArrayList<TrParameter>();
                trParameters.add(trParameter);
                trParameterDao.deleteListData(trParameters);
                getData();
                rh_adapter.notifyDataSetChanged();
                int i = 0;
                if(!rh_postion.getText().toString().equals("")){
                    i = Integer.valueOf(rh_postion.getText().toString()).intValue();
                }
                if(i != rh_list.size()){
                    rh_company.setText(rh_list.get(i).getCompany());
                    rh_model.setText(rh_list.get(i).getModel());
                    rh_sd_route.setText(rh_list.get(i).getSdfileurl());
                    String fileprefix = (rh_list.get(i).getFileprefix() == null || rh_list.get(i).getFileprefix().equals("null")) ? "" : rh_list.get(i).getFileprefix();
                    String filesuffix = (rh_list.get(i).getFilesuffix() == null || rh_list.get(i).getFilesuffix().equals("null")) ? "" : rh_list.get(i).getFilesuffix();
                    rh_file_prefix.setText(fileprefix);
                    rh_file_postfix.setText(filesuffix);
                    rh_hint.setText(rh_list.get(i).getId());
                }
                deleteData(trParameter);
            }
        });
        theme_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1 && msg.obj != null){
                getData();
                if(dc_list.size() > 0){
                    dc_adapter.notifyDataSetChanged();
                    dc_spinner.setSelection(0);
                    dc_company.setText(dc_list.get(0).getCompany());
                    dc_model.setText(dc_list.get(0).getModel());
                    dc_sd_route.setText(dc_list.get(0).getSdfileurl());
                    String fileprefix = (dc_list.get(0).getFileprefix() == null || dc_list.get(0).getFileprefix().equals("null")) ? "" : dc_list.get(0).getFileprefix();
                    String filesuffix = (dc_list.get(0).getFilesuffix() == null || dc_list.get(0).getFilesuffix().equals("null")) ? "" : dc_list.get(0).getFilesuffix();
                    dc_file_prefix.setText(fileprefix);
                    dc_file_postfix.setText(filesuffix);
                    dc_postion.setText(0 + "");
                }
                if(rh_list.size() > 0){
                    rh_adapter.notifyDataSetChanged();
                    rh_spinner.setSelection(0);
                    rh_company.setText(rh_list.get(0).getCompany());
                    rh_model.setText(rh_list.get(0).getModel());
                    rh_sd_route.setText(rh_list.get(0).getSdfileurl());
                    String fileprefix = (rh_list.get(0).getFileprefix() == null || rh_list.get(0).getFileprefix().equals("null")) ? "" : rh_list.get(0).getFileprefix();
                    String filesuffix = (rh_list.get(0).getFilesuffix() == null || rh_list.get(0).getFilesuffix().equals("null")) ? "" : rh_list.get(0).getFilesuffix();
                    rh_file_prefix.setText(fileprefix);
                    rh_file_postfix.setText(filesuffix);
                    rh_postion.setText(0 + "");
                }
            }
        }
    };

    public void getData(){
        dc_name.clear();
        rh_name.clear();
        dc_button_update.setBackgroundResource(R.drawable.affirm_button_sel);
        dc_button_update.setEnabled(true);
        dc_button_delete.setBackgroundResource(R.drawable.affirm_button_sel);
        dc_button_delete.setEnabled(true);
        rh_button_update.setBackgroundResource(R.drawable.affirm_button_sel);
        rh_button_update.setEnabled(true);
        rh_button_delete.setBackgroundResource(R.drawable.affirm_button_sel);
        rh_button_delete.setEnabled(true);
        TrParameter dc = new TrParameter();
        dc.setParametertype("1");
        dc_list = trParameterDao.queryTrParameterList(dc);
        for(int i = 0;i < dc_list.size() ; i++){
            dc_name.add(i,dc_list.get(i).getCompany() + " " + dc_list.get(i).getModel());
        }
        TrParameter rh = new TrParameter();
        rh.setParametertype("2");
        rh_list = trParameterDao.queryTrParameterList(rh);
        for(int i = 0;i < rh_list.size() ; i++){
            rh_name.add(i,rh_list.get(i).getCompany() + " " + rh_list.get(i).getModel());
        }
        if(dc_name.size() <= 0){
            dc_name.add(0,"无");
            dc_company.setText("");
            dc_model.setText("");
            dc_sd_route.setText("");
            dc_file_prefix.setText("");
            dc_file_postfix.setText("");
            dc_button_update.setBackgroundResource(R.drawable.affirm_button_enable);
            dc_button_update.setEnabled(false);
            dc_button_delete.setBackgroundResource(R.drawable.affirm_button_enable);
            dc_button_delete.setEnabled(false);
        }
        if(rh_name.size() <= 0){
            rh_name.add(0,"无");
            rh_company.setText("");
            rh_model.setText("");
            rh_sd_route.setText("");
            rh_file_prefix.setText("");
            rh_file_postfix.setText("");
            rh_button_update.setBackgroundResource(R.drawable.affirm_button_enable);
            rh_button_update.setEnabled(false);
            rh_button_delete.setBackgroundResource(R.drawable.affirm_button_enable);
            rh_button_delete.setEnabled(false);
        }
    }

    public void getDataWeb(){
        Map<String,Object> condMap = new HashMap<String, Object>();
        condMap.put("OBJ", "PARAMETER");
        condMap.put("OPELIST", null);
        condMap.put("TYPE", null);
        condMap.put("SIZE", 20);
        condMap.put("USERID", Constants.getLoginPerson(context).get("userId"));
        new DataSyschronized(SettingImageActivity.this).getDataFromWeb(condMap, handler);
    }

    private void updateData(String id,String company,String model,String sd_route,String file_prefix,String file_postfix,String userid,String date,String type){
        TrParameter trParameter = new TrParameter();
        trParameter.setId(id);
        trParameter.setParametertype(type);
        trParameter.setCompany(company);
        trParameter.setModel(model);
        trParameter.setSdfileurl(sd_route);
        trParameter.setFileprefix(file_prefix);
        trParameter.setFilesuffix(file_postfix);
        trParameter.setUpdateperson(userid);
        trParameter.setUpdatetime(date);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trParameter);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    private void deleteData(TrParameter trParameter){
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trParameter);
        list1.add(list2);
        sympMessageRealDao.deleteTextMessages(list1);
    }

    private void addData(TrParameter trParameter){
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trParameter);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
    }

    private void dc_true(){
        dc_sure.setVisibility(View.VISIBLE);
        dc_cancel.setVisibility(View.VISIBLE);
        dc_button_add.setVisibility(View.GONE);
        dc_button_update.setVisibility(View.GONE);
        dc_button_delete.setVisibility(View.GONE);
        dc_company.setEnabled(true);
        dc_model.setEnabled(true);
        dc_sd_route.setEnabled(true);
        dc_file_prefix.setEnabled(true);
        dc_file_postfix.setEnabled(true);
        dc_company.setBackgroundResource(R.color.card_background);
        dc_model.setBackgroundResource(R.color.card_background);
        dc_sd_route.setBackgroundResource(R.color.card_background);
        dc_file_prefix.setBackgroundResource(R.color.card_background);
        dc_file_postfix.setBackgroundResource(R.color.card_background);
    }

    private void rh_true(){
        rh_sure.setVisibility(View.VISIBLE);
        rh_cancel.setVisibility(View.VISIBLE);
        rh_button_add.setVisibility(View.GONE);
        rh_button_update.setVisibility(View.GONE);
        rh_button_delete.setVisibility(View.GONE);
        rh_company.setEnabled(true);
        rh_model.setEnabled(true);
        rh_sd_route.setEnabled(true);
        rh_file_prefix.setEnabled(true);
        rh_file_postfix.setEnabled(true);
        rh_company.setBackgroundResource(R.color.card_background);
        rh_model.setBackgroundResource(R.color.card_background);
        rh_sd_route.setBackgroundResource(R.color.card_background);
        rh_file_prefix.setBackgroundResource(R.color.card_background);
        rh_file_postfix.setBackgroundResource(R.color.card_background);
    }

    private void dc_false(){
        dc_sure.setVisibility(View.GONE);
        dc_cancel.setVisibility(View.GONE);
        dc_button_add.setVisibility(View.VISIBLE);
        dc_button_update.setVisibility(View.VISIBLE);
        dc_button_delete.setVisibility(View.VISIBLE);
        dc_company.setEnabled(false);
        dc_model.setEnabled(false);
        dc_sd_route.setEnabled(false);
        dc_file_prefix.setEnabled(false);
        dc_file_postfix.setEnabled(false);
        dc_company.setBackgroundResource(R.color.dividers);
        dc_model.setBackgroundResource(R.color.dividers);
        dc_sd_route.setBackgroundResource(R.color.dividers);
        dc_file_prefix.setBackgroundResource(R.color.dividers);
        dc_file_postfix.setBackgroundResource(R.color.dividers);
    }

    private void rh_false(){
        rh_sure.setVisibility(View.GONE);
        rh_cancel.setVisibility(View.GONE);
        rh_button_add.setVisibility(View.VISIBLE);
        rh_button_update.setVisibility(View.VISIBLE);
        rh_button_delete.setVisibility(View.VISIBLE);
        rh_company.setEnabled(false);
        rh_model.setEnabled(false);
        rh_sd_route.setEnabled(false);
        rh_file_prefix.setEnabled(false);
        rh_file_postfix.setEnabled(false);
        rh_company.setBackgroundResource(R.color.dividers);
        rh_model.setBackgroundResource(R.color.dividers);
        rh_sd_route.setBackgroundResource(R.color.dividers);
        rh_file_prefix.setBackgroundResource(R.color.dividers);
        rh_file_postfix.setBackgroundResource(R.color.dividers);
    }

    //判断EditText是否为空
    private boolean isEditTextNull(String string,EditText editText){
        boolean isNull = false;
        if ("".equals(string)) {
            Toast.makeText(getBaseContext(), "请输入" + editText.getTag() + "！", Toast.LENGTH_SHORT).show();
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            isNull = true;
        }
        return isNull;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
