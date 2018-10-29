package com.netease.demo.demomodule1;

import com.netease.libs.apiservice.anno.ApiServiceClassAnno;

/**
 * Created by zyl06 on 2018/10/26.
 */
@ApiServiceClassAnno(name = "ModuleFunction", allPublicStaticApi = true)
public class ModuleFunctionUtil {

    public static int multiple(int a, int b) {
        return a * b;
    }
}
