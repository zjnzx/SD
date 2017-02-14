package com.energyfuture.symphony.m3.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 超 on 2015/5/4.
 */
public class MyEditText extends EditText {
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
//        if(this.isFocused() == true)
//            paint.setColor(Color.parseColor("#91A7A5"));
//        else
//            paint.setColor(Color.parseColor("#FAFAFA"));
        paint.setColor(Color.parseColor("#dddddd"));
        canvas.drawLine(0,this.getHeight()-5,  this.getWidth()-5, this.getHeight()-5, paint);
        super.onDraw(canvas);
    }
}
