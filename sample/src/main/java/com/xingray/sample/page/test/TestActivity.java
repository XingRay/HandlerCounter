package com.xingray.sample.page.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xingray.sample.R;


/**
 * description : xxx
 *
 * @author : leixing
 * email : leixing1012@qq.com
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
