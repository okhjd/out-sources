package com.dw.imximeng.activitys.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.SearchAdapter;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.SearchTitle;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.FlowGroupView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_hot_title)
    TextView tvHotTitle;
    @BindView(R.id.fgv_hot_search)
    FlowGroupView fgvHotSearch;
    @BindView(R.id.tv_historical_title)
    TextView tvHistoricalTitle;
    @BindView(R.id.fgv_historical_search)
    FlowGroupView fgvHistoricalSearch;
    @BindView(R.id.ll_hot)
    LinearLayout llHot;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;

    private List<SearchTitle> searchTitles = new ArrayList<>();
    private SearchAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        initHistoricalSearch();

        etSearch.setOnEditorActionListener(this);

        adapter = new SearchAdapter(this, searchTitles, R.layout.item_service_search);
        lvSearchResult.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getHotSearch(sharedPreferencesHelper.isSwitchLanguage());
    }

    private void initHistoricalSearch() {
        fgvHistoricalSearch.removeAllViews();
        List<String> list = sharedPreferencesHelper.getHistoricalSearchData();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                addTextView(list.get(i), fgvHistoricalSearch);
            }
        }
    }

    @OnClick({R.id.iv_historical_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_historical_delete:
                sharedPreferencesHelper.clearHistoricalSearch();
                initHistoricalSearch();
                break;
        }
    }

    private void getHotSearch(boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.HOT_SEARCH)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                closeProgressBar();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                closeProgressBar();
                if (response.getStatus() == 1) {
                    List<String> list = new Gson().fromJson(new Gson().toJson(response.getData()), new TypeToken<List<String>>() {
                    }.getType());
                    fgvHotSearch.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        addTextView(list.get(i), fgvHotSearch);
                    }
                }
            }
        });
    }

    /**
     * 动态添加布局
     *
     * @param str
     */
    private void addTextView(String str, FlowGroupView flowGroupView) {
        View view = View.inflate(this, R.layout.item_search, null);
        CheckBox cbItem = view.findViewById(R.id.cb_item);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(MaDensityUtils.dp2px(this, 7),
                MaDensityUtils.dp2px(this, 7),
                MaDensityUtils.dp2px(this, 7),
                MaDensityUtils.dp2px(this, 7));
        cbItem.setText(str);
        cbItem.setTag(str);
        view.setLayoutParams(params);
        initEvents(cbItem, flowGroupView);//监听
        flowGroupView.addView(view);
    }

    /**
     * 为每个view 添加点击事件
     */
    private void initEvents(final CheckBox checkBox, final FlowGroupView flowGroupView) {
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    etSearch.setText(checkBox.getText().toString());
                    etSearch.setSelection(etSearch.getText().toString().length());
                    for (int i = 0; i < flowGroupView.getChildCount(); i++) {
                        CheckBox cbItem = (((CheckBox) ((LinearLayout) flowGroupView.getChildAt(i)).getChildAt(0)));
                        if (!checkBox.getTag().equals(cbItem.getTag())) {
                            cbItem.setChecked(false);
                        }
                    }
                } else {
                    etSearch.setText("");
                }

                if (BaseApplication.userInfo.getArea() == null) {
                    ActivityUtils.forward(SearchActivity.this, SelectAreaActivity.class, etSearch.getText().toString());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, BaseApplication.userInfo.getArea());
                    bundle.putString(ActivityExtras.EXTRAS_CATE_ID, "");
                    bundle.putString(ActivityExtras.EXTRAS_KEY_WORD, etSearch.getText().toString());
                    ActivityUtils.forward(SearchActivity.this, SearchResultActivity.class, bundle);
                }
            }
        });
    }

    @OnTextChanged(R.id.et_search)
    public void onTextChanged(CharSequence c) {
        if (etSearch.getText().toString().isEmpty()) {
            lvSearchResult.setVisibility(View.GONE);
            llHot.setVisibility(View.VISIBLE);
            initHistoricalSearch();
        } else {
            lvSearchResult.setVisibility(View.VISIBLE);
            llHot.setVisibility(View.GONE);
        }
        getSearchTitle(BaseApplication.userInfo.getArea(), "", etSearch.getText().toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /*判断是否是“SEARCH”键*/
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            if (!etSearch.getText().toString().isEmpty()) {
                sharedPreferencesHelper.setHistoricalSearchData(etSearch.getText().toString());
            }

            if (BaseApplication.userInfo.getArea() == null) {
                ActivityUtils.forward(this, SelectAreaActivity.class, etSearch.getText().toString());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, BaseApplication.userInfo.getArea());
                bundle.putString(ActivityExtras.EXTRAS_CATE_ID, "");
                bundle.putString(ActivityExtras.EXTRAS_KEY_WORD, etSearch.getText().toString());
                ActivityUtils.forward(this, SearchResultActivity.class, bundle);
            }
            return true;
        }
        return false;
    }

    private void getSearchTitle(final String area, String cid, String keywords) {
        OkHttpUtils.post().url(MethodHelper.SEARCH_TITLE)
                .addParams("area", StringUtils.stringsIsEmpty(area))
                .addParams("cid", cid)//分类ID（非必传，默认：0）
                .addParams("keywords", keywords)//搜索关键词（非必传）
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
                lvSearchResult.setVisibility(View.GONE);
                llHot.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(Result response, int id) {
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    List<SearchTitle> dataBean = new Gson().fromJson(data, new TypeToken<List<SearchTitle>>() {
                    }.getType());

                    searchTitles.clear();
                    searchTitles.addAll(dataBean);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnItemClick(R.id.lv_search_result)
    public void onItemClick(int position) {
        if (BaseApplication.userInfo.getArea() == null) {
            ActivityUtils.forward(this, SelectAreaActivity.class, etSearch.getText().toString());
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(ActivityExtras.EXTRAS_INFO_DETAILS_CITY_ID, BaseApplication.userInfo.getArea());
            bundle.putString(ActivityExtras.EXTRAS_CATE_ID, "");
            bundle.putString(ActivityExtras.EXTRAS_KEY_WORD, etSearch.getText().toString());
            ActivityUtils.forward(this, SearchResultActivity.class, bundle);
        }
    }
}
