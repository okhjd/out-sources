package com.dw.imximeng.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Information;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.widgets.GridViewNoScroll;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class InformationAdapter extends CommonAdapter<Information.ListBean> {
    public InformationAdapter(Context context, List<Information.ListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Information.ListBean item) {
        ImageViewRoundOval ivHead = helper.getView(R.id.iv_head);
        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(mContext, 5));//圆角大小
        ImageLoader.getInstance().displayImage(item.getHportrait(), ivHead);
        helper.setText(R.id.tv_name, item.getShowName());
        helper.setText(R.id.tv_time, item.getShowTime());

        TextView tvNew = helper.getView(R.id.tv_new);
        if (item.isIs_new()){
            tvNew.setVisibility(View.VISIBLE);
        }else {
            tvNew.setVisibility(View.GONE);
        }
        TextView tvTop = helper.getView(R.id.tv_top);
        if (item.getIs_stick().equals("1")){
            tvTop.setVisibility(View.VISIBLE);
        }else {
            tvTop.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_phone, item.getShowTelephone());
        helper.setText(R.id.tv_collection, item.getCommentnum()+"");
        helper.setText(R.id.tv_comment, item.getCollectnum()+"");

        GridViewNoScroll gvImage = helper.getView(R.id.gv_image);
        gvImage.setAdapter(new GvInfoImageAdapter(mContext, item.getImgList(), R.layout.item_image));
    }
}
