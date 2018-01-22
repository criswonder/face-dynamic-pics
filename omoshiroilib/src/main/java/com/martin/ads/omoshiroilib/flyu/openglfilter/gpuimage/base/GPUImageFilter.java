package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

import com.martin.ads.omoshiroilib.R;
import com.martin.ads.omoshiroilib.debug.removeit.GlobalConfig;
import com.martin.ads.omoshiroilib.flyu.openglfilter.detector.FacePointWrapper;
import com.martin.ads.omoshiroilib.util.ShaderUtils;

import java.nio.FloatBuffer;
import java.util.LinkedList;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageFilter {
    private final String TAG = "GPUImageFilter";
    private boolean VERBOSE = true;
    protected static final Bitmap filterResHolder = BitmapFactory.decodeResource(GlobalConfig.context.getResources(), R.drawable.filter_res_hold);
    private final LinkedList<Runnable> mRunnableList;
    private String vertexSource;
    protected String fragmentSource;
    protected int programId;
    protected int maPostionLocation;
    protected int muInputImageTextureLocation;
    protected int maInputTextureCoordinateLocation;
    public int surfaceWidth;
    public int surfaceHeight;
    private boolean mIsInitialized;
    protected FacePointWrapper facePointWrapper = new FacePointWrapper();
    protected int mOutputWidth;
    protected int mOutputHeight;
    protected boolean needFlip = false;
    protected boolean aZ = false;
    protected int phoneDirection = 1;
    protected float[] bb;
    protected boolean pause = false;
    private int muIsAndroidLocation;
    private int muSurfaceWidthLocation;
    private int muSurfaceHeightLocation;
    private int muNeedFlipLocation;
    protected String name = null;
    protected int bi = 0;
    protected int bj = 0;
    protected int bk = 0;

    public GPUImageFilter() {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    }

    public GPUImageFilter(String paramString1, String paramString2) {
        this.mRunnableList = new LinkedList();
        this.vertexSource = paramString1;
        this.fragmentSource = paramString2;
    }

    public void setPhoneDirection(int paramInt) {
        this.phoneDirection = paramInt;
    }

    public final void init() {
        onInit();
        this.mIsInitialized = true;
        onInitialized();
    }

    protected int createProgram() {
        return ShaderUtils.createProgram(vertexSource, fragmentSource);
    }

    public void c(boolean paramBoolean) {
        this.needFlip = paramBoolean;
    }

    public void onDrawArraysPre(boolean paramBoolean) {
        this.aZ = paramBoolean;
    }

    protected float faceMethoda(int paramInt1, int paramInt2) {
        if (VERBOSE) Log.e(TAG, "faceMethoda will access face pointArray");
        return this.facePointWrapper.pointArray[paramInt1][paramInt2].x;
    }

    protected float faceMethodb(int paramInt1, int paramInt2) {
        if (VERBOSE) Log.e(TAG, "faceMethodb will access face pointArray");
        if (!this.needFlip) {
            return this.facePointWrapper.pointArray[paramInt1][paramInt2].y;
        }
        return this.mOutputHeight - this.facePointWrapper.pointArray[paramInt1][paramInt2].y;
    }

    public void onInit() {
        this.programId = createProgram();
        this.maPostionLocation = GLES20.glGetAttribLocation(this.programId, "position");
        this.muInputImageTextureLocation = GLES20.glGetUniformLocation(this.programId, "inputImageTexture");
        this.maInputTextureCoordinateLocation = GLES20.glGetAttribLocation(this.programId, "inputTextureCoordinate");

        this.muIsAndroidLocation = GLES20.glGetUniformLocation(this.programId, "isAndroid");
        this.muSurfaceWidthLocation = GLES20.glGetUniformLocation(this.programId, "surfaceWidth");
        this.muSurfaceHeightLocation = GLES20.glGetUniformLocation(this.programId, "surfaceHeight");
        this.muNeedFlipLocation = GLES20.glGetUniformLocation(this.programId, "needFlip");
        this.mIsInitialized = true;
    }

    public void onInitialized() {
    }

    public String x() {
        return this.name;
    }

    public PointF[][] setFaceDetResult(int faceCount, PointF[][] paramArrayOfPointF, int outputWidth, int outputHeight) {
        this.facePointWrapper.init(faceCount, paramArrayOfPointF);
        this.mOutputWidth = outputWidth;
        this.mOutputHeight = outputHeight;
        return paramArrayOfPointF;
    }

    public void pause() {
        this.pause = true;
    }

    public void resume() {
        this.pause = false;
    }

    public void releaseNoGLESRes() {
    }

    public final void destroy() {
        runPendingOnDrawTasks();

        this.mIsInitialized = false;
        GLES20.glDeleteProgram(this.programId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void onOutputSizeChanged(int paramInt1, int paramInt2) {
        this.surfaceWidth = paramInt1;
        this.surfaceHeight = paramInt2;
    }

    public int y() {
        return 3553;
    }

    public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
        beforeGroupDraw();
        GLES20.glUseProgram(this.programId);
        runPendingOnDrawTasks();
        if (!this.mIsInitialized) {
            return;
        }
        paramFloatBuffer1.position(0);
        GLES20.glVertexAttribPointer(this.maPostionLocation, 2, 5126, false, 0, paramFloatBuffer1);
        GLES20.glEnableVertexAttribArray(this.maPostionLocation);
        paramFloatBuffer2.position(0);
        GLES20.glVertexAttribPointer(this.maInputTextureCoordinateLocation, 2, 5126, false, 0, paramFloatBuffer2);

        GLES20.glEnableVertexAttribArray(this.maInputTextureCoordinateLocation);
        if (paramInt != -1) {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(y(), paramInt);
            GLES20.glUniform1i(this.muInputImageTextureLocation, 0);
        }
        onDrawArraysPre(paramInt);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.maPostionLocation);
        GLES20.glDisableVertexAttribArray(this.maInputTextureCoordinateLocation);

        onDrawArraysAfter(paramInt);

        GLES20.glBindTexture(y(), 0);
    }

    protected void beforeGroupDraw() {
    }

    protected void onDrawArraysAfter(int paramInt) {
    }

    protected void onDrawArraysPre(int paramInt) {
        if (-1 != this.muIsAndroidLocation) {
            setInt(this.muIsAndroidLocation, 1);
        }
        if (-1 != this.muSurfaceWidthLocation) {
            setInt(this.muSurfaceWidthLocation, this.surfaceWidth);
        }
        if (-1 != this.muSurfaceHeightLocation) {
            setInt(this.muSurfaceHeightLocation, this.surfaceHeight);
        }
        if (-1 != this.muNeedFlipLocation) {
            setInt(this.muNeedFlipLocation, this.needFlip ? 1 : 0);
        }
    }

    public void resetDrawStartTimeStamp() {
    }

    public boolean B() {
        return true;
    }

    public int n() {
        return 5;
    }

    protected void runPendingOnDrawTasks() {
        LinkedList localLinkedList = new LinkedList();
        synchronized (this.mRunnableList) {
            for (Runnable localRunnable : this.mRunnableList) {
                localLinkedList.add(localRunnable);
            }
            this.mRunnableList.clear();
        }
        while (!localLinkedList.isEmpty()) {
            ((Runnable) localLinkedList.removeFirst()).run();
        }
    }

    public boolean isInitialized() {
        return this.mIsInitialized;
    }

    public int getProgram() {
        return this.programId;
    }

    protected void setInt(int paramInt1, int paramInt2) {
        GLES20.glUniform1i(paramInt1, paramInt2);
    }

    protected void setFloat(int paramInt, float paramFloat) {
        GLES20.glUniform1f(paramInt, paramFloat);
    }

    protected void setFloatArray(int paramInt, float[] paramArrayOfFloat) {
        GLES20.glUniform2fv(paramInt, 1, FloatBuffer.wrap(paramArrayOfFloat));
    }

    protected void setPointF(int paramInt, PointF paramPointF) {
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = paramPointF.x;
        arrayOfFloat[1] = paramPointF.y;
        GLES20.glUniform2fv(paramInt, 1, arrayOfFloat, 0);
    }

    protected void setUniformMatrix4f(int paramInt, float[] paramArrayOfFloat) {
        GLES20.glUniformMatrix4fv(paramInt, 1, false, paramArrayOfFloat, 0);
    }

    protected double getTwoPointDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void addTask(Runnable paramRunnable) {
        synchronized (this.mRunnableList) {
            this.mRunnableList.addLast(paramRunnable);
        }
    }

    public void b(float[] paramArrayOfFloat) {
        this.bb = paramArrayOfFloat;
    }

    public void f(int paramInt) {
        this.bi = paramInt;
    }

    public int[] D() {
        return new int[]{this.bi, this.bj, this.bk};
    }

    public void a(int paramInt1, int paramInt2, int paramInt3) {
        addTask(new UnnamedD(this, paramInt1, paramInt2, paramInt3));
    }
}
