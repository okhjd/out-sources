package com.dw.imximeng.helper;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author hjd
 * @Created_Time 2018\6\27 0027
 */
public class MyCountDownTimer extends CountDownTimer {

    private TextView textView;
    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        textView.setClickable(false);
        textView.setText(l/1000+"秒");

    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        textView.setText("重新获取");
        //设置可点击
        textView.setClickable(true);
    }
}
