package com.netease.demo.autoapi.test;

import com.netease.demo.autoapi.http.HttpListener;
import com.netease.demo.autoapi.http.Request;
import com.netease.libs.autoapi.anno.AutoApiCallbackAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2018/11/8.
 */
public class BaseWzpCommonRequestTask {

    /**
     * async query for the task.
     *
     * @param listener the callback listener for the response.
     * @return the request for the task to be cancelled.
     */
    @AutoApiMethodAnno()
    public Request query(@AutoApiCallbackAnno HttpListener listener) {
        return null;
    }



    @AutoApiMethodAnno()
    public Request queryArray(@AutoApiCallbackAnno HttpListener listener) {
        return null;
    }
}