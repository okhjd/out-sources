package com.dw.imximeng.activitys.advertisements;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dw.imximeng.R;
import com.dw.imximeng.adapters.UserHomeAdapter;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.base.BaseApplication;
import com.dw.imximeng.bean.Result;
import com.dw.imximeng.bean.UserHome;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.helper.MethodHelper;
import com.dw.imximeng.helper.StringUtils;
import com.dw.imximeng.widgets.ListViewNoScroll;
import com.dw.imximeng.widgets.SwipeRefreshView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author hjd
 * @Created_Time 2018\7\7 0007
 */
public class UserHomeActivity extends BaseActivity{
    @BindView(R.id.iv_backgroup)
    ImageView ivBackgroup;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_autograph)
    TextView tvAutograph;
    @BindView(R.id.lv_info)
    ListViewNoScroll lvInfo;
    @BindView(R.id.refresh_scroll_view)
    PullToRefreshScrollView refreshScrollView;
    private String uid = "";
    private int page = 1;
    private UserHomeAdapter adapter;
    private List<UserHome.ListBean> list = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        uid = ActivityUtils.getStringExtra(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_home;
    }

    @Override
    public void initView() {

        adapter = new UserHomeAdapter(this, list, R.layout.item_user_home);
        lvInfo.setAdapter(adapter);
    }

    @Override
    public void initData() {
        intRefreshScrollView();
        getUserHomeInfo(BaseApplication.userInfo.getSessionid(), uid, page);
    }

    private void intRefreshScrollView() {
        //1.设置模式
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        //2.通过调用getLoadingLayoutProxy方法，设置下拉刷新状况布局中显示的文字 ，第一个参数为true,代表下拉刷新
        ILoadingLayout headLables = refreshScrollView.getLoadingLayoutProxy(true, false);
        headLables.setPullLabel("下拉刷新");
        headLables.setRefreshingLabel("正在刷新");
        headLables.setReleaseLabel("松开刷新");

        //2.设置上拉加载底部视图中显示的文字，第一个参数为false,代表上拉加载更多
        ILoadingLayout footerLables = refreshScrollView.getLoadingLayoutProxy(false, true);
        footerLables.setPullLabel("上拉加载");
        footerLables.setRefreshingLabel("正在载入...");
        footerLables.setReleaseLabel("松开加载更多");

        //3.设置监听事件
        refreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                getUserHomeInfo(BaseApplication.userInfo.getSessionid(), uid, page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                getUserHomeInfo(BaseApplication.userInfo.getSessionid(), uid, page);
            }
        });

    }

    private void getUserHomeInfo(String sessionid, String uid, int cpage) {
        OkHttpUtils.post().url(MethodHelper.USER_HOME)
                .addParams("sessionid", StringUtils.stringsIsEmpty(sessionid))
                .addParams("uid", uid)//地区ID
                .addParams("cpage", String.valueOf(cpage))//地区ID
                .build().execute(new Callback<Result>() {
            @Override
            public Result parseNetworkResponse(Response response, int id) throws Exception {
                String string = response.body().string();
                return new Gson().fromJson(string, Result.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                refreshScrollView.onRefreshComplete();
                Log.e(this.getClass().getName(), "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Result response, int id) {
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                refreshScrollView.onRefreshComplete();
                if (response.getStatus() == 1) {
                    String data = new Gson().toJson(response.getData());
                    UserHome dataBean = new Gson().fromJson(data, UserHome.class);

                    ImageLoader.getInstance().displayImage(dataBean.getUserInfo().getHportrait(), ivHead);
                    ImageLoader.getInstance().displayImage(dataBean.getUserInfo().getHpageimg(), ivBackgroup);
                    tvNickname.setText(dataBean.getUserInfo().getNickname());
                    tvPhone.setText(dataBean.getUserInfo().getShowPhone());
                    tvAutograph.setText(dataBean.getUserInfo().getSignature());

                    if (page == 1) {
                        list.clear();
                        refreshScrollView.getRefreshableView().smoothScrollTo(0,0);
                    }
                    if (dataBean.getList().isEmpty()){
                        refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }else {
                        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                    list.addAll(dataBean.getList());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
