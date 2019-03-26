package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiConstructAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2019/3/26.
 */
@AutoApiClassAnno
public class Minus {

    private int mData1 = 0;
    private int mData2 = 0;

    @AutoApiConstructAnno
    public Minus(int data1, int data2) {
        mData1 = data1;
        mData2 = data2;
    }

    @AutoApiMethodAnno
    public int calu() {
        return mData1 - mData2;
    }

    @AutoApiMethodAnno
    public static int minus(int a, int b) {
        return a - b;
    }
}
