package com.energyfuture.symphony.m3.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.activity.TaskListActivity;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.ui.RedMaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.Viewholder>{
    public List<TrTask> taskList;
    private int rowlayout;
    private TaskListActivity context;

    public TaskListAdapter(List<TrTask> taskList, int rowlayout, TaskListActivity context) {
        this.taskList = taskList;
        this.rowlayout = rowlayout;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowlayout, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        final TrTask taskinfo = taskList.get(position);
        holder.name.setText(taskinfo.getTaskname());
        setState(taskinfo, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.animateActivity(taskinfo);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!taskinfo.getTaskname().equals("站内巡检")){
                    final MaterialDialog mMaterialDialog = new MaterialDialog(context);
                    mMaterialDialog.setTitle("任务删除")
                            .setMessage(taskinfo.getTaskname())
                            .setPositiveButton("平台删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (taskinfo.getTaskstate().equals("1")) {
                                        final RedMaterialDialog dialog = new RedMaterialDialog(context);
                                        dialog.setTitle("确认从平台删除该任务吗？")
                                                .setMessage("平台删除任务不可恢复，请确认要执行该操作。")
                                                .setPositiveButton("删除", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        context.deleteTask(taskinfo, position);
                                                        dialog.dismiss();
                                                        mMaterialDialog.dismiss();
                                                    }
                                                }).setNegativeButton("取消",new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        }).setCanceledOnTouchOutside(false).show();
                                    } else {
                                        Toast.makeText(context, "只能删除未开始的任务", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    }).setCanceledOnTouchOutside(false).show();
                }else{
                    Toast.makeText(context,"站内巡检不能删除",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    private void setState(TrTask task,Viewholder holder) {
        if(task.getTaskstate().equals("1")){
            holder.frameLayout.setVisibility(View.GONE);
            holder.task_image.setImageResource(R.drawable.orange);
            holder.state.setText("未开始");
        }else if(task.getTaskstate().equals("2")){
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.task_image.setImageResource(R.drawable.task_state2);
            holder.state.setText("进行中");
        }else if(task.getTaskstate().equals("3")){
            holder.frameLayout.setVisibility(View.GONE);
            holder.task_image.setImageResource(R.drawable.green);
            holder.state.setText("待补录");
        }else if(task.getTaskstate().equals("4")){
            holder.frameLayout.setVisibility(View.GONE);
            holder.task_image.setImageResource(R.drawable.task_state4);
            holder.state.setText("待总结");
        }else if(task.getTaskstate().equals("5")){
            holder.frameLayout.setVisibility(View.GONE);
            holder.task_image.setImageResource(R.drawable.task_state5);
            holder.state.setText("待审核");
        }else if(task.getTaskstate().equals("6")){
            holder.frameLayout.setVisibility(View.GONE);
            holder.task_image.setImageResource(R.drawable.task_state6);
            holder.state.setText("已完成");
        }
        //设置人员头像
        if(!("1").equals(task.getTaskstate())){
            String persons = task.getWorkperson();
            Log.e("--------------------------------",persons);
            String[] per = persons.split(";");
            ImageView[] imageViews = {holder.task_person1,holder.task_person2,holder.task_person3,holder.task_person4,holder.task_person5,holder.task_person6,holder.task_person7};
            if(per.length > 0){
                if(per.length > 7){
                    for(int i = 6;i >= 0;i--){
                        //imageViews[i].setImageResource(getImage(per[i]));

                        if(!Constants.getWorkicon(context,per[i]).equals("")){
                            String imagepath=Constants.getWorkicon(context,per[i]);
                            imageViews[i].setImageURI(Uri.parse(imagepath));
                        }else{
                            int userhead= Constants.userHead(per[i]);
                            imageViews[i].setImageResource(userhead);
                        }
                    }
                }else{
                    for(int i = per.length-1;i >= 0;i--){
                        //imageViews[i].setImageResource(getImage(per[i]));

                        if(!Constants.getWorkicon(context,per[i]).equals("")){
                            String imagepath=Constants.getWorkicon(context,per[i]);
                            imageViews[i].setImageURI(Uri.parse(imagepath));
                        }else{
                            int userhead= Constants.userHead(per[i]);
                            imageViews[i].setImageResource(userhead);
                        }
                    }
                }
            }
        }
    }

    //获取人员头像
    private int getImage(String taskperson) {
        int image=R.drawable.zhaogr;
        if (taskperson!=null){
            image= Constants.userHead(taskperson);
        }
        return image;
    }

    public void addTask(List<TrTask> list){

        this.taskList.addAll(list);
        notifyItemRangeChanged(0, list.size() - 1);
    }

    public void clearTask(){
        int size = taskList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                taskList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    class Viewholder extends RecyclerView.ViewHolder{
        public TextView name,state;
        public ImageView task_person1,task_image,task_person2,task_person3,task_person4,task_person5,task_person6,task_person7;
        public FrameLayout frameLayout;
        public Viewholder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.task_name);
            state= (TextView) itemView.findViewById(R.id.task_state);
            task_person1= (ImageView) itemView.findViewById(R.id.task_person1);
            task_person2= (ImageView) itemView.findViewById(R.id.task_person2);
            task_person3= (ImageView) itemView.findViewById(R.id.task_person3);
            task_person4= (ImageView) itemView.findViewById(R.id.task_person4);
            task_person5= (ImageView) itemView.findViewById(R.id.task_person5);
            task_person6= (ImageView) itemView.findViewById(R.id.task_person6);
            task_person7= (ImageView) itemView.findViewById(R.id.task_person7);
            task_image= (ImageView) itemView.findViewById(R.id.task_image);
           frameLayout= (FrameLayout) itemView.findViewById(R.id.layout_rate);
        }
    }
}
