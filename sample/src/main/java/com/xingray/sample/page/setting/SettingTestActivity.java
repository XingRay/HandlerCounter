package com.xingray.sample.page.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xingray.handlercounter.CountListener;
import com.xingray.handlercounter.HandlerCounter;
import com.xingray.handlercounter.RepeatMode;
import com.xingray.sample.R;
import com.xingray.sample.page.test.TestActivity;
import com.xingray.sample.util.ViewHelper;


/**
 * description : 主页
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/8/9 14:17
 */
public class SettingTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SettingTestActivity.class.getSimpleName();

    private HandlerCounter mCounter;
    private Activity mActivity;
    private EditText etStartValue;
    private EditText etEndValue;
    private EditText etInterval;
    private EditText etStepSize;
    private Switch swStrictMode;
    private RadioButton rbNone;
    private RadioButton rbRestart;
    private RadioButton rbReverse;
    private Switch swInfiniteRepeat;
    private EditText etRepeatCount;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        setContentView(R.layout.activity_setting_test);
        findViews();

        ViewHelper.setOnClick(getWindow().getDecorView(), new int[]{
                R.id.bt_start,
                R.id.bt_stop,
                R.id.bt_pause,
                R.id.bt_restart,
                R.id.bt_apply}, this);

        mCounter = new HandlerCounter()
                .startValue(1)
                .endValue(100)
                .countValue(1)
                .countInterval(1000)
                .strictMode(true)
                .countListener(new CountListener() {
                    @Override
                    public void onCount(long count) {
                        String text = count + "";
                        tvText.setText(text);
                        Log.i(TAG, text);
                    }
                })
                .repeatMode(RepeatMode.RESTART)
                .repeatCount(3)
                .start();

        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.start(mActivity);
            }
        });
        findViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                mCounter.start();
                break;
            case R.id.bt_stop:
                mCounter.stop();
                break;
            case R.id.bt_pause:
                mCounter.pause();
                break;
            case R.id.bt_restart:
                mCounter.restart();
                break;
            case R.id.bt_apply:
                mCounter.startValue(readValue(etStartValue, 0))
                        .endValue(readValue(etEndValue, 0))
                        .countInterval(readValue(etInterval, 1000))
                        .countValue(readValue(etStepSize, 1))
                        .strictMode(swStrictMode.isChecked());
                if (rbNone.isChecked()) {
                    mCounter.clearRepeat();
                } else {
                    if (rbReverse.isChecked()) {
                        mCounter.repeatMode(RepeatMode.REVERSE);
                    } else if (rbRestart.isChecked()) {
                        mCounter.repeatMode(RepeatMode.RESTART);
                    }
                    if (swInfiniteRepeat.isChecked()) {
                        mCounter.repeatInfinite();
                    } else {
                        mCounter.repeatCount(readValue(etRepeatCount, 1));
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");

        if (mCounter != null) {
            mCounter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");

        if (mCounter != null) {
            mCounter.onRestoreInstanceState(savedInstanceState);
        }
    }

    private int readValue(TextView textView, int defaultValue) {
        String text = textView.getText().toString().trim();
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    private void findViews() {
        etStartValue = findViewById(R.id.et_start_value);
        etEndValue = findViewById(R.id.et_end_value);
        etInterval = findViewById(R.id.et_interval);
        etStepSize = findViewById(R.id.et_step_size);
        swStrictMode = findViewById(R.id.sw_strict_mode);
        rbNone = findViewById(R.id.rb_none);
        rbRestart = findViewById(R.id.rb_restart);
        rbReverse = findViewById(R.id.rb_reverse);
        swInfiniteRepeat = findViewById(R.id.sw_infinite_repeat);
        etRepeatCount = findViewById(R.id.et_repeat_count);
        tvText = findViewById(R.id.tv_text);
    }
}
