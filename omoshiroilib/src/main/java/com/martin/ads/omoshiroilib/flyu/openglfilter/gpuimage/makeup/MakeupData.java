package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.makeup;

import android.graphics.PointF;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.AbsData;

import java.util.List;

/**
 * Created by Ads on 2017/6/6.
 */

public class MakeupData{
    public int maxcount;
    public String foldername;
    public List<a> eo;
    public int resloadtype;

    public static class b
    {
        public int ew;
        public int ex;
        public float ey;
        public int ez;
        public float eA;
    }

    public static class a
    {
        public String res;
        public int[] vertexIndexes;
        public b[] facePointOffset;
        public PointF[] et;
        public PointF[] eu;
        public boolean ev;
    }
}
