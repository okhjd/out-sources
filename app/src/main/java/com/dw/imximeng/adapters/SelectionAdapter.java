package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.Selection;
import com.dw.imximeng.bean.UserIndexData;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class SelectionAdapter extends CommonAdapter<Selection> {
    public SelectionAdapter(Context context, List<Selection> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Selection item) {
        CheckBox cbName = helper.getView(R.id.cb_name);
        cbName.setChecked(item.isCheck());
        cbName.setText(item.getName());
    }
}
