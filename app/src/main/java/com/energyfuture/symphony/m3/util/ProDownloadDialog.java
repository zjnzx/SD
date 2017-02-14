package com.energyfuture.symphony.m3.util;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.ProjectListActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.adapter.ProDownloadAdapter;
import com.energyfuture.symphony.m3.analysis.ProjectDataSyschronized;
import com.energyfuture.symphony.m3.domain.TrProject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/30.
 */
public class ProDownloadDialog extends Dialog {
    private ProjectListActivity context;
    private ListView listView;
    private TextView none_data;
    private ProDownloadAdapter proDownloadAdapter;
    private Button cancel,selectall;
    public Button download;
    private List<TrProject> trProjectList;
    private Handler handler;

    public ProDownloadDialog(ProjectListActivity context, int theme,List<TrProject> trProjectList) {
        super(context, theme);
        this.context = context;
        this.trProjectList = trProjectList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_download_list);

        listView = (ListView)findViewById(R.id.list);
        cancel = (Button)findViewById(R.id.cancel);
        selectall = (Button)findViewById(R.id.selectall);
        download = (Button)findViewById(R.id.download);
        none_data = (TextView)findViewById(R.id.none_data);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                trProjectList = (List<TrProject>)msg.obj;
                context.menu_download_text.setText(trProjectList.size() + "");
                if(trProjectList.size() > 0 ){
                    none_data.setVisibility(View.GONE);
                    proDownloadAdapter = new ProDownloadAdapter(trProjectList, ProDownloadDialog.this);
                    listView.setAdapter(proDownloadAdapter);
                }else{
                    none_data.setText("暂无待下载项目");
                }
            }
        };

        getProDataFromWeb();
        buttonClick();
    }

    private void buttonClick(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProDownloadDialog.this.dismiss();
            }
        });
        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trProjectList != null && trProjectList.size() > 0){
                    for (int i = 0; i < trProjectList.size(); i++) {
                        ProDownloadAdapter.getIsSelected().put(i, true);
                    }
                    // 刷新listview和TextView的显示
                    download.setEnabled(true);
                    download.setBackgroundResource(R.drawable.affirm_button_sel);
                    dataChanged();
                }else{
                    Toast.makeText(context,"暂无可选项目",Toast.LENGTH_SHORT).show();
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                List<TrProject> downProjectList = new ArrayList<TrProject>();
                for (int i = 0; i < trProjectList.size(); i++) {
                    if(ProDownloadAdapter.getIsSelected().get(i)){
                        sb.append(trProjectList.get(i).getProjectid() + ",");
                        downProjectList.add(trProjectList.get(i));
                    }
                }
                ProDownloadDialog.this.dismiss();
                try {
                    context.downloadPro(downProjectList,sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString()+"/ProDownloadDialog");
                }
            }
        });
    }

    public void getProDataFromWeb(){
        none_data.setText("获取待下载项目信息中…");
        Map<String, Object> proCondMap = new HashMap<String, Object>();
        proCondMap.put("OBJ","PROJECT");
        proCondMap.put("TYPE",null);
        proCondMap.put("SIZE",20);
        proCondMap.put("USERID",Constants.getLoginPerson(context).get("userId"));
        new ProjectDataSyschronized(context).getProDataFromWeb(proCondMap, handler);
    }

    private void dataChanged() {
        // 通知listView刷新
        proDownloadAdapter.notifyDataSetChanged();
    };
}
