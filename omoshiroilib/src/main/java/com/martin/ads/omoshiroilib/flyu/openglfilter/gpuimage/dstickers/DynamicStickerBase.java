package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.martin.ads.omoshiroilib.flyu.openglfilter.common.BitmapLoader;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterE;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.MResFileIndexReader;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.MResFileReaderBase;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.draw.OpenGlUtils;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;

import java.io.IOException;

/**
 * Created by Ads on 2017/6/6.
 */

public class DynamicStickerBase extends GPUImageFilterE {
    private final String TAG = "DynamicStickerBase";
    private boolean VERBOSE = true;
    protected int bitmapTextureId;
    String multiSectionResPath;
    DstickerDataBean mDstickerDataBean;
    MResFileIndexReader mResMergeFileReader = null;
    int previousFrameIndex = -1;
    long mStartTimestamp = -1L;
    int cJ = 0;

    public DynamicStickerBase(DstickerDataBean dstickerDataBean, String resPath, String vSource, String fSource) {
        super(vSource, fSource);
        this.multiSectionResPath = resPath;
        this.mDstickerDataBean = dstickerDataBean;
        this.name = this.mDstickerDataBean.folderName;

        String str = this.multiSectionResPath.substring("file://".length());
        Pair localPair = MResFileReaderBase.tryGetMergeFile(str);
        if (null != localPair) {
            this.mResMergeFileReader = new MResFileIndexReader(str + "/" + (String) localPair.first, str + "/" + (String) localPair.second);
        }
    }

    public void onInit() {
        super.onInit();
        if (null != this.mResMergeFileReader) {
            try {
                this.mResMergeFileReader.init();
            } catch (IOException localIOException) {
                Log.e("DynamicStickerBase", "init merge res reader failed", localIOException);
                this.mResMergeFileReader = null;
            }
        }
        this.bitmapTextureId = -1;
        if ((!MiscUtils.isNilOrNull(this.mDstickerDataBean.audio)) &&
                (this.multiSectionResPath.startsWith("file://"))) {
            String str = this.multiSectionResPath.substring("file://".length());
            setAudioUri(Uri.parse(str + "/" + this.mDstickerDataBean.audio));
            b(this.mDstickerDataBean.looping);
        }
    }

    public void resetDrawStartTimeStamp() {
        super.resetDrawStartTimeStamp();

        this.mStartTimestamp = -1L;
        this.previousFrameIndex = -1;
    }

    protected void beforeGroupDraw() {
        super.beforeGroupDraw();
        if (this.facePointWrapper.faceCount <= 0) {
            this.mStartTimestamp = -1L;
            stop();
            return;
        }
        if (((1 != this.mDstickerDataBean.triggerType) || (!this.facePointWrapper.shakeEyeBrow())) && ((0 != this.mDstickerDataBean.triggerType) ||
                (!this.facePointWrapper.mouthOpenBig())) && (2 != this.mDstickerDataBean.triggerType)) {
            if (3 != this.mDstickerDataBean.triggerType) {

            }
        }
        int i = this.facePointWrapper.isMouthOpen() ? 1 : 0;
        if ((i == 0) && (!this.mDstickerDataBean.showUtilFinish)) {
            this.cJ = 0;
            stop();
            this.mStartTimestamp = -1L;
        } else if ((i == 0) && (this.cJ == 1)) {
            this.cJ = 1;
            start();
        } else if (i != 0) {
            this.cJ = 1;
            start();
        } else {
            this.cJ = 0;
            stop();
        }
        if (this.cJ != 1) {
            this.bitmapTextureId = -1;
            this.previousFrameIndex = -1;
            return;
        }
        if (this.mStartTimestamp == -1L) {
            this.mStartTimestamp = System.currentTimeMillis();
        }
        int frameIndex = (int) ((System.currentTimeMillis() - this.mStartTimestamp) / this.mDstickerDataBean.frameDuration);
        if (frameIndex >= this.mDstickerDataBean.frames) {
            if (!this.mDstickerDataBean.looping) {
                this.mStartTimestamp = -1L;
                this.bitmapTextureId = -1;
                this.previousFrameIndex = -1;
                this.cJ = 0;
                return;
            }
            frameIndex = 0;
            this.mStartTimestamp = System.currentTimeMillis();
        }
        if (frameIndex < 0) {
            frameIndex = 0;
        }
        if (this.previousFrameIndex == frameIndex) {
            return;
        }
        if ((frameIndex == 0) && (this.mDstickerDataBean.alignAudio)) {
            playerVideo();
        }
        Bitmap localBitmap = null;
        if (null != this.mResMergeFileReader) {
            localBitmap = this.mResMergeFileReader.loadBitmapAtIndex(frameIndex);
        }
        if (null == localBitmap) {
            String bitmapPath = String.format(this.mDstickerDataBean.folderName + "_%03d.png", new Object[]{Integer.valueOf(frameIndex)});
            if (VERBOSE) Log.e(TAG, "beforeGroupDraw filePath=" + bitmapPath);
            if (this.multiSectionResPath.startsWith("file://")) {
                String str2 = this.multiSectionResPath.substring("file://".length()) + "/" + bitmapPath;

                localBitmap = BitmapLoader.loadBitmapFromFile(str2);
            } else {
                localBitmap = null;
            }
        }
        if (null != localBitmap) {
            this.bitmapTextureId = OpenGlUtils.loadTexture(localBitmap, this.bitmapTextureId, true);
            this.previousFrameIndex = frameIndex;
        } else {
            this.bitmapTextureId = -1;
            this.previousFrameIndex = -1;
        }
    }

    public int n() {
        return this.mDstickerDataBean.maxcount;
    }

    public void onDestroy() {
        OpenGlUtils.deleteTexture(this.bitmapTextureId);
        this.bitmapTextureId = -1;
        super.onDestroy();
    }
}
