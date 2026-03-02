package org.xiaoli.xiaolicommonsecurity.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.xiaoli.xiaolicommondomain.constants.SecurityConstants;
import org.xiaoli.xiaolicommondomain.constants.TokenConstants;

import java.util.Map;

/**
 * Jwt工具类
 */
public class JwtUtil {


    /**
     * 获取令牌密钥~~
     */
    public static String secret = TokenConstants.SECRET;


    /**
     * 从原始数据声明生成令牌
     * @param claims
     * @return
     */
    public static String createToken(Map<String, Object> claims){
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    /**
     * 根据令牌获取数据声明
     * @param token
     * @return
     */
    public static Claims parseToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     * @param token
     * @return
     */
    public static String getUserKey(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据数据声明来获取用户标识
     * @param claims
     * @return
     */
    public static String getUserKey(Claims claims){
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     * @param token
     * @return
     */
    public static String getUserID(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_ID);
    }

    /**
     * 根据数据声明来获取用户ID
     * @param claims
     * @return
     */
    public static String getUserID(Claims claims){
        return getValue(claims, SecurityConstants.USER_ID);
    }
    /**
     * 根据令牌获取用户来源
     * @param token
     * @return
     */
    public static String getUserFrom(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_FROM);
    }

    /**
     * 根据数据声明来获取用户来源
     * @param claims
     * @return
     */
    public static String getUserFrom(Claims claims){
        return getValue(claims, SecurityConstants.USER_FROM);
    }


    /**
     * 根据令牌获取用户名称
     * @param token
     * @return
     */
    public static String getUserName(String token){
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USERNAME);
    }

    /**
     * 根据数据声明来获取用户名称
     * @param claims
     * @return
     */
    public static String getUserName(Claims claims){
        return getValue(claims, SecurityConstants.USERNAME);
    }




    public static String getValue(Claims claims,String key){
        Object value = claims.get(key);
        if(value == null){
            return "";
        }
        return value.toString();
    }







}
