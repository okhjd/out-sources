package com.dw.imximeng.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * http://blog.csdn.net/lmj623565791/article/details/38965311
 */

/**
 * @author: Dean
 * @date: 2017-11-8
 * @Description: 常用单位转换的辅助类
 */
public class MaDensityUtils {

    private MaDensityUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal)
    {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpVal * scale + 0.5f);
	}

	public  static float dp2pix(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f) ;

	}

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal)
    {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spVal * fontScale + 0.5f);
	}

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxVal / scale + 0.5f);
	}

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal)
    {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxVal / fontScale + 0.5f);
	}
    
	/**
	 * 修改普通View的高<br>
	 * Adapter---getView方法中慎用
	 */
	public static void changeH(View v, int H) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.height = H;
		v.setLayoutParams(params);
	}

	/**
	 * 修改普通View的宽<br>
	 * Adapter---getView方法中慎用
	 */
	public static void changeW(View v, int W) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.width = W;
		v.setLayoutParams(params);
	}

	/**
	 * 修改控件的宽高<br>
	 * Adapter---getView方法中慎用
	 * 
	 * @param v
	 *            控件
	 * @param W
	 *            宽度
	 * @param H
	 *            高度
	 */
	public static void changeWH(View v, int W, int H) {
		LayoutParams params = (LayoutParams) v.getLayoutParams();
		params.width = W;
		params.height = H;
		v.setLayoutParams(params);
	}
}
