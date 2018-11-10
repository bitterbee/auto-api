package com.netease.demo.apiservice.http;

import com.netease.libs.apiservice.anno.ApiServiceCallbackAnno;
import com.netease.libs.apiservice.anno.ApiServiceClassAnno;

import java.util.List;

/**
 * Created by zyl06 on 2018/11/5.
 */

@ApiServiceClassAnno(allPublicNormalApi = true)
public class HttpUtil {

    public Request query(@ApiServiceCallbackAnno HttpListener listener) {
        return new Request();
    }

    public Request queryNoCallback(HttpListener listener) {
        return new Request();
    }

    public <T> T get(Class<T> aaaaaa) {
        return null;
    }

    public <T> List<T> toJsonArr(String jsonStr, Class<T> clazz) {
        return null;
    }
}
