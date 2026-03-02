package org.xiaoli.xiaolicommonsecurity.domain.dto;


import lombok.Data;
import org.xiaoli.xiaolicommondomain.domain.vo.TokenVO;

/**
 * token信息
 */
@Data
public class TokenDTO {

    /**
     * 访问令牌
     */
    private String accessToken;


    /**
     * 过期时间
     */
    private Long expires;


    /**
     * 转化TokenVO
     * @return
     */
    public TokenVO convertTokenVO() {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(this.accessToken);
        tokenVO.setExipre(this.expires);
        return tokenVO;
    }
}
