package com.leixing.handlercounter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.leixing.lib.handlercounter.CountListener;
import com.leixing.lib.handlercounter.HandlerCounter;
import com.leixing.lib.handlercounter.RepeatMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 14:17
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.sw_strict_mode)
    Switch swStrictMode;
    @BindView(R.id.sw_infinite_repeat)
    Switch swInfiniteRepeat;
    @BindView(R.id.et_repeat_count)
    EditText etRepeatCount;
    @BindView(R.id.rb_none)
    RadioButton rbNone;
    @BindView(R.id.rb_restart)
    RadioButton rbRestart;
    @BindView(R.id.rb_reverse)
    RadioButton rbReverse;
    @BindView(R.id.rg_repeat_mode_group)
    RadioGroup rgRepeatModeGroup;
    @BindView(R.id.et_start_value)
    EditText etStartValue;
    @BindView(R.id.et_end_value)
    EditText etEndValue;
    @BindView(R.id.et_interval)
    EditText etInterval;
    @BindView(R.id.et_step_size)
    EditText etStep;

    private HandlerCounter mCounter;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mCounter = new HandlerCounter()
                .startValue(1)
                .endValue(100)
                .stepSize(1)
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: ");

        if (mCounter != null) {
            mCounter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @OnClick({R.id.bt_start,
            R.id.bt_stop,
            R.id.bt_pause,
            R.id.bt_restart,
            R.id.bt_apply})
    public void onViewClicked(View view) {
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
                        .stepSize(readValue(etStep, 1))
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

    private int readValue(TextView textView, int defaultValue) {
        String text = textView.getText().toString().trim();
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }
}
