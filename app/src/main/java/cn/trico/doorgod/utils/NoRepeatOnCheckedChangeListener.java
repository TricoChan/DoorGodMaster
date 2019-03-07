package cn.trico.doorgod.utils;

import android.widget.CompoundButton;

import java.util.Calendar;

/**
 * 防止短期两次以上点击多次提交表单
 */
public abstract class NoRepeatOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    public static final int MIN_CLICK_DELAY_TIME = 3000;
    private long lastClickTime = 0;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoRepeatClick(buttonView, isChecked);
        }
    }

    public abstract void onNoRepeatClick(CompoundButton buttonView, boolean isChecked);
}
