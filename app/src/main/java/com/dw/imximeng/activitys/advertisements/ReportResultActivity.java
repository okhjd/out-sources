package com.dw.imximeng.activitys.advertisements;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class ReportResultActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_report_result;
    }

    @Override
    public void initView() {
        setTitle("结果");
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.tv_back)
    public void onClick() {
        finish();
    }
}
