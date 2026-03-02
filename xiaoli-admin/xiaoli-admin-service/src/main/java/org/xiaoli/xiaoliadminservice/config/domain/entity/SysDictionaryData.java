package org.xiaoli.xiaoliadminservice.config.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@TableName("sys_dictionary_data")
public class SysDictionaryData {


    /**
     * 主键ID，并且可以自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型业务主键
     */
    private String typeKey;

    /**
     * 字典数据业务主键
     */
    private String dataKey;

    /**
     * 字典数据名称
     */
    private String value;

    /**
     * 字典数据排序
     */
    private Integer sort;

    /**
     * 字典数据的备注
     */
    private String remark;


    /**
     * 状态  1:正常，0:停用
     */
    private String status;
}
