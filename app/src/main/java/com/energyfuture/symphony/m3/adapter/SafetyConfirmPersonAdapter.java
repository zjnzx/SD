package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.List;
import java.util.Map;

public class SafetyConfirmPersonAdapter extends RecyclerView.Adapter<SafetyConfirmPersonAdapter.ViewHolder> {
    private Context context;
    private List<String> confirmperson;
    private int flag ;

    public SafetyConfirmPersonAdapter(Context context, List<String> confirmperson,int flag){
        this.context = context;
        this.flag = flag;
        this.confirmperson = confirmperson;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final String persionId = confirmperson.get(i);
        List<String> nameList = Constants.getUserNameById(persionId,context);
        if(nameList != null && nameList.size() > 0){
            viewHolder.checkBox.setText(nameList.get(0));
        }else{
            viewHolder.checkBox.setText("");
        }

        viewHolder.personId.setText(persionId);
        if(flag == 1){//checkbox选中并且不可用
            viewHolder.checkBox.setChecked(true);
            viewHolder.checkBox.setEnabled(false);
        }
        //如果选中下次进入会保持选中
        if(Utils.personMap.size() > 0){
            for(Map.Entry<String, Boolean> entry: Utils.personMap.entrySet()){
                if(entry.getKey().equals(persionId) && entry.getValue() == true){
                    viewHolder.checkBox.setChecked(true);
                }
            }
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.personMap.put(persionId,isChecked);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.safe_list_person_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return confirmperson == null ? 0 : confirmperson.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        private TextView personId;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox= (CheckBox) itemView.findViewById(R.id.confirm_checkbox);
            personId= (TextView) itemView.findViewById(R.id.person_id);
        }

    }
}
