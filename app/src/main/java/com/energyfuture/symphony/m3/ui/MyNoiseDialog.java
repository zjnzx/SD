package com.energyfuture.symphony.m3.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.adapter.NoiseAdapter;
import com.energyfuture.symphony.m3.dao.TrNoiseRecordValueDao;
import com.energyfuture.symphony.m3.domain.TrNoiseRecordGroup;
import com.energyfuture.symphony.m3.domain.TrNoiseRecordValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MyNoiseDialog extends Dialog {
    private GridViewforListView noise_gridview;
    private Context context;
    private String distance;
    private TrNoiseRecordGroup trNoiseRecordGroup;
    private TextView noise_back;

    public MyNoiseDialog(Context context,String distance,TrNoiseRecordGroup trNoiseRecordGroup) {
        super(context);
        this.context = context;
        this.distance = distance;
        this.trNoiseRecordGroup = trNoiseRecordGroup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.setCancelable(false);// 设置点击屏幕Dialog不消失
        setContentView(R.layout.noise_dialog);
        setTitle(distance + "(米)噪声测试值");
        noise_gridview = (GridViewforListView)findViewById(R.id.noise_gridview);
        noise_back = (TextView)findViewById(R.id.noise_back);

        List<TrNoiseRecordValue> trNoiseRecordValueList = new ArrayList<TrNoiseRecordValue>();
        TrNoiseRecordValue trNoiseRecordValue = new TrNoiseRecordValue();
        trNoiseRecordValue.setGroupid(trNoiseRecordGroup.getId());
        TrNoiseRecordValueDao trNoiseRecordValueDao = new TrNoiseRecordValueDao(context);
        trNoiseRecordValueList = trNoiseRecordValueDao.queryTrNoiseRecordValueList(trNoiseRecordValue);

        NoiseAdapter noiseAdapter = new NoiseAdapter(context,trNoiseRecordValueList);
        noise_gridview.setAdapter(noiseAdapter);
        noiseAdapter.notifyDataSetChanged();
        MyNoiseDialog.this.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            noise_back.setFocusable(true);
            noise_back.setFocusableInTouchMode(true);
            noise_back.requestFocus();
            noise_back.requestFocusFromTouch();
            MyNoiseDialog.this.dismiss();
        }
        return true;
    }
}
