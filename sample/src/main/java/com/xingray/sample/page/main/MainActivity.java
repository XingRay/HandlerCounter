package com.xingray.sample.page.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xingray.recycleradapter.ItemClickListener;
import com.xingray.recycleradapter.RecyclerAdapter;
import com.xingray.sample.R;
import com.xingray.sample.page.setting.SettingTestActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * description : 主页
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/8/9 14:17
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rvList;
    private Context mContext;
    private RecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setContentView(R.layout.activity_main);
        rvList = findViewById(R.id.rv_list);

        initList(rvList);

        loadTest();
    }

    private void loadTest() {
        List<Test> testList = new ArrayList<>();
        testList.add(new Test("设置测试", SettingTestActivity.class));
        mAdapter.update(testList);
    }

    private void initList(RecyclerView list) {
        list.setLayoutManager(new LinearLayoutManager(mContext));
        list.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));

        mAdapter = new RecyclerAdapter(mContext)
                .addTypeJ(Test.class, TestViewHolder.class, new ItemClickListener<Test>() {
                    @Override
                    public void onItemClick(ViewGroup viewGroup, int i, Test test) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, test.getPageClass());
                        startActivity(intent);
                    }
                });
        list.setAdapter(mAdapter);
    }
}
