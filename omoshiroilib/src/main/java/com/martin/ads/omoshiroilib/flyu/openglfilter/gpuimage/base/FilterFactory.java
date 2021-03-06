package com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.base;

import android.graphics.PointF;
import android.util.Log;

import com.martin.ads.omoshiroilib.flyu.openglfilter.common.FilterCompat;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.changeface.ChangeFaceInfo;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.decorateface.DecorateFaceBean;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DStickerVignetteBean;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DstickerDataBean;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DstickerDataBeanExt;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.dstickers.DynamicStickerData;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup.GroupData;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.filtergroup.MultiSectionInfo;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.makeup.MakeupData;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.multitriangle.MultiTriangleInfo;
import com.martin.ads.omoshiroilib.flyu.openglfilter.gpuimage.switchface.SwitchFaceInfo;
import com.martin.ads.omoshiroilib.flyu.sdk.utils.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ads on 2017/6/6.
 */

public class FilterFactory {
    private final static String TAG = "FilterFactory";
    private static boolean VERBOSE = true;
    public static final String STICKER_TYPE_DOT = "D";
    public static final String STICKER_TYPE_VIGNETTE = "V";

    public static DynamicStickerData parseStickerJson(String paramString)
            throws JSONException {
        JSONObject localJSONObject1 = new JSONObject(paramString);
        DynamicStickerData localDynamicStickerData = new DynamicStickerData();
        localDynamicStickerData.bS = localJSONObject1.optString("audio");
        localDynamicStickerData.cL = (localJSONObject1.optInt("looping") == 1);
        localDynamicStickerData.cM = localJSONObject1.optString("tips");
        localDynamicStickerData.cN = localJSONObject1.optInt("count", 5);
        localDynamicStickerData.dstickerDataBeanList = new ArrayList();

        JSONArray localJSONArray1 = localJSONObject1.getJSONArray("itemList");
        for (int i = 0; i < localJSONArray1.length(); i++) {
            JSONObject localJSONObject2 = localJSONArray1.getJSONObject(i);
            String str = localJSONObject2.getString("type");
            Object localObject1 = null;
            Object localObject2;
            if ("D".equals(str)) {
                //动图
                localObject2 = new DstickerDataBeanExt();
                localObject1 = localObject2;
                JSONArray localJSONArray2 = localJSONObject2.getJSONArray("alignIndexLst");
                ((DstickerDataBeanExt) localObject2).alignIndexLst = new int[localJSONArray2.length()];
                for (int j = 0; j < localJSONArray2.length(); j++) {
                    ((DstickerDataBeanExt) localObject2).alignIndexLst[j] = localJSONArray2.getInt(j);
                }
                ((DstickerDataBeanExt) localObject2).alignX = localJSONObject2.getInt("alignX");
                ((DstickerDataBeanExt) localObject2).alignY = localJSONObject2.getInt("alignY");
                ((DstickerDataBeanExt) localObject2).scaleWidth = localJSONObject2.getInt("scaleWidth");
                ((DstickerDataBeanExt) localObject2).leftIndex = localJSONObject2.getInt("leftIndex");
                ((DstickerDataBeanExt) localObject2).rightIndex = localJSONObject2.getInt("rightIndex");
            } else {
                //Vignette 小插图
                if (!"V".equals(str)) {
                    continue;
                }
                localObject2 = new DStickerVignetteBean();
                localObject1 = localObject2;
                ((DStickerVignetteBean) localObject2).showTop = (localJSONObject2.getInt("showTop") == 1);
            }
            DstickerDataBean dstickerDataBean = ((DstickerDataBean) localObject1);
            dstickerDataBean.width = localJSONObject2.getInt("width");
            dstickerDataBean.height = localJSONObject2.getInt("height");
            dstickerDataBean.frames = localJSONObject2.getInt("frames");
            dstickerDataBean.folderName = localJSONObject2.getString("folderName");
            dstickerDataBean.frameDuration = localJSONObject2.getInt("frameDuration");
            dstickerDataBean.triggerType = localJSONObject2.getInt("triggerType");
            dstickerDataBean.looping = (localJSONObject2.getInt("looping") == 1);
            dstickerDataBean.showUtilFinish = (localJSONObject2.getInt("showUtilFinish") == 1);
            dstickerDataBean.audio = localJSONObject2.optString("audio");
            dstickerDataBean.alignAudio = (localJSONObject2.optInt("alignAudio", 0) == 1);
            dstickerDataBean.maxcount = 5;

            localDynamicStickerData.dstickerDataBeanList.add(dstickerDataBean);
        }
        return localDynamicStickerData;
    }

