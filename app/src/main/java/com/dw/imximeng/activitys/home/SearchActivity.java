package com.dw.imximeng.activitys.home;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.FlowGroupView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\6\30 0030
 */
public class SearchActivity extends BaseActivity implements TextWatcher {
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        List<String> list = sharedPreferencesHelper.getHistoricalSearchData();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                addTextView(list.get(i), fgvHistoricalSearch);
            }
        }

        etSearch.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        getHotSearch(sharedPreferencesHelper.isSwitchLanguage());
    }

    @OnClick({R.id.iv_historical_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_historical_delete:
                sharedPreferencesHelper.clearHistoricalSearch();
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
                    for (int i = 0; i < fgvHotSearch.getChildCount(); i++) {
                        CheckBox cbItem = (((CheckBox) ((LinearLayout) flowGroupView.getChildAt(i)).getChildAt(0)));
                        if (!checkBox.getTag().equals(cbItem.getTag())) {
                            cbItem.setChecked(false);
                        }
                    }
                } else {
                    etSearch.setText("");
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void searchInfoList(int area, String sessionid, int cid, String keywords, String orderby, String cpage, boolean language) {
        showProgressBar();
        OkHttpUtils.post().url(MethodHelper.INFORMATION_LIST)
                .addParams("language", language ? "cn" : "mn")//中文：cn，蒙古文：mn
                .addParams("area", String.valueOf(area))
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))//非必传
                .addParams("cid", String.valueOf(cid))//非必传，默认所有
                .addParams("keywords", keywords)//非必传
                .addParams("orderby", orderby)//非必传，最新发布：zxfb，收藏最多：sczd，默认zxfb
                .addParams("cpage", cpage)//非必传，默认1
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
                    for (int i = 0; i < list.size(); i++) {
                        addTextView(list.get(i), fgvHotSearch);
                    }
                }
            }
        });
    }
}
