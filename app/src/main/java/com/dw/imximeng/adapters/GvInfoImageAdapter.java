package com.dw.imximeng.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dw.imximeng.MainActivity;
import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class GvInfoImageAdapter extends CommonAdapter<Information.ImgListBean> {

    public GvInfoImageAdapter(Context context, List<Information.ImgListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Information.ImgListBean item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(item.getShowImg(), ivImage);
    }
}
