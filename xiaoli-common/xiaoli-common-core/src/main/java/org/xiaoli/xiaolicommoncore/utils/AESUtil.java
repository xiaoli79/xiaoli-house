package org.xiaoli.xiaolicommoncore.utils;


import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;

import java.nio.charset.StandardCharsets;

/**
 * AES加密工具类
 */
public class AESUtil {


    /**
     * 前后端规定的秘钥
     */
    private static final byte[] KEYS = "12345678abcdefgh".getBytes(StandardCharsets.UTF_8);


    /**
     * 加密
     * @param data 原始数据
     * @return 加密后的数据
     */
    public static String encryptHex(String data){
//      判断为空逻辑~~
        if(StringUtils.isEmpty(data)){
            throw new ServiceException("原始数据为空");
        }
        return SecureUtil.aes(KEYS).encryptHex(data);
    }


    /**
     * aes解密
     * @param data
     * @return 原始数据
     */
    public static String decryptHex(String data){
        if(StringUtils.isNotBlank(data)){
            return SecureUtil.aes(KEYS).decryptStr(HexUtil.decodeHex(data));
        }
        return null;
    }

}
