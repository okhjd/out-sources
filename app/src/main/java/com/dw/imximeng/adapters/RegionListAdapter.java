package com.dw.imximeng.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.bean.RegionList;
import com.dw.imximeng.app.ViewHolder;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class RegionListAdapter extends CommonAdapter<RegionList.DataBean> {
    public RegionListAdapter(Context context, List<RegionList.DataBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, RegionList.DataBean item) {
        TextView tvRegion = helper.getView(R.id.tv_region);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        if (item.isIsdefault()){
            tvRegion.setText(item.getName());
            ivIcon.setVisibility(View.VISIBLE);
        }else {
            tvRegion.setText("");
            ivIcon.setVisibility(View.GONE);
        }
        tvRegion.setHint(item.getName());
    }
}
