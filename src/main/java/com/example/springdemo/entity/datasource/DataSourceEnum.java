package com.example.springdemo.entity.datasource;

public enum DataSourceEnum {
    /* demo1 */
    DATASOURCE_01("demo1"),
    /* demo2 */
    DATASOURCE_02("demo2"),

    UNKNOW("未知数据源");

    private final String desc;

    DataSourceEnum(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return this.desc;
    }

    public static DataSourceEnum getByName(String name) {
        for(DataSourceEnum en : DataSourceEnum.values()) {
            if(en.name().equals(name)) {
                return en;
            }
        }
        return UNKNOW;
    }
}
