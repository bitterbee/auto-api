package com.netease.demo.apiservice.test;

import com.netease.demo.apiservice.http.HttpListener;
import com.netease.demo.apiservice.http.Request;
import com.netease.libs.apiservice.anno.ApiServiceMethodAnno;

/**
 * Created by zyl06 on 2018/11/8.
 */
public class BaseWzpCommonRequestTask {

    public BaseWzpCommonRequestTask() {
        super();
    }

    /**
     * async query for the task.
     *
     * @param listener the callback listener for the response.
     * @return the request for the task to be cancelled.
     */
    // ????? 持有住Request返回值，提供接口做cancel
    @ApiServiceMethodAnno()
    public Request query(final HttpListener listener) {
        return null;
    }



    @ApiServiceMethodAnno()
    public Request queryArray(HttpListener listener) {
        return null;
    }
}