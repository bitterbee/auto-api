package com.netease.libs.autoapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 2018/10/19.
 */
public class AutoApi {

    public static void init() {
        // inner call ApiRegister.init();
    }

    public static final Map<String, Object> API_FACTORYS = new HashMap<>();

    public static <T> T getApiFactory(String name) {
        return (T) API_FACTORYS.get(name);
    }
}
