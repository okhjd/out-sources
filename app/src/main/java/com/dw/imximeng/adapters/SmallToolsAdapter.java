package com.dw.imximeng.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.SmallTools;
import com.dw.imximeng.bean.UserIndexData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class SmallToolsAdapter extends CommonAdapter<SmallTools> {
    public SmallToolsAdapter(Context context, List<SmallTools> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper,SmallTools item) {
        helper.setText(R.id.tv_title, item.getName());
        ImageView imageView = helper.getView(R.id.iv_icon_tools);
        ImageLoader.getInstance().displayImage(item.getShowIcon(), imageView);
    }
}
