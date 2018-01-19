package com.martin.ads.omoshiroilib.flyu.openglfilter.detector;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Ads on 2017/6/6.
 */

public class FacePointWrapper {
    private final String TAG = "FacePointWrapper";
    private boolean VERBOSE = true;
    public int faceCount = 0;
    public PointF[][] pointArray = (PointF[][])null;
    double j = 0.0D;
    boolean k = false;

    public void init(int paramInt, PointF[][] paramArrayOfPointF)
    {
        this.faceCount = paramInt;
        this.pointArray = paramArrayOfPointF;
    }

    public boolean b()
    {
        if(VERBOSE) Log.e(TAG,"b");
        this.k = false;
        if (this.faceCount > 0)
        {
            PointF[] arrayOfPointF = this.pointArray[0];
            double d1 = Math.sqrt(
                    Math.pow((arrayOfPointF[39].x + arrayOfPointF[36].x) * 0.5D - arrayOfPointF[43].x, 2.0D) +
                            Math.pow((arrayOfPointF[39].y + arrayOfPointF[36].y) * 0.5D - arrayOfPointF[43].y, 2.0D));
            double d2 = Math.sqrt(Math.pow(arrayOfPointF[46].x - arrayOfPointF[43].x, 2.0D) +
                    Math.pow(arrayOfPointF[46].y - arrayOfPointF[43].y, 2.0D));
            double d3 = d1 / d2;
            if ((this.j != 0.0D) &&
                    ((d3 - this.j) / this.j > 0.15D)) {
                this.k = true;
            }
            this.j = d3;
        }
        return this.k;
    }

    public boolean c()
    {
        if(VERBOSE) Log.e(TAG,"c");
        if (null == this.pointArray[0]) {
            return false;
        }
        return distanceA(93, 87) > distanceA(90, 84) * 0.8D;
    }

    double distanceA(int paramInt1, int paramInt2)
    {
        if(VERBOSE) Log.d(TAG, "distanceA() called with: paramInt1 = [" + paramInt1 + "], paramInt2 = [" + paramInt2 + "]");
        return Math.sqrt(Math.pow(this.pointArray[0][paramInt1].x - this.pointArray[0][paramInt2].x, 2.0D) +
                Math.pow(this.pointArray[0][paramInt1].y - this.pointArray[0][paramInt2].y, 2.0D));
    }

    public boolean d()
    {
        if(VERBOSE) Log.e(TAG,"d");
        if (null == this.pointArray[0]) {
            return false;
        }
        float f1 = (float) distanceA(93, 87);
        float f2 = (float) distanceA(90, 84);
        return (f1 / f2 > 0.3D) && (f1 / f2 < 0.5D);
    }
}
