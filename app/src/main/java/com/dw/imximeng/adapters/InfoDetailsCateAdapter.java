package com.dw.imximeng.adapters;

import android.content.Context;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.InfoDetails;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class InfoDetailsCateAdapter extends CommonAdapter<InfoDetails.AttrListBean> {
    public InfoDetailsCateAdapter(Context context, List<InfoDetails.AttrListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, InfoDetails.AttrListBean item) {
        helper.setText(R.id.tv_cate_title, item.getName() + ":" + item.getVals());
    }
}
