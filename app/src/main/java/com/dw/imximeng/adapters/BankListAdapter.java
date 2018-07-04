package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Bank;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class BankListAdapter extends CommonAdapter<Bank> {
    public BankListAdapter(Context context, List<Bank> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Bank item) {
        helper.setText(R.id.tv_bank_name, item.getName());
        ImageView ivBankIcon = helper.getView(R.id.iv_bank_icon);
        ImageLoader.getInstance().displayImage(item.getIcon(), ivBankIcon);
    }
}
