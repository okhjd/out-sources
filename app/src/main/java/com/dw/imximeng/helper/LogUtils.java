package com.dw.imximeng.helper;

import android.util.Log;

/**
 * @author hjd
 * @time 2017-11-06 15:40
 */

public class LogUtils {
    private final static String TAG = "LogUtils";

    public static void logError(String str){
        logError(TAG, str);
    }

    public static void logDebug(String str){
        logDebug(TAG, str);
    }

    public static void logInfo(String str){
        logInfo(TAG, str);
    }

    public static void logVerbose(String str){
        logVerbose(TAG, str);
    }

    public static void logWarn(String str){
        logWarn(TAG, str);
    }

    public static void logError(String tag, String str){
        Log.e(tag, str);
    }

    public static void logDebug(String tag, String str){
        Log.d(tag, str);
    }

    public static void logInfo(String tag, String str){
        Log.i(tag, str);
    }

    public static void logVerbose(String tag, String str){
        Log.v(tag, str);
    }

    public static void logWarn(String tag, String str){
        Log.w(tag, str);
    }
}
