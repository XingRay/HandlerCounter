package com.xingray.sample.page.main;

import android.view.View;
import android.widget.TextView;

import com.xingray.recycleradapter.LayoutId;
import com.xingray.recycleradapter.ViewHolder;
import com.xingray.sample.R;

/**
 * xxx
 *
 * @author : leixing
 * @version : 1.0.0
 * mail : leixing1012@qq.com
 * @date : 2019/7/28 16:46
 */
@LayoutId(R.layout.item_test)
public class TestViewHolder extends ViewHolder<Test> {
    private TextView tvName;

    public TestViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
    }

    @Override
    protected void onBindItemView(Test test, int i) {
        tvName.setText(test.getName());
    }
}
