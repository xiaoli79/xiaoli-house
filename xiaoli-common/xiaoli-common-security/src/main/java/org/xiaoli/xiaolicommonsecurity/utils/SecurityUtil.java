package org.xiaoli.xiaolicommonsecurity.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.xiaoli.xiaolicommoncore.utils.ServletUtil;
import org.xiaoli.xiaolicommondomain.constants.SecurityConstants;
import org.xiaoli.xiaolicommondomain.constants.TokenConstants;

/**
 *安全工具类
 */
public class SecurityUtil {


    /**
     * 获取请求的Token的信息
     * @return
     */
    public static String getToken(){
        return getToken(ServletUtil.getRequest());
    }

    /**
     * 根据request请求获取请求token
     *
     * @param request
     * @return token信息
     */
    public static String getToken(HttpServletRequest request){
        String token = request.getHeader(SecurityConstants.AUTHENTICATION);
        return token;
    }


    /**
     * 裁剪token前缀
     * @param token
     * @return
     */
    public static String replaceTokenPrefix(String token){
        if(StringUtils.isNotBlank(token) && token.startsWith(TokenConstants.PREFIX)){
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }



}
