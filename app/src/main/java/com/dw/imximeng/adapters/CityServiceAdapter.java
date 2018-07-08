package com.dw.imximeng.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.CityService;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.widgets.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class CityServiceAdapter extends CommonAdapter<CityService.ListBean> {

    public CityServiceAdapter(Context context, List<CityService.ListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, final CityService.ListBean item) {
        ImageView ivHead = helper.getView(R.id.iv_head);
        ImageLoader.getInstance().displayImage(item.getShowIcon(), ivHead);

        helper.setText(R.id.tv_content, item.getName());
        LinearLayout llType = helper.getView(R.id.ll_type);
        llType.removeAllViews();
        for (int i = 0; i < item.getLabelArray().size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tvType = new TextView(mContext);
            tvType.setText(item.getLabelArray().get(i));
            tvType.setTextSize(10);
            tvType.setTextColor(Color.parseColor("#699df5"));
            tvType.setBackgroundResource(R.drawable.shape_info_details_margin_blue);
            tvType.setPadding(MaDensityUtils.dp2px(mContext, 10),
                    MaDensityUtils.dp2px(mContext, 3),
                    MaDensityUtils.dp2px(mContext, 10),
                    MaDensityUtils.dp2px(mContext, 3));

            params.setMargins(0, 0, MaDensityUtils.dp2px(mContext, 5), 0);
            tvType.setLayoutParams(params);
            llType.addView(tvType);
        }

        helper.getView(R.id.iv_call_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item.getPhone());
            }
        });

    }

    private void showDialog(final String phone) {
        new AlertDialog(mContext)
                .builder()
                .setMsg("是否拨打电话")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        diallPhone(phone);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }
}
