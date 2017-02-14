package com.energyfuture.symphony.m3.util;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.energyfuture.symphony.m3.activity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    /*
    public static void configureWindowEnterExitTransition(Window w) {
        Explode ex = new Explode();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }
    */
    //红外全局变量 图号
    public static String number = "";

    //新增任务时,存放取消选中的任务ID
   public static  Map<String,String> iscchak = new HashMap<String,String>();

    //保存已选择照片
    public static  Map<String,String> picChecked = new HashMap<String,String>();//保存沟通交流图片
    public static  Map<String,String> picCheckedFeedBack = new HashMap<String,String>();//保存反馈的图片
    public static List<String> cameraPath = new ArrayList<String>();

    //存放选中的小组人员
    public static  Map<String,Boolean> personMap = new HashMap<String,Boolean>();
    //存放作业情况检测人员
    public static  Map<String,Map<String,Boolean>> cpersonMap = new HashMap<String,Map<String,Boolean>>();
    public static  Map<String,Boolean> condPersonMap = new HashMap<String,Boolean>();
    public static int position = 0;
    public static boolean isstop ;

    public static boolean iscurrentpage = false;//判断消息提醒是否为当前页面
    public static String themeid ;//主贴id
    public static boolean isMore = false ;//判断是否加载更多

    //成员及负责人
    public static Map<Integer,Boolean> radioButtonMap = new HashMap<Integer,Boolean>();
    //设备
    public static Map<String,Boolean> equipmentMap = new HashMap<String,Boolean>();


    //保存选中工具
    public static Map<String,String> countMap = new HashMap<String,String>();
    public static String wificount;

    public static void configureFab(View fabButton) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            ((ImageButton) fabButton).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                // hideSoftInput(v.getWindowToken());
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    public static void hideSoftInput(IBinder token, InputMethodManager im) {
        if (token != null) {
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
