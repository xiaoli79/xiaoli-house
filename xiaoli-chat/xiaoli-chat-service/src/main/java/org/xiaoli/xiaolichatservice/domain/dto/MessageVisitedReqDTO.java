package org.xiaoli.xiaolichatservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageVisitedReqDTO {
    @NotNull(message = "会话id不能为空！")
    private Long sessionId;
}