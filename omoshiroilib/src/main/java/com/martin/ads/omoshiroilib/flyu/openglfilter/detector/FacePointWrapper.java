package com.martin.ads.omoshiroilib.flyu.openglfilter.detector;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Ads on 2017/6/6.
 */

/**
 * 84,90 左右嘴唇的点
 * 87,93 上下嘴唇的点
 * <p>
 * 46,43 鼻梁之间的距离
 * <p>
 * 36 左眉毛，39 右眉毛，43 两眼中点，
 */
public class FacePointWrapper {
    private final String TAG = "FacePointWrapper";
    private boolean VERBOSE = true;
    public int faceCount = 0;
    public PointF[][] pointArray = (PointF[][]) null;
    double j = 0.0D;
    boolean eyebrowMoved = false;

    public void init(int paramInt, PointF[][] paramArrayOfPointF) {
        this.faceCount = paramInt;
        this.pointArray = paramArrayOfPointF;
    }

    public boolean shakeEyeBrow() {
        this.eyebrowMoved = false;
        if (this.faceCount > 0) {
            PointF[] arrayOfPointF = this.pointArray[0];

            //两个眉毛的中点 和两个眼睛中点的距离
            double d1 = Math.sqrt(
                    Math.pow((arrayOfPointF[39].x + arrayOfPointF[36].x) * 0.5D - arrayOfPointF[43].x, 2.0D) +
                            Math.pow((arrayOfPointF[39].y + arrayOfPointF[36].y) * 0.5D - arrayOfPointF[43].y, 2.0D));

            //两眼中点和鼻子最下面的点之间的距离
            double d2 = Math.sqrt(Math.pow(arrayOfPointF[46].x - arrayOfPointF[43].x, 2.0D) +
                    Math.pow(arrayOfPointF[46].y - arrayOfPointF[43].y, 2.0D));

            double d3 = d1 / d2;
            if ((this.j != 0.0D) &&
                    ((d3 - this.j) / this.j > 0.15D)) {
                this.eyebrowMoved = true;
            }
            this.j = d3;
        }
        if (VERBOSE) Log.e(TAG, "shakeEyeBrow result=" + eyebrowMoved);
        return this.eyebrowMoved;
    }

    public boolean mouthOpenBig() {
        if (VERBOSE) Log.e(TAG, "c");
        if (null == this.pointArray[0]) {
            return false;
        }
        return distanceA(93, 87) > distanceA(90, 84) * 0.8D;
    }

    double distanceA(int paramInt1, int paramInt2) {
        if (VERBOSE)
            Log.d(TAG, "distanceA() called with: paramInt1 = [" + paramInt1 + "], paramInt2 = [" + paramInt2 + "]");
        return Math.sqrt(Math.pow(this.pointArray[0][paramInt1].x - this.pointArray[0][paramInt2].x, 2.0D) +
                Math.pow(this.pointArray[0][paramInt1].y - this.pointArray[0][paramInt2].y, 2.0D));
    }

    public boolean isMouthOpen() {
        if (VERBOSE) Log.e(TAG, "d");
        if (null == this.pointArray[0]) {
            return false;
        }
        float mouthVerticalDistance = (float) distanceA(93, 87);
        float mouthHorizatalDistance = (float) distanceA(90, 84);
        return (mouthVerticalDistance / mouthHorizatalDistance > 0.3D) && (mouthVerticalDistance / mouthHorizatalDistance < 0.5D);
    }
}
