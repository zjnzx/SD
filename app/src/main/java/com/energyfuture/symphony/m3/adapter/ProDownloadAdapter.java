package com.energyfuture.symphony.m3.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.TrBureauDao;
import com.energyfuture.symphony.m3.dao.TrDepartmentDao;
import com.energyfuture.symphony.m3.dao.TrStationDao;
import com.energyfuture.symphony.m3.dao.TrTaskDao;
import com.energyfuture.symphony.m3.domain.Department;
import com.energyfuture.symphony.m3.domain.TrBureau;
import com.energyfuture.symphony.m3.domain.TrProject;
import com.energyfuture.symphony.m3.domain.TrStation;
import com.energyfuture.symphony.m3.util.Constants;
import com.energyfuture.symphony.m3.util.ProDownloadDialog;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * 设备适配器
 */
public class ProDownloadAdapter extends BaseAdapter{
    public SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public List<TrProject> trProjectList;
    private String workperson = "";
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    //  用来保存项目ID
//    private
    private ProDownloadDialog mAct;
    private Context context;
    private TrStationDao trStationDao;
    private TrBureauDao trBureauDao;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    public ProDownloadAdapter(List<TrProject> trProjectList, ProDownloadDialog act) {
        this.trProjectList = trProjectList;
        this.mAct = act;
        isSelected = new HashMap<Integer, Boolean>();
        trStationDao = new TrStationDao(mAct.getContext());
        trBureauDao = new TrBureauDao(mAct.getContext());
        // 初始化数据
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < trProjectList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public int getCount() {
        return trProjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return trProjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TrProject trProject = trProjectList.get(position);
        final MyViewholder myViewholder;
        if(convertView==null){
            convertView= LayoutInflater.from(mAct.getContext()).inflate(R.layout.pro_download_item,null);
            myViewholder=new MyViewholder(convertView);
            convertView.setTag(myViewholder);
        }else{
            myViewholder= (MyViewholder) convertView.getTag();
        }
        myViewholder.name.setText(trProject.getProjectname());

        TrStation trStation = new TrStation();
        trStation.setStationid(trProject.getStationid());
        List<TrStation> trStations = trStationDao.queryTrStationList(trStation);
        if(trStations.size() > 0){
            TrBureau trBureau = new TrBureau();
            trBureau.setBureauid(trStations.get(0).getBureauid());
            List<TrBureau> trBureaus = trBureauDao.queryTrBureauList(trBureau);
            myViewholder.id_voltage.setText(trStations.get(0).getVoltage() + "kV");
            if(trBureaus.size() > 0){
                myViewholder.id_bureau.setText(trBureaus.get(0).getBureauname());
            }
        }

        Department department = new Department();
        department.setBmid(trProject.getDepartment());
        TrDepartmentDao trDepartmentDao = new TrDepartmentDao(this.mAct.getContext());
        List<Department> departments = trDepartmentDao.queryDepartmentList(department);
        for (Department department1:departments){
            myViewholder.department.setText(department1.getBmmc());
        }
        TrTaskDao taskDao=new TrTaskDao(mAct.getContext());
        String workpersons = trProject.getWorkperson();
        if(workpersons != null && !workpersons.equals("")){
            workperson = workpersons.substring(0,workpersons.indexOf(";"));
        }
        int res= taskDao.taskStepCount(trProject.getProjectid());
        int headUser= Constants.userHead(workperson);
        myViewholder.project_people.setImageResource(headUser);
        //计划1,进行中2,已完成3
        if(trProject.getProjectstate()!=null)
        {
            if(trProject.getProjectstate().equals("1"))
            {
                myViewholder.id_tv_task.setText("未开始");
//                myViewholder.image.setBackgroundResource(R.drawable.orange);
                myViewholder.bar.setVisibility(View.GONE);
                myViewholder.progressbar_text.setVisibility(View.GONE);
            }
            else if(trProject.getProjectstate().equals("3"))
            {
                myViewholder.id_tv_task.setText("进行中");
//                myViewholder.image.setBackgroundResource(R.drawable.green);
                myViewholder.bar.setVisibility(View.VISIBLE);
                myViewholder.bar.setProgress(res);
                myViewholder.progressbar_text.setVisibility(View.VISIBLE);
                myViewholder.progressbar_text.setText(res+"%");
            }
            else if(trProject.getProjectstate().equals("4"))
            {
                myViewholder.id_tv_task.setText("已完成");
//                myViewholder.image.setBackgroundResource(R.drawable.blue);
                myViewholder.bar.setVisibility(View.GONE);
                myViewholder.progressbar_text.setVisibility(View.GONE);
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

        myViewholder.tv_date.setText(startDate + "/" + endDate);

        // 监听checkBox并根据原来的状态来设置新的状态
        myViewholder.ispassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean flag = false;
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
                for(int i = 0 ; i < isSelected.size() ; i++){
                    if(isSelected.get(i)){
                        mAct.download.setEnabled(true);
                        mAct.download.setBackgroundResource(R.drawable.affirm_button_sel);
                        flag = true;
                    }
                }
                if(flag == false){
                    mAct.download.setEnabled(false);
                    mAct.download.setBackgroundResource(R.drawable.project_button_gray);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (isSelected.get(position)) {
                    myViewholder.ispassword.setChecked(false);
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    myViewholder.ispassword.setChecked(true);
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
                for(int i = 0 ; i < isSelected.size() ; i++){
                    if(isSelected.get(i)){
                        mAct.download.setEnabled(true);
                        mAct.download.setBackgroundResource(R.drawable.affirm_button_sel);
                        flag = true;
                    }
                }
                if(flag == false){
                    mAct.download.setEnabled(false);
                    mAct.download.setBackgroundResource(R.drawable.project_button_gray);
                }
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        myViewholder.ispassword.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ProDownloadAdapter.isSelected = isSelected;
    }


    public class MyViewholder{
        public TextView name,tv_date,id_tv_task,progressbar_text,tv_content,department,id_bureau,id_voltage;
        public ImageView image,img_date,project_people;
        private ProgressBar bar;
        private CheckBox ispassword;
        private FrameLayout progressBar_FrameLayout;

        public MyViewholder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.countryName);
//            image = (ImageView) itemView.findViewById(R.id.countryImage);
            tv_date = (TextView) itemView.findViewById(R.id.id_tv_date);
            department = (TextView)itemView.findViewById(R.id.group);
            id_tv_task = (TextView)itemView.findViewById(R.id.id_tv_task);
            tv_content = (TextView)itemView.findViewById(R.id.id_tv_content);
            id_bureau = (TextView)itemView.findViewById(R.id.id_bureau);
            id_voltage = (TextView)itemView.findViewById(R.id.id_voltage);
            project_people = (ImageView)itemView.findViewById(R.id.project_people);
            bar = (ProgressBar)itemView.findViewById(R.id.id_app_progressBar);
            progressbar_text=(TextView) itemView.findViewById(R.id.progressbar_text);
            progressBar_FrameLayout=(FrameLayout) itemView.findViewById(R.id.progressBar_FrameLayout);
            ispassword = (CheckBox)itemView.findViewById(R.id.ispassword);
        }
    }
}
