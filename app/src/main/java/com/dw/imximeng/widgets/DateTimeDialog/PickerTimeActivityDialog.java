package com.dw.imximeng.widgets.DateTimeDialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dw.imximeng.R;
import com.dw.imximeng.helper.MaDateUtil;
import com.dw.imximeng.helper.MaDensityUtils;
import com.dw.imximeng.helper.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/12/1.
 */

public class PickerTimeActivityDialog extends Dialog implements PickTimeView.onSelectedChangeListener {
    Activity activity;
    @BindView(R.id.ll_picker)
    LinearLayout llPicker;
    SimpleDateFormat sdfDate;
    String dataTime;
    String choiceTime;
    boolean isVisible;
    TextView tvTime;
    private Toast toast = null;
    PickerDataOnClickListener pickerDataOnClickListener;

    public PickerTimeActivityDialog(Activity activity, String dataTime, boolean isVisible, TextView tvTime) {
        super(activity, R.style.CommonDialog);
        this.activity = activity;
        if (StringUtils.isEmpty(dataTime)) {
            dataTime = MaDateUtil.getCurrentDate(MaDateUtil.dateFormatYMD);
        }
        this.dataTime = dataTime;
        this.isVisible = isVisible;
        this.tvTime = tvTime;
        initView();
    }

    public void initView() {
        // 获取Dialog布局
        View view = LayoutInflater.from(activity).inflate(
                R.layout.date_picker_dialog, null);
        sdfDate = new SimpleDateFormat(MaDateUtil.dateFormatYMD);
        PickTimeView datePickerStart = (PickTimeView) view.findViewById(R.id.datePickerStart);
        datePickerStart.setViewType(PickTimeView.TYPE_PICK_DATE, isVisible);
        try {
            datePickerStart.setTimeMillis(MaDateUtil.dateToStamp(dataTime, MaDateUtil.dateFormatYMD));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePickerStart.setOnSelectedChangeListener(this);
        ButterKnife.bind(this, view);
        setContentView(view);
        // 调整dialog背景大小
//        llPicker.setLayoutParams(new FrameLayout.LayoutParams(MaDensityUtils.dp2px(activity, 360), FrameLayout.LayoutParams.WRAP_CONTENT));
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.CommonDialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @OnClick({R.id.txv_sure, R.id.txv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_sure:
                if (StringUtils.isEmpty(choiceTime)) {
                    choiceTime = dataTime;
                }
                if (isVisible) {
                    if (MaDateUtil.getOffectMinutes(choiceTime,
                            MaDateUtil.getCurrentDate(MaDateUtil.dateFormatYMD)) > 0) {
                        showToast("不能选择未来时间！");
                    } else {
                        tvTime.setText(choiceTime);
                        if (pickerDataOnClickListener != null) {
                            pickerDataOnClickListener.Onclick(choiceTime);
                        }
                    }
                }
                dismiss();
                break;
            case R.id.txv_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onSelected(PickTimeView view, long timeMillis) {
        choiceTime = sdfDate.format(timeMillis);
    }

    public void showToast(@StringRes int res) {
        if (toast == null) {
            toast = Toast.makeText(activity, res, Toast.LENGTH_SHORT);
        } else {
            toast.setText(activity.getString(res));
        }
        toast.show();
    }

    public void showToast(String res) {
        if (toast == null) {
            toast = Toast.makeText(activity, res, Toast.LENGTH_SHORT);
        } else {
            toast.setText(res);
        }
        toast.show();
    }

    public void setPickerDataOnClickListener(PickerDataOnClickListener pickerDataOnClickListener) {
        this.pickerDataOnClickListener = pickerDataOnClickListener;
    }

    public interface PickerDataOnClickListener {
        void Onclick(String choiceTime);
    }
}
