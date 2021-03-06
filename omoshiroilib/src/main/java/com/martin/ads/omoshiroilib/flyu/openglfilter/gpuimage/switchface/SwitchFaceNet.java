package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.switchface;

import android.graphics.PointF;
import android.net.Uri;
import android.opengl.GLES20;
import android.util.Log;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterE;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;

/**
 * Created by Ads on 2017/6/6.
 */

public class SwitchFaceNet extends GPUImageFilterE
{
    private final String TAG = "SwitchFaceNet";
    private boolean VERBOSE = true;
    SwitchFaceInfo ff;
    String bV;
    PointF cA = new PointF(0.0F, 0.0F);
    int[] cB;
    int[] dZ;
    int ea;

    public SwitchFaceNet(String paramString, SwitchFaceInfo paramSwitchFaceInfo)
    {
        super(paramString, "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", paramSwitchFaceInfo.bN);
        this.ff = paramSwitchFaceInfo;
        this.bV = paramString;
        for (int i = 0; i < this.ff.bR.length; i++) {
            j(this.bV + "/" + this.ff.bR[i]);
        }
        if (!MiscUtils.isNilOrNull(this.ff.bS)) {
            setAudioUri(Uri.parse(this.bV + "/" + this.ff.bS));
        }
        setRestypeTo2();
    }

    public void onInit()
    {
        super.onInit();

        this.cB = new int[this.ff.cv.size()];
        for (int i = 0; i < this.ff.cv.size(); i++) {
            this.cB[i] = GLES20.glGetUniformLocation(getProgram(), "location" + i);
        }
        this.dZ = new int[this.ff.cw];
        for (int i = 0; i < this.ff.cw; i++) {
            this.dZ[i] = GLES20.glGetUniformLocation(getProgram(), "angle" + i);
        }
        this.ea = GLES20.glGetUniformLocation(getProgram(), "m_orientation");
    }

    protected void onDrawArraysPre(int paramInt)
    {
        super.onDrawArraysPre(paramInt);

        int i = 0;
        switch (this.phoneDirection)
        {
            case 0:
                i = 3;
                break;
            case 1:
                i = 1;
                break;
            case 2:
                i = 4;
                break;
            case 3:
                i = 2;
        }
        if (-1 != this.ea) {
            setInt(this.ea, i);
        }
        for (int j = 0; j < this.cB.length; j++)
        {
            SwitchFaceInfo.a locala = (SwitchFaceInfo.a)this.ff.cv.get(j);
            if (locala.cx >= this.facePointWrapper.faceCount) {
                setPointF(this.cB[j], this.cA);
            } else {
                updateUniformValue(this.cB[j], locala.cx, locala.cy);
            }
        }
        for (int j = 0; j < this.dZ.length; j++) {
            if (j >= this.facePointWrapper.faceCount)
            {
                setPointF(this.dZ[j], new PointF(0.0F, 0.0F));
            }
            else
            {
                float f2 = 0.0F;
                float f3 = -1.0F;
                if (VERBOSE) Log.e(TAG, "onDrawArraysPre will access face pointArray");
                float f4 = this.facePointWrapper.pointArray[j][43].x - this.facePointWrapper.pointArray[j][46].x;
                float f5 = this.facePointWrapper.pointArray[j][43].y - this.facePointWrapper.pointArray[j][46].y;
                float f1 = (float)Math.acos((f2 * f4 + f3 * f5) / Math.sqrt(f2 * f2 + f3 * f3) / Math.sqrt(f4 * f4 + f5 * f5));
                if (f2 > f4) {
                    f1 = -f1;
                }
                setPointF(this.dZ[j], new PointF(
                        (float)Math.sin(-f1 + 1.5707963267948966D), (float)Math.cos(-f1 + 1.5707963267948966D)));
            }
        }
    }
}
