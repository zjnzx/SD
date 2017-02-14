package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.OperationItemActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTypeTempletObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialOliDataDao;
import com.energyfuture.symphony.m3.datepicker.DatePicker;
import com.energyfuture.symphony.m3.domain.TrDetectiontypEtempletObj;
import com.energyfuture.symphony.m3.domain.TrSpecialOliData;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public class OilListAdapter extends BaseAdapter implements View.OnClickListener{
    private OperationItemActivity context;
    private List<TrSpecialOliData> oildatalist;
    private TrSpecialOliDataDao trSpecialOliDataDao;
    private SympMessageRealDao sympMessageRealDao;
    private String userId,text1 = "";
    private TrTask trTask;
    private TrDetectiontypEtempletObj trDetectiontypEtempletObj;
    public OilListAdapter(Context context, List<TrSpecialOliData> oildatalist,TrTask trTask,TrDetectiontypEtempletObj trDetectiontypEtempletObj) {
        this.context = (OperationItemActivity) context;
        this.oildatalist = oildatalist;
        this.trTask=trTask;
        this.trDetectiontypEtempletObj = trDetectiontypEtempletObj;
        trSpecialOliDataDao = new TrSpecialOliDataDao(context);
        sympMessageRealDao = new SympMessageRealDao(context);
        userId = Constants.getLoginPerson(context).get("userId");
    }

    @Override
    public int getCount() {
        return oildatalist.size();
    }

    @Override
    public Object getItem(int position) {
        return oildatalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrSpecialOliData oildata = oildatalist.get(position);
        myViewholder viewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.oil_item1, null);
            viewholder=new myViewholder(convertView);
            convertView.setTag(viewholder);
        }else{
            viewholder= (myViewholder) convertView.getTag();
        }
        String detectiondes = oildata.getDetectiondes();
        viewholder.view3.setTag(R.id.oil_id, oildata.getId());
        viewholder.view3.setTag(R.id.oil_name, oildata.getDetectiondes());
        viewholder.view1.setText((detectiondes==null||detectiondes.equals("null"))?"":detectiondes);
        viewholder.view2.setText((oildata.getDetectionstandard()==null||oildata.getDetectionstandard().equals("null"))?"":oildata.getDetectionstandard());
        viewholder.view3.setText((oildata.getResult1()==null||oildata.getResult1().equals("null"))?"":oildata.getResult1());
        viewholder.view4.setText((oildata.getResult2()==null||oildata.getResult2().equals("null"))?"":oildata.getResult2());
        viewholder.view5.setText((oildata.getResult3()==null||oildata.getResult3().equals("null"))?"":oildata.getResult3());

        //输入框不可编辑
        viewholder.view4.setEnabled(false);
        viewholder.view5.setEnabled(false);

        if(position == 0){
            viewholder.view2.setVisibility(View.VISIBLE);
        }

        //隐藏输入框
        if(detectiondes.contains("测试温度") || detectiondes.contains("采样")){
            viewholder.view2.setVisibility(View.GONE);
        }

        if(position == (oildatalist.size() - 1)){
            viewholder.view3.setFocusableInTouchMode(false);
            viewholder.view3.setFocusable(false);
            viewholder.view3.setEnabled(true);
            viewholder.view3.setOnClickListener(this);
        }
        //检测结果监听事件
        updateOilInfo(viewholder.view3,oildata.getResult1(),"RESULT1",oildata.getId());
        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            viewholder.view2.setEnabled(false);
            viewholder.view3.setEnabled(false);
        }

        return convertView;
    }

    /**
     * 修改油色谱信息
     * @param editText
     * @param text
     * @param colum
     */
    private void updateOilInfo(final EditText editText, final String text, final String colum, final String id){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String date = Constants.dateformat2.format(new Date());
                    String textNew =  editText.getText().toString();
                    if(!textNew.equals("") && !text.equals(textNew) && !text1.equals(textNew)){
                        text1 = textNew;
                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                        columnsMap.put(colum,textNew);
                        columnsMap.put("UPDATETIME",date);
                        columnsMap.put("UPDATEPERSON",userId);
                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                        wheresMap.put("ID",id);
                        trSpecialOliDataDao.updateTrSpecialOliDataInfo(columnsMap, wheresMap);
                        //发送消息
                        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                        TrSpecialOliData trSpecialOliData = new TrSpecialOliData();
                        if(colum.equals("RESULT1")){
                            trSpecialOliData.setResult1(textNew);
                        }
                        trSpecialOliData.setUpdatetime(date);
                        trSpecialOliData.setUpdateperson(userId);
                        trSpecialOliData.setId(id);
                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trSpecialOliData);
                        list1.add(list2);
                        sympMessageRealDao.updateTextMessages(list1);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        final EditText editText = (EditText) v;
        final String id = editText.getTag(R.id.oil_id).toString();
        final String name = editText.getTag(R.id.oil_name).toString();
        if(!name.contains("氢气") && !name.contains("耐压")){
            String[] data=new String[]{userId,id};
            context.overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
            DatePicker dataPicker = new DatePicker(context);
            dataPicker.selectDateTimeDialog(editText,data,3);
            if(editText.equals("")){
                updateStatus(trDetectiontypEtempletObj,"");
            }else{
                updateStatus(trDetectiontypEtempletObj,"1");
            }
        }else{
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.setEnabled(true);
            editText.requestFocus();
            //弹出键盘
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
            return;
        }
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

    class myViewholder{
        TextView view1;
        EditText view2,view3,view4,view5;
        RelativeLayout layout1,layout2,layout3,layout4;
        myViewholder(View v) {
            view1= (TextView) v.findViewById(R.id.gascomponent);
            view2= (EditText) v.findViewById(R.id.carriedstard);
            view3= (EditText) v.findViewById(R.id.testresults1);
            view4= (EditText) v.findViewById(R.id.testresults2);
            view5= (EditText) v.findViewById(R.id.testresults3);
            layout1= (RelativeLayout) v.findViewById(R.id.oil_layout1);
            layout2= (RelativeLayout) v.findViewById(R.id.oil_layout2);
            layout3= (RelativeLayout) v.findViewById(R.id.oil_layout3);
            layout4= (RelativeLayout) v.findViewById(R.id.oil_layout4);
        }
    }
}
