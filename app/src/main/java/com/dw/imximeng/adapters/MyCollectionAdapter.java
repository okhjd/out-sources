package com.dw.imximeng.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.LargerImageActivity;
import com.dw.imximeng.activitys.advertisements.UserHomeActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.bean.MyCollection;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.widgets.GridViewNoScroll;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class MyCollectionAdapter extends CommonAdapter<MyCollection> {
    private boolean isComment = false;

    public MyCollectionAdapter(Context context, List<MyCollection> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    public MyCollectionAdapter(Context context, List<MyCollection> mDatas, int itemLayoutId, boolean isComment) {
        super(context, mDatas, itemLayoutId);
        this.isComment = isComment;
    }

    @Override
    public void convert(ViewHolder helper, final MyCollection item) {
        ImageViewRoundOval ivHead = helper.getView(R.id.iv_head);
        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(mContext, 5));//圆角大小
        ImageLoader.getInstance().displayImage(item.getHportrait(), ivHead);
        helper.setText(R.id.tv_name, item.getShowName());
        helper.setText(R.id.tv_time, item.getShowTime());

        TextView tvNew = helper.getView(R.id.tv_new);
        if (item.isIs_new()) {
            tvNew.setVisibility(View.VISIBLE);
        } else {
            tvNew.setVisibility(View.GONE);
        }
        TextView tvTop = helper.getView(R.id.tv_top);
        if (item.getIs_stick() == 1) {
            tvTop.setVisibility(View.VISIBLE);
        } else {
            tvTop.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_phone, item.getShowTelephone());
        helper.setText(R.id.tv_collection, item.getCommentnum() + "");
        TextView tvComment = helper.getView(R.id.tv_comment);
        tvComment.setText(item.getCollectnum() + "");

        if (isComment) {
            tvComment.setVisibility(View.VISIBLE);
        } else {
            tvComment.setVisibility(View.GONE);
        }

        GridViewNoScroll gvImage = helper.getView(R.id.gv_image);
        gvImage.setAdapter(new GvInfoImageAdapter(mContext, item.getImgList(), R.layout.item_image));

        ImageView ivImage = helper.getView(R.id.iv_image);
        if (item.getImgList().size() > 1) {
            gvImage.setAdapter(new GvInfoImageAdapter(mContext, item.getImgList(), R.layout.item_image));
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
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.overlay(mContext, UserHomeActivity.class, item.getUid());
            }
        });
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
        notifyDataSetChanged();
    }
}
