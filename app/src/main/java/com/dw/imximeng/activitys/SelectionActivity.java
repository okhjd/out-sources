package com.dw.imximeng.activitys;

import android.os.Bundle;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.SelectionAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Selection;
import com.dw.imximeng.helper.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * @author hjd
 * @create-time 2018-07-06 16:34:11
 */
public class SelectionActivity extends BaseActivity {

    @BindView(R.id.lv_selection)
    ListView lvSelection;
    private SelectionAdapter adapter;
    private List<Selection> list = new ArrayList<>();
    private boolean single = true;
    private String title = "";

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        if (bundle != null) {
            list.addAll((ArrayList<Selection>) bundle.getSerializable(ActivityExtras.EXTRAS_SELECTION_DATA));
            single = bundle.getBoolean(ActivityExtras.EXTRAS_SELECTION_TYPE);
            title = bundle.getString(ActivityExtras.EXTRAS_SELECTION_TITLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_selection;
    }

    @Override
    public void initView() {
        setTitle(title);
        if (!single) {
            setSubmit("保存");
        }
        adapter = new SelectionAdapter(this, list, R.layout.item_selection);
        lvSelection.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @OnItemClick(R.id.lv_selection)
    public void onItemClick(int position) {
        list.get(position).setCheck(!list.get(position).isCheck());
        if (single) {
            //单选@
            ActivityUtils.setResult(this, RESULT_OK, list.get(position));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tv_submit)
    public void onClick() {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck()) {
                if (i == list.size() - 1) {
                    result = list.get(i).getName();
                } else {
                    result = list.get(i).getName() + ",";
                }
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(ActivityExtras.EXTRAS_SELECTION_TITLE, title);
        bundle.putString(ActivityExtras.EXTRAS_SELECTION_DATA, result);
        ActivityUtils.setResult(this, RESULT_OK, bundle);
    }
}
