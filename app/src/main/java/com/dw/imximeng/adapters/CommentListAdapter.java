package com.dw.imximeng.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.CommentItem;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.widgets.ImageViewRoundOval;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class CommentListAdapter extends CommonAdapter<CommentItem> {
    private View.OnClickListener onClickListener;
    public CommentListAdapter(Context context, List<CommentItem> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, CommentItem item) {
        ImageViewRoundOval ivHead = helper.getView(R.id.iv_head);
        ivHead.setType(ImageViewRoundOval.TYPE_ROUND);
        ivHead.setRoundRadius(MaDensityUtils.dp2px(mContext, 5));//圆角大小
        ImageLoader.getInstance().displayImage(item.getHportrait(), ivHead);

        helper.setText(R.id.tv_name, item.getShowName());
        helper.setText(R.id.tv_time, item.getShowTime());
        TextView content = helper.getView(R.id.tv_content);
        ImageView ivComment = helper.getView(R.id.iv_comment);
        if (item.getPinfo().isEmpty()) {
            SpannableString spannableString = new SpannableString("回复" + item.getPinfo().get(0).getShowName() + ":" + item.getContent());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#689df5"));
            spannableString.setSpan(colorSpan, 2, item.getPinfo().get(0).getShowName().length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ivComment.setTag(item.getPinfo().get(0).getUid());
            content.setText(spannableString);
        } else {
            ivComment.setTag(0);
            content.setText(item.getContent());
        }


        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
            }
        });
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
