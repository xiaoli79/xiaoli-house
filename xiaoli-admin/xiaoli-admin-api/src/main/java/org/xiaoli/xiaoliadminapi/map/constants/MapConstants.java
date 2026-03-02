package org.xiaoli.xiaoliadminapi.map.constants;




public class MapConstants {

    /**
     * 城市级别
     */
    public static final Integer CITY_LEVEL = 2;

    /**
     *城市列表缓存key
     */
    public static final String CACHE_MAP_CITY_KEY = "map:city:id";


    /**
     * 城市拼音缓存key
     */
    public static final String CACHE_MAP_CITY_PINYIN_KEY = "map:city:pinyin";



    public static final String CACHE_MAP_CITY_CHILDREN_KEY = "map:city:children";

    /**
     * 获取热门城市
     */
    public static final String CACHE_MAP_HOT_CITY = "map:city:hot";


    /**
     * 根据关键字搜索的接口路由~~
     */
    public static final String QQMAP_API_PLACE_SUGGESTION = "/ws/place/v1/suggestion";


    /**
     * 根据经纬度来获取区域信息的接口路由
     */
    public final static String QQMAP_GEOCODER = "/ws/geocoder/v1";


    /**
     * 热门城市的键
     */
    public final static String CONFIG_KEY = "sys_hot_city";



}
