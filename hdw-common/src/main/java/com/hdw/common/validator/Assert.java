package com.hdw.common.validator;

import com.hdw.common.exception.GlobalException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 * @author TuMinglong
 * @email tuminglong@126.com
 * @date 2018-12-13 15:50
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new GlobalException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new GlobalException(message);
        }
    }
}
