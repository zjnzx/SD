package com.energyfuture.symphony.m3.datepicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.SympMessageRealDao;
import com.energyfuture.symphony.m3.dao.TrSpecialOliDataDao;
import com.energyfuture.symphony.m3.dao.TrSpecialWorkDao;
import com.energyfuture.symphony.m3.domain.TrSpecialOliData;
import com.energyfuture.symphony.m3.domain.TrSpecialWork;
import com.energyfuture.symphony.m3.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatePicker {

	private Dialog dialog_time;
	private Context context;
//	private boolean timeChanged = false;
//	private boolean timeScrolled = false;
	StringBuffer buffer;
	private String age;
    private String nedAge;
	private int mCurYear;
	private int mCurMonth;
	
	private int mCurHour;
	private int mCurMinute;
	
	private String[] dateType;
	private TextView submit;
	private TextView cancel;
    private TextView clear;
	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter,hourAdapter,minuteAdapter;
	private int mCurDay;
	private int style;
	public DatePicker(Context context){
		this.context = context;
		style = R.style.Dialog_Fullscreen;
	} 
	public DatePicker(Context context, int style){
		this.context = context;
		this.style = style;
	} 
	/**
	 * 选择日期对话框
	 * @param tv_time
	 */
	public void selectDateDialog(final TextView tv_time,final int type,final String[] data,final TextView texttime,final int flag) {
		View view = newDataDialog(R.layout.picker_date);

		submit = (TextView) view.findViewById(R.id.submit);
		cancel = (TextView) view.findViewById(R.id.cancel);
        clear= (TextView) view.findViewById(R.id.clear);

		final WheelView year = (WheelView) view.findViewById(R.id.year);
		final WheelView month = (WheelView) view.findViewById(R.id.month);
		final WheelView day = (WheelView) view.findViewById(R.id.day);
		// TextView tv_title = (TextView)
		// view.findViewById(R.id.dialog_time_title);

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String defaultTime=dateFormat.format(new Date());
        if(type==1){
            if(clear.getVisibility()==View.VISIBLE){
                clear.setVisibility(View.INVISIBLE);
            }
        }else{
            clear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_time.setText("");
                    dialog_time.dismiss();
                }
            });
        }
		
		try {
			String time = ((TextView)tv_time).getText().toString().trim();
			if(TextUtils.isEmpty(time)){
				this.age=defaultTime;
			}else{
				this.age = time;
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.age = defaultTime;
            Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
		}
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((TextView) tv_time).setText("" + age);
                if(flag == 0){
                    ((TextView) texttime).setText("" + nedAge);
                }
				dialog_time.dismiss();

                if(type==3){
                    //修改日期
                    TrSpecialWorkDao trSpecialWorkDao=new TrSpecialWorkDao(context);
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("DETECTIONDATE",age);
                    columnsMap.put("UPDATETIME",date);
                    columnsMap.put("UPDATEPERSON",data[0]);
                    Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID",data[1]);
                    trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
                    //发送消息
                    SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                    TrSpecialWork trSpecialWork = new TrSpecialWork();
                    trSpecialWork.setDetectiondate(age);
                    trSpecialWork.setUpdatetime(date);
                    trSpecialWork.setUpdateperson(data[0]);
                    trSpecialWork.setId(data[1]);
                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trSpecialWork);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                }
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog_time.dismiss();
			}
		});
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day,0,null,0);
			}

		};
		int curYear = calendar.get(Calendar.YEAR);
		if (age != null && age.contains("-")) {
			String str[] = age.split("-");
			mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
			mCurMonth = Integer.parseInt(str[1]) - 1;
			mCurDay = Integer.parseInt(str[2]) - 1;
		}
		dateType = context.getResources().getStringArray(R.array.date);
		monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
		monthAdapter.setTextType(dateType[1]);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);
		// year

		yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, 100 - 20);
		yearAdapter.setTextType(dateType[0]);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);
		// day

		updateDays(year, month, day,0,null,0);
		day.setCurrentItem(mCurDay);
		updateDays(year, month, day,0,null,0);
		day.addChangingListener(listener);

		dialog_time.show();
	}

    /**
     * 选择年月对话框
     * @param tv_time
     */
    public void selectNoDayDateDialog(final TextView tv_time,final int type,final String[] data,final TextView texttime,final int flag) {
        View view = newDataDialog(R.layout.picker_date_noday);

        submit = (TextView) view.findViewById(R.id.submit);
        cancel = (TextView) view.findViewById(R.id.cancel);
        clear= (TextView) view.findViewById(R.id.clear);

        final WheelView year = (WheelView) view.findViewById(R.id.year);
        final WheelView month = (WheelView) view.findViewById(R.id.month);
        // TextView tv_title = (TextView)
        // view.findViewById(R.id.dialog_time_title);

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM");
        String defaultTime=dateFormat.format(new Date());
        if(type==1){
            if(clear.getVisibility()==View.VISIBLE){
                clear.setVisibility(View.INVISIBLE);
            }
        }else{
            clear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_time.setText("");
                    dialog_time.dismiss();
                }
            });
        }

        try {
            String time = ((TextView)tv_time).getText().toString().trim();
            if(TextUtils.isEmpty(time)){
                this.age=defaultTime;
            }else{
                this.age = time;
            }
        } catch (Exception e) {
            // TODO: handle exception
            this.age = defaultTime;
            Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
        }


        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((TextView) tv_time).setText("" + age);
                if(flag == 0){
                    ((TextView) texttime).setText("" + nedAge);
                }
                dialog_time.dismiss();

                if(type==3){
                    //修改日期
                    TrSpecialWorkDao trSpecialWorkDao=new TrSpecialWorkDao(context);
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("DETECTIONDATE",age);
                    columnsMap.put("UPDATETIME",date);
                    columnsMap.put("UPDATEPERSON",data[0]);
                    Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID",data[1]);
                    trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
                    //发送消息
                    SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                    TrSpecialWork trSpecialWork = new TrSpecialWork();
                    trSpecialWork.setDetectiondate(age);
                    trSpecialWork.setUpdatetime(date);
                    trSpecialWork.setUpdateperson(data[0]);
                    trSpecialWork.setId(data[1]);
                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trSpecialWork);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_time.dismiss();
            }
        });
        Calendar calendar = Calendar.getInstance();
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateYears(year, month,0,null,0);
            }

        };
        int curYear = calendar.get(Calendar.YEAR);
        if (age != null && age.contains("-")) {
            String str[] = age.split("-");
            mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
            mCurMonth = Integer.parseInt(str[1]) - 1;
        }
        dateType = context.getResources().getStringArray(R.array.date);
        monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, 100 - 20);
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

