package com.dw.imximeng.interf;

import android.view.View;


/**
 * 基类fragment实现接口
 * @author hjd
 * @time 2014年9月25日 上午11:00:25
 *
 */
public interface BaseFragmentInterface {
	public int getLayoutId();
	public void initView(View view);
	
	public void initData();
}
