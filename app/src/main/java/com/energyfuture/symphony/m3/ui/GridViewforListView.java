package com.energyfuture.symphony.m3.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2015/9/9.
 */
public class GridViewforListView extends GridView {
    public GridViewforListView(Context context) {
        super(context);
    }

    public GridViewforListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewforListView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
