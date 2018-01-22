package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * fres_xxx.idx 文件的索引；文件名:数据块开始位置:数据块长度
 * erduo_003.png:36437:9061;
 *
 * fres_xxx.dat bitmap的数据
 */
public class MResFileIndexReader extends MResFileReaderBase {
    private final String TAG = "MResFileIndexReader";
    private boolean VERBOSE = true;
    int[] mStartPosIndex;
    int[] mDataLenForIndex;

    public MResFileIndexReader(String paramString1, String paramString2) {
        super(paramString1, paramString2);
        if (VERBOSE)
            Log.d(TAG, "MResFileIndexReader() called with: paramString1 = [" + paramString1 + "], paramString2 = [" + paramString2 + "]");
    }

    public void init() throws IOException {
        super.init();

        int i = 0;
        for (Iterator localIterator1 = this.mStartPosMap.keySet().iterator(); localIterator1.hasNext(); ) {
            i = Math.max(extractIndexFromFileName((String) localIterator1.next()), i);
        }
        Object localObject;
        this.mStartPosIndex = new int[i + 1];
        this.mDataLenForIndex = new int[i + 1];
        for (int j = 0; j < this.mStartPosIndex.length; j++) {
            this.mStartPosIndex[j] = -1;
            this.mDataLenForIndex[j] = -1;
        }

        for (Iterator localIterator2 = this.mStartPosMap.entrySet().iterator(); localIterator2.hasNext(); ) {
            localObject = (Entry) localIterator2.next();
            int k = extractIndexFromFileName((String) ((Entry) localObject).getKey());
            if ((k >= 0) && (k < this.mStartPosIndex.length)) {


                this.mStartPosIndex[k] = ((Integer) ((Pair) ((Entry) localObject).getValue()).first).intValue();
                this.mDataLenForIndex[k] = ((Integer) ((Pair) ((Entry) localObject).getValue()).second).intValue();
            }
        }
    }

    int extractIndexFromFileName(String fileName) {
        String str = fileName.substring(fileName.length() - 7).substring(0, 3);

        int i = 0;
        try {
            i = Integer.parseInt(str);
        } catch (Exception localException) {
            Log.e("MergeResFileReader", "parserInt error " + str);
        }
        if (VERBOSE) Log.e(TAG, "extractIndexFromFileName index=" + i+",fileName="+fileName);
        return i;
    }

    public Bitmap loadBitmapAtIndex(int paramInt) {
        if (VERBOSE) Log.d(TAG, "loadBitmapAtIndex() called with: paramInt = [" + paramInt + "]");
        if ((paramInt < 0) || (paramInt >= this.mStartPosIndex.length)) {
            return null;
        }
        int i = this.mStartPosIndex[paramInt];
        int j = this.mDataLenForIndex[paramInt];
        if ((i == -1) || (j == -1)) {
            return null;
        }
        return BitmapFactory.decodeByteArray(this.mDataBuffer.array(), this.mDataBuffer.arrayOffset() + i, j);
    }
}
