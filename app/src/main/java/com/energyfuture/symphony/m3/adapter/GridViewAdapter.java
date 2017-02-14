package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletDataObjDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletDataObj;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MyEditText;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/16.
 */
public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private TrTask trTask;
    List<TrDetectiontempletDataObj> list;
    String userId = "";
    int figure = 0;
    public GridViewAdapter(Context context, List<TrDetectiontempletDataObj> list,TrTask trTask) {
        this.context = context;
        this.list = list;
        this.trTask=trTask;
        userId = Constants.getLoginPerson(context).get("userId");
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final TrDetectiontempletDataObj data = list.get(position);
        final Myviewholder myviewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.general_gridview_item, null);
            myviewholder=new Myviewholder(convertView);
            convertView.setTag(myviewholder);
        }else{
            myviewholder= (Myviewholder) convertView.getTag();
        }
        String dataformat = data.getDataformat().toString();
        if(!dataformat.equals("") && !dataformat.equals("0")){
            dataformat = dataformat.substring(dataformat.indexOf("."),dataformat.length());
            figure = dataformat.length() - 1;
        }else{
            myviewholder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        myviewholder.general_desc.setText(data.getDescribetion().equals("null") ? "" : data.getDescribetion());
        myviewholder.general_unit.setText(data.getDataunit().equals("null") ? "" : data.getDataunit());
        myviewholder.editText.setRawInputType(Configuration.KEYBOARD_12KEY);

        if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
            myviewholder.editText.setText(data.getResult().equals("null") ? " " : data.getResult());
        }else{
            myviewholder.editText.setText(data.getResult().equals("null") ? "" : data.getResult());
        }

        myviewholder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int d = temp.indexOf(".");
                if (d < 0) return;
                if (temp.length() - d - 1 > figure){
                    s.delete(d + (figure + 1), temp.length());
                }else if (d==0) {
                    s.delete(d, d+1);
                }
            }
        });
        final String result = data.getResult();

        //监听输入框
        final Myviewholder finalmyviewholder = myviewholder;
        finalmyviewholder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String date = Constants.dateformat2.format(new Date());

                if(!hasFocus){
                    String resultNew =  finalmyviewholder.editText.getText().toString();
                    if(!result.equals(resultNew)){
                        TrDetectionTempletDataObjDao trDetectionTempletDataObjDao = new TrDetectionTempletDataObjDao(context);
                        Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                        columnsMap.put("RESULT",resultNew);
                        columnsMap.put("UPDATETIME",date);
                        columnsMap.put("UPDATEPERSON",userId);
                        Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                        wheresMap.put("ID",data.getId());
                        trDetectionTempletDataObjDao.updateTrDetectionTempletDataObjInfo(columnsMap,wheresMap);
                        //发送消息
                        SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                        TrDetectiontempletDataObj trDetectionTempletDataObj = new TrDetectiontempletDataObj();
                        trDetectionTempletDataObj.setResult(resultNew);
                        trDetectionTempletDataObj.setUpdatetime(date);
                        trDetectionTempletDataObj.setUpdateperson(userId);
                        trDetectionTempletDataObj.setId(data.getId());
                        List<List<Object>> list1 = new ArrayList<List<Object>>();
                        List<Object> list2 = new ArrayList<Object>();
                        list2.add(trDetectionTempletDataObj);
                        list1.add(list2);
                        sympMessageRealDao.updateTextMessages(list1);
                    }

                }

            }
        });

        return convertView;
    }
    class Myviewholder{
        TextView general_desc;
        TextView general_unit;
        MyEditText editText;

        Myviewholder(View v) {
            general_desc= (TextView) v.findViewById(R.id.general_desc);
            general_unit= (TextView) v.findViewById(R.id.general_unit);
            editText= (MyEditText) v.findViewById(R.id.general_content);
            if(!trTask.getTaskstate().equals("1")&!trTask.getTaskstate().equals("2")){
                editText.setEnabled(false);
            }
        }
    }
}
