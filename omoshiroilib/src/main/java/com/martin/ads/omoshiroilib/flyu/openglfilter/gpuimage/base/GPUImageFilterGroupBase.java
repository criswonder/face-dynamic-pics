package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;


import android.graphics.PointF;
import android.opengl.GLES20;

import com.martin.ads.omoshiroilib.constant.Rotation;
import com.martin.ads.omoshiroilib.flyu.openglfilter.common.FilterCompat;
import com.martin.ads.omoshiroilib.flyu.openglfilter.common.FilterConstants;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup.ShapeChangeFilter;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.draw.OpenGlUtils;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DynamicStickerDot;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;
import com.martin.ads.omoshiroilib.util.PlaneTextureRotationUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
/**
 * Created by Ads on 2017/6/6.
 */

public abstract class GPUImageFilterGroupBase extends GPUImageAudioFilter
{
    int[] mFrameBuffers;
    int[] mTextureIds;
    final FloatBuffer bF;
    final FloatBuffer bG;
    final FloatBuffer bH;
    GPUImageFilter bI;
    protected IGroupStateChanged bJ;
    protected IFilterDrawListener bK;

    public GPUImageFilterGroupBase()
    {
        this.bF = ByteBuffer.allocateDirect(FilterConstants.CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.bF.put(FilterConstants.CUBE).position(0);

        this.bG = ByteBuffer.allocateDirect(PlaneTextureRotationUtils.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.bG.put(PlaneTextureRotationUtils.TEXTURE_NO_ROTATION).position(0);

        float[] arrayOfFloat = PlaneTextureRotationUtils.getRotation(Rotation.NORMAL, false, true);

        this.bH = ByteBuffer.allocateDirect(arrayOfFloat.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.bH.put(arrayOfFloat).position(0);
    }

    public void setGroupStateChangedListener(IGroupStateChanged paramIGroupStateChanged)
    {
        this.bJ = paramIGroupStateChanged;
    }

    public void setFilterDrawListener(IFilterDrawListener paramIFilterDrawListener)
    {
        this.bK = paramIFilterDrawListener;
    }

    public abstract List<GPUImageFilter> groupGPUFilterList();

    public abstract void addFilter(GPUImageFilter paramGPUImageFilter);

    public void setPhoneDirection(int paramInt)
    {
        super.setPhoneDirection(paramInt);
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            localGPUImageFilter.setPhoneDirection(paramInt);
        }
    }

    public void releaseNoGLESRes()
    {
        super.releaseNoGLESRes();
        if (null != this.bI)
        {
            this.bI.releaseNoGLESRes();
            this.bI = null;
        }
    }

    public void onDestroy()
    {
        destoryFrameBuffers();
        if (null != this.bI)
        {
            this.bI.destroy();
            this.bI = null;
        }
        super.onDestroy();
    }

    private void destoryFrameBuffers()
    {
        if (this.mTextureIds != null)
        {
            GLES20.glDeleteTextures(this.mTextureIds.length, this.mTextureIds, 0);
            this.mTextureIds = null;
            GLES20.glDeleteFramebuffers(this.mFrameBuffers.length, this.mFrameBuffers, 0);
            this.mFrameBuffers = null;
        }
    }

    public void onOutputSizeChanged(int width, int height)
    {
        super.onOutputSizeChanged(width, height);
        if (this.mFrameBuffers != null) {
            destoryFrameBuffers();
        }
        int i = groupGPUFilterList().size();
        for (int j = 0; j < i; j++) {
            ((GPUImageFilter) groupGPUFilterList().get(j)).onOutputSizeChanged(width, height);
        }
        if (null != this.bI) {
            this.bI.onOutputSizeChanged(width, height);
        }
        if ((groupGPUFilterList() != null) && (groupGPUFilterList().size() > 0))
        {
            this.mFrameBuffers = new int[2];
            this.mTextureIds = new int[2];
            for (int j = 0; j < this.mFrameBuffers.length; j++)
            {
                GLES20.glGenFramebuffers(1, this.mFrameBuffers, j);
                GLES20.glGenTextures(1, this.mTextureIds, j);
                //Log.d(TAG, "20170717: "+width+" "+height);
                OpenGlUtils.bindTextureToFrameBuffer(this.mFrameBuffers[j], this.mTextureIds[j], width, height);
            }
        }
    }

    public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
    {
        throw new RuntimeException("this method should not been call!");
    }

    public void draw(int paramInt1, int paramInt2, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
    {
        beforeGroupDraw();
        runPendingOnDrawTasks();
        start();
        if ((!isInitialized()) || (this.mFrameBuffers == null) || (this.mTextureIds == null) || (null == groupGPUFilterList())) {
            return;
        }
        if (paramInt1 == -1) {
            return;
        }
        int i = groupGPUFilterList().size();
        int j = paramInt1;
        for (int k = 0; k < i; k++)
        {
            GPUImageFilter localGPUImageFilter = (GPUImageFilter) groupGPUFilterList().get(k);
            int m = k < i - 1 ? 1 : 0;
            if (m != 0)
            {
                GLES20.glBindFramebuffer(36160, this.mFrameBuffers[(k % 2)]);
                GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            }
            else if (-1 != paramInt2)
            {
                GLES20.glBindFramebuffer(36160, paramInt2);
                GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            }
            if (k == 0)
            {
                localGPUImageFilter.onDrawArraysPre(false);
                localGPUImageFilter.onDraw(j, paramFloatBuffer1, paramFloatBuffer2);
            }
            else if (k == i - 1)
            {
                localGPUImageFilter.onDrawArraysPre(i % 2 == 0);
                localGPUImageFilter.onDraw(j, this.bF, i % 2 == 0 ? this.bH : this.bG);
            }
            else
            {
                localGPUImageFilter.onDrawArraysPre(false);
                localGPUImageFilter.onDraw(j, this.bF, this.bG);
            }
            if (null != this.bK) {
                this.bK.onSingleFilterDrawed(this.surfaceWidth, this.surfaceHeight);
            }
            if (m != 0)
            {
                GLES20.glBindFramebuffer(36160, 0);
                j = this.mTextureIds[(k % 2)];
            }
            else
            {
                GLES20.glBindFramebuffer(36160, 0);
            }
        }
    }

    public PointF[][] setFaceDetResult(int faceCount, PointF[][] paramArrayOfPointF, int outPutWith, int outputHeight)
    {
        super.setFaceDetResult(faceCount, paramArrayOfPointF, outPutWith, outputHeight);
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            paramArrayOfPointF = localGPUImageFilter.setFaceDetResult(faceCount, paramArrayOfPointF, outPutWith, outputHeight);
        }
        return paramArrayOfPointF;
    }

    public void b(float[] paramArrayOfFloat)
    {
        super.b(paramArrayOfFloat);
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            localGPUImageFilter.b(paramArrayOfFloat);
        }
    }

    public int J()
    {
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            if (b(localGPUImageFilter))
            {
                if (((localGPUImageFilter instanceof ShapeChangeFilter)) && (((ShapeChangeFilter)localGPUImageFilter).R())) {
                    return 1;
                }
                if ((localGPUImageFilter instanceof DynamicStickerDot)) {
                    return 3;
                }
            }
        }
        return 0;
    }

    public void a(int paramInt1, int paramInt2, int paramInt3)
    {
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            if (b(localGPUImageFilter))
            {
                localGPUImageFilter.a(paramInt1, paramInt2, paramInt3);
                break;
            }
        }
    }

    public void f(int paramInt)
    {
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            if (b(localGPUImageFilter))
            {
                localGPUImageFilter.f(paramInt);
                break;
            }
        }
    }

    public int[] D()
    {
        for (GPUImageFilter localGPUImageFilter : groupGPUFilterList()) {
            if (b(localGPUImageFilter)) {
                return localGPUImageFilter.D();
            }
        }
        return new int[] { 0, 0, 0 };
    }

    boolean b(GPUImageFilter paramGPUImageFilter)
    {
        if (MiscUtils.isNilOrNull(FilterCompat.nameOfEditing)) {
            return false;
        }
        return FilterCompat.nameOfEditing.equals(paramGPUImageFilter.x());
    }

    public interface IGroupStateChanged {
        void onTipsAndCountChanged(int paramInt1, String paramString, int paramInt2);
    }

    public interface IFilterDrawListener {
        void onSingleFilterDrawed(int paramInt1, int paramInt2);
    }
}
