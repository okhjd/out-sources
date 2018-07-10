package com.dw.imximeng.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.bean.CateList;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\7\5 0005
 */
public class GvCateListAdapter extends BaseAdapter {
    private List<CateList.CateItem> dataList;

    public GvCateListAdapter(List<CateList.CateItem> datas, int page) {
        dataList = new ArrayList<>();
        //start end分别代表要显示的数组在总数据List中的开始和结束位置
        int start = page * 8;
        int end = start + 8;
        while ((start < datas.size()) && (start < end)) {
            dataList.add(datas.get(start));
            start++;
        }
    }
    @Override
    public int getCount() {
        return dataList.size();
    }
    @Override
    public CateList.CateItem getItem(int i) {
        return dataList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (itemView == null) {
            mHolder = new ViewHolder();
            itemView= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_gridview_cate, viewGroup, false);
            mHolder.iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mHolder.tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setTag(mHolder);
        }
        else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        CateList.CateItem bean = dataList.get(i);
        if (bean != null) {
            mHolder.tv_text.setText(bean.getName());
            if (bean.isCheck()){
                mHolder.tv_text.setTextColor(Color.parseColor("#568fed"));
                ImageLoader.getInstance().displayImage(bean.getSel_icon(), mHolder.iv_img);
            }else {
                mHolder.tv_text.setTextColor(Color.parseColor("#484848"));
                ImageLoader.getInstance().displayImage(bean.getIcon(), mHolder.iv_img);
            }
        }
        return itemView;
    }
    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_text;
    }
}
