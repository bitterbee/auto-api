package com.netease.libs.apiservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyl06 on 2018/10/19.
 */
public class ApiService {

    public static final Map<String, Object> API_FACTORYS = new HashMap<>();

    public static <T> T getApiFactory(String name) {
        return (T) API_FACTORYS.get(name);
    }
}
