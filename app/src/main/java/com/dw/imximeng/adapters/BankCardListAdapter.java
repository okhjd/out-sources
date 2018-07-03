package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.BankCard;
import com.dw.imximeng.bean.MyWallets;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class BankCardListAdapter extends CommonAdapter<BankCard> {
    public BankCardListAdapter(Context context, List<BankCard> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper,BankCard item) {
        helper.setText(R.id.tv_bank_name, item.getBankName());
        ImageView ivBankIcon = helper.getView(R.id.iv_bank_icon);
        ImageLoader.getInstance().displayImage(item.getIcon(), ivBankIcon);
        helper.setText(R.id.tv_bank_card_number, item.getShowCnumber());
    }
}
