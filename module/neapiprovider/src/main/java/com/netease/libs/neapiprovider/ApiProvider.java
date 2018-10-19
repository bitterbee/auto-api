package com.netease.libs.neapiprovider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 2018/10/19.
 */
public class ApiProvider {

    public static final Map<String, Object> APIS = new HashMap<>();

    public static <T> T getApi(String name) {
        return (T) APIS.get(name);
    }
}
