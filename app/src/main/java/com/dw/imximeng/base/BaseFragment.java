package com.dw.imximeng.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dw.imximeng.helper.SharedPreferencesHelper;
import com.dw.imximeng.interf.BaseFragmentInterface;
import com.dw.imximeng.widgets.LoadingAlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 碎片基类
 *
 * @author hjd
 * @Created_Time 2016年5月16日21:54:06
 */
public abstract class BaseFragment extends Fragment implements BaseFragmentInterface {

    protected LayoutInflater mInflater;
    private Unbinder unbinder;
    private LoadingAlertDialog dialog;
    public SharedPreferencesHelper sharedPreferencesHelper;

    public BaseApplication getApplication() {
        return (BaseApplication) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //取消某个请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showToast(int message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void showProgressBar() {
        dialog = new LoadingAlertDialog(getActivity());
        dialog.show("请稍候...");
    }

    public void closeProgressBar() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
