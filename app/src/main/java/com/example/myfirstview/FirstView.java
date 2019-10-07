package com.example.myfirstview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by YHF at 13:51 on 2019-10-05.
 */

public class FirstView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Camera camera;

    private float firstValue;
    private float secondValue;
    private float thirdValue;
    private float forthValue;

    private int centerX;
    private int centerY;
    private int drawX;
    private int drawY;

    public FirstView(Context context){
        this(context,null);
    }
    public FirstView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.FirstView);
        BitmapDrawable drawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.FirstView_pic);
        typedArray.recycle();
        if(drawable!=null){
            bitmap = drawable.getBitmap();
        }else{
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon);
        }
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        camera = new Camera();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);

        bitmap = Bitmap.createScaledBitmap(bitmap,500, 500,false);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        centerY = getHeight()/2;
        centerX = getWidth()/2;
        drawX = centerX - bitmap.getWidth()/2;
        drawY = centerY - bitmap.getHeight()/2;

        //变化的一半
        canvas.save();
        camera.save();
        //移动轴心到画布中心
        canvas.translate(centerX,centerY);
        camera.rotateY(firstValue);
        camera.rotateZ(forthValue);
        canvas.rotate(-secondValue);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0,centerY,centerX,-centerY);
        //恢复轴心
        canvas.rotate(secondValue);
        canvas.translate(-centerX,-centerY);
        canvas.drawBitmap(bitmap,drawX,drawY,paint);
        camera.restore();
        canvas.restore();

        //不变的一半
        canvas.save();
        camera.save();
        canvas.translate(centerX,centerY);
        camera.rotateY(thirdValue);
        camera.rotateZ(forthValue);
        canvas.rotate(-secondValue);
        camera.applyToCanvas(canvas);
        canvas.clipRect(0,centerY,-centerX,-centerY);
        canvas.rotate(secondValue);
        canvas.translate(-centerX,-centerY);
        canvas.drawBitmap(bitmap,drawX,drawY,paint);
        camera.restore();
        canvas.restore();

    }

    public void reset(){
        firstValue = 0;
        secondValue = 0;
        thirdValue = 0;
        forthValue = 0;
    }
    public void setFirstValue(float firstValue) {
        this.firstValue = firstValue;
        invalidate();
    }

    public void setSecondValue(float secondValue) {
        this.secondValue = secondValue;
        invalidate();
    }

    public void setThirdValue(float thirdValue) {
        this.thirdValue = thirdValue;
        invalidate();
    }

    public void setForthValue(float forthValue) {
        this.forthValue = forthValue;
        invalidate();
    }

}