package org.xiaoli.xiaolicommoncore.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseDO {


    @TableId(value= "id",type= IdType.AUTO)
    private Long id;
}
