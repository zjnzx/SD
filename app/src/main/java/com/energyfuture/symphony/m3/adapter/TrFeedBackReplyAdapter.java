package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.TrFeedBackReplyActivity;
import com.energyfuture.symphony.m3.dao.TrFeedBackAccessoryDao;
import com.energyfuture.symphony.m3.domain.TrFeedBackAccessory;
import com.energyfuture.symphony.m3.domain.TrFeedBackReply;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TrFeedBackReplyAdapter extends BaseAdapter {
    private TrFeedBackReplyActivity mAct;
    public List<TrFeedBackReply> list=null;
    private String picUrl,opicUrl,themeId;
    private TrFeedBackAccessoryDao trFeedBackAccessoryDao;
    public TrFeedBackReplyAdapter(List<TrFeedBackReply> list, Context context, String picUrl, String opicUrl,String themeId) {
        this.list = list;
        this.picUrl = picUrl;
        this.opicUrl = opicUrl;
        this.themeId = themeId;
        this.mAct = (TrFeedBackReplyActivity)context;
        trFeedBackAccessoryDao = new TrFeedBackAccessoryDao(mAct);
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
        final TrFeedBackReply reply = list.get(position);
        ViewHolder holder;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            convertView= LayoutInflater.from(mAct).inflate(R.layout.feedback_reply_item, null);
            holder = new ViewHolder(convertView);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<String>  urlList = new ArrayList<String>();
        TrFeedBackAccessory trFeedBackAccessory = new TrFeedBackAccessory();
        trFeedBackAccessory.setReplyid(reply.getId());
        //查询图片的url
        List<TrFeedBackAccessory>  accList = trFeedBackAccessoryDao.queryTrFeedBackAccessoryList(trFeedBackAccessory);
        for(TrFeedBackAccessory accessory : accList){
            urlList.add(accessory.getFileurl()+"small/"+accessory.getFilename());
        }
        //设置图片显示适配器
        TrFeedBackSendReplyPicAdapter picAdapter = new TrFeedBackSendReplyPicAdapter(mAct,urlList,reply.getId(),picUrl,opicUrl,themeId);
        holder.reply_item_gridview.setAdapter(picAdapter);
        picAdapter.notifyDataSetChanged();

//        holder.reply_item_gridview.setClickable(false);
//        holder.reply_item_gridview.setPressed(false);
//        holder.reply_item_gridview.setEnabled(false);

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
        holder.reply_content.setText(reply.getReplycontent());

        int headUser = 0;
        if (reply.getReplypersonid()!=null){
            headUser=  Constants.userHead(reply.getReplypersonid());
        }
        holder.reply_person_img.setImageResource(headUser);

        return convertView;
    }
    public class ViewHolder {
        public TextView reply_person,reply_time,reply_content,reply_person_name,reply_order;
        public ImageView reply_person_img;
        public GridView reply_item_gridview;
        public ViewHolder(View view){
            reply_person_img = (ImageView) view.findViewById(R.id.feedback_person_img);
            reply_person= (TextView) view.findViewById(R.id.feedback_person);
            reply_time= (TextView) view.findViewById(R.id.feedback_time);
            reply_content= (TextView) view.findViewById(R.id.feedback_content);
            reply_item_gridview= (GridView) view.findViewById(R.id.feedback_item_gridview);
            reply_order= (TextView) view.findViewById(R.id.feedback_order);
        }
    }

}
