package org.xiaoli.xiaolichatservice.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionAddReqDTO {

    /**
     * 用户1
     */
    @NotNull(message = "1用户id不能为空")
    private Long userId1;

    /**
     * 用户2
     */
    @NotNull(message = "2用户id不能为空")
    private Long userId2;


}
