package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.Base;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.piclook.ImagePagerActivity;
import com.energyfuture.symphony.m3.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/12/22.
 */
public class ImageGetAdapter extends BaseAdapter{
    private Context context;
    private TrTask trTask;
    private String pic_route;
    private List<String> datalist=Constants.imagelist;
    private TrDetectionTempletFileObjDao fileObjDao;  //普通文件表
    private TrSpecialBushingFileDao bushingFileDao; //套管文件表
    private SympMessageRealDao sympMessageRealDao;  //发送消息
    public ImageGetAdapter(Context context, TrTask trTask, String pic_route) {
        this.context = context;
        this.trTask = trTask;
        this.pic_route = pic_route;

        fileObjDao=new TrDetectionTempletFileObjDao(context);  //普通文件表
        bushingFileDao=new TrSpecialBushingFileDao(context); //套管文件表
        sympMessageRealDao = new SympMessageRealDao(context);  //发送消息
    }

    public void setdata(){
        this.datalist=Constants.imagelist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String data=datalist.get(position);
        String number="";
        String id="";
        String type="";
        String fileName="";
        Viewholder viewholder;
        if(data!=null&&!data.equals("")){
            if(data.contains("|")){
                int first=data.indexOf("|");
                int second=data.indexOf("|",first+1);
                number=data.substring(0,first);
                id=data.substring(first+1,second);
                type=data.substring(second+1);
            }
        }
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.image_get_item,null);
            viewholder=new Viewholder(convertView);
            convertView.setTag(viewholder);
        }
            viewholder= (Viewholder) convertView.getTag();
            viewholder.input.setText(number);

           /* if(type.equals("general")){
                List<TrDetectiontempletFileObj> listdata = getGeneralDetectiontemplet(id, number); //查询DETECTIONOBJID
                if(listdata!=null&&listdata.size()>0){
                    detid=listdata.get(0).getDetectionobjid();
                    fileName=listdata.get(0).getFilename();
                }
            }else if(type.equals("bushing")){
                List<TrSpecialBushingFile> listdata = getPositionid(id, number); //查询POSITIONID
                if(listdata!=null&&listdata.size()!=0){
                    detid=listdata.get(0).getPositionid();
                    fileName=listdata.get(0).getFilename();
                }
            }*/

            if(Constants.datamap.size()!=0){
                if(Constants.datamap.get(number)!=null){
                    fileName=Constants.datamap.get(number);
                    File file = new File(pic_route + "small/" + fileName);
                    try{
                        viewholder.view.setImageBitmap(Base.revitionImageSize(file, 50));
                        viewholder.view.setBackgroundResource(R.drawable.image_square);
                    }catch (Exception e){
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, context, context.toString()+"/ImageGetAdapter");
                    }
                }
            }

            final Viewholder finalViewholder = viewholder;
            final String finalType = type;
            final String finalId = id;
            final String finalNumber=number;
            final String finalName=fileName;
            viewholder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Constants.isExistPic(pic_route + "small/",finalNumber,context)){
                        String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/original/";
                        Intent intent = new Intent(context, ImagePagerActivity.class);
                        intent.putExtra("index", getPositon(finalNumber));
                        intent.putExtra("url",url);
                        intent.putExtra("localurl",pic_route+"original");
                        intent.putExtra("type","imageshow");
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            viewholder.input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        String number= finalViewholder.input.getText().toString();
                        if(!number.equals("")){
                            finalViewholder.input.setText(number);
                            if(finalType.equals("general")){
                                if(!number.equals(finalNumber)){
                                    Map<Object,Object> columnsMap=new HashMap<Object, Object>();
                                    Map<Object,Object> wheresMap=new HashMap<Object, Object>();
                                    columnsMap.put("FILENUMBER",number);

                                    if(Constants.datamap.get(finalNumber)!=null){
                                        columnsMap.put("FILENAME","");
                                        columnsMap.put("FILEURL","");

                                        finalViewholder.view.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                        finalViewholder.view.setBackgroundResource(0);
                                        Constants.successcount--;
                                        delmapData(finalNumber,number);
                                    }
                                    wheresMap.put("ID",finalId);
                                    fileObjDao.updateTrDetectionTempletFileObjInfo(columnsMap,wheresMap);

                                    TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                                    trDetectiontempletFileObj.setFilenumber(number);
                                    trDetectiontempletFileObj.setFilename("");
                                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trDetectiontempletFileObj);
                                    list1.add(list2);
                                    sympMessageRealDao.addTextMessages(list1);

                                    datalist.remove(position);
                                    datalist.add(position,number+"|"+finalId+"|general");
                                    Constants.flag=true;
                                }
                            }else if(finalType.equals("bushing")){
                                if(!number.equals(finalNumber)){
                                    Map<Object,Object> columnsMap=new HashMap<Object, Object>();
                                    Map<Object,Object> wheresMap=new HashMap<Object, Object>();
                                    columnsMap.put("FILENUMBER",number);
                                    if(Constants.datamap.get(finalNumber)!=null){
                                        columnsMap.put("FILENAME","");
                                        columnsMap.put("FILEURL","");
                                        finalViewholder.view.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                        finalViewholder.view.setBackgroundResource(0);
                                        Constants.successcount--;
                                        delmapData(finalNumber,number);
                                    }
                                    wheresMap.put("ID",finalId);
                                    bushingFileDao.updateTrSpecialBushingFileInfo(columnsMap,wheresMap);

                                    TrSpecialBushingFile trSpecialButshingFile = new TrSpecialBushingFile();
                                    trSpecialButshingFile.setFilenumber(number);
                                    trSpecialButshingFile.setFilename("");
                                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trSpecialButshingFile);
                                    list1.add(list2);
                                    sympMessageRealDao.addTextMessages(list1);

                                    datalist.remove(position);
                                    datalist.add(position,number+"|"+finalId+"|bushing");
                                    Constants.flag=true;
                                }
                            }
                        }
                    }
                }
            });
            return convertView;
        }

    private void delmapData(String oldnumber,String newnumber) {
        Map<String,String> datamap=new LinkedHashMap<>();
        for (Map.Entry<String,String> entry : Constants.datamap.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();

            if(key.equals(oldnumber)){
                datamap.put(newnumber,"");
            }else{
                datamap.put(key,value);
            }
        }
        Constants.datamap=datamap;
    }

    private int getPositon(String finalNumber) {
        int position=0;
        for (Map.Entry<String,String> entry : Constants.datamap.entrySet()){
            String number=entry.getKey();
            if(number.equals(finalNumber)){
                return position;
            }
            position++;
        }
        return 0;
    }

    private List<TrDetectiontempletFileObj> getGeneralDetectiontemplet(String id,String number) {
        fileObjDao=new TrDetectionTempletFileObjDao(context);
        TrDetectiontempletFileObj fileObjParam=new TrDetectiontempletFileObj();
        fileObjParam.setId(id);
        fileObjParam.setFilenumber(number);
        List<TrDetectiontempletFileObj> list = fileObjDao.queryTrDetectionTempletFileObjList(fileObjParam);
        if(list.size()!=0){
            return  list;
        }else{
            return null;
        }
    }

    private List<TrSpecialBushingFile> getPositionid(String id, String number) {
        bushingFileDao=new TrSpecialBushingFileDao(context);
        TrSpecialBushingFile bushingFileParam=new TrSpecialBushingFile();
        bushingFileParam.setId(id);
        bushingFileParam.setFilenumber(number);
        List<TrSpecialBushingFile> list = bushingFileDao.queryTrSpecialBushingFileList(bushingFileParam);
        if(list.size()!=0){
            return list;
        }else{
            return null;
        }
    }

    class Viewholder{
        EditText input;
        ImageView view;
        ProgressBar progressBar;
        Viewholder(View convertView) {
           input= (EditText) convertView.findViewById(R.id.image_get_item_number);
           view= (ImageView) convertView.findViewById(R.id.image_get_item_show);
           progressBar= (ProgressBar) convertView.findViewById(R.id.image_get_item_progressbar);
        }
    }
}
