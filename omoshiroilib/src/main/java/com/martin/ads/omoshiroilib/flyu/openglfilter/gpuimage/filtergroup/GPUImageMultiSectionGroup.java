package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup;

import com.martin.ads.omoshiroilib.flyu.openglfilter.common.FilterCompat;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilter;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterGroupBase;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DStickerVignetteBean;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DstickerDataBeanExt;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DynamicStickerDot;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DynamicStickerVignette;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.makeup.MakeUpFilter;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.makeup.MakeupData;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.MiscUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageMultiSectionGroup extends GPUImageFilterGroupBase {
    static final String TAG = "GPUImageMultiSectionGroup";
    String resFolderPath;
    MultiSectionInfo mMultiSectionInfo;
    Map<String, GPUImageFilter> stringGPUImageFilterMap;
    List<GPUImageFilter> dE;
    List<GPUImageFilter> dF;
    String dG;
    long dH = -1L;
    List<GPUImageFilter> gpuImageFilters;

    public GPUImageMultiSectionGroup(String paramString, MultiSectionInfo paramMultiSectionInfo) {
        this.resFolderPath = paramString;
        this.mMultiSectionInfo = paramMultiSectionInfo;
        this.stringGPUImageFilterMap = new HashMap();

        this.dE = new ArrayList();
        this.dF = new ArrayList();
        this.gpuImageFilters = new ArrayList();
    }

    public void onInit() {
        super.onInit();
        for (Map.Entry localEntry : this.mMultiSectionInfo.stringFilterMap.entrySet()) {
            MultiSectionInfo.Filter filter = (MultiSectionInfo.Filter) localEntry.getValue();
            Object filterData = filter.data;
            Object localObject2;
            if ((filterData instanceof DstickerDataBeanExt)) {
                localObject2 = new DynamicStickerDot("file://" + ((MultiSectionInfo.Filter) localEntry.getValue()).path, (DstickerDataBeanExt) filterData);
            } else if ((filterData instanceof DStickerVignetteBean)) {
                localObject2 = new DynamicStickerVignette("file://" + ((MultiSectionInfo.Filter) localEntry.getValue()).path, (DStickerVignetteBean) filterData);
            } else {
                Object localObject3;
                Object localObject4;
                if ((filterData instanceof GroupData)) {
                    localObject3 = (GroupData) filterData;
                    localObject4 = new ShapeChangeFilter(((MultiSectionInfo.Filter) localEntry.getValue()).path, (GroupData) filterData);
                    if (2 == ((GroupData) localObject3).resloadtype) {
                        ((ShapeChangeFilter) localObject4).setRestypeTo2();
                        ((ShapeChangeFilter) localObject4).init();
                    }
                    localObject2 = localObject4;
                } else if ((filterData instanceof MakeupData)) {
                    localObject3 = (MakeupData) filterData;
                    localObject4 = new MakeUpFilter(((MultiSectionInfo.Filter) localEntry.getValue()).path, (MakeupData) localObject3);
                    if (2 == ((MakeupData) localObject3).resloadtype) {
                        ((MakeUpFilter) localObject4).setRestypeTo2();
                        ((MakeUpFilter) localObject4).init();
                    }
                    localObject2 = localObject4;
                } else {
                    localObject2 = new GPUImageFilter();
                }
            }
            this.stringGPUImageFilterMap.put((String) localEntry.getKey(), (GPUImageFilter) localObject2);
        }
        this.stringGPUImageFilterMap.put("__empty__", new GPUImageFilter());
        for (int i = 0; i < this.dE.size(); i++) {
            ((GPUImageFilter) this.dE.get(i)).init();
        }
        for (int i = 0; i < this.dF.size(); i++) {
            ((GPUImageFilter) this.dF.get(i)).init();
        }
        this.dG = this.mMultiSectionInfo.sectionName;
        this.dH = System.currentTimeMillis();
        P();
        Q();
    }

    void P() {
        ArrayList localArrayList = new ArrayList();
        for (Object localObject1 = this.dE.iterator(); ((Iterator) localObject1).hasNext(); ) {
            GPUImageFilter localObject2 = (GPUImageFilter) ((Iterator) localObject1).next();
            if ((localObject2 instanceof GPUImageFilterGroup)) {
                ((GPUImageFilterGroup) localObject2).copyFilterList();
                List<GPUImageFilter> localList = ((GPUImageFilterGroup) localObject2).getGPUFilterList();
                if ((localList != null) && (!localList.isEmpty())) {
                    localArrayList.addAll(localList);
                }
            } else {
                localArrayList.add(localObject2);
            }
        }
        List localList;
        MultiSectionInfo.Section localObject1 = (MultiSectionInfo.Section) this.mMultiSectionInfo.sectionsMap.get(this.dG);
        for (Object localObject2 = ((MultiSectionInfo.Section) localObject1).filterlist.iterator(); ((Iterator) localObject2).hasNext(); ) {
            String localObject3 = (String) ((Iterator) localObject2).next();
            GPUImageFilter localGPUImageFilter = (GPUImageFilter) this.stringGPUImageFilterMap.get(localObject3);
            MultiSectionInfo.Filter locala = (MultiSectionInfo.Filter) this.mMultiSectionInfo.stringFilterMap.get(localObject3);

            localArrayList.add(localGPUImageFilter);
            if (!localGPUImageFilter.isInitialized()) {
                localGPUImageFilter.init();
                localGPUImageFilter.onOutputSizeChanged(this.surfaceWidth, this.surfaceHeight);
            }
            if ((null != locala) && (locala.reload)) {
                localGPUImageFilter.resetDrawStartTimeStamp();
            }
            if (this.pause) {
                localGPUImageFilter.pause();
            } else {
                localGPUImageFilter.resume();
            }
            localGPUImageFilter.setPhoneDirection(this.phoneDirection);
        }
        GPUImageFilter localGPUImageFilter;
        for (Iterator localObject2 = this.dF.iterator(); ((Iterator) localObject2).hasNext(); ) {
            Object localObject3 = (GPUImageFilter) ((Iterator) localObject2).next();
            if ((localObject3 instanceof GPUImageFilterGroup)) {
                ((GPUImageFilterGroup) localObject3).copyFilterList();
                localList = ((GPUImageFilterGroup) localObject3).getGPUFilterList();
                if ((localList != null) && (!localList.isEmpty())) {
                    localArrayList.addAll(localList);
                }
            } else {
                localArrayList.add(localObject3);
            }
        }
        int i = 0;
        for (Object localObject3 = localArrayList.iterator(); ((Iterator) localObject3).hasNext(); ) {
            localGPUImageFilter = (GPUImageFilter) ((Iterator) localObject3).next();
            localGPUImageFilter.c(i % 2 == 1);
            i++;
        }
        for (Iterator localObject3 = this.gpuImageFilters.iterator(); ((Iterator) localObject3).hasNext(); ) {
            localGPUImageFilter = (GPUImageFilter) ((Iterator) localObject3).next();
            if (!localArrayList.contains(localGPUImageFilter)) {
                if (localGPUImageFilter.B()) {
                    localGPUImageFilter.destroy();
                } else {
                    localGPUImageFilter.releaseNoGLESRes();
                }
            }
        }
        this.gpuImageFilters.clear();
        this.gpuImageFilters = localArrayList;
    }

    void Q() {
        int i = 0;
        for (Object localObject = this.gpuImageFilters.iterator(); ((Iterator) localObject).hasNext(); ) {
            GPUImageFilter localGPUImageFilter = (GPUImageFilter) ((Iterator) localObject).next();
            if (localGPUImageFilter.n() > i) {
                i = localGPUImageFilter.n();
            }
        }
        if (null != this.bJ) {
            MultiSectionInfo.Section localObject = (MultiSectionInfo.Section) this.mMultiSectionInfo.sectionsMap.get(this.dG);
            this.bJ.onTipsAndCountChanged(i, ((MultiSectionInfo.Section) localObject).tips, -1 == ((MultiSectionInfo.Section) localObject).duration ? 65535 : ((MultiSectionInfo.Section) localObject).duration);
        }
    }

    public void onDestroy() {
        for (Iterator localIterator = this.dE.iterator(); localIterator.hasNext(); ) {
            GPUImageFilter localObject = (GPUImageFilter) localIterator.next();
            ((GPUImageFilter) localObject).destroy();
        }
        Object localObject;
        Iterator localIterator;
        for (localIterator = this.dF.iterator(); localIterator.hasNext(); ) {
            localObject = (GPUImageFilter) localIterator.next();
            ((GPUImageFilter) localObject).destroy();
        }
        for (localIterator = this.stringGPUImageFilterMap.entrySet().iterator(); localIterator.hasNext(); ) {
            localObject = (Map.Entry) localIterator.next();
            ((GPUImageFilter) ((Map.Entry) localObject).getValue()).destroy();
        }
        super.onDestroy();
    }

    public void releaseNoGLESRes() {
        this.bK = null;
        for (Object localObject1 = this.dE.iterator(); ((Iterator) localObject1).hasNext(); ) {
            GPUImageFilter localObject2 = (GPUImageFilter) ((Iterator) localObject1).next();
            ((GPUImageFilter) localObject2).releaseNoGLESRes();
        }
        Object localObject2;
        Iterator localObject1;
        for (localObject1 = this.dF.iterator(); ((Iterator) localObject1).hasNext(); ) {
            localObject2 = (GPUImageFilter) ((Iterator) localObject1).next();
            ((GPUImageFilter) localObject2).releaseNoGLESRes();
        }
        for (localObject1 = this.stringGPUImageFilterMap.entrySet().iterator(); ((Iterator) localObject1).hasNext(); ) {
            localObject2 = (Map.Entry) ((Iterator) localObject1).next();
            ((GPUImageFilter) ((Map.Entry) localObject2).getValue()).releaseNoGLESRes();
        }

        super.releaseNoGLESRes();
    }

    protected void beforeGroupDraw() {
        super.beforeGroupDraw();

        Map localMap = (Map) this.mMultiSectionInfo.stateMachineMap.get(this.dG);
        if ((!FilterCompat.noFaceuAssist) && (null == localMap)) {
            throw new RuntimeException("section state is null");
        }
        if (null != localMap) {
            String str = null;
            MultiSectionInfo.SateMachineBean localc;
            if (this.facePointWrapper.mouthOpenBig()) {
                localc = (MultiSectionInfo.SateMachineBean) localMap.get(Integer.valueOf(0));
                str = null == localc ? null : localc.sectionname;
            } else if (this.facePointWrapper.shakeEyeBrow()) {
                localc = (MultiSectionInfo.SateMachineBean) localMap.get(Integer.valueOf(1));
                str = null == localc ? null : localc.sectionname;
            } else if (this.facePointWrapper.isMouthOpen()) {
                localc = (MultiSectionInfo.SateMachineBean) localMap.get(Integer.valueOf(3));
                str = null == localc ? null : localc.sectionname;
            } else if (this.facePointWrapper.faceCount > 0) {
                localc = (MultiSectionInfo.SateMachineBean) localMap.get(Integer.valueOf(2));
                str = null == localc ? null : localc.sectionname;
            }
            if (localMap.containsKey(Integer.valueOf(4))) {
                localc = (MultiSectionInfo.SateMachineBean) localMap.get(Integer.valueOf(4));
                if (System.currentTimeMillis() - this.dH > localc.sectionduration) {
                    str = localc.sectionname;
                }
            }
            if ((!MiscUtils.isNilOrNull(str)) && (!str.equals(this.dG))) {
                this.dG = str;
                this.dH = System.currentTimeMillis();
                P();
                Q();
            }
        }
    }

    public List<GPUImageFilter> groupGPUFilterList() {
        return this.gpuImageFilters;
    }

    public void addFilter(GPUImageFilter paramGPUImageFilter) {
        this.dE.add(paramGPUImageFilter);
    }

    public void c(GPUImageFilter paramGPUImageFilter) {
        this.dF.add(paramGPUImageFilter);
    }

    public void pause() {
        super.pause();
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilters) {
            localGPUImageFilter.pause();
        }
    }

    public void resume() {
        super.resume();
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilters) {
            localGPUImageFilter.resume();
        }
    }
}
