package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

/**
 * Created by Ads on 2017/6/6.
 */

public class UnnamedD implements Runnable {
    private GPUImageFilter gpuImageFilter;
    private int bl,bm,bn;
    UnnamedD(GPUImageFilter gpuImageFilter1, int var2, int var3, int var4) {
        this.gpuImageFilter = gpuImageFilter1;
        this.bl = var2;
        this.bm = var3;
        this.bn = var4;
    }

    public void run() {
        this.gpuImageFilter.bi = this.bl;
        this.gpuImageFilter.bj = this.bm;
        this.gpuImageFilter.bk = this.bn;
    }
}