    public static ChangeFaceInfo parseChangeFaceJson(String paramString)
            throws JSONException {
        if (VERBOSE) Log.e(TAG, "parseChangeFaceJson");
        ChangeFaceInfo localChangeFaceInfo = new ChangeFaceInfo();
        JSONObject localJSONObject = new JSONObject(paramString);

        JSONArray localJSONArray1 = localJSONObject.getJSONArray("params");
        localChangeFaceInfo.bO = new float[localJSONArray1.length()];
        for (int i = 0; i < localJSONArray1.length(); i++) {
            localChangeFaceInfo.bO[i] = ((float) localJSONArray1.getDouble(i));
        }
        JSONArray localJSONArray2 = localJSONObject.getJSONArray("reslist");
        localChangeFaceInfo.bR = new String[localJSONArray2.length()];
        for (int j = 0; j < localJSONArray2.length(); j++) {
            localChangeFaceInfo.bR[j] = localJSONArray2.getString(j);
        }
        localChangeFaceInfo.bP = localJSONObject.getString("tips");
        localChangeFaceInfo.bQ = localJSONObject.getInt("soundPlayMode");
        localChangeFaceInfo.bS = localJSONObject.optString("audio");
        localChangeFaceInfo.bT = (localJSONObject.optInt("disableEnvFilter", 0) == 1);

        return localChangeFaceInfo;
    }

    public static SwitchFaceInfo parseSwitchFaceJson(String paramString)
            throws JSONException {
        if (VERBOSE) Log.e(TAG, "parseSwitchFaceJson");
        SwitchFaceInfo localSwitchFaceInfo = new SwitchFaceInfo();
        JSONObject localJSONObject = new JSONObject(paramString);

        localSwitchFaceInfo.bP = localJSONObject.getString("tips");
        localSwitchFaceInfo.bQ = localJSONObject.optInt("soundPlayMode");
        localSwitchFaceInfo.bS = localJSONObject.optString("audio");
        localSwitchFaceInfo.cw = localJSONObject.getInt("count");
        localSwitchFaceInfo.cv = new ArrayList();

        JSONArray localJSONArray1 = localJSONObject.getJSONArray("reslist");
        localSwitchFaceInfo.bR = new String[localJSONArray1.length()];
        for (int i = 0; i < localJSONArray1.length(); i++) {
            localSwitchFaceInfo.bR[i] = localJSONArray1.getString(i);
        }
        JSONArray localJSONArray2 = localJSONObject.getJSONArray("pointIndexArray");
        for (int j = 0; j < localJSONArray2.length(); j++) {
            if (j < localSwitchFaceInfo.cw) {
                JSONArray localJSONArray3 = localJSONArray2.getJSONArray(j);
                for (int k = 0; k < localJSONArray3.length(); k++) {
                    SwitchFaceInfo.a locala = new SwitchFaceInfo.a();
                    locala.cx = j;
                    locala.cy = localJSONArray3.getInt(k);
                    localSwitchFaceInfo.cv.add(locala);
                }
            }
        }
        return localSwitchFaceInfo;
    }

