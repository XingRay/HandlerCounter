package com.xingray.sample.page.main;

import android.app.Activity;

/**
 * xxx
 *
 * @author : leixing
 * @version : 1.0.0
 * mail : leixing1012@qq.com
 * @date : 2019/7/28 16:45
 */
public class Test {
    private final String name;
    private final Class<? extends Activity> pageClass;

    public Test(String name, Class<? extends Activity> pageClass) {
        this.name = name;
        this.pageClass = pageClass;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Activity> getPageClass() {
        return pageClass;
    }
}
