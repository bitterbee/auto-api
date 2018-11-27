package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiConstructAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

import java.util.Timer;

/**
 * Created by zyl06 on 2018/10/18.
 */
@AutoApiClassAnno(name = "AppCalculator", allPublicStaticApi = true)
public class Calculator {

    private int mIncrement = 2;

    @AutoApiConstructAnno()
    public Calculator() {

    }

    @AutoApiConstructAnno(alias = "newWithIncr")
    public Calculator(int incre) {
        this.mIncrement = incre;
    }

    @AutoApiMethodAnno(alias = "appMinus")
    public static int minus(int a, int b) {
        return a - b;
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static float add(float a, float b) {
        return a + b;
    }

    @AutoApiMethodAnno(provide = false)
    public static double innerAdd(double a, double b) {
        return a + b;
    }

    public static Timer getTimer() {
        return null;
    }

    @AutoApiMethodAnno(provide = true)
    public double nonStaticAdd(double a, double b) {
        return a + b;
    }

    @AutoApiMethodAnno(provide = true, alias = "increase")
    public int incr(int a) {
        return a + mIncrement;
    }

    @AutoApiMethodAnno(provide = true, alias = "decrease")
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
