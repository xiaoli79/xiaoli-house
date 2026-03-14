package org.xiaoli.xiaoliportalservice.homepage.domain.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class CityDescVO implements Serializable {

    private Long id;
    private String name;
    private String fullName;

}