//        updateDays(year, month, day,0,null,0);
//        day.setCurrentItem(mCurDay);
//        updateDays(year, month, day,0,null,0);
//        day.addChangingListener(listener);

        dialog_time.show();
    }


    public void selectDateTimeDialog(final TextView tv_time,final String[] data, final int type){
        View view = newDataDialog(R.layout.picker_date_time);

        submit= (TextView) view.findViewById(R.id.submit);
        clear= (TextView) view.findViewById(R.id.clear);
        cancel= (TextView) view.findViewById(R.id.cancel);

        final WheelView year = (WheelView) view.findViewById(R.id.year);
        final WheelView month = (WheelView) view.findViewById(R.id.month);
        final WheelView day = (WheelView) view.findViewById(R.id.day);
        final WheelView hour = (WheelView) view.findViewById(R.id.hour);
        final WheelView minute = (WheelView) view.findViewById(R.id.minute);


        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDaysTime(year,month,day,hour,minute);
            }
        };

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String defaultDate=dateFormat.format(new Date());
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        String defaultTime=timeFormat.format(new Date());

        try {
            String time = ((TextView)tv_time).getText().toString().trim();
            if(TextUtils.isEmpty(time)){
                this.age=defaultDate+" "+defaultTime;
            }else{
                this.age = time;
            }
        } catch (Exception e) {
            this.age = defaultDate+" "+defaultTime;
            Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
        }

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setText(age);
                dialog_time.dismiss();

                if(type==3){
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object, Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("RESULT1", age);
                    columnsMap.put("UPDATETIME", date);
                    columnsMap.put("UPDATEPERSON", data[0]);
                    Map<Object, Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID", data[1]);
                    TrSpecialOliDataDao trSpecialOliDataDao = new TrSpecialOliDataDao(context);
                    trSpecialOliDataDao.updateTrSpecialOliDataInfo(columnsMap, wheresMap);
                    //发送消息
                    SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                    TrSpecialOliData trSpecialOliData = new TrSpecialOliData();
                    trSpecialOliData.setResult1(age);
                    trSpecialOliData.setUpdatetime(date);
                    trSpecialOliData.setUpdateperson(data[0]);
                    trSpecialOliData.setId(data[1]);
                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trSpecialOliData);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                }
            }
        });

        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setText("");
                dialog_time.dismiss();
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_time.dismiss();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);

        if(age!=null&&age.contains(" ")){
            String[] date=age.split(" ");

            if (date[0] != null && date[0].contains("-")) {
                String str[] = date[0].split("-");
                mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
                mCurMonth = Integer.parseInt(str[1]) - 1;
                mCurDay = Integer.parseInt(str[2]) - 1;
                ;
            }
            if (date[1] != null && date[1].contains(":")) {
                String str[] = date[1].split(":");
                mCurHour =Integer.parseInt(str[0]);
                mCurMinute = Integer.parseInt(str[1]);
            }
        }


        dateType = context.getResources().getStringArray(R.array.date);
        monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, 100 - 20);
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

        updateDays(year, month, day,1,defaultTime,1);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day,1,defaultTime,1);
        day.addChangingListener(listener);

        hourAdapter = new DateNumericAdapter(context, 0, 23, calendar.get(Calendar.HOUR_OF_DAY));
        hourAdapter.setTextType(dateType[3]);
        hour.setViewAdapter(hourAdapter);
        hour.setCurrentItem(mCurHour);
        hour.addChangingListener(listener);
        // hour

        minuteAdapter = new DateNumericAdapter(context, 0, 59, calendar.get(Calendar.MINUTE));
        minuteAdapter.setTextType(dateType[4]);
        minute.setViewAdapter(minuteAdapter);
        minute.setCurrentItem(mCurMinute);
        minute.addChangingListener(listener);

        dialog_time.show();
    }


    /**
	 * 显示  小时和分钟的dialog
	 * @param tv_time  需要填充时间的控件
	 */
	public void selectTimeDialog(final TextView tv_time,final String[] data) {
		View view = newDataDialog(R.layout.picker_time);

		 submit = (TextView) view.findViewById(R.id.submit);
         clear= (TextView) view.findViewById(R.id.clear);
		 cancel = (TextView) view.findViewById(R.id.cancel);
		final WheelView hour = (WheelView) view.findViewById(R.id.dialog_time_hour);
		final WheelView minute = (WheelView) view.findViewById(R.id.dialog_time_minute);

        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        String defaultTime=timeFormat.format(new Date());
		try {
			String time = ((TextView)tv_time).getText().toString().trim();
			if(TextUtils.isEmpty(time)){
				this.age=defaultTime;
			}else{
				this.age = time;
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.age = defaultTime;
            Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
		}
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((TextView) tv_time).setText(age);
				dialog_time.dismiss();

                if(data!=null){
                    TrSpecialWorkDao trSpecialWorkDao=new TrSpecialWorkDao(context);
                    String date = Constants.dateformat2.format(new Date());
                    Map<Object,Object> columnsMap = new HashMap<Object, Object>();
                    columnsMap.put("DETECTIONTIME",age);
                    columnsMap.put("UPDATETIME",date);
                    columnsMap.put("UPDATEPERSON",data[0]);
                    Map<Object,Object> wheresMap = new HashMap<Object, Object>();
                    wheresMap.put("ID",data[1]);
                    trSpecialWorkDao.updateTrSpecialWorkInfo(columnsMap,wheresMap);
                    //发送消息
                    SympMessageRealDao sympMessageRealDao = new SympMessageRealDao(context);
                    TrSpecialWork trSpecialWork = new TrSpecialWork();
                    trSpecialWork.setDetectiontime(age);
                    trSpecialWork.setUpdatetime(date);
                    trSpecialWork.setUpdateperson(data[0]);
                    trSpecialWork.setId(data[1]);

                    List<List<Object>> list1 = new ArrayList<List<Object>>();
                    List<Object> list2 = new ArrayList<Object>();
                    list2.add(trSpecialWork);
                    list1.add(list2);
                    sympMessageRealDao.updateTextMessages(list1);
                }
			}
		});
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setText("");
                dialog_time.dismiss();
            }
        });
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog_time.dismiss();
			}
		});
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateTime(hour, minute);
			}

		};
		if (age != null && age.contains(":")) {
			String str[] = age.split(":");
			mCurHour =Integer.parseInt(str[0]);
			mCurMinute = Integer.parseInt(str[1]);
		}

		dateType = context.getResources().getStringArray(R.array.date);
		hourAdapter = new DateNumericAdapter(context, 0, 23, calendar.get(Calendar.HOUR_OF_DAY));
		hourAdapter.setTextType(dateType[3]);
		hour.setViewAdapter(hourAdapter);
		hour.setCurrentItem(mCurHour);
		hour.addChangingListener(listener);
		// hour

		minuteAdapter = new DateNumericAdapter(context, 0, 59, calendar.get(Calendar.MINUTE));
		minuteAdapter.setTextType(dateType[4]);
		minute.setViewAdapter(minuteAdapter);
		minute.setCurrentItem(mCurMinute);
		minute.addChangingListener(listener);
		// minute

