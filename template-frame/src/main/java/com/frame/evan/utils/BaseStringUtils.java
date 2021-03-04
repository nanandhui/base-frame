package com.frame.evan.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @program base-frame 
 * @description:
 * @author: wang
 * @create: 2021/03/02 17:16 
 */
public class BaseStringUtils {
    public static String formatIndex(String message, Object... params) {
        if (message == null || params == null || params.length == 0) {
            return message;
        }
        String txt;
        for (int i = 0; i < params.length; i++) {
            txt = convert2String(params[i]);
            if (txt != null) {
                message = message.replaceAll("\\{" + (i + 1) + '}', txt);
            }
        }
        return message;
    }

    /**
     * [SAFE] 转换输出 String
     */
    public static String convert2String(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return DateUtil.format((Date) value,"yyyy-MM-dd HH:mm:ss");
        } else {
            return value.toString();
        }
    }
}
