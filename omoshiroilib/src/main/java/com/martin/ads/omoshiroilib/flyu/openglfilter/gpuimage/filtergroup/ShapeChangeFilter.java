package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup;

import android.graphics.PointF;
import android.net.Uri;
import android.opengl.GLES20;
import android.util.Log;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterE;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.IOUtils;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ads on 2017/6/6.
 */

public class ShapeChangeFilter extends GPUImageFilterE
{
    private final String TAG = "ShapeChangeFilter";
    private boolean VERBOSE = true;
    static final String dW = "#define parameter";
    static final String dX = "uniform float parameter;";
    GroupData mGroupData;
    String mFragmentStr;
    PointF pointF = new PointF(0.0F, 0.0F);
    int[] muLocations;
    int[] muAngleLocations;
    int muOrientationLoction;
    int muDetectLocation;
    int muTimeLocation;
    float ct;
    float cu;
    boolean eb = false;
    int muParameterLocation = -1;

    public ShapeChangeFilter(String fragmentStr, GroupData parama)
    {
        super(fragmentStr, "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", parama.glsl);
        this.mFragmentStr = fragmentStr;
        this.mGroupData = parama;
        this.fragmentSource = seeIfNeedDefineParameter(this.fragmentSource);
        this.name = this.mGroupData.name;
        for (int i = 0; i < this.mGroupData.resList.size(); i++) {
            j(this.mFragmentStr + "/" + (String)this.mGroupData.resList.get(i));
        }
        if (!MiscUtils.isNilOrNull(this.mGroupData.audio)) {
            setAudioUri(Uri.parse(this.mFragmentStr + "/" + this.mGroupData.audio));
        }
        if (1 == this.mGroupData.resloadtype) {
            F();
        }
    }

    String seeIfNeedDefineParameter(String paramString)
    {
        if (paramString.contains("#define parameter"))
        {
            int i = paramString.indexOf("#define parameter");
            int j = paramString.indexOf('\n', i);
            String str = paramString.substring(i + "#define parameter".length(), j);
            this.bi = ((int)(MiscUtils.safeParseFloat(str) * 100.0F));

            paramString = paramString.substring(0, i) + "uniform float parameter;" + paramString.substring(j, paramString.length());
            this.eb = true;
        }
        return paramString;
    }

    public void onInit()
    {
        super.onInit();

        this.muLocations = new int[this.mGroupData.pointIndexArray.size()];
        for (int i = 0; i < this.mGroupData.pointIndexArray.size(); i++) {
            this.muLocations[i] = GLES20.glGetUniformLocation(getProgram(), "location" + i);
        }
        this.muAngleLocations = new int[this.mGroupData.maxcount];
        for (int i = 0; i < this.mGroupData.maxcount; i++) {
            this.muAngleLocations[i] = GLES20.glGetUniformLocation(getProgram(), "angle" + i);
        }
        this.muOrientationLoction = GLES20.glGetUniformLocation(getProgram(), "m_orientation");
        this.muDetectLocation = GLES20.glGetUniformLocation(getProgram(), "m_detect");
        this.muTimeLocation = GLES20.glGetUniformLocation(getProgram(), "m_time");
        this.muParameterLocation = GLES20.glGetUniformLocation(getProgram(), "parameter");
    }

    protected void onDrawArraysPre(int paramInt)
    {
        super.onDrawArraysPre(paramInt);
        int i = Math.min(this.facePointWrapper.faceCount, this.mGroupData.maxcount);

        int j = 0;
        switch (this.ba)
        {
            case 0:
                j = 3;
                break;
            case 1:
                j = 1;
                break;
            case 2:
                j = 4;
                break;
            case 3:
                j = 2;
        }
        if (-1 != this.muOrientationLoction) {
            setInt(this.muOrientationLoction, j);
        }
        if (-1 != this.muParameterLocation) {
            setFloat(this.muParameterLocation, this.bi * 0.01F);
        }
        for (int k = 0; k < this.muLocations.length; k++)
        {
            GroupData.Point locala = (GroupData.Point)this.mGroupData.pointIndexArray.get(k);
            if (locala.x >= i) {
                setPointF(this.muLocations[k], this.pointF);
            } else {
                b(this.muLocations[k], locala.x, locala.y);
            }
        }
        for (int k = 0; k < this.muAngleLocations.length; k++) {
            if (k >= i)
            {
                setPointF(this.muAngleLocations[k], new PointF(0.0F, 0.0F));
            }
            else
            {
                float f2 = 0.0F;
                float f3 = -1.0F;
                if (VERBOSE) Log.e(TAG, "onDrawArraysPre will access face pointArray");
                float f4 = this.facePointWrapper.pointArray[k][43].x - this.facePointWrapper.pointArray[k][46].x;
                float f5 = this.facePointWrapper.pointArray[k][43].y - this.facePointWrapper.pointArray[k][46].y;
                float f1 = (float)Math.acos((f2 * f4 + f3 * f5) / Math.sqrt(f2 * f2 + f3 * f3) / Math.sqrt(f4 * f4 + f5 * f5));
                if (f2 > f4) {
                    f1 = -f1;
                }
                setPointF(this.muAngleLocations[k], new PointF(
                        (float)Math.sin(-f1 + 1.5707963267948966D), (float)Math.cos(-f1 + 1.5707963267948966D)));
            }
        }
        if ((-1 != this.muTimeLocation) && (-1 != this.muDetectLocation))
        {
            if (i > 0)
            {
                if (this.cu >= 1.9F) {
                    this.cu = this.mGroupData.timeparam[0];
                } else {
                    this.cu = 1.0F;
                }
                if (((this.mGroupData.triggerType == 1) && (this.facePointWrapper.b())) || ((this.mGroupData.triggerType == 0) &&
                        (this.facePointWrapper.c())) || (this.mGroupData.triggerType == 2))
                {
                    this.cu = 2.1F;
                    start();
                    if (this.ct >= this.mGroupData.timeparam[2]) {
                        this.cu = this.mGroupData.timeparam[3];
                    }
                }
                else
                {
                    if (this.mGroupData.soundPlayMode == 1) {
                        stop();
                    }
                    this.cu += this.mGroupData.timeparam[4];
                }
            }
            else
            {
                this.cu = 0.0F;
                this.ct = 0.0F;
                stop();
            }
            if (this.cu >= this.mGroupData.timeparam[5])
            {
                this.ct += E() * this.mGroupData.timeparam[6];
                if (this.ct > this.mGroupData.timeparam[7])
                {
                    this.ct = 0.0F;
                    this.cu = 1.0F;
                    stop();
                }
            }
            else
            {
                this.ct = 0.0F;
            }
            setFloat(this.muTimeLocation, this.ct);
            setFloat(this.muDetectLocation, this.cu);
        }
    }

    public void releaseNoGLESRes()
    {
        super.releaseNoGLESRes();
        if (this.eb) {
            S();
        }
    }

    public void A()
    {
        super.A();

        this.ct = 0.0F;
        this.cu = 0.0F;
    }

    public int n()
    {
        return this.mGroupData.maxcount;
    }

    public boolean B()
    {
        return 2 != this.mGroupData.resloadtype;
    }

    public boolean R()
    {
        return this.eb;
    }

    public void S()
    {
        String str = this.fragmentSource.replace("uniform float parameter;", "#define parameter " + this.bi * 0.01F);
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(str);
        try
        {
            IOUtils.writeLinesToFile(this.mFragmentStr, "glsl", localArrayList);
        }
        catch (IOException localIOException)
        {
            Log.e("ShapeChangeFilter", "write failed");
        }
    }
}