//		updateDays(hour, minute);

		dialog_time.show();
	
	}



	@SuppressWarnings("deprecation")
	private View newDataDialog(int layoutID) {
		buffer = new StringBuffer();
		dialog_time = new Dialog(context, style);
		dialog_time.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutID, null);
		int screenWidth =((Activity)context).getWindowManager().getDefaultDisplay().getWidth();

		Window window = dialog_time.getWindow();
		// 重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setWindowAnimations(R.style.addresspickerstyle); // 添加动画
		window.setGravity(Gravity.BOTTOM);
		// window.setWindowAnimations(R.style.mystyle); // 添加动画
		lp.x = 0; // 新位置X坐标
		lp.y = 0; // 新位置Y坐标
		// lp.width = screenWidth;
		// window.setAttributes(lp);
		view.setMinimumWidth(screenWidth - 0);
		dialog_time.setContentView(view, lp);
		return view;
	}
	/**
	 * 年月日 的更新操作
	 * @param year
	 * @param month
	 * @param day
	 */
	public void updateDays(WheelView year, WheelView month, WheelView day,int type,String defaultime,int nedtype) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setTextType(dateType[2]);
		day.setViewAdapter(dayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);

		int years = calendar.get(Calendar.YEAR) - 100;
		String monthss = "";
        String dayss = "";
		if(month.getCurrentItem()+1<=9){
			monthss = "0"+(month.getCurrentItem()+1);
		}else{
			monthss = ""+(month.getCurrentItem()+1);
		}
		if(day.getCurrentItem()+1<=9){
			dayss = "0"+(day.getCurrentItem()+1);
		}else{
			dayss = ""+(day.getCurrentItem()+1);
		}

        if(type==1){
            age = years + "-" + monthss + "-" + dayss+" "+defaultime;
        }else{
            age = years + "-" + monthss + "-" + dayss;
            if(nedtype == 0){
                Date date = null;
                try {
                    date = Constants.dateformat1.parse(age);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
                }
                date.setMonth(date.getMonth()+1);
                nedAge = Constants.dateformat1.format(date);
            }
        }
	}

    public void updateYears(WheelView year, WheelView month,int type,String defaultime,int nedtype) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