    public static DecorateFaceBean parseDecorateFaceJson(String paramString)
            throws JSONException {
        if (VERBOSE) Log.e(TAG, "parseDecorateFaceJson");
        DecorateFaceBean locala = new DecorateFaceBean();
        JSONObject localJSONObject = new JSONObject(paramString);

        locala.bP = localJSONObject.getString("tips");
        locala.cw = localJSONObject.getInt("count");
        locala.cv = new ArrayList();

        JSONArray localJSONArray1 = localJSONObject.getJSONArray("reslist");
        locala.bR = new String[localJSONArray1.length()];
        for (int i = 0; i < localJSONArray1.length(); i++) {
            locala.bR[i] = localJSONArray1.getString(i);
        }
        JSONArray localJSONArray2 = localJSONObject.getJSONArray("pointIndexArray");
        for (int j = 0; j < localJSONArray2.length(); j++) {
            if (j < locala.cw) {
                JSONArray localJSONArray3 = localJSONArray2.getJSONArray(j);
                for (int k = 0; k < localJSONArray3.length(); k++) {
                    DecorateFaceBean.a locala1 = new DecorateFaceBean.a();
                    locala1.cx = j;
                    locala1.cy = localJSONArray3.getInt(k);
                    locala.cv.add(locala1);
                }
            }
        }
        return locala;
    }

