package com.netease.demo.apiservice;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice.anno.ApiServiceConstructAnno;

/**
 * Created by zyl06 on 2018/10/27.
 */
@ApiServiceClassAnno(name = "AppDataModel")
public class DataModel {

    private int a;
    private int b;

    @ApiServiceConstructAnno()
    public DataModel(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
