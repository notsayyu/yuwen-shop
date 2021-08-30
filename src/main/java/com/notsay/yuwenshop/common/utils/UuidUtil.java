package com.notsay.yuwenshop.common.utils;

import java.util.UUID;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/29 11:03
 */
public class UuidUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}