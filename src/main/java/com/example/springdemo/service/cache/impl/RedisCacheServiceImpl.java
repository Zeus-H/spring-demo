package com.example.springdemo.service.cache.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.springdemo.service.cache.CacheService;
import com.example.springdemo.service.cache.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 订单缓存服务redis实现，
 * 当完成缓存key切换之后，可以删除redisson的实现。
 * </pre>
 */
@Slf4j
@Service
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void cache(String key, CacheType cacheType) {
        redisTemplate.opsForValue().set(cacheType.getKey(key), "", cacheType.getCacheMinutes(), TimeUnit.MINUTES);
    }

    @Override
    public void cache(String key, String value, CacheType cacheType) {
        if (value == null) {
            value = "";
        }
        redisTemplate.opsForValue().set(cacheType.getKey(key), value, cacheType.getCacheMinutes(), TimeUnit.MINUTES);
    }

    @Override
    public void cacheToJson(String key, Object value, CacheType cacheType) {
        if (value == null || value instanceof String) {
            cache(key, (String) value, cacheType);
        } else {
            cache(key, JSON.toJSONString(value), cacheType);
        }
    }

    @Override
    public boolean contains(String key, CacheType cacheType) {
        return redisTemplate.hasKey(cacheType.getKey(key));
    }

    @Override
    public String get(String key, CacheType cacheType) {
        return redisTemplate.opsForValue().get(cacheType.getKey(key));

//        Object oldResult = getMapCache(cacheType).get(key);
//        if (oldResult != null) {
//            if (oldResult instanceof String) {
//                return oldResult.toString();
//            } else {
//                log.warn("改版前历史缓存数据:" + oldResult);
//                remove(key, cacheType);
//            }
//        }
    }

    @Override
    public <T> T getFromJson(String key, Class<T> clazz, CacheType cacheType) {
        String result = redisTemplate.opsForValue().get(cacheType.getKey(key));
        if (result != null) {
            return JSON.parseObject(result, clazz);
        }

//        Object newResult = getMapCache(cacheType).get(key);
//        if (newResult != null) {
//            if (newResult instanceof String) {
//                return JsonUtil.fromJson(newResult.toString(), clazz);
//            } else {
//                log.warn("改版前历史缓存数据:" + newResult);
//                remove(key, cacheType);
//            }
//        }
        return null;
    }

    @Override
    public <T> List<T> getListFromJson(String key, TypeReference<List<T>> typeReference, CacheType cacheType) {
        String result = redisTemplate.opsForValue().get(cacheType.getKey(key));
        if (result != null) {
            return JSON.parseObject(result, typeReference);
        }

//        Object newResult = getMapCache(cacheType).get(key);
//        if (newResult != null) {
//            if (newResult instanceof String) {
//                return JSON.parseObject(newResult.toString(),typeReference);
//            } else {
//                log.warn("改版前历史缓存数据:" + newResult);
//                remove(key, cacheType);
//            }
//        }
        return Collections.emptyList();
    }

//    private RMapCache<Object, Object> getMapCache(CacheType cacheType) {
//        return redissonClient.getMapCache(cacheType.getCacheKey());
//    }

    @Override
    public void remove(String key, CacheType cacheType) {
        if (key == null) {
            return;
        }
        redisTemplate.delete(cacheType.getKey(key));
    }
}
