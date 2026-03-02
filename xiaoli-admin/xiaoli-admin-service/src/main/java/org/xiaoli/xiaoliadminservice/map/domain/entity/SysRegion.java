package org.xiaoli.xiaoliadminservice.map.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.xiaoli.xiaolicommoncore.domain.entity.BaseDO;

@TableName("sys_region")
@Data
public class SysRegion extends BaseDO {

    /**
     * 简称
     */
    private String name;


    /**
     * 区域全称
     */
    private String fullName;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 城市拼音
     */
    private String pinyin;

    /**
     * 城市级别
     */
    private Integer level;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;


    /**
     *区域编码
     */
    private String code;


    /**
     * 父级区域编码
     */
    private String parentCode;

}
