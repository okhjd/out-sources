package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.MyPoints;
import com.dw.imximeng.bean.MyWallets;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class MyWalletListAdapter extends CommonAdapter<MyWallets.Wallet> {
    public MyWalletListAdapter(Context context, List<MyWallets.Wallet> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, MyWallets.Wallet item) {
        helper.setText(R.id.tv_name, item.getCateName());
        helper.setText(R.id.tv_time, item.getShowTime());
        helper.setText(R.id.tv_price, item.getType() == 1 ? "+" + item.getNumber() : "-" + item.getNumber());
    }
}
