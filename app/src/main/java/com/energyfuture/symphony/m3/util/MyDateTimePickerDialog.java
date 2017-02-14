package com.energyfuture.symphony.m3.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrSpecialWorkDao;
import com.energyfuture.symphony.m3.domain.TrSpecialWork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期时间选择控件 
 * @author 大漠
 */
public class MyDateTimePickerDialog implements  OnDateChangedListener,OnTimeChangedListener{
 private DatePicker datePicker;
 private TimePicker timePicker;
 private AlertDialog ad;
 private String dateTime;
 private String initDateTime;
 private Context context;
 private TrSpecialWorkDao trSpecialWorkDao;

 /**
  * 日期时间弹出选择框构
  * @param context：调用的父activity
  */
 public MyDateTimePickerDialog(Context context)
 {
     this.context = context;
     trSpecialWorkDao = new TrSpecialWorkDao(context);
 }
 
 public void init(DatePicker datePicker,TimePicker timePicker)
 {
  Calendar calendar= Calendar.getInstance();
  initDateTime=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+
    calendar.get(Calendar.DAY_OF_MONTH)+" "+
    calendar.get(Calendar.HOUR_OF_DAY)+":"+
    calendar.get(Calendar.MINUTE)+
    calendar.get(Calendar.SECOND);
  datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), this);
  timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
  timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
 }
 
 /**
  * 弹出日期时间选择框
  * //@param dateTimeTextEdite 需要设置的日期时间文本编辑框
  * @param type: 0为日期时间类型:yyyy-MM-dd HH:mm:ss
  *         1为日期类型:yyyy-MM-dd
  *         2为时间类型:HH:mm:ss
  * @return
  */
 public AlertDialog dateTimePicKDialog(final TextView dateStartTimeTextEdite, int type, final String id, final String userId)
 {
  Calendar c = Calendar.getInstance();
  switch (type) {
  case 1:
   new DatePickerDialog(context,
     new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
        int dayOfMonth) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Calendar calendar = Calendar.getInstance();
          calendar.set(datePicker.getYear(), datePicker.getMonth(),
                  datePicker.getDayOfMonth());
          dateTime = sdf.format(calendar.getTime());
          dateStartTimeTextEdite.setText(dateTime);
          //修改日期
          String date = Constants.dateformat2.format(new Date());
          Map<Object,Object> columnsMap = new HashMap<Object, Object>();
          columnsMap.put("DETECTIONDATE",dateTime);
          columnsMap.put("UPDATETIME",date);
          columnsMap.put("UPDATEPERSON",userId);
          Map<Object,Object> wheresMap = new HashMap<Object, Object>();
          wheresMap.put("ID",id);
          trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
          //发送消息
          SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
          TrSpecialWork trSpecialWork = new TrSpecialWork();
          trSpecialWork.setDetectiondate(dateTime);
          trSpecialWork.setUpdatetime(date);
          trSpecialWork.setUpdateperson(userId);
          trSpecialWork.setId(id);
          List<List<Object>> list1 = new ArrayList<List<Object>>();
          List<Object> list2 = new ArrayList<Object>();
          list2.add(trSpecialWork);
          list1.add(list2);
          sympMessageRealDao.updateTextMessages(list1);
      }
     },
     c.get(Calendar.YEAR),
     c.get(Calendar.MONTH),
     c.get(Calendar.DATE)).show();
   return null;
  case 2:
   new TimePickerDialog(context,
     new TimePickerDialog.OnTimeSetListener() {
      public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
       Calendar calendar = Calendar.getInstance();
       calendar.set(Calendar.YEAR, Calendar.MONTH,
         Calendar.DAY_OF_MONTH, timePicker.getCurrentHour(),
         timePicker.getCurrentMinute());
       SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
       dateTime=sdf.format(calendar.getTime());
          dateStartTimeTextEdite.setText(dateTime);

          //修改日期
          String date = Constants.dateformat2.format(new Date());
          Map<Object,Object> columnsMap = new HashMap<Object, Object>();
          columnsMap.put("DETECTIONTIME",dateTime);
          columnsMap.put("UPDATETIME",date);
          columnsMap.put("UPDATEPERSON",userId);
          Map<Object,Object> wheresMap = new HashMap<Object, Object>();
          wheresMap.put("ID",id);
          trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
          //发送消息
          SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
          TrSpecialWork trSpecialWork = new TrSpecialWork();
          trSpecialWork.setDetectiontime(dateTime);
          trSpecialWork.setUpdatetime(date);
          trSpecialWork.setUpdateperson(userId);
          trSpecialWork.setId(id);
          List<List<Object>> list1 = new ArrayList<List<Object>>();
          List<Object> list2 = new ArrayList<Object>();
          list2.add(trSpecialWork);
          list1.add(list2);
          sympMessageRealDao.updateTextMessages(list1);
      }
     }, 
     c.get(Calendar.HOUR_OF_DAY), 
     c.get(Calendar.MINUTE), 
     true).show();
   return null;
  default:
//   LinearLayout dateTimeLayout  = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.datetime, null);
//   datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
//   timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
//   init(datePicker,timePicker);
//   timePicker.setIs24HourView(true);
//   timePicker.setOnTimeChangedListener(this);
//
//   ad = new AlertDialog.Builder(activity).setTitle("取样时间设置").setView(dateTimeLayout).setPositiveButton("设置",
//       new DialogInterface.OnClickListener()
//       {
//        public void onClick(DialogInterface dialog,
//          int whichButton)
//        {
//            dateStartTimeTextEdite.setText(dateTime);
//        }
//       }).setNegativeButton("取消",
//       new DialogInterface.OnClickListener()
//       {
//        public void onClick(DialogInterface dialog,
//          int whichButton)
//        {
////         dateTimeTextEdite.setText("");
//        }
//       }).show();
//
//   onDateChanged(null, 0, 0, 0);
   return ad;
  }
 }
 
 public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
 {
  onDateChanged(null, 0, 0, 0);
 }
 public void onDateChanged(DatePicker view, int year, int monthOfYear,
   int dayOfMonth)
 {
  Calendar calendar = Calendar.getInstance();
  calendar.set(datePicker.getYear(), datePicker.getMonth(),
    datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
    timePicker.getCurrentMinute());
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  dateTime=sdf.format(calendar.getTime());
  ad.setTitle(dateTime);
 }
 
}