package com.dw.imximeng.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.UserIndexData;
import com.dw.imximeng.bean.UserMessage;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class MessageListAdapter extends CommonAdapter<UserMessage> {
    public MessageListAdapter(Context context, List<UserMessage> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, UserMessage item) {
        helper.setText(R.id.tv_content, item.getBrief());
        helper.setText(R.id.tv_title, item.getTitle());
        TextView tvDateTime = helper.getView(R.id.tv_date_time);
        tvDateTime.setText(item.getShowTime());

        View point = helper.getView(R.id.red_point);
        if (item.getIssee() == 1){//阅读状态（1：已读，0：未读）
            point.setVisibility(View.GONE);
        }else {
            point.setVisibility(View.VISIBLE);
        }
    }
}
