package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletDataObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordDao;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordGroupDao;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordValueDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingPositionDao;
import com.energyfuture.symphony.m3.dao.TrSpecialOliDataDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletDataObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrNoiseRecord;
import com.energyfuture.symphony.m3.domain.TrNoiseRecordGroup;
import com.energyfuture.symphony.m3.domain.TrSpecialBushing;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingPosition;
import com.energyfuture.symphony.m3.domain.TrSpecialOliData;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.piclook.ImagePagerActivity;
import com.energyfuture.symphony.m3.ui.GridViewforListView;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/11.
 * 采集卡适配器
 */
public class OperationItemAdaperOne extends BaseAdapter{
    private OperationItemActivity context;
    private List<TrDetectiontypEtempletObj> list;
    private String userid;
    private SympMessageRealDao sympMessageRealDao;
    private Handler handler;
    private String pic_route;
    private MaterialDialog methodMaterialDialog,standardMaterialDialog,dialog,mMaterialDialog;
    private TrDetectiontypEtempletObj trDetectiontypEtempletObjOne;
    private int color,green,orange;
    private TrNoiseRecordDao trNoiseRecordDao;
    private TrNoiseRecordValueDao trNoiseRecordValueDao;
    private TrNoiseRecordGroupDao trNoiseRecordGroupDao;
    Map<String,String> idMap = new HashMap<String,String>();
    private String firstid; //
    String text2 = "",text3 = "";
    private int express = 0;
    private TrSpecialBushingFileDao trSpecialBushingFileDao;
    private TrDetectionTempletFileObjDao trDetectionTempletFileObjDao;
    private TrTask trTask;
    private List<List<TrDetectiontempletObj>> dataList;
    private Map<Integer,Integer> points = new HashMap<Integer,Integer>();

