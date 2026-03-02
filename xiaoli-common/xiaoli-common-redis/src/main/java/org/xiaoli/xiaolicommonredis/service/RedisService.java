package org.xiaoli.xiaolicommonredis.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.xiaoli.xiaolicommoncore.utils.JsonUtil;
import java.util.*;
import java.util.concurrent.TimeUnit;



@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;



//    ************************基本操作****************************
    /**
     * 设置数据有效时间（时间默认是s）
     * @param key
     * @param timeout
     * @return
     */
    public Boolean expire(final String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }


    /**
     * redis设置数据有效时间（可指定时间单位）
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    public Boolean expire(final String key, final long timeout, final TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }


    /**
     * 获取有效时间
     * @param key
     * @return
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据提供的键模式查找Redis中匹配的键
     * @param pattern  要查找的键的模式
     * @return 键列表
     */
    public Collection<String> keys(final String pattern){
        return redisTemplate.keys(pattern);
    }


    /**
     * 重命名key
     * @param oldKey  原来key
     * @param newKey  新key
     */
    public void renameKey(String oldKey, String newKey){
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 删除单个数据
     * @param key
     * @return
     */
    public boolean deleteObject(final String key){
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个数据
     * @param collection
     * @return
     */
    public boolean deleteObjects(final Collection<String> collection){
        return redisTemplate.delete(collection) > 0;
    }


//    ************************操作String类型*******************************

//  缓存String类型的数据~~
    public <T> void setCacheObject(final String key,final T value){
        //通常和方法的返回值一样~~
        redisTemplate.opsForValue().set(key,value);
    }

//  缓存String类型的数据~~并且设置有效时间
    public <T> void setCacheObject(final String key,final T value,final Long timeout,final TimeUnit timeUnit){
        //通常和方法的返回值一样~~
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }


//  如果没有key，则设置，如果有key,则不设置~~
    public <T> Boolean setCacheObjectIfAbsent(final String key, final T value, long timeout, TimeUnit seconds){
        return redisTemplate.opsForValue().setIfAbsent(key,value);
    }


//  如果没有key，则设置，如果有key,则不设置~~ 并且设置有超时时间~~
    public <T> void setCacheObjectIfAbsent(final String key,final T value,final Long timeout,final TimeUnit timeUnit){
        //通常和方法的返回值一样~~
        redisTemplate.opsForValue().setIfAbsent(key,value,timeout,timeUnit);
    }

//  得到键对应的值  （将缓存的数据反序列化为指定类型返回）
    public <T> T getCacheObject(final String key,final Class<T> clazz){
        ValueOperations valueOperations = redisTemplate.opsForValue();


        Object o = valueOperations.get(key);
//      判断得到的对象是否为空~~
        if(o == null){
            return null;
        }
//      先序列化
        String jsonStr = JsonUtil.obj2String(o);
//      反序列化
        return JsonUtil.string2Obj(jsonStr,clazz);

    }

//  得到键对应的值  （将缓存的数据反序列化为指定类型返回）
    public <T> T getCacheObject(final String key, TypeReference<T> valueTypeRef){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get(key);
//      判断o是否为空
        if(o == null){
            return null;
        }
        String jsonStr = JsonUtil.obj2String(o);
        return JsonUtil.string2Obj(jsonStr,valueTypeRef);
    }


    //************************操作List类型******************************
    /**
     * 缓存List数据
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     * @param <T> 对象类型
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 从List结构左侧插入数据
     * @param key
     * @param dataList
     * @param <T>
     */


    public <T> void leftPushForList(final String key, final List<T> dataList) {
        redisTemplate.opsForList().leftPush(key, dataList);
    }

    /**从List结构右侧插入数据
     * 从List
     * @param key
     * @param dataList
     * @param <T>
     */

    public <T> void rightPushForList(final String key, final List<T> dataList) {
        redisTemplate.opsForList().rightPush(key, dataList);
    }

    /**
     * 删除左侧第一个数据
     * @param key
     */
    public void leftPopForList(final String key) {
        redisTemplate.opsForList().leftPop(key);
    }


    /**
     * 删除右侧第一个数据
     * @param key
     */
    public void rightPopForList(final String key) {
        redisTemplate.opsForList().rightPop(key);
    }


    /**
     * 移除List第一个匹配的元素
     *
     * @param key key
     * @param value 值
     * @param <T> 值类型
     */
    public <T> void removeForList(final String key, T value) {
        redisTemplate.opsForList().remove(key, 1L, value);
    }
    //⬆️
    //如果count > 0 ，就是从左向右删除count个与value匹配的元素
    //如果count ==0 ，就是删除所有与value相关的元素
    //如果count < 0 , 就是从右向左删除count个与value匹配的元素

    /**
     * 移除List中匹配第所有列表元素
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void removeForAllList(final String key,T value) {
        redisTemplate.opsForList().remove(key, 0, value);
    }


    /**
     * 移除key下所有列表元素
     * @param key
     */
    //这里面的区间是闭区间，也就是保留[1,3]（假设）之间的数据
    public void removeForAllList(final String key) {

        redisTemplate.opsForList().trim(key, -1,0);

    }

    /**
     * 根据下表进行更新列表中的元素
     * @param key
     * @param index
     * @param value
     * @param <T>
     */
    public <T> void setElementAtIndex(final String key, final int index, final T value) {
        redisTemplate.opsForList().set(key, index, value);
    }


    /**
     * 获得缓存的list对象,list中不能有模板
     * @param key key 缓存的键值
     * @param clazz 对象的类
     * @return 列表
     * @param <T> 对象类型
     */
    public <T> List<T> getCacheList(final String key, Class<T> clazz) {
        List list = redisTemplate.opsForList().range(key, 0, -1);
        return JsonUtil.string2List(JsonUtil.obj2String(list), clazz);
    }


    /**
     * 获得缓存的list对象,list中可以支持复杂模板类
     * @param key key信息
     * @param typeReference 类型模板
     * @return list对象
     * @param <T> 对象类型
     */
    public <T> List<T> getCacheList(final String key, TypeReference<List<T>> typeReference) {
        List list = redisTemplate.opsForList().range(key, 0, -1);
        List<T> res = JsonUtil.string2Obj(JsonUtil.obj2String(list), typeReference);
        return res;
    }

    /**
     * 根据范围获取List
     *
     * @param key key
     * @param start 开始位置
     * @param end 结束位置
     * @param clazz 类信息
     * @return List列表
     * @param <T> 类型
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, Class<T> clazz) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2List(JsonUtil.obj2String(range), clazz);
    }


    /**
     * 根据范围获取List
     *
     * @param key key
     * @param start 开始
     * @param end 结果
     * @param typeReference 类型模板
     * @return list列表
     * @param <T> 类型信息
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, TypeReference<List<T>> typeReference) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return JsonUtil.string2Obj(JsonUtil.obj2String(range), typeReference);
    }


    /**
     * 获取指定列表长度
     * @param key key信息
     * @return 列表长度
     */
    public long getCacheListSize(final String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0L : size;
    }


    //************************ 操作Set类型 ***************************
    /**
     * set添加元素（批量添加或添加单个元素）
     * @param key key
     * @param member 元素信息
     */
    public void addMember(final String key, Object... member) {
        redisTemplate.opsForSet().add(key, member);
    }


    /**
     * 删除元素
     * @param key key
     * @param member 元素信息
     */
    public void deleteMember(final String key, Object... member) {
        redisTemplate.opsForSet().remove(key, member);
    }


    /**
     * 获取set数据（支持复杂的泛型嵌套）
     * @param key key
     * @param typeReference 类型模板
     * @return set数据
     * @param <T> 类型信息
     */
    public <T> Set<T> getCacheSet(final String key, TypeReference<Set<T>> typeReference) {
        Set data = redisTemplate.opsForSet().members(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }



    //************************ 操作ZSet类型 ***************************
    /**
     * 添加元素
     * @param key key
     * @param value 值
     * @param seqNo 分数
     */
    public void addMemberZSet(String key, Object value, double seqNo) {
        redisTemplate.opsForZSet().add(key, value, seqNo);
    }

    /**
     * 删除元素
     * @param key    key
     * @param value  值
     */
    public void delMemberZSet(String key, Object value) {
        redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 根据排序分值删除
     *
     * @param key key
     * @param minScore 最小分
     * @param maxScore 最大分
     */
    public void removeZSetByScore(final String key, double minScore, double maxScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }


    /**
     * 获取有序集合数据（支持复杂的泛型嵌套）
     *
     * @param key key信息
     * @param typeReference 类型模板
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSet(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set data = redisTemplate.opsForZSet().range(key, 0, -1);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 降序获取有序集合（支持复杂的泛型嵌套）
     * @param key key信息
     * @param typeReference 类型模板
     * @return 降序的有序集合
     * @param <T> 对象类型信息
     */
    public <T> Set<T> getCacheZSetDesc(final String key, TypeReference<LinkedHashSet<T>> typeReference) {
        Set data = redisTemplate.opsForZSet().reverseRange(key, 0, -1);

        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    //************************ 操作Hash类型 ***************************

    /**
     * 缓存Map数据
     * @param key key
     * @param dataMap map
     * @param <T> 对象类型
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 往Hash中存入单个数据
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     * @param <T> 对象类型
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获取缓存的map数据（支持复杂的泛型嵌套）
     * @param key key
     * @param typeReference 类型模板
     * @return hash对应的map
     * @param <T> 对象类型
     */
    public <T> Map<String, T> getCacheMap(final String key, TypeReference<Map<String, T>> typeReference) {
        Map data= redisTemplate.opsForHash().entries(key);
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }

    /**
     * 获取Hash中的单个数据
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     * @param <T> 对象类型
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取Hash中的多个数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @param typeReference 对象模板
     * @return 获取的多个数据的集合
     * @param <T> 对象类型
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<String> hKeys, TypeReference<List<T>> typeReference) {


//      这个方法会从Redis的Hash中获取多个值,当我的RedisTemplate会对value序列化成Json字符串存储在Redis中，所以在
//      根据这些key中拿取value的时候，这些value的类型是String！！也就是List<String>
        List data = redisTemplate.opsForHash().multiGet(key, hKeys);
//      然后对List进行序列化~~ 然后在反序列化拿到对象，然后就巧妙地获取了List<String>
        return JsonUtil.string2Obj(JsonUtil.obj2String(data), typeReference);
    }


    //************************ Lua脚本 ***************************

    /**
     * 删除指定值对应的Redis中的键值（compareanddelete）
     *
     * @param key   缓存key
     * @param value value
     * @return 是否完成了比较并删除
     */
    public boolean cad(String key, String value) {
        if (key.contains(StringUtils.SPACE) || value.contains(StringUtils.SPACE))
            return false;

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        // 通过Lua脚本原子验证令牌和删除令牌
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value);
        return !Objects.equals(result, 0L);
    }


}



