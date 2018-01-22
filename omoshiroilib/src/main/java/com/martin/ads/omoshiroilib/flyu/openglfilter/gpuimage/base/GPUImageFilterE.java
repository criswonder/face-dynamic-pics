package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.support.annotation.CallSuper;
import android.util.Log;
import android.util.Pair;

import com.martin.ads.omoshiroilib.flyu.openglfilter.common.BitmapLoader;
import com.martin.ads.omoshiroilib.flyu.openglfilter.common.ImageLoader;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.draw.OpenGlUtils;
import com.martin.ads.omoshiroilib.flyu.sdk.commoninterface.IImageLoader;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.IOUtils;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageFilterE extends GPUImageAudioFilter
        implements IImageLoader.IAsyncLoadImgListener {
    private final String TAG = "GPUImageFilterE";
    private boolean VERBOSE = true;
    final int bp = 2;
    final int bq = 3;
    final int br = 8;
    static int[] bs = {33987, 33988, 33989, 33990, 33991, 33992, 33993, 33994};
    int[] muInputImageTextureLocations = new int[8];
    int[] bitmapTextureArray = new int[8];
    List<SoftReference<Bitmap>> bitmapCacheList = new ArrayList();
    public List<String> bitmapFileNames;
    boolean resTypeIs2 = false;
    MResFileNameReader resFileNameReader = null;
    long onDrawStartTime = -1L;

    public GPUImageFilterE(String vSource, String fSource) {
        this(null, vSource, fSource);
    }

    public GPUImageFilterE(String resPath, String vSource, String fSource) {
        super(vSource, fSource);
        for (int i = 0; i < 8; i++) {
            this.bitmapTextureArray[i] = -1;
            this.bitmapCacheList.add(null);
        }
        if (!MiscUtils.isNilOrNull(resPath)) {
            Pair localPair = MResFileReaderBase.tryGetMergeFile(resPath);
            if (null != localPair) {
                this.resFileNameReader = new MResFileNameReader(resPath + "/" + (String) localPair.first, resPath + "/" + (String) localPair.second);
            }
        }
    }

    public void onInit() {
        super.onInit();
        if (null != this.resFileNameReader) {
            try {
                this.resFileNameReader.init();
            } catch (IOException localIOException) {
                Log.e("GPUImageAudioFilter", "init res file name reader failed", localIOException);
                this.resFileNameReader = null;
            }
        }
        for (int i = 0; i < 8; i++) {
            this.muInputImageTextureLocations[i] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture" + (i + 2));
        }
        loadTextures();
    }

    protected float E() {
        if (-1L == this.onDrawStartTime) {
            return 0.0F;
        }
        return (float) (System.currentTimeMillis() - this.onDrawStartTime) / 1000.0F;
    }

    public void onDestroy() {
        super.onDestroy();

        for (int var1 = 0; var1 < 8; ++var1) {
            if (this.bitmapTextureArray[var1] != -1) {
                int[] var2 = new int[]{this.bitmapTextureArray[var1]};
                GLES20.glDeleteTextures(1, var2, 0);
                this.bitmapTextureArray[var1] = -1;
            }

            this.bitmapCacheList.set(var1, null);
        }

        if (null != this.bitmapFileNames) {
            Iterator var3 = this.bitmapFileNames.iterator();

            while (var3.hasNext()) {
                String var4 = (String) var3.next();
                ImageLoader.getImageLoaderImpl().cancelLoad(var4, this);
            }
        }
    }

    public void onOutputSizeChanged(int paramInt1, int paramInt2) {
        this.surfaceWidth = paramInt1;
        this.surfaceHeight = paramInt2;
    }

    public void onDraw(int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2) {
        super.onDraw(paramInt, paramFloatBuffer1, paramFloatBuffer2);
        this.onDrawStartTime = System.currentTimeMillis();
    }

    @CallSuper
    protected void onDrawArraysPre(int paramInt) {
        super.onDrawArraysPre(paramInt);
        for (int i = 0; i < 8; i++) {
            if (this.bitmapTextureArray[i] != -1) {
                GLES20.glActiveTexture(bs[i]);
                GLES20.glBindTexture(3553, this.bitmapTextureArray[i]);
                GLES20.glUniform1i(this.muInputImageTextureLocations[i], 3 + i);
            }
        }
    }

    public void resetDrawStartTimeStamp() {
        super.resetDrawStartTimeStamp();
        this.onDrawStartTime = -1L;
    }

    public void setRestypeTo2() {
        this.resTypeIs2 = true;
    }

    public void i(String paramString) {
        if (this.bitmapFileNames == null) {
            this.bitmapFileNames = new ArrayList();
        }
        this.bitmapFileNames.add("assets://" + paramString);
    }

    public void j(String paramString) {
        if (this.bitmapFileNames == null) {
            this.bitmapFileNames = new ArrayList();
        }
        this.bitmapFileNames.add("file://" + paramString);
    }

    public void loadTextures() {
        if (this.bitmapFileNames == null) {
            return;
        }
        for (int i = 0; i < this.bitmapFileNames.size(); i++) {
            if ((null != this.bitmapCacheList.get(i)) && (null != ((SoftReference) this.bitmapCacheList.get(i)).get())) {
                this.bitmapTextureArray[i] = OpenGlUtils.loadTexture((Bitmap) ((SoftReference) this.bitmapCacheList.get(i)).get(), -1, false);
            } else {
                Object bitmap;
                String str;
                String bitmapName = this.bitmapFileNames.get(i);
                if (this.resTypeIs2) {
                    bitmap = filterResHolder;
                    if (bitmapName.startsWith("assets://")) {
                        str = (bitmapFileNames.get(i)).substring("assets://".length());
                        bitmap = BitmapLoader.loadBitmapFromAssets(str);
                    } else if (bitmapName.startsWith("file://")) {
                        str = bitmapName.substring("file://".length());
                        if (null != this.resFileNameReader) {
                            bitmap = this.resFileNameReader.loadBitmapForName(IOUtils.extractFileName(str));
                        } else {
                            bitmap = BitmapLoader.loadBitmapFromFile(str);
                        }
                    } else if (bitmapName.startsWith("http://")) {
                        if (null != this.resFileNameReader) {
                            str = bitmapName.substring("http://".length());
                            bitmap = this.resFileNameReader.loadBitmapForName(IOUtils.extractFileName(str));
                        }
                    }
                    if (bitmap == null) {
                        Log.i("GPUImageAudioFilter", "filter res is null:" + bitmapName);
                        bitmap = filterResHolder;
                    }
                    this.bitmapTextureArray[i] = OpenGlUtils.loadTexture((Bitmap) bitmap, -1, false);
                } else {
                    this.bitmapTextureArray[i] = OpenGlUtils.loadTexture(filterResHolder, -1, false);
                    if (null != this.resFileNameReader) {
                        bitmap = this.resFileNameReader.getFileBuffer();
                        if (bitmapName.startsWith("http://")) {
                            str = bitmapName.substring("http://".length());
                        } else {
                            str = bitmapName.substring("file://".length());
                        }
                        String fileName = IOUtils.extractFileName(str);
                        Pair localPair = this.resFileNameReader.getOffsetAndLength(fileName);
                        if (null != localPair) {
                            ImageLoader.getImageLoaderImpl().asyncLoadImage(bitmapName, (byte[]) bitmap, ((Integer) localPair.first)
                                    .intValue(), ((Integer) localPair.second).intValue(), this);
                        }
                    } else {
                        ImageLoader.getImageLoaderImpl().asyncLoadImage(bitmapName, this);
                    }
                }
            }
        }
    }

    protected void updateUniformValue(int uniformLocation, int paramInt2, int face106PointIndex) {
        if (VERBOSE)
            Log.d(TAG, "pointArray updateUniformValue() called with: uniformLocation = [" + uniformLocation
                    + "], paramInt2 = [" + paramInt2 + "], face106PointIndex = [" + face106PointIndex + "]");
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = (this.facePointWrapper.pointArray[paramInt2][face106PointIndex].x / this.mOutputWidth);
        if (this.needFlip) {
            arrayOfFloat[1] = (1.0F - this.facePointWrapper.pointArray[paramInt2][face106PointIndex].y / this.mOutputHeight);
        } else {
            arrayOfFloat[1] = (this.facePointWrapper.pointArray[paramInt2][face106PointIndex].y / this.mOutputHeight);
        }
        GLES20.glUniform2fv(uniformLocation, 1, arrayOfFloat, 0);
    }

    protected PointF a(float paramFloat1, float paramFloat2) {
        PointF localPointF = new PointF();
        localPointF.x = (2.0F * paramFloat1 / this.mOutputWidth - 1.0F);
        localPointF.y = (2.0F * (1.0F - paramFloat2 / this.mOutputHeight) - 1.0F);
        return localPointF;
    }

    public void onLoadFinish(String filePath, Bitmap paramBitmap) {
        addTask(new BitmapLoadFinishRunnable(this, filePath, paramBitmap));
    }

    class BitmapLoadFinishRunnable implements Runnable {
        private GPUImageFilterE gpuImageFilterE;
        private String mFileName;
        Bitmap bitmap;

        BitmapLoadFinishRunnable(GPUImageFilterE var1, String var2, Bitmap var3) {
            this.gpuImageFilterE = var1;
            this.mFileName = var2;
            this.bitmap = var3;
        }

        public void run() {
            for (int i = 0; i < this.gpuImageFilterE.bitmapFileNames.size(); ++i) {
                if (((String) this.gpuImageFilterE.bitmapFileNames.get(i)).equals(this.mFileName)) {
                    this.gpuImageFilterE.bitmapCacheList.set(i, new SoftReference(this.bitmap));
                    if (null != this.bitmap) {
                        int[] var2 = new int[]{this.gpuImageFilterE.bitmapTextureArray[i]};
                        GLES20.glDeleteTextures(1, var2, 0);
                        this.gpuImageFilterE.bitmapTextureArray[i] = OpenGlUtils.loadTexture(this.bitmap, -1, false);
                    }
                }
            }

        }
    }
}
