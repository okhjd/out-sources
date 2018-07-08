package com.dw.imximeng.adapters;

import android.content.Context;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.CityService;
import com.dw.imximeng.bean.ServiceSearch;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class ServiceSearchAdapter extends CommonAdapter<ServiceSearch> {
    public ServiceSearchAdapter(Context context, List<ServiceSearch> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, ServiceSearch item) {
        helper.setText(R.id.tv_content, item.getName());

    }
}
