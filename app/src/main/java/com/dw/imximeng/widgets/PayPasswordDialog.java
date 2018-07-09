package com.dw.imximeng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dw.imximeng.R;


public class PayPasswordDialog {
	private Context context;
	private Dialog dialog;

	private ItemPasswordLayout payPassword;
	private Display display;
	public PayPasswordDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public PayPasswordDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_pay_password, null);

		// 获取自定义Dialog布局中的控件
		LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.ll_edit);

		payPassword = (ItemPasswordLayout)view.findViewById(R.id.pay_password);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.75), FrameLayout.LayoutParams.WRAP_CONTENT));

		return this;
	}

	public PayPasswordDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public PayPasswordDialog setPositiveButton(final ItemPasswordLayout.OnInputFinishListener listener) {
		payPassword.setOnInputFinishListener(new ItemPasswordLayout.OnInputFinishListener() {

			@Override
			public void onInputFinish(String password) {
				listener.onInputFinish(password);
				dialog.dismiss();
			}
		});
		return this;
	}

	public void show() {
		dialog.show();
	}
}

