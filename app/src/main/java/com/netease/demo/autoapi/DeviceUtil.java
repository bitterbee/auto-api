package com.netease.demo.autoapi;

import com.netease.libs.autoapi.anno.AutoApiClassAnno;
import com.netease.libs.autoapi.anno.AutoApiConstructAnno;
import com.netease.libs.autoapi.anno.AutoApiMethodAnno;

/**
 * Created by zyl06 on 2018/10/27.
 */
@AutoApiClassAnno
public class DeviceUtil {

    @AutoApiConstructAnno
    public DeviceUtil() {
    }

    @AutoApiMethodAnno
    public int getDeviceId() {
        return 0;
    }
}
