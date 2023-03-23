package com.example.springdemo.service.cache;

import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 * 缓存服务<br/>
 * 每一种cacheType都采用独立MAP保存数据
 * string类型与非string类型存在问题
 */
public interface CacheService {

    /**
     * 将 key 缓存
     * @param key       缓存对象(value默认为"")
     * @param cacheType 缓存类型
     */
    void cache(String key, CacheType cacheType);

    /**
     * 将key-value缓存
     * @param key      缓存对象key
     * @param value    缓存对象value
     * @param cacheType 缓存类型
     */
    void cache(String key, String value, CacheType cacheType);

    /**
     * 将key-value缓存
     * @param key      缓存对象key
     * @param value    缓存对象value
     * @param cacheType 缓存类型
     */
    void cacheToJson(String key, Object value, CacheType cacheType);

    /**
     * 判断是否存在缓存中
     * @param key       根据cache时的key
     * @param cacheType 缓存类型
     */
    boolean contains(String key, CacheType cacheType);

    /** 删除缓存 */
    void remove(String key, CacheType cacheType);

    /**
     * key_value缓存时,根据key获取value对象
     * @param key       根据cache时的key
     * @param cacheType 缓存类型
     */
    String get(String key, CacheType cacheType);

    /**
     * key_value缓存时,根据key获取value对象
     * @param key       根据cache时的key
     * @param cacheType 缓存类型
     */
    <T> T getFromJson(String key, Class<T> clazz, CacheType cacheType);


    /**
     * key_value缓存时,根据key获取value对象
     * @param key       根据cache时的key
     * @param cacheType 缓存类型(List中的泛型)
     */
    <T> List<T> getListFromJson(String key, TypeReference<List<T>> typeReference, CacheType cacheType);

}
