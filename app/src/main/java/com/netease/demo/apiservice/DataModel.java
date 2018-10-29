package com.netease.demo.apiservice;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice.anno.ApiServiceConstructAnno;

/**
 * Created by zyl06 on 2018/10/27.
 */

public class DataModel {

    public int a;
    public long b;
    private float c;

    @ApiServiceConstructAnno()
    public DataModel(int a, long b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }


}
