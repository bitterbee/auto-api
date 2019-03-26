package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiConstructAnno;

/**
 * Created by zyl06 on 2018/10/27.
 */
@AutoApiClassAnno
public class DataModel {

    private int a;
    private int b;

    @AutoApiConstructAnno()
    public DataModel(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public DataModel() {
        this.a = 0;
        this.b = 0;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
