package com.example.myfirstview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;


import java.io.File;
import java.util.Random;

/**
 * Created by YHF at 14:48 on 2019-10-31.
 */

public class FloatingVideoService extends Service {
    public static boolean isStart = false;
    private static String LOCAL_VIDEO_NAME = Environment.getExternalStorageDirectory() + "/kdroid/local_video";
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private View displayView;
    private File[]files;

    private int back = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        isStart = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 512;
        layoutParams.height = 288;

        init();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        windowManager.addView(displayView, layoutParams);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stop();
        isStart = false;
        windowManager.removeView(displayView);
        mSurfaceView.getHolder().removeCallback(mSurfaceCallback);
    }

    private void init(){
        files = new File(LOCAL_VIDEO_NAME).listFiles();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // 在播放完毕被回调
                mMediaPlayer.reset();
                play();
            }
        });

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView = layoutInflater.inflate(R.layout.video_display, null);
        displayView.setOnTouchListener(new FloatingOnTouchListener());
        mSurfaceView = displayView.findViewById(R.id.video_display_surfaceview);
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);

    }


    private int last = -1;
    private void play() {
        Random random = new Random();
        int num = random.nextInt(files.length);
        if(num == last){
            num = num+1;
            if(num==files.length){
                num = 0;
            }
        }
        last = num;
        File file = files[num];
        if (!file.exists()) {
            Log.d("screenSave", "files is not exist");
            return;
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            if (!TextUtils.isEmpty(file.getAbsolutePath())) {
                mMediaPlayer.setDataSource(file.getAbsolutePath());

            }
            // 设置显示视频的SurfaceHolder
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();
            Log.d("screenSave", "play video: "+file.getName());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            play();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            stop();
        }
    };

    private void stop() {
        if (mMediaPlayer != null) {
            Log.d("screenSave", "mediaPlayer release");
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    back++;
                    if(back>2){
                        stopSelf();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    if(movedX>10||movedY>10){
                        back = 0;
                    }
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
