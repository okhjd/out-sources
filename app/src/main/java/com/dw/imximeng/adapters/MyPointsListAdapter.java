package com.dw.imximeng.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.MyPoints;
import com.dw.imximeng.bean.UserMessage;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class MyPointsListAdapter extends CommonAdapter<MyPoints.PointsItem> {
    public MyPointsListAdapter(Context context, List<MyPoints.PointsItem> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, MyPoints.PointsItem item) {
        helper.setText(R.id.tv_point_cate, item.getCateName());
        helper.setText(R.id.tv_point_type, item.getTypeName());
        helper.setText(R.id.tv_date_time, item.getShowTime());
        helper.setText(R.id.tv_point_num, item.getNumber());
    }
}
