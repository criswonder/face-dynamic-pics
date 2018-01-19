package com.martin.ads.omoshiroilib.flyu.openglfilter.detector;

import android.graphics.PointF;

/**
 * Created by Ads on 2017/6/6.
 */

public class FacePointWrapper {
    public int faceCount = 0;
    public PointF[][] pointArray = (PointF[][])null;
    double j = 0.0D;
    boolean k = false;

    public void a(int paramInt, PointF[][] paramArrayOfPointF)
    {
        this.faceCount = paramInt;
        this.pointArray = paramArrayOfPointF;
    }

    public boolean b()
    {
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
        if (null == this.pointArray[0]) {
            return false;
        }
        return a(93, 87) > a(90, 84) * 0.8D;
    }

    double a(int paramInt1, int paramInt2)
    {
        return Math.sqrt(Math.pow(this.pointArray[0][paramInt1].x - this.pointArray[0][paramInt2].x, 2.0D) +
                Math.pow(this.pointArray[0][paramInt1].y - this.pointArray[0][paramInt2].y, 2.0D));
    }

    public boolean d()
    {
        if (null == this.pointArray[0]) {
            return false;
        }
        float f1 = (float)a(93, 87);
        float f2 = (float)a(90, 84);
        return (f1 / f2 > 0.3D) && (f1 / f2 < 0.5D);
    }
}