//        dayAdapter.setTextType(dateType[2]);
//        day.setViewAdapter(dayAdapter);
//        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
//        day.setCurrentItem(curDay - 1, true);

        int years = calendar.get(Calendar.YEAR) - 100;
        String monthss = "";
        String dayss = "";
        if(month.getCurrentItem()+1<=9){
            monthss = "0"+(month.getCurrentItem()+1);
        }else{
            monthss = ""+(month.getCurrentItem()+1);
        }
//        if(day.getCurrentItem()+1<=9){
//            dayss = "0"+(day.getCurrentItem()+1);
//        }else{
//            dayss = ""+(day.getCurrentItem()+1);
//        }

        if(type==1){
            age = years + "-" + monthss;
        }else{
            age = years + "-" + monthss;
            if(nedtype == 0){
                Date date = null;
                try {
                    date = Constants.dateformat6.parse(age);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Constants.recordExceptionInfo(e, context, context.toString() + "/DatePicker");
                }
                date.setMonth(date.getMonth()+1);
                nedAge = Constants.dateformat1.format(date);
            }
        }
    }
	/**
	 * 时分的更新操作
	 * @param hour
	 * @param minutes
	 */
	public void updateTime(WheelView hour, WheelView minutes) {
		
		/*Calendar calendar = Calendar.getInstance();
		int maxMinute = calendar.getMaximum(Calendar.MINUTE);
		minuteAdapter = new DateNumericAdapter(context, 0, maxMinute, calendar.get(Calendar.MINUTE));
		minuteAdapter.setTextType(dateType[4]);
		minutes.setViewAdapter(minuteAdapter);
		int curDay = Math.min(maxMinute, minutes.getCurrentItem() + 1);
		minutes.setCurrentItem(curDay - 1, true);*/
		String hourss = "";
		String minutess = "";
		if(hour.getCurrentItem()<=9){
			hourss = "0"+(hour.getCurrentItem());
		}else{
			hourss = ""+(hour.getCurrentItem());
		}
		if(minutes.getCurrentItem()<=9){
			minutess = "0"+(minutes.getCurrentItem());
		}else{
			minutess = ""+(minutes.getCurrentItem());
		}
		
		age = hourss + ":" + minutess;
	}

    private void updateDaysTime(WheelView year, WheelView month, WheelView day,WheelView hour, WheelView minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        int years = calendar.get(Calendar.YEAR) - 100;
        String monthss = "";
        String dayss = "";
        if(month.getCurrentItem()+1<=9){
            monthss = "0"+(month.getCurrentItem()+1);
        }else{
            monthss = ""+(month.getCurrentItem()+1);
        }
        if(day.getCurrentItem()+1<=9){
            dayss = "0"+(day.getCurrentItem()+1);
        }else{
            dayss = ""+(day.getCurrentItem()+1);
        }

        String hourss = "";
        String minutess = "";
        if(hour.getCurrentItem()<=9){
            hourss = "0"+(hour.getCurrentItem());
        }else{
            hourss = ""+(hour.getCurrentItem());
        }
        if(minutes.getCurrentItem()<=9){
            minutess = "0"+(minutes.getCurrentItem());
        }else{
            minutess = ""+(minutes.getCurrentItem());
        }


        age = years + "-" + monthss + "-" + dayss+" "+hourss + ":" + minutess;
    }

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}
	
}
