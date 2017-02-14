package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.TrEquipment;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.List;

/**
 * 巡检设备适配器
 */
public class EquipmentAdapter extends BaseAdapter {
    private Context context;
    public List<TrEquipment> trEquipmentList;

    public EquipmentAdapter(Context context, List<TrEquipment> trEquipmentList){
        this.trEquipmentList = trEquipmentList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return trEquipmentList == null ? 0 : trEquipmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myviewholder myviewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.checkbox_item, null);
            myviewholder=new Myviewholder(convertView);
            convertView.setTag(myviewholder);
        }else{
            myviewholder= (Myviewholder) convertView.getTag();
        }
        final TrEquipment trEquipment = trEquipmentList.get(position);
        myviewholder.equipmentName.setText(trEquipment.getEquipmentname());

        myviewholder.equipmentName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.equipmentMap.put(trEquipment.getEquipmentid(),isChecked);
                }else{
                    Utils.equipmentMap.remove(trEquipment.getEquipmentid());
                }
            }
        });
        return convertView;
    }

    class Myviewholder{
        public CheckBox equipmentName;

        Myviewholder(View v) {
            equipmentName= (CheckBox) v.findViewById(R.id.equipment_name);
        }
    }
}
