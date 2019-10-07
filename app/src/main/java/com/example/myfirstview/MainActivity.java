package com.example.myfirstview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MainActivity extends Activity {

    private FirstView firstView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstView = (FirstView)findViewById(R.id.first_view);
        init();
    }
    private void init(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(firstView,"firstValue",0,-45);
        animator1.setDuration(1000);
        animator1.setStartDelay(1000);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(firstView,"secondValue",0,270);
        animator2.setDuration(1000);
        animator2.setStartDelay(1000);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(firstView,"thirdValue",0,-45);
        animator3.setDuration(1000);
        animator3.setStartDelay(1000);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(firstView,"forthValue",0,360*50);
        animator4.setDuration(1000*10);
        animator4.setStartDelay(1000);
        animator4.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator5 = ObjectAnimator.ofFloat(firstView,"firstValue",-45,0);
        animator5.setDuration(1000);

        ObjectAnimator animator6 = ObjectAnimator.ofFloat(firstView,"thirdValue",-45,0);
        animator6.setDuration(1000);


        final AnimatorSet set = new AnimatorSet();
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
                        firstView.reset();
                        set.start();
                    }
                },1000);
            }
        });
        set.start();
    }
}
