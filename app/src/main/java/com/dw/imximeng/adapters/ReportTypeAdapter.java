package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Bank;
import com.dw.imximeng.bean.ReportType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class ReportTypeAdapter extends CommonAdapter<ReportType> {
    public ReportTypeAdapter(Context context, List<ReportType> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, ReportType item) {
        CheckBox cbName = helper.getView(R.id.cb_name);
        cbName.setChecked(item.isCheck());
        cbName.setText(item.getContent());
    }

    public String checkReport(){
        String result = "";
        for (int i=0;i<mDatas.size();i++){
            if (mDatas.get(i).isCheck()){
                result = mDatas.get(i).getContent();
                return result;
            }
        }
        return result;
    }
}
