package com.martin.ads.omoshiroilib.filter.base;

import android.opengl.GLES20;

import java.util.LinkedList;

/**
 * Created by Ads on 2016/11/19.
 */

public abstract class AbsFilter {
    protected static final String TAG = "AbsFilter";
    private final LinkedList<Runnable> mPreDrawTaskList;
    protected int surfaceWidth,surfaceHeight;

    public AbsFilter() {
        mPreDrawTaskList = new LinkedList<Runnable>();
    }

    abstract public void init();

    public void onPreDrawElements(){
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    abstract public void destroy();

    public void onFilterChanged(int surfaceWidth, int surfaceHeight){
        this.surfaceWidth=surfaceWidth;
        this.surfaceHeight=surfaceHeight;
    }

    abstract public void onDrawFrame(final int textureId);


    public void runPreDrawTasks() {
        while (!mPreDrawTaskList.isEmpty()) {
            mPreDrawTaskList.removeFirst().run();
        }
    }

    public void addPreDrawTask(final Runnable runnable) {
        synchronized (mPreDrawTaskList) {
            mPreDrawTaskList.addLast(runnable);
        }
    }
}
