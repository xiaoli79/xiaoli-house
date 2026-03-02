package org.xiaoli.xiaolicommoncore.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void matches() {
        String pattern1 = "/admin/**/bc";
        String pattern2 = "/file/**/bc";
        List<String> patternList = new ArrayList<>();
        patternList.add(pattern1);
        patternList.add(pattern2);
        System.out.println(StringUtil.matches(patternList,"/admin/ew/wqe/wqeqw/eqwe/bc"));
        System.out.println(StringUtil.matches(patternList,"/file/sewq/ewq/ewqeqwe/eqwe23/323/bc"));
        System.out.println(StringUtil.matches( patternList,"a/a/bc"));
    }


    @Test
    void isMatch() {
    //精确匹配
        String pattern1 = "/sys/bc";
        System.out.println(StringUtil.isMatch(pattern1,"/sys/bc"));
        System.out.println(StringUtil.isMatch(pattern1,"/scs/bc"));
//      ?表示单个字符
        String pattern2 = "/sys/?bc";
        System.out.println(StringUtil.isMatch(pattern2,"/sys/bc"));
        System.out.println(StringUtil.isMatch(pattern2,"/sys/abc"));
//      *表示多个字符，但是不可跨域，就是不能够跨越/
        String pattern3 = "/sys/*bc";
        System.out.println(StringUtil.isMatch(pattern3,"/sys/a/bc"));
        System.out.println(StringUtil.isMatch(pattern3,"/sys/assasdasdbc"));

//      **表示多个字符，也可以跨域
        String pattern4 = "/sys/**/bc";
        System.out.println(StringUtil.isMatch(pattern4,"/sys/a123123/2/bc"));
        System.out.println(StringUtil.isMatch(pattern4,"/sys/assasdasdbc/abc"));
    }
}