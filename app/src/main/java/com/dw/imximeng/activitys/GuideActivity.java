package com.dw.imximeng.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.VpGuideAdapter;
import com.dw.imximeng.base.BaseActivity;

import butterknife.BindView;

/**
 * @author hjd
 * @Created_Time 2018\6\23 0023
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.vp_guide)
    ViewPager vpGuide;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //设置全屏
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        int[] guide = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        vpGuide.setAdapter(new VpGuideAdapter(this, guide));
    }

    @Override
    public void initData() {

    }
}
