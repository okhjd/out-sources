package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.UserHome;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class UserHomeAdapter extends CommonAdapter<UserHome.ListBean> {
    public UserHomeAdapter(Context context, List<UserHome.ListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, UserHome.ListBean item) {

    }
}
