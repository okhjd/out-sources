package com.dw.imximeng.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.Payment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class PaymentListAdapter extends CommonAdapter<Payment.PtypeListBean> {
    public PaymentListAdapter(Context context, List<Payment.PtypeListBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Payment.PtypeListBean item) {
        RadioButton rbPayment = helper.getView(R.id.rb_payment);
        rbPayment.setText(item.getName());
        rbPayment.setChecked(item.isCheck());
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        if (item.getCode().equals("wxpay")){
            ivIcon.setImageResource(R.mipmap.pic57);
        }else if (item.getCode().equals("alipay")){
            ivIcon.setImageResource(R.mipmap.pic58);
        }
    }

    public String checkCode(){
        for (int i = 0;i<mDatas.size();i++){
            if (mDatas.get(i).isCheck()){
                return mDatas.get(i).getCode();
            }
        }
        return null;
    }
}
