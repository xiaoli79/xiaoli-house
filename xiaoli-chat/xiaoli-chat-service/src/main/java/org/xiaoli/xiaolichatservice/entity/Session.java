package org.xiaoli.xiaolichatservice.entity;


import lombok.Data;
import org.xiaoli.xiaolicommoncore.domain.entity.BaseDO;

@Data
public class Session extends BaseDO {

    private Long userId1;
    private Long userId2;
}
