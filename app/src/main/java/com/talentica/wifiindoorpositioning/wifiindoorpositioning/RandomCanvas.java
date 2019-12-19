package com.talentica.wifiindoorpositioning.wifiindoorpositioning;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RandomCanvas extends View {

    Integer width, height;
    Paint paint1;
    float x = 0;
    float y =0;
    Resources res;
    Bitmap bitmap;

    public RandomCanvas(Context context) {
        super(context);
    }

    public RandomCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.map_marker);
        paint1.setColor(Color.RED);
        paint1.setStrokeJoin(Paint.Join.ROUND);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        height=View.MeasureSpec.getSize(heightMeasureSpec);
        width=View.MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width,height);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap,x,y,new Paint(Paint.DITHER_FLAG));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_UP) {
            x = event.getX();
            y = event.getY();
            invalidate();
        }
        return true;
    }
}

