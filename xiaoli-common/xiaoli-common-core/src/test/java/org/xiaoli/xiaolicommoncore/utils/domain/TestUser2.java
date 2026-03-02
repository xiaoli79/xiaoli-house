package org.xiaoli.xiaolicommoncore.utils.domain;

import lombok.Data;

import java.util.Date;
import java.util.Map;


@Data
public class TestUser2 {


    public String name;

    public Integer age;


    public String sex;

    public String QQ;

    public String Wechat;



    private Date date;

    private Map<Date,String> map;

}
