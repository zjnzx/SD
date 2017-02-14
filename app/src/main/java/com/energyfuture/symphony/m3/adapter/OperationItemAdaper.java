package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.analysis.DataSyschronized;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.common.URLs;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletDataObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingDataDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingPositionDao;
import com.energyfuture.symphony.m3.dao.TrSpecialOliDataDao;
import com.energyfuture.symphony.m3.dao.TrSpecialWorkDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletDataObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletObj;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrSpecialBushing;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingData;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingPosition;
import com.energyfuture.symphony.m3.domain.TrSpecialOliData;
import com.energyfuture.symphony.m3.domain.TrSpecialWork;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.piclook.ImagePagerActivity;
import com.energyfuture.symphony.m3.ui.ListViewForScrollView;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;
import com.energyfuture.symphony.m3.wifi.util.WifiAdmin2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/9/11.
 * 二级巡检任务适配器
 */
public class OperationItemAdaper extends BaseExpandableListAdapter {
    private List<TrDetectiontypEtempletObj> data1;   //上层显示的数据
    public List<List<TrDetectiontypEtempletObj>> data2; //下层显示的数据
    private OperationItemActivity context;
    private ExpandableListView expandableListView;
    private List<TrSpecialOliData> oildatalist; //油色谱数据
    private boolean isOne = true;
    private String userid;
    private List<List<TrSpecialBushingData>> trSpecialBushingDatasList;
    private SympMessageRealDao sympMessageRealDao;
    private String pic_route;

    private MyviewHolder lastViewholder=null;
    private LinearLayout lastdellayout;
    private Handler handler1;

    public MaterialDialog methodMaterialDialog,standardMaterialDialog,dialog,photodialog,waitdialog;

    private int color,green,orange;

    private String firstid; //第一条Item的ID
    private String detectionname;
    private TrTask trTask;
    private TrSpecialBushingFileDao trSpecialBushingFileDao;
    private TrDetectionTempletFileObjDao trDetectionTempletFileObjDao;
    private TrDetectiontypEtempletObj trDetectiontypEtempletObj3;
    private String text2 = "";
    public boolean isOpen = false;
    private int lastgroupPosition=-1;
    private ImageView firstImageView;
    private EditText firstEditText;
    private TextView firstTextView;
    public boolean flag=false;
    public WifiAdmin2 wifiAdmin2;
    private LinearLayout dellayout;
    public OperationItemAdaper(Handler handler,List<TrDetectiontypEtempletObj> data1,
                               List<List<TrDetectiontypEtempletObj>> data2, Context context,String pic_route,
                               TrTask trTask,String detectionname,ExpandableListView job_list) {
        this.context = (OperationItemActivity) context;
        this.data1 = data1;
        this.data2 = data2;
        this.handler1=handler;
        this.pic_route = pic_route;
        this.trTask = trTask;
        this.detectionname = detectionname;
        this.expandableListView=job_list;
        userid = Constants.getLoginPerson(context).get("userId");
        color = this.context.getResources().getColor(R.color.rd_null_checked);
        green = this.context.getResources().getColor(R.color.rd_bc_checked);
        orange = this.context.getResources().getColor(R.color.rd_no_checked);
        trSpecialBushingDatasList = new ArrayList<List<TrSpecialBushingData>>();
        sympMessageRealDao = new SympMessageRealDao(context);
        trSpecialBushingFileDao = new TrSpecialBushingFileDao(context);
        trDetectionTempletFileObjDao = new TrDetectionTempletFileObjDao(context);

        wifiAdmin2 = new WifiAdmin2(context);

    }

