package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup;

import java.util.List;

/**
 * Created by Ads on 2017/6/6.
 */

public class GroupData {
    public String name;
    public String glsl;
    public int triggerType;
    public int maxcount;
    public List<Point> pointIndexArray;
    public List<String> resList;
    public int resloadtype;
    public float[] timeparam;
    public String audio;
    public int soundPlayMode;

    public static class Point
    {
        public int x;
        public int y;
    }
}
