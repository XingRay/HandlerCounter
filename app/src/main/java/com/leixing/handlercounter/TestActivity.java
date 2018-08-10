package com.leixing.handlercounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 20:40
 */
public class TestActivity extends AppCompatActivity {

    private Activity mActivity;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, TestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        setContentView(R.layout.activity_test);

        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.tv_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(mActivity);
            }
        });
    }
}
