package com.xingray.handlercounter;

/**
 * description : 计数监听器
 *
 * @author : leixing
 * email : leixing1012@qq.com
 * @date : 2018/8/9 14:17
 */
public interface CountListener {
    /**
     * 计数回调
     *
     * @param count 当前的计数值
     */
    void onCount(long count);
}