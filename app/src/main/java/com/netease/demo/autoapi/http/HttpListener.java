package com.netease.demo.autoapi.http;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;

/**
 * Created by zyl06 on 2018/11/5.
 */
@AutoApiClassAnno(allPublicNormalApi = true, includeSuperApi = true)
public interface HttpListener extends BaseHttpListener {
    void onCancel();
}
