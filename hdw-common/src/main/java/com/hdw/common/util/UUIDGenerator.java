package com.hdw.common.util;

import java.util.UUID;

/**
 * @Description 生成UUId或随机数字
 * @Author TuMinglong
 * @Date 2018/5/17 15:44
 */
public class UUIDGenerator {

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return temp;
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    public static String getWordName() {
        int num = (int) Math.round(Math.random() * 90000 + 1);
        return String.valueOf(num);
    }

    /**
     * 生成企业Id
     *
     * @param prefix
     * @return
     */
    public static String getEnterpriseId(String prefix) {
        return prefix + getUUID().substring(0, 16);
    }
}
