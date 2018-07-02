package com.dw.imximeng.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.dw.imximeng.R;

public class AutoListView extends ListView implements OnScrollListener {

    public static final int REFRESH = 0;
    public static final int LOAD = 1;

    // 区分PULL和RELEASE的距离的大小
    private static final int SPACE = 20;

    private static final int NONE = 0;
    private static final int PULL = 1;
    private static final int RELEASE = 2;
    private static final int REFRESHING = 3;
    private static final int STATE_SUCCESS = 4;
    private static final int STATE_FAILED = -1;
    private int state;
    public Context context;
    //    private LayoutInflater inflater;
    private View header;
    private View footer;

    private TextView noData;
    private TextView loadFull;
    private TextView more;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private int startY;

    private int firstVisibleItem;
    private int scrollState;
    private int headerContentInitialHeight;
    private int headerContentHeight;

    // 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview
    private boolean isRecorded;
    private boolean isLoading;// 判断是否正在加载
    private boolean loadEnable = true;
    private boolean isLoadFull;
    private int pageSize = 20;

    private OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;
    private TextView refreshTv = null;
    private ProgressBar refreshProgress = null;
    private Scroller scroller = null;

    public AutoListView(Context context) {
        super(context);
        initView(context);
    }

    public AutoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AutoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 下拉刷新监听
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    // 加载更多监听
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.loadEnable = true;
        this.onLoadListener = onLoadListener;
    }

    public boolean isLoadEnable() {
        return loadEnable;
    }

    public void setLoadEnable(boolean loadEnable) {
        this.loadEnable = loadEnable;
        this.removeFooterView(footer);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private void initView(Context context) {
        this.context = context;
        scroller = new Scroller(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.auto_listview_footer, null);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        header = inflater.inflate(R.layout.auto_listview_header, null);
        header.getLayoutParams();
        refreshTv = (TextView) header.findViewById(R.id.refresh_text);
        refreshProgress = (ProgressBar) header.findViewById(R.id.refresh_progress);
        // 为listview添加头部和尾部，并进行初始化
        headerContentInitialHeight = header.getPaddingTop();
//        headerContentHeight = MaDensityUtils.dp2px(context, 30f);
        headerContentHeight = calculationViewHeight(header);

        topPadding(-headerContentHeight);
        //添加头部并不让点击
        this.addHeaderView(header, null, false);
        this.addFooterView(footer, null, false);
        this.setOnScrollListener(this);
    }

    private int calculationViewHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    public void onRefresh() {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    public void onLoad() {
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }


    // 用于下拉刷新结束后的回调
    public void onRefreshComplete() {
        state = STATE_SUCCESS;
        refreshHeaderViewByState();
    }

    // 用于加载更多结束后的回调
    public void onLoadComplete() {
        isLoading = false;
    }

    // 用于下拉刷新结束后的回调
    public void onRefreshFailue() {
        state = STATE_FAILED;
        refreshHeaderViewByState();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        ifNeedLoad(view, scrollState);
    }

    private void ifNeedLoad(AbsListView view, int scrollState) {
        if (!loadEnable) {
            return;
        }
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && !isLoading
                    && view.getLastVisiblePosition() == view
                    .getPositionForView(footer) && !isLoadFull) {
                onLoad();
                isLoading = true;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRecorded = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (state == PULL) {
                    topPadding(-headerContentHeight);
                    state = NONE;
                    refreshHeaderViewByState();
                } else if (state == RELEASE) {
                    topPadding(0);
                    state = REFRESHING;
                    refreshHeaderViewByState();
                    onRefresh();
                }
                isRecorded = false;
                break;
            case MotionEvent.ACTION_MOVE:
                whenMove(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void whenMove(MotionEvent ev) {
        refreshProgress.setIndeterminate(true);
        refreshProgress.setProgress(R.mipmap.bg_loginbox0);
        if (!isRecorded) {
            return;
        }
        int tmpY = (int) ev.getY();
        int space = tmpY - startY;
        int topPadding = space - headerContentHeight;
        if (topPadding > 20) {
            topPadding = 20;
        }
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    refreshHeaderViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerContentHeight + SPACE) {
                    state = RELEASE;
                    refreshHeaderViewByState();
                }
                break;
            case RELEASE:
                topPadding(0);
                if (space > 0 && space < headerContentHeight + SPACE) {
                    state = PULL;
                    refreshHeaderViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    refreshHeaderViewByState();
                }
                break;
        }

    }

    private void topPadding(int topPadding) {
        if (topPadding < -headerContentHeight) {
            return;
        }
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    /**
     * @param resultSize list的子项数量
     */
    public void setResultSize(int resultSize) {
        if (resultSize == 0) {
            isLoadFull = true;
            loadFull.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        } else if (resultSize > 0 && resultSize < pageSize) {
            isLoadFull = true;
            loadFull.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        } else if (resultSize == pageSize) {
            isLoadFull = false;
            loadFull.setVisibility(View.GONE);
            more.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

    }

    /**
     * @param page list的子项页数
     */
    public void setResultPage(int page, int listSize) {
        if (page == 0) {
            isLoadFull = true;
            loadFull.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        } else if (page > 0 && listSize > 0) {
            isLoadFull = true;
            loadFull.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        } else if (page > 0 && listSize == 0) {
            isLoadFull = false;
            loadFull.setVisibility(View.GONE);
            more.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

    }

    private void refreshHeaderViewByState() {
        switch (state) {
            case NONE:
                refreshTv.setText("下拉刷新");
                refreshProgress.setVisibility(View.INVISIBLE);
                break;
            case PULL:
                refreshTv.setText("下拉刷新");
                refreshProgress.setVisibility(View.INVISIBLE);
                break;
            case RELEASE:
                refreshTv.setText("松开刷新");
                refreshProgress.setVisibility(View.INVISIBLE);
                break;
            case REFRESHING:
                topPadding(headerContentInitialHeight);
                refreshTv.setText("正在加载…");
                refreshProgress.setVisibility(View.VISIBLE);
                break;
            case STATE_FAILED:
                refreshTv.setText("刷新失败");
                refreshProgress.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroller.startScroll(0, header.getPaddingTop(), 0, -headerContentHeight, 800);
                        invalidate();
                    }
                }, 500);
                state = NONE;
                break;
            case STATE_SUCCESS:
                refreshTv.setText("刷新成功");
                refreshProgress.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //设置mScroller的滚动偏移量
                        scroller.startScroll(0, header.getPaddingTop(), 0, -headerContentHeight, 800);
                        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果;
                    }
                }, 500);
                state = NONE;
                break;
        }
    }

    /*
     * 定义下拉刷新接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /*
     * 定义加载更多接口
     */
    public interface OnLoadListener {
        public void onLoad();
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (scroller != null && scroller.computeScrollOffset()) {
            //这里调用View的scrollTo()完成实际的滚动
            topPadding(scroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    public void firstOnRefresh() {
        if (state == NONE) {
            setSelection(0);
            topPadding(0);
            loadFull.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            state = REFRESHING;
            refreshHeaderViewByState();
            onRefresh();
        }
    }

    /**
     * @param totalSize   数据总数量
     * @param currentSize 当前列表数量
     */
    public void setTotalSize(int totalSize, int currentSize) {
        if (currentSize < totalSize) {
            isLoadFull = false;
            loadFull.setVisibility(View.GONE);
            more.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            isLoadFull = true;
            loadFull.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
        }
    }
}
