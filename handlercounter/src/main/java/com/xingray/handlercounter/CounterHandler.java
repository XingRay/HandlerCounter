package com.xingray.handlercounter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 基于{@link Handler}的计数器实现
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/8/9 14:17
 */
class CounterHandler extends Handler {

    static final int MSG_COUNT = 100;

    CounterHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == MSG_COUNT) {
            handleCountMessage(msg);
        }
    }

    private void handleCountMessage(Message msg) {
        if (!(msg.obj instanceof WeakReference)) {
            return;
        }
        WeakReference reference = (WeakReference) msg.obj;
        Object o = reference.get();
        if (!(o instanceof HandlerCounter)) {
            return;
        }

        HandlerCounter counter = (HandlerCounter) o;
        counter.onCount();
    }
}