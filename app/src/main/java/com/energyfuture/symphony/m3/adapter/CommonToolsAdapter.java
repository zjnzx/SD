package com.energyfuture.symphony.m3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.CommToolsActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrCommonTools;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备适配器
 */
public class CommonToolsAdapter extends BaseAdapter{
    private CommToolsActivity context;
    public List<TrCommonTools> trCommonToolsList;
    private String path = Constants.pathTemp.substring(0, Constants.pathTemp.length() - 1) + "legacy" + "/temp/";

    public CommonToolsAdapter(CommToolsActivity context, List<TrCommonTools> trCommonToolsList) {
        this.context = context;
        this.trCommonToolsList = trCommonToolsList;
    }

    @Override
    public int getCount() {
        return trCommonToolsList == null ? 0 : trCommonToolsList.size();
    }

    @Override
    public Object getItem(int position) {
        return trCommonToolsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TrCommonTools trCommonTools = trCommonToolsList.get(position);
        final MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comm_tool_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        final String toolName = trCommonTools.getFilename().substring(0,trCommonTools.getFilename().lastIndexOf("."));
        myViewholder.toolName.setText(toolName);
        if(toolName.equals("WORD")){
            myViewholder.toolIcon.setBackgroundResource(R.drawable.word);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("toolInstall",myViewholder.toolInstall);
            map.put("toolProgress",myViewholder.toolProgress);
            Constants.commToolMap.put("word",map);
        }else if(toolName.equals("PDF阅读器")){
            myViewholder.toolIcon.setBackgroundResource(R.drawable.pdfreader);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("toolInstall",myViewholder.toolInstall);
            map.put("toolProgress",myViewholder.toolProgress);
            Constants.commToolMap.put("pdf",map);
        }else{
            myViewholder.toolIcon.setVisibility(View.GONE);
            myViewholder.toolName.setVisibility(View.GONE);
            myViewholder.toolInstall.setVisibility(View.GONE);
        }

        if(position == trCommonToolsList.size() - 1){
            //下载工具
            context.openFileDownLoadApk();
        }

        String url = path + trCommonTools.getFilename();
        final File file = new File(url);

        setOnItemClick(toolName, myViewholder.toolInstall, myViewholder.toolProgress,file, trCommonTools, 1);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean b = Constants.isDownLoad;
                if(Constants.isDownLoad){
                    setOnItemClick(toolName, myViewholder.toolInstall, myViewholder.toolProgress,file, trCommonTools, 2);
                }else{
                    Toast.makeText(context,"正在下载,请稍候...",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    /**
     * 点击事件
     * @param toolName
     * @param toolInstall
     * @param file
     * @param type 1,设置是否安装  2,点击事件
     * @param trCommonTools
     */
    private void setOnItemClick(String toolName,TextView toolInstall,ProgressBar toolProgress,File file,TrCommonTools trCommonTools,int type){
        //本地文件大小
        Long length = file.length();
        String fileSize = trCommonTools.getFilesize();
        String len = "",fileSize2 = "";
        if(fileSize != null && !fileSize.equals("") && !fileSize.equals("null")){
            len = Constants.byteToM(Long.parseLong(fileSize)) + "M";
        }
        if(length != null){
            fileSize2 = String.valueOf(length);
        }
        if(toolName.equals("WORD")){
            //如果已安装
            if(Constants.isAppInstalled(context,"com.microsoft.office.word")){
                if(type == 1){
                    toolInstall.setText("(已安装 "+len+")");
                }else if(type == 2){
                    Toast.makeText(context,"应用已安装",Toast.LENGTH_SHORT).show();
                }
            }else if(!file.exists() || !fileSize.equals(fileSize2)){ //文件不存在或者不完整就下载
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    context.downLoadApk(trCommonTools,toolProgress,toolInstall);
                }
            }else{//存在就安装
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    Constants.isDownLoad = true;
                    Constants.openFile(context, file);
                }
            }
        }else if(toolName.equals("PDF阅读器")){
            //如果已安装
            if(Constants.isAppInstalled(context,"com.adobe.reader")){
                if(type == 1){
                    toolInstall.setText("(已安装 "+len+")");
                }else if(type == 2){
                    Toast.makeText(context,"应用已安装",Toast.LENGTH_SHORT).show();
                }
            }else if(!file.exists() || !fileSize.equals(fileSize2)){ //文件不存在或者不完整就下载
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    context.downLoadApk(trCommonTools,toolProgress,toolInstall);
                }
            }else{//存在就安装
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    Constants.isDownLoad = true;
                    Constants.openFile(context, file);
                }
            }
        }else{
            if(!file.exists() || !fileSize.equals(fileSize2)){ //文件不存在或者不完整就下载
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    context.downLoadApk(trCommonTools,toolProgress,toolInstall);
                }
            }else{//存在就安装
                if(type == 1){
                    toolInstall.setText("(未安装 "+len+")");
                }else if(type == 2){
                    Constants.isDownLoad = true;
                    Constants.openFile(context,file);
                }
            }
        }
    }

    public class MyViewholder{
        TextView toolName,toolInstall;
        ImageView toolIcon;
        ProgressBar toolProgress;

        public MyViewholder(View v) {
            toolName= (TextView) v.findViewById(R.id.comm_tool_name);
            toolIcon= (ImageView) v.findViewById(R.id.comm_tool_icon);
            toolInstall= (TextView) v.findViewById(R.id.comm_tool_install);
            toolProgress= (ProgressBar) v.findViewById(R.id.comm_tool_progress);
        }
    }
}
