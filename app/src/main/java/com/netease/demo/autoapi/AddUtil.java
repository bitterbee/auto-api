package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiClassBuildMethodAnno;
import com.netease.libs.autoapi.anno.AutoApiConstructAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2018/11/25.
 */
@AutoApiClassAnno
public class AddUtil {

    private int mData1 = 0;
    private int mData2 = 0;

    @AutoApiConstructAnno
    public AddUtil(int data1, int data2) {
        mData1 = data1;
        mData2 = data2;
    }

    @AutoApiConstructAnno
    public AddUtil(int data) {
        mData1 = data;
        mData2 = data;
    }

    @AutoApiClassBuildMethodAnno
    public static AddUtil getInstance(int data1, int data2) {
        return new AddUtil(data1, data2);
    }

    @AutoApiMethodAnno
    public int calu() {
        return mData1 + mData2;
    }

    @AutoApiMethodAnno
    public static int add(int a, int b) {
        return a + b;
    }
}
