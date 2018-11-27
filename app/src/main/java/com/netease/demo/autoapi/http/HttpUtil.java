package com.netease.demo.autoapi.http;

import com.netease.libs.autoapi.anno.AutoApiCallbackAnno;
import com.netease.libs.autoapi.anno.AutoApiClassAnno;

import java.util.List;

/**
 * Created by zyl06 on 2018/11/5.
 */

@AutoApiClassAnno(allPublicNormalApi = true)
public class HttpUtil {

    public Request query(@AutoApiCallbackAnno HttpListener listener) {
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
