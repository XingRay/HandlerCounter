package com.leixing.handlercounter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leixing.lib.handlercounter.CountListener;
import com.leixing.lib.handlercounter.HandlerCounter;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 14:17
 */
public class MainActivity extends AppCompatActivity {

    private HandlerCounter mCounter;
    private TextView tvText;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tv_text);

        mCounter = new HandlerCounter()
                .startValue(100)
                .endValue(10000)
                .countStep(1)
                .countInterval(1000)
                .strictMode(true)
                .countListener(new CountListener() {
                    @Override
                    public void onCount(long count) {
                        String text = count + "s";
                        tvText.setText(text);
                    }
                });

        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCounter.start();
            }
        });

        findViewById(R.id.bt_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCounter.stop();
            }
        });

        findViewById(R.id.bt_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCounter.pause();
            }
        });

        findViewById(R.id.bt_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCounter.restart();
            }
        });

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
}
