package com.notsay.yuwenshop.common.constants;

/**
 * @description: 正则表达式常量集合
 * @author: dsy
 * @date: 2021/8/23 14:03
 */
public class RegexPattern {
    /**
     * 中国境内手机号码正则表达式
     * See Also: https://github.com/VincentSit/ChinaMobilePhoneNumberRegex
     */
    public final static String MOBILE_ALL = "^(?=\\d{11}$)^1(?:3\\d|4[57]|5[^4\\D]|66|7[^249\\D]|8\\d|9[89])\\d{8}$";

    /**
     * 密码校验规则,必须是包含大写字母、小写字母、数字、特殊符号（不是字母，数字，下划线，汉字的字符）的8位以上组合
     * https://www.cnblogs.com/feynman61/p/9419640.html
     */
    public final static String PASSWORD_REGEX1 = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

    /**
     * 密码校验规则,8到16长度，不能是纯数字或者纯字母
     * https://cloud.tencent.com/developer/article/1054252
     */
    public final static String PASSWORD_REGEX2 = "^(?![0-9]+$)(?![a-zA-Z]+$)[\\S]{8,16}$";


    public static void main(String[] args) {
        String pwd = null;
        System.out.println(pwd.matches(PASSWORD_REGEX2));
    }
}