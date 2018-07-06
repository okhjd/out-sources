package com.dw.imximeng.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @time 2017-11-03 13:35
 */

public final class ActivityUtils {
    public final static String PARCELABLE_EXTRA_KEY = "extra:parcelable_key";
    public final static String SERIALIZABLE_EXTRA_KEY = "extra:serializable_key";
    public final static String STRING_EXTRA_KEY = "extra:string_key";
    public final static String BOOLEAN_EXTRA_KEY = "extra:boolean_key";

    public static <T> T getParcelableExtra(Activity activity) {
        Parcelable parcelable = activity.getIntent().getParcelableExtra(PARCELABLE_EXTRA_KEY);
        activity = null;
        return (T) parcelable;
    }

    public static <T> T getSerializableExtra(Activity activity) {
        Serializable serializable = activity.getIntent().getSerializableExtra(SERIALIZABLE_EXTRA_KEY);
        activity = null;
        return (T) serializable;
    }

    public static <T> T getSerializableExtra(Intent intent) {
        Serializable serializable = intent.getSerializableExtra(SERIALIZABLE_EXTRA_KEY);
        return (T) serializable;
    }

    public static <T> T getParcelableExtra(Intent intent) {
        Parcelable parcelable = intent.getParcelableExtra(PARCELABLE_EXTRA_KEY);
        return (T) parcelable;
    }

    public static String getStringExtra(Activity activity) {
        String string = activity.getIntent().getStringExtra(STRING_EXTRA_KEY);
        activity = null;
        return string;
    }

    public static String getStringExtra(Intent intent) {
        return intent.getStringExtra(STRING_EXTRA_KEY);
    }

    public static boolean getBooleanExtra(Activity activity) {
        boolean booleanExtra = activity.getIntent().getBooleanExtra(BOOLEAN_EXTRA_KEY, false);
        activity = null;
        return booleanExtra;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, String extra) {
        Intent intent = new Intent(context, targetClazz);
        intent.putExtra(STRING_EXTRA_KEY, extra);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, String extra, Boolean booleanExtra) {
        Intent intent = new Intent(context, targetClazz);
        intent.putExtra(STRING_EXTRA_KEY, extra);
        intent.putExtra(BOOLEAN_EXTRA_KEY, booleanExtra);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, int flags) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, int flags, ArrayList<? extends Parcelable> parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        putParcelableArrayListExtra(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz, Serializable serializable) {
        Intent intent = new Intent(context, targetClazz);
        putSerializableExtra(intent, serializable);
        context.startActivity(intent);
        context = null;
    }

    public static void overlay(Context context, Class<? extends Activity> targetClazz) {
        Intent intent = new Intent(context, targetClazz);
        context.startActivity(intent);
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz, int flags) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        intent.putExtra(PARCELABLE_EXTRA_KEY, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        putParcelableExtra(intent, parcelable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz, Serializable serializable) {
        Intent intent = new Intent(context, targetClazz);
        putSerializableExtra(intent, serializable);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz, String extra) {
        Intent intent = new Intent(context, targetClazz);
        intent.putExtra(STRING_EXTRA_KEY, extra);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void forward(Context context, Class<? extends Activity> targetClazz) {
        Intent intent = new Intent(context, targetClazz);
        context.startActivity(intent);
        if (isActivity(context)) return;
        ((Activity) context).finish();
        context = null;
    }

    public static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        ((Activity) context).startActivityForResult(intent, flags);
        context = null;
    }

    public static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        putParcelableExtra(intent, parcelable);
        ((Activity) context).startActivityForResult(intent, flags);
        context = null;
    }

    public static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags, Serializable serializable) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        putSerializableExtra(intent, serializable);
        ((Activity) context).startActivityForResult(intent, flags);
        context = null;
    }

    public static void startForResult(Context context, Class<? extends Activity> targetClazz, int flags, String extra) {
        Intent intent = new Intent(context, targetClazz);
        if (isActivity(context)) return;
        intent.putExtra(STRING_EXTRA_KEY, extra);
        ((Activity) context).startActivityForResult(intent, flags);
        context = null;
    }

    public static void setResult(Context context, int flags, String string) {
        Intent intent = new Intent();
        setFlags(intent, flags);
        intent.putExtra(STRING_EXTRA_KEY, string);
        if (isActivity(context)) return;
        ((Activity) context).setResult(flags, intent);
        ((Activity) context).finish();
    }

    public static void setResult(Context context, int flags, Serializable serializable) {
        Intent intent = new Intent();
        setFlags(intent, flags);
        putSerializableExtra(intent, serializable);
        if (isActivity(context)) return;
        ((Activity) context).setResult(flags, intent);
        ((Activity) context).finish();
    }

    public static void setResult(Context context, int flags, Parcelable parcelable) {
        Intent intent = new Intent();
        setFlags(intent, flags);
        putParcelableExtra(intent, parcelable);
        if (isActivity(context)) return;
        ((Activity) context).setResult(flags, intent);
        ((Activity) context).finish();
    }

    public static void setResult(Context context, Class<? extends Activity> targetClazz, int flags, Parcelable parcelable) {
        Intent intent = new Intent(context, targetClazz);
        setFlags(intent, flags);
        putParcelableExtra(intent, parcelable);
        if (isActivity(context)) return;
        ((Activity) context).setResult(flags, intent);
        ((Activity) context).finish();
    }

    private static boolean isActivity(Context context) {
        return !(context instanceof Activity);
    }

    private static void setFlags(Intent intent, int flags) {
        if (flags < 0) return;
        intent.setFlags(flags);
    }

    private static void putParcelableExtra(Intent intent, Parcelable parcelable) {
        if (parcelable == null) return;
        intent.putExtra(PARCELABLE_EXTRA_KEY, parcelable);
    }

    private static void putParcelableArrayListExtra(Intent intent,ArrayList<? extends Parcelable> parcelable) {
        if (parcelable == null) return;
        intent.putParcelableArrayListExtra(PARCELABLE_EXTRA_KEY, parcelable);
    }

    private static void putSerializableExtra(Intent intent, Serializable serializable) {
        if (serializable == null) return;
        intent.putExtra(SERIALIZABLE_EXTRA_KEY, serializable);
    }
}
