package com.netease.demo.autoapi.test1;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;

/**
 * Created by zyl06 on 2018/10/18.
 */
@AutoApiClassAnno(name = "CalculatorAlias", allPublicStaticApi = true, includeSuperApi = true)
public class Calculator extends Adder {

    public static int minuse(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }
}
