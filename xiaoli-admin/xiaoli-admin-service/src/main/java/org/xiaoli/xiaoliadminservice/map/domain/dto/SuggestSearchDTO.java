package org.xiaoli.xiaoliadminservice.map.domain.dto;


import lombok.Data;

/**
 * 搜索的关键字
 */

@Data
public class SuggestSearchDTO {


    /**
     * 搜索的关键字
     */
    private String keyword;


    /**
     * 城市ID
     */
    private String id;

    /**
     * 页码
     */
    private Integer pageIndex;


    /**
     * 每页的大小
     */

    private Integer pageSize;

}
