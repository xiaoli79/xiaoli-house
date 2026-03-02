package org.xiaoli.xiaolicommoncore.utils.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCat extends TestAnimal {

    private String sex;

    @Override
    public String toString() {
        return "TestCat{" +
                "name=" + getName() +
                ", sex='" + sex + '\'' +
                "}";
    }
}