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
public class EditText_Login extends EditText {

    public EditText_Login(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        if(this.isFocused() == true)
            paint.setColor(Color.parseColor("#439AC8"));
        else
            paint.setColor(Color.parseColor("#999999"));
        canvas.drawLine(0,this.getHeight()-5,  this.getWidth()-5, this.getHeight()-5, paint);
        super.onDraw(canvas);
    }
}
