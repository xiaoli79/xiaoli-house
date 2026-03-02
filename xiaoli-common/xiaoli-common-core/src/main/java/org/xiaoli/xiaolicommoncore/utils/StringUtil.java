package org.xiaoli.xiaolicommoncore.utils;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 判断匹配列表是否与url匹配~
     * @param patternList
     * @param url
     * @return
     */

// 看url是否匹配这个匹配列表~~
    public static boolean matches(List<String> patternList,String url){
        if(StringUtils.isEmpty(url) || CollectionUtils.isEmpty(patternList)){
            return false;
        }

        for(String pattern :patternList ){
            if(isMatch(pattern,url)){
                return true;
            }
        }
        return false;
    }



//  判断是否匹配~~
    public static boolean isMatch(String pattern, String url) {
        if(StringUtils.isEmpty(pattern) || StringUtils.isEmpty(url)){
            return false;
        }

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(pattern, url);
    }

}
