package org.xiaoli.xiaoliadminservice.config.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;


/**
 * 系统参数表的实体类
 */

@Getter
@Setter
@TableName("sys_argument")
public class SysArgument {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;


    /**
     * 名称
     */
    private String name;


    /**
     * 业务主键
     */
    private String configKey;

    /**
     * 值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;
}
