package com.netease.demo.apiservice;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;
import com.netease.libs.apiservice.anno.ApiServiceConstructAnno;
import com.netease.libs.apiservice.anno.ApiServiceMethodAnno;

import java.util.Timer;

/**
 * Created by zyl06 on 2018/10/18.
 */
@ApiServiceClassAnno(name = "AppCalculator", allPublicStaticApi = true)
public class Calculator {

    private int mIncrement = 2;

    @ApiServiceConstructAnno()
    public Calculator() {

    }

    @ApiServiceConstructAnno(alias = "newWithIncr")
    public Calculator(int incre) {
        this.mIncrement = incre;
    }

    @ApiServiceMethodAnno(alias = "appMinus")
    public static int minus(int a, int b) {
        return a - b;
    }

    public static int add(int a, int b) {
        return a + b;
    }

    @ApiServiceMethodAnno(provide = false)
    public static float innerAdd(float a, float b) {
        return a + b;
    }

    public static Timer getTimer() {
        return null;
    }

    @ApiServiceMethodAnno(provide = true)
    public double nonStaticAdd(double a, double b) {
        return a + b;
    }

    @ApiServiceMethodAnno(provide = true, alias = "increase")
    public int incr(int a) {
        return a + mIncrement;
    }

    @ApiServiceMethodAnno(provide = true, alias = "decrease")
    public int decr(int a) {
        return a - mIncrement;
    }

    public static int add(DataModel model) {
        return model != null ? add(model.getA(), model.getB()) : 0;
    }

    public static DataModel add(DataModel a, DataModel b) {
        return a == null || b == null ? null :
                new DataModel(a.getA() + b.getA(), a.getB() + b.getB());
    }
}
