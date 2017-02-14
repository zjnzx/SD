package com.energyfuture.symphony.m3.adapter;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.energyfuture.symphony.m3.activity.ProjectListActivity;
import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.TrBureauDao;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.ui.MaterialDialog;
import com.energyfuture.symphony.m3.ui.RedMaterialDialog;
import com.energyfuture.symphony.m3.util.Constants;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public List<TrProject> applications;
    private int rowLayout;
    private Bitmap bitmap;
    private String workperson = "";
    private TextView none_data;
    private ProjectListActivity mAct;
    private TrStationDao trStationDao;
    private TrBureauDao trBureauDao;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    public ProjectListAdapter(List<TrProject> applications, int rowLayout, ProjectListActivity act,TextView none_data) {
        this.applications = applications;
        this.rowLayout = rowLayout;
        this.mAct = act;
        this.none_data=none_data;
        trStationDao = new TrStationDao(mAct);
        trBureauDao = new TrBureauDao(mAct);
    }

    public void clearApplications() {
        int size = this.applications.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                applications.remove(0);
            }

           this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(List<TrProject> applications) {

        this.applications.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

        @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        none_data.setVisibility(View.INVISIBLE);
        final TrProject trProject = applications.get(i);
        viewHolder.name.setText(trProject.getProjectname());

        TrStation trStation = new TrStation();
        trStation.setStationid(trProject.getStationid());
        List<TrStation> trStations = trStationDao.queryTrStationList(trStation);
        if(trStations.size() > 0){
            TrBureau trBureau = new TrBureau();
            trBureau.setBureauid(trStations.get(0).getBureauid());
            List<TrBureau> trBureaus = trBureauDao.queryTrBureauList(trBureau);
            viewHolder.id_voltage.setText(trStations.get(0).getVoltage() + "kV");
            if(trBureaus.size() > 0){
                viewHolder.id_bureau.setText(trBureaus.get(0).getBureauname());
            }
        }

        Department department = new Department();
        department.setBmid(trProject.getDepartment());
        TrDepartmentDao trDepartmentDao = new TrDepartmentDao(this.mAct);
        List<Department> departments = trDepartmentDao.queryDepartmentList(department);
        for (Department department1:departments){
            viewHolder.department.setText(department1.getBmmc());
        }
        TrTaskDao taskDao=new TrTaskDao(mAct);
        String workpersons = trProject.getWorkperson();
        if(workpersons != null && !workpersons.equals("")){
            workperson = workpersons.substring(0,workpersons.indexOf(";"));
        }
        int res= taskDao.taskStepCount(trProject.getProjectid());

        if(!Constants.getWorkicon(mAct,workperson).equals("")){
            String imagepath=Constants.getWorkicon(mAct,workperson);
            viewHolder.project_people.setImageURI(Uri.parse(imagepath));
        }else{
            int userhead= Constants.userHead(workperson);
            viewHolder.project_people.setImageResource(userhead);
        }

        /*int headUser= Constants.userHead(workperson);
        viewHolder.project_people.setImageResource(headUser);*/
//        File avatarFile = new File(pictureFILE_SAVEPATH + "head/USER/" + trProject.getZrpearson() + ".jpg");
//        try {
//            bitmap = Base.revitionImageSize(avatarFile, 50);
//        } catch (IOException e) {
//            e.printStackTrace();
//            try {
//                avatarFile = new File(pictureFILE_SAVEPATH + "head/USER/nh.jpg");
//                bitmap = Base.revitionImageSize(avatarFile, 50);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//        viewHolder.project_people.setImageBitmap(bitmap);
        //计划1,进行中2,已完成3
        if(trProject.getProjectstate()!=null)
        {
            if(trProject.getProjectstate().equals("1"))
            {
                viewHolder.id_tv_task.setText("未开始");
                viewHolder.image.setBackgroundResource(R.drawable.orange);
                viewHolder.bar.setVisibility(View.GONE);
                viewHolder.progressbar_text.setVisibility(View.GONE);
            }
            else if(trProject.getProjectstate().equals("3"))
            {
                viewHolder.id_tv_task.setText("进行中");
                viewHolder.image.setBackgroundResource(R.drawable.green);
                viewHolder.bar.setVisibility(View.VISIBLE);
                viewHolder.bar.setProgress(res);
                viewHolder.progressbar_text.setVisibility(View.VISIBLE);
                viewHolder.progressbar_text.setText(res+"%");
            }
            else if(trProject.getProjectstate().equals("4"))
            {
                viewHolder.id_tv_task.setText("已完成");
                viewHolder.image.setBackgroundResource(R.drawable.blue);
                viewHolder.bar.setVisibility(View.GONE);
                viewHolder.progressbar_text.setVisibility(View.GONE);
            }
        }

        String startDate = "",endDate = "";
        if(trProject.getStarttime().contains("-")){
            startDate = trProject.getStarttime().substring(0,10);
        }else{
            Long l1=new Long(trProject.getStarttime());
            startDate = Constants.dateformat1.format(l1);
        }
        if(trProject.getEndtime().contains("-")){
            endDate = trProject.getEndtime().substring(0,10);
        }else{
            Long l1=new Long(trProject.getEndtime());
            endDate = Constants.dateformat1.format(l1);
        }

        trProject.setStarttime(startDate);
        trProject.setEndtime(endDate);

        viewHolder.tv_date.setText(startDate+"/"+endDate);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.animateActivity(trProject);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final MaterialDialog mMaterialDialog = new MaterialDialog(mAct);
                mMaterialDialog.setTitle("项目删除")
                        .setMessage(trProject.getProjectname())
                        .setPositiveButton("本地删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final RedMaterialDialog dialog = new RedMaterialDialog(mAct);
                                dialog.setTitle("确认从本地删除该项目吗？")
                                        .setMessage("本地删除可从平台下载进行恢复。")
                                        .setPositiveButton("删除", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mAct.deletePro(trProject,i);
                                                dialog.dismiss();
                                                mMaterialDialog.dismiss();
                                            }
                                        }).setNegativeButton("取消",new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                }).setCanceledOnTouchOutside(false).show();
                            }
                        }).setChangeButton("平台删除", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(trProject.getProjectstate().equals("1")){
                                    final RedMaterialDialog dialog = new RedMaterialDialog(mAct);
                                    dialog.setTitle("确认从平台删除该项目吗？")
                                            .setMessage("平台删除项目不可恢复，请确认要执行该操作。")
                                            .setPositiveButton("删除", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mAct.deleteProWeb(trProject, i);
                                                    dialog.dismiss();
                                                    mMaterialDialog.dismiss();
                                                }
                                            }).setNegativeButton("取消",new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    }).setCanceledOnTouchOutside(false).show();
                                }else{
                                    Toast.makeText(mAct, "只能删除未开始的项目", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                }).setCanceledOnTouchOutside(false).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name,tv_date,id_tv_task,progressbar_text,tv_content,department,id_bureau,id_voltage;
        public ImageView image,img_date,project_people;
        private ProgressBar bar;
        private FrameLayout progressBar_FrameLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.countryName);
            image = (ImageView) itemView.findViewById(R.id.countryImage);
            tv_date = (TextView) itemView.findViewById(R.id.id_tv_date);
            id_bureau = (TextView) itemView.findViewById(R.id.id_bureau);
            id_voltage = (TextView) itemView.findViewById(R.id.id_voltage);
            department = (TextView)itemView.findViewById(R.id.group);
//          project_grade= (ImageView) itemView.findViewById(R.id.project_grade);
//          reason_info= (TextView) itemView.findViewById(R.id.reason_info);
//            img_date = (ImageView) itemView.findViewById(R.id.id_img_date);
            id_tv_task = (TextView)itemView.findViewById(R.id.id_tv_task);
            tv_content = (TextView)itemView.findViewById(R.id.id_tv_content);
            project_people = (ImageView)itemView.findViewById(R.id.project_people);
            bar = (ProgressBar)itemView.findViewById(R.id.id_app_progressBar);
            progressbar_text=(TextView) itemView.findViewById(R.id.progressbar_text);
            progressBar_FrameLayout=(FrameLayout) itemView.findViewById(R.id.progressBar_FrameLayout);
        }

    }
}