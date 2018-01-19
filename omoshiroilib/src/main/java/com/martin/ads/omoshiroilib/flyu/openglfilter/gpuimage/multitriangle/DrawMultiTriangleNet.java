package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.multitriangle;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterE;
import com.martin.ads.omoshiroilib.flyu.ysj.OmoshiroiNative;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Ads on 2017/6/6.
 */

public class DrawMultiTriangleNet extends GPUImageFilterE
{
    private final String TAG = "DrawMultiTriangleNet";
    private boolean VERBOSE = true;
    static final int eB = 200;
    int ej;
    int ek;
    MultiTriangleInfo eC;
    String db;
    FloatBuffer el;
    FloatBuffer em;
    FloatBuffer en;
    PointF[][] eD;
    PointF[][] eE;
    PointF[] eF;
    float[] eG = new float[9];
    float[] eH = new float[9];

    public DrawMultiTriangleNet(String paramString, MultiTriangleInfo paramMultiTriangleInfo)
    {
        super(paramString, "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n}", "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}");
        this.eC = paramMultiTriangleInfo;
        this.db = paramString;

        int i = 0;
        for (MultiTriangleInfo.a locala1 : this.eC.eI) {
            if (locala1.eJ.length > i) {
                i = locala1.eJ.length;
            }
        }
        this.el = ByteBuffer.allocateDirect(4 * i * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.em = ByteBuffer.allocateDirect(4 * i * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.en = ByteBuffer.allocateDirect(4 * i * 2).order(ByteOrder.nativeOrder()).asFloatBuffer();

        int j = 200;
        this.eD = new PointF[this.eC.eI.size()][];
        this.eE = new PointF[this.eC.eI.size()][];
        for (int k = 0; k < this.eC.eI.size(); k++)
        {
            MultiTriangleInfo.a locala2 = (MultiTriangleInfo.a)this.eC.eI.get(k);
            this.eD[k] = new PointF[106];

            int m = 200;
            for (int n = 0; n < locala2.eN.length; n++) {
                if (m < locala2.eN[n]) {
                    m = locala2.eN[n];
                }
            }
            this.eE[k] = new PointF[m - 200 + 1];
            for (int n = 0; n < locala2.eN.length; n++)
            {
                int i1 = locala2.eN[n];
                if (i1 >= 200) {
                    this.eE[k][(i1 - 200)] = locala2.eO[n];
                } else {
                    this.eD[k][i1] = locala2.eO[n];
                }
            }
            if (m > j) {
                j = m;
            }
        }
        this.eF = new PointF[j - 200 + 1];
        int k;
        for (k = 0; k < this.eF.length; k++) {
            this.eF[k] = new PointF();
        }
        for (k = 0; k < this.eC.eI.size(); k++) {
            j(this.db + "/" + ((MultiTriangleInfo.a)this.eC.eI.get(k)).eq);
        }
    }

    protected int createProgram()
    {
        return OmoshiroiNative.loadDrawMultiTriangleFilter();
    }

    public void onInit()
    {
        super.onInit();

        this.ej = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        this.ek = GLES20.glGetUniformLocation(getProgram(), "drawMask");
    }

    protected void onDrawArraysPre(int paramInt)
    {
        super.onDrawArraysPre(paramInt);
        setInt(this.ek, 0);
    }

    PointF a(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat)
    {
        PointF localPointF = new PointF();
        localPointF.x = (paramArrayOfFloat[0] * paramFloat1 + paramArrayOfFloat[1] * paramFloat2 + paramArrayOfFloat[2]);
        localPointF.y = (paramArrayOfFloat[3] * paramFloat1 + paramArrayOfFloat[4] * paramFloat2 + paramArrayOfFloat[5]);
        return localPointF;
    }

    protected void onDrawArraysAfter(int paramInt)
    {
        super.onDrawArraysAfter(paramInt);
        if (this.facePointWrapper.faceCount == 0) {
            return;
        }
        int i = Math.min(this.facePointWrapper.faceCount, this.eC.eI.size());
        for (int j = 0; j < i; j++)
        {
            if (VERBOSE) Log.e(TAG, "onDrawArraysAfter will access face pointArray");
            setInt(this.ek, j + 1);
            PointF[] arrayOfPointF = this.facePointWrapper.pointArray[j];
            MultiTriangleInfo.a locala = (MultiTriangleInfo.a)this.eC.eI.get(j);

            this.el.position(0);
            this.em.position(0);
            this.en.position(0);

            float f2 = 0.0F;
            float f3 = -1.0F;
            float f4 = this.facePointWrapper.pointArray[j][43].x - this.facePointWrapper.pointArray[j][46].x;
            float f5 = this.facePointWrapper.pointArray[j][43].y - this.facePointWrapper.pointArray[j][46].y;
            float f1 = (float)Math.acos((f2 * f4 + f3 * f5) / Math.sqrt(f2 * f2 + f3 * f3) / Math.sqrt(f4 * f4 + f5 * f5));
            if (f2 > f4) {
                f1 = -f1;
            }
            float f6 = this.facePointWrapper.pointArray[j][46].x;
            float f7 = this.facePointWrapper.pointArray[j][46].y;

            Matrix localMatrix1 = new Matrix();
            localMatrix1.setValues(new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F });
            if (!this.needFlip) {
                localMatrix1.setRotate((float)(-f1 * 180.0F / 3.141592653589793D), f6, f7);
            } else {
                localMatrix1.setRotate((float)(f1 * 180.0F / 3.141592653589793D), f6, f7);
            }
            localMatrix1.getValues(this.eG);

            Matrix localMatrix2 = new Matrix();
            localMatrix2.setValues(new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F });
            if (!this.needFlip) {
                localMatrix2.setRotate((float)(f1 * 180.0F / 3.141592653589793D), f6, f7);
            } else {
                localMatrix2.setRotate((float)(-f1 * 180.0F / 3.141592653589793D), f6, f7);
            }
            localMatrix2.getValues(this.eH);

            float f8 = (float) getTwoPointDistance(arrayOfPointF[locala.eK[0]].x, arrayOfPointF[locala.eK[0]].y, arrayOfPointF[locala.eK[1]].x, arrayOfPointF[locala.eK[1]].y);
            for (int k = 0; k < locala.eL.length; k++)
            {
                PointF localPointF1 = a(arrayOfPointF[locala.eL[k]].x, arrayOfPointF[locala.eL[k]].y, this.eH);
                localPointF1.x += f8 * locala.eM[k].x;
                localPointF1.y += f8 * locala.eM[k].y;
                this.eF[k] = a(localPointF1.x, localPointF1.y, this.eG);
            }
            for (int k = 0; k < locala.eJ.length; k++)
            {
                int m = locala.eJ[k];
                PointF localPointF2;
                if (m >= 200)
                {
                    m -= 200;
                    this.en.put(this.eE[j][m].x).put(this.eE[j][m].y);
                    if ((this.needFlip) && (!this.aZ)) {
                        localPointF2 = a(this.eF[m].x, this.surfaceHeight - this.eF[m].y);
                    } else {
                        localPointF2 = a(this.eF[m].x, this.eF[m].y);
                    }
                    this.el.put(localPointF2.x).put(localPointF2.y);
                    if (this.needFlip) {
                        this.em.put(this.eF[m].x / this.mOutputWidth).put(1.0F - this.eF[m].y / this.mOutputHeight);
                    } else {
                        this.em.put(this.eF[m].x / this.mOutputWidth).put(this.eF[m].y / this.mOutputHeight);
                    }
                }
                else
                {
                    this.en.put(this.eD[j][m].x).put(this.eD[j][m].y);
                    if ((this.needFlip) && (!this.aZ)) {
                        localPointF2 = a(arrayOfPointF[m].x, this.surfaceHeight - arrayOfPointF[m].y);
                    } else {
                        localPointF2 = a(arrayOfPointF[m].x, arrayOfPointF[m].y);
                    }
                    this.el.put(localPointF2.x).put(localPointF2.y);
                    if (this.needFlip) {
                        this.em.put(arrayOfPointF[m].x / this.mOutputWidth).put(1.0F - arrayOfPointF[m].y / this.mOutputHeight);
                    } else {
                        this.em.put(arrayOfPointF[m].x / this.mOutputWidth).put(arrayOfPointF[m].y / this.mOutputHeight);
                    }
                }
            }
            this.em.position(0);
            GLES20.glVertexAttribPointer(this.maInputTextureCoordinateLocation, 2, 5126, false, 0, this.em);

            GLES20.glEnableVertexAttribArray(this.maInputTextureCoordinateLocation);
            if (paramInt != -1)
            {
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(y(), paramInt);
                GLES20.glUniform1i(this.muInputImageTextureLocation, 0);
            }
            this.el.position(0);
            GLES20.glVertexAttribPointer(this.maPostionLocation, 2, 5126, false, 0, this.el);
            GLES20.glEnableVertexAttribArray(this.maPostionLocation);
            this.en.position(0);
            GLES20.glVertexAttribPointer(this.ej, 2, 5126, false, 0, this.en);

            GLES20.glEnableVertexAttribArray(this.ej);

            GLES20.glDrawArrays(4, 0, locala.eJ.length);

            GLES20.glDisableVertexAttribArray(this.maInputTextureCoordinateLocation);

            GLES20.glDisableVertexAttribArray(this.maPostionLocation);
            GLES20.glDisableVertexAttribArray(this.ej);
        }
        GLES20.glBindTexture(y(), 0);
    }
}
