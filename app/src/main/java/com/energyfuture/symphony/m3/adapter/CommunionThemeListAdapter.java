package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.CommunionThemeListActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympCommunicationPromptDao;
import com.energyfuture.symphony.m3.dao.SympCommunicationReplyDao;
import com.energyfuture.symphony.m3.domain.SympCommunicationPrompt;
import com.energyfuture.symphony.m3.domain.SympCommunicationTheme;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.List;
import java.util.Map;

public class CommunionThemeListAdapter extends RecyclerView.Adapter<CommunionThemeListAdapter.ViewHolder> {
    private int rowLayout;
    private String launchPosition;
    public List<SympCommunicationTheme> list;
    private CommunionThemeListActivity mAct;
    private SympCommunicationReplyDao sympCommunicationReplyDao = new SympCommunicationReplyDao(mAct);
    private SympCommunicationPromptDao ympCommunicationPromptDao = new SympCommunicationPromptDao(mAct);
    public CommunionThemeListAdapter(List<SympCommunicationTheme> list, int rowLayout, Context context,String launchPosition) {
        this.rowLayout = rowLayout;
        this.list = list;
        this.launchPosition = launchPosition;
        mAct = (CommunionThemeListActivity) context;
    }

    public void clearList() {
        int size = this.list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
    public void addToList(List<SympCommunicationTheme> list) {
        this.list.addAll(list);
        this.notifyItemRangeInserted(0, list.size() - 1);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final SympCommunicationTheme theme = list.get(i);

        //根据用户id查用户姓名
        List<String> userList = Constants.getUserNameById(theme.getCreatepersonid(), mAct);
        //查询总回复数量
        String replyCount = sympCommunicationReplyDao.querySympCommunicationReplyCount(theme.getId(),"");
        //查询未读回复数量
        String unReadCount = sympCommunicationReplyDao.querySympCommunicationReplyCount(theme.getId(), "0");
        //查询是否有人@
        SympCommunicationPrompt sympCommunicationPrompt = new SympCommunicationPrompt();
        sympCommunicationPrompt.setThemeid(theme.getId());
        List<String> promptList = ympCommunicationPromptDao.querySympCommunicationPrompt(sympCommunicationPrompt);
        if(promptList != null && promptList.size() > 0){//有人@
            for(int j = 0;j < promptList.size();j++){
                String replyContent = promptList.get(j);
                if(replyContent.contains(userList.get(0))){ //如果包含主题创建人
                    viewHolder.theme_call.setVisibility(View.VISIBLE);
                    break;
                }else{//不包含主题创建人
                    viewHolder.theme_call.setVisibility(View.GONE);
                }
            }
        }else{
            viewHolder.theme_call.setVisibility(View.GONE);
        }
        String createTime = theme.getCreatetime();
        if(!theme.getCreatetime().contains("-")){
            Long timestamp = Long.parseLong(theme.getCreatetime());
            createTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        }

        viewHolder.theme_position.setText(launchPosition);
        viewHolder.theme_title.setText(theme.getThemetitle());
        viewHolder.theme_person.setText(userList.get(0));
        viewHolder.theme_time.setText(createTime);
        viewHolder.theme_reply_count.setText(replyCount+"条讨论信息");
        if(unReadCount != null && !unReadCount.equals("") && Integer.parseInt(unReadCount) > 0){
            viewHolder.theme_unread_count.setText("[" + unReadCount + "条未读]");
        }else{
            viewHolder.theme_unread_count.setVisibility(View.GONE);
        }
        int headUser = 0;
        if (theme.getCreatepersonid()!=null){
             headUser=  Constants.userHead(theme.getCreatepersonid());
        }
        viewHolder.theme_person_img.setImageResource(headUser);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAct.toActivity(theme);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Map<String,String> map= Constants.getLoginPerson(mAct);
               String userId=map.get("userId");
                if(userId.equals(theme.getCreatepersonid())){
                    mAct.toDeleteThemeAndReply(theme,i);
                }else{
                    Toast.makeText(mAct, "你不是该主贴创建人，无权删除！", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView theme_person_img;
        private TextView theme_person,theme_position,theme_title,theme_time,theme_reply_count,theme_unread_count,theme_call;
        public ViewHolder(View itemView) {
            super(itemView);
            theme_person_img=(ImageView) itemView.findViewById(R.id.theme_person_img);
            theme_person=(TextView) itemView.findViewById(R.id.theme_person);
            theme_position=(TextView) itemView.findViewById(R.id.theme_position);
            theme_title=(TextView) itemView.findViewById(R.id.theme_title);
            theme_time=(TextView) itemView.findViewById(R.id.theme_time);
            theme_reply_count=(TextView) itemView.findViewById(R.id.theme_reply_count);
            theme_unread_count=(TextView) itemView.findViewById(R.id.theme_unread_count);
            theme_call=(TextView) itemView.findViewById(R.id.theme_call);

        }

    }
}
