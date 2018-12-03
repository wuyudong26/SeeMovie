package tvsdk.huawei.com.seemovie;

/**
 * @Date 2018/10/16
 * @Author wuyd
 * @ClassName MediaplayerActivity
 * @Description 视频播放
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.chinaunicom.medical.tv.tools.LogTrace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;

public class MediaplayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener {
    private static final int MSG_HIDE = 0X001;
    private static final int MSG_SHOW = 0X002;
    private static final int MSG_BACK = 0X003;
    private static final int MSG_FORWARD = 0X004;
    private static final int MSG_UPDATE_PROGRESSBAR = 0X005;
    private static final int MSG_PLAY= 0X006;
    private static final int MSG_PAUSE_SHOW= 0X007;
    private static final int MSG_PAUSE_HIDE= 0X008;
    private static final int MSG_CURRENT_PLAYTIME= 0X009;
    private static final int MSG_COMPLETE_SHOW=0X010;

    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private ProgressBar progressBar;
    private String TAG="MediaplayerActivity";
    private RelativeLayout mediaplayerControl;
    private RelativeLayout pause;
    private RelativeLayout mediaplayer_complete;

    private TextView totalTime;//影片总时长
    private TextView playTime;//影片当前播放的时长
    private  int current = 0;//当前播放时间控制
    private int hour,min,sec;
    private int hour_current,min_current,sec_current;

    private long mExitTime;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //播放暂停
                case MSG_PLAY:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    sendEmptyMessage(MSG_PAUSE_SHOW);
                    removeMessages(MSG_UPDATE_PROGRESSBAR);
                    current-=1000;
                } else {
                    mediaPlayer.start();
                    sendEmptyMessage(MSG_PAUSE_HIDE);
                    handler.sendEmptyMessage(MSG_UPDATE_PROGRESSBAR);
                }
                break;
                //快退
                case MSG_BACK:
                    removeMessages(MSG_UPDATE_PROGRESSBAR);
                    int pos=mediaPlayer.getCurrentPosition();
                    pos-=30000;
//                    LogTrace.d("TAG","getCurrentPosition","pos="+pos);
                    mediaPlayer.seekTo(pos);
                    if (current>30000){
                    current=current-30000;}
                    else current=0;
                    current-=1000;
                     handler.sendEmptyMessage(MSG_UPDATE_PROGRESSBAR);
                    handler.sendEmptyMessageDelayed(MSG_HIDE,5000);
                    break;
                    //快进
                case MSG_FORWARD:
                    removeMessages(MSG_UPDATE_PROGRESSBAR);
                    int pos1=mediaPlayer.getCurrentPosition();
//                    LogTrace.d("TAG","getCurrentPosition","pos"+pos1);
                    pos1+=30000;
                    if(pos1<=mediaPlayer.getDuration()){
                    mediaPlayer.seekTo(pos1);}
                    else {mediaPlayer.seekTo(mediaPlayer.getDuration());}
                    current=current+30000;
                    current-=1000;
                    handler.sendEmptyMessage(MSG_UPDATE_PROGRESSBAR);
                    handler.sendEmptyMessageDelayed(MSG_HIDE,5000);
                    break;
                    //进度条和播放时间隐藏
                case MSG_HIDE:
                    // do hide
                    mediaplayerControl.setVisibility(View.GONE);
                    break;
                    //暂停图标隐藏
                case MSG_PAUSE_HIDE:
                    pause.setVisibility(View.GONE);
                    break;
                    //进度条和播放时间显示
                case MSG_SHOW:
                    removeMessages(MSG_HIDE);
                    mediaplayerControl.setVisibility(View.VISIBLE);
                    break;
                   //暂停图标显示
                case MSG_PAUSE_SHOW:
                    pause.setVisibility(View.VISIBLE);
                    break;
                    //进度条更新
                case MSG_UPDATE_PROGRESSBAR:
                    if(mediaPlayer!=null)
                    {
                        if(current<=mediaPlayer.getDuration()){
                            progressBar.setProgress(current);}
                        else{progressBar.setProgress(mediaPlayer.getDuration());}
                        current= current+1000;
                        sendEmptyMessageDelayed(MSG_UPDATE_PROGRESSBAR,1000);
                    }
                    break;
                    //当前播放时间
                case MSG_CURRENT_PLAYTIME:
                    if(mediaPlayer!=null)
                    {
                        hour_current=mediaPlayer.getCurrentPosition()/3600000;
                        min_current=mediaPlayer.getCurrentPosition()/60000-hour_current*60;
                        sec_current=(mediaPlayer.getCurrentPosition()/1000)%60;
                        playTime.setText(hour_current+":"+min_current+":"+sec_current);
                        handler.sendEmptyMessageDelayed(MSG_CURRENT_PLAYTIME,1000);
                    }
                    break;
                    //播放完成界面显示
                case MSG_COMPLETE_SHOW:
                    removeMessages(MSG_HIDE);
                    mediaplayer_complete.setVisibility(View.VISIBLE);
                    mediaplayerControl.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mediaplayerControl=(RelativeLayout)findViewById(R.id.mediaplayerControl) ;
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        pause=(RelativeLayout) findViewById(R.id.pause);
        mediaplayer_complete=(RelativeLayout) findViewById(R.id.mediaplayer_complete);
        totalTime=(TextView)findViewById(R.id.totalTime);
        playTime=(TextView)findViewById(R.id.playTime);
        mediaPlayer=new MediaPlayer();

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        try {
//            File file = new File(url);
//            FileInputStream fis = new FileInputStream(file);
//            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);

//            mediaPlayer.setDataSource(url);
            holder=surfaceView.getHolder();
            holder.addCallback(this);
            mediaPlayer.prepare();
//            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        int max=mediaPlayer.getDuration();
        Log.d(TAG,"max="+max);
        progressBar.setMax(max);

        hour=max/3600000;
        min=(max-hour*3600000)/60000;
        sec=(max-hour*3600000-min*60000)/1000;
        totalTime.setText(hour+":"+min+":"+sec);
        handler.sendEmptyMessage(MSG_CURRENT_PLAYTIME);
        progressBar.setProgress(current);
        handler.sendEmptyMessage(MSG_UPDATE_PROGRESSBAR);
        //播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG,"播放完成监听执行了");
//                LogTrace.d("TAG","stop()","**************************");
             handler.sendEmptyMessage(MSG_COMPLETE_SHOW);
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.release();
        mediaPlayer = null;
    }
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()== KeyEvent.ACTION_DOWN){
            handler.sendEmptyMessage(MSG_SHOW);

            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_LEFT:
                            handler.sendEmptyMessage(MSG_BACK);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                        handler.sendEmptyMessage(MSG_FORWARD);
                    break;
                case KEYCODE_DPAD_CENTER:
                    handler.sendEmptyMessage(MSG_PLAY);
                    break;
            }
        }
        /**
         * 当按键抬起的不是enter键或者是enter键但是视频正在
         * 播放的时候，5s之后隐藏视频控制布局
         */
        else if (event.getAction()== KeyEvent.ACTION_UP) {
            if (event.getKeyCode() != KEYCODE_DPAD_CENTER) {
                handler.sendEmptyMessageDelayed(MSG_HIDE, 5000);
            } else if (event.getKeyCode() == KEYCODE_DPAD_CENTER && mediaPlayer.isPlaying()) {
                handler.sendEmptyMessageDelayed(MSG_HIDE, 5000);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * @Date 2018/10/16
     * @Author wuyd
     * @Description 两次按下返回键退出当前播放视频
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MediaplayerActivity.this, "再按一次退出当前播放视频", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler=null;
        if(null!=mediaPlayer)
        {
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
}


