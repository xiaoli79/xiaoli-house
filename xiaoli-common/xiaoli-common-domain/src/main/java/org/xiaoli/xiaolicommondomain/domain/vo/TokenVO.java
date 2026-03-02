package org.xiaoli.xiaolicommondomain.domain.vo;


import lombok.Data;

/**
 * 登录令牌
 */
@Data
public class TokenVO {


    /**
     * 令牌
     */
    private String accessToken;


    /**
     * 过期时间
     */
    private Long exipre;
}
