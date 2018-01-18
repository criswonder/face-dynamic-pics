package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers;

import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup.GPUImageFilterGroup;

/**
 * Created by Ads on 2017/6/6.
 */

public class DynamicStickerMulti extends GPUImageFilterGroup {
    static final String TAG = "DynamicStickerMulti";
    String mFileDir;
    DynamicStickerData dc;

    public DynamicStickerMulti(String fileDir, DynamicStickerData dynamicStickerData) {
        this.mFileDir = fileDir;
        this.dc = dynamicStickerData;
        for (DstickerDataBean stickerBean : dynamicStickerData.dstickerDataBeanList) {
            String str = "file://" + fileDir + "/" + stickerBean.folderName;
            if ((stickerBean instanceof DstickerDataBeanExt)) {
                addFilter(new DynamicStickerDot(str, (DstickerDataBeanExt) stickerBean));
            } else if ((stickerBean instanceof DStickerVignetteBean)) {
                addFilter(new DynamicStickerVignette(str, (DStickerVignetteBean) stickerBean));
            }
        }
    }
    public void releaseNoGLESRes() {
        super.releaseNoGLESRes();
    }
}
