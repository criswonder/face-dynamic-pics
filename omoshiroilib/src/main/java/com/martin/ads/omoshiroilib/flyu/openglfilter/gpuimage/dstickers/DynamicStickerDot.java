package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers;

import android.graphics.Matrix;
import android.opengl.GLES20;
import android.util.Log;

import com.martin.ads.omoshiroilib.flyu.ysj.OmoshiroiNative;

/**
 * Created by Ads on 2017/6/6.
 */

public class DynamicStickerDot extends DynamicStickerBase
{
    private final String TAG = "DynamicStickerDot";
    private boolean VERBOSE = true;
    int[] muAlignPointLocations = new int[5];
    int[] muSizeLocation = new int[5];
    int uLocationInputImageTexture2;
    int muLocationFaceCnt;
    int muLocationFlipSticker;
    int[] muRotateMatrixLocation = new int[5];
    DstickerDataBeanExt mDstickerDataBeanExt;
    float[] cV = new float[16];
    float[] cW = new float[16];
    float[] cX = new float[9];
    float scaleWidth;
    float alignX;
    float alignY;

    public DynamicStickerDot(String resDir, DstickerDataBeanExt dstickerDataBeanExt)
    {
        super(dstickerDataBeanExt, resDir, "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
        this.mDstickerDataBeanExt = dstickerDataBeanExt;

        this.scaleWidth = this.mDstickerDataBeanExt.scaleWidth;
        this.alignX = this.mDstickerDataBeanExt.alignX;
        this.alignY = this.mDstickerDataBeanExt.alignY;
        this.bi = (this.bj = this.bk = 50);
    }

    protected int createProgram() {
        return OmoshiroiNative.loadDStickerDotFilter();
    }

    public void onInit()
    {
        super.onInit();
        this.uLocationInputImageTexture2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        this.muLocationFaceCnt = GLES20.glGetUniformLocation(getProgram(), "faceCnt");
        this.muLocationFlipSticker = GLES20.glGetUniformLocation(getProgram(), "flipSticker");
        for (int i = 0; i < 5; i++)
        {
            this.muAlignPointLocations[i] = GLES20.glGetUniformLocation(getProgram(), "alignPoint" + i);
            this.muSizeLocation[i] = GLES20.glGetUniformLocation(getProgram(), "size" + i);
            this.muRotateMatrixLocation[i] = GLES20.glGetUniformLocation(getProgram(), "rotateMatrix" + i);
        }
    }

    float g(int paramInt)
    {
        return (paramInt - 50) * 2 / 100.0F;
    }

    protected void onDrawArraysPre(int var1) {
        super.onDrawArraysPre(var1);
        if (VERBOSE) Log.e(TAG, "onDrawArraysPre will access face pointArray");

        this.mDstickerDataBeanExt.scaleWidth = (int)(this.scaleWidth + this.g(this.bi) * this.scaleWidth);
        this.mDstickerDataBeanExt.alignX = (int)(this.alignX + this.g(this.bj) * this.alignX);
        this.mDstickerDataBeanExt.alignY = (int)(this.alignY + this.g(this.bk) * this.alignY);
        if(this.mDstickerDataBeanExt.scaleWidth == 0) {
            this.mDstickerDataBeanExt.scaleWidth = 1;
        }

        int var2 = Math.min(this.facePointWrapper.faceCount, this.mDstickerDataBeanExt.maxcount);
        GLES20.glUniform1i(this.muLocationFaceCnt, var2);
        GLES20.glUniform1i(this.muLocationFlipSticker, this.needFlip ?1:0);

        for(int var3 = 0; var3 < var2; ++var3) {
            float var4 = (float)this.getTwoPointDistance(this.facePointWrapper.pointArray[var3][this.mDstickerDataBeanExt.rightIndex].x, this.facePointWrapper.pointArray[var3][this.mDstickerDataBeanExt.rightIndex].y, this.facePointWrapper.pointArray[var3][this.mDstickerDataBeanExt.leftIndex].x, this.facePointWrapper.pointArray[var3][this.mDstickerDataBeanExt.leftIndex].y) / (float)this.mDstickerDataBeanExt.scaleWidth;
            float var5 = var4 * (float)this.mDstickerDataBeanExt.width;
            float var6 = var5 * (float)this.mDstickerDataBeanExt.height / (float)this.mDstickerDataBeanExt.width;
            float var8 = 0.0F;
            float var9 = -1.0F;
            float var10 = this.facePointWrapper.pointArray[var3][43].x - this.facePointWrapper.pointArray[var3][46].x;
            float var11 = this.facePointWrapper.pointArray[var3][43].y - this.facePointWrapper.pointArray[var3][46].y;
            float var7 = (float)Math.acos((double)(var8 * var10 + var9 * var11) / Math.sqrt((double)(var8 * var8 + var9 * var9)) / Math.sqrt((double)(var10 * var10 + var11 * var11)));
            if(var8 > var10) {
                var7 = -var7;
            }

            float var12 = 0.0F;
            float var13 = 0.0F;

            int var14;
            for(var14 = 0; var14 < this.mDstickerDataBeanExt.alignIndexLst.length; ++var14) {
                var12 += this.faceMethoda(var3, this.mDstickerDataBeanExt.alignIndexLst[var14]);
                var13 += this.faceMethodb(var3, this.mDstickerDataBeanExt.alignIndexLst[var14]);
            }

            var12 /= (float)this.mDstickerDataBeanExt.alignIndexLst.length;
            var13 /= (float)this.mDstickerDataBeanExt.alignIndexLst.length;
            var14 = this.mDstickerDataBeanExt.width / 2 - this.mDstickerDataBeanExt.alignX;
            int var15 = this.mDstickerDataBeanExt.height / 2 - this.mDstickerDataBeanExt.alignY;
            float var16 = (float)var14 * 1.0F / (float)this.mDstickerDataBeanExt.width * var5;
            float var17 = (float)var15 * 1.0F / (float)this.mDstickerDataBeanExt.height * var6;
            float var18;
            float var19;
            if(!this.needFlip) {
                var18 = var12 + var16;
                var19 = var13 + var17;
            } else {
                var18 = var12 + var16;
                var19 = var13 - var17;
            }

            Matrix var20 = new Matrix();
            var20.setValues(new float[]{1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F});
            if(this.needFlip) {
                var20.setRotate(-((float)((double)(var7 * 180.0F) / 3.141592653589793D)), var12, var13);
            } else {
                var20.setRotate((float)((double)(var7 * 180.0F) / 3.141592653589793D), var12, var13);
            }

            var20.getValues(this.cX);
            float var21 = this.cX[0] * var18 + this.cX[1] * var19 + this.cX[2];
            float var22 = this.cX[3] * var18 + this.cX[4] * var19 + this.cX[5];
            float var23 = 1.0F * (float)this.mOutputHeight / (float)this.mOutputWidth;
            android.opengl.Matrix.setIdentityM(this.cV, 0);
            android.opengl.Matrix.scaleM(this.cW, 0, this.cV, 0, var23, 1.0F, 1.0F);
            android.opengl.Matrix.translateM(this.cV, 0, this.cW, 0, var21 / (float)this.mOutputWidth / var23, var22 / (float)this.mOutputHeight, 0.0F);
            if(this.needFlip) {
                android.opengl.Matrix.rotateM(this.cW, 0, this.cV, 0, (float)((double)(var7 * 180.0F) / 3.141592653589793D), 0.0F, 0.0F, 1.0F);
            } else {
                android.opengl.Matrix.rotateM(this.cW, 0, this.cV, 0, -((float)((double)(var7 * 180.0F) / 3.141592653589793D)), 0.0F, 0.0F, 1.0F);
            }

            android.opengl.Matrix.translateM(this.cV, 0, this.cW, 0, -var21 / (float)this.mOutputWidth / var23, -var22 / (float)this.mOutputHeight, 0.0F);
            android.opengl.Matrix.scaleM(this.cW, 0, this.cV, 0, 1.0F / var23, 1.0F, 1.0F);
            this.setFloatArray(this.muAlignPointLocations[var3], new float[]{var21 / (float)this.mOutputWidth, var22 / (float)this.mOutputHeight});
            this.setFloatArray(this.muSizeLocation[var3], new float[]{var5 / (float)this.mOutputWidth, var6 / (float)this.mOutputHeight});
            this.setUniformMatrix4f(this.muRotateMatrixLocation[var3], this.cW);
        }

        if(this.bitmapTextureId != -1) {
            GLES20.glActiveTexture('蓃');
            GLES20.glBindTexture(3553, this.bitmapTextureId);
            GLES20.glUniform1i(this.uLocationInputImageTexture2, 3);
        } else {
            GLES20.glUniform1i(this.muLocationFaceCnt, 0);
        }

    }
}