    private ImageView firstImageView;
    private EditText firstEditText;
    private TextView firstTextView;
    private MaterialDialog photodialog;
    private List<TrDetectiontempletFileObj> firstFileList;
    private boolean flag=false;
    public OperationItemAdaperOne(Handler handler,Context context,List<List<TrDetectiontempletObj>> dataList, List<TrDetectiontypEtempletObj> data,String pic_route,TrDetectiontypEtempletObj trDetectiontypEtempletObjOne,TrTask trTask) {
        this.handler=handler;
        this.context = (OperationItemActivity)context;
        this.list = data;
        this.pic_route = pic_route;
        this.trDetectiontypEtempletObjOne = trDetectiontypEtempletObjOne;
        this.firstid = trDetectiontypEtempletObjOne.getId();
        this.trTask=trTask;
        this.dataList=dataList;

        //如果有共同的图片,给list的第一个位置添加一条数据占位显示图片
        firstFileList = getFile2(firstid);
        if(firstFileList.size() > 0||!trDetectiontypEtempletObjOne.getPiccount().equals("0")){
            this.list.add(0,data.get(0));
            List<TrDetectiontempletObj> list2 = new ArrayList<TrDetectiontempletObj>();
            if(data.size()!=0&&dataList.get(0).size()!=0){
                list2.add(dataList.get(0).get(0));
            }
            this.dataList.add(0,list2);
        }

        color = this.context.getResources().getColor(R.color.rd_null_checked);
        green = this.context.getResources().getColor(R.color.rd_bc_checked);
        orange = this.context.getResources().getColor(R.color.rd_no_checked);
        userid = Constants.getLoginPerson(context).get("userId");
        sympMessageRealDao = new SympMessageRealDao(context);
        trNoiseRecordDao = new TrNoiseRecordDao(context);
        trNoiseRecordValueDao = new TrNoiseRecordValueDao(context);
        trNoiseRecordGroupDao = new TrNoiseRecordGroupDao(context);
        trSpecialBushingFileDao = new TrSpecialBushingFileDao(context);
        trDetectionTempletFileObjDao = new TrDetectionTempletFileObjDao(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TrDetectiontypEtempletObj data = list.get(position);
        List<TrDetectiontempletFileObj> pfilelist = null;
        MyviewHolder myviewHolder = null;
/*        if (convertView == null) {*/
        if (data.getTemplettype().equals("general")) {
            if(position==0){
                firstFileList = getFile2(firstid);
                if(firstFileList.size()!=0||!trDetectiontypEtempletObjOne.getPiccount().equals("0")){ //设置第一条为图片的采集卡
                    convertView=LayoutInflater.from(context).inflate(R.layout.first_capture_card_item,null);
                    myviewHolder=new MyviewHolder(convertView,5,null);
                    getViewforFirst(myviewHolder,firstFileList.size(),firstFileList,data,trDetectiontypEtempletObjOne);
                    return convertView;
                }
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.general_item, null);
            myviewHolder = new MyviewHolder(convertView, 1,mItemClickListener);

        }else if (data.getTemplettype().equals("work")) {
            convertView = LayoutInflater.from(context).inflate(R.layout.general_item, null);
            myviewHolder = new MyviewHolder(convertView, 1,null);
        }else if (data.getTemplettype().equals("oillevel")) {//油位指示
            convertView = LayoutInflater.from(context).inflate(R.layout.oillevel_item, null);
            myviewHolder = new MyviewHolder(convertView, 3,mItemClickListener);
        }else if (data.getTemplettype().equals("noise")) {//噪声
            convertView = LayoutInflater.from(context).inflate(R.layout.noise_item, null);
            myviewHolder = new MyviewHolder(convertView, 6,mItemClickListener);
        }

//        TrDetectionTempletObjDao trDetectionTempletObjDao=new TrDetectionTempletObjDao(context);
//        final List<TrDetectiontempletObj> objList = trDetectionTempletObjDao.getTrDetectiontempletObj(data);
        if (data.getTemplettype().equals("general")) {
            if(dataList.size()!=0&&dataList.get(position).size()!=0){
                final TrDetectiontempletObj trDetectiontempletObj = dataList.get(position).get(0);
                final List<TrDetectiontempletDataObj> list=getTrDetectionTempletData(trDetectiontempletObj.getId());

                String method = (trDetectiontempletObj.getDetectionmethod()!= null && !("null").equals(trDetectiontempletObj.getDetectionmethod()))?trDetectiontempletObj.getDetectionmethod() : "方法";
                if(method.length()<=4){
                    myviewHolder.method.setText(method);
                }else{
                    myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        createMethodeDialog(trDetectiontempletObj,null,1);
                        }
                    });
                }
                myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createStandardDialog(trDetectiontempletObj,null,1);
                    }
                });

                if(list.size()>0){
                    GridViewAdapter gridViewAdapter=new GridViewAdapter(context,list,trTask);
                    myviewHolder.gridView.setVisibility(View.VISIBLE);
                    myviewHolder.gridView.setAdapter(gridViewAdapter);
                }
                updateGeneral(myviewHolder.result,myviewHolder.remarkinput,myviewHolder.checkYes,myviewHolder.checkNo,null,trDetectiontempletObj,data,null,null,null,null,null,position);
            }else{
                insertGeneral(myviewHolder.result, myviewHolder.remarkinput, myviewHolder.checkYes, myviewHolder.checkNo,null,data);
            }

            List<TrDetectiontempletFileObj> filelist = getFileItem(data);
            pfilelist = getFile2(firstid);
            //如果父节点没有图片就显示子节点图片
            if(pfilelist == null || pfilelist.size() == 0){
                if(filelist.size() >0||!data.getPiccount().equals("0")){
                    getViewforcommon(filelist.size(), myviewHolder,filelist,data);  //显示图片
                }else{
                    myviewHolder.view.setVisibility(View.GONE);
                    myviewHolder.general_hlistview.setVisibility(View.GONE);
                }
            }else{
                myviewHolder.view.setVisibility(View.GONE);
                myviewHolder.general_hlistview.setVisibility(View.GONE);
            }
            myviewHolder.textView.setText(data.getDetectionname());
            myviewHolder.general_item_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setTitle("确认删除该检测点吗？")
                            .setMessage("")
                            .setPositiveButton(
                                    "确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                            columnsMap.put("STATUS","2");
                                            Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                            wheresMap.put("ID", data.getId());
                                            TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
                                            trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);
                                            final View viewParent1 = (View) v.getParent();
                                            final View viewParent2 = (View) viewParent1.getParent();
                                            viewParent2.setVisibility(View.GONE);
                                            TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
                                            trDetectiontypEtempletObj.setStatus("2");
                                            trDetectiontypEtempletObj.setId(data.getId());
                                            List<List<Object>> list1 = new ArrayList<List<Object>>();
                                            List<Object> list2 = new ArrayList<Object>();
                                            list2.add(trDetectiontypEtempletObj);
                                            list1.add(list2);
                                            sympMessageRealDao.updateTextMessages(list1);
                                            OperationItemAdaperOne.this.list.remove(position);
                                            OperationItemAdaperOne.this.dataList = getDetectiontypEtempletObj(trDetectiontypEtempletObjOne);
                                            OperationItemAdaperOne.this.notifyDataSetChanged();
                                            mMaterialDialog.dismiss();
                                            context.setStatus(trTask.getTaskid(),data.getPid());
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    "取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mMaterialDialog.dismiss();
                                        }
                                    }
                            )
                            .setCanceledOnTouchOutside(false)
                            .setOnDismissListener(
                                    new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                        }
                                    }
                            )
                            .show();
                }
            });
        }else if (data.getTemplettype().equals("work")) {
            myviewHolder.textView.setText(data.getDetectionname());
        }else if (data.getTemplettype().equals("noise")) {
            myviewHolder.noise_textView.setText(data.getDetectionname());
            if(dataList.get(position).size()!=0){
                String detecMethod = dataList.get(position).get(0).getDetectionmethod();
                String method = (detecMethod!= null && !("null").equals(detecMethod))?dataList.get(position).get(0).getDetectionmethod() : "方法";
                if(method.length()<=4){
                    myviewHolder.method.setText(method);
                }else{
                    myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createMethodeDialog(dataList.get(position).get(0),null,1);
                        }
                    });
                }
            }
            myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createStandardDialog(dataList.get(position).get(0),null,1);
                }
            });

            if(dataList.size()!=0&&dataList.get(position).size()!=0){
                final TrDetectiontempletObj trDetectiontempletObj = dataList.get(position).get(0);
                    List<TrNoiseRecord> trNoiseRecordList = new ArrayList<TrNoiseRecord>();
                    TrNoiseRecord trNoiseRecord = new TrNoiseRecord();
                    trNoiseRecord.setDetectionobjid(trDetectiontempletObj.getId());
                    trNoiseRecordList = trNoiseRecordDao.queryTrNoiseRecordList(trNoiseRecord);
                    List<TrNoiseRecordGroup> trNoiseRecordGroups = new ArrayList<TrNoiseRecordGroup>();
                    if(trNoiseRecordList != null && trNoiseRecordList.size() > 0){
                        TrNoiseRecordGroup trNoiseRecordGroup = new TrNoiseRecordGroup();
                        trNoiseRecordGroup.setNoiseid(trNoiseRecordList.get(0).getId());
                        //查询变压器噪声声级测量记录分组表
                        trNoiseRecordGroups = trNoiseRecordGroupDao.queryTrNoiseRecordGroupList(trNoiseRecordGroup);
                        NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                        myviewHolder.noise_gridview_group.setAdapter(noiseGroupAdapter);
                        noiseGroupAdapter.notifyDataSetChanged();

                        myviewHolder.noise_item_groupcount.setText(trNoiseRecordList.get(0).getDcfan().equals("null") ? "":trNoiseRecordList.get(0).getDcfan());
                        myviewHolder.noise_item_number.setText(trNoiseRecordList.get(0).getOilpump().equals("null") ? "":trNoiseRecordList.get(0).getOilpump());
                        myviewHolder.noise_avg.setText(trNoiseRecordList.get(0).getNoiseavg().equals("null") ? "":trNoiseRecordList.get(0).getNoiseavg());

                        final List<TrNoiseRecord> finalTrNoiseRecordList = trNoiseRecordList;
                        final MyviewHolder finalMyviewHolder = myviewHolder;
                        myviewHolder.noise_item_groupcount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(!hasFocus){
                                    String dcfan = finalMyviewHolder.noise_item_groupcount.getText().toString();
                                    if(dcfan != finalTrNoiseRecordList.get(0).getDcfan()){
                                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("DCFAN",dcfan);
                                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID",finalTrNoiseRecordList.get(0).getId());
                                        trNoiseRecordDao.updateTrNoiseRecordInfo(columnsMap,wheresMap);
                                        TrNoiseRecord trNoiseRecord1 = new TrNoiseRecord();
                                        trNoiseRecord1.setDcfan(dcfan);
                                        trNoiseRecord1.setId(finalTrNoiseRecordList.get(0).getId());
                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trNoiseRecord1);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);
                                    }
                                }
                            }
                        });
                        myviewHolder.noise_item_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(!hasFocus){
                                    String oilpump = finalMyviewHolder.noise_item_number.getText().toString();
                                    if(oilpump != finalTrNoiseRecordList.get(0).getDcfan()){
                                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("OILPUMP",oilpump);
                                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID",finalTrNoiseRecordList.get(0).getId());
                                        trNoiseRecordDao.updateTrNoiseRecordInfo(columnsMap,wheresMap);
                                        TrNoiseRecord trNoiseRecord1 = new TrNoiseRecord();
                                        trNoiseRecord1.setOilpump(oilpump);
                                        trNoiseRecord1.setId(finalTrNoiseRecordList.get(0).getId());
                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trNoiseRecord1);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);
                                    }
                                }
                            }
                        });
                        myviewHolder.noise_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if(!hasFocus){
                                    String noiseavg = finalMyviewHolder.noise_avg.getText().toString();
                                    if(noiseavg != finalTrNoiseRecordList.get(0).getDcfan()){
                                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("NOISEAVG",noiseavg);
                                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID",finalTrNoiseRecordList.get(0).getId());
                                        trNoiseRecordDao.updateTrNoiseRecordInfo(columnsMap,wheresMap);
                                        TrNoiseRecord trNoiseRecord1 = new TrNoiseRecord();
                                        trNoiseRecord1.setNoiseavg(noiseavg);
                                        trNoiseRecord1.setId(finalTrNoiseRecordList.get(0).getId());
                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trNoiseRecord1);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);
                                    }
                                }
                            }
                        });
                    }
                updateGeneral(myviewHolder.noise_result, myviewHolder.noise_remarkinput, myviewHolder.noise_checkYes, myviewHolder.noise_checkNo, myviewHolder.noise_item_ll, trDetectiontempletObj, data,myviewHolder.noise_gridview_group,myviewHolder.noise_item_groupcount,myviewHolder.noise_item_number,myviewHolder.noise_avg,trNoiseRecordGroups,position);
            }else{
                insertGeneral(myviewHolder.noise_result,myviewHolder.noise_remarkinput,myviewHolder.noise_checkYes,myviewHolder.noise_checkNo,myviewHolder.noise_item_ll,data);
            }
        }else if (data.getTemplettype().equals("oillevel")) {
            myviewHolder.textView.setText(data.getDetectionname());
            List<TrSpecialBushing> trSpecialBushingList = getbushing(data.getId());
            for(final TrSpecialBushing trSpecialBushing : trSpecialBushingList){

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    myviewHolder.result.setText(trSpecialBushing.getResultdescribe().equals("null") ? " " : trSpecialBushing.getResultdescribe());
                    myviewHolder.remarkinput.setText(trSpecialBushing.getRemarks().equals("null") ? " " : trSpecialBushing.getRemarks());
                }else{
                    myviewHolder.result.setText(trSpecialBushing.getResultdescribe().equals("null") ? "" : trSpecialBushing.getResultdescribe());
                    myviewHolder.remarkinput.setText(trSpecialBushing.getRemarks().equals("null") ? "" : trSpecialBushing.getRemarks());
                }


                String method = (trSpecialBushing.getDetectionmethod()!= null && !("null").equals(trSpecialBushing.getDetectionmethod()))?trSpecialBushing.getDetectionmethod():"方法";
                if(method.length()<=4){
                    myviewHolder.method.setText(method);
                }else{
                    myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        createMethodeDialog(null,trSpecialBushing,2);
                        }
                    });
                }
                myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createStandardDialog(null,trSpecialBushing,2);
                    }
                });

                if(trSpecialBushing.getStatus() != null && trSpecialBushing.getStatus().equals("0")){
                    myviewHolder.checkNo.setChecked(true);
                    myviewHolder.checkNo.setTextColor(orange);
                    myviewHolder.checkYes.setChecked(false);
                    myviewHolder.checkYes.setTextColor(color);
                }
                if(trSpecialBushing.getStatus() != null && trSpecialBushing.getStatus().equals("1")){
                    myviewHolder.checkYes.setChecked(true);
                    myviewHolder.checkYes.setTextColor(green);
                    myviewHolder.checkNo.setChecked(false);
                    myviewHolder.checkNo.setTextColor(color);
                }
                List<TrSpecialBushingPosition> trSpecialBushingPositionList = getBushingData(trSpecialBushing.getId());
                for (TrSpecialBushingPosition trSpecialBushingPosition :trSpecialBushingPositionList){
                    List<TrSpecialBushingFile> trSpecialBushingFileList = getbushingFile(trSpecialBushingPosition.getId());
                    String filenumber ="";
                    if(trSpecialBushingFileList.size() > 0){
                        filenumber = trSpecialBushingFileList.get(0).getFilenumber().equals("null") ? "" : trSpecialBushingFileList.get(0).getFilenumber();
                    }
                    if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                        if(filenumber.equals("")){
                            filenumber=" ";
                        }
                    }
                    if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("高压")){
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HA")){
                            if(trSpecialBushingFileList.size()!=0){
                                    myviewHolder.ha_image_camera.setVisibility(View.GONE);
                                    myviewHolder.ha_rl.setVisibility(View.VISIBLE);
                                    myviewHolder.oil_ha_index.setText(filenumber);
                                    bushingFile(myviewHolder.oil_ha_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_ha_bar);
                                    setOnfocuslistenerBushing(myviewHolder.oil_ha_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_ha_image);
                                    delbusingFile(myviewHolder.ha_image_delete,myviewHolder.oil_ha_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.ha_image_delete,myviewHolder.oil_ha_image,myviewHolder.ha_image_camera,myviewHolder.oil_ha_index,myviewHolder.ha_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HB")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.hb_image_camera.setVisibility(View.GONE);
                                myviewHolder.hb_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_hb_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_hb_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_hb_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_hb_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_hb_image);
                                delbusingFile(myviewHolder.hb_image_delete,myviewHolder.oil_hb_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.hb_image_delete,myviewHolder.oil_hb_image,myviewHolder.hb_image_camera,myviewHolder.oil_hb_index,myviewHolder.hb_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HC")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.hc_image_camera.setVisibility(View.GONE);
                                myviewHolder.hc_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_hc_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_hc_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_hc_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_hc_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_hc_image);
                                delbusingFile(myviewHolder.hc_image_delete,myviewHolder.oil_hc_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.hc_image_delete,myviewHolder.oil_hc_image,myviewHolder.hc_image_camera,myviewHolder.oil_hc_index,myviewHolder.hc_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("H0")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.ho_image_camera.setVisibility(View.GONE);
                                myviewHolder.ho_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_ho_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_ho_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_ho_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_ho_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_ho_image);
                                delbusingFile(myviewHolder.ho_image_delete,myviewHolder.oil_ho_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.ho_image_delete,myviewHolder.oil_ho_image,myviewHolder.ho_image_camera,myviewHolder.oil_ho_index,myviewHolder.ho_rl,trSpecialBushingPosition);
                            }
                        }
                    }
                    if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("中压")){
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MA")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.ma_image_camera.setVisibility(View.GONE);
                                myviewHolder.ma_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_ma_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_ma_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_ma_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_ma_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_ma_image);
                                delbusingFile(myviewHolder.ma_image_delete,myviewHolder.oil_ma_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.ma_image_delete,myviewHolder.oil_ma_image,myviewHolder.ma_image_camera,myviewHolder.oil_ma_index,myviewHolder.ma_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MB")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.mb_image_camera.setVisibility(View.GONE);
                                myviewHolder.mb_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_mb_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_mb_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_mb_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_mb_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_mb_image);
                                delbusingFile(myviewHolder.mb_image_delete,myviewHolder.oil_mb_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.mb_image_delete,myviewHolder.oil_mb_image,myviewHolder.mb_image_camera,myviewHolder.oil_mb_index,myviewHolder.mb_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MC")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.mc_image_camera.setVisibility(View.GONE);
                                myviewHolder.mc_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_mc_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_mc_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_mc_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_mc_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_mc_image);
                                delbusingFile(myviewHolder.mc_image_delete,myviewHolder.oil_mc_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.mc_image_delete,myviewHolder.oil_mc_image,myviewHolder.mc_image_camera,myviewHolder.oil_mc_index,myviewHolder.mc_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("M0")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.mo_image_camera.setVisibility(View.GONE);
                                myviewHolder.mo_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_mo_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_mo_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_mo_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_mo_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_mo_image);
                                delbusingFile(myviewHolder.mo_image_delete,myviewHolder.oil_mo_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.mo_image_delete,myviewHolder.oil_mo_image,myviewHolder.mo_image_camera,myviewHolder.oil_mo_index,myviewHolder.mo_rl,trSpecialBushingPosition);
                            }
                        }
                    }
                    if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("低压")){
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("La")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.la_image_camera.setVisibility(View.GONE);
                                myviewHolder.la_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_la_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_la_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_la_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_la_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_la_image);
                                delbusingFile(myviewHolder.la_image_delete,myviewHolder.oil_la_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.la_image_delete,myviewHolder.oil_la_image,myviewHolder.la_image_camera,myviewHolder.oil_la_index,myviewHolder.la_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("Lb")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.lb_image_camera.setVisibility(View.GONE);
                                myviewHolder.lb_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_lb_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_lb_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_lb_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_lb_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_lb_image);
                                delbusingFile(myviewHolder.lb_image_delete,myviewHolder.oil_lb_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.lb_image_delete,myviewHolder.oil_lb_image,myviewHolder.lb_image_camera,myviewHolder.oil_lb_index,myviewHolder.lb_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("Lc")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.lc_image_camera.setVisibility(View.GONE);
                                myviewHolder.lc_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_lc_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_lc_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_lc_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_lc_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_lc_image);
                                delbusingFile(myviewHolder.lc_image_delete,myviewHolder.oil_lc_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.lc_image_delete,myviewHolder.oil_lc_image,myviewHolder.lc_image_camera,myviewHolder.oil_lc_index,myviewHolder.lc_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("L0")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.lo_image_camera.setVisibility(View.GONE);
                                myviewHolder.lo_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_lo_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_lo_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_lo_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_lo_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_lo_image);
                                delbusingFile(myviewHolder.lo_image_delete,myviewHolder.oil_lo_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.lo_image_delete,myviewHolder.oil_lo_image,myviewHolder.lo_image_camera,myviewHolder.oil_lo_index,myviewHolder.lo_rl,trSpecialBushingPosition);
                            }
                        }
                    }
                    if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("稳压")){
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("s1")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.s1_image_camera.setVisibility(View.GONE);
                                myviewHolder.s1_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_s1_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_s1_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_s1_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_s1_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_s1_image);
                                delbusingFile(myviewHolder.s1_image_delete,myviewHolder.oil_s1_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.s1_image_delete,myviewHolder.oil_s1_image,myviewHolder.s1_image_camera,myviewHolder.oil_s1_index,myviewHolder.s1_rl,trSpecialBushingPosition);
                            }
                        }
                        if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("s2")){
                            if(trSpecialBushingFileList.size()!=0){
                                myviewHolder.s2_image_camera.setVisibility(View.GONE);
                                myviewHolder.s2_rl.setVisibility(View.VISIBLE);
                                myviewHolder.oil_s2_index.setText(filenumber);
                                bushingFile(myviewHolder.oil_s2_image,trSpecialBushingFileList.get(0),trSpecialBushingPosition,myviewHolder.oil_s2_bar);
                                setOnfocuslistenerBushing(myviewHolder.oil_s2_index,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.oil_s2_image);
                                delbusingFile(myviewHolder.s2_image_delete,myviewHolder.oil_s2_image);
                            }else{
                                //拍照
                                takePhotoBush(myviewHolder.s2_image_delete,myviewHolder.oil_s2_image,myviewHolder.s2_image_camera,myviewHolder.oil_s2_index,myviewHolder.s2_rl,trSpecialBushingPosition);
                            }
                        }
                    }
                }
                final MyviewHolder finalMyviewHolder1 = myviewHolder;
                finalMyviewHolder1.result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus){
                            String result = finalMyviewHolder1.result.getText().toString();
                            if(!trSpecialBushing.getResultdescribe().equals(result)){
                                String date = Constants.dateformat2.format(new Date());
                                Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                                columnsMap.put("RESULTDESCRIBE",result);
                                columnsMap.put("UPDATETIME", date);
                                columnsMap.put("UPDATEPERSON",userid);
                                Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                                wheresMap.put("ID", trSpecialBushing.getId());
                                TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap,wheresMap);
                                TrSpecialBushing trSpecialBushing1 = new TrSpecialBushing();
                                trSpecialBushing1.setResultdescribe(result);
                                trSpecialBushing1.setUpdatetime(date);
                                trSpecialBushing1.setUpdateperson(userid);
                                trSpecialBushing1.setId(trSpecialBushing.getId());
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trSpecialBushing1);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                            }
                        }
                    }
                });
                finalMyviewHolder1.remarkinput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            String remark = finalMyviewHolder1.remarkinput.getText().toString();
                            if (!trSpecialBushing.getRemarks().equals(remark)) {
                                String date = Constants.dateformat2.format(new Date());
                                Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                columnsMap.put("REMARKS", remark);
                                columnsMap.put("UPDATETIME", date);
                                columnsMap.put("UPDATEPERSON", userid);
                                Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                wheresMap.put("ID", trSpecialBushing.getId());
                                TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                                TrSpecialBushing trSpecialBushing1 = new TrSpecialBushing();
                                trSpecialBushing1.setRemarks(remark);
                                trSpecialBushing1.setUpdatetime(date);
                                trSpecialBushing1.setUpdateperson(userid);
                                trSpecialBushing1.setId(trSpecialBushing.getId());
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trSpecialBushing1);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                            }
                        }
                    }
                });
                finalMyviewHolder1.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked == true){
                            finalMyviewHolder1.checkYes.setTextColor(green);
                            finalMyviewHolder1.checkNo.setChecked(false);
                            finalMyviewHolder1.checkNo.setTextColor(color);
                            String date = Constants.dateformat2.format(new Date());
                            Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                            columnsMap.put("STATUS","1");
                            columnsMap.put("UPDATETIME", date);
                            columnsMap.put("UPDATEPERSON",userid);
                            Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                            wheresMap.put("ID", trSpecialBushing.getId());
                            TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                            trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                            TrSpecialBushing trSpecialBushing1 = new TrSpecialBushing();
                            trSpecialBushing1.setStatus("1");
                            trSpecialBushing1.setUpdatetime(date);
                            trSpecialBushing1.setUpdateperson(userid);
                            trSpecialBushing1.setId(trSpecialBushing.getId());
                            List list1 = new ArrayList<>();
                            List<Object> list2 = new ArrayList<Object>();
                            list2.add(trSpecialBushing1);
                            list1.add(list2);
                            sympMessageRealDao.updateTextMessages(list1);
                            updateStatus(data,"1");
                        }else{
                            finalMyviewHolder1.checkYes.setTextColor(color);
                        }
                    }
                });
                finalMyviewHolder1.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked == true){
                            finalMyviewHolder1.checkNo.setTextColor(orange);
                            finalMyviewHolder1.checkYes.setChecked(false);
                            finalMyviewHolder1.checkYes.setTextColor(color);
                            String date = Constants.dateformat2.format(new Date());
                            Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                            columnsMap.put("STATUS","0");
                            columnsMap.put("UPDATETIME", date);
                            columnsMap.put("UPDATEPERSON",userid);
                            Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                            wheresMap.put("ID", trSpecialBushing.getId());
                            TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                            trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                            TrSpecialBushing trSpecialBushing1 = new TrSpecialBushing();
                            trSpecialBushing1.setStatus("0");
                            trSpecialBushing1.setUpdatetime(date);
                            trSpecialBushing1.setUpdateperson(userid);
                            trSpecialBushing1.setId(trSpecialBushing.getId());
                            List list1 = new ArrayList<>();
                            List<Object> list2 = new ArrayList<Object>();
                            list2.add(trSpecialBushing1);
                            list1.add(list2);
                            sympMessageRealDao.updateTextMessages(list1);
                            updateStatus(data,"0");
                        }else{
                            finalMyviewHolder1.checkNo.setTextColor(color);
                        }
                    }
                });
            }
        }else if (data.getTemplettype().equals("oli")) {
            List<TrSpecialOliData> oildata = getOildata(data);
            myviewHolder.listView.setAdapter(new OilListAdapter(context,oildata,trTask,data));
        }
        if(position==list.size()-1){
            flag=true;
        }
        return convertView;
    }

    /**
     * 套管拍照
     */
    private void takePhotoBush(final LinearLayout del,final ImageView imageViewShow, final LinearLayout imageViewCamera, final EditText editText, final RelativeLayout imageshow,final TrSpecialBushingPosition position){
        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uuid = Constants.getUuid();
                String fileNumber = "";
                if(!("").equals(Utils.number)){
                    if(Constants.state==1){
                        fileNumber = Constants.formatNum();
                    }
                }
                List<TrSpecialBushingFile> bushingFileList=new ArrayList<TrSpecialBushingFile>();
                TrSpecialBushingFile bushingFile=new TrSpecialBushingFile();
                bushingFile.setId(uuid);
                bushingFile.setPositionid(position.getId());
                bushingFile.setFilenumber(fileNumber);
                bushingFile.setTaskid(trTask.getTaskid());
                bushingFile.setProjectid(trTask.getProjectid());
                bushingFile.setIsupload("1");
                bushingFileList.add(bushingFile);
                TrSpecialBushingFileDao bushingFileDao=new TrSpecialBushingFileDao(context);
                bushingFileDao.insertListData(bushingFileList);

                List<List<Object>> list1 = new ArrayList<List<Object>>();
                List<Object> list2 = new ArrayList<Object>();
                list2.add(bushingFile);
                list1.add(list2);
                sympMessageRealDao.addTextMessages(list1);
                //设置positionId
                imageViewShow.setTag(R.id.position_id, position.getId());
                imageViewShow.setTag(R.id.pic_id, uuid);
                if(Constants.state==2){
                    ((OperationItemActivity) context).getImageView(imageViewShow);
                    ((OperationItemActivity) context).getEditText(editText);
                    ((OperationItemActivity) context).getType("bush");
                    ((OperationItemActivity) context).toTakePhoto();
                    ((OperationItemActivity) context).getRelativelayout(imageshow);
                    ((OperationItemActivity) context).getLinearlayout(imageViewCamera);

                }else{
                    imageViewCamera.setVisibility(View.GONE);
                    imageshow.setVisibility(View.VISIBLE);

                    editText.setGravity(Gravity.NO_GRAVITY);
                    editText.setEnabled(true);

                    if(Utils.number.equals("")){
                        Constants.showSoftKeyboard(editText);
                    }else{
                        editText.setText(fileNumber);
                    }

                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String number=editText.getText().toString().trim();
                            if(!TextUtils.isEmpty(number)){
                                    List<TrSpecialBushingFile> bushingFileList=new ArrayList<TrSpecialBushingFile>();
                                    TrSpecialBushingFile bushingFile=new TrSpecialBushingFile();
                                    bushingFile.setId(uuid);
                                    bushingFile.setPositionid(position.getId());
                                    bushingFile.setFilenumber(number);
                                    bushingFile.setIsupload("1");
                                    bushingFile.setTaskid(trTask.getTaskid());
                                    bushingFile.setProjectid(trTask.getProjectid());
                                    bushingFileList.add(bushingFile);
                                    TrSpecialBushingFileDao bushingFileDao=new TrSpecialBushingFileDao(context);
                                    bushingFileDao.insertListData(bushingFileList);

                                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(bushingFile);
                                    list1.add(list2);
                                    sympMessageRealDao.updateTextMessages(list1);

                                    Utils.number = number;//当前输入框的值赋给number
                            }
                        }
                    });
                }
            }
        });
        delbusingFile(del,imageViewShow);
    }

    /**
     * 通过图号获取套管图片
     */
