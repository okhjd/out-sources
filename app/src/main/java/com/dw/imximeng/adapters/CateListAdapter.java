package com.dw.imximeng.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ViewHolder;
import com.dw.imximeng.bean.CateList;
import com.dw.imximeng.bean.Selection;

import java.util.List;

/**
 * @author hjd
 * @Created_Time 2018\6\28 0028
 */
public class CateListAdapter extends BaseAdapter implements  View.OnTouchListener, View.OnFocusChangeListener{
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<CateList.AttrList> mDatas;
    protected final int mItemLayoutId;

    public CateListAdapter(Context context, List<CateList.AttrList> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    private int selectedEditTextPosition = -1;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedEditTextPosition != -1) {
                Log.w("MyEditAdapter", "onTextPosiotion " + selectedEditTextPosition);
                CateList.AttrList itemTest = (CateList.AttrList) getItem(selectedEditTextPosition);
                itemTest.setContent(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            EditText editText = (EditText) v;
            selectedEditTextPosition = (int) editText.getTag();
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;
        if (hasFocus) {
            editText.addTextChangedListener(mTextWatcher);
        } else {
            editText.removeTextChangedListener(mTextWatcher);
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CateList.AttrList getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        CateList.AttrList item = getItem(position);

        vh.tvCateTitle.setText(item.getName());

        vh.etCate.setOnTouchListener(this); // 正确写法
        vh.etCate.setOnFocusChangeListener(this);
        vh.etCate.setTag(position);
        vh.etCate.setText(item.getContent());

        if (selectedEditTextPosition != -1 && position == selectedEditTextPosition) { // 保证每个时刻只有一个EditText能获取到焦点
            vh.etCate.requestFocus();
        } else {
            vh.etCate.clearFocus();
        }


        if (item.getType().equals("text")){
            vh.etCate.setVisibility(View.VISIBLE);
            vh.tvCate.setVisibility(View.GONE);
        }else {
            vh.etCate.setVisibility(View.GONE);
            vh.tvCate.setVisibility(View.VISIBLE);
        }
        vh.tvCate.setText(item.getContent());

        return convertView;
    }

    public class ViewHolder {
        EditText etCate;
        TextView tvCate;
        TextView tvCateTitle;

        public ViewHolder(View convertView) {
            etCate = convertView.findViewById(R.id.et_cate);
            tvCate = convertView.findViewById(R.id.tv_cate);
            tvCateTitle = convertView.findViewById(R.id.tv_cate_title);
        }
    }
}
