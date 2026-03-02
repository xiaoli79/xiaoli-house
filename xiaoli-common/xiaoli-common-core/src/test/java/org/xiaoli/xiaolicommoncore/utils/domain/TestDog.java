package org.xiaoli.xiaolicommoncore.utils.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDog extends TestAnimal {

    private int age;

    @Override
    public String toString() {
        return "TestDog{" +
                "name=" + getName() +
                ", age=" + age +
                "}";
    }
}