/*    private void getImage(final LinearLayout add,TrSpecialBushingPosition position) {
       final String id=position.getId();
        add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<TrSpecialBushingFile> trSpecialBushingFile = getbushingFile(id);
                String uuid = "";
                if(trSpecialBushingFile.size()==0){
                    uuid = Constants.getUuid();
                    List<TrSpecialBushingFile> bushingFileList=new ArrayList<TrSpecialBushingFile>();
                    TrSpecialBushingFile bushingFile=new TrSpecialBushingFile();
                    bushingFile.setId(uuid);
                    bushingFile.setPositionid(id);
                    bushingFile.setIsupload("1");
                    bushingFileList.add(bushingFile);
                    TrSpecialBushingFileDao bushingFileDao=new TrSpecialBushingFileDao(context);
                    bushingFileDao.insertListData(bushingFileList);

                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(bushingFile);
                    list1.add(list2);
                    sympMessageRealDao.addTextMessages(list1);
                }else{
                    uuid=trSpecialBushingFile.get(0).getId();
                }

                Intent intent=new Intent(context,ImageGetActivity.class);
                intent.putExtra("id",uuid);
                intent.putExtra("Trtask",trTask);
                intent.putExtra("flag",1);
                context.startActivity(intent);
                return false;
            }
        });
    }*/

    /**
     * 设置套管图号的监听事件
     */
    private void setOnfocuslistenerBushing(final EditText editText, final TrSpecialBushingFile file,final String positionId,final ImageView image_show){
        if(Constants.state==1){
            editText.setGravity(Gravity.NO_GRAVITY);
            editText.setEnabled(true);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String text = editText.getText().toString();
                    String date = Constants.dateformat2.format(new Date());
                    if(!hasFocus){
                        Utils.number = text;//当前输入框的值赋给number
                        if(!text.equals("")){
                            if(!file.getFilenumber().equals(text)){
                                Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
                                columnsMapImage.put("FILENUMBER", text);
                                columnsMapImage.put("UPDATETIME", date);
                                columnsMapImage.put("UPDATEPERSON", userid);
                                columnsMapImage.put("POSITIONID", positionId);
                                columnsMapImage.put("PROJECTID",trTask.getProjectid());
                                columnsMapImage.put("TASKID", trTask.getTaskid());
                                if(file.getFilename()!=null&&!file.getFilename().equals("null")){
                                    columnsMapImage.put("FILENAME","");
                                    columnsMapImage.put("FILEURL","");

                                    image_show.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                    image_show.setBackgroundResource(0);
                                }
                                Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
                                wheresMapImage.put("ID", file.getId());
                                trSpecialBushingFileDao.updateTrSpecialBushingFileInfo(columnsMapImage,wheresMapImage);
                                //发送消息
                                TrSpecialBushingFile trSpecialButshingFile = new TrSpecialBushingFile();
                                trSpecialButshingFile.setFilenumber(text);
                                trSpecialButshingFile.setUpdatetime(date);
                                trSpecialButshingFile.setUpdateperson(userid);
//                        trSpecialButshingFile.setFilename(text+".jpg");
                                trSpecialButshingFile.setProjectid(trTask.getProjectid());
                                trSpecialButshingFile.setTaskid(trTask.getTaskid());
                                trSpecialButshingFile.setId(file.getId());
                                List<List<Object>> list1 = new ArrayList<List<Object>>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trSpecialButshingFile);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                            }
                        }
                    }else{//获得焦点,图号加1
                        if(!Utils.number.equals("")){
                            if(text.equals("")){
                                editText.setText(Constants.formatNum());
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置普通图号的监听事件
     */
    private void setOnFocusListenerGeneral(final EditText editText, final TrDetectiontempletFileObj file, final TrDetectiontypEtempletObj obj){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = editText.getText().toString();
                String date = Constants.dateformat2.format(new Date());
                if(!hasFocus){
                    Utils.number = text;//当前输入框的值赋给number
                    if(file.getId() != null && !file.getId().equals("null")) {
                        if (!file.getFilenumber().equals(text)  && !file.getProjectid().equals("")) {
                            Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
                            columnsMapImage.put("FILENUMBER", text);
                            columnsMapImage.put("UPDATETIME", date);
                            columnsMapImage.put("UPDATEPERSON", userid);
                            columnsMapImage.put("TEMPLETNAME", "普通模板");
                            columnsMapImage.put("TEMPLETTYPE", "general");
//                            columnsMapImage.put("FILENAME", text+".jpg");
                            Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
                            wheresMapImage.put("ID", file.getId());
                            trDetectionTempletFileObjDao.updateTrDetectionTempletFileObjInfo(columnsMapImage, wheresMapImage);
                            //发送消息
                            TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                            trDetectiontempletFileObj.setFilenumber(text);
                            trDetectiontempletFileObj.setUpdatetime(date);
                            trDetectiontempletFileObj.setUpdateperson(userid);
                            trDetectiontempletFileObj.setTempletname("普通模板");
                            trDetectiontempletFileObj.setTemplettype("general");
//                            trDetectiontempletFileObj.setFilename(text+".jpg");
                            trDetectiontempletFileObj.setId(file.getId());
                            List<List<Object>> list1 = new ArrayList<List<Object>>();
                            List<Object> list2 = new ArrayList<Object>();
                            list2.add(trDetectiontempletFileObj);
                            list1.add(list2);
                            sympMessageRealDao.updateTextMessages(list1);
                        }
                    }else if(!text.equals("") && !text.equals(text2)){//插入数据
                        text2 = text;
                        List<TrDetectiontempletFileObj> fileObjs = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj fileobj = new TrDetectiontempletFileObj();
                        String id = Constants.getUuid().toString();
                        fileobj.setId(id);
                        fileobj.setUpdateperson(userid);
                        fileobj.setFilenumber(text);
                        fileobj.setUpdatetime(date);
                        fileobj.setIsupload("0");
                        fileobj.setTempletname("普通模板");
                        fileobj.setTemplettype("general");
//                        fileobj.setFilename(text+".jpg");
                        fileobj.setProjectid(trTask.getProjectid());
                        fileobj.setTaskid(trTask.getTaskid());
                        fileobj.setDetectionobjid(obj.getDetectiontemplet());
                        fileObjs.add(fileobj);
                        trDetectionTempletFileObjDao.insertListData(fileObjs);
                        //发送消息
                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(fileobj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);
                    }
                }else{//获得焦点,图号加1
                    if(!Utils.number.equals("")){
                        if(text.equals("")){
                            editText.setText(Constants.formatNum());
                        }
                    }
                }
            }
        });
    }
    /**
     * 非红外普通图片(第一个采集卡图片,公用图片)
     * @param view
     * @param size
     * @param filelist
     * @param trDetectiontypEtempletObj
     */
    public void getViewforFirst(final MyviewHolder view,final int size,final List<TrDetectiontempletFileObj> filelist, final TrDetectiontypEtempletObj trDetectiontypEtempletObj,final TrDetectiontypEtempletObj obj){  //第一张采集卡
        int imagedefaultcount = Integer.parseInt(obj.getPiccount());
        if(size==0){
            View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
            RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
            LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
            image_rl.setVisibility(View.INVISIBLE);
            TextView image_count= (TextView) v.findViewById(R.id.image_count);
            image_count.setText("至少"+imagedefaultcount+"张图片");

            image_add_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.linearLayout.removeAllViews();
                    String fileNumber = "";
                    if(!("").equals(Utils.number)){
                        if(Constants.state==1){
                            fileNumber = Constants.formatNum();
                        }
                    }
                    final String uuid = Constants.getUuid();
                    List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                    TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                    trDetectiontempletFileObj.setId(uuid);
                    trDetectiontempletFileObj.setDetectionobjid(firstid);
                    trDetectiontempletFileObj.setIsupload("1");
                    trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                    trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                    trDetectiontempletFileObj.setFilenumber(fileNumber);
                    insertfile.add(trDetectiontempletFileObj);
                    trDetectionTempletFileObjDao.insertListData(insertfile);

                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trDetectiontempletFileObj);
                    list1.add(list2);
                    sympMessageRealDao.addTextMessages(list1);

                    List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                    getViewforFirst(view, filelist.size(), filelist,trDetectiontypEtempletObj,obj);

                    if(Constants.state==2){
                        ((OperationItemActivity) context).getImageView(firstImageView);
                        ((OperationItemActivity) context).getEditText(firstEditText);
                        ((OperationItemActivity) context).getItemID(firstid);
                        ((OperationItemActivity) context).getTextView(firstTextView);
                        ((OperationItemActivity) context).getType("general");
                        ((OperationItemActivity) context).toTakePhoto();
                    }else{
                        if(Utils.number.equals("")){
                            Constants.showSoftKeyboard(firstEditText);
                        }
                    }

                    Constants.takephotohandler=new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            deletePhoto(uuid);
                            List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                            view.gener_ll.removeAllViews();
                            getViewforFirst(view,filelist.size(),filelist,trDetectiontypEtempletObj,obj);
                        }
                    };
                }
            });

            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                image_add_layout.setEnabled(false);
                view.linearLayout.setVisibility(View.GONE);
            }
            view.linearLayout.addView(v);
        }else{
            for(int i=0;i<size;i++){

                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setVisibility(View.GONE);


                final LinearLayout lay_delete= (LinearLayout) v.findViewById(R.id.lay_delete);
                final ImageView image_show= (ImageView) v.findViewById(R.id.image_show);
                final EditText image_number= (EditText) v.findViewById(R.id.image_number);
                final TextView hind = (TextView) v.findViewById(R.id.hind);
                final ProgressBar image_bar = (ProgressBar) v.findViewById(R.id.image_bar);

                //删除图片
                lay_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                            mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认",new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deletePhoto(hind.getText().toString());
                                    List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                                    view.gener_ll.removeAllViews();
                                    getViewforFirst(view,filelist.size(),filelist,trDetectiontypEtempletObj,obj);
                                    mMaterialDialog.dismiss();
                                }
                            }).setNegativeButton("取消",new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            }).setCanceledOnTouchOutside(false).show();
                    }
                });

                //点击图片放大
                final int finalI = i;
                image_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileNumber = image_number.getText().toString();
                        if(!fileNumber.equals("")){
                            if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                                String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/original/";
                                Intent intent = new Intent(context, ImagePagerActivity.class);
                                intent.putExtra("index", finalI);
                                intent.putExtra("url",url);
                                intent.putExtra("localurl",pic_route+"original");
                                intent.putExtra("type","general");
                                intent.putExtra("id",firstid);
                                context.startActivity(intent);
                            }else{
                                Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(filelist.size() > 0){
                    context.imageGet(filelist.get(i),image_show,image_bar);
                    //设置图号监听
//                    setOnFocusListenerGeneralFirst(image_number, filelist.get(i));
                    if(filelist.get(i).getFilename() != null && !filelist.get(i).getFilename().equals("")){
                        String fileName = filelist.get(i).getFilename();
                        String fileNumber = Constants.getFileNumber(fileName);
                        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }else{
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }
                    }

                    if(Constants.state==1){
                        image_number.setGravity(Gravity.NO_GRAVITY);
                        image_number.setEnabled(true);
                    }

                    if(filelist.get(i).getId() != null && !filelist.get(i).getId().equals("")){
                        hind.setText(filelist.get(i).getId());
                    }
                }

                if(Constants.state==1){
                    final int position = i;
                    image_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            String number=image_number.getText().toString().trim();
                            if(!number.equals("")){
                                Utils.number=number;
                                String id=filelist.get(position).getId();
                                if( id!=null){
                                    if(!filelist.get(position).getFilenumber().equals(number)){
                                        Map<Object,Object> columnsMap=new HashMap<Object, Object>();
                                        columnsMap.put("FILENUMBER",number);
                                        columnsMap.put("TASKID",trTask.getTaskid());
                                        columnsMap.put("PROJECTID",trTask.getProjectid());
                                        columnsMap.put("DETECTIONOBJID",firstid);
                                        if(filelist.get(position).getFilename()!=null&&!filelist.get(position).getFilename().equals("null")){
                                            columnsMap.put("FILENAME","");
                                            columnsMap.put("FILEURL","");

                                            image_show.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                            image_show.setBackgroundResource(0);
                                        }
                                        Map<Object,Object> wheresMap=new HashMap<Object, Object>();
                                        wheresMap.put("ID",id);
                                        trDetectionTempletFileObjDao.updateTrDetectionTempletFileObjInfo(columnsMap,wheresMap); //新增的时候可以显示图号

                                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                                        trDetectiontempletFileObj.setId(id);
                                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                                        trDetectiontempletFileObj.setDetectionobjid(firstid);
                                        trDetectiontempletFileObj.setFilenumber(number);
                                        trDetectiontempletFileObj.setIsupload("1");

                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trDetectiontempletFileObj);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);
                                    }
                                }
                            }
                        }
                    });
                }

                if(i==size-1) {
                    firstImageView = (ImageView) v.findViewById(R.id.image_show);
                    firstEditText = (EditText) v.findViewById(R.id.image_number);
                    firstTextView = (TextView) v.findViewById(R.id.hind);
                }

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_number.setEnabled(false);

                    lay_delete.setVisibility(View.GONE);
                }

                view.linearLayout.addView(v);
            }
            if(size<imagedefaultcount){
                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                image_rl.setVisibility(View.INVISIBLE);

                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String fileNumber = "";
                        if(!("").equals(Utils.number)){
                            if(Constants.state==1){
                                fileNumber = Constants.formatNum();
                            }
                        }
                        final String uuid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        trDetectiontempletFileObj.setId(uuid);
                        trDetectiontempletFileObj.setDetectionobjid(firstid);
                        trDetectiontempletFileObj.setIsupload("1");
                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        trDetectiontempletFileObj.setFilenumber(fileNumber);
                        insertfile.add(trDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                        getViewforFirst(view, filelist.size(), filelist,trDetectiontypEtempletObj,obj);

                        if(Constants.state==2){
                            ((OperationItemActivity) context).getImageView(firstImageView);
                            ((OperationItemActivity) context).getEditText(firstEditText);
                            ((OperationItemActivity) context).getItemID(firstid);
                            ((OperationItemActivity) context).getTextView(firstTextView);
                            ((OperationItemActivity) context).getType("general");
                            ((OperationItemActivity) context).toTakePhoto();
                        }else{
                            if(Utils.number.equals("")){
                                Constants.showSoftKeyboard(firstEditText);
                            }
                        }

                        Constants.takephotohandler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                deletePhoto(uuid);
                                List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                                view.gener_ll.removeAllViews();
                                getViewforFirst(view,filelist.size(),filelist,trDetectiontypEtempletObj,obj);
                            }
                        };
                    }
                });

                TextView image_count= (TextView) v.findViewById(R.id.image_count);
                image_count.setText("至少"+imagedefaultcount+"张图片");

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_add_layout.setEnabled(false);
                    image_add_layout.setVisibility(View.GONE);
                }
                view.linearLayout.addView(v);
            }else{
                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                image_rl.setVisibility(View.INVISIBLE);

                TextView image_count= (TextView) v.findViewById(R.id.image_count);
                image_count.setVisibility(View.GONE);
                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String fileNumber = "";
                        if(!("").equals(Utils.number)){
                            if(Constants.state==1){
                                fileNumber = Constants.formatNum();
                            }
                        }
                        final String uuid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        trDetectiontempletFileObj.setId(uuid);
                        trDetectiontempletFileObj.setDetectionobjid(firstid);
                        trDetectiontempletFileObj.setIsupload("1");
                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        trDetectiontempletFileObj.setFilenumber(fileNumber);
                        insertfile.add(trDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                        getViewforFirst(view, filelist.size(), filelist,trDetectiontypEtempletObj,obj);

                        if(Constants.state==2){
                            ((OperationItemActivity) context).getImageView(firstImageView);
                            ((OperationItemActivity) context).getEditText(firstEditText);
                            ((OperationItemActivity) context).getItemID(firstid);
                            ((OperationItemActivity) context).getTextView(firstTextView);
                            ((OperationItemActivity) context).getType("general");
                            ((OperationItemActivity) context).toTakePhoto();
                        }else{
                            if(Utils.number.equals("")){
                                Constants.showSoftKeyboard(firstEditText);
                            }
                        }

                        Constants.takephotohandler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                deletePhoto(uuid);
                                List<TrDetectiontempletFileObj> filelist = getFile2(firstid);
                                view.gener_ll.removeAllViews();
                                getViewforFirst(view,filelist.size(),filelist,trDetectiontypEtempletObj,obj);
                            }
                        };
                    }
                });
                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_add_layout.setEnabled(false);
                    image_add_layout.setVisibility(View.GONE);
                }
                view.linearLayout.addView(v);
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaperOne");
                }
                int scrollViewwight=view.horizontalScrollView.getWidth();
                int linearLayoutwight=view.linearLayout.getWidth();
                if(linearLayoutwight>scrollViewwight){
                    int x=linearLayoutwight-scrollViewwight;
                    view.horizontalScrollView.scrollTo(linearLayoutwight-scrollViewwight, 0);
                }
            }
        });
    }
    /**
     * 非红外普通图片
     * @param view
     * @param size
     * @param filelist
     */
    public void getViewforcommon(final int size, final MyviewHolder view,final List<TrDetectiontempletFileObj> filelist,final TrDetectiontypEtempletObj trDetectiontypEtempletObj){ //普通模板
        int imagedefaultcount = Integer.parseInt(trDetectiontypEtempletObj.getPiccount());
        if(size==0){
            View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
            RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
            LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
            image_rl.setVisibility(View.INVISIBLE);
            TextView image_count= (TextView) v.findViewById(R.id.image_count);
            image_count.setText("至少"+imagedefaultcount+"张图片");
            image_add_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.gener_ll.removeAllViews();
                    String fileNumber = "";
                    if(!("").equals(Utils.number)){
                        if(Constants.state==1){
                            fileNumber = Constants.formatNum();
                        }
                    }
                    final String uuid = Constants.getUuid();
                    List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                    TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                    trDetectiontempletFileObj.setId(uuid);
                    trDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                    trDetectiontempletFileObj.setIsupload("1");
                    trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                    trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                    trDetectiontempletFileObj.setFilenumber(fileNumber);

                    insertfile.add(trDetectiontempletFileObj);
                    trDetectionTempletFileObjDao.insertListData(insertfile);

                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trDetectiontempletFileObj);
                    list1.add(list2);
                    sympMessageRealDao.addTextMessages(list1);

                    List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                    getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);

                    if(Constants.state==2){
                        ((OperationItemActivity) context).getImageView(firstImageView);
                        ((OperationItemActivity) context).getEditText(firstEditText);
                        ((OperationItemActivity) context).getItemID(trDetectiontypEtempletObj.getDetectiontemplet());
                        ((OperationItemActivity) context).getTextView(firstTextView);
                        ((OperationItemActivity) context).getType("general");
                        ((OperationItemActivity) context).toTakePhoto();
                    }else{
                         if(Utils.number.equals("")){
                             Constants.showSoftKeyboard(firstEditText);
                         }
                    }

                    Constants.takephotohandler=new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            deletePhoto(uuid);
                            List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                            view.gener_ll.removeAllViews();
                            getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);
                        }
                    };
                }
            });

            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                image_add_layout.setEnabled(false);
                view.gener_ll.setVisibility(View.GONE);
            }
            view.gener_ll.addView(v);
        }else{
            for(int i=0;i<size;i++){
                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setVisibility(View.GONE);

                final LinearLayout lay_delete= (LinearLayout) v.findViewById(R.id.lay_delete);
                final ImageView image_show= (ImageView) v.findViewById(R.id.image_show);
                final EditText image_number= (EditText) v.findViewById(R.id.image_number);
                final TextView hind = (TextView) v.findViewById(R.id.hind);
                final ProgressBar image_bar = (ProgressBar) v.findViewById(R.id.image_bar);
                if(filelist.size() > 0){
                    context.imageGet(filelist.get(i),image_show,image_bar);
                    if(filelist.get(i).getFilename() != null && !filelist.get(i).getFilename().equals("")){
                        String fileName = filelist.get(i).getFilename();
                        String fileNumber = Constants.getFileNumber(fileName);
                        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }else{
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }
                    }
                    if(Constants.state==1){
                        image_number.setGravity(Gravity.NO_GRAVITY);
                        image_number.setEnabled(true);
                    }
                    if(filelist.get(i).getId() != null && !filelist.get(i).getId().equals("")){
                        hind.setText(filelist.get(i).getId());
                    }
                }
                if(i==size-1){
                    firstImageView= (ImageView) v.findViewById(R.id.image_show);
                    firstEditText= (EditText) v.findViewById(R.id.image_number);
                    firstTextView = (TextView) v.findViewById(R.id.hind);
                }

                if(Constants.state==1){
                    final int position = i;
                    image_number.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String number=image_number.getText().toString().trim();
                            if(!number.equals("")){
                                String id=filelist.get(position).getId();
                                if( id!=null){
                                    if(!filelist.get(position).getFilenumber().equals(number)){
                                        Map<Object,Object> columnsMap=new HashMap<Object, Object>();
                                        columnsMap.put("FILENUMBER",number);
                                        columnsMap.put("TASKID",trTask.getTaskid());
                                        columnsMap.put("PROJECTID",trTask.getProjectid());
                                        columnsMap.put("DETECTIONOBJID",trDetectiontypEtempletObj.getDetectiontemplet());
                                        if(!filelist.get(position).getFilename().equals("null")&&filelist.get(position).getFilename()!=null){
                                            columnsMap.put("FILENAME","");
                                            columnsMap.put("FILEURL","");

                                            image_show.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                            image_show.setBackgroundResource(0);
                                        }
                                        Map<Object,Object> wheresMap=new HashMap<Object, Object>();
                                        wheresMap.put("ID",id);
                                        trDetectionTempletFileObjDao.updateTrDetectionTempletFileObjInfo(columnsMap,wheresMap); //新增的时候可以显示图号

                                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                                        trDetectiontempletFileObj.setId(id);
                                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                                        trDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                                        trDetectiontempletFileObj.setFilenumber(number);
                                        trDetectiontempletFileObj.setIsupload("1");

                                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                                        List<Object> list2 = new ArrayList<Object>();
                                        list2.add(trDetectiontempletFileObj);
                                        list1.add(list2);
                                        sympMessageRealDao.updateTextMessages(list1);

                                        Utils.number=number;
                                    }
                                }
                            }
                        }
                    });
                }

                //删除图片
                lay_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deletePhoto(hind.getText().toString());
                                List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                                view.gener_ll.removeAllViews();
                                getViewforcommon(size-1, view,filelist,trDetectiontypEtempletObj);
                                mMaterialDialog.dismiss();
                            }
                        }).setNegativeButton("取消",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        }).setCanceledOnTouchOutside(false).show();
                    }
                });

                //点击图片放大
                final int finalI = i;
                image_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileNumber = image_number.getText().toString();
                        if(!fileNumber.equals("")){
                            if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                                String url  = URLs.HTTP+URLs.HOSTA+URLs.IMAGEPATH+trTask.getProjectid() +"/"+trTask.getTaskid()+"/original/";
                                Intent intent = new Intent(context, ImagePagerActivity.class);
                                intent.putExtra("index", finalI);
                                intent.putExtra("url",url);
                                intent.putExtra("localurl",pic_route+"original");
                                intent.putExtra("type","general");
                                intent.putExtra("id",trDetectiontypEtempletObj.getDetectiontemplet());
                                context.startActivity(intent);
                            }else{
                                Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_number.setEnabled(false);
                    lay_delete.setVisibility(View.GONE);
                }
                view.gener_ll.addView(v);
            }
            if(size<imagedefaultcount){
                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                image_rl.setVisibility(View.INVISIBLE);

                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String fileNumber = "";
                        if(!("").equals(Utils.number)){
                            if(Constants.state==1){
                                fileNumber = Constants.formatNum();
                            }
                        }
                        final String uuid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        trDetectiontempletFileObj.setId(uuid);
                        trDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        trDetectiontempletFileObj.setIsupload("1");
                        trDetectiontempletFileObj.setFilenumber(fileNumber);
                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        insertfile.add(trDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                        getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);

                        if(Constants.state==2){
                            ((OperationItemActivity) context).getImageView(firstImageView);
                            ((OperationItemActivity) context).getEditText(firstEditText);
                            ((OperationItemActivity) context).getItemID(trDetectiontypEtempletObj.getDetectiontemplet());
                            ((OperationItemActivity) context).getTextView(firstTextView);
                            ((OperationItemActivity) context).getType("general");
                            ((OperationItemActivity) context).toTakePhoto();
                        }else {
                            if(Utils.number.equals("")){
                                Constants.showSoftKeyboard(firstEditText);
                            }
                        }

                        Constants.takephotohandler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                deletePhoto(uuid);
                                List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                                view.gener_ll.removeAllViews();
                                getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);
                            }
                        };
                    }
                });

                TextView image_count= (TextView) v.findViewById(R.id.image_count);
                image_count.setText("至少"+imagedefaultcount+"张图片");

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_add_layout.setEnabled(false);
                    image_add_layout.setVisibility(View.GONE);
                }
                view.gener_ll.addView(v);
            }else{
                View v=LayoutInflater.from(context).inflate(R.layout.image_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                image_rl.setVisibility(View.INVISIBLE);
                TextView image_count= (TextView) v.findViewById(R.id.image_count);
                image_count.setVisibility(View.GONE);

                LinearLayout image_add_layout= (LinearLayout) v.findViewById(R.id.image_add_layout);
                image_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String fileNumber = "";
                        if(!("").equals(Utils.number)){
                            if(Constants.state==1){
                                fileNumber = Constants.formatNum();
                            }
                        }
                        final String uuid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        trDetectiontempletFileObj.setId(uuid);
                        trDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        trDetectiontempletFileObj.setIsupload("1");
                        trDetectiontempletFileObj.setFilenumber(fileNumber);
                        trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        insertfile.add(trDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                        getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);

                        if(Constants.state==2){
                            ((OperationItemActivity) context).getImageView(firstImageView);
                            ((OperationItemActivity) context).getEditText(firstEditText);
                            ((OperationItemActivity) context).getItemID(trDetectiontypEtempletObj.getDetectiontemplet());
                            ((OperationItemActivity) context).getTextView(firstTextView);
                            ((OperationItemActivity) context).getType("general");
                            ((OperationItemActivity) context).toTakePhoto();
                        }else{
                            if(Utils.number.equals("")){
                                Constants.showSoftKeyboard(firstEditText);
                            }
                        }

                        Constants.takephotohandler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                deletePhoto(uuid);
                                List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                                view.gener_ll.removeAllViews();
                                getViewforcommon(filelist.size(), view,filelist,trDetectiontypEtempletObj);
                            }
                        };
                    }
                });
                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    image_add_layout.setEnabled(false);
                    image_add_layout.setVisibility(View.GONE);
                }
                view.gener_ll.addView(v);
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaperOne");
                }
                int scrollViewwight=view.general_hlistview.getWidth();
                int linearLayoutwight=view.gener_ll.getWidth();
                if(linearLayoutwight>scrollViewwight){
                    int x=linearLayoutwight-scrollViewwight;
                    view.general_hlistview.scrollTo(x,0);
                }
            }
        });
    }

    //套管文件展示方法
    private void bushingFile(ImageView image,TrSpecialBushingFile trSpecialBushingFile,TrSpecialBushingPosition position,ProgressBar bar){
        context.imageBushingGet(trSpecialBushingFile,image,bar);
        image.setTag(R.id.pic_id, trSpecialBushingFile.getId());
        image.setTag(R.id.position_id, position.getId());
    }

    private void delbusingFile(final LinearLayout del,final ImageView id) {  //套管图片的删除
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletebusingPhoto((String)id.getTag(R.id.pic_id));
                        Constants.bushinghandler.sendEmptyMessage(1);
                        mMaterialDialog.dismiss();
                    }
                }).setNegativeButton("取消",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                }).setCanceledOnTouchOutside(false).show();
            }
        });
    }

    //查询检测项目表（套管）
    public List<TrSpecialBushing> getbushing(String id){
        TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
        trSpecialBushing.setDetectionprojectid(id);
        List<TrSpecialBushing> trSpecialBushings = new ArrayList<TrSpecialBushing>();
        TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
        trSpecialBushings = trSpecialBushingDao.queryTrSpecialBushingList(trSpecialBushing);
        return trSpecialBushings;
    }

    //查询检测项目（套管）检测位置
    public List<TrSpecialBushingPosition> getBushingData(String bushingid){
        TrSpecialBushingPosition trSpecialBushingPosition = new TrSpecialBushingPosition();
        trSpecialBushingPosition.setBushingid(bushingid);
        List<TrSpecialBushingPosition> trSpecialBushingPositions = new ArrayList<TrSpecialBushingPosition>();
        TrSpecialBushingPositionDao trSpecialBushingPositionDao = new TrSpecialBushingPositionDao(context);
        trSpecialBushingPositions = trSpecialBushingPositionDao.queryTrSpecialBushingPositionList(trSpecialBushingPosition);
        return trSpecialBushingPositions;
    }

    //查询检测项目（套管）文件表
    public List<TrSpecialBushingFile> getbushingFile(String positionid){
        TrSpecialBushingFile trSpecialBushingFile = new TrSpecialBushingFile();
        trSpecialBushingFile.setPositionid(positionid);
        List<TrSpecialBushingFile> trSpecialBushingFiles = new ArrayList<TrSpecialBushingFile>();
        trSpecialBushingFiles = trSpecialBushingFileDao.queryTrSpecialBushingFileList(trSpecialBushingFile);
        return trSpecialBushingFiles;
    }
//    public List<TrDetectiontempletFileObj> getFile(TrDetectiontypEtempletObj trDetectiontypEtempletObj) { //查询模板下面是否有图片
//        TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
//        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
//        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getId());
//        List<TrDetectiontempletFileObj> list = fileObjDao.queryTrDetectionTempletFileObjList(fileObj);
//        return list;
//    }

    public List<TrDetectiontempletFileObj> getFile2(String firstid) { //查询父节点下面是否有图片
        TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(firstid);
        List<TrDetectiontempletFileObj> list = fileObjDao.queryTrDetectionTempletFileObjList(fileObj);
        return list;
    }

    private void createdelDialog(int count) {  //删除提示的dialog
        photodialog=new MaterialDialog(context);
        photodialog.setTitle("删除").setMessage("至少应该保留"+count+"张照片").setCanceledOnTouchOutside(true).show();
    }

    private void deletebusingPhoto(String fileid) { //从数据库中删除套管的照片
        TrSpecialBushingFileDao fileDao=new TrSpecialBushingFileDao(context);
        Map<Object,Object> columnsMap=new HashMap<>();
        columnsMap.put("ISUPLOAD","2");
        Map<Object,Object> wheresMap=new HashMap<>();
        wheresMap.put("ID",fileid);
        fileDao.updateTrSpecialBushingFileInfo(columnsMap,wheresMap);

        TrSpecialBushingFile file=new TrSpecialBushingFile();
        file.setId(fileid);
        file.setIsupload("2");
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(file);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    private void deletebusing(String posid) { //从数据库中删除套管的照片
        TrSpecialBushingFileDao fileDao=new TrSpecialBushingFileDao(context);
        Map<Object,Object> columnsMap=new HashMap<>();
        columnsMap.put("ISUPLOAD","2");
        Map<Object,Object> wheresMap=new HashMap<>();
        wheresMap.put("POSITIONID",posid);
        fileDao.updateTrSpecialBushingFileInfo(columnsMap,wheresMap);

        TrSpecialBushingFile file=new TrSpecialBushingFile();
        file.setPositionid(posid);
        file.setIsupload("2");
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(file);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    private void deletePhoto(String fileid) { //从数据库中删除照片
        TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
        Map<Object,Object> columnsMap=new HashMap<>();
        columnsMap.put("ISUPLOAD","2");
        Map<Object,Object> wheresMap=new HashMap<>();
        wheresMap.put("ID",fileid);
        fileObjDao.updateTrDetectionTempletFileObjInfo(columnsMap,wheresMap);
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setId(fileid);
        fileObj.setIsupload("2");
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(fileObj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    public List<TrDetectiontempletFileObj> getFileItem(TrDetectiontypEtempletObj trDetectiontypEtempletObj) { //查询采集卡下面是否有图片
        TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
        List<TrDetectiontempletFileObj> list = fileObjDao.queryTrDetectionTempletFileObjList(fileObj);
        return list;
    }

    private List<TrDetectiontempletDataObj> getTrDetectionTempletData(String id) { //获取检测项目模板（输入项）实体表信息
        TrDetectionTempletDataObjDao trDetectionTempletDataObjDao=new TrDetectionTempletDataObjDao(context);
        TrDetectiontempletDataObj trDetectiontempletDataObj=new TrDetectiontempletDataObj();
        trDetectiontempletDataObj.setDetectionobjid(id);
        List<TrDetectiontempletDataObj> data = trDetectionTempletDataObjDao.queryTrDetectionTempletDataObjList(trDetectiontempletDataObj);
        return data;
    }

    public  List<TrSpecialOliData>  getOildata(TrDetectiontypEtempletObj data) {  //获得油色谱数据
        TrSpecialOliDataDao oliDataDao=new TrSpecialOliDataDao(context);
        TrSpecialOliData oildata = new TrSpecialOliData();
        oildata.setDetectionprojectid(data.getId());
        List<TrSpecialOliData> oildatalist = oliDataDao.queryTrSpecialOliDataList(oildata);
        return oildatalist;
    }

    public void insertGeneral(final EditText results,final EditText remarks,final CheckBox checkYes,final CheckBox checkNo,final LinearLayout linearLayout,final TrDetectiontypEtempletObj data){
        final String uuid = Constants.getUuid();
        final String date = Constants.dateformat2.format(new Date());
        results.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String result = results.getText().toString();
                    String remark = remarks.getText().toString();
                    String state = "";
                    if(checkNo.isChecked() == true){
                        state = "0";
                    }else if(checkYes.isChecked() == true){
                        state = "1";
                    }
                    if(!result.equals("")){
                        insertTrDetectiontempletObj(data,uuid,date,remark,state,result);
                    }
                }
            }
        });
        remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String result = results.getText().toString();
                    String remark = remarks.getText().toString();
                    String state = "";
                    if(checkNo.isChecked() == true){
                        state = "0";
                    }else if(checkYes.isChecked() == true){
                        state = "1";
                    }
                    if(!remark.equals("")){
                        insertTrDetectiontempletObj(data,uuid,date,remark,state,result);
                    }
                }
            }
        });
        checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if(data.getTemplettype().equals("noise")){
                        linearLayout.setVisibility(View.GONE);
                    }
                    checkYes.setTextColor(green);
                    checkNo.setChecked(false);
                    checkNo.setTextColor(color);
                    String result = results.getText().toString();
                    String remark = remarks.getText().toString();
                    insertTrDetectiontempletObj(data, uuid, date, remark, "1", result);
                } else {
//                    if(data.getTemplettype().equals("noise")){
//                        linearLayout.setVisibility(View.GONE);
//                    }
                    checkYes.setTextColor(color);
                }
            }
        });
        checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if(data.getTemplettype().equals("noise")){
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    checkNo.setTextColor(orange);
                    checkYes.setChecked(false);
                    checkYes.setTextColor(color);
                    String result = results.getText().toString();
                    String remark = remarks.getText().toString();
                    insertTrDetectiontempletObj(data, uuid, date, remark, "0", result);
                } else {
//                    if(data.getTemplettype().equals("noise")){
//                        linearLayout.setVisibility(View.GONE);
//                    }
                    checkNo.setTextColor(color);
                }
            }
        });
    }

    public void updateGeneral(final EditText results,final EditText remarks,final CheckBox checkYes,final CheckBox checkNo,final LinearLayout linearLayout,final TrDetectiontempletObj trDetectiontempletObj,final TrDetectiontypEtempletObj data,final GridViewforListView gridViewforListView,final EditText groupcount,final EditText number,final EditText avg,final List<TrNoiseRecordGroup> trNoiseRecordGroups,final int position){
        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            results.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? " " : trDetectiontempletObj.getResultdescribe());
            remarks.setText(trDetectiontempletObj.getRemarks().equals("null") ? " " : trDetectiontempletObj.getRemarks());
        }else{
            results.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? "" : trDetectiontempletObj.getResultdescribe());
            remarks.setText(trDetectiontempletObj.getRemarks().equals("null") ? "" : trDetectiontempletObj.getRemarks());
        }

        if(trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("0")){
            checkNo.setChecked(true);
            checkNo.setTextColor(orange);
            checkYes.setChecked(false);
            checkYes.setTextColor(color);
            if(data.getTemplettype().equals("noise") && express == 0){
                linearLayout.setBackgroundResource(R.color.list_background_normal);
                groupcount.setEnabled(true);
                number.setEnabled(true);
                avg.setEnabled(true);
                NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,1);
                gridViewforListView.setAdapter(noiseGroupAdapter);
                noiseGroupAdapter.notifyDataSetChanged();
            }
        }else if (trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("1")) {
            checkYes.setChecked(true);
            checkYes.setTextColor(green);
            checkNo.setChecked(false);
            checkNo.setTextColor(color);
            if (data.getTemplettype().equals("noise") && express == 0) {
                linearLayout.setBackgroundResource(R.color.dividers);
                groupcount.setEnabled(false);
                number.setEnabled(false);
                avg.setEnabled(false);
                NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                gridViewforListView.setAdapter(noiseGroupAdapter);
                noiseGroupAdapter.notifyDataSetChanged();
            }
        }else{
            if (data.getTemplettype().equals("noise") && express == 0) {
                linearLayout.setBackgroundResource(R.color.dividers);
                groupcount.setEnabled(false);
                number.setEnabled(false);
                avg.setEnabled(false);
                NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                gridViewforListView.setAdapter(noiseGroupAdapter);
                noiseGroupAdapter.notifyDataSetChanged();
            }
        }

        //检测结果失去焦点修改
        results.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点时的处理内容
                    String result = results.getText().toString();
                    if(!trDetectiontempletObj.getResultdescribe().equals(result)){
                        String date = Constants.dateformat2.format(new Date());
                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                        columnsMap.put("RESULTDESCRIBE",result);
                        columnsMap.put("UPDATETIME", date);
                        columnsMap.put("UPDATEPERSON",userid);
                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                        wheresMap.put("ID",trDetectiontempletObj.getId());
                        TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                        trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap,wheresMap);
                        TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                        trDetectiontempletObj2.setResultdescribe(result);
                        trDetectiontempletObj2.setUpdatetime(date);
                        trDetectiontempletObj2.setUpdateperson(userid);
                        trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletObj2);
                        list1.add(list2);
                        sympMessageRealDao.updateTextMessages(list1);
                    }
                }
            }
        });
        //备注失去焦点修改
        remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点时的处理内容
                    String remark = remarks.getText().toString();
                    if(!trDetectiontempletObj.getRemarks().equals(remark)){
                        String date = Constants.dateformat2.format(new Date());
                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                        columnsMap.put("REMARKS",remark);
                        columnsMap.put("UPDATETIME", date);
                        columnsMap.put("UPDATEPERSON",userid);
                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                        wheresMap.put("ID", trDetectiontempletObj.getId());
                        TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                        trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap,wheresMap);
                        TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                        trDetectiontempletObj2.setRemarks(remark);
                        trDetectiontempletObj2.setUpdatetime(date);
                        trDetectiontempletObj2.setUpdateperson(userid);
                        trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                        List list1 = new ArrayList();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectiontempletObj2);
                        list1.add(list2);
                        sympMessageRealDao.updateTextMessages(list1);
                    }
                }
            }
        });
        //修改状态
        checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    if(data.getTemplettype().equals("noise")){
                        dataList.get(position).get(0).setStatus("1");
                        linearLayout.setBackgroundResource(R.color.dividers);
                        groupcount.setEnabled(false);
                        number.setEnabled(false);
                        avg.setEnabled(false);
                        NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                        gridViewforListView.setAdapter(noiseGroupAdapter);
                        noiseGroupAdapter.notifyDataSetChanged();
                        express = 1;
                    }
                    checkYes.setTextColor(green);
                    checkNo.setChecked(false);
                    checkNo.setTextColor(color);
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("STATUS","1");
                    columnsMap.put("UPDATETIME", date);
                    columnsMap.put("UPDATEPERSON",userid);
                    Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID", trDetectiontempletObj.getId());
                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                    trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap,wheresMap);
                    TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                    trDetectiontempletObj2.setStatus("1");
                    trDetectiontempletObj2.setUpdatetime(date);
                    trDetectiontempletObj2.setUpdateperson(userid);
                    trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                    List list1 = new ArrayList();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trDetectiontempletObj2);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                    updateStatus(data,"1");
                }else{
                    if(data.getTemplettype().equals("noise") && checkNo.isChecked() == false){
                        linearLayout.setBackgroundResource(R.color.dividers);
                        groupcount.setEnabled(false);
                        number.setEnabled(false);
                        avg.setEnabled(false);
                        NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                        gridViewforListView.setAdapter(noiseGroupAdapter);
                        noiseGroupAdapter.notifyDataSetChanged();
                    }
                    checkYes.setTextColor(color);
                }
            }
        });
        checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    if(data.getTemplettype().equals("noise")){
                        dataList.get(position).get(0).setStatus("0");
                        linearLayout.setBackgroundResource(R.color.list_background_normal);
                        groupcount.setEnabled(true);
                        number.setEnabled(true);
                        avg.setEnabled(true);
                        NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,1);
                        gridViewforListView.setAdapter(noiseGroupAdapter);
                        noiseGroupAdapter.notifyDataSetChanged();
                        express = 1;
                    }
                    checkNo.setTextColor(orange);
                    checkYes.setChecked(false);
                    checkYes.setTextColor(color);
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("STATUS", "0");
                    columnsMap.put("UPDATETIME", date);
                    columnsMap.put("UPDATEPERSON", userid);
                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID", trDetectiontempletObj.getId());
                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                    trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
                    TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                    trDetectiontempletObj2.setStatus("0");
                    trDetectiontempletObj2.setUpdatetime(date);
                    trDetectiontempletObj2.setUpdateperson(userid);
                    trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                    List list1 = new ArrayList();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trDetectiontempletObj2);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                    updateStatus(data,"0");
                } else {
                    if(data.getTemplettype().equals("noise") && checkYes.isChecked() == false){
                        linearLayout.setBackgroundResource(R.color.dividers);
                        groupcount.setEnabled(false);
                        number.setEnabled(false);
                        avg.setEnabled(false);
                        NoiseGroupAdapter noiseGroupAdapter = new NoiseGroupAdapter(context,trNoiseRecordGroups,trTask,0);
                        gridViewforListView.setAdapter(noiseGroupAdapter);
                        noiseGroupAdapter.notifyDataSetChanged();
                    }
                    checkNo.setTextColor(color);
                }
            }
        });
    }

    /**
     * 查询采集卡
     * @param
     */
    private List<List<TrDetectiontempletObj>> getDetectiontypEtempletObj(TrDetectiontypEtempletObj trDetectiontypEtempletObj){
        List<List<TrDetectiontempletObj>> dataList = new ArrayList<List<TrDetectiontempletObj>>();
        List<TrDetectiontypEtempletObj> objList = getData(trDetectiontypEtempletObj);
        TrDetectionTempletObjDao trDetectionTempletObjDao=new TrDetectionTempletObjDao(context);
        if(objList != null && objList.size() > 0){
            for(int i = 0; i < objList.size();i++){
                List<TrDetectiontempletObj> list = trDetectionTempletObjDao.getTrDetectiontempletObj(objList.get(i));
                dataList.add(list);
            }
        }

        firstFileList = getFile2(firstid);
        if(firstFileList.size() > 0||!trDetectiontypEtempletObjOne.getPiccount().equals("0")){
//            this.list.add(0,list.get(0));
            List<TrDetectiontempletObj> list2 = new ArrayList<TrDetectiontempletObj>();
            if(list.size()!=0&&this.dataList.get(0).size()!=0){
                list2.add(this.dataList.get(0).get(0));
            }
            dataList.add(0,list2);
        }

        return dataList;
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

    //获取采集卡
    public List<TrDetectiontypEtempletObj> getData(TrDetectiontypEtempletObj trDetectiontypEtempletObj) {
        String id = trDetectiontypEtempletObj.getRid();
        String taskid = trDetectiontypEtempletObj.getTaskid();
        TrDetectiontypEtempletObj trDetectiontypEtempletObj1 = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj1.setPid(id);
        trDetectiontypEtempletObj1.setTaskid(taskid);
        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        List<TrDetectiontypEtempletObj> list = trDetectionTypeTempletObjDao.queryTrDetectionTypeTempletObjList(trDetectiontypEtempletObj1);
        return list;
    }

    public void insertTrDetectiontempletObj(TrDetectiontypEtempletObj data,String uuid,String date,String remark,String state,String result){
        List<TrDetectiontempletObj> trDetectiontempletObjs = new ArrayList<TrDetectiontempletObj>();
        TrDetectiontempletObj trDetectiontempletObj = new TrDetectiontempletObj();
        trDetectiontempletObj.setId(uuid);
        trDetectiontempletObj.setUpdateperson(userid);
        trDetectiontempletObj.setUpdatetime(date);
        trDetectiontempletObj.setRemarks(remark);
        trDetectiontempletObj.setStatus(state);
        trDetectiontempletObj.setResultdescribe(result);
        trDetectiontempletObjs.add(trDetectiontempletObj);
        TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
        trDetectionTempletObjDao.insertListData(trDetectiontempletObjs);
        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
        columnsMap.put("DETECTIONTEMPLET",uuid);
        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
        wheresMap.put("ID",data.getId());
        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
        trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap,wheresMap);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trDetectiontempletObj);
        list1.add(list2);
        sympMessageRealDao.addTextMessages(list1);
        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
        trDetectiontypEtempletObj.setDetectiontemplet(uuid);
        trDetectiontypEtempletObj.setId(data.getId());
        List<List<Object>> list3 = new ArrayList<List<Object>>();
        List<Object> list4 = new ArrayList<Object>();
        list4.add(trDetectiontypEtempletObj);
        list3.add(list4);
        sympMessageRealDao.updateTextMessages(list3);
    }

    class MyviewHolder implements View.OnClickListener{
        //普通模板
        TextView textView; //检查内容
        EditText result;   //检查结果
        EditText remarkinput;//备注
        TextView method,standard;//方法，标准
        GridView gridView;   //输入值
        LinearLayout general_item_image1;
        View view;
        TextView noise_textView;
        EditText noise_result,noise_remarkinput;

        HorizontalScrollView general_hlistview,noise_hlistview;  //显示图片的HorizontalScrollView
        LinearLayout gener_ll,noise_gener_ll,noise_item_ll;

        CheckBox checkYes,checkNo,noise_checkYes,noise_checkNo;
        GridViewforListView imagegridView,noise_gridview_group;

        //套管模板
        ImageView oil_ha_image,oil_hb_image,oil_hc_image,oil_ho_image,oil_ma_image,oil_mb_image,oil_mc_image,oil_mo_image,oil_la_image,oil_lb_image,oil_lc_image,oil_lo_image,oil_s1_image,oil_s2_image;
        EditText oil_ha_index,oil_hb_index,oil_hc_index,oil_ho_index,oil_ma_index,oil_mb_index,oil_mc_index,oil_mo_index,oil_la_index,oil_lb_index,oil_lc_index,oil_lo_index,oil_s1_index,oil_s2_index,noise_item_groupcount,noise_item_number,noise_avg;
        ProgressBar oil_ha_bar, oil_hb_bar, oil_hc_bar, oil_ho_bar, oil_la_bar, oil_lb_bar, oil_lc_bar, oil_lo_bar, oil_ma_bar, oil_mb_bar, oil_mc_bar, oil_mo_bar, oil_s1_bar, oil_s2_bar;

        //油位指示
        LinearLayout ha_image_camera,hb_image_camera,hc_image_camera,ho_image_camera,ma_image_camera,mb_image_camera,mc_image_camera,mo_image_camera,
                  la_image_camera,lb_image_camera,lc_image_camera,lo_image_camera,s1_image_camera,s2_image_camera;
        RelativeLayout ha_rl,hb_rl,hc_rl,ho_rl,ma_rl,mb_rl,mc_rl,mo_rl,la_rl,lb_rl,lc_rl,lo_rl,s1_rl,s2_rl;
        LinearLayout ha_image_delete,hb_image_delete,hc_image_delete,ho_image_delete,ma_image_delete,mb_image_delete,mc_image_delete,mo_image_delete,
                  la_image_delete,lb_image_delete,lc_image_delete,lo_image_delete,s1_image_delete,s2_image_delete;


        //油色谱模板
        ListView listView;

        LinearLayout linearLayout;//用来放在前面显示图片的采集卡
        HorizontalScrollView horizontalScrollView;

        MyItemClickListener mItemClickListener;

        public MyviewHolder(View itemView,int type,MyItemClickListener listener) {
            if(type==1){ //普通模板
                this.mItemClickListener = listener;
                textView= (TextView) itemView.findViewById(R.id.general_item_content);
                result= (EditText) itemView.findViewById(R.id.general_item_result);
                remarkinput= (EditText) itemView.findViewById(R.id.general_item_remarkinput);
                method= (TextView) itemView.findViewById(R.id.general_item_method);
                standard= (TextView) itemView.findViewById(R.id.general_item_standard);
                imagegridView= (GridViewforListView) itemView.findViewById(R.id.general_item_gv);
                general_hlistview= (HorizontalScrollView) itemView.findViewById(R.id.general_hlistview);
                gener_ll= (LinearLayout) itemView.findViewById(R.id.gener_ll);
                checkYes = (CheckBox) itemView.findViewById(R.id.general_checkYes);
                checkNo = (CheckBox) itemView.findViewById(R.id.general_checkNo);
                view = (View) itemView.findViewById(R.id.view);
                gridView= (GridView) itemView.findViewById(R.id.general_item_gv);
                general_item_image1 = (LinearLayout) itemView.findViewById(R.id.general_item_image1);
//              select_roject_state = (Switch)itemView.findViewById(R.id.select_roject_state);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    result.setEnabled(false);
                    remarkinput.setEnabled(false);
                    checkNo.setEnabled(false);
                    checkYes.setEnabled(false);
                    general_item_image1.setEnabled(false);
                    general_item_image1.setVisibility(View.INVISIBLE);
                }

            }
            if(type==3){
                this.mItemClickListener = listener;
                textView= (TextView) itemView.findViewById(R.id.oil_content);

                method= (TextView) itemView.findViewById(R.id.infrared_item_method);
                standard= (TextView) itemView.findViewById(R.id.infrared_item_standard);

                oil_ha_image = (ImageView) itemView.findViewById(R.id.oil_ha_image);
                oil_hb_image = (ImageView) itemView.findViewById(R.id.oil_hb_image);
                oil_hc_image = (ImageView) itemView.findViewById(R.id.oil_hc_image);
                oil_ho_image = (ImageView) itemView.findViewById(R.id.oil_ho_image);
                oil_ma_image = (ImageView) itemView.findViewById(R.id.oil_ma_image);
                oil_mb_image = (ImageView) itemView.findViewById(R.id.oil_mb_image);
                oil_mc_image = (ImageView) itemView.findViewById(R.id.oil_mc_image);
                oil_mo_image = (ImageView) itemView.findViewById(R.id.oil_mo_image);
                oil_la_image = (ImageView) itemView.findViewById(R.id.oil_la_image);
                oil_lb_image = (ImageView) itemView.findViewById(R.id.oil_lb_image);
                oil_lc_image = (ImageView) itemView.findViewById(R.id.oil_lc_image);
                oil_lo_image = (ImageView) itemView.findViewById(R.id.oil_lo_image);
                oil_s1_image = (ImageView) itemView.findViewById(R.id.oil_s1_image);
                oil_s2_image = (ImageView) itemView.findViewById(R.id.oil_s2_image);

                ha_image_delete= (LinearLayout) itemView.findViewById(R.id.ha_image_delete);
                hb_image_delete= (LinearLayout) itemView.findViewById(R.id.hb_image_delete);
                hc_image_delete= (LinearLayout) itemView.findViewById(R.id.hc_image_delete);
                ho_image_delete= (LinearLayout) itemView.findViewById(R.id.ho_image_delete);
                ma_image_delete= (LinearLayout) itemView.findViewById(R.id.ma_image_delete);
                mb_image_delete= (LinearLayout) itemView.findViewById(R.id.mb_image_delete);
                mc_image_delete= (LinearLayout) itemView.findViewById(R.id.mc_image_delete);
                mo_image_delete= (LinearLayout) itemView.findViewById(R.id.mo_image_delete);
                la_image_delete= (LinearLayout) itemView.findViewById(R.id.la_image_delete);
                lb_image_delete= (LinearLayout) itemView.findViewById(R.id.lb_image_delete);
                lc_image_delete= (LinearLayout) itemView.findViewById(R.id.lc_image_delete);
                lo_image_delete= (LinearLayout) itemView.findViewById(R.id.lo_image_delete);
                s1_image_delete= (LinearLayout) itemView.findViewById(R.id.s1_image_delete);
                s2_image_delete= (LinearLayout) itemView.findViewById(R.id.s2_image_delete);

                oil_ha_bar = (ProgressBar) itemView.findViewById(R.id.oil_ha_bar);
                oil_hb_bar = (ProgressBar) itemView.findViewById(R.id.oil_hb_bar);
                oil_hc_bar = (ProgressBar) itemView.findViewById(R.id.oil_hc_bar);
                oil_ho_bar = (ProgressBar) itemView.findViewById(R.id.oil_ho_bar);
                oil_ma_bar = (ProgressBar) itemView.findViewById(R.id.oil_ma_bar);
                oil_mb_bar = (ProgressBar) itemView.findViewById(R.id.oil_mb_bar);
                oil_mc_bar = (ProgressBar) itemView.findViewById(R.id.oil_mc_bar);
                oil_mo_bar = (ProgressBar) itemView.findViewById(R.id.oil_mo_bar);
                oil_la_bar = (ProgressBar) itemView.findViewById(R.id.oil_la_bar);
                oil_lb_bar = (ProgressBar) itemView.findViewById(R.id.oil_lb_bar);
                oil_lc_bar = (ProgressBar) itemView.findViewById(R.id.oil_lc_bar);
                oil_lo_bar = (ProgressBar) itemView.findViewById(R.id.oil_lo_bar);
                oil_s1_bar = (ProgressBar) itemView.findViewById(R.id.oil_s1_bar);
                oil_s2_bar = (ProgressBar) itemView.findViewById(R.id.oil_s2_bar);

                oil_ha_index = (EditText) itemView.findViewById(R.id.oil_ha_index);
                oil_hb_index = (EditText) itemView.findViewById(R.id.oil_hb_index);
                oil_hc_index = (EditText) itemView.findViewById(R.id.oil_hc_index);
                oil_ho_index = (EditText) itemView.findViewById(R.id.oil_ho_index);
                oil_ma_index = (EditText) itemView.findViewById(R.id.oil_ma_index);
                oil_mb_index = (EditText) itemView.findViewById(R.id.oil_mb_index);
                oil_mc_index = (EditText) itemView.findViewById(R.id.oil_mc_index);
                oil_mo_index = (EditText) itemView.findViewById(R.id.oil_mo_index);
                oil_la_index = (EditText) itemView.findViewById(R.id.oil_la_index);
                oil_lb_index = (EditText) itemView.findViewById(R.id.oil_lb_index);
                oil_lc_index = (EditText) itemView.findViewById(R.id.oil_lc_index);
                oil_lo_index = (EditText) itemView.findViewById(R.id.oil_lo_index);
                oil_s1_index = (EditText) itemView.findViewById(R.id.oil_s1_index);
                oil_s2_index = (EditText) itemView.findViewById(R.id.oil_s2_index);
                result = (EditText) itemView.findViewById(R.id.oil_result);
                remarkinput = (EditText) itemView.findViewById(R.id.oil_remarks_result);
                checkYes = (CheckBox) itemView.findViewById(R.id.oil_checkYes);
                checkNo = (CheckBox) itemView.findViewById(R.id.oil_checkNo);

                ha_image_camera= (LinearLayout) itemView.findViewById(R.id.ha_image_add_layout);
                hb_image_camera= (LinearLayout) itemView.findViewById(R.id.hb_image_add_layout);
                hc_image_camera= (LinearLayout) itemView.findViewById(R.id.hc_image_add_layout);
                ho_image_camera= (LinearLayout) itemView.findViewById(R.id.ho_image_add_layout);
                ma_image_camera= (LinearLayout) itemView.findViewById(R.id.ma_image_add_layout);
                mb_image_camera= (LinearLayout) itemView.findViewById(R.id.mb_image_add_layout);
                mc_image_camera= (LinearLayout) itemView.findViewById(R.id.mc_image_add_layout);
                mo_image_camera= (LinearLayout) itemView.findViewById(R.id.mo_image_add_layout);
                la_image_camera= (LinearLayout) itemView.findViewById(R.id.la_image_add_layout);
                lb_image_camera= (LinearLayout) itemView.findViewById(R.id.lb_image_add_layout);
                lc_image_camera= (LinearLayout) itemView.findViewById(R.id.lc_image_add_layout);
                lo_image_camera= (LinearLayout) itemView.findViewById(R.id.lo_image_add_layout);
                s1_image_camera= (LinearLayout) itemView.findViewById(R.id.s1_image_add_layout);
                s2_image_camera= (LinearLayout) itemView.findViewById(R.id.s2_image_add_layout);

                ha_rl= (RelativeLayout) itemView.findViewById(R.id.ha_rl);
                hb_rl= (RelativeLayout) itemView.findViewById(R.id.hb_rl);
                hc_rl= (RelativeLayout) itemView.findViewById(R.id.hc_rl);
                ho_rl= (RelativeLayout) itemView.findViewById(R.id.ho_rl);
                ma_rl= (RelativeLayout) itemView.findViewById(R.id.ma_rl);
                mb_rl= (RelativeLayout) itemView.findViewById(R.id.mb_rl);
                mc_rl= (RelativeLayout) itemView.findViewById(R.id.mc_rl);
                mo_rl= (RelativeLayout) itemView.findViewById(R.id.mo_rl);
                la_rl= (RelativeLayout) itemView.findViewById(R.id.la_rl);
                lb_rl= (RelativeLayout) itemView.findViewById(R.id.lb_rl);
                lc_rl= (RelativeLayout) itemView.findViewById(R.id.lc_rl);
                lo_rl= (RelativeLayout) itemView.findViewById(R.id.lo_rl);
                s1_rl= (RelativeLayout) itemView.findViewById(R.id.s1_rl);
                s2_rl= (RelativeLayout) itemView.findViewById(R.id.s2_rl);

                //注册事件
                registerDoubleClickListener(oil_ha_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_hb_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_hc_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_ho_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_ma_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_mb_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_mc_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_mo_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_la_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_lb_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_lc_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_lo_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_s1_image,this.mItemClickListener,context);
                registerDoubleClickListener(oil_s2_image,this.mItemClickListener,context);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    result.setEnabled(false);
                    remarkinput.setEnabled(false);
                    checkYes.setEnabled(false);
                    checkNo.setEnabled(false);
                    LinearLayout[] imageViews=new LinearLayout[]{
                            ha_image_camera,hb_image_camera,hc_image_camera,ho_image_camera,ma_image_camera,mb_image_camera,mc_image_camera,mo_image_camera,
                            la_image_camera,lb_image_camera,lc_image_camera,lo_image_camera,s1_image_camera,s2_image_camera
                    };
                    LinearLayout[] del=new LinearLayout[]{
                            ha_image_delete,hb_image_delete,hc_image_delete,ho_image_delete,
                            ma_image_delete,mb_image_delete,mc_image_delete,mo_image_delete,
                            la_image_delete,lb_image_delete,lc_image_delete,lo_image_delete,
                            s1_image_delete,s2_image_delete};
                    for(int i=0;i<imageViews.length;i++){
                        if(imageViews[i]!=null){
                            //imageViews[i].setEnabled(false);
                            imageViews[i].setVisibility(View.GONE);
                        }
                    }
                    for(int i=0;i<del.length;i++){
                        if(del[i]!=null){
                            //imageViews[i].setEnabled(false);
                            del[i].setVisibility(View.GONE);
                        }
                    }
                }
            }
            if(type==4){ //油色谱，欧版
                listView= (ListView) itemView.findViewById(R.id.oillist);
            }
            if(type==5){  //第一条采集卡
                linearLayout= (LinearLayout) itemView.findViewById(R.id.image_ll);
                horizontalScrollView= (HorizontalScrollView) itemView.findViewById(R.id.image_hlistview);
                gener_ll= (LinearLayout) itemView.findViewById(R.id.image_ll);
            }
            if(type==6){ //噪声
                method= (TextView) itemView.findViewById(R.id.noise_item_method);
                standard= (TextView) itemView.findViewById(R.id.noise_item_standard);

                noise_item_ll = (LinearLayout)itemView.findViewById(R.id.noise_item_ll);
                noise_textView= (TextView) itemView.findViewById(R.id.noise_item_content);
                noise_result= (EditText) itemView.findViewById(R.id.noise_item_result);
                noise_remarkinput= (EditText) itemView.findViewById(R.id.noise_item_remarkinput);
                noise_gridview_group= (GridViewforListView) itemView.findViewById(R.id.noise_gridview_group);
                noise_hlistview= (HorizontalScrollView) itemView.findViewById(R.id.general_hlistview);
                noise_gener_ll= (LinearLayout) itemView.findViewById(R.id.gener_ll);
                noise_checkYes = (CheckBox) itemView.findViewById(R.id.noise_checkYes);
                noise_checkNo = (CheckBox) itemView.findViewById(R.id.noise_checkNo);
                noise_item_groupcount = (EditText)itemView.findViewById(R.id.noise_item_groupcount);
                noise_item_number = (EditText)itemView.findViewById(R.id.noise_item_number);
                noise_avg = (EditText)itemView.findViewById(R.id.noise_avg);
                general_item_image1= (LinearLayout) itemView.findViewById(R.id.noise_item_image1);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    noise_result.setEnabled(false);
                    noise_remarkinput.setEnabled(false);
                    noise_checkYes.setEnabled(false);
                    noise_checkNo.setEnabled(false);
                    general_item_image1.setVisibility(View.INVISIBLE);

                    noise_item_groupcount.setEnabled(false);
                    noise_item_number.setEnabled(false);
                    noise_avg.setEnabled(false);
                }
                noise_item_number.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_NEXT){
                            noise_avg.setFocusable(true);
                            noise_avg.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.OnSingleClick(v);
            }
        }
    }

    MyItemClickListener mItemClickListener;
    public interface MyItemClickListener {
        public boolean OnSingleClick(View v);
        public boolean OnDoubleClick(View v);
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public static void registerDoubleClickListener(View view, final MyItemClickListener listener,final Context context){
        if(listener==null) return;
        view.setOnClickListener(new View.OnClickListener() {
            private static final int DOUBLE_CLICK_TIME = 350;//双击间隔时间350毫秒
            private boolean waitDouble = true;
            private Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    listener.OnSingleClick((View)msg.obj);
                }
            };
            //等待双击
            public void onClick(final View v) {
                if(waitDouble){
                    waitDouble = false; //与执行双击事件
                    new Thread(){
                        public void run() {
                            try{
                                Thread.sleep(DOUBLE_CLICK_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaperOne");

                            }//等待双击时间，否则执行单击事件
                            if(!waitDouble){
                                //如果过了等待事件还是预执行双击状态，则视为单击
                                waitDouble = true;
                                Message msg = handler.obtainMessage();
                                msg.obj = v;
                                handler.sendMessage(msg);}
                        }
                    }.start();
                }else{
                    waitDouble = true;
                    listener.OnDoubleClick(v);//执行双击
                }
            }
        });
    }

    private void createMethodeDialog(TrDetectiontempletObj trDetectiontempletObj,TrSpecialBushing trSpecialBushing,int flag) { //方法的dialong
        methodMaterialDialog = new MaterialDialog(context);
        methodMaterialDialog.setTitle("方法").setCanceledOnTouchOutside(true);

        if(flag==1){  //普通模板
            if(trDetectiontempletObj!=null){
                methodMaterialDialog.setMessage((trDetectiontempletObj.getDetectionmethod()!=null && !trDetectiontempletObj.getDetectionmethod().equals("null"))?trDetectiontempletObj.getDetectionmethod():"");
            }
        }else{ //套管
            if(trSpecialBushing!=null){
                methodMaterialDialog.setMessage((trSpecialBushing.getDetectionmethod()!=null && !trDetectiontempletObj.getDetectionmethod().equals("null"))?trSpecialBushing.getDetectionmethod():"");
            }
        }
        methodMaterialDialog.show();
    }

    private void createStandardDialog(TrDetectiontempletObj trDetectiontempletObj,TrSpecialBushing trSpecialBushing,int flag) { //标准的dialong
        standardMaterialDialog = new MaterialDialog(context);
        standardMaterialDialog.setTitle("标准").setCanceledOnTouchOutside(true);

        if(flag==1){ //普通模板
            if(trDetectiontempletObj!=null){
                String standard=trDetectiontempletObj.getDetectionstandard()!=null?trDetectiontempletObj.getDetectionstandard():"";
                standard=handleStandard(standard);
                standardMaterialDialog.setMessage(standard);
            }
        }else{ //套管
            if(trSpecialBushing!=null){
                String standard=trSpecialBushing.getDetectionstandard()!=null?trSpecialBushing.getDetectionstandard():"";
                standard=handleStandard(standard);
                standardMaterialDialog.setMessage(standard);
            }
        }
        standardMaterialDialog.show();
    }

    private String handleStandard(String standard) {
        StringBuffer buffer=null;
        int count=1;
        if(!standard.equals("")){
            buffer=new StringBuffer(standard);
            for(int i=0;i<standard.length();){
                int index = standard.indexOf("。", i);
                if(index+1==standard.length()){
                    return buffer.toString();
                }
                if(index!=-1){
                    buffer.insert(index+count,"\n");
                    count++;
                    i=index+1;
                }else{
                    return buffer.insert(standard.length(),"。").toString();
                }
            }
            return buffer.toString();
        }else{
            return "";
        }
    }
}
