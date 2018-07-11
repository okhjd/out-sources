package com.dw.imximeng.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.LargerImageActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.MyRelease;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.widgets.GridViewNoScroll;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class MyReleaseAdapter extends CommonAdapter<MyRelease> {

    private OnDeteleInfoListener onDeteleInfoListener;

    public MyReleaseAdapter(Context context, List<MyRelease> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, final MyRelease item) {

        helper.setText(R.id.tv_content, item.getContent());
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvStatus = helper.getView(R.id.tv_status);

        helper.setText(R.id.tv_collection, item.getCommentnum() + "");

        TextView tvComment = helper.getView(R.id.tv_comment);
        tvComment.setText(item.getCollectnum() + "");

        GridViewNoScroll gvImage = helper.getView(R.id.gv_image);

        ImageView ivImage = helper.getView(R.id.iv_image);
        if (item.getImgList().size() > 1) {
            gvImage.setAdapter(new GvInfoImageAdapter(mContext, item.getImgList(), R.layout.item_gridview_release_image));
            ivImage.setVisibility(View.GONE);
            gvImage.setVisibility(View.VISIBLE);
        } else if (item.getImgList().size() == 1) {
            ImageLoader.getInstance().displayImage(item.getImgList().get(0).getShowImg(), ivImage);
            ivImage.setVisibility(View.VISIBLE);
            gvImage.setVisibility(View.GONE);
        } else {
            ivImage.setVisibility(View.GONE);
            gvImage.setVisibility(View.GONE);
        }

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION, 0);
                bundle.putSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST,
                        new ArrayList<String>().add(item.getImgList().get(0).getShowImg()));
                ActivityUtils.overlay(mContext, LargerImageActivity.class, bundle);
            }
        });

        TextView tvDetele = helper.getView(R.id.tv_delete);
        if (item.getSh_status() == 0) {
            tvDetele.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(item.getSh_remarks());
        } else if (item.getSh_status() == 2) {
            tvDetele.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(item.getSh_remarks());
        } else {
            tvDetele.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.GONE);
            tvTime.setText(item.getShowShtime());
        }

        tvDetele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeteleInfoListener.onDeteleInfo(item);
            }
        });
    }

    public interface OnDeteleInfoListener {
        void onDeteleInfo(MyRelease item);
    }

    public OnDeteleInfoListener getOnDeteleInfoListener() {
        return onDeteleInfoListener;
    }

    public void setOnDeteleInfoListener(OnDeteleInfoListener onDeteleInfoListener) {
        this.onDeteleInfoListener = onDeteleInfoListener;
    }
}
