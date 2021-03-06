package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.martin.ads.omoshiroilib.debug.removeit.GlobalConfig;
import com.martin.ads.omoshiroilib.flyu.sdk.mediaplayer.FMediaPlayer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageAudioFilter extends GPUImageFilter
{
    static final String TAG = "GPUImageAudioFilter";
    static final int H = 0;
    static final int I = 1;
    static final int J = 2;
    static final int K = 3;
    Uri L;
    boolean M = false;
    boolean N = false;
    int O = 0;
    MediaPlayer mediaPlayer = null;
    Set<Object> Q = new HashSet();

    public GPUImageAudioFilter() {}

    public GPUImageAudioFilter(String paramString1, String paramString2)
    {
        super(paramString1, paramString2);
    }

    protected void h(String paramString)
    {
        setAudioUri(Uri.parse("android.resource://" + GlobalConfig.context + "/raw/" + paramString));
    }

    public void setAudioUri(Uri paramUri)
    {
        this.L = paramUri;
    }

    public Uri p()
    {
        return this.L;
    }

    public void b(boolean paramBoolean)
    {
        this.M = paramBoolean;
    }

    public boolean q()
    {
        return this.M;
    }

    protected void start()
    {
        if ((null == this.L) || (this.pause)) {
            return;
        }
        if (0 == this.O)
        {
            v();
        }
        else if (2 == this.O)
        {
            this.mediaPlayer.start();
            this.mediaPlayer.seekTo(0);
            this.O = 3;
            Log.d("GPUImageAudioFilter", "status: STATUS_PLAYING");
        }
        else if (1 == this.O)
        {
            this.N = true;
        }
    }

    protected void stop()
    {
        if ((null != this.mediaPlayer) && (3 == this.O))
        {
            this.mediaPlayer.pause();
            this.O = 2;
            Log.d("GPUImageAudioFilter", "status: STATUS_INITED");
        }
        this.N = false;
    }

    protected void playerVideo()
    {
        if ((null != this.mediaPlayer) && (3 == this.O)) {
            this.mediaPlayer.seekTo(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        s();
    }

    void s()
    {
        stop();
        if ((null != this.mediaPlayer) && (2 == this.O))
        {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.Q.remove(this.mediaPlayer);
        }
        this.mediaPlayer = null;
        this.O = 0;
        Log.d("GPUImageAudioFilter", "status: STATUS_UNINITIAL");
    }

    public void releaseNoGLESRes()
    {
        super.releaseNoGLESRes();
        s();
    }

    public void pause()
    {
        super.pause();
        if ((null != this.mediaPlayer) && (3 == this.O)) {
            this.mediaPlayer.pause();
        }
    }

    public void resume()
    {
        super.resume();
        if ((null != this.mediaPlayer) && (3 == this.O)) {
            this.mediaPlayer.start();
        }
    }

    void v()
    {
        this.mediaPlayer = new b(this);
        try
        {
            this.mediaPlayer.setDataSource(GlobalConfig.context, this.L);
            this.mediaPlayer.setOnPreparedListener(new a());
            this.Q.add(this.mediaPlayer);
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setLooping(this.M);
            this.O = 1;
            this.N = true;
            Log.d("GPUImageAudioFilter", "status: STATUS_INITING");
        }
        catch (IOException localIOException)
        {
            Log.e("GPUImageAudioFilter", "open audio failed, " + localIOException.getMessage());
        }
    }

    class a
            implements MediaPlayer.OnPreparedListener
    {
        a() {}

        public void onPrepared(MediaPlayer paramMediaPlayer)
        {
            addTask(new c(paramMediaPlayer));
        }
    }

    class b extends FMediaPlayer
    {
        private GPUImageAudioFilter R;
        b(GPUImageAudioFilter paramGPUImageAudioFilter) {
            this.R=paramGPUImageAudioFilter;
        }

        protected void audioFocusLoss(boolean paramBoolean)
        {
            Log.d("GPUImageAudioFilter", "loss focus");
            if (paramBoolean) {
                this.R.stop();
            }
        }
    }

    class c
            implements Runnable
    {
        MediaPlayer S;
        c(MediaPlayer paramMediaPlayer) {
            this.S=paramMediaPlayer;
        }

        public void run()
        {
            if (N && (1 == O) && (null != mediaPlayer))
            {
                mediaPlayer.start();
                O = 3;
                Log.d("GPUImageAudioFilter", "status: STATUS_PLAYING");
            }
            else if (1 == O)
            {
                O = 2;
                Log.d("GPUImageAudioFilter", "status: STATUS_INITED");
            }
            if ((this.S != mediaPlayer) && (Q.contains(this.S)))
            {
                this.S.stop();
                this.S.release();
            }
        }
    }
}
