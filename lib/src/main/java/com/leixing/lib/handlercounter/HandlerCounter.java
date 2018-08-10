package com.leixing.lib.handlercounter;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

import static com.leixing.lib.handlercounter.CounterHandler.MSG_COUNT;

/**
 * description : 计数器
 *
 * @author : leixing
 * email : leixing@baidu.com
 * @date : 2018/8/9 14:17
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HandlerCounter {
    private static final String TAG = HandlerCounter.class.getSimpleName();

    private static final int MIN_INTERVAL_MILLS = 1;
    private static final int DEFAULT_INTERVAL_MILLS = 1000;

    private static final String KEY_PREFIX = HandlerCounter.class.getCanonicalName() + "#";

    private static final String KEY_STATUS = KEY_PREFIX + "mStatus";
    private static final String KEY_PAUSE_TIME = KEY_PREFIX + "mPauseTime";
    private static final String KEY_PAUSE_TIME_MILLS = KEY_PREFIX + "mPauseTimeMills";
    private static final String KEY_START_MILLS = KEY_PREFIX + "mStartTimeMills";
    private static final String KEY_COUNTED = KEY_PREFIX + "mCounted";
    private static final String KEY_CURRENT_VALUE = KEY_PREFIX + "mCurrentValue";
    private static final String KEY_STRICT_MODE = KEY_PREFIX + "mStrictMode";
    private static final String KEY_COUNTER_STEP = KEY_PREFIX + "mCountStep";
    private static final String KEY_COUNTER_INTERVAL = KEY_PREFIX + "mCountInterval";
    private static final String KEY_END_VALUE = KEY_PREFIX + "mEndValue";
    private static final String KEY_START_VALUE = KEY_PREFIX + "mStartValue";

    private long mStartValue = 0;
    private long mEndValue = Long.MAX_VALUE;
    private long mCountInterval = DEFAULT_INTERVAL_MILLS;
    private long mCountStep = 1;
    private boolean mStrictMode = false;

    private long mCurrentValue;
    private long mCounted;
    private long mStartTimeMills;
    private long mPauseTimeMills;
    private long mPauseTime;
    private CounterStatus mStatus;

    private final CounterHandler mHandler;
    private CountListener mCountListener;
    private CounterStatusListener mCounterStatusListener;


    public HandlerCounter() {
        mHandler = new CounterHandler(Looper.getMainLooper());
        mStatus = CounterStatus.IDLE;
    }

    public HandlerCounter startValue(long start) {
        mStartValue = start;
        return this;
    }

    public HandlerCounter endValue(long end) {
        mEndValue = end;
        return this;
    }

    public HandlerCounter countInterval(long mills) {
        if (mills < MIN_INTERVAL_MILLS) {
            throw new IllegalArgumentException();
        }
        mCountInterval = mills;
        return this;
    }

    public HandlerCounter countStep(long step) {
        mCountStep = step;
        return this;
    }

    public HandlerCounter strictMode(boolean strictMode) {
        mStrictMode = strictMode;
        return this;
    }

    public HandlerCounter countListener(CountListener listener) {
        mCountListener = listener;
        return this;
    }

    public HandlerCounter counterStatusListener(CounterStatusListener listener) {
        mCounterStatusListener = listener;
        return this;
    }


    public HandlerCounter start() {
        removeMessage();
        mCurrentValue = mStartValue;
        mCounted = 0;
        mStartTimeMills = System.currentTimeMillis();
        mPauseTime = 0;
        sendMessage();
        mStatus = CounterStatus.RUNNING;
        notifyNewStatus();
        return this;
    }

    public HandlerCounter stop() {
        removeMessage();
        mStatus = CounterStatus.IDLE;
        notifyNewStatus();
        return this;
    }

    public HandlerCounter pause() {
        if (mStatus != CounterStatus.RUNNING) {
            return this;
        }
        mPauseTimeMills = System.currentTimeMillis();
        removeMessage();
        mStatus = CounterStatus.PAUSE;
        notifyNewStatus();
        return this;
    }

    public HandlerCounter restart() {
        if (mStatus != CounterStatus.PAUSE) {
            return this;
        }
        mPauseTime += System.currentTimeMillis() - mPauseTimeMills;
        removeMessage();
        sendMessage();
        mStatus = CounterStatus.RUNNING;
        notifyNewStatus();
        return this;
    }

    public void onSaveInstanceState(Bundle state) {
        state.putLong(KEY_START_VALUE, mStartValue);
        state.putLong(KEY_END_VALUE, mEndValue);
        state.putLong(KEY_COUNTER_INTERVAL, mCountInterval);
        state.putLong(KEY_COUNTER_STEP, mCountStep);
        state.putBoolean(KEY_STRICT_MODE, mStrictMode);
        state.putLong(KEY_CURRENT_VALUE, mCurrentValue);
        state.putLong(KEY_COUNTED, mCounted);
        state.putLong(KEY_START_MILLS, mStartTimeMills);
        state.putLong(KEY_PAUSE_TIME_MILLS, mPauseTimeMills);
        state.putLong(KEY_PAUSE_TIME, mPauseTime);
        state.putInt(KEY_STATUS, mStatus.ordinal());

        removeMessage();
    }

    public void onRestoreInstanceState(Bundle state) {
        mStartValue = state.getLong(KEY_START_VALUE);
        mEndValue = state.getLong(KEY_END_VALUE);
        mCountInterval = state.getLong(KEY_COUNTER_INTERVAL);
        mCountStep = state.getLong(KEY_COUNTER_STEP);
        mStrictMode = state.getBoolean(KEY_STRICT_MODE);
        mCurrentValue = state.getLong(KEY_CURRENT_VALUE);
        mCounted = state.getLong(KEY_COUNTED);
        mStartTimeMills = state.getLong(KEY_START_MILLS);
        mPauseTimeMills = state.getLong(KEY_PAUSE_TIME_MILLS);
        mPauseTime = state.getLong(KEY_PAUSE_TIME);
        mStatus = CounterStatus.fromOrdinal(state.getInt(KEY_STATUS));
        if (mStatus == CounterStatus.RUNNING) {
            sendMessage();
        }
    }

    public long getCurrentValue() {
        return mCurrentValue;
    }

    @UiThread
    void onCount() {
        long currentTimeMillis = System.currentTimeMillis();
        long countTime = currentTimeMillis - mStartTimeMills - mPauseTime;
        long count = countTime / mCountInterval;
        long target = nextValue(mCurrentValue, mEndValue, (count - mCounted) * mCountStep);
        boolean hasNext = true;

        if (mStrictMode) {
            while (mCurrentValue != target) {
                mCurrentValue = nextValue(mCurrentValue, target, mCountStep);
                if (mCountListener != null) {
                    // listener will invoke after every nextValue in strict mode
                    mCountListener.onCount(mCurrentValue);
                }
                mCounted++;
            }
        } else {
            mCurrentValue = target;
            mCounted = count;
            if (mCountListener != null) {
                // listener will invoke once in non strict mode
                mCountListener.onCount(mCurrentValue);
            }
        }

        if (mCurrentValue == mEndValue) {
            return;
        }

        long nextTimeMills = mStartTimeMills + mCounted * mCountInterval;
        long delayMills = Math.max(0, nextTimeMills - currentTimeMillis);
        sendMessage(delayMills);
    }

    private long nextValue(long currentValue, long endValue, long step) {
        long nextValue;
        if (currentValue < endValue) {
            nextValue = Math.min(currentValue + step, endValue);
        } else if (currentValue > endValue) {
            nextValue = Math.max(currentValue - step, endValue);
        } else {
            nextValue = currentValue;
        }
        return nextValue;
    }

    private void sendMessage() {
        sendMessage(0);
    }

    private void sendMessage(long delayMills) {
        Message message = mHandler.obtainMessage(MSG_COUNT);
        message.obj = new WeakReference<>(this);
        mHandler.sendMessageDelayed(message, delayMills);
    }

    private void removeMessage() {
        mHandler.removeMessages(MSG_COUNT);
    }

    private void notifyNewStatus() {
        if (mCounterStatusListener != null) {
            mCounterStatusListener.onNewStatus(mStatus);
        }
    }
}
