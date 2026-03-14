package org.xiaoli.xiaoliportalservice.homepage.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DictsVO implements Serializable {
    private Long id;
    private String key;
    private String name;
}