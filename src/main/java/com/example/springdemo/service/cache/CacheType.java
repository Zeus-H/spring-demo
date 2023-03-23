package com.example.springdemo.service.cache;

/**
 * 缓存采用MAP结构
 */
public enum CacheType {
    access_statistics("trade:demo:api", 4 * 24 * 60, DataType.LIST),
    demo("trade:demo:channelsn", 30 * 24 * 60, DataType.MAP);

    /**
     * redis key
     */
    private String cacheKey;
    /**
     * 缓存时间(分钟)
     */
    private int cacheMinutes;
    /**
     * 数据类型(MAP:默认, LIST, SET)
     */
    private DataType dataType;

    /**
     * 缓存结构(默认Map)
     */
    public enum DataType {MAP, LIST, SET}

    /**
     * Link to {@link #cacheKey }
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     * Link to {@link #cacheMinutes }
     */
    public int getCacheMinutes() {
        return cacheMinutes;
    }

    /**
     * Link to {@link #dataType }
     */
    public DataType getDataType() {
        return dataType;
    }

    CacheType(String cacheKey, int cacheMinutes, DataType dataType) {
        this.cacheKey = cacheKey;
        this.cacheMinutes = cacheMinutes;
        this.dataType = dataType;
    }

    public String getKey(String id) {
        return cacheKey + ":" + id;
    }
}
