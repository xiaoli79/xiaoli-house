package org.xiaoli.xiaolicommondomain.domain.dto;


import lombok.Data;

/**
 * 分页查询类的基类~~
 */
@Data
public class BasePageReqDTO {


    /**
     * 告诉数据库从哪里开始取数据
     */
    private Integer pageNo = 1;

    /**
     * 分页数量
     */
    private Integer pageSize = 10;

    /**
     * 获取偏移
     *
     * @return 偏移信息  就是从哪个地方开始的~~
     */
    public Integer getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
