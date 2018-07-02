package com.dw.imximeng.helper;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author hjd
 * @time 2016-06-17 11:31
 */
public class ImageLoaderUtils {


    public static DisplayImageOptions getDisplayImageOptions(int resDefault){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resDefault)
                .showImageForEmptyUri(resDefault)
                .showImageOnFail(resDefault)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
//            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptionsRound(int resDefault, int round){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resDefault)
                .showImageForEmptyUri(resDefault)
                .showImageOnFail(resDefault)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(round))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptionsNoLoad(int resDefault){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(resDefault)
                .showImageOnFail(resDefault)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
//            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                //.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
//            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }

//    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.mipmap.default_img_5_3)
//            .showImageForEmptyUri(R.mipmap.default_img_5_3)
//            .showImageOnFail(R.mipmap.default_img_5_3)
//            .cacheInMemory(true)
//            .cacheOnDisk(true)
//            .considerExifParams(true)
//            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
//            .bitmapConfig(Bitmap.Config.RGB_565)
////            .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
//            .build();

}