    public static ChangeFaceInfo readChangeFaceInfo(String paramString)
            throws IOException, JSONException {
        if (VERBOSE) Log.e(TAG, "readChangeFaceInfo");
        File localFile1 = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile1));
        ChangeFaceInfo localChangeFaceInfo = parseChangeFaceJson(str);

        File localFile2 = new File(paramString, "glsl");
        localChangeFaceInfo.bN = IOUtils.convertStreamToString(new FileInputStream(localFile2));

        return localChangeFaceInfo;
    }

    public static DynamicStickerData readDynamicStickerData(String paramString)
            throws IOException, JSONException {
        if (VERBOSE) Log.e(TAG, "readDynamicStickerData");
        File localFile = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile));
        return parseStickerJson(str);
    }

    public static SwitchFaceInfo readSwitchFaceData(String paramString)
            throws IOException, JSONException {
        if (VERBOSE) Log.e(TAG, "readSwitchFaceData");
        File localFile1 = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile1));
        SwitchFaceInfo localSwitchFaceInfo = parseSwitchFaceJson(str);

        File localFile2 = new File(paramString, "glsl");
        localSwitchFaceInfo.bN = IOUtils.convertStreamToString(new FileInputStream(localFile2));

        return localSwitchFaceInfo;
    }

    public static DecorateFaceBean readDecorateFaceData(String paramString)
            throws IOException, JSONException {
        if (VERBOSE) Log.e(TAG, "readDecorateFaceData");
        File localFile1 = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile1));
        DecorateFaceBean locala = parseDecorateFaceJson(str);
        File localFile2 = new File(paramString, "glsl");
        locala.bN = IOUtils.convertStreamToString(new FileInputStream(localFile2));
        return locala;
    }

    public static MultiSectionInfo parseMultiSectionData(String paramString1, String paramString2)
            throws JSONException, IOException {
        if(VERBOSE) Log.e(TAG,"parseMultiSectionData");
        MultiSectionInfo localMultiSectionInfo = new MultiSectionInfo();

        JSONObject localJSONObject1 = new JSONObject(paramString2);
        JSONArray jsonArray = localJSONObject1.getJSONArray("filterlist");
        localMultiSectionInfo.stringFilterMap = new HashMap();
        Object section;
        Object type;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject filterJSONObject = jsonArray.getJSONObject(i);
            section = new MultiSectionInfo.Filter();
            ((MultiSectionInfo.Filter) section).name = filterJSONObject.getString("name");
            ((MultiSectionInfo.Filter) section).path = (paramString1 + "/" + ((MultiSectionInfo.Filter) section).name);
            ((MultiSectionInfo.Filter) section).reload = (filterJSONObject.getInt("reload") == 1);

            type = filterJSONObject.getString("type");
            if ("dsticker".equals(type)) {
                ((MultiSectionInfo.Filter) section).data = parseDstickerDataBean(filterJSONObject.getJSONObject("data"));
            } else if ("shapechange".equals(type)) {
                ((MultiSectionInfo.Filter) section).data = parseShapeChangeData(paramString1, filterJSONObject.getJSONObject("data"));
            } else if ("makeup".equals(type)) {
                ((MultiSectionInfo.Filter) section).data = parseMakeUpInfo(paramString1, filterJSONObject.getJSONObject("data"));
            }
            localMultiSectionInfo.stringFilterMap.put(((MultiSectionInfo.Filter) section).name, (MultiSectionInfo.Filter) section);
        }
        JSONArray localJSONArray2 = localJSONObject1.getJSONArray("sections");
        localMultiSectionInfo.sectionsMap = new HashMap();
        Object localObject3;
        int m;
        for (int j = 0; j < localJSONArray2.length(); j++) {
            section = new MultiSectionInfo.Section();
            type = localJSONArray2.getJSONObject(j);

            ((MultiSectionInfo.Section) section).sectionname = ((JSONObject) type).getString("sectionname");
            ((MultiSectionInfo.Section) section).tips = ((JSONObject) type).getString("tips");
            ((MultiSectionInfo.Section) section).duration = ((JSONObject) type).getInt("duration");
            ((MultiSectionInfo.Section) section).filterlist = new ArrayList();

            localObject3 = ((JSONObject) type).getJSONArray("filterlist");
            for (m = 0; m < ((JSONArray) localObject3).length(); m++) {
                ((MultiSectionInfo.Section) section).filterlist.add(((JSONArray) localObject3).getString(m));
            }
            localMultiSectionInfo.sectionsMap.put(((MultiSectionInfo.Section) section).sectionname, (MultiSectionInfo.Section) section);
        }
        JSONArray localJSONArray3;
        if (!FilterCompat.noFaceuAssist) {
            localJSONArray3 = localJSONObject1.getJSONArray("statemachine");
        } else {
            localJSONArray3 = localJSONObject1.optJSONArray("statemachine");
            if (null == localJSONArray3) {
                localJSONArray3 = new JSONArray();
            }
        }
        localMultiSectionInfo.stateMachineMap = new HashMap();
        for (int k = 0; k < localJSONArray3.length(); k++) {
            type = localJSONArray3.getJSONObject(k);
            localObject3 = ((JSONObject) type).getString("oldsection");
            m = ((JSONObject) type).getInt("triggerType");
            String str = ((JSONObject) type).getString("newsection");
            int n = ((JSONObject) type).optInt("sectionduration", 0);
            Map localObject4;
            if (localMultiSectionInfo.stateMachineMap.containsKey(localObject3)) {
                localObject4 = (Map) localMultiSectionInfo.stateMachineMap.get(localObject3);
            } else {
                localObject4 = new HashMap();
                localMultiSectionInfo.stateMachineMap.put((String) localObject3, localObject4);
            }
            MultiSectionInfo.SateMachineBean localc = new MultiSectionInfo.SateMachineBean();
            localc.sectionduration = n;
            localc.sectionname = str;
            ((Map) localObject4).put(Integer.valueOf(m), localc);
        }
        localMultiSectionInfo.sectionName = localJSONObject1.getString("initsection");
        return localMultiSectionInfo;
    }

    public static MultiSectionInfo readMultiSectionData(String paramString)
            throws IOException, JSONException {
        if(VERBOSE) Log.e(TAG,"readMultiSectionData");
        File localFile = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile));
        return parseMultiSectionData(paramString, str);
    }

    public static MultiTriangleInfo readMultiTriangleInfo(String paramString)
            throws IOException, JSONException {
        if(VERBOSE) Log.e(TAG,"readMultiTriangleInfo");
        File localFile = new File(paramString, "params.txt");
        String str = IOUtils.convertStreamToString(new FileInputStream(localFile));
        return g(str);
    }

    static MultiTriangleInfo g(String paramString)
            throws JSONException {
        if(VERBOSE) Log.e(TAG,"g");
        MultiTriangleInfo localMultiTriangleInfo = new MultiTriangleInfo();
        JSONObject localJSONObject1 = new JSONObject(paramString);
        localMultiTriangleInfo.eI = new ArrayList();
        localMultiTriangleInfo.bP = localJSONObject1.getString("tips");

        JSONArray localJSONArray1 = localJSONObject1.getJSONArray("itemlist");
        for (int i = 0; i < localJSONArray1.length(); i++) {
            MultiTriangleInfo.a locala = new MultiTriangleInfo.a();
            JSONObject localJSONObject2 = localJSONArray1.getJSONObject(i);

            locala.eq = localJSONObject2.getString("resname");
            JSONArray localJSONArray2 = localJSONObject2.getJSONArray("vertexidx");
            locala.eJ = new int[localJSONArray2.length()];
            for (int j = 0; j < localJSONArray2.length(); j++) {
                locala.eJ[j] = localJSONArray2.getInt(j);
            }
            JSONArray localJSONArray3 = localJSONObject2.getJSONArray("resFacePointsKey");
            locala.eN = new int[localJSONArray3.length()];
            for (int k = 0; k < localJSONArray3.length(); k++) {
                locala.eN[k] = localJSONArray3.getInt(k);
            }
            JSONArray localJSONArray4 = localJSONObject2.getJSONArray("resFacePointsValue");
            locala.eO = new PointF[localJSONArray4.length() / 2];
            for (int m = 0; m < locala.eO.length; m++) {
                locala.eO[m] = new PointF();
                locala.eO[m].x = ((float) localJSONArray4.getDouble(2 * m));
                locala.eO[m].y = ((float) localJSONArray4.getDouble(2 * m + 1));
            }
            JSONArray localJSONArray5 = localJSONObject2.getJSONArray("scaleIdx");
            locala.eK = new int[2];
            locala.eK[0] = localJSONArray5.getInt(0);
            locala.eK[1] = localJSONArray5.getInt(1);

            JSONArray localJSONArray6 = localJSONObject2.getJSONArray("baselineIdx");
            locala.eL = new int[localJSONArray6.length()];
            for (int n = 0; n < localJSONArray6.length(); n++) {
                locala.eL[n] = localJSONArray6.getInt(n);
            }
            JSONArray localJSONArray7 = localJSONObject2.getJSONArray("fakePosScaleRatio");
            locala.eM = new PointF[localJSONArray7.length() / 2];
            for (int i1 = 0; i1 < locala.eM.length; i1++) {
                locala.eM[i1] = new PointF();
                locala.eM[i1].x = ((float) localJSONArray7.getDouble(2 * i1));
                locala.eM[i1].y = ((float) localJSONArray7.getDouble(2 * i1 + 1));
            }
            localMultiTriangleInfo.eI.add(locala);
        }
        return localMultiTriangleInfo;
    }

    static GroupData parseShapeChangeData(String paramString, JSONObject paramJSONObject)
            throws JSONException, IOException {
        GroupData locala = new GroupData();
        if(VERBOSE) Log.e(TAG,"parseShapeChangeData");
        locala.name = paramJSONObject.getString("foldername");
        locala.maxcount = paramJSONObject.getInt("maxcount");
        locala.resloadtype = paramJSONObject.getInt("resloadtype");
        locala.audio = paramJSONObject.getString("audio");
        locala.soundPlayMode = paramJSONObject.getInt("soundPlayMode");
        locala.triggerType = paramJSONObject.getInt("triggerType");

        locala.pointIndexArray = new ArrayList();
        JSONArray localJSONArray1 = paramJSONObject.getJSONArray("pointindexarray");
        for (int i = 0; i < localJSONArray1.length(); i++) {
            JSONArray localJSONArray3 = localJSONArray1.getJSONArray(i);
            for (int k = 0; k < localJSONArray3.length(); k++) {
                GroupData.Point point = new GroupData.Point();
                point.x = i;
                point.y = localJSONArray3.getInt(k);
                locala.pointIndexArray.add(point);
            }
        }
        locala.timeparam = new float[8];
        JSONArray localJSONArray2 = paramJSONObject.getJSONArray("timeparam");
        for (int j = 0; j < 8; j++) {
            locala.timeparam[j] = ((float) localJSONArray2.getDouble(j));
        }
        JSONArray localJSONArray4 = paramJSONObject.getJSONArray("reslist");
        locala.resList = new ArrayList();
        for (int k = 0; k < localJSONArray4.length(); k++) {
            locala.resList.add(localJSONArray4.getString(k));
        }
        File localFile = new File(paramString + "/" + locala.name, "glsl");
        locala.glsl = IOUtils.convertStreamToString(new FileInputStream(localFile));
        return locala;
    }

    static DstickerDataBean parseDstickerDataBean(JSONObject paramJSONObject)
            throws JSONException {
        if(VERBOSE) Log.e(TAG,"parseDstickerDataBean");
        String str = paramJSONObject.getString("type");
        DstickerDataBean localObject1 = null;
        DstickerDataBean localObject2;
        if ("D".equals(str)) {
            localObject2 = new DstickerDataBeanExt();
            localObject1 = localObject2;
            JSONArray localJSONArray = paramJSONObject.getJSONArray("alignIndexLst");
            ((DstickerDataBeanExt) localObject2).alignIndexLst = new int[localJSONArray.length()];
            for (int i = 0; i < localJSONArray.length(); i++) {
                ((DstickerDataBeanExt) localObject2).alignIndexLst[i] = localJSONArray.getInt(i);
            }
            ((DstickerDataBeanExt) localObject2).alignX = paramJSONObject.getInt("alignX");
            ((DstickerDataBeanExt) localObject2).alignY = paramJSONObject.getInt("alignY");
            ((DstickerDataBeanExt) localObject2).scaleWidth = paramJSONObject.getInt("scaleWidth");
            ((DstickerDataBeanExt) localObject2).leftIndex = paramJSONObject.getInt("leftIndex");
            ((DstickerDataBeanExt) localObject2).rightIndex = paramJSONObject.getInt("rightIndex");
        } else if ("V".equals(str)) {
            localObject2 = new DStickerVignetteBean();
            localObject1 = localObject2;
            ((DStickerVignetteBean) localObject2).showTop = (paramJSONObject.getInt("showTop") == 1);
        }
        localObject1.width = paramJSONObject.getInt("width");
        localObject1.height = paramJSONObject.getInt("height");
        localObject1.frames = paramJSONObject.getInt("frames");
        localObject1.folderName = paramJSONObject.getString("folderName");
        localObject1.frameDuration = paramJSONObject.getInt("frameDuration");
        localObject1.triggerType = paramJSONObject.getInt("triggerType");
        localObject1.looping = (paramJSONObject.getInt("looping") == 1);
        localObject1.showUtilFinish = (paramJSONObject.getInt("showUtilFinish") == 1);
        localObject1.audio = paramJSONObject.optString("audio");
        localObject1.alignAudio = (paramJSONObject.optInt("alignAudio", 0) == 1);
        localObject1.maxcount = paramJSONObject.getInt("maxcount");

        return localObject1;
    }

    public static MakeupData parseMakeUpInfo(String paramString, JSONObject paramJSONObject)
            throws IOException, JSONException {
        if(VERBOSE) Log.e(TAG,"parseMakeUpInfo");
        MakeupData locala = new MakeupData();
        locala.resloadtype = paramJSONObject.getInt("resloadtype");
        locala.foldername = paramJSONObject.getString("foldername");
        locala.maxcount = paramJSONObject.getInt("maxcount");
        JSONArray localJSONArray1 = paramJSONObject.getJSONArray("triangles");
        locala.maxcount = Math.min(localJSONArray1.length(), locala.maxcount);

        locala.eo = new ArrayList();
        for (int i = 0; i < locala.maxcount; i++) {
            MakeupData.a locala1 = new MakeupData.a();
            JSONObject localJSONObject = localJSONArray1.getJSONObject(i);

            locala1.res = localJSONObject.getString("res");
            JSONArray localJSONArray2 = localJSONObject.getJSONArray("vertexIndexes");
            locala1.vertexIndexes = new int[localJSONArray2.length()];
            for (int j = 0; j < localJSONArray2.length(); j++) {
                locala1.vertexIndexes[j] = localJSONArray2.getInt(j);
            }
            JSONArray localJSONArray3 = localJSONObject.optJSONArray("facePointOffset");
            if (null != localJSONArray3) {
                if (localJSONArray3.length() % 5 != 0) {
                    throw new JSONException("facePointOffset is not multiple of 5");
                }
                locala1.facePointOffset = new MakeupData.b[localJSONArray3.length() / 5];
                for (int k = 0; k < locala1.facePointOffset.length; k++) {
                    MakeupData.b localb = new MakeupData.b();
                    localb.ew = localJSONArray3.getInt(5 * k);
                    localb.ex = localJSONArray3.getInt(5 * k + 1);
                    localb.ey = ((float) localJSONArray3.getDouble(5 * k + 2));
                    localb.ez = localJSONArray3.getInt(5 * k + 3);
                    localb.eA = ((float) localJSONArray3.getDouble(5 * k + 4));
                    locala1.facePointOffset[k] = localb;
                }
            } else {
                locala1.facePointOffset = new MakeupData.b[0];
            }
            JSONArray localJSONArray4 = localJSONObject.getJSONArray("resFacePoints");
            if (localJSONArray4.length() != 212) {
                throw new JSONException("resFacePoints size is error");
            }
            locala1.et = new PointF[106];
            for (int m = 0; m < 106; m++) {
                locala1.et[m] = new PointF((float) localJSONArray4.getDouble(2 * m), (float) localJSONArray4.getDouble(2 * m + 1));
            }
            locala1.ev = (localJSONObject.optInt("inheritoffset") == 1);
            locala.eo.add(locala1);
        }
        return locala;
    }

    public static MultiTriangleInfo photoInfoToMultiTriangleInfo(String paramString, PointF[] paramArrayOfPointF) {
        if(VERBOSE) Log.e(TAG,"photoInfoToMultiTriangleInfo");
        MultiTriangleInfo localMultiTriangleInfo = new MultiTriangleInfo();
        MultiTriangleInfo.a locala = new MultiTriangleInfo.a();
        locala.eq = IOUtils.extractFileName(paramString);
        locala.eJ = new int[]{0, 52, 2, 0, 34, 52, 52, 34, 74, 74, 34, 43, 43, 34, 41, 43, 41, 77, 77, 41, 61, 61, 41, 32, 61, 32, 30, 8, 2, 52, 8, 52, 84, 84, 52, 82, 82, 52, 74, 82, 74, 43, 82, 43, 46, 46, 43, 83, 83, 43, 77, 83, 77, 61, 83, 61, 90, 90, 61, 24, 24, 61, 30, 84, 82, 97, 97, 82, 46, 97, 46, 99, 99, 46, 83, 99, 83, 90, 12, 8, 84, 12, 84, 14, 14, 84, 16, 16, 84, 103, 16, 103, 101, 16, 101, 90, 16, 90, 18, 18, 90, 20, 20, 90, 24};

        locala.eK = new int[]{2, 30};
        locala.eL = new int[0];
        locala.eM = new PointF[0];

        locala.eN = new int[106];
        locala.eO = new PointF[106];
        PointF[] arrayOfPointF = paramArrayOfPointF;
        for (int i = 0; i < arrayOfPointF.length; i++) {
            locala.eN[i] = i;
            locala.eO[i] = arrayOfPointF[i];
        }
        localMultiTriangleInfo.eI = new ArrayList();
        for (int i = 0; i < 5; i++) {
            localMultiTriangleInfo.eI.add(locala);
        }
        return localMultiTriangleInfo;
    }

    public static boolean isOriginFilter(String paramString) {
        if(VERBOSE) Log.e(TAG,"isOriginFilter");
        String str1 = "//default\n\nprecision mediump float;\nuniform sampler2D inputImageTexture;\nvarying highp vec2 textureCoordinate;\n\n\nvoid main()\n{\n\n    gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n\n}\n\n";

        String str2 = "precision lowp float;\n\nvarying highp vec2 textureCoordinate;\n\nuniform sampler2D inputImageTexture;\nuniform sampler2D inputImageTexture2;\n\nvoid main()\n{\n\ngl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n\n}\n";

        String str3 = paramString.trim();
        return (str1.trim().equals(str3)) || (str2.trim().equals(str3));
    }
}
