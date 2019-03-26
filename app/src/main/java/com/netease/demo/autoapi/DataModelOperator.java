package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2019/3/26.
 */
@AutoApiClassAnno
public class DataModelOperator {

    @AutoApiMethodAnno
    public static DataModel add(DataModel a, DataModel b) {
        return a == null || b == null ?
                null :
                new DataModel(a.getA() + b.getA(), a.getB() + b.getB());
    }
}
