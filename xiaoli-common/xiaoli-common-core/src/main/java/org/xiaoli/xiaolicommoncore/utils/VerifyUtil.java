package org.xiaoli.xiaolicommoncore.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用校验工具类
 */
public class VerifyUtil {


    /**
     * 手机号的正则校验
     */
    public static final Pattern PHONE_PATTERN = Pattern.compile("^1[2-9][0-9]\\d{8}$");


    /**
     * 手机号校验
     * @param phone 手机号
     * @return
     */
    public static boolean checkPhone(String phone) {

        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }











}
