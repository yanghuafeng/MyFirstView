package com.example.myfirstview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import static android.os.Looper.getMainLooper;

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
    private AnimatorSet set ;

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
        initPaint();
        initAnimator();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        camera = new Camera();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = -displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);

        bitmap = Bitmap.createScaledBitmap(bitmap,500, 500,false);
    }

    private void initAnimator(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this,"firstValue",0,-45);
        animator1.setDuration(1000);
        animator1.setStartDelay(1000);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this,"secondValue",0,270);
        animator2.setDuration(1000);
        animator2.setStartDelay(1000);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(this,"thirdValue",0,-45);
        animator3.setDuration(1000);
        animator3.setStartDelay(1000);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(this,"forthValue",0,360*50);
        animator4.setDuration(1000*10);
        animator4.setStartDelay(1000);
        animator4.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(this,"firstValue",-45,0);
        animator5.setDuration(1000);

        ObjectAnimator animator6 = ObjectAnimator.ofFloat(this,"thirdValue",-45,0);
        animator6.setDuration(1000);

        set = new AnimatorSet();
        AnimatorSet reset = new AnimatorSet();
        reset.playTogether(animator5, animator6);
        reset.setStartDelay(1000);
        set.playSequentially(animator1, animator2,animator3,animator4,reset);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Handler handler = new Handler(getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                        set.start();
                    }
                },1000);
            }
        });
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

    public void startAnimator(){
        set.start();
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