    @Override
    public int getGroupCount() {
        return data1.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data2.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data1.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data2.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        trDetectiontypEtempletObj3 = data1.get(groupPosition);
        firstid=trDetectiontypEtempletObj3.getId();

        final String id=data1.get(groupPosition).getId();
        final String pid=data1.get(groupPosition).getPid();
        MyviewHolder myviewHolder;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.job_content_first_item,null);
            myviewHolder=new MyviewHolder(1,convertView,mItemClickListener);
            convertView.setTag(myviewHolder);
        }else{
            myviewHolder= (MyviewHolder) convertView.getTag();
        }
        myviewHolder.textView1.setText(trDetectiontypEtempletObj3.getDetectionname());
        if(trDetectiontypEtempletObj3.getStatus().equals("")){
            myviewHolder.upimage.setBackgroundResource(R.drawable.gray_round);
        }else if(trDetectiontypEtempletObj3.getStatus().equals("1")){
            myviewHolder.upimage.setBackgroundResource(R.drawable.green_round);
        }else if(trDetectiontypEtempletObj3.getStatus().equals("0")){
            myviewHolder.upimage.setBackgroundResource(R.drawable.orange_round);
        }

        dellayout=myviewHolder.job_content_del;
        dellayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setTitle("确认删除该检测点吗？")
                            .setMessage("")
                            .setPositiveButton(
                                    "确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                            columnsMap.put("STATUS", "2");
                                            Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                            wheresMap.put("ID", id);
                                            TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
                                            trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);

                                            TrDetectiontypEtempletObj trDetectiontypEtempletObj1=new TrDetectiontypEtempletObj();
                                            trDetectiontypEtempletObj1.setStatus("2");
                                            trDetectiontypEtempletObj1.setId(id);
                                            List<List<Object>> list1 = new ArrayList<List<Object>>();
                                            List<Object> list2 = new ArrayList<Object>();
                                            list2.add(trDetectiontypEtempletObj1);
                                            list1.add(list2);
                                            sympMessageRealDao.updateTextMessages(list1);

                                            data1.remove(groupPosition);
                                            data2.remove(groupPosition);

                                            dellayout.setVisibility(View.INVISIBLE);
                                            OperationItemAdaper.this.notifyDataSetChanged();
                                             context.setStatus(trTask.getTaskid(),pid);
                                            ((OperationItemActivity) context).inioadapter(groupPosition,-1);
                                            mMaterialDialog.dismiss();
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
                            .setCanceledOnTouchOutside(false).show();
                }
            });
        context.map.put(groupPosition,convertView);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final List<TrDetectiontypEtempletObj> list2 = data2.get(groupPosition);
        final TrDetectiontypEtempletObj trDetectiontypEtempletObj = list2.get(childPosition);
        final List<TrDetectiontypEtempletObj> data = getData(trDetectiontypEtempletObj);
        final List<List<TrDetectiontempletObj>> dataList = getDetectiontypEtempletObj(trDetectiontypEtempletObj);
        MyviewHolder myviewHolder = null;

        if (data != null && data.size() != 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.job_content_second_item, null);
            myviewHolder = new MyviewHolder(2, convertView, mItemClickListener);
            context.viewmap.put(groupPosition + "|" + childPosition, convertView);
            myviewHolder.textView2.setText(trDetectiontypEtempletObj.getDetectionname());
            myviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
            if(trDetectiontypEtempletObj.getStatus().equals("")){
                myviewHolder.downimage.setBackgroundResource(R.drawable.gray_round_child);
            }else if(trDetectiontypEtempletObj.getStatus().equals("1")){
                myviewHolder.downimage.setBackgroundResource(R.drawable.green_round_child);
            }else if(trDetectiontypEtempletObj.getStatus().equals("0")){
                myviewHolder.downimage.setBackgroundResource(R.drawable.orange_round_child);
            }
            if(isLastChild){
                if(myviewHolder.seconed_view.getVisibility()==View.GONE){
                    myviewHolder.seconed_view.setVisibility(View.VISIBLE);
                }
            }

            if (!trDetectiontypEtempletObj.getDetectionname().equals("油位")) {
                List<TrDetectiontempletFileObj> filelist = getFile(trDetectiontypEtempletObj);
                if (filelist.size() > 0) {
                    data.add(0, data.get(0));
                }
            }

            myviewHolder.job_content_del.setOnClickListener(new View.OnClickListener() {  //删除下面的所有采集卡
                @Override
                public void onClick(View v) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setTitle("确认删除该检测点吗？")
                            .setMessage("")
                            .setPositiveButton(
                                    "确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                            columnsMap.put("STATUS", "2");
                                            Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                            wheresMap.put("ID", trDetectiontypEtempletObj.getId());
                                            TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
                                            trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);

                                            TrDetectiontypEtempletObj trDetectiontypEtempletObj1=new TrDetectiontypEtempletObj();
                                            trDetectiontypEtempletObj1.setStatus("2");
                                            trDetectiontypEtempletObj1.setId(trDetectiontypEtempletObj.getId());
                                            List<List<Object>> list1 = new ArrayList<List<Object>>();
                                            List<Object> list2 = new ArrayList<Object>();
                                            list2.add(trDetectiontypEtempletObj1);
                                            list1.add(list2);
                                            sympMessageRealDao.updateTextMessages(list1);

                                            data2.get(groupPosition).remove(childPosition);
                                            OperationItemAdaper.this.notifyDataSetChanged();
                                            context.setStatus(trTask.getTaskid(),trDetectiontypEtempletObj.getPid());
                                            ((OperationItemActivity) context).inioadapter(groupPosition,childPosition);
                                            mMaterialDialog.dismiss();
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
                            .setCanceledOnTouchOutside(false).show();
                }
            });

            //点击一项关闭其它项
            final MyviewHolder finalMyviewHolder = myviewHolder;
            //点击第三层打开或关闭
            final MyviewHolder finalMyviewHolder2 = myviewHolder;
            myviewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastViewholder == null) {//打开
                        createwaitDialog();
                        if (finalMyviewHolder.linearLayout.getVisibility() == View.GONE) {
                            isOpen = true;
                            Animation mShowAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);//弹出效果
                            finalMyviewHolder.linearLayout.startAnimation(mShowAction);
                            finalMyviewHolder.linearLayout.setVisibility(View.VISIBLE);
                            finalMyviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey600_24dp);
                        } else {
                            isOpen = false;
                            Animation mHiddenAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);//隐藏效果
                            finalMyviewHolder.linearLayout.startAnimation(mHiddenAction);
                            finalMyviewHolder.linearLayout.setVisibility(View.GONE);
                            finalMyviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        }
                    }
                    if (lastViewholder != null && !finalMyviewHolder.equals(lastViewholder)) {//关闭当前打开的采集卡,打开点击的
                        isOpen = true;
                        Animation mHiddenAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);//隐藏效果
                        lastViewholder.linearLayout.startAnimation(mHiddenAction);
                        lastViewholder.linearLayout.setVisibility(View.GONE);
                        lastViewholder.image.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
                        lastdellayout.setVisibility(View.INVISIBLE);

                        createwaitDialog();

                        Animation mShowAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);//弹出效果
                        finalMyviewHolder.linearLayout.startAnimation(mShowAction);
                        finalMyviewHolder.linearLayout.setVisibility(View.VISIBLE);
                        finalMyviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey600_24dp);
                    }
                    if (finalMyviewHolder.equals(lastViewholder)) {
                        if (finalMyviewHolder.linearLayout.getVisibility() == View.GONE) {//打开
                            isOpen = true;
                            Animation mShowAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);//弹出效果
                            finalMyviewHolder.linearLayout.startAnimation(mShowAction);
                            finalMyviewHolder.linearLayout.setVisibility(View.VISIBLE);
                            finalMyviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey600_24dp);
                        } else {//关闭
                            isOpen = false;
                            Animation mHiddenAction = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                            finalMyviewHolder.linearLayout.startAnimation(mHiddenAction);
                            finalMyviewHolder.linearLayout.setVisibility(View.GONE);
                            finalMyviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);

                            if(finalMyviewHolder.job_content_del.getVisibility()==View.VISIBLE){
                                finalMyviewHolder.job_content_del.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    lastViewholder = finalMyviewHolder;
                    lastdellayout=finalMyviewHolder.job_content_del;
                    if (isOpen) {
                        //查询模板类型
                        TrDetectionTypeTempletObjDao objDao = new TrDetectionTypeTempletObjDao(context);
                        TrDetectiontypEtempletObj objParam = new TrDetectiontypEtempletObj();
                        String id = trDetectiontypEtempletObj.getId();
                        String rid = trDetectiontypEtempletObj.getRid();
                        objParam.setPid(rid);
                        List<TrDetectiontypEtempletObj> list = objDao.queryTrDetectionTypeTempletObjList(objParam);
                        if (list != null && list.size() > 0) {
                            TrDetectiontypEtempletObj objdata = list.get(0);
                            if (objdata.getTemplettype().equals("general")) {
                                //普通模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "GENERAL", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        final List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        final OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);
                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();

                                        if(finalMyviewHolder.job_content_del.getVisibility()==View.INVISIBLE){
                                            finalMyviewHolder.job_content_del.setVisibility(View.VISIBLE);
                                        }

                                        Constants.generalhandler=new Handler(){
                                            @Override
                                            public void handleMessage(Message msg) {
                                                if(msg.what==1){
                                                    adapterone.notifyDataSetChanged();
                                                }
                                            }
                                        };
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                            if (objdata.getTemplettype().equals("bushing")) {
                                //套管模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "BUSHING", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);
                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        adapterone.setOnItemClickListener(context);
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                            if (objdata.getTemplettype().equals("oli")) {
                                //油色谱模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "OLI", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);
                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                            if (objdata.getTemplettype().equals("oillevel")) {
                                //油位模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "OILLEVEL", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {

                                        final List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        final OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);

                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        adapterone.setOnItemClickListener(context);
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();

                                        Constants.bushinghandler=new Handler(){
                                                @Override
                                                public void handleMessage(Message msg) {
                                                    adapterone.notifyDataSetChanged();
                                            }
                                            };
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                            if (objdata.getTemplettype().equals("work")) {
                                //作业情况模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "WORK", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);
                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                            if (objdata.getTemplettype().equals("noise")) {
                                //作业情况模板
                                final Map<String, Object> condMap = Constants.getRequestParam(context, "INSPINFO", "NOISE", id + "@@" + rid + "@@" + trTask.getTaskid());
                                final Handler handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        List<TrDetectiontypEtempletObj> data2 = getData(trDetectiontypEtempletObj);
                                        OperationItemAdaperOne adapterone = new OperationItemAdaperOne(handler1, context, dataList, data2, pic_route, trDetectiontypEtempletObj, trTask);
                                        finalMyviewHolder2.listView.setAdapter(adapterone);
                                        adapterone.notifyDataSetChanged();
                                        expandableListView.setSelectedChild(groupPosition,childPosition,false);
                                        waitdialog.dismiss();
                                    }
                                };
                                new DataSyschronized(context).getDataFromWeb(condMap, handler);
                            }
                        }
                    }
                }
            });
            if ((isLastChild && groupPosition != lastgroupPosition)||(flag==true&&isLastChild)) {
                handler1.sendEmptyMessage(1);
                lastgroupPosition = groupPosition;
                flag=false;
            }
            return convertView;
        } else {
          /*  if(convertView==null){*/
            if (trDetectiontypEtempletObj.getTemplettype().equals("")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.job_content_second_item, null);
                myviewHolder = new MyviewHolder(2, convertView, mItemClickListener);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("general")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.general_item, null);
                dellayout.setVisibility(View.VISIBLE);
                myviewHolder = new MyviewHolder(7, convertView, mItemClickListener);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("work")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.oper_cond_record, null);
                myviewHolder = new MyviewHolder(4, convertView, mItemClickListener);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("bushing")) {
                convertView = LayoutInflater.from(context).inflate(R.layout.bushing_item, null);
                myviewHolder = new MyviewHolder(3, convertView, mItemClickListener);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("oli")) { //油色谱ma
                convertView = LayoutInflater.from(context).inflate(R.layout.oil_list_item1, null);
                myviewHolder = new MyviewHolder(6, convertView, mItemClickListener);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("ultrasonic")) {
                if (childPosition == 0) {//设置第一条为图片的采集卡
                    convertView = LayoutInflater.from(context).inflate(R.layout.ult_pic_item, null);
                    myviewHolder = new MyviewHolder(10, convertView, null);
                    ultImageView(myviewHolder);
                    return convertView;
                }
                convertView = LayoutInflater.from(context).inflate(R.layout.general_item, null);
                myviewHolder = new MyviewHolder(8, convertView, mItemClickListener);
            }
           /*     convertView.setTag(myviewHolder);
            }else{
                myviewHolder= (MyviewHolder) convertView.getTag();
            }*/

            if (trDetectiontypEtempletObj.getTemplettype().equals("")) {
                myviewHolder.textView2.setText(trDetectiontypEtempletObj.getDetectionname());
                myviewHolder.image.setImageResource(R.drawable.ic_keyboard_arrow_down_grey600_24dp);
            }


            if (trDetectiontypEtempletObj.getTemplettype().equals("general")) {
                TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);

                if (trDetectionTempletObjDao.getTrDetectiontempletObj(trDetectiontypEtempletObj).size() != 0) {
                    final TrDetectiontempletObj trDetectiontempletObj = trDetectionTempletObjDao.getTrDetectiontempletObj(trDetectiontypEtempletObj).get(0);

                    if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                        myviewHolder.result.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? " " : trDetectiontempletObj.getResultdescribe());
                        myviewHolder.remarkinput.setText(trDetectiontempletObj.getRemarks().equals("null") ? " " : trDetectiontempletObj.getRemarks());
                    }else{
                        myviewHolder.result.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? "" : trDetectiontempletObj.getResultdescribe());
                        myviewHolder.remarkinput.setText(trDetectiontempletObj.getRemarks().equals("null") ? "" : trDetectiontempletObj.getRemarks());
                    }

                    String method = (trDetectiontempletObj.getDetectionmethod() != null && !("null").equals(trDetectiontempletObj.getDetectionmethod())) ? trDetectiontempletObj.getDetectionmethod() : "方法";
                    if (method.length() <= 4) {
                        myviewHolder.method.setText(method);
                    } else {
                        myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createMethodeDialog(trDetectiontempletObj, null, 1);
                            }
                        });
                    }
                    myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createStandardDialog(trDetectiontempletObj, null, 1);
                        }
                    });

                    if (trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("0")) {
                        myviewHolder.checkNo.setChecked(true);
                        myviewHolder.checkNo.setTextColor(orange);
                        myviewHolder.checkYes.setChecked(false);
                        myviewHolder.checkYes.setTextColor(color);
                    }
                    if (trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("1")) {
                        myviewHolder.checkYes.setChecked(true);
                        myviewHolder.checkYes.setTextColor(green);
                        myviewHolder.checkNo.setChecked(false);
                        myviewHolder.checkNo.setTextColor(color);
                    }

                    List<TrDetectiontempletDataObj> list = getTrDetectionTempletData(trDetectiontempletObj.getId());
                    if (list.size() > 0) {
                        GridViewAdapter gridViewAdapter = new GridViewAdapter(context,list,trTask);
                        myviewHolder.gridView.setVisibility(View.VISIBLE);
                        myviewHolder.gridView.setAdapter(gridViewAdapter);
                    }

                    //检测结果失去焦点修改
                    final MyviewHolder finalMyviewHolder = myviewHolder;
                    finalMyviewHolder.result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // 失去焦点时的处理内容
                                String result = finalMyviewHolder.result.getText().toString();
                                if (!trDetectiontempletObj.getResultdescribe().equals(result)) {
                                    String date = Constants.dateformat2.format(new Date());
                                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                    columnsMap.put("RESULTDESCRIBE", result);
                                    columnsMap.put("UPDATETIME", date);
                                    columnsMap.put("UPDATEPERSON", userid);
                                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                    wheresMap.put("ID", trDetectiontempletObj.getId());
                                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                    trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
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
                    finalMyviewHolder.remarkinput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                // 失去焦点时的处理内容
                                String remark = finalMyviewHolder.remarkinput.getText().toString();
                                if (!trDetectiontempletObj.getRemarks().equals(remark)) {
                                    String date = Constants.dateformat2.format(new Date());
                                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                    columnsMap.put("REMARKS", remark);
                                    columnsMap.put("UPDATETIME", date);
                                    columnsMap.put("UPDATEPERSON", userid);
                                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                    wheresMap.put("ID", trDetectiontempletObj.getId());
                                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                    trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
                                    TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                                    trDetectiontempletObj2.setRemarks(remark);
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
                    //修改状态
                    finalMyviewHolder.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder.checkNo.setTextColor(orange);
                                finalMyviewHolder.checkYes.setChecked(false);
                                finalMyviewHolder.checkYes.setTextColor(color);
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
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trDetectiontempletObj2);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                                updateTaskState();
                                updateStatus(trDetectiontypEtempletObj,"0");
                            } else {
                                finalMyviewHolder.checkNo.setTextColor(color);
                            }
                        }
                    });

                    finalMyviewHolder.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder.checkYes.setTextColor(green);
                                finalMyviewHolder.checkNo.setChecked(false);
                                finalMyviewHolder.checkNo.setTextColor(color);
                                String date = Constants.dateformat2.format(new Date());
                                Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                columnsMap.put("STATUS", "1");
                                columnsMap.put("UPDATETIME", date);
                                columnsMap.put("UPDATEPERSON", userid);
                                Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                wheresMap.put("ID", trDetectiontempletObj.getId());
                                TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
                                TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                                trDetectiontempletObj2.setStatus("1");
                                trDetectiontempletObj2.setUpdatetime(date);
                                trDetectiontempletObj2.setUpdateperson(userid);
                                trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trDetectiontempletObj2);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                                updateTaskState();
                                updateStatus(trDetectiontypEtempletObj,"1");
                            } else {
                                finalMyviewHolder.checkYes.setTextColor(color);
                            }
                        }
                    });
                } else {
                    final String uuid = Constants.getUuid();
                    final String date = Constants.dateformat2.format(new Date());
                    final MyviewHolder finalMyviewHolder = myviewHolder;
                    finalMyviewHolder.result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                String result = finalMyviewHolder.result.getText().toString();
                                String remark = finalMyviewHolder.remarkinput.getText().toString();
                                String state = "";
                                if (finalMyviewHolder.checkYes.isChecked() == true) {
                                    state = "1";
                                }
                                if (finalMyviewHolder.checkNo.isChecked() == true) {
                                    state = "0";
                                }
                                if (!result.equals("")) {
                                    insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, state, result);
                                }
                            }
                        }
                    });
                    finalMyviewHolder.remarkinput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                String result = finalMyviewHolder.result.getText().toString();
                                String remark = finalMyviewHolder.remarkinput.getText().toString();
                                String state = "";
                                if (finalMyviewHolder.checkYes.isChecked() == true) {
                                    state = "1";
                                }
                                if (finalMyviewHolder.checkNo.isChecked() == true) {
                                    state = "0";
                                }
                                if (!remark.equals("")) {
                                    insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, state, result);
                                }
                            }
                        }
                    });
                    finalMyviewHolder.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder.checkNo.setTextColor(orange);
                                finalMyviewHolder.checkYes.setChecked(false);
                                finalMyviewHolder.checkYes.setTextColor(color);
                                String result = finalMyviewHolder.result.getText().toString();
                                String remark = finalMyviewHolder.remarkinput.getText().toString();
                                insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, "0", result);
                                updateTaskState();
                                updateStatus(trDetectiontypEtempletObj,"0");
                            } else {
                                finalMyviewHolder.checkNo.setTextColor(color);
                            }
                        }
                    });

                    finalMyviewHolder.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder.checkYes.setTextColor(green);
                                finalMyviewHolder.checkNo.setChecked(false);
                                finalMyviewHolder.checkNo.setTextColor(color);
                                String result = finalMyviewHolder.result.getText().toString();
                                String remark = finalMyviewHolder.remarkinput.getText().toString();
                                insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, "1", result);
                                updateTaskState();
                                updateStatus(trDetectiontypEtempletObj,"1");
                            } else {
                                finalMyviewHolder.checkYes.setTextColor(color);
                            }
                        }
                    });
                }
                List<TrDetectiontempletFileObj> filelist = getFileItem(trDetectiontypEtempletObj);
                List<List<TrDetectiontempletFileObj>> fileObjGroup = new ArrayList<List<TrDetectiontempletFileObj>>();//存放采集卡下多组图片
                List<String> idList = getFileItemInfId(trDetectiontypEtempletObj);
                for (int i = 0; i < idList.size(); i++) {
                    TrDetectiontempletFileObj fileObj = new TrDetectiontempletFileObj();
                    fileObj.setSignid(idList.get(i));
                    List<TrDetectiontempletFileObj> fileObjList = getFileItemInf(fileObj);
                    fileObjGroup.add(fileObjList);
                }
                if (detectionname.equals("红外测温")) {
                    if (idList.size() > 0||!trDetectiontypEtempletObj.getPiccount().equals("0")) {
                        getViewforInfrared(fileObjGroup.size(), myviewHolder, fileObjGroup, trDetectiontypEtempletObj);  //显示图片
                    } else {
                        myviewHolder.view.setVisibility(View.GONE);
                        myviewHolder.general_hlistview.setVisibility(View.GONE);
                    }
                } else {
                    if (filelist.size() >0||!trDetectiontypEtempletObj.getPiccount().equals("0")) {
                        getViewforcommon(filelist.size(), myviewHolder, filelist, trDetectiontypEtempletObj);  //显示图片
                    } else {
                        myviewHolder.view.setVisibility(View.GONE);
                        myviewHolder.general_hlistview.setVisibility(View.GONE);
                    }
                }

                myviewHolder.textView3.setText(trDetectiontypEtempletObj.getDetectionname());
                myviewHolder.general_item_image1.setTag(R.id.item_id, trDetectiontypEtempletObj.getId());
                myviewHolder.general_item_image1.setTag(R.id.pid, trDetectiontypEtempletObj.getPid());
                myviewHolder.general_item_image1.setTag(R.id.groupPosition_id, groupPosition);
                myviewHolder.general_item_image1.setTag(R.id.childPosition_id, childPosition);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("work")) {
                List<TrSpecialWork> workdatalist = getWorkrecord(trDetectiontypEtempletObj);
                WorkListAdapter workListAdapter = new WorkListAdapter(context, workdatalist, trDetectiontypEtempletObj, trTask);
                workListAdapter.setName(trDetectiontypEtempletObj.getDetectionname());
                myviewHolder.workrecordlv.setAdapter(workListAdapter);
            }else if (trDetectiontypEtempletObj.getTemplettype().equals("bushing")) {
                myviewHolder.textView3.setText(trDetectiontypEtempletObj.getDetectionname());
                final List<TrSpecialBushing> trSpecialBushingList = getbushing(trDetectiontypEtempletObj.getId());
                if(trSpecialBushingList.size()!=0){
                    String detecMethod = trSpecialBushingList.get(0).getDetectionmethod();
                    String method = (detecMethod != null && !("null").equals(detecMethod)) ? detecMethod : "方法";
                    if (method.length() <= 4) {
                        myviewHolder.method.setText(method);
                    } else {
                        myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createMethodeDialog(null, trSpecialBushingList.get(0), 2);
                            }
                        });
                    }
                    myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createStandardDialog(null, trSpecialBushingList.get(0), 2);
                        }
                    });
                }
                for (TrSpecialBushing trSpecialBushing : trSpecialBushingList) {
                    myviewHolder.bushing_item_result.setText(trSpecialBushing.getResultdescribe().equals("null") ? " " : trSpecialBushing.getResultdescribe());
                    myviewHolder.remarks_result.setText(trSpecialBushing.getRemarks().equals("null") ? " " : trSpecialBushing.getRemarks());

                    if (trSpecialBushingList.get(0).getStatus() != null && trSpecialBushingList.get(0).getStatus().equals("0")) {
                        myviewHolder.checkNo.setChecked(true);
                        myviewHolder.checkNo.setTextColor(orange);
                        myviewHolder.checkYes.setChecked(false);
                        myviewHolder.checkYes.setTextColor(color);
                    }
                    if (trSpecialBushingList.get(0).getStatus() != null && trSpecialBushingList.get(0).getStatus().equals("1")) {
                        myviewHolder.checkYes.setChecked(true);
                        myviewHolder.checkYes.setTextColor(green);
                        myviewHolder.checkNo.setChecked(false);
                        myviewHolder.checkNo.setTextColor(color);
                    }

                    List<TrSpecialBushingPosition> trSpecialBushingPositionList = getBushingData(trSpecialBushing.getId());
                    for (TrSpecialBushingPosition trSpecialBushingPosition :trSpecialBushingPositionList){
                        List<TrSpecialBushingData> trSpecialBushingDataList = getbushingObjData(trSpecialBushingPosition.getId());
                        trSpecialBushingDatasList.add(trSpecialBushingDataList);
                        TrSpecialBushingFile bushingFile = new TrSpecialBushingFile();
                        bushingFile.setPositionid(trSpecialBushingPosition.getId());
                        List<TrSpecialBushingFile> trSpecialBushingFileList = getbushingFile(bushingFile);
                        if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("高压")){
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HA")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.ha_infimage_number,myviewHolder.ha_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.ha_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ha_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.ha_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.ha_visibleimage_number,myviewHolder.ha_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.ha_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ha_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.ha_visibleimage_show);
                                    }
                                    myviewHolder.ha_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.ha_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.ha_max,myviewHolder.ha_min,myviewHolder.ha_avg,trSpecialBushingDataList);




                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HB")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.hb_infimage_number,myviewHolder.hb_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.hb_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.hb_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.hb_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.hb_visibleimage_number,myviewHolder.hb_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.hb_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.hb_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.hb_visibleimage_show);
                                    }
                                    myviewHolder.hb_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.hb_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.hb_max,myviewHolder.hb_min,myviewHolder.hb_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("HC")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.hc_infimage_number,myviewHolder.hc_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.hc_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.hc_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.hc_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.hc_visibleimage_number,myviewHolder.hc_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.hc_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.hc_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.hc_visibleimage_show);
                                    }
                                    myviewHolder.hc_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.hc_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.hc_max,myviewHolder.hc_min,myviewHolder.hc_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("H0")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.ho_infimage_number,myviewHolder.ho_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.ho_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ho_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.ho_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.ho_visibleimage_number,myviewHolder.ho_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.ho_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ho_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.ho_visibleimage_show);
                                    }
                                    myviewHolder.ho_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.ho_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.ho_max,myviewHolder.ho_min,myviewHolder.ho_avg,trSpecialBushingDataList);
                            }
                        }
                        if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("中压")&&trSpecialBushingFileList.size()!=0){
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MA")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.ma_infimage_number,myviewHolder.ma_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.ma_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ma_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.ma_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.ma_visibleimage_number,myviewHolder.ma_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.ma_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.ma_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.ma_visibleimage_show);
                                    }
                                    myviewHolder.ma_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.ma_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.ma_max, myviewHolder.ma_min, myviewHolder.ma_avg, trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MB")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.mb_infimage_number,myviewHolder.mb_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.mb_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mb_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.mb_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.mb_visibleimage_number,myviewHolder.mb_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.mb_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mb_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.mb_visibleimage_show);
                                    }
                                    myviewHolder.mb_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.mb_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.mb_max,myviewHolder.mb_min,myviewHolder.mb_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("MC")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.mc_infimage_number,myviewHolder.mc_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.mc_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mc_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.mc_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.mc_visibleimage_number,myviewHolder.mc_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.mc_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mc_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.mc_visibleimage_show);
                                    }
                                    myviewHolder.mc_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.mc_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.mc_max,myviewHolder.mc_min,myviewHolder.mc_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("M0")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.mo_infimage_number,myviewHolder.mo_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.mo_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mo_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.mo_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.mo_visibleimage_number,myviewHolder.mo_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.mo_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.mo_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.mo_visibleimage_show);
                                    }
                                    myviewHolder.mo_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.mo_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.mo_max,myviewHolder.mo_min,myviewHolder.mo_avg,trSpecialBushingDataList);
                            }
                        }
                        if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("低压")&&trSpecialBushingFileList.size()!=0){
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("La")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.la_infimage_number,myviewHolder.la_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.la_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.la_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.la_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.la_visibleimage_number,myviewHolder.la_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.la_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.la_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.la_visibleimage_show);
                                    }
                                    myviewHolder.la_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.la_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.la_max,myviewHolder.la_min,myviewHolder.la_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("Lb")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.lb_infimage_number,myviewHolder.lb_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.lb_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lb_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.lb_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.lb_visibleimage_number,myviewHolder.lb_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.lb_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lb_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.lb_visibleimage_show);
                                    }
                                    myviewHolder.lb_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.lb_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.lb_max,myviewHolder.lb_min,myviewHolder.lb_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("Lc")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.lc_infimage_number,myviewHolder.lc_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.lc_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lc_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.lc_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.lc_visibleimage_number,myviewHolder.lc_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.lc_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lc_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.lc_visibleimage_show);
                                    }
                                    myviewHolder.lc_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.lc_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.lc_max,myviewHolder.lc_min,myviewHolder.lc_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("L0")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.lo_infimage_number,myviewHolder.lo_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.lo_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lo_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.lo_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.lo_visibleimage_number,myviewHolder.lo_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.lo_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.lo_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.lo_visibleimage_show);
                                    }
                                    myviewHolder.lo_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.lo_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.lo_max,myviewHolder.lo_min,myviewHolder.lo_avg,trSpecialBushingDataList);
                                }
                        }
                        if(trSpecialBushingPosition.getVoltage() != null && trSpecialBushingPosition.getVoltage().equals("稳压")&&trSpecialBushingFileList.size()!=0){
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("s1")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.s1_infimage_number,myviewHolder.s1_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.s1_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.s1_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.s1_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.s1_visibleimage_number,myviewHolder.s1_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.s1_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.s1_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.s1_visibleimage_show);
                                    }
                                    myviewHolder.s1_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.s1_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.s1_max,myviewHolder.s1_min,myviewHolder.s1_avg,trSpecialBushingDataList);
                            }
                            if(trSpecialBushingPosition.getPhash() != null && trSpecialBushingPosition.getPhash().equals("s2")){
                                    if(trSpecialBushingFileList.get(0).getOrderby().equals("1")){
                                        bushingFile(myviewHolder.s2_infimage_number,myviewHolder.s2_infimage_show,trSpecialBushingFileList.get(0),trSpecialBushingPosition,0,myviewHolder.s2_infimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.s2_infimage_number,trSpecialBushingFileList.get(0),trSpecialBushingPosition.getId(),myviewHolder.s2_infimage_show);
                                    }
                                    if(trSpecialBushingFileList.get(1).getOrderby().equals("2")){
                                        bushingFile(myviewHolder.s2_visibleimage_number,myviewHolder.s2_visibleimage_show,trSpecialBushingFileList.get(1),trSpecialBushingPosition,1,myviewHolder.s2_visibleimage_bar);
                                        setOnfocuslistenerBushing(myviewHolder.s2_visibleimage_number,trSpecialBushingFileList.get(1),trSpecialBushingPosition.getId(),myviewHolder.s2_visibleimage_show);
                                    }
                                    myviewHolder.s2_deletepictures.setTag(R.id.infimage_id,trSpecialBushingFileList.get(0).getId());
                                    myviewHolder.s2_deletepictures.setTag(R.id.visibleimage_id,trSpecialBushingFileList.get(1).getId());
                                    bushingData(myviewHolder.s2_max,myviewHolder.s2_min,myviewHolder.s2_avg,trSpecialBushingDataList);
                            }
                        }
                    }
                }

                final MyviewHolder finalMyviewHolder1 = myviewHolder;
                myviewHolder.ha_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.ha_deletepictures, finalMyviewHolder1.ha_infimage_number, finalMyviewHolder1.ha_visibleimage_number, finalMyviewHolder1.ha_infimage_show, finalMyviewHolder1.ha_visibleimage_show);
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
                myviewHolder.hb_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.hb_deletepictures,finalMyviewHolder1.hb_infimage_number,finalMyviewHolder1.hb_visibleimage_number,finalMyviewHolder1.hb_infimage_show,finalMyviewHolder1.hb_visibleimage_show);
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
                myviewHolder.hc_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.hc_deletepictures,finalMyviewHolder1.hc_infimage_number,finalMyviewHolder1.hc_visibleimage_number,finalMyviewHolder1.hc_infimage_show,finalMyviewHolder1.hc_visibleimage_show);
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
                myviewHolder.ho_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.ho_deletepictures,finalMyviewHolder1.ho_infimage_number,finalMyviewHolder1.ho_visibleimage_number,finalMyviewHolder1.ho_infimage_show,finalMyviewHolder1.ho_visibleimage_show);
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
                myviewHolder.ma_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.ma_deletepictures,finalMyviewHolder1.ma_infimage_number,finalMyviewHolder1.ma_visibleimage_number,finalMyviewHolder1.ma_infimage_show,finalMyviewHolder1.ma_visibleimage_show);
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
                myviewHolder.mb_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.mb_deletepictures,finalMyviewHolder1.mb_infimage_number,finalMyviewHolder1.mb_visibleimage_number,finalMyviewHolder1.mb_infimage_show,finalMyviewHolder1.mb_visibleimage_show);
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
                myviewHolder.mc_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.mc_deletepictures,finalMyviewHolder1.mc_infimage_number,finalMyviewHolder1.mc_visibleimage_number,finalMyviewHolder1.mc_infimage_show,finalMyviewHolder1.mc_visibleimage_show);
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
                myviewHolder.mo_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.mo_deletepictures,finalMyviewHolder1.mo_infimage_number,finalMyviewHolder1.mo_visibleimage_number,finalMyviewHolder1.mo_infimage_show,finalMyviewHolder1.mo_visibleimage_show);
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
                myviewHolder.la_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.la_deletepictures,finalMyviewHolder1.la_infimage_number,finalMyviewHolder1.la_visibleimage_number,finalMyviewHolder1.la_infimage_show,finalMyviewHolder1.la_visibleimage_show);
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
                myviewHolder.lb_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.lb_deletepictures,finalMyviewHolder1.lb_infimage_number,finalMyviewHolder1.lb_visibleimage_number,finalMyviewHolder1.lb_infimage_show,finalMyviewHolder1.lb_visibleimage_show);
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
                myviewHolder.lc_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.lc_deletepictures,finalMyviewHolder1.lc_infimage_number,finalMyviewHolder1.lc_visibleimage_number,finalMyviewHolder1.lc_infimage_show,finalMyviewHolder1.lc_visibleimage_show);
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
                myviewHolder.lo_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.lo_deletepictures,finalMyviewHolder1.lo_infimage_number,finalMyviewHolder1.lo_visibleimage_number,finalMyviewHolder1.lo_infimage_show,finalMyviewHolder1.lo_visibleimage_show);
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
                myviewHolder.s1_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.s1_deletepictures,finalMyviewHolder1.s1_infimage_number,finalMyviewHolder1.s1_visibleimage_number,finalMyviewHolder1.s1_infimage_show,finalMyviewHolder1.s1_visibleimage_show);
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
                myviewHolder.s2_deletepictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                        mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteInfBusPhoto(finalMyviewHolder1.s2_deletepictures,finalMyviewHolder1.s2_infimage_number,finalMyviewHolder1.s2_visibleimage_number,finalMyviewHolder1.s2_infimage_show,finalMyviewHolder1.s2_visibleimage_show);
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
                //获取图片
                final MyviewHolder finalMyviewHolder3 = myviewHolder;

                //EditText失去焦点修改值
                    finalMyviewHolder1.ha_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ha_max);
                            }
                        }
                    });
                    finalMyviewHolder1.ha_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ha_min);
                            }
                        }
                    });
                    finalMyviewHolder1.ha_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ha_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.hb_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hb_max);
                            }
                        }
                    });
                    finalMyviewHolder1.hb_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hb_min);
                            }
                        }
                    });
                    finalMyviewHolder1.hb_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hb_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.hc_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hc_max);
                            }
                        }
                    });
                    finalMyviewHolder1.hc_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hc_min);
                            }
                        }
                    });
                    finalMyviewHolder1.hc_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.hc_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.ho_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ho_max);
                            }
                        }
                    });
                    finalMyviewHolder1.ho_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ho_min);
                            }
                        }
                    });
                    finalMyviewHolder1.ho_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ho_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.ma_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ma_max);
                            }
                        }
                    });
                    finalMyviewHolder1.ma_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ma_min);
                            }
                        }
                    });
                    finalMyviewHolder1.ma_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.ma_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.mb_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mb_max);
                            }
                        }
                    });
                    finalMyviewHolder1.mb_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mb_min);
                            }
                        }
                    });
                    finalMyviewHolder1.mb_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mb_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.mc_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mc_max);
                            }
                        }
                    });
                    finalMyviewHolder1.mc_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mc_min);
                            }
                        }
                    });
                    finalMyviewHolder1.mc_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mc_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.mo_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mo_max);
                            }
                        }
                    });
                    finalMyviewHolder1.mo_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mo_min);
                            }
                        }
                    });
                    finalMyviewHolder1.mo_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.mo_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.la_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.la_max);
                            }
                        }
                    });
                    finalMyviewHolder1.la_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.la_min);
                            }
                        }
                    });
                    finalMyviewHolder1.la_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.la_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.lb_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lb_max);
                            }
                        }
                    });
                    finalMyviewHolder1.lb_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lb_min);
                            }
                        }

                    });
                    finalMyviewHolder1.lb_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lb_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.lc_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lc_max);
                            }
                        }
                    });
                    finalMyviewHolder1.lc_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lc_min);
                            }
                        }
                    });
                    finalMyviewHolder1.lc_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lc_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.lo_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lo_max);
                            }
                        }
                    });
                    finalMyviewHolder1.lo_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lo_min);
                            }
                        }
                    });
                    finalMyviewHolder1.lo_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.lo_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.s1_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s1_max);
                            }
                        }
                    });
                    finalMyviewHolder1.s1_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s1_min);
                            }
                        }
                    });
                    finalMyviewHolder1.s1_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s1_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.s2_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s2_max);
                            }
                        }
                    });
                    finalMyviewHolder1.s2_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s2_min);
                            }
                        }
                    });
                    finalMyviewHolder1.s2_avg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                updateBushingObj(finalMyviewHolder1.s2_avg);
                            }
                        }
                    });
                    finalMyviewHolder1.bushing_item_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                String result = finalMyviewHolder1.bushing_item_result.getText().toString();
                                if (!trSpecialBushingList.get(0).getResultdescribe().equals(result)) {
                                    String date = Constants.dateformat2.format(new Date());
                                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                    columnsMap.put("RESULTDESCRIBE", result);
                                    columnsMap.put("UPDATETIME", date);
                                    columnsMap.put("UPDATEPERSON", userid);
                                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                    wheresMap.put("ID", trSpecialBushingList.get(0).getId());
                                    TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                    trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                                    TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
                                    trSpecialBushing.setResultdescribe(result);
                                    trSpecialBushing.setUpdatetime(date);
                                    trSpecialBushing.setUpdateperson(userid);
                                    trSpecialBushing.setId(trSpecialBushingList.get(0).getId());
                                    List list1 = new ArrayList<>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trSpecialBushing);
                                    list1.add(list2);
                                    sympMessageRealDao.updateTextMessages(list1);
                                }
                            }
                        }
                    });
                    finalMyviewHolder1.remarks_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                String remark = finalMyviewHolder1.remarks_result.getText().toString();
                                if (!trSpecialBushingList.get(0).getRemarks().equals(remark)) {
                                    String date = Constants.dateformat2.format(new Date());
                                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                    columnsMap.put("REMARKS", remark);
                                    columnsMap.put("UPDATETIME", date);
                                    columnsMap.put("UPDATEPERSON", userid);
                                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                    wheresMap.put("ID", trSpecialBushingList.get(0).getId());
                                    TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                    trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                                    TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
                                    trSpecialBushing.setRemarks(remark);
                                    trSpecialBushing.setUpdatetime(date);
                                    trSpecialBushing.setUpdateperson(userid);
                                    trSpecialBushing.setId(trSpecialBushingList.get(0).getId());
                                    List list1 = new ArrayList<>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trSpecialBushing);
                                    list1.add(list2);
                                    sympMessageRealDao.updateTextMessages(list1);
                                }
                            }
                        }
                    });
                    finalMyviewHolder1.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder1.checkYes.setTextColor(green);
                                finalMyviewHolder1.checkNo.setChecked(false);
                                finalMyviewHolder1.checkNo.setTextColor(color);
                                String date = Constants.dateformat2.format(new Date());
                                Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                columnsMap.put("STATUS", "1");
                                columnsMap.put("UPDATETIME", date);
                                columnsMap.put("UPDATEPERSON", userid);
                                Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                wheresMap.put("ID", trSpecialBushingList.get(0).getId());
                                TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                                TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
                                trSpecialBushing.setStatus("1");
                                trSpecialBushing.setUpdatetime(date);
                                trSpecialBushing.setUpdateperson(userid);
                                trSpecialBushing.setId(trSpecialBushingList.get(0).getId());
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trSpecialBushing);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                                updateStatus(trDetectiontypEtempletObj,"1");
                            } else {
                                finalMyviewHolder1.checkYes.setTextColor(color);
                            }
                        }
                    });
                    finalMyviewHolder1.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                finalMyviewHolder1.checkNo.setTextColor(orange);
                                finalMyviewHolder1.checkYes.setChecked(false);
                                finalMyviewHolder1.checkYes.setTextColor(color);
                                String date = Constants.dateformat2.format(new Date());
                                Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                columnsMap.put("STATUS", "0");
                                columnsMap.put("UPDATETIME", date);
                                columnsMap.put("UPDATEPERSON", userid);
                                Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                wheresMap.put("ID", trSpecialBushingList.get(0).getId());
                                TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
                                trSpecialBushingDao.updateTrSpecialBushingInfo(columnsMap, wheresMap);
                                TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
                                trSpecialBushing.setStatus("0");
                                trSpecialBushing.setUpdatetime(date);
                                trSpecialBushing.setUpdateperson(userid);
                                trSpecialBushing.setId(trSpecialBushingList.get(0).getId());
                                List list1 = new ArrayList<>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trSpecialBushing);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);
                                updateStatus(trDetectiontypEtempletObj,"0");
                            } else {
                                finalMyviewHolder1.checkNo.setTextColor(color);
                            }
                        }
                    });
                }else if (trDetectiontypEtempletObj.getTemplettype().equals("oli")) {
                    List<TrSpecialOliData> oildatalist = getOildata(trDetectiontypEtempletObj);
                    myviewHolder.listView1.setAdapter(new OilListAdapter(context, oildatalist,trTask,trDetectiontypEtempletObj));
                }else if (trDetectiontypEtempletObj.getTemplettype().equals("ultrasonic")) {
                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                    myviewHolder.ultrasonic_hlistview.setVisibility(View.GONE);
                    myviewHolder.ultrasonic_view1.setVisibility(View.GONE);
                    myviewHolder.ultrasonic_view2.setVisibility(View.GONE);
                    myviewHolder.ultrasonic_view3.setVisibility(View.GONE);
                    if (trDetectionTempletObjDao.getTrDetectiontempletObj(trDetectiontypEtempletObj).size() != 0) {
                        final TrDetectiontempletObj trDetectiontempletObj = trDetectionTempletObjDao.getTrDetectiontempletObj(trDetectiontypEtempletObj).get(0);

                        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                            myviewHolder.ultrasonic_result.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? " " : trDetectiontempletObj.getResultdescribe());
                            myviewHolder.ultrasonic_remarks.setText(trDetectiontempletObj.getRemarks().equals("null") ? " " : trDetectiontempletObj.getRemarks());
                        }else{
                            myviewHolder.ultrasonic_result.setText(trDetectiontempletObj.getResultdescribe().equals("null") ? "" : trDetectiontempletObj.getResultdescribe());
                            myviewHolder.ultrasonic_remarks.setText(trDetectiontempletObj.getRemarks().equals("null") ? "" : trDetectiontempletObj.getRemarks());
                        }


                        String method = (trDetectiontempletObj.getDetectionmethod() != null && !("null").equals(trDetectiontempletObj.getDetectionmethod())) ? trDetectiontempletObj.getDetectionmethod() : "方法";
                        if (method.length() <= 4) {
                            myviewHolder.method.setText(method);
                        } else {
                            myviewHolder.method.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                createMethodeDialog(trDetectiontempletObj, null, 1);
                                }
                            });
                        }
                        myviewHolder.standard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createStandardDialog(trDetectiontempletObj, null, 1);
                            }
                        });

                        if (trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("0")) {
                            myviewHolder.checkNo.setChecked(true);
                            myviewHolder.checkNo.setTextColor(orange);
                            myviewHolder.checkYes.setChecked(false);
                            myviewHolder.checkYes.setTextColor(color);
                        }
                        if (trDetectiontempletObj.getStatus() != null && trDetectiontempletObj.getStatus().equals("1")) {
                            myviewHolder.checkYes.setChecked(true);
                            myviewHolder.checkYes.setTextColor(green);
                            myviewHolder.checkNo.setChecked(false);
                            myviewHolder.checkNo.setTextColor(color);
                        }
                        //检测结果失去焦点修改
                        final MyviewHolder finalMyviewHolder = myviewHolder;
                        finalMyviewHolder.ultrasonic_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    // 失去焦点时的处理内容
                                    String result = finalMyviewHolder.ultrasonic_result.getText().toString();
                                    if (!trDetectiontempletObj.getResultdescribe().equals(result)) {
                                        String date = Constants.dateformat2.format(new Date());
                                        Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("RESULTDESCRIBE", result);
                                        columnsMap.put("UPDATETIME", date);
                                        columnsMap.put("UPDATEPERSON", userid);
                                        Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID", trDetectiontempletObj.getId());
                                        TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                        trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
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
                        finalMyviewHolder.ultrasonic_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    // 失去焦点时的处理内容
                                    String remark = finalMyviewHolder.ultrasonic_remarks.getText().toString();
                                    if (!trDetectiontempletObj.getRemarks().equals(remark)) {
                                        String date = Constants.dateformat2.format(new Date());
                                        Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                        columnsMap.put("REMARKS", remark);
                                        columnsMap.put("UPDATETIME", date);
                                        columnsMap.put("UPDATEPERSON", userid);
                                        Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                        wheresMap.put("ID", trDetectiontempletObj.getId());
                                        TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                        trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
                                        TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                                        trDetectiontempletObj2.setRemarks(remark);
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
                        //修改状态
                        finalMyviewHolder.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked == true) {
                                    finalMyviewHolder.checkYes.setTextColor(green);
                                    finalMyviewHolder.checkNo.setChecked(false);
                                    finalMyviewHolder.checkNo.setTextColor(color);
                                    String date = Constants.dateformat2.format(new Date());
                                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                                    columnsMap.put("STATUS", "1");
                                    columnsMap.put("UPDATETIME", date);
                                    columnsMap.put("UPDATEPERSON", userid);
                                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                                    wheresMap.put("ID", trDetectiontempletObj.getId());
                                    TrDetectionTempletObjDao trDetectionTempletObjDao = new TrDetectionTempletObjDao(context);
                                    trDetectionTempletObjDao.updateTrDetectionTempletObjInfo(columnsMap, wheresMap);
                                    TrDetectiontempletObj trDetectiontempletObj2 = new TrDetectiontempletObj();
                                    trDetectiontempletObj2.setStatus("1");
                                    trDetectiontempletObj2.setUpdatetime(date);
                                    trDetectiontempletObj2.setUpdateperson(userid);
                                    trDetectiontempletObj2.setId(trDetectiontempletObj.getId());
                                    List list1 = new ArrayList<>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trDetectiontempletObj2);
                                    list1.add(list2);
                                    sympMessageRealDao.updateTextMessages(list1);
                                    updateStatus(trDetectiontypEtempletObj,"1");
                                } else {
                                    finalMyviewHolder.checkYes.setTextColor(color);
                                }
                            }
                        });
                        finalMyviewHolder.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked == true) {
                                    finalMyviewHolder.checkNo.setTextColor(orange);
                                    finalMyviewHolder.checkYes.setChecked(false);
                                    finalMyviewHolder.checkYes.setTextColor(color);
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
                                    List list1 = new ArrayList<>();
                                    List<Object> list2 = new ArrayList<Object>();
                                    list2.add(trDetectiontempletObj2);
                                    list1.add(list2);
                                    sympMessageRealDao.updateTextMessages(list1);
                                    updateStatus(trDetectiontypEtempletObj,"0");
                                } else {
                                    finalMyviewHolder.checkNo.setTextColor(color);
                                }
                            }
                        });
                    } else {
                        final String uuid = Constants.getUuid();
                        final String date = Constants.dateformat2.format(new Date());
                        final MyviewHolder finalMyviewHolder = myviewHolder;
                        finalMyviewHolder.ultrasonic_result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    String result = finalMyviewHolder.ultrasonic_result.getText().toString();
                                    String remark = finalMyviewHolder.ultrasonic_remarks.getText().toString();
                                    String state = "";
                                    if (finalMyviewHolder.checkNo.isChecked() == true) {
                                        state = "0";
                                    } else if (finalMyviewHolder.checkYes.isChecked() == true) {
                                        state = "1";
                                    }
                                    if (!result.equals("")) {
                                        insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, state, result);
                                    }
                                }
                            }
                        });
                        finalMyviewHolder.ultrasonic_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    String result = finalMyviewHolder.ultrasonic_result.getText().toString();
                                    String remark = finalMyviewHolder.ultrasonic_remarks.getText().toString();
                                    String state = "";
                                    if (finalMyviewHolder.checkNo.isChecked() == true) {
                                        state = "0";
                                    } else if (finalMyviewHolder.checkYes.isChecked() == true) {
                                        state = "1";
                                    }
                                    if (!remark.equals("")) {
                                        insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, state, result);
                                    }
                                }
                            }
                        });
                        finalMyviewHolder.checkYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked == true) {
                                    finalMyviewHolder.checkYes.setTextColor(green);
                                    finalMyviewHolder.checkNo.setChecked(false);
                                    finalMyviewHolder.checkNo.setTextColor(color);
                                    String result = finalMyviewHolder.ultrasonic_result.getText().toString();
                                    String remark = finalMyviewHolder.ultrasonic_remarks.getText().toString();
                                    insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, "1", result);
                                    updateStatus(trDetectiontypEtempletObj,"1");
                                } else {
                                    finalMyviewHolder.checkYes.setTextColor(color);
                                }
                            }
                        });
                        finalMyviewHolder.checkNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked == true) {
                                    finalMyviewHolder.checkNo.setTextColor(orange);
                                    finalMyviewHolder.checkYes.setChecked(false);
                                    finalMyviewHolder.checkYes.setTextColor(color);
                                    String result = finalMyviewHolder.ultrasonic_result.getText().toString();
                                    String remark = finalMyviewHolder.ultrasonic_remarks.getText().toString();
                                    insertTrDetectiontempletObj(trDetectiontypEtempletObj, uuid, date, remark, "0", result);
                                    updateStatus(trDetectiontypEtempletObj,"0");
                                } else {
                                    finalMyviewHolder.checkNo.setTextColor(color);
                                }
                            }
                        });
                    }
                    myviewHolder.ultrasonic_content.setText(trDetectiontypEtempletObj.getDetectionname());
                }
                return convertView;
            }
        }

    /**
     * 设置红外套管图号的监听事件
     */
    private void setOnfocuslistenerBushing(final EditText editText, final TrSpecialBushingFile file, final String positionId,final ImageView image_show){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = editText.getText().toString();
                String date = Constants.dateformat2.format(new Date());
                if(!hasFocus){
                    Utils.number = text;//当前输入框的值赋给number
                    if(!file.getFilenumber().equals(text)){
                        Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
                        columnsMapImage.put("FILENUMBER", text);
                        columnsMapImage.put("UPDATETIME", date);
                        columnsMapImage.put("UPDATEPERSON", userid);
//                        columnsMapImage.put("FILENAME", text+".jpg");
                        columnsMapImage.put("POSITIONID", positionId);
                        columnsMapImage.put("PROJECTID",trTask.getProjectid());
                        columnsMapImage.put("TASKID", trTask.getTaskid());
                        if(file.getFilename()!=null&&!file.getFilename().equals("null")){
                            if(file.getFilename()!=null&&!file.getFilename().equals("null")){
                                columnsMapImage.put("FILENAME","");
                                columnsMapImage.put("FILEURL","");

                                image_show.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                image_show.setBackgroundResource(0);
                            }
                        }
                        Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
                        wheresMapImage.put("ID", file.getId());
                        trSpecialBushingFileDao.updateTrSpecialBushingFileInfo(columnsMapImage,wheresMapImage);
                        //发送消息
                        TrSpecialBushingFile trSpecialButshingFile = new TrSpecialBushingFile();
                        trSpecialButshingFile.setFilenumber(text);
                        trSpecialButshingFile.setUpdatetime(date);
                        trSpecialButshingFile.setUpdateperson(userid);
//                        trSpecialButshingFile.setFilename(text + ".jpg");
                        trSpecialButshingFile.setProjectid(trTask.getProjectid());
                        trSpecialButshingFile.setTaskid(trTask.getTaskid());
                        trSpecialButshingFile.setId(file.getId());
                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trSpecialButshingFile);
                        list1.add(list2);
                        sympMessageRealDao.updateTextMessages(list1);

                        /*if(!TextUtils.isEmpty(text)){
                            if(!text.equals(lastnumber)){
                                Constants.imagelist.add(text+"|"+file.getId()+"|"+"bushing");
                                lastnumber=text;
                            }
                        }*/
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

    /**
     * 设置红外普通图号的监听事件
     */
    private void setOnFocusListenerGeneral(final EditText editText, final TrDetectiontempletFileObj file, final TrDetectiontypEtempletObj obj,final int position,final List<List<TrDetectiontempletFileObj>> filelist,final


    ImageView image_show){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText1 = (EditText) v;
                String text = editText1.getText().toString();
                String date = Constants.dateformat2.format(new Date());
                if(!hasFocus){
                    if(!text.equals("")){
                        if(file.getId() != null && !file.getId().equals("null")) {
                            if (!file.getFilenumber().equals(text)) {
                                Map<Object, Object> columnsMapImage = new HashMap<Object, Object>();
                                columnsMapImage.put("FILENUMBER", text);
                                columnsMapImage.put("UPDATETIME", date);
                                columnsMapImage.put("UPDATEPERSON", userid);
                                columnsMapImage.put("TEMPLETNAME", "普通模板");
                                columnsMapImage.put("TEMPLETTYPE", "general");
                                columnsMapImage.put("TASKID",trTask.getTaskid());
                                columnsMapImage.put("PROJECTID",trTask.getProjectid());
                                columnsMapImage.put("DETECTIONOBJID",file.getDetectionobjid());
                                if(file.getFilename()!=null&&!file.getFilename().equals("null")){
                                    columnsMapImage.put("FILENAME","");
                                    columnsMapImage.put("FILEURL","");

                                    image_show.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
                                    image_show.setBackgroundResource(0);
                                }
//                              columnsMapImage.put("FILENAME", text+".jpg");
                                Map<Object, Object> wheresMapImage = new HashMap<Object, Object>();
                                wheresMapImage.put("ID", file.getId());
                                trDetectionTempletFileObjDao.updateTrDetectionTempletFileObjInfo(columnsMapImage, wheresMapImage);
                                //发送消息
                                TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
                                trDetectiontempletFileObj.setFilenumber(text);
                                trDetectiontempletFileObj.setDetectionobjid(file.getDetectionobjid());
                                trDetectiontempletFileObj.setUpdatetime(date);
                                trDetectiontempletFileObj.setUpdateperson(userid);
                                trDetectiontempletFileObj.setTempletname("普通模板");
                                trDetectiontempletFileObj.setTemplettype("general");
//                              trDetectiontempletFileObj.setFilename(text + ".jpg");
                                trDetectiontempletFileObj.setId(file.getId());
                                trDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                                trDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                                List<List<Object>> list1 = new ArrayList<List<Object>>();
                                List<Object> list2 = new ArrayList<Object>();
                                list2.add(trDetectiontempletFileObj);
                                list1.add(list2);
                                sympMessageRealDao.updateTextMessages(list1);

                                for(int i=0;i<filelist.get(position).size();i++){
                                    TrDetectiontempletFileObj fileObj = filelist.get(position).get(i);
                                    String id=fileObj.getId();
                                    if(id.equals(file.getId())){
                                        fileObj.setFilenumber(text);
                                    }
                                }
                                Utils.number = text;//当前输入框的值赋给number
                            }
                        }
                    }else if(!text.equals("") && !text.equals(text2)){//插入数据
//                        editText1.setEnabled(false);
//                        editText1.setFocusable(false);
//                        editText1.setFocusableInTouchMode(false);
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
//                        fileobj.setFilename(text + ".jpg");
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

                        /*if(text!=null&&!text.equals("")){
                            if(!text.equals(lastnumber)){
                                Constants.imagelist.add(text+"|"+id+"|"+"general");
                                lastnumber=text;
                            }
                        }*/

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

    public void ultImageView(MyviewHolder viewHolder){
        String [] arr= new String[]{"可见光","A相","B相","C相"};
        String [] arr1 =new String[]{"HP","MP","LP","SP"}; //可见光
        String [] arrA =new String[]{"HA", "MA", "LA", "SA"}; //A
        String [] arrB =new String[]{"HB", "MB", "LB", "SB"}; //B
        String [] arrC =new String[]{"HC", "MC", "LC", "SC"};
        for(int i = 0; i <arr.length; i++){
            String[] ar=null;
            if(i==0){
                ar= arr1;
            }else if(i==1){
                ar= arrA;
            }else if(i==2){
                ar= arrB;
            }else if(i==3){
                ar= arrC;
            }
            List ListH = new ArrayList();
            List ListA = new ArrayList();
            List ListB = new ArrayList();
            List ListC = new ArrayList();
            List<TrDetectiontempletFileObj> filelist = getUltFileItem(trDetectiontypEtempletObj3,ar[0]);
            if(filelist!=null && filelist.size()!=0){
                for (int j = 0; j < filelist.size(); j++) {
                    String name = filelist.get(j).getFilename();
                    ListH.add(name);
                }
            }
            List<TrDetectiontempletFileObj> filelistA = getUltFileItem(trDetectiontypEtempletObj3,ar[1]);
            if(filelistA!=null && filelistA.size()!=0){
                for (int j = 0; j < filelistA.size(); j++) {
                    String name = filelistA.get(j).getFilename();
                    ListA.add(name);
                }
            }
            List<TrDetectiontempletFileObj> filelistB = getUltFileItem(trDetectiontypEtempletObj3,ar[2]);
            if(filelistB!=null && filelistB.size()!=0){
                for (int j = 0; j < filelistB.size(); j++) {
                    String name = filelistB.get(j).getFilename();
                    ListB.add(name);
                }
            }
            List<TrDetectiontempletFileObj> filelistC = getUltFileItem(trDetectiontypEtempletObj3,ar[3]);
            if(filelistC!=null && filelistC.size()!=0){
                for (int j = 0; j < filelistC.size(); j++) {
                    String name = filelistC.get(j).getFilename();
                    ListC.add(name);
                }
            }
            if(i == 0){
                if(ListH.size() > 0){
                    context.imageUltGet(ListH.get(0).toString(),viewHolder.ult_hv_pic,trDetectiontypEtempletObj3,ar[0]);
                }
                if(ListA.size() > 0){
                    context.imageUltGet(ListA.get(0).toString(),viewHolder.ult_mv_pic,trDetectiontypEtempletObj3,ar[1]);
                }
                if(ListB.size() > 0){
                    context.imageUltGet(ListB.get(0).toString(),viewHolder.ult_lv_pic,trDetectiontypEtempletObj3,ar[2]);
                }
                if(ListC.size() > 0){
                    context.imageUltGet(ListC.get(0).toString(),viewHolder.ult_sv_pic,trDetectiontypEtempletObj3,ar[3]);
                }
            }else if(i == 1){
                List<ImageView> imageViewList = new ArrayList<ImageView>();
                imageViewList.add(viewHolder.ult_ha_pic1);
                imageViewList.add(viewHolder.ult_ha_pic2);
                imageViewList.add(viewHolder.ult_ha_pic3);
                imageViewList.add(viewHolder.ult_ha_pic4);
                imageViewList.add(viewHolder.ult_ma_pic1);
                imageViewList.add(viewHolder.ult_ma_pic2);
                imageViewList.add(viewHolder.ult_ma_pic3);
                imageViewList.add(viewHolder.ult_ma_pic4);
                imageViewList.add(viewHolder.ult_la_pic1);
                imageViewList.add(viewHolder.ult_la_pic2);
                imageViewList.add(viewHolder.ult_la_pic3);
                imageViewList.add(viewHolder.ult_la_pic4);
                imageViewList.add(viewHolder.ult_sa_pic1);
                imageViewList.add(viewHolder.ult_sa_pic2);
                imageViewList.add(viewHolder.ult_sa_pic3);
                imageViewList.add(viewHolder.ult_sa_pic4);
                ultImage(imageViewList,ListH,ListA,ListB,ListC,arrA);
            }else if(i == 2){
                List<ImageView> imageViewList = new ArrayList<ImageView>();
                imageViewList.add(viewHolder.ult_hb_pic1);
                imageViewList.add(viewHolder.ult_hb_pic2);
                imageViewList.add(viewHolder.ult_hb_pic3);
                imageViewList.add(viewHolder.ult_hb_pic4);
                imageViewList.add(viewHolder.ult_mb_pic1);
                imageViewList.add(viewHolder.ult_mb_pic2);
                imageViewList.add(viewHolder.ult_mb_pic3);
                imageViewList.add(viewHolder.ult_mb_pic4);
                imageViewList.add(viewHolder.ult_lb_pic1);
                imageViewList.add(viewHolder.ult_lb_pic2);
                imageViewList.add(viewHolder.ult_lb_pic3);
                imageViewList.add(viewHolder.ult_lb_pic4);
                imageViewList.add(viewHolder.ult_sb_pic1);
                imageViewList.add(viewHolder.ult_sb_pic2);
                imageViewList.add(viewHolder.ult_sb_pic3);
                imageViewList.add(viewHolder.ult_sb_pic4);
                ultImage(imageViewList,ListH,ListA,ListB,ListC,arrB);
            }else if(i == 3){
                List<ImageView> imageViewList = new ArrayList<ImageView>();
                imageViewList.add(viewHolder.ult_hc_pic1);
                imageViewList.add(viewHolder.ult_hc_pic2);
                imageViewList.add(viewHolder.ult_hc_pic3);
                imageViewList.add(viewHolder.ult_hc_pic4);
                imageViewList.add(viewHolder.ult_mc_pic1);
                imageViewList.add(viewHolder.ult_mc_pic2);
                imageViewList.add(viewHolder.ult_mc_pic3);
                imageViewList.add(viewHolder.ult_mc_pic4);
                imageViewList.add(viewHolder.ult_lc_pic1);
                imageViewList.add(viewHolder.ult_lc_pic2);
                imageViewList.add(viewHolder.ult_lc_pic3);
                imageViewList.add(viewHolder.ult_lc_pic4);
                imageViewList.add(viewHolder.ult_sc_pic1);
                imageViewList.add(viewHolder.ult_sc_pic2);
                imageViewList.add(viewHolder.ult_sc_pic3);
                imageViewList.add(viewHolder.ult_sc_pic4);
                ultImage(imageViewList,ListH,ListA,ListB,ListC,arrC);
            }
        }
    }

    public void ultImage(List<ImageView> imageViewList,List ListH,List ListA,List ListB,List ListC,String[] str){
        int j = 0;
        for(int i = 0 ; i < imageViewList.size() ; i++){
            if(i < 4){
                if(ListH.size() > 0  && j < ListH.size()){
                    context.imageUltGet(ListH.get(j).toString(),imageViewList.get(i),trDetectiontypEtempletObj3,str[0]);
                }
            }else if(i > 3 && i < 8){
                if(ListA.size() > 0 && j < ListA.size()){
                    context.imageUltGet(ListA.get(j).toString(),imageViewList.get(i),trDetectiontypEtempletObj3,str[1]);
                }
            }else if(i > 7 && i < 12){
                if(ListB.size() > 0 && j < ListB.size()){
                    context.imageUltGet(ListB.get(j).toString(),imageViewList.get(i),trDetectiontypEtempletObj3,str[2]);
                }
            }else if(i > 11){
                if(ListC.size() > 0 && j < ListC.size()){
                    context.imageUltGet(ListC.get(j).toString(),imageViewList.get(i),trDetectiontypEtempletObj3,str[3]);
                }
            }
            if(j < 3){
                j++;
            }else{
                j = 0;
            }
        }
    }

    public void updateTaskState(){
        if(trTask.getTaskname().equals("站内巡检")){
            if(trTask.getTaskstate().equals("1")){
                trTask.setTaskstate("2");
                Map<Object,Object> columnsMap = new HashMap<Object,Object>();
                columnsMap.put("TASKSTATE","2");
                Map<Object,Object> wheresMap = new HashMap<Object,Object>();
                wheresMap.put("TASKID",trTask.getTaskid());
                TrTaskDao trTaskDao = new TrTaskDao(context);
                trTaskDao.updateTrTaskInfo(columnsMap,wheresMap);
                TrTask trTaskUpdate = new TrTask();
                trTaskUpdate.setTaskstate("2");
                trTaskUpdate.setFinishtime(Constants.dateformat2.format(new Date()));
                trTaskUpdate.setTaskid(trTask.getTaskid());
                List<List<Object>> list1 = new ArrayList<List<Object>>();
                List<Object> list2 = new ArrayList<Object>();
                list2.add(trTaskUpdate);
                list1.add(list2);
                sympMessageRealDao.updateTextMessages(list1);
            }
        }
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
    //修改套管数据方法
    private void updateBushing(final String result,final String date,final String id){
        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
        columnsMap.put("RESULT",result);
        columnsMap.put("UPDATETIME", date);
        columnsMap.put("UPDATEPERSON",userid);
        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
        wheresMap.put("ID", id);
        TrSpecialBushingDataDao trSpecialBushingDataDao = new TrSpecialBushingDataDao(context);
        trSpecialBushingDataDao.updateTrSpecialBushingDataInfo(columnsMap,wheresMap);
        TrSpecialBushingData trSpecialBushingData1 = new TrSpecialBushingData();
        trSpecialBushingData1.setResult(result);
        trSpecialBushingData1.setUpdateperson(userid);
        trSpecialBushingData1.setUpdatetime(date);
        trSpecialBushingData1.setId(id);
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(trSpecialBushingData1);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }
    private void updateBushingObj(EditText resultEdit){
        String result = resultEdit.getText().toString();
        String id = resultEdit.getTag().toString();
        String date = Constants.dateformat2.format(new Date());
        for (List<TrSpecialBushingData> trSpecialBushingDataList : trSpecialBushingDatasList){
            for(TrSpecialBushingData trSpecialBushingData : trSpecialBushingDataList){
                if(trSpecialBushingData.getId().equals(id)){
                    if(!trSpecialBushingData.getResult().equals(result)){
                        updateBushing(result,date,id);
                    }
                }
            }
        }
    }

    /**
     * 红外普通图片
     * @param size
     * @param view
     * @param filelist
     * @param trDetectiontypEtempletObj
     */
    public void getViewforInfrared(final int size,final MyviewHolder view,final List<List<TrDetectiontempletFileObj>> filelist,final TrDetectiontypEtempletObj trDetectiontypEtempletObj){ //普通模板
        int  imagedefaultcount = Integer.parseInt(trDetectiontypEtempletObj.getPiccount());
        if(size==0){
            View v=LayoutInflater.from(context).inflate(R.layout.infimage_gridview_item,null);
            RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
            LinearLayout infimage_add_layout= (LinearLayout) v.findViewById(R.id.infimage_add_layout);
            TextView infimage_count= (TextView) v.findViewById(R.id.infimage_count);

            image_rl.setVisibility(View.INVISIBLE);
            infimage_count.setText("至少"+imagedefaultcount+"组图片");
            infimage_add_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.gener_ll.removeAllViews();
                    String infNumber = "",visNumber = "";
                    if(!Utils.number.equals("")){
                        infNumber = Constants.formatNum();
                        visNumber = Constants.formatNum();
                    }
                    String infuuid = Constants.getUuid();
                    String visibleuuid = Constants.getUuid();
                    String signid = Constants.getUuid();
                    List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                    TrDetectiontempletFileObj inftrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                    inftrDetectiontempletFileObj.setId(infuuid);
                    inftrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                    inftrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                    inftrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                    inftrDetectiontempletFileObj.setIsupload("1");
                    inftrDetectiontempletFileObj.setFilenumber(infNumber);
                    inftrDetectiontempletFileObj.setSignid(signid);
                    inftrDetectiontempletFileObj.setOrderby("1");
                    inftrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                    TrDetectiontempletFileObj visibletrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                    visibletrDetectiontempletFileObj.setId(visibleuuid);
                    visibletrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                    visibletrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                    visibletrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                    visibletrDetectiontempletFileObj.setIsupload("1");
                    visibletrDetectiontempletFileObj.setFilenumber(visNumber);
                    visibletrDetectiontempletFileObj.setSignid(signid);
                    visibletrDetectiontempletFileObj.setOrderby("2");
                    visibletrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                    insertfile.add(inftrDetectiontempletFileObj);
                    insertfile.add(visibletrDetectiontempletFileObj);
                    trDetectionTempletFileObjDao.insertListData(insertfile);

                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(inftrDetectiontempletFileObj);
                    list2.add(visibletrDetectiontempletFileObj);
                    list1.add(list2);
                    sympMessageRealDao.addTextMessages(list1);

                    filelist.add(insertfile);
                    getViewforInfrared(filelist.size(), view, filelist, trDetectiontypEtempletObj);

                    if(Utils.number.equals("")){
                        Constants.showSoftKeyboard(firstEditText);
                    }
                }
            });

            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                infimage_add_layout.setEnabled(false);
                view.gener_ll.setVisibility(View.GONE);
            }
            view.gener_ll.addView(v);
        }else{
            for(int i=0;i<size;i++){
                View v=LayoutInflater.from(context).inflate(R.layout.infimage_gridview_item,null);

                LinearLayout infimage_add_layout= (LinearLayout) v.findViewById(R.id.infimage_add_layout);
                infimage_add_layout.setVisibility(View.GONE);

                final EditText infimage_number= (EditText) v.findViewById(R.id.infimage_number);
                final EditText visibleimage_number= (EditText) v.findViewById(R.id.visibleimage_number);
                ImageView image_delete= (ImageView) v.findViewById(R.id.deletepictures);
                final ImageView infimage_show= (ImageView) v.findViewById(R.id.infimage_show);
                final ImageView visibleimage_show= (ImageView) v.findViewById(R.id.visibleimage_show);
                final ProgressBar infimage_bar= (ProgressBar) v.findViewById(R.id.infimage_bar);
                final ProgressBar visibleimage_bar= (ProgressBar) v.findViewById(R.id.visibleimage_bar);
                final TextView hind_inf = (TextView) v.findViewById(R.id.hind_inf);
                final TextView hind_vis = (TextView) v.findViewById(R.id.hind_vis);
                final TextView hind_inf_pic = (TextView) v.findViewById(R.id.hind_inf_pic);
                final TextView hind_vis_pic = (TextView) v.findViewById(R.id.hind_vis_pic);

                final int pos=i;
                image_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                            mMaterialDialog.setTitle("确认删除该照片吗").setMessage("").
                                    setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteInfPhoto(hind_inf.getText().toString(), hind_vis.getText().toString());
                                           /* List<List<TrDetectiontempletFileObj>> fileObjGroup = new ArrayList<List<TrDetectiontempletFileObj>>();//存放采集卡下多组图片
                                            List<String> idList = getFileItemInfId(trDetectiontypEtempletObj);
                                            for (int i = 0; i < idList.size(); i++) {
                                                TrDetectiontempletFileObj fileObj = new TrDetectiontempletFileObj();
                                                fileObj.setSignid(idList.get(i));
                                                List<TrDetectiontempletFileObj> fileObjList = getFileItemInf(fileObj);
                                                fileObjGroup.add(fileObjList);
                                            }
                                            view.gener_ll.removeAllViews();
                                            getViewforInfrared(fileObjGroup.size(), view, fileObjGroup, trDetectiontypEtempletObj);*/
                                            filelist.remove(pos);
                                            view.gener_ll.removeAllViews();
                                            getViewforInfrared(filelist.size(), view, filelist, trDetectiontypEtempletObj);
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

                if(filelist.size() > 0) {
                    final List<TrDetectiontempletFileObj> objList = filelist.get(i);

                    for (int j = 0; j < objList.size(); j++) { //隐藏域设置图片主键
                        String fileName = objList.get(j).getFilename();
                        String fileNumber = Constants.getFileNumber(fileName);
                        if (j == 0) {
                            hind_inf.setText(objList.get(j).getId());
                            hind_inf_pic.setText(objList.get(j).getFilename());

                            if (!trTask.getTaskstate().equals("1") & !trTask.getTaskstate().equals("2")) {
                                infimage_number.setText(objList.get(j).getFilenumber().equals("null") ? fileNumber : objList.get(j).getFilenumber());
                            } else {
                                infimage_number.setText(objList.get(j).getFilenumber().equals("null") ? fileNumber : objList.get(j).getFilenumber());
                            }

                            infimage_number.setTag(R.id.infimage_number, objList.get(j).getFilenumber().equals("null") ? "" : objList.get(j).getFilenumber());
                            context.imageGet(objList.get(j), infimage_show, infimage_bar);
                            setOnFocusListenerGeneral(infimage_number, objList.get(j), trDetectiontypEtempletObj,i,filelist,infimage_show);
                        } else if (j == 1) {
                            hind_vis.setText(objList.get(j).getId());
                            hind_vis_pic.setText(objList.get(j).getFilename());

                            if (!trTask.getTaskstate().equals("1") & !trTask.getTaskstate().equals("2")) {
                                visibleimage_number.setText(objList.get(j).getFilenumber().equals("null") ? fileNumber : objList.get(j).getFilenumber());
                            } else {
                                visibleimage_number.setText(objList.get(j).getFilenumber().equals("null") ? fileNumber : objList.get(j).getFilenumber());
                            }

                            visibleimage_number.setTag(R.id.visibleimage_number, objList.get(j).getFilenumber().equals("null") ? "" : objList.get(j).getFilenumber());
                            context.imageGet(objList.get(j), visibleimage_show, visibleimage_bar);
                            setOnFocusListenerGeneral(visibleimage_number, objList.get(j), trDetectiontypEtempletObj,i,filelist,visibleimage_show);
                        }
                        //点击图片放大
                        final int finalI = i;
                        final int finalJ = j;
                        infimage_show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fileNumber = infimage_number.getText().toString();
                                if(!fileNumber.equals("")){
                                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                                        String url = URLs.HTTP + URLs.HOSTA + URLs.IMAGEPATH + trTask.getProjectid() + "/" + trTask.getTaskid() + "/original/";
                                        Intent intent = new Intent(context, ImagePagerActivity.class);
                                        intent.putExtra("index", 0);
                                        intent.putExtra("url", url);
                                        intent.putExtra("localurl", pic_route + "original");
                                        intent.putExtra("type", "infgeneral");
                                        intent.putExtra("id", objList.get(finalI).getSignid());
                                        context.startActivity(intent);
                                    }else{
                                        Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        visibleimage_show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fileNumber = visibleimage_number.getText().toString();
                                if(!fileNumber.equals("")){
                                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                                        String url = URLs.HTTP + URLs.HOSTA + URLs.IMAGEPATH + trTask.getProjectid() + "/" + trTask.getTaskid() + "/original/";
                                        Intent intent = new Intent(context, ImagePagerActivity.class);
                                        intent.putExtra("index", 1);
                                        intent.putExtra("url", url);
                                        intent.putExtra("localurl", pic_route + "original");
                                        intent.putExtra("type", "infgeneral");
                                        intent.putExtra("id", objList.get(finalJ).getSignid());
                                        context.startActivity(intent);
                                    }else {
                                        Toast.makeText(context, "暂无照片", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                if(i==size-1){
                    firstEditText= (EditText) v.findViewById(R.id.infimage_number);
                }

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    infimage_number.setEnabled(false);
                    visibleimage_number.setEnabled(false);
                    image_delete.setEnabled(false);

                    image_delete.setVisibility(View.GONE);
                }

                view.gener_ll.addView(v);
            }
            if(size<imagedefaultcount){
                View v=LayoutInflater.from(context).inflate(R.layout.infimage_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                LinearLayout infimage_add_layout= (LinearLayout) v.findViewById(R.id.infimage_add_layout);
                TextView infimage_count= (TextView) v.findViewById(R.id.infimage_count);

                image_rl.setVisibility(View.INVISIBLE);
                infimage_count.setText("至少"+imagedefaultcount+"组图片");
                infimage_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String infNumber = "",visNumber = "";
                        if(!Utils.number.equals("")){
                            infNumber = Constants.formatNum();
                            visNumber = Constants.formatNum();
                        }
                        String infuuid = Constants.getUuid();
                        String visibleuuid = Constants.getUuid();
                        String signid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj inftrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        inftrDetectiontempletFileObj.setId(infuuid);
                        inftrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        inftrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        inftrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        inftrDetectiontempletFileObj.setIsupload("1");
                        inftrDetectiontempletFileObj.setFilenumber(infNumber);
                        inftrDetectiontempletFileObj.setSignid(signid);
                        inftrDetectiontempletFileObj.setOrderby("1");
                        inftrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                        TrDetectiontempletFileObj visibletrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        visibletrDetectiontempletFileObj.setId(visibleuuid);
                        visibletrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        visibletrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        visibletrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        visibletrDetectiontempletFileObj.setIsupload("1");
                        visibletrDetectiontempletFileObj.setFilenumber(visNumber);
                        visibletrDetectiontempletFileObj.setSignid(signid);
                        visibletrDetectiontempletFileObj.setOrderby("2");
                        visibletrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                        insertfile.add(inftrDetectiontempletFileObj);
                        insertfile.add(visibletrDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(inftrDetectiontempletFileObj);
                        list2.add(visibletrDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        filelist.add(insertfile);
                        getViewforInfrared(filelist.size(), view, filelist, trDetectiontypEtempletObj);

                        if(Utils.number.equals("")){
                            Constants.showSoftKeyboard(firstEditText);
                        }
                    }
                });
                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    infimage_add_layout.setEnabled(false);
                    infimage_add_layout.setVisibility(View.GONE);
                }
                view.gener_ll.addView(v);
            }else{
                View v=LayoutInflater.from(context).inflate(R.layout.infimage_gridview_item,null);
                RelativeLayout image_rl= (RelativeLayout) v.findViewById(R.id.image_rl);
                LinearLayout infimage_add_layout= (LinearLayout) v.findViewById(R.id.infimage_add_layout);
                TextView infimage_count= (TextView) v.findViewById(R.id.infimage_count);

                image_rl.setVisibility(View.INVISIBLE);
                infimage_count.setVisibility(View.GONE);
                infimage_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.gener_ll.removeAllViews();
                        String infNumber = "",visNumber = "";
                        if(!Utils.number.equals("")){
                            infNumber = Constants.formatNum();
                            visNumber = Constants.formatNum();
                        }
                        String infuuid = Constants.getUuid();
                        String visibleuuid = Constants.getUuid();
                        String signid = Constants.getUuid();
                        List<TrDetectiontempletFileObj> insertfile = new ArrayList<TrDetectiontempletFileObj>();
                        TrDetectiontempletFileObj inftrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        inftrDetectiontempletFileObj.setId(infuuid);
                        inftrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        inftrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        inftrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        inftrDetectiontempletFileObj.setIsupload("1");
                        inftrDetectiontempletFileObj.setFilenumber(infNumber);
                        inftrDetectiontempletFileObj.setSignid(signid);
                        inftrDetectiontempletFileObj.setOrderby("1");
                        inftrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                        TrDetectiontempletFileObj visibletrDetectiontempletFileObj = new TrDetectiontempletFileObj();
                        visibletrDetectiontempletFileObj.setId(visibleuuid);
                        visibletrDetectiontempletFileObj.setTaskid(trTask.getTaskid());
                        visibletrDetectiontempletFileObj.setProjectid(trTask.getProjectid());
                        visibletrDetectiontempletFileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
                        visibletrDetectiontempletFileObj.setIsupload("1");
                        visibletrDetectiontempletFileObj.setFilenumber(visNumber);
                        visibletrDetectiontempletFileObj.setSignid(signid);
                        visibletrDetectiontempletFileObj.setOrderby("2");
                        visibletrDetectiontempletFileObj.setUpdatetime(Constants.dateformat2.format(new Date()));
                        insertfile.add(inftrDetectiontempletFileObj);
                        insertfile.add(visibletrDetectiontempletFileObj);
                        trDetectionTempletFileObjDao.insertListData(insertfile);

                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(inftrDetectiontempletFileObj);
                        list2.add(visibletrDetectiontempletFileObj);
                        list1.add(list2);
                        sympMessageRealDao.addTextMessages(list1);

                        filelist.add(insertfile);
                        getViewforInfrared(filelist.size(), view, filelist, trDetectiontypEtempletObj);

                        if(Utils.number.equals("")){
                            Constants.showSoftKeyboard(firstEditText);
                        }
                    }
                });
                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    infimage_add_layout.setEnabled(false);
                    infimage_add_layout.setVisibility(View.GONE);
                }
                view.gener_ll.addView(v);
            }
            handler1.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaper");
                    }
                    int scrollViewwight=view.general_hlistview.getWidth();
                    int linearLayoutwight=view.gener_ll.getWidth();
                    if(linearLayoutwight>scrollViewwight){
                        view.general_hlistview.scrollTo(linearLayoutwight-scrollViewwight, 0);
                    }
                }
            });
        }
    }

    //作业情况记录状态改变
    private void workStatusUpdate(TrDetectiontypEtempletObj trDetectiontypEtempletObj,ImageView upimage,TrDetectiontypEtempletObj trDetectiontypEtempletObj3){
        boolean isshow = false;
        List<TrSpecialWork> workdatalist = getWorkrecord(trDetectiontypEtempletObj);
        for(TrSpecialWork trSpecialWork : workdatalist){
            Map<String,Boolean> personsmap=getDetectiondeperson(trSpecialWork);
            for(Map.Entry<String,Boolean> entry: personsmap.entrySet()){
                if(entry.getValue() == true){
                    isshow = true;
                }
            }
        }
        if(isshow){
            upimage.setBackgroundResource(R.drawable.green_round);
            updateStatus(trDetectiontypEtempletObj,"");
        }else{
            upimage.setBackgroundResource(R.drawable.gray_round);
            updateStatus(trDetectiontypEtempletObj,"");
        }
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
                    //设置图号监听
//                    setOnFocusListenerGeneral(image_number,filelist.get(i),trDetectiontypEtempletObj);
                    if(filelist.get(i).getFilename() != null && !filelist.get(i).getFilename().equals("")){
                        String fileName = filelist.get(i).getFilename();
                        String fileNumber = Constants.getFileNumber(fileName);
                        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }else{
                            image_number.setText(filelist.get(i).getFilenumber().equals("null") ? fileNumber : filelist.get(i).getFilenumber());
                        }
                        if(fileName.equals("null")&&Constants.state==1){
                            image_number.setGravity(Gravity.NO_GRAVITY);
                            image_number.setEnabled(true);
                        }
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
        handler1.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaper");
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

    private void deleteInfBusPhoto(ImageView imageView,EditText infnumber,EditText visnumber,ImageView infimage,ImageView visimage){
        String date = Constants.dateformat2.format(new Date());
        TrSpecialBushingFileDao trSpecialBushingFileDao = new TrSpecialBushingFileDao(context);
        Map<Object, Object> infcolumnsMapImage = new HashMap<Object, Object>();
        infcolumnsMapImage.put("FILENUMBER", "");
        infcolumnsMapImage.put("UPDATETIME", date);
        infcolumnsMapImage.put("UPDATEPERSON", userid);
        infcolumnsMapImage.put("FILENAME", "");
        Map<Object, Object> infwheresMapImage = new HashMap<Object, Object>();
        infwheresMapImage.put("ID", imageView.getTag(R.id.infimage_id).toString());
        trSpecialBushingFileDao.updateTrSpecialBushingFileInfo(infcolumnsMapImage,infwheresMapImage);
        Map<Object, Object> viscolumnsMapImage = new HashMap<Object, Object>();
        viscolumnsMapImage.put("FILENUMBER", "");
        viscolumnsMapImage.put("UPDATETIME", date);
        viscolumnsMapImage.put("UPDATEPERSON", userid);
        viscolumnsMapImage.put("FILENAME", "");
        Map<Object, Object> viswheresMapImage = new HashMap<Object, Object>();
        viswheresMapImage.put("ID", imageView.getTag(R.id.visibleimage_id).toString());
        trSpecialBushingFileDao.updateTrSpecialBushingFileInfo(viscolumnsMapImage,viswheresMapImage);
        //发送消息
        TrSpecialBushingFile inftrSpecialButshingFile = new TrSpecialBushingFile();
        inftrSpecialButshingFile.setFilenumber("");
        inftrSpecialButshingFile.setUpdatetime(date);
        inftrSpecialButshingFile.setUpdateperson(userid);
        inftrSpecialButshingFile.setFilename("");
        inftrSpecialButshingFile.setId(imageView.getTag(R.id.infimage_id).toString());
        TrSpecialBushingFile vistrSpecialButshingFile = new TrSpecialBushingFile();
        vistrSpecialButshingFile.setFilenumber("");
        vistrSpecialButshingFile.setUpdatetime(date);
        vistrSpecialButshingFile.setUpdateperson(userid);
        vistrSpecialButshingFile.setFilename("");
        vistrSpecialButshingFile.setId(imageView.getTag(R.id.visibleimage_id).toString());
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(inftrSpecialButshingFile);
        list2.add(vistrSpecialButshingFile);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
        infnumber.setText("");
        visnumber.setText("");
        infimage.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
        visimage.setImageResource(R.drawable.ic_switch_camera_grey600_36dp);
    }

    private void deleteInfPhoto(String inffileid,String vibfileid) { //从数据库中删除照片
        TrDetectionTempletFileObjDao fileObjDao=new TrDetectionTempletFileObjDao(context);
        Map<Object,Object> infcolumnsMap=new HashMap<>();
        infcolumnsMap.put("ISUPLOAD","2");
        Map<Object,Object> infwheresMap=new HashMap<>();
        infwheresMap.put("ID",inffileid);
        fileObjDao.updateTrDetectionTempletFileObjInfo(infcolumnsMap,infwheresMap);
        Map<Object,Object> vibcolumnsMap=new HashMap<>();
        vibcolumnsMap.put("ISUPLOAD","2");
        Map<Object,Object> vibwheresMap=new HashMap<>();
        vibwheresMap.put("ID",vibfileid);
        fileObjDao.updateTrDetectionTempletFileObjInfo(vibcolumnsMap,vibwheresMap);
        TrDetectiontempletFileObj inffileObj=new TrDetectiontempletFileObj();
        inffileObj.setId(inffileid);
        inffileObj.setIsupload("2");
        TrDetectiontempletFileObj vibfileObj=new TrDetectiontempletFileObj();
        vibfileObj.setId(vibfileid);
        vibfileObj.setIsupload("2");
        List<List<Object>> list1 = new ArrayList<List<Object>>();
        List<Object> list2 = new ArrayList<Object>();
        list2.add(inffileObj);
        list2.add(vibfileObj);
        list1.add(list2);
        sympMessageRealDao.updateTextMessages(list1);
    }

    private void createdelDialog() {  //删除提示的dialog
        photodialog=new MaterialDialog(context);
        photodialog.setTitle("删除").setMessage("至少应该保留一张照片").setCanceledOnTouchOutside(true).show();
    }

    private void createwaitDialog() {
        if(!context.isFinishing()){  //不加判断关闭Avtivity重新打开会出异常
            waitdialog=new MaterialDialog(context);
            View v=LayoutInflater.from(context).inflate(R.layout.progressdialog, null);
            TextView text= (TextView) v.findViewById(R.id.text);
            text.setText("正在获取数据，请稍候..");
            waitdialog.setView(v).show();
        }
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
        return dataList;
    }

    private List<TrDetectiontempletDataObj> getTrDetectionTempletData(String id) { //获取检测项目模板（输入项）实体表信息
        TrDetectionTempletDataObjDao trDetectionTempletDataObjDao=new TrDetectionTempletDataObjDao(context);
        TrDetectiontempletDataObj trDetectiontempletDataObj=new TrDetectiontempletDataObj();
        trDetectiontempletDataObj.setDetectionobjid(id);
        List<TrDetectiontempletDataObj> data = trDetectionTempletDataObjDao.queryTrDetectionTempletDataObjList(trDetectiontempletDataObj);
        return data;
    }

    //套管数据展示方法
    private void bushingData(EditText max,EditText min,EditText avg,List<TrSpecialBushingData> trSpecialBushingDataList){
        max.setText((trSpecialBushingDataList.get(0).getResult() == null || trSpecialBushingDataList.get(0).getResult().equals("null")) ? "" : trSpecialBushingDataList.get(0).getResult());
        min.setText((trSpecialBushingDataList.get(1).getResult() == null || trSpecialBushingDataList.get(1).getResult().equals("null")) ? "" : trSpecialBushingDataList.get(1).getResult());
        avg.setText((trSpecialBushingDataList.get(2).getResult() == null || trSpecialBushingDataList.get(2).getResult().equals("null")) ? "" : trSpecialBushingDataList.get(2).getResult());
        max.setTag(trSpecialBushingDataList.get(0).getId());
        min.setTag(trSpecialBushingDataList.get(1).getId());
        avg.setTag(trSpecialBushingDataList.get(2).getId());
    }

    //红外套管文件展示方法
    private void bushingFile(final EditText num,ImageView image, final TrSpecialBushingFile trSpecialBushingFile, final TrSpecialBushingPosition position, final int orderby,ProgressBar bar){
        context.imageBushingGet(trSpecialBushingFile,image,bar);
        if(trSpecialBushingFile.getFilenumber() != null && !trSpecialBushingFile.getFilenumber().equals("")){
            num.setText(trSpecialBushingFile.getFilenumber());
            //image.setBackgroundResource(0);
        }else{
            num.setText("");
            //image.setBackgroundResource(0);
        }
        image.setTag(R.id.pic_id, trSpecialBushingFile.getId());
        image.setTag(R.id.pic_name, trSpecialBushingFile.getFilename());
        image.setTag(R.id.position_id, position.getId());
        //点击图片放大
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileNumber = num.getText().toString();
                if(!fileNumber.equals("")){
                    if(Constants.isExistPic(pic_route + "small/",fileNumber,context)){
                        String url = URLs.HTTP + URLs.HOSTA + URLs.IMAGEPATH + trTask.getProjectid() + "/" + trTask.getTaskid() + "/original/";
                        Intent intent = new Intent(context, ImagePagerActivity.class);
                        intent.putExtra("index", orderby);
                        intent.putExtra("url", url);
                        intent.putExtra("localurl", pic_route + "original");
                        intent.putExtra("type", "bushing");
                        intent.putExtra("id", position.getId());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,"暂无照片",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<TrDetectiontempletFileObj> getFile(TrDetectiontypEtempletObj trDetectiontypEtempletObj) { //查询模板下面是否有图片
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getId());
        List<TrDetectiontempletFileObj> list = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(fileObj);
        return list;
    }

    public List<TrDetectiontempletFileObj> getFileItem(TrDetectiontypEtempletObj trDetectiontypEtempletObj) { //查询采集卡下面是否有图片
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
        List<TrDetectiontempletFileObj> list = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(fileObj);
        return list;
    }

    public List<String> getFileItemInfId(TrDetectiontypEtempletObj trDetectiontypEtempletObj) { //查询采集卡下面一组图片的标识id
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getDetectiontemplet());
        List<String> idList = trDetectionTempletFileObjDao.queryTrDetectionTempletFileId(fileObj);
        return idList;
    }

    public List<TrDetectiontempletFileObj> getFileItemInf(TrDetectiontempletFileObj trDetectiontempletFileObj) { //查询红外采集卡下面图片
        List<TrDetectiontempletFileObj> list = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(trDetectiontempletFileObj);
        return list;
    }

    public List<TrDetectiontempletFileObj> getUltFileItem(TrDetectiontypEtempletObj trDetectiontypEtempletObj,String filenumber) { //查询采集卡下面是否有图片
        TrDetectiontempletFileObj fileObj=new TrDetectiontempletFileObj();
        fileObj.setDetectionobjid(trDetectiontypEtempletObj.getId());
        fileObj.setFilenumber(filenumber);
        List<TrDetectiontempletFileObj> list = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(fileObj);
        return list;
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

    //查询检测项目（套管）数据表
    public List<TrSpecialBushingData> getbushingObjData(String positionid){
        TrSpecialBushingData trSpecialBushingData = new TrSpecialBushingData();
        trSpecialBushingData.setPositionid(positionid);
        List<TrSpecialBushingData> trSpecialBushingDatas = new ArrayList<TrSpecialBushingData>();
        TrSpecialBushingDataDao trSpecialBushingDataDao = new TrSpecialBushingDataDao(context);
        trSpecialBushingDatas = trSpecialBushingDataDao.queryTrSpecialBushingDataList(trSpecialBushingData);
        return trSpecialBushingDatas;
    }

    //查询检测项目表（套管）
    public List<TrSpecialBushing> getbushing(String id){
        TrSpecialBushing trSpecialBushing = new TrSpecialBushing();
        trSpecialBushing.setId(id);
        List<TrSpecialBushing> trSpecialBushings = new ArrayList<TrSpecialBushing>();
        TrSpecialBushingDao trSpecialBushingDao = new TrSpecialBushingDao(context);
        trSpecialBushings = trSpecialBushingDao.queryTrSpecialBushingList(trSpecialBushing);
        return trSpecialBushings;
    }

    //查询检测项目（套管）文件表
    public List<TrSpecialBushingFile> getbushingFile(TrSpecialBushingFile bushFile){
        List<TrSpecialBushingFile> trSpecialBushingFiles = new ArrayList<TrSpecialBushingFile>();
        trSpecialBushingFiles = trSpecialBushingFileDao.queryTrSpecialBushingFileList(bushFile);
        return trSpecialBushingFiles;
    }
    //查询文件模板实体表(普通图片)
    public List<TrDetectiontempletFileObj> getGeneralFile(TrDetectiontempletFileObj file){
        List<TrDetectiontempletFileObj> fileList = new ArrayList<TrDetectiontempletFileObj>();
        fileList = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(file);
        return fileList;
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

//    //修改三层目录状态
//    public void updateStstus(String status,String id){
//        TrDetectionTypeTempletObjDao trDetectionTypeTempletObjDao = new TrDetectionTypeTempletObjDao(context);
//        Map<Object,Object> columnsMap = new HashMap<Object,Object>();
//        columnsMap.put("STATUS",status);
//        Map<Object,Object> wheresMap = new  HashMap<Object,Object>();
//        wheresMap.put("ID",id);
//        trDetectionTypeTempletObjDao.updateTrDetectionTypeTempletObjInfo(columnsMap, wheresMap);
//        TrDetectiontypEtempletObj trDetectiontypEtempletObj = new TrDetectiontypEtempletObj();
//        trDetectiontypEtempletObj.setStatus(status);
//        trDetectiontypEtempletObj.setId(id);
//        List<List<Object>> list1 = new ArrayList<List<Object>>();
//        List<Object> list2 = new ArrayList<Object>();
//        list2.add(trDetectiontypEtempletObj);
//        list1.add(list2);
//        sympMessageRealDao.updateTextMessages(list1);
//    }

    private Map<String,Boolean> getDetectiondeperson(TrSpecialWork date) {  //截取人员的名字
        Map<String,Boolean> personslist = new HashMap<String,Boolean>();
        String taskPersons=getTaskPerson();
        String wordPerson=date.getDetectiondeperson();
        if(!taskPersons.equals("")){
            String[] taskPer = taskPersons.split(";");
            String[] wordPer = wordPerson.split(";");
            for(int i = 0;i < taskPer.length;i++){
                personslist.put(taskPer[i],false);
                for(int j = 0; j < wordPer.length;j++){
                    if(taskPer[i].equals(wordPer[j])){
                        personslist.put(taskPer[i],true);
                    }
                }
            }
        }
        return personslist;
    }

    private String getTaskPerson() {  //从任务表中获取小组成员
        TrTaskDao taskDao=new TrTaskDao(context);
        TrTask trTaskParam=new TrTask();
        trTaskParam.setTaskid(trTask.getTaskid());
        List<TrTask> datalist = taskDao.queryTrTaskList(trTaskParam);
        if(datalist.size()>0){
            return datalist.get(0).getWorkperson();
        }
        return "";
    }

    //获取油色谱的数据
    public  List<TrSpecialOliData>  getOildata(TrDetectiontypEtempletObj data) {
        TrSpecialOliDataDao oliDataDao=new TrSpecialOliDataDao(context);
        TrSpecialOliData oildata = new TrSpecialOliData();
        oildata.setDetectionprojectid(data.getId());
        List<TrSpecialOliData> oildatalist = oliDataDao.queryTrSpecialOliDataList(oildata);
        return oildatalist;
    }
    //获取作业情况记录
    public List<TrSpecialWork> getWorkrecord(TrDetectiontypEtempletObj data){
        TrSpecialWorkDao workDao=new TrSpecialWorkDao(context);
        TrSpecialWork trSpecialWork=new TrSpecialWork();
        trSpecialWork.setDetectionprojectid(data.getId());
        List<TrSpecialWork> workdata = workDao.queryTrSpecialWorkList(trSpecialWork);
        return workdata;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class MyviewHolder implements View.OnClickListener{
        TextView textView1; //上层的textview
        ImageView image1,upimage,downimage;

        TextView textView2; //下层的textview
        ListViewForScrollView listView; //下层的listview
        LinearLayout relativeLayout; //点击的
        LinearLayout job_content_del; //删除按钮
        LinearLayout linearLayout; //显示隐藏的
        ImageView image;
        View seconed_view;

        TextView textView3;  //套管模板(普通)
        View view,ultrasonic_view1,ultrasonic_view2,ultrasonic_view3;


        EditText result,remarkinput;   //检查结果,备注(普通)
        TextView method,standard;//方法，标准(套管)
        GridView gridView;   //输入值
        CheckBox checkYes,checkNo;
        LinearLayout general_item_image1;

        HorizontalScrollView general_hlistview;  //显示图片的HorizontalScrollView
        LinearLayout gener_ll;

        EditText ha_max,ha_min,ha_avg,hb_max,hb_min,hb_avg,hc_max,hc_min,hc_avg,ho_max,ho_min,ho_avg,ma_max,ma_min,ma_avg,mb_max,
                mb_min,mb_avg,mc_max,mc_min,mc_avg,mo_max,mo_min,mo_avg,la_max,la_min,la_avg,lb_max,lb_min,lb_avg,lc_max,lc_min,
                lc_avg,lo_max,lo_min,lo_avg,s1_max,s1_min,s1_avg,s2_max,s2_min,s2_avg,general_item_result,bushing_item_result,remarks_result,
                ha_infimage_number,ha_visibleimage_number,hb_infimage_number,hb_visibleimage_number,hc_infimage_number,hc_visibleimage_number,ho_infimage_number,ho_visibleimage_number,
                ma_infimage_number,ma_visibleimage_number,mb_infimage_number,mb_visibleimage_number,mc_infimage_number,mc_visibleimage_number,mo_infimage_number,mo_visibleimage_number,
                la_infimage_number,la_visibleimage_number,lb_infimage_number,lb_visibleimage_number,lc_infimage_number,lc_visibleimage_number,lo_infimage_number,lo_visibleimage_number,
                s1_infimage_number,s1_visibleimage_number,s2_infimage_number,s2_visibleimage_number;
        ImageView ha_infimage_show,ha_visibleimage_show,hb_infimage_show,hb_visibleimage_show,hc_infimage_show,hc_visibleimage_show,ho_infimage_show,ho_visibleimage_show,
                ma_infimage_show,ma_visibleimage_show,mb_infimage_show,mb_visibleimage_show,mc_infimage_show,mc_visibleimage_show,mo_infimage_show,mo_visibleimage_show,
                la_infimage_show,la_visibleimage_show,lb_infimage_show,lb_visibleimage_show,lc_infimage_show,lc_visibleimage_show,lo_infimage_show,lo_visibleimage_show,
                s1_infimage_show,s1_visibleimage_show,s2_infimage_show,s2_visibleimage_show,ha_deletepictures,hb_deletepictures,hc_deletepictures,ho_deletepictures,
                ma_deletepictures,mb_deletepictures,mc_deletepictures,mo_deletepictures,la_deletepictures,lb_deletepictures,lc_deletepictures,lo_deletepictures,
                s1_deletepictures,s2_deletepictures,ha_getpictures, hb_getpictures, hc_getpictures, ho_getpictures, ma_getpictures, mb_getpictures, mc_getpictures, mo_getpictures,
                la_getpictures, lb_getpictures, lc_getpictures, lo_getpictures, s1_getpictures, s2_getpictures;
        ProgressBar ha_infimage_bar,ha_visibleimage_bar, hb_infimage_bar, hb_visibleimage_bar, hc_infimage_bar, hc_visibleimage_bar, ho_infimage_bar, ho_visibleimage_bar,
                ma_infimage_bar, ma_visibleimage_bar, mb_infimage_bar, mb_visibleimage_bar, mc_infimage_bar, mc_visibleimage_bar, mo_infimage_bar, mo_visibleimage_bar,
                la_infimage_bar, la_visibleimage_bar, lb_infimage_bar, lb_visibleimage_bar, lc_infimage_bar, lc_visibleimage_bar, lo_infimage_bar, lo_visibleimage_bar,
                s1_infimage_bar, s1_visibleimage_bar, s2_infimage_bar, s2_visibleimage_bar;
        MyItemClickListener mItemClickListener;

        ListView listView1; //油色谱模板

        ListViewForScrollView workrecordlv;//作业情况记录

        LinearLayout layout;//用来放在前面显示图片的采集卡
        HorizontalScrollView horizontalScrollView,ultrasonic_hlistview;

        //ultrasonic模板
        TextView ultrasonic_content;   //检测内容
        EditText ultrasonic_result;    //检测结果
        EditText ultrasonic_remarks;   //备注
        Switch ultrasonic_standard;
        ImageView ult_hv_pic,ult_mv_pic,ult_lv_pic,ult_sv_pic,ult_ha_pic1,ult_ha_pic2,ult_ha_pic3,ult_ha_pic4,ult_hb_pic1,ult_hb_pic2,ult_hb_pic3,ult_hb_pic4
                ,ult_hc_pic1,ult_hc_pic2,ult_hc_pic3,ult_hc_pic4,ult_ma_pic1,ult_ma_pic2,ult_ma_pic3,ult_ma_pic4,ult_mb_pic1,ult_mb_pic2,ult_mb_pic3,ult_mb_pic4
                ,ult_mc_pic1,ult_mc_pic2,ult_mc_pic3,ult_mc_pic4,ult_la_pic1,ult_la_pic2,ult_la_pic3,ult_la_pic4,ult_lb_pic1,ult_lb_pic2,ult_lb_pic3,ult_lb_pic4
                ,ult_lc_pic1,ult_lc_pic2,ult_lc_pic3,ult_lc_pic4,ult_sa_pic1,ult_sa_pic2,ult_sa_pic3,ult_sa_pic4,ult_sb_pic1,ult_sb_pic2,ult_sb_pic3,ult_sb_pic4
                ,ult_sc_pic1,ult_sc_pic2,ult_sc_pic3,ult_sc_pic4;

        ListViewForScrollView transformer_oil_list;  //变压器油样常规测试分析

        MyviewHolder(int type,View v,MyItemClickListener listener) {

            if(type==1){
               textView1= (TextView) v.findViewById(R.id.job_coutent_name);
                image1= (ImageView) v.findViewById(R.id.job_coutent_pic2);
                upimage = (ImageView)v.findViewById(R.id.job_coutent_pic3);
                job_content_del= (LinearLayout) v.findViewById(R.id.job_content_del);
            }
            if(type==2){
                textView2= (TextView) v.findViewById(R.id.job_coutent_name);
                listView= (ListViewForScrollView) v.findViewById(R.id.job_list_hide);
                relativeLayout= (LinearLayout) v.findViewById(R.id.job_layout);
                job_content_del= (LinearLayout) v.findViewById(R.id.job_content_del);
                linearLayout= (LinearLayout) v.findViewById(R.id.job_layout_hide);
                image= (ImageView) v.findViewById(R.id.job_coutent_pic2);
                seconed_view=v.findViewById(R.id.seconed_view);
                downimage = (ImageView)v.findViewById(R.id.job_coutent_pic3);

            }
            if(type==3){
                textView3= (TextView) v.findViewById(R.id.bushing_item_content);
                method= (TextView) v.findViewById(R.id.bushing_item_method);
                standard= (TextView) v.findViewById(R.id.bushing_item_standard);
                bushing_item_result = (EditText) v.findViewById(R.id.bushing_item_result);
                remarks_result = (EditText)v.findViewById(R.id.remarks_result);
                checkYes = (CheckBox) v.findViewById(R.id.bushing_checkYes);
                checkNo = (CheckBox) v.findViewById(R.id.bushing_checkNo);

                setActionNext(bushing_item_result,remarks_result);

                ha_max = (EditText)v.findViewById(R.id.ha_max);
                ha_min = (EditText)v.findViewById(R.id.ha_min);
                ha_avg = (EditText)v.findViewById(R.id.ha_avg);
                hb_max = (EditText)v.findViewById(R.id.hb_max);
                hb_min = (EditText)v.findViewById(R.id.hb_min);
                hb_avg = (EditText)v.findViewById(R.id.hb_avg);
                hc_max = (EditText)v.findViewById(R.id.hc_max);
                hc_min = (EditText)v.findViewById(R.id.hc_min);
                hc_avg = (EditText)v.findViewById(R.id.hc_avg);
                ho_max = (EditText)v.findViewById(R.id.ho_max);
                ho_min = (EditText)v.findViewById(R.id.ho_min);
                ho_avg = (EditText)v.findViewById(R.id.ho_avg);
                ma_max = (EditText)v.findViewById(R.id.ma_max);
                ma_min = (EditText)v.findViewById(R.id.ma_min);
                ma_avg = (EditText)v.findViewById(R.id.ma_avg);
                mb_max = (EditText)v.findViewById(R.id.mb_max);
                mb_min = (EditText)v.findViewById(R.id.mb_min);
                mb_avg = (EditText)v.findViewById(R.id.mb_avg);
                mc_max = (EditText)v.findViewById(R.id.mc_max);
                mc_min = (EditText)v.findViewById(R.id.mc_min);
                mc_avg = (EditText)v.findViewById(R.id.mc_avg);
                mo_max = (EditText)v.findViewById(R.id.mo_max);
                mo_min = (EditText)v.findViewById(R.id.mo_min);
                mo_avg = (EditText)v.findViewById(R.id.mo_avg);
                la_max = (EditText)v.findViewById(R.id.la_max);
                la_min = (EditText)v.findViewById(R.id.la_min);
                la_avg = (EditText)v.findViewById(R.id.la_avg);
                lb_max = (EditText)v.findViewById(R.id.lb_max);
                lb_min = (EditText)v.findViewById(R.id.lb_min);
                lb_avg = (EditText)v.findViewById(R.id.lb_avg);
                lc_max = (EditText)v.findViewById(R.id.lc_max);
                lc_min = (EditText)v.findViewById(R.id.lc_min);
                lc_avg = (EditText)v.findViewById(R.id.lc_avg);
                lo_max = (EditText)v.findViewById(R.id.lo_max);
                lo_min = (EditText)v.findViewById(R.id.lo_min);
                lo_avg = (EditText)v.findViewById(R.id.lo_avg);
                s1_max = (EditText)v.findViewById(R.id.s1_max);
                s1_min = (EditText)v.findViewById(R.id.s1_min);
                s1_avg = (EditText)v.findViewById(R.id.s1_avg);
                s2_max = (EditText)v.findViewById(R.id.s2_max);
                s2_min = (EditText)v.findViewById(R.id.s2_min);
                s2_avg = (EditText)v.findViewById(R.id.s2_avg);

                ha_infimage_show = (ImageView)v.findViewById(R.id.ha_infimage_show);
                ha_visibleimage_show = (ImageView)v.findViewById(R.id.ha_visibleimage_show);
                hb_infimage_show = (ImageView)v.findViewById(R.id.hb_infimage_show);
                hb_visibleimage_show = (ImageView)v.findViewById(R.id.hb_visibleimage_show);
                hc_infimage_show = (ImageView)v.findViewById(R.id.hc_infimage_show);
                hc_visibleimage_show = (ImageView)v.findViewById(R.id.hc_visibleimage_show);
                ho_infimage_show = (ImageView)v.findViewById(R.id.ho_infimage_show);
                ho_visibleimage_show = (ImageView)v.findViewById(R.id.ho_visibleimage_show);
                ma_infimage_show = (ImageView)v.findViewById(R.id.ma_infimage_show);
                ma_visibleimage_show = (ImageView)v.findViewById(R.id.ma_visibleimage_show);
                mb_infimage_show = (ImageView)v.findViewById(R.id.mb_infimage_show);
                mb_visibleimage_show = (ImageView)v.findViewById(R.id.mb_visibleimage_show);
                mc_infimage_show = (ImageView)v.findViewById(R.id.mc_infimage_show);
                mc_visibleimage_show = (ImageView)v.findViewById(R.id.mc_visibleimage_show);
                mo_infimage_show = (ImageView)v.findViewById(R.id.mo_infimage_show);
                mo_visibleimage_show = (ImageView)v.findViewById(R.id.mo_visibleimage_show);
                la_infimage_show = (ImageView)v.findViewById(R.id.la_infimage_show);
                la_visibleimage_show = (ImageView)v.findViewById(R.id.la_visibleimage_show);
                lb_infimage_show = (ImageView)v.findViewById(R.id.lb_infimage_show);
                lb_visibleimage_show = (ImageView)v.findViewById(R.id.lb_visibleimage_show);
                lc_infimage_show = (ImageView)v.findViewById(R.id.lc_infimage_show);
                lc_visibleimage_show = (ImageView)v.findViewById(R.id.lc_visibleimage_show);
                lo_infimage_show = (ImageView)v.findViewById(R.id.lo_infimage_show);
                lo_visibleimage_show = (ImageView)v.findViewById(R.id.lo_visibleimage_show);
                s1_infimage_show = (ImageView)v.findViewById(R.id.s1_infimage_show);
                s1_visibleimage_show = (ImageView)v.findViewById(R.id.s1_visibleimage_show);
                s2_infimage_show = (ImageView)v.findViewById(R.id.s2_infimage_show);
                s2_visibleimage_show = (ImageView)v.findViewById(R.id.s2_visibleimage_show);

                ha_infimage_number = (EditText)v.findViewById(R.id.ha_infimage_number);
                ha_visibleimage_number = (EditText)v.findViewById(R.id.ha_visibleimage_number);
                hb_infimage_number = (EditText)v.findViewById(R.id.hb_infimage_number);
                hb_visibleimage_number = (EditText)v.findViewById(R.id.hb_visibleimage_number);
                hc_infimage_number = (EditText)v.findViewById(R.id.hc_infimage_number);
                hc_visibleimage_number = (EditText)v.findViewById(R.id.hc_visibleimage_number);
                ho_infimage_number = (EditText)v.findViewById(R.id.ho_infimage_number);
                ho_visibleimage_number = (EditText)v.findViewById(R.id.ho_visibleimage_number);
                ma_infimage_number = (EditText)v.findViewById(R.id.ma_infimage_number);
                ma_visibleimage_number = (EditText)v.findViewById(R.id.ma_visibleimage_number);
                mb_infimage_number = (EditText)v.findViewById(R.id.mb_infimage_number);
                mb_visibleimage_number = (EditText)v.findViewById(R.id.mb_visibleimage_number);
                mc_infimage_number = (EditText)v.findViewById(R.id.mc_infimage_number);
                mc_visibleimage_number = (EditText)v.findViewById(R.id.mc_visibleimage_number);
                mo_infimage_number = (EditText)v.findViewById(R.id.mo_infimage_number);
                mo_visibleimage_number = (EditText)v.findViewById(R.id.mo_visibleimage_number);
                la_infimage_number = (EditText)v.findViewById(R.id.la_infimage_number);
                la_visibleimage_number = (EditText)v.findViewById(R.id.la_visibleimage_number);
                lb_infimage_number = (EditText)v.findViewById(R.id.lb_infimage_number);
                lb_visibleimage_number = (EditText)v.findViewById(R.id.lb_visibleimage_number);
                lc_infimage_number = (EditText)v.findViewById(R.id.lc_infimage_number);
                lc_visibleimage_number = (EditText)v.findViewById(R.id.lc_visibleimage_number);
                lo_infimage_number = (EditText)v.findViewById(R.id.lo_infimage_number);
                lo_visibleimage_number = (EditText)v.findViewById(R.id.lo_visibleimage_number);
                s1_infimage_number = (EditText)v.findViewById(R.id.s1_infimage_number);
                s1_visibleimage_number = (EditText)v.findViewById(R.id.s1_visibleimage_number);
                s2_infimage_number = (EditText)v.findViewById(R.id.s2_infimage_number);
                s2_visibleimage_number = (EditText)v.findViewById(R.id.s2_visibleimage_number);

                ha_deletepictures = (ImageView)v.findViewById(R.id.ha_deletepictures);
                hb_deletepictures = (ImageView)v.findViewById(R.id.hb_deletepictures);
                hc_deletepictures = (ImageView)v.findViewById(R.id.hc_deletepictures);
                ho_deletepictures = (ImageView)v.findViewById(R.id.ho_deletepictures);
                ma_deletepictures = (ImageView)v.findViewById(R.id.ma_deletepictures);
                mb_deletepictures = (ImageView)v.findViewById(R.id.mb_deletepictures);
                mc_deletepictures = (ImageView)v.findViewById(R.id.mc_deletepictures);
                mo_deletepictures = (ImageView)v.findViewById(R.id.mo_deletepictures);
                la_deletepictures = (ImageView)v.findViewById(R.id.la_deletepictures);
                lb_deletepictures = (ImageView)v.findViewById(R.id.lb_deletepictures);
                lc_deletepictures = (ImageView)v.findViewById(R.id.lc_deletepictures);
                lo_deletepictures = (ImageView)v.findViewById(R.id.lo_deletepictures);
                s1_deletepictures = (ImageView)v.findViewById(R.id.s1_deletepictures);
                s2_deletepictures = (ImageView)v.findViewById(R.id.s2_deletepictures);

                ha_infimage_bar = (ProgressBar)v.findViewById(R.id.ha_infimage_bar);
                ha_visibleimage_bar = (ProgressBar)v.findViewById(R.id.ha_visibleimage_bar);
                hb_infimage_bar = (ProgressBar)v.findViewById(R.id.hb_infimage_bar);
                hb_visibleimage_bar = (ProgressBar)v.findViewById(R.id.hb_visibleimage_bar);
                hc_infimage_bar = (ProgressBar)v.findViewById(R.id.hc_infimage_bar);
                hc_visibleimage_bar = (ProgressBar)v.findViewById(R.id.hc_visibleimage_bar);
                ho_infimage_bar = (ProgressBar)v.findViewById(R.id.ho_infimage_bar);
                ho_visibleimage_bar = (ProgressBar)v.findViewById(R.id.ho_visibleimage_bar);
                ma_infimage_bar = (ProgressBar)v.findViewById(R.id.ma_infimage_bar);
                ma_visibleimage_bar = (ProgressBar)v.findViewById(R.id.ma_visibleimage_bar);
                mb_infimage_bar = (ProgressBar)v.findViewById(R.id.mb_infimage_bar);
                mb_visibleimage_bar = (ProgressBar)v.findViewById(R.id.mb_visibleimage_bar);
                mc_infimage_bar = (ProgressBar)v.findViewById(R.id.mc_infimage_bar);
                mc_visibleimage_bar = (ProgressBar)v.findViewById(R.id.mc_visibleimage_bar);
                mo_infimage_bar = (ProgressBar)v.findViewById(R.id.mo_infimage_bar);
                mo_visibleimage_bar = (ProgressBar)v.findViewById(R.id.mo_visibleimage_bar);
                la_infimage_bar = (ProgressBar)v.findViewById(R.id.la_infimage_bar);
                la_visibleimage_bar = (ProgressBar)v.findViewById(R.id.la_visibleimage_bar);
                lb_infimage_bar = (ProgressBar)v.findViewById(R.id.lb_infimage_bar);
                lb_visibleimage_bar = (ProgressBar)v.findViewById(R.id.lb_visibleimage_bar);
                lc_infimage_bar = (ProgressBar)v.findViewById(R.id.lc_infimage_bar);
                lc_visibleimage_bar = (ProgressBar)v.findViewById(R.id.lc_visibleimage_bar);
                lo_infimage_bar = (ProgressBar)v.findViewById(R.id.lo_infimage_bar);
                lo_visibleimage_bar = (ProgressBar)v.findViewById(R.id.lo_visibleimage_bar);
                s1_infimage_bar = (ProgressBar)v.findViewById(R.id.s1_infimage_bar);
                s1_visibleimage_bar = (ProgressBar)v.findViewById(R.id.s1_visibleimage_bar);
                s2_infimage_bar = (ProgressBar)v.findViewById(R.id.s2_infimage_bar);
                s2_visibleimage_bar = (ProgressBar)v.findViewById(R.id.s2_visibleimage_bar);

                this.mItemClickListener = listener;

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    EditText[] editTexts = new EditText[]{
                            bushing_item_result,remarks_result,ha_max,ha_min,ha_avg,hb_max,hb_min,hb_avg,hc_max,hc_min,hc_avg,ho_max,ho_min,ho_avg,ma_max,ma_min,ma_avg,mb_max,mb_min,
                            mb_avg,mc_max,mc_min,mc_avg,mo_max,mo_min,mo_avg,la_max,la_min,la_avg,lb_max,lb_min,lb_avg,lc_max,lc_min,lc_avg,lo_max,lo_min,lo_avg,s1_max,s1_min,s1_avg,
                            s2_max,s2_min,s2_avg,ha_infimage_number,ha_visibleimage_number,hb_infimage_number,hb_visibleimage_number,hc_infimage_number,hc_visibleimage_number,
                            ho_infimage_number,ho_visibleimage_number,ma_infimage_number,ma_visibleimage_number,mb_infimage_number,mb_visibleimage_number,mc_infimage_number,
                            mc_visibleimage_number,mo_infimage_number,mo_visibleimage_number,la_infimage_number,la_visibleimage_number,lb_infimage_number,lb_visibleimage_number,
                            lc_infimage_number,lc_visibleimage_number,lo_infimage_number,lo_visibleimage_number,s1_infimage_number,s1_visibleimage_number,s2_infimage_number,s2_visibleimage_number
                    };
                    ImageView[] imageViews=new ImageView[]{
                            ha_deletepictures,hb_deletepictures,hc_deletepictures,ho_deletepictures,ma_deletepictures,mb_deletepictures,mc_deletepictures,mo_deletepictures,
                            la_deletepictures,lb_deletepictures,lc_deletepictures,lo_deletepictures,s1_deletepictures,s2_deletepictures,ha_getpictures,hb_getpictures,hc_getpictures,
                            ho_getpictures, ma_getpictures,mb_getpictures,mc_getpictures,mo_getpictures,la_getpictures,lb_getpictures,lc_getpictures,lo_getpictures,s1_getpictures,s2_getpictures
                    };
                    for(int i=0;i<editTexts.length;i++){
                        if(editTexts[i]!=null){
                            editTexts[i].setEnabled(false);
                        }
                    }
                    for(int i=0;i<imageViews.length;i++){
                        if(imageViews[i]!=null){
                            imageViews[i].setEnabled(false);
                            imageViews[i].setVisibility(View.GONE);
                        }
                    }
                }
            }
            if(type==4){
               workrecordlv= (ListViewForScrollView) v.findViewById(R.id.open_cond_record_list);
            }
            if(type==5){ //第一个采集卡
                horizontalScrollView= (HorizontalScrollView) v.findViewById(R.id.image_hlistview);
                layout= (LinearLayout) v.findViewById(R.id.image_ll);
            }
            if(type==6){
                listView1= (ListView) v.findViewById(R.id.oillist);
            }
            if(type==7){
                textView3= (TextView) v.findViewById(R.id.general_item_content);
//                general_item_result = (EditText) v.findViewById(R.id.general_item_result);
//                remarks_result = (EditText)v.findViewById(R.id.remarks_result);

                result= (EditText) v.findViewById(R.id.general_item_result);
                remarkinput= (EditText) v.findViewById(R.id.general_item_remarkinput);
                method= (TextView) v.findViewById(R.id.general_item_method);
                standard= (TextView) v.findViewById(R.id.general_item_standard);
                gridView= (GridView) v.findViewById(R.id.general_item_gv);
                checkYes = (CheckBox) v.findViewById(R.id.general_checkYes);
                checkNo = (CheckBox) v.findViewById(R.id.general_checkNo);

                general_hlistview= (HorizontalScrollView) v.findViewById(R.id.general_hlistview);
                gener_ll= (LinearLayout) v.findViewById(R.id.gener_ll);

                general_item_image1= (LinearLayout) v.findViewById(R.id.general_item_image1);
                view = (View) v.findViewById(R.id.view);

                this.mItemClickListener = listener;
                //注册事件
                registerDoubleClickListener(general_item_image1,this.mItemClickListener,context);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    result.setEnabled(false);
                    remarkinput.setEnabled(false);
                    checkNo.setEnabled(false);
                    checkYes.setEnabled(false);
                    general_item_image1.setVisibility(View.INVISIBLE);
                }
                setActionNext(result,remarkinput);
            }
            if(type==8){
                method= (TextView) v.findViewById(R.id.general_item_method);
                standard= (TextView) v.findViewById(R.id.general_item_standard);
                ultrasonic_content= (TextView) v.findViewById(R.id.general_item_content);
                ultrasonic_result= (EditText) v.findViewById(R.id.general_item_result);
                ultrasonic_remarks= (EditText) v.findViewById(R.id.general_item_remarkinput);
                checkYes = (CheckBox) v.findViewById(R.id.general_checkYes);
                checkNo = (CheckBox) v.findViewById(R.id.general_checkNo);
                ultrasonic_hlistview = (HorizontalScrollView) v.findViewById(R.id.general_hlistview);
                ultrasonic_view1 = (View) v.findViewById(R.id.general_view1);
                ultrasonic_view2 = (View) v.findViewById(R.id.general_view2);
                ultrasonic_view3 = (View) v.findViewById(R.id.view);
                general_item_image1= (LinearLayout) v.findViewById(R.id.general_item_image1);

                if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                    ultrasonic_result.setEnabled(false);
                    ultrasonic_remarks.setEnabled(false);
                    checkYes.setEnabled(false);
                    checkNo.setEnabled(false);
                    general_item_image1.setVisibility(View.INVISIBLE);
                }

            }
            if(type==9){
                transformer_oil_list= (ListViewForScrollView) v.findViewById(R.id.transformer_oil_list);
            }
            if(type == 10){
                ult_hv_pic = (ImageView) v.findViewById(R.id.ult_hv_pic);
                ult_mv_pic = (ImageView) v.findViewById(R.id.ult_mv_pic);
                ult_lv_pic = (ImageView) v.findViewById(R.id.ult_lv_pic);
                ult_sv_pic = (ImageView) v.findViewById(R.id.ult_sv_pic);
                ult_ha_pic1 = (ImageView) v.findViewById(R.id.ult_ha_pic1);
                ult_ha_pic2 = (ImageView) v.findViewById(R.id.ult_ha_pic2);
                ult_ha_pic3 = (ImageView) v.findViewById(R.id.ult_ha_pic3);
                ult_ha_pic4 = (ImageView) v.findViewById(R.id.ult_ha_pic4);
                ult_hb_pic1 = (ImageView) v.findViewById(R.id.ult_hb_pic1);
                ult_hb_pic2 = (ImageView) v.findViewById(R.id.ult_hb_pic2);
                ult_hb_pic3 = (ImageView) v.findViewById(R.id.ult_hb_pic3);
                ult_hb_pic4 = (ImageView) v.findViewById(R.id.ult_hb_pic4);
                ult_hc_pic1 = (ImageView) v.findViewById(R.id.ult_hc_pic1);
                ult_hc_pic2 = (ImageView) v.findViewById(R.id.ult_hc_pic2);
                ult_hc_pic3 = (ImageView) v.findViewById(R.id.ult_hc_pic3);
                ult_hc_pic4 = (ImageView) v.findViewById(R.id.ult_hc_pic4);
                ult_ma_pic1 = (ImageView) v.findViewById(R.id.ult_ma_pic1);
                ult_ma_pic2 = (ImageView) v.findViewById(R.id.ult_ma_pic2);
                ult_ma_pic3 = (ImageView) v.findViewById(R.id.ult_ma_pic3);
                ult_ma_pic4 = (ImageView) v.findViewById(R.id.ult_ma_pic4);
                ult_mb_pic1 = (ImageView) v.findViewById(R.id.ult_mb_pic1);
                ult_mb_pic2 = (ImageView) v.findViewById(R.id.ult_mb_pic2);
                ult_mb_pic3 = (ImageView) v.findViewById(R.id.ult_mb_pic3);
                ult_mb_pic4 = (ImageView) v.findViewById(R.id.ult_mb_pic4);
                ult_mc_pic1 = (ImageView) v.findViewById(R.id.ult_mc_pic1);
                ult_mc_pic2 = (ImageView) v.findViewById(R.id.ult_mc_pic2);
                ult_mc_pic3 = (ImageView) v.findViewById(R.id.ult_mc_pic3);
                ult_mc_pic4 = (ImageView) v.findViewById(R.id.ult_mc_pic4);
                ult_la_pic1 = (ImageView) v.findViewById(R.id.ult_la_pic1);
                ult_la_pic2 = (ImageView) v.findViewById(R.id.ult_la_pic2);
                ult_la_pic3 = (ImageView) v.findViewById(R.id.ult_la_pic3);
                ult_la_pic4 = (ImageView) v.findViewById(R.id.ult_la_pic4);
                ult_lb_pic1 = (ImageView) v.findViewById(R.id.ult_lb_pic1);
                ult_lb_pic2 = (ImageView) v.findViewById(R.id.ult_lb_pic2);
                ult_lb_pic3 = (ImageView) v.findViewById(R.id.ult_lb_pic3);
                ult_lb_pic4 = (ImageView) v.findViewById(R.id.ult_lb_pic4);
                ult_lc_pic1 = (ImageView) v.findViewById(R.id.ult_lc_pic1);
                ult_lc_pic2 = (ImageView) v.findViewById(R.id.ult_lc_pic2);
                ult_lc_pic3 = (ImageView) v.findViewById(R.id.ult_lc_pic3);
                ult_lc_pic4 = (ImageView) v.findViewById(R.id.ult_lc_pic4);
                ult_sa_pic1 = (ImageView) v.findViewById(R.id.ult_sa_pic1);
                ult_sa_pic2 = (ImageView) v.findViewById(R.id.ult_sa_pic2);
                ult_sa_pic3 = (ImageView) v.findViewById(R.id.ult_sa_pic3);
                ult_sa_pic4 = (ImageView) v.findViewById(R.id.ult_sa_pic4);
                ult_sb_pic1 = (ImageView) v.findViewById(R.id.ult_sb_pic1);
                ult_sb_pic2 = (ImageView) v.findViewById(R.id.ult_sb_pic2);
                ult_sb_pic3 = (ImageView) v.findViewById(R.id.ult_sb_pic3);
                ult_sb_pic4 = (ImageView) v.findViewById(R.id.ult_sb_pic4);
                ult_sc_pic1 = (ImageView) v.findViewById(R.id.ult_sc_pic1);
                ult_sc_pic2 = (ImageView) v.findViewById(R.id.ult_sc_pic2);
                ult_sc_pic3 = (ImageView) v.findViewById(R.id.ult_sc_pic3);
                ult_sc_pic4 = (ImageView) v.findViewById(R.id.ult_sc_pic4);
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
                                Constants.recordExceptionInfo(e, context, context.toString()+"/OperationItemAdaper");
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

    /**
     * 设置点击键盘下一项获取焦点
     */
    private void setActionNext(EditText editTextFirst, final EditText editTextNext){
        editTextFirst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    editTextNext.setFocusable(true);
                    editTextNext.requestFocus();
                    return true;
                }
                return false;
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

