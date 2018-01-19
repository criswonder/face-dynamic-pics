package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.AbsData;

import java.util.List;
import java.util.Map;

/**
 * Created by Ads on 2017/6/6.
 */

public class MultiSectionInfo extends AbsData {
    public static final String dI = "__empty__";
    public Map<String, Filter> dJ;
    public Map<String, Section> sectionsMap;
    public Map<String, Map<Integer, c>> dL;
    public String dM;

    public String m()
    {
        return "";
    }

    public int n()
    {
        return 5;
    }

    public static class c
    {
        public String dU;
        public long dV;
    }

    public static class Section
    {
        public String sectionname;
        public String tips;
        public int duration;
        public List<String> filterlist;
    }

    public static class Filter
    {
        public String name;
        public boolean reload;
        public String dP;
        public Object data;
    }
}
