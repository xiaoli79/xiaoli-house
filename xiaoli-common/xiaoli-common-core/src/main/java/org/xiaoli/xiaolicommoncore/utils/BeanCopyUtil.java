package org.xiaoli.xiaolicommoncore.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BeanCopyUtil extends BeanUtils {

    /**
     * 集合数据的拷贝
     * @param sourceList 数据源类
     * @param targetClass 目标类
     * @return 拷贝结果
     * @param <S> 源目标类型
     * @param <T> 目标对象类型
     */

    public static <S,T> List<T> copyList(List<S> sourceList, Supplier<T> targetClass) {

        List<T> targetList = new ArrayList<T>();
        for (S s : sourceList) {
            T t = targetClass.get(); //创建目标对象~~
            BeanUtils.copyProperties(s, t);
            targetList.add(t);
        }
        return targetList;
    }
}
