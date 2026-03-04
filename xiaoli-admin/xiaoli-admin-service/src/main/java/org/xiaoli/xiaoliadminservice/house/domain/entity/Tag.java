package org.xiaoli.xiaoliadminservice.house.domain.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.xiaoli.xiaolicommoncore.domain.entity.BaseDO;

/**
 * @author: yibo
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Tag extends BaseDO {

    private String tagCode;

    private String tagName;

}