package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.domain.UserInfo;
import com.energyfuture.symphony.m3.util.Utils;

import java.util.List;

/**
 * 用户适配器
 */
public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {
    private Context context;
    public List<UserInfo> userInfoList;

    public UserInfoAdapter(Context context, List<UserInfo> userInfoList){
        this.context = context;
        this.userInfoList = userInfoList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final UserInfo userInfo = userInfoList.get(i);
        viewHolder.userName.setText(userInfo.getXm());
        boolean isChecked;
        if(Utils.radioButtonMap.size() > 0){
            if(Utils.radioButtonMap.containsKey(i)){
                isChecked = Utils.radioButtonMap.get(i);
                viewHolder.userName.setChecked(isChecked);
            }else{
                viewHolder.userName.setChecked(false);
            }
        }
        viewHolder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重置，确保最多只有一项被选中
                for (int key : Utils.radioButtonMap.keySet()) {
                    Utils.radioButtonMap.put(key, false);
                }
                Utils.radioButtonMap.put(i,true);
                UserInfoAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.radiobutton_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return userInfoList == null ? 0 : userInfoList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public RadioButton userName;
        public ViewHolder(View itemView) {
            super(itemView);
            userName= (RadioButton) itemView.findViewById(R.id.user_name);
        }

    }
}
