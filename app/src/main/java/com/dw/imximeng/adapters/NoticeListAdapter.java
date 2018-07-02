package com.dw.imximeng.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.bean.UserIndexData;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class NoticeListAdapter extends CommonAdapter<UserIndexData.NoticeListBean> {
    public NoticeListAdapter(Context context, List<UserIndexData.NoticeListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, UserIndexData.NoticeListBean item) {
        helper.setText(R.id.tv_content, item.getContent());
        TextView tvDateTime = helper.getView(R.id.tv_date_time);
        tvDateTime.setText(item.getShowTime());
    }
}
