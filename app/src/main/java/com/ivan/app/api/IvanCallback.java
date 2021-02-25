package com.ivan.app.api;

/**
 * 回调处理接口
 */
public interface IvanCallback {

    /**
     * 成功的回调方法
     * @param res
     */
    void onSuccess(String res);

    /**
     * 失败的回调方法
     * @param e
     */
    void onFailure(Exception e);
}
