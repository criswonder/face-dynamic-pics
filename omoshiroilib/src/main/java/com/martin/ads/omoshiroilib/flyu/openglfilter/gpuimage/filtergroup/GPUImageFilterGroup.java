package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup;

import com.martin.ads.omoshiroilib.flyu.openglfilter.common.FilterCompat;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilter;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base.GPUImageFilterGroupBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ads on 2017/6/6.
 */

public class GPUImageFilterGroup extends GPUImageFilterGroupBase
{
    protected List<GPUImageFilter> gpuImageFilterList;
    protected List<GPUImageFilter> gpuImageFilterList1;

    public GPUImageFilterGroup()
    {
        this.gpuImageFilterList = new ArrayList();
        this.gpuImageFilterList1 = new ArrayList();
    }

    public List<GPUImageFilter> groupGPUFilterList()
    {
        return this.gpuImageFilterList1;
    }

    public void addFilter(GPUImageFilter paramGPUImageFilter)
    {
        if (paramGPUImageFilter == null) {
            return;
        }
        this.gpuImageFilterList.add(paramGPUImageFilter);
        copyFilterList();
    }

    public void onInit()
    {
        super.onInit();
        for (int i = 0; i < this.gpuImageFilterList1.size(); i++)
        {
            ((GPUImageFilter)this.gpuImageFilterList1.get(i)).init();
            ((GPUImageFilter)this.gpuImageFilterList1.get(i)).c(i % 2 == 1);
        }
    }

    public void onDestroy()
    {
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilterList1) {
            localGPUImageFilter.destroy();
        }
        super.onDestroy();
    }

    public void releaseNoGLESRes()
    {
        Iterator var1 = this.gpuImageFilterList1.iterator();

        GPUImageFilter var2;
        while(var1.hasNext()) {
            var2 = (GPUImageFilter)var1.next();
            var2.releaseNoGLESRes();
        }
        if(FilterCompat.saveParamsOnRelease) {
            var1 = this.gpuImageFilterList.iterator();

            while(var1.hasNext()) {
                var2 = (GPUImageFilter)var1.next();
                var2.releaseNoGLESRes();
            }
        }

        super.releaseNoGLESRes();
    }

    public void pause()
    {
        super.pause();
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilterList1) {
            localGPUImageFilter.pause();
        }
    }

    public void resume()
    {
        super.resume();
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilterList1) {
            localGPUImageFilter.resume();
        }
    }

    public List<GPUImageFilter> getGPUFilterList()
    {
        return this.gpuImageFilterList1;
    }

    public void copyFilterList()
    {
        if (this.gpuImageFilterList == null) {
            return;
        }
        this.gpuImageFilterList1.clear();
        for (GPUImageFilter localGPUImageFilter : this.gpuImageFilterList) {
            if ((localGPUImageFilter instanceof GPUImageFilterGroup))
            {
                ((GPUImageFilterGroup)localGPUImageFilter).copyFilterList();
                List localList = ((GPUImageFilterGroup)localGPUImageFilter).getGPUFilterList();
                if ((localList != null) && (!localList.isEmpty())) {
                    this.gpuImageFilterList1.addAll(localList);
                }
            }
            else
            {
                this.gpuImageFilterList1.add(localGPUImageFilter);
            }
        }
    }
}

