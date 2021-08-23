package com.notsay.yuwenshop.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/23 14:13
 */
public class GenCodeUtils {
    /**
     * 用户code的数字部分长度
     */
    private static final int USER_CODE_NUMBER_LENGTH = 6;

    /**
     * 生成用户code
     *
     * @param id
     * @return
     */
    public static String genUserCode(int id) {
        return getUserCodeDate() + StringUtils.leftPad(String.valueOf(id), USER_CODE_NUMBER_LENGTH, '0');
    }

    private static String getUserCodeDate() {
        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        return date.substring(2);
    }

    public static void main(String[] args) {
        System.out.println(genUserCode(1));
    }

}