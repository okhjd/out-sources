package com.dw.imximeng.activitys;

import android.os.Bundle;
import android.widget.ListView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.SelectionAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Selection;
import com.dw.imximeng.helper.ActivityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

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

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        if (bundle != null){
            list.addAll((ArrayList<Selection>) bundle.getSerializable("data"));
            single = bundle.getBoolean("single");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_selection;
    }

    @Override
    public void initView() {
        if (!single){
            setSubmit("保存");
        }
        adapter = new SelectionAdapter(this, list, R.layout.item_selection);
        lvSelection.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }


}
