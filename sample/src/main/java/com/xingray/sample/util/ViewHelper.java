package com.xingray.sample.util;

import android.view.View;

/**
 * xxx
 *
 * @author : leixing
 * @version : 1.0.0
 * mail : leixing1012@qq.com
 * @date : 2019/7/22 17:08
 */
public class ViewHelper {
    public static void setOnClick(View rootView, int[] ids, View.OnClickListener listener) {
        if (rootView == null || ids == null || ids.length == 0) {
            return;
        }

        for (int id : ids) {
            View view = rootView.findViewById(id);
            if (view == null) {
                continue;
            }
            view.setOnClickListener(listener);
        }
    }
}
