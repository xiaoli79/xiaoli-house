package org.xiaoli.xiaoliadminservice.map.domain.dto;
import lombok.Data;


/**
 * 腾讯位置服务响应基类
 */
@Data
public class QQMapBaseResponseDTO {

    /**
     * 响应码 0标识成功
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 请求ID
     */
    private String request_id;
}
