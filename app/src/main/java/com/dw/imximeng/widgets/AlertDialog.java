package com.dw.imximeng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;


public class AlertDialog {
	private Context context;
	private Dialog dialog;
//	private LinearLayout lLayout_bg;
	private TextView mTvTitle;
	private TextView mTvContent;

	private TextView mTvCancel;
	private TextView mTvSure;
	private LinearLayout mLlBottom;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	private ListView mListView;
	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_view_alertdialog, null);

		// 获取自定义Dialog布局中的控件
		LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.ll_edit);

		mTvTitle = (TextView)view.findViewById(R.id.tv_title);
		mTvContent = (TextView)view.findViewById(R.id.tv_content);

		mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
		mTvSure = (TextView) view.findViewById(R.id.tv_sure);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.75), FrameLayout.LayoutParams.WRAP_CONTENT));

		return this;
	}

	public AlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			mTvTitle.setText("标题");
			mTvTitle.setVisibility(View.GONE);
		} else {
			mTvTitle.setText(title);
		}
		return this;
	}

	public AlertDialog setMsg(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			mTvContent.setText("内容");
			mTvContent.setVisibility(View.GONE);
		} else {
			mTvContent.setText(msg);
		}
		return this;
	}

	public AlertDialog setMsg(SpannableString msg) {
		showMsg = true;
		if ("".equals(msg.toString())) {
			mTvContent.setText("内容");
			mTvContent.setVisibility(View.GONE);
		} else {
			mTvContent.setText(msg);
		}
		return this;
	}

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AlertDialog setPositiveButton(String text,
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

	public AlertDialog setNegativeButton(String text,
										 final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			mTvCancel.setText("取消");
		} else {
			mTvCancel.setText(text);
		}
		mTvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public AlertDialog setMsgColor(int resColor) {
		mTvContent.setTextColor(resColor);
		return this;
	}

	/**
	 * 设置取消按钮
	 * @return
     */
	public AlertDialog setNegativeButton() {
		showNegBtn = true;
		mTvCancel.setText("取消");
		mTvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		}else {
			mTvTitle.setVisibility(View.GONE);
		}

		if (showMsg) {
			mTvContent.setVisibility(View.VISIBLE);
		}

		if (!showPosBtn && !showNegBtn) {
			mTvCancel.setText("确定");
			mTvCancel.setVisibility(View.GONE);
			mTvSure.setVisibility(View.GONE);
			mTvCancel.setBackgroundResource(R.color.transparent);
			mTvCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			mTvCancel.setVisibility(View.VISIBLE);
			mTvCancel.setBackgroundResource(R.color.transparent);
			mTvSure.setVisibility(View.VISIBLE);
			mTvSure.setBackgroundResource(R.color.transparent);
		}

		if (showPosBtn && !showNegBtn) {
			mTvSure.setVisibility(View.VISIBLE);
			mTvSure.setBackgroundResource(R.color.transparent);
			mTvCancel.setVisibility(View.GONE);
		}

		if (!showPosBtn && showNegBtn) {
			mTvCancel.setVisibility(View.VISIBLE);
			mTvCancel.setBackgroundResource(R.color.transparent);
			mTvSure.setVisibility(View.GONE);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}

