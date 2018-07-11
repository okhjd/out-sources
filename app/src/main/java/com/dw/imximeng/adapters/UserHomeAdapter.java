package com.dw.imximeng.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.activitys.LargerImageActivity;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.UserHome;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 * 待优化
 */
public class UserHomeAdapter extends CommonAdapter<UserHome.ListBean> {
    public UserHomeAdapter(Context context, List<UserHome.ListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(final ViewHolder helper, final UserHome.ListBean item) {
        helper.setText(R.id.tv_day, item.getShowDay());
        helper.setText(R.id.tv_month, item.getShowMonth());
        ImageView imageview = helper.getView(R.id.imageview);
        ImageView ivTwo1 = helper.getView(R.id.iv_two_1);
        ImageView ivTwo2 = helper.getView(R.id.iv_two_2);
        ImageView ivThree1 = helper.getView(R.id.iv_three_1);
        ImageView ivThree2 = helper.getView(R.id.iv_three_2);
        ImageView ivThree3 = helper.getView(R.id.iv_three_3);

        LinearLayout llTwoImage = helper.getView(R.id.ll_image);
        LinearLayout llThreeImage = helper.getView(R.id.ll_image_all);

        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setText(item.getContent());

        TextView tvImgNum = helper.getView(R.id.tv_img_num);

        if (item.getImgList().isEmpty()) {
            imageview.setVisibility(View.GONE);
            llTwoImage.setVisibility(View.GONE);
            llThreeImage.setVisibility(View.GONE);
            tvImgNum.setVisibility(View.GONE);

            tvContent.setBackgroundColor(Color.parseColor("#f3f3f5"));
            tvContent.setPadding(MaDensityUtils.dp2px(mContext, 8), 0,
                    MaDensityUtils.dp2px(mContext, 8), 0);
            tvContent.setGravity(Gravity.CENTER_VERTICAL);
            tvContent.setLines(1);
            tvContent.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            imageview.setVisibility(View.VISIBLE);
            tvContent.setBackgroundColor(Color.WHITE);
            tvContent.setLines(Integer.MAX_VALUE);
            tvContent.setGravity(Gravity.TOP);
            tvContent.setPadding(0, 0, 0, 0);

            if (item.getImgList().size() == 1) {
                imageview.setVisibility(View.VISIBLE);
                llTwoImage.setVisibility(View.GONE);
                llThreeImage.setVisibility(View.GONE);
                tvImgNum.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(item.getImgList().get(0).getShowImg(), imageview);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION, 0);
                        bundle.putSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST,
                                new ArrayList<String>().add(item.getImgList().get(0).getShowImg()));
                        ActivityUtils.overlay(mContext, LargerImageActivity.class, bundle);
                    }
                });

            } else if (item.getImgList().size() == 2) {
                imageview.setVisibility(View.GONE);
                llTwoImage.setVisibility(View.VISIBLE);
                llThreeImage.setVisibility(View.GONE);
                tvImgNum.setVisibility(View.VISIBLE);
                tvImgNum.setText("共" + item.getImgList().size() + "张");

                ImageLoader.getInstance().displayImage(item.getImgList().get(0).getShowImg(), ivTwo1);
                ImageLoader.getInstance().displayImage(item.getImgList().get(1).getShowImg(), ivTwo2);

                llTwoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < getItem(helper.getPosition()).getImgList().size(); i++) {
                            list.add(getItem(helper.getPosition()).getImgList().get(i).getShowImg());
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION, 0);
                        bundle.putSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST, list);
                        ActivityUtils.overlay(mContext, LargerImageActivity.class, bundle);
                    }
                });
            } else if (item.getImgList().size() == 3) {
                imageview.setVisibility(View.GONE);
                llTwoImage.setVisibility(View.GONE);
                llThreeImage.setVisibility(View.VISIBLE);
                tvImgNum.setVisibility(View.VISIBLE);
                tvImgNum.setText("共" + item.getImgList().size() + "张");

                ImageLoader.getInstance().displayImage(item.getImgList().get(0).getShowImg(), ivThree1);
                ImageLoader.getInstance().displayImage(item.getImgList().get(1).getShowImg(), ivThree2);
                ImageLoader.getInstance().displayImage(item.getImgList().get(2).getShowImg(), ivThree3);

                llThreeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < getItem(helper.getPosition()).getImgList().size(); i++) {
                            list.add(getItem(helper.getPosition()).getImgList().get(i).getShowImg());
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(ActivityExtras.EXTRAS_IMAGE_LARGER_POSITION, 0);
                        bundle.putSerializable(ActivityExtras.EXTRAS_IMAGE_LARGER_LIST, list);
                        ActivityUtils.overlay(mContext, LargerImageActivity.class, bundle);
                    }
                });
            }
        }

        helper.setText(R.id.tv_collection, item.getCollectnum());
        helper.setText(R.id.tv_comment, String.valueOf((int) item.getCommentnum()));
    }
}
