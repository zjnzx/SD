package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.CommunionThemeReplyActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympCommunicationAccessoryDao;
import com.energyfuture.symphony.m3.domain.SympCommunicationAccessory;
import com.energyfuture.symphony.m3.domain.SympCommunicationReply;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class CommunionThemeReplyAdapter extends BaseAdapter {
    private CommunionThemeReplyActivity mAct;
    public List<SympCommunicationReply> list=null;
    private String picUrl,opicUrl,themeId;
    public CommunionThemeReplyAdapter(List<SympCommunicationReply> list, Context context,String picUrl,String opicUrl,String themeId) {
        this.list = list;
        this.picUrl = picUrl;
        this.opicUrl = opicUrl;
        this.themeId = themeId;
        this.mAct = (CommunionThemeReplyActivity)context;
    }

    @Override
    public int getCount() {
//        int count = 0;
//        if(list.size() > 10){
//            if(Utils.isMore){
//                count = list.size();
//            }else{
//                count = 10;
//            }
//        }else{
//            count = list.size();
//        }
//        return count;
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SympCommunicationReply reply = list.get(position);
        ViewHolder holder;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            convertView= LayoutInflater.from(mAct).inflate(R.layout.communion_theme_reply_item, null);
            holder = new ViewHolder(convertView);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        SympCommunicationAccessoryDao sympCommunicationAccessoryDao = new SympCommunicationAccessoryDao(mAct);
        List<String>  urlList = new ArrayList<String>();
        SympCommunicationAccessory sympCommunicationAccessory = new SympCommunicationAccessory();
        sympCommunicationAccessory.setReplyid(reply.getId());
        //查询图片的url
        List<SympCommunicationAccessory>  accList = sympCommunicationAccessoryDao.querySympCommunicationAccessoryList(sympCommunicationAccessory);
        for(SympCommunicationAccessory accessory : accList){
            urlList.add(accessory.getFileurl()+"small/"+accessory.getFilename());
        }
        //设置图片显示适配器
        CommunionSendReplyPicAdapter picAdapter = new CommunionSendReplyPicAdapter(mAct,urlList,reply.getId(),picUrl,opicUrl,themeId);
        holder.reply_item_gridview.setAdapter(picAdapter);
        picAdapter.notifyDataSetChanged();

        holder.reply_item_gridview.setClickable(false);
        holder.reply_item_gridview.setPressed(false);
        holder.reply_item_gridview.setEnabled(false);

        //根据用户id查用户姓名
        final List<String> userList = Constants.getUserNameById(reply.getReplypersonid(), mAct);
        if(userList.size() > 0){
            holder.reply_person.setText(userList.get(0));
        }
        String replyTime = reply.getReplytime();
        if(!reply.getReplytime().contains("-")){
            Long timestamp = Long.parseLong(reply.getReplytime());
            replyTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        }

        holder.reply_time.setText(replyTime);
        holder.reply_order.setText("#"+(position+1));
        String replyContent = reply.getReplycontent();

        //如果包含@
        if(replyContent.startsWith("@") && (reply.getReplyobjectid() == null || reply.getReplyobjectid().equals("") || reply.getReplyobjectid().equals("null"))){
            String call = replyContent.substring(0,replyContent.indexOf(":") + 1);
            String content = replyContent.substring(replyContent.indexOf(":") + 1);
            if(("").equals(content)){
                holder.reply_content_layout.setVisibility(View.GONE);
            }else{
                holder.reply_call2.setVisibility(View.GONE);
                holder.reply_content2.setVisibility(View.GONE);
                holder.reply_content.setText(content);
            }
            holder.reply_person_name.setVisibility(View.GONE);
            holder.reply_object_name.setVisibility(View.GONE);
            holder.reply_text.setVisibility(View.GONE);
            holder.reply_call.setVisibility(View.VISIBLE);
            holder.reply_call.setText(call);
        } else if(replyContent.contains("@") && (reply.getReplyobjectid() == null || reply.getReplyobjectid().equals("") || reply.getReplyobjectid().equals("null"))){
            String str1 = replyContent.substring(replyContent.indexOf("@"));
            String call = str1.substring(0,str1.indexOf(" "));
            //@之前的内容
            String frontContent = replyContent.substring(0,replyContent.indexOf("@") - 1);
            //@之后的内容
            String backContent = str1.substring(str1.indexOf(" "));
            holder.reply_call.setVisibility(View.GONE);
            holder.reply_call2.setVisibility(View.VISIBLE);
            holder.reply_content2.setVisibility(View.VISIBLE);
            holder.reply_call2.setText(call);
            holder.reply_content.setText(frontContent + " ");
            holder.reply_content2.setText(backContent);
            holder.reply_person_name.setVisibility(View.GONE);
            holder.reply_object_name.setVisibility(View.GONE);
            holder.reply_text.setVisibility(View.GONE);
        }else{
            holder.reply_call.setVisibility(View.GONE);
            //回复某一个人
            if(reply.getReplyobjectid() != null && !reply.getReplyobjectid().equals("null") && !reply.getReplyobjectid().equals("")){
                if(replyContent.startsWith("@")){
                    String call = replyContent.substring(0,replyContent.indexOf(":") + 1);
                    String content = replyContent.substring(replyContent.indexOf(":") + 1);
                    if(("").equals(content)){
                        holder.reply_content_layout.setVisibility(View.GONE);
                    }
                    holder.reply_call2.setVisibility(View.GONE);
                    holder.reply_content2.setVisibility(View.GONE);
                    holder.reply_content.setText(content);
                    holder.reply_call.setVisibility(View.VISIBLE);
                    holder.reply_call.setText(call);
                }else if(replyContent.contains("@")){
                    String str1 = replyContent.substring(replyContent.indexOf("@"));
                    String call = str1.substring(0,str1.indexOf(" "));
                    //@之前的内容
                    String frontContent = replyContent.substring(0,replyContent.indexOf("@") - 1);
                    //@之后的内容
                    String backContent = str1.substring(str1.indexOf(" "));
                    holder.reply_call.setVisibility(View.GONE);
                    holder.reply_call2.setVisibility(View.VISIBLE);
                    holder.reply_content2.setVisibility(View.VISIBLE);
                    holder.reply_call2.setText(call);
                    holder.reply_content.setText(frontContent + " ");
                    holder.reply_content2.setText(backContent);
                    holder.reply_person_name.setVisibility(View.GONE);
                    holder.reply_object_name.setVisibility(View.GONE);
                    holder.reply_text.setVisibility(View.GONE);
                }else{
                    holder.reply_call2.setVisibility(View.GONE);
                    holder.reply_content2.setVisibility(View.GONE);
                    holder.reply_content.setText(replyContent);
                }
                List<String> users = Constants.getUserNameById(reply.getReplyobjectid(), mAct);
                holder.reply_person_name.setVisibility(View.VISIBLE);
                holder.reply_object_name.setVisibility(View.VISIBLE);
                holder.reply_text.setVisibility(View.VISIBLE);
                holder.reply_person_name.setText(userList.get(0));
                holder.reply_object_name.setText(users.get(0)+":");
            }else {
                holder.reply_person_name.setVisibility(View.GONE);
                holder.reply_object_name.setVisibility(View.GONE);
                holder.reply_text.setVisibility(View.GONE);
                holder.reply_call2.setVisibility(View.GONE);
                holder.reply_content2.setVisibility(View.GONE);
                holder.reply_content.setText(replyContent);
            }
        }

        int headUser = 0;
        if (reply.getReplypersonid()!=null){
            headUser=  Constants.userHead(reply.getReplypersonid());
        }
        holder.reply_person_img.setImageResource(headUser);
        reply.setPersonname(userList.get(0));
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发表回复
                mAct.replyForPerson(reply);
            }
        });

        return convertView;
    }
    public class ViewHolder {
        public TextView reply_person,reply_time,reply_content,reply_call,reply_object_name,reply_person_name,reply_text,reply_order,reply_call2,reply_content2;
        public ImageView reply_person_img,reply;
        public GridView reply_item_gridview;
        public LinearLayout reply_content_layout;
        public ViewHolder(View view){
            reply_person_img = (ImageView) view.findViewById(R.id.reply_person_img);
            reply = (ImageView) view.findViewById(R.id.reply);
            reply_person= (TextView) view.findViewById(R.id.reply_person);
            reply_time= (TextView) view.findViewById(R.id.reply_time);
            reply_content= (TextView) view.findViewById(R.id.reply_content);
            reply_content2= (TextView) view.findViewById(R.id.reply_content2);
            reply_call= (TextView) view.findViewById(R.id.reply_call);
            reply_call2= (TextView) view.findViewById(R.id.reply_call2);
            reply_item_gridview= (GridView) view.findViewById(R.id.reply_item_gridview);
            reply_object_name= (TextView) view.findViewById(R.id.reply_object_name);
            reply_person_name= (TextView) view.findViewById(R.id.reply_person_name);
            reply_text= (TextView) view.findViewById(R.id.reply_text);
            reply_order= (TextView) view.findViewById(R.id.reply_order);
            reply_content_layout= (LinearLayout) view.findViewById(R.id.reply_content_layout);
        }
    }

}
