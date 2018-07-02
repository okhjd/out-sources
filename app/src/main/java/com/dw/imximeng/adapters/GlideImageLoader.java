package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.dw.imximeng.bean.UserIndexData;
import com.youth.banner.loader.ImageLoader;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        UserIndexData.LbListBean item = ( UserIndexData.LbListBean)path;
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(item.getImg(), imageView);
    }
}
