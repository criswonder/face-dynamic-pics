package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers;

import android.opengl.GLES20;

import com.martin.ads.omoshiroilib.flyu.ysj.OmoshiroiNative;

/**
 * Created by Ads on 2017/6/6.
 */

public class DynamicStickerVignette extends DynamicStickerBase
{
    DStickerVignetteBean dd;
    int cQ;
    int cR;
    int cS;
    int de;
    int df;

    public DynamicStickerVignette(String paramString, DStickerVignetteBean paramc)
    {
        super(paramc, paramString, "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
        this.dd = paramc;
    }

    protected int createProgram()
    {
        return OmoshiroiNative.loadDStickerVignetteFilter();
    }

    public void onInit()
    {
        super.onInit();
        this.cQ = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        this.cR = GLES20.glGetUniformLocation(getProgram(), "faceCnt");
        this.cS = GLES20.glGetUniformLocation(getProgram(), "flipSticker");
        this.de = GLES20.glGetUniformLocation(getProgram(), "leftTop");
        this.df = GLES20.glGetUniformLocation(getProgram(), "rightBottom");
    }

    protected void onDrawArraysPre(int paramInt)
    {
        super.onDrawArraysPre(paramInt);

        GLES20.glUniform1i(this.cR, this.facePointWrapper.faceCount > 0 ? 1 : 0);
        GLES20.glUniform1i(this.cS, this.needFlip ? 1 : 0);
        if (this.facePointWrapper.faceCount > 0)
        {
            float f1 = this.surfaceWidth * this.dd.height * 1.0F / this.dd.width;
            float f2 = f1 / this.surfaceHeight;
            if (this.needFlip)
            {
                if (this.dd.showTop)
                {
                    setFloatArray(this.de, new float[] { 0.0F, 1.0F - f2 });
                    setFloatArray(this.df, new float[] { 1.0F, 1.0F });
                }
                else
                {
                    setFloatArray(this.de, new float[] { 0.0F, 0.0F });
                    setFloatArray(this.df, new float[] { 1.0F, f2 });
                }
            }
            else if (!this.dd.showTop)
            {
                setFloatArray(this.de, new float[] { 0.0F, 1.0F - f2 });
                setFloatArray(this.df, new float[] { 1.0F, 1.0F });
            }
            else
            {
                setFloatArray(this.de, new float[] { 0.0F, 0.0F });
                setFloatArray(this.df, new float[] { 1.0F, f2 });
            }
        }
        if (this.bitmapTextureId != -1)
        {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, this.bitmapTextureId);
            GLES20.glUniform1i(this.cQ, 3);
        }
        else
        {
            GLES20.glUniform1i(this.cR, 0);
        }
    }
}
