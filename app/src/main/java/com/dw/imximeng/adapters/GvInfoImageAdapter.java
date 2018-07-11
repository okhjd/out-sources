package com.dw.imximeng.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.LargerImageActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.ImgListBean;
import com.dw.imximeng.helper.ActivityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class GvInfoImageAdapter extends CommonAdapter<ImgListBean> {

    public GvInfoImageAdapter(Context context, List<ImgListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(final ViewHolder helper, ImgListBean item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(item.getShowImg(), ivImage);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for (int i=0;i<getCount();i++){
                    list.add(getItem(i).getShowImg());
                }

                Bundle bundle = new Bundle();
                bundle.putInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION, helper.getPosition());
                bundle.putSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST,list);
                ActivityUtils.overlay(mContext, LargerImageActivity.class, bundle);
            }
        });
    }
}
