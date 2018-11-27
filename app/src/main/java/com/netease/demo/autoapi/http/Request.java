package com.netease.demo.autoapi.http;

import android.util.Log;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;

/**
 * Created by zyl06 on 2018/11/5.
 */
@AutoApiClassAnno(allPublicNormalApi = true)
public class Request {

    public void cancel() {
        Log.i("AutoApi", "cancel request");
    }
}
