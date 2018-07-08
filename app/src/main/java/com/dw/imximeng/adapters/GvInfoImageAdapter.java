package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.ImgListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    public void convert(ViewHolder helper, ImgListBean item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(item.getShowImg(), ivImage);
    }
}
