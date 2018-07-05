package com.dw.imximeng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dw.imximeng.R;


public class SigninAlertDialog {
    private Context context;
    private Dialog dialog;
    //	private LinearLayout lLayout_bg;
    private TextView mTvTitle;
    private TextView mTvMnTitle;
    private TextView mTvNum;

    private TextView mTvSure;
    private LinearLayout mLlBottom;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;

    public SigninAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public SigninAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_sign_in, null);

        // 获取自定义Dialog布局中的控件
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.ll_sign_in);

        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvMnTitle = (TextView) view.findViewById(R.id.tv_mn_title);
        mTvNum = (TextView) view.findViewById(R.id.tv_num);

        mTvSure = (TextView) view.findViewById(R.id.tv_sure);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.75), FrameLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    public SigninAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            mTvTitle.setText("标题");
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setText(title);
        }
        return this;
    }

    public SigninAlertDialog setNum(String num) {
        showMsg = true;
        if ("".equals(num)) {
            mTvNum.setVisibility(View.GONE);
        } else {
            mTvNum.setText(num);
        }
        return this;
    }

    public SigninAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public SigninAlertDialog setPositiveButton(String text,
                                               final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            mTvSure.setText("确定");
        } else {
            mTvSure.setText(text);
        }
        mTvSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            mTvTitle.setText("提示");
            mTvTitle.setVisibility(View.GONE);
        }

        if (showTitle) {
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }

        if (showMsg) {
            mTvNum.setVisibility(View.VISIBLE);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}

