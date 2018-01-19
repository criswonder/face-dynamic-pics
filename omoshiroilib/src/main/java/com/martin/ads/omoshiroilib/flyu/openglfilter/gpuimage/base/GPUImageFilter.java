package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import com.martin.ads.omoshiroilib.R;
import com.martin.ads.omoshiroilib.debug.removeit.GlobalConfig;
import com.martin.ads.omoshiroilib.flyu.openglfilter.detector.FacePointWrapper;
import com.martin.ads.omoshiroilib.util.ShaderUtils;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageFilter {
    protected static final Bitmap aK = BitmapFactory.decodeResource(GlobalConfig.context.getResources(), R.drawable.filter_res_hold);
    private final LinkedList<Runnable> aL;
    private String vertexSource;
    protected String fragmentSource;
    protected int programId;
    protected int maPostionLocation;
    protected int muInputImageTextureLocation;
    protected int maInputTextureCoordinateLocation;
    public int surfaceWidth;
    public int surfaceHeight;
    private boolean mLocationInited;
    protected FacePointWrapper facePointWrapper = new FacePointWrapper();
    protected int mOutputWidth;
    protected int mOutputHeight;
    protected boolean needFlip = false;
    protected boolean aZ = false;
    protected int ba = 1;
    protected float[] bb;
    protected boolean bc = false;
    private int muIsAndroidLocation;
    private int muSurfaceWidthLocation;
    private int muSurfaceHeightLocation;
    private int muNeedFlipLocation;
    protected String name = null;
    protected int bi = 0;
    protected int bj = 0;
    protected int bk = 0;

    public GPUImageFilter()
    {
        this("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
    }

    public GPUImageFilter(String paramString1, String paramString2)
    {
        this.aL = new LinkedList();
        this.vertexSource = paramString1;
        this.fragmentSource = paramString2;
    }

    public void setPhoneDirection(int paramInt)
    {
        this.ba = paramInt;
    }

    public final void init()
    {
        locationInit();
        this.mLocationInited = true;
        w();
    }

    protected int createProgram() {
        return ShaderUtils.createProgram(vertexSource, fragmentSource);
    }

    public void c(boolean paramBoolean)
    {
        this.needFlip = paramBoolean;
    }

    public void d(boolean paramBoolean)
    {
        this.aZ = paramBoolean;
    }

    protected float g(int paramInt1, int paramInt2)
    {
        return this.facePointWrapper.pointArray[paramInt1][paramInt2].x;
    }

    protected float h(int paramInt1, int paramInt2)
    {
        if (!this.needFlip) {
            return this.facePointWrapper.pointArray[paramInt1][paramInt2].y;
        }
        return this.mOutputHeight - this.facePointWrapper.pointArray[paramInt1][paramInt2].y;
    }

    public void locationInit()
    {
        this.programId = createProgram();
        this.maPostionLocation = GLES20.glGetAttribLocation(this.programId, "position");
        this.muInputImageTextureLocation = GLES20.glGetUniformLocation(this.programId, "inputImageTexture");
        this.maInputTextureCoordinateLocation = GLES20.glGetAttribLocation(this.programId, "inputTextureCoordinate");

        this.muIsAndroidLocation = GLES20.glGetUniformLocation(this.programId, "isAndroid");
        this.muSurfaceWidthLocation = GLES20.glGetUniformLocation(this.programId, "surfaceWidth");
        this.muSurfaceHeightLocation = GLES20.glGetUniformLocation(this.programId, "surfaceHeight");
        this.muNeedFlipLocation = GLES20.glGetUniformLocation(this.programId, "needFlip");
        this.mLocationInited = true;
    }

    public void w() {}

    public String x()
    {
        return this.name;
    }

    public PointF[][] setFaceDetResult(int faceCount, PointF[][] paramArrayOfPointF, int outputWidth, int outputHeight)
    {
        this.facePointWrapper.a(faceCount, paramArrayOfPointF);
        this.mOutputWidth = outputWidth;
        this.mOutputHeight = outputHeight;
        return paramArrayOfPointF;
    }

    public void t()
    {
        this.bc = true;
    }

    public void u()
    {
        this.bc = false;
    }

    public void releaseNoGLESRes() {}

    public final void destroy()
    {
        C();

        this.mLocationInited = false;
        GLES20.glDeleteProgram(this.programId);
        onDestroy();
    }

    public void onDestroy() {}

    public void onOutputSizeChanged(int paramInt1, int paramInt2)
    {
        this.surfaceWidth = paramInt1;
        this.surfaceHeight = paramInt2;
    }

    public int y()
    {
        return 3553;
    }

    public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
    {
        z();
        GLES20.glUseProgram(this.programId);
        C();
        if (!this.mLocationInited) {
            return;
        }
        paramFloatBuffer1.position(0);
        GLES20.glVertexAttribPointer(this.maPostionLocation, 2, 5126, false, 0, paramFloatBuffer1);
        GLES20.glEnableVertexAttribArray(this.maPostionLocation);
        paramFloatBuffer2.position(0);
        GLES20.glVertexAttribPointer(this.maInputTextureCoordinateLocation, 2, 5126, false, 0, paramFloatBuffer2);

        GLES20.glEnableVertexAttribArray(this.maInputTextureCoordinateLocation);
        if (paramInt != -1)
        {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(y(), paramInt);
            GLES20.glUniform1i(this.muInputImageTextureLocation, 0);
        }
        d(paramInt);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.maPostionLocation);
        GLES20.glDisableVertexAttribArray(this.maInputTextureCoordinateLocation);

        e(paramInt);

        GLES20.glBindTexture(y(), 0);
    }

    protected void z() {}

    protected void e(int paramInt) {}

    protected void d(int paramInt)
    {
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

    public void A() {}

    public boolean B()
    {
        return true;
    }

    public int n()
    {
        return 5;
    }

    protected void C()
    {
        LinkedList localLinkedList = new LinkedList();
        synchronized (this.aL)
        {
            for (Runnable localRunnable : this.aL) {
                localLinkedList.add(localRunnable);
            }
            this.aL.clear();
        }
        while (!localLinkedList.isEmpty()) {
            ((Runnable)localLinkedList.removeFirst()).run();
        }
    }

    public boolean isInitialized()
    {
        return this.mLocationInited;
    }

    public int getProgram()
    {
        return this.programId;
    }

    protected void setInt(int paramInt1, int paramInt2)
    {
        GLES20.glUniform1i(paramInt1, paramInt2);
    }

    protected void setFloat(int paramInt, float paramFloat)
    {
        GLES20.glUniform1f(paramInt, paramFloat);
    }

    protected void setFloatArray(int paramInt, float[] paramArrayOfFloat)
    {
        GLES20.glUniform2fv(paramInt, 1, FloatBuffer.wrap(paramArrayOfFloat));
    }

    protected void setPointF(int paramInt, PointF paramPointF)
    {
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = paramPointF.x;
        arrayOfFloat[1] = paramPointF.y;
        GLES20.glUniform2fv(paramInt, 1, arrayOfFloat, 0);
    }

    protected void setUniformMatrix4f(int paramInt, float[] paramArrayOfFloat)
    {
        GLES20.glUniformMatrix4fv(paramInt, 1, false, paramArrayOfFloat, 0);
    }

    protected double a(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
        return Math.sqrt((paramFloat1 - paramFloat3) * (paramFloat1 - paramFloat3) + (paramFloat2 - paramFloat4) * (paramFloat2 - paramFloat4));
    }

    public void addTask(Runnable paramRunnable)
    {
        synchronized (this.aL)
        {
            this.aL.addLast(paramRunnable);
        }
    }

    public void b(float[] paramArrayOfFloat)
    {
        this.bb = paramArrayOfFloat;
    }

    public void f(int paramInt)
    {
        this.bi = paramInt;
    }

    public int[] D()
    {
        return new int[] { this.bi, this.bj, this.bk };
    }

    public void a(int paramInt1, int paramInt2, int paramInt3)
    {
        addTask(new UnnamedD(this, paramInt1, paramInt2, paramInt3));
    }
}
