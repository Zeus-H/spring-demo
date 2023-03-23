package com.example.springdemo.service.cache;

/**
 * 缓存采用MAP结构
 *
 * @author fong on 2019/4/12.
 */
public enum CacheType {

    /**
     * 默认Map缓存24个小时
     */
    default_cache("TRADE_CACHE_DEFAULT", 24 * 60, DataType.MAP),
    /**
     * 默认Map缓存24个小时
     */
    export_cache_task("EXPORT_CACHE_TASK", 24 * 60, DataType.MAP),
//    /**  默认List缓存24个小时 */
//    default_list_cache("trade:default:list", 24*60, DataType.LIST),
    /**
     * 寻源结果, 缓存7天
     */
    source_result("TRADE_CACHE_SOURCE_RESULT", 7 * 24 * 60, DataType.MAP),
    /**
     * 寻源结果处理状态, 缓存24小时
     */
    source_result_handler_status("TRADE_CACHE_SOURCE_RESULT_HANDLER_STATUS", 24 * 60, DataType.MAP),
    /**
     * 淘宝门店 , 缓存24小时
     */
    taobao_store_cache("TRADE_CACHE_TAOBAO_STORE", 24 * 60, DataType.MAP),
    /**
     * 订单取消明细, 缓存7天
     */
    cancel_order_item_id("TRADE_CACHE_CANCEL_ORDER_ITEM_ID", 7 * 24 * 60, DataType.MAP),
    /**
     * 订单取消, 缓存7天
     */
    cancel_order_id("TRADE_CACHE_CANCEL_ORDER_ID", 7 * 24 * 60, DataType.MAP),
    /**
     * 订单id与其它表主键的关系 缓存3个月
     */
    primaryKey_order_id("PRIMARYKEY_ID_RELATION", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 已支付的淘宝订单ID 缓存3个月
     */
    payed_tb_order_id("PAYED_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 已取消的淘宝订单明细 缓存3个月
     */
    canceled_tb_order_item_detail("CANCELED_ORDER_ITEM_DETAIL", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 已支付的京东订单ID 缓存3个月
     */
    payed_jd_order_id("PAYED_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 已取消的京东订单明细 缓存3个月
     */
    canceled_jd_order_item_detail("CANCELED_ORDER_ITEM_DETAIL", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 拣货单ID关联的订单ID集合
     */
    pick_id_mapping_order_id("TRADE_CACHE_PICK_ID_MAPPING_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 退款单ID关联的订单ID
     */
    refund_id_mapping_order_id("TRADE_CACHE_REFUND_ID_MAPPING_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 退货单ID关联的订单ID
     */
    return_id_mapping_order_id("TRADE_CACHE_RETURN_ID_MAPPING_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 配货单ID关联的订单ID集合
     */
    assign_id_mapping_order_id("TRADE_CACHE_ASSIGN_ID_MAPPING_ORDER_ID", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 订单相关的拆分键(订单ID关联)
     */
    order_id_mapping_db_split_key("TRADE_CACHE_ORDER_ID_MAPPING_DB_SPLIT_KEY", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 订单明细相关的拆分键(订单明细ID关联)
     */
    order_item_id_mapping_db_split_key("TRADE_CACHE_ORDER_ITEM_ID_MAPPING_DB_SPLIT_KEY", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 配货单相关的拆分键(配货单ID关联)
     */
    assign_id_mapping_db_split_key("TRADE_CACHE_ASSIGN_ID_MAPPING_DB_SPLIT_KEY", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 退单相关的拆分键(退单ID关联)
     */
    return_id_mapping_db_split_key("TRADE_CACHE_RETURN_ID_MAPPING_DB_SPLIT_KEY", 3 * 30 * 24 * 60, DataType.MAP),
    /**
     * 缓存订单导入结果
     */
    order_sale_import_result("TRADE_CACHE_ORDER_SALE_IMPORT_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存订单导入错误信息
     */
    order_sale_import_result_fail_msg("TRADE_CACHE_ORDER_SALE_IMPORT_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存订单导入结果
     */
    sap_report_import_result("TRADE_CACHE_SAP_REPORT_IMPORT_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存订单导入错误信息
     */
    sap_report_import_result_fail_msg("TRADE_CACHE_SAP_REPORT_IMPORT_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存根据店铺编码和日期重新生成报表导入结果
     */
    day_sap_report_import_rebuild_result("DAY_SAP_REPORT_IMPORT_REBUILD_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存根据店铺编码和日期重新生成报表导入入错误信息
     */
    day_sap_report_import_rebuild_result_fail_msg("DAY_SAP_REPORT_IMPORT_REBUILD_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存批量替换sku结果
     */
    batch_change_sku_result("TRADE_CACHE_ORDER_SALE_CHANGE_SKU_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存批处理结果
     */
    batch_operate_result("trade:batch:operate:result", 60, DataType.MAP),
    /**
     * 缓存批量处理结果
     */
    batch_handle_result("trade:batch:handle:result", 60, DataType.MAP),
    /**
     * 缓存批量处理失败原因
     */
    batch_handle_fail_msg("trade:batch:handle:failMsg", 60, DataType.MAP),
    /**
     * 缓存批量替换sku错误信息
     */
    batch_change_sku_fail_msg("TRADE_CACHE_ORDER_SALE_CHANGE_SKU_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存批量新增赠品结果
     */
    batch_add_gift_result("TRADE_CACHE_ORDER_SALE_ADD_GIFT_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存批量新增赠品错误信息
     */
    batch_add_gift_fail_msg("TRADE_CACHE_ORDER_SALE_ADD_GIFT_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存批量删除赠品结果
     */
    batch_remove_gift_result("TRADE_CACHE_ORDER_SALE_REMOVE_GIFT_RESULT", 24 * 60, DataType.MAP),
    /**
     * 缓存批量删除赠品错误信息
     */
    batch_remove_gift_fail_msg("TRADE_CACHE_ORDER_SALE_REMOVE_GIFT_RESULT_FAIL_MSG", 24 * 60, DataType.MAP),
    /**
     * 缓存批量取消拣货单结果
     */
    batch_cancel_order_pick_result("TRADE_CACHE_ORDER_PICK_RESULT", 24 * 60, DataType.MAP),
    /**
     * 配货单挂起策略
     */
    strategy("trade:strategy", 24 * 60, DataType.MAP),
    /**
     * 访问统计
     */
    access_statistics("trade:statistics:controllerApi", 4 * 24 * 60, DataType.LIST),
    /**
     * 幂等性结果缓存(不建议存储过大的值)
     */
    idempotent_response("trade:idempotent:response", 24 * 60, DataType.MAP),
    /**
     * 订单撤销取消订单, 缓存7天
     */
    revoke_cancel_order_id("REVOKE_TRADE_CACHE_CANCEL_ORDER_ID", 7 * 24 * 60, DataType.MAP),
    /**
     * 订单部分退货退款, 缓存7天
     */
    order_return_order_id("ORDER_RETURN_ORDER_ID", 7 * 24 * 60, DataType.MAP),
    /**
     * <pre>
     *  未转单前取消, 保存取消的参数(一个单可能存在多次取消)
     *  它是一个列表, (因为可能会在转单之前出现多次取消订单的请求)
     * </pre>
     */
    cancel_order_params("trade:adaptation:cancel:order:params", 7 * 24 * 60, DataType.MAP),
    /**
     * 未转单前取消退单
     */
    cancel_refund_params("trade:adaptation:cancel:refund:params", 7 * 24 * 60, DataType.MAP),
    /**
     * 退单转单订单报文未找到, 缓存7天
     */
    trade_not_found("TRADE_NOT_FOUND", 7 * 24 * 60, DataType.MAP),
    /**
     * 缓存已回传的外部交易号或子交易号(每个渠道可能逻辑不一样)
     */
    delivery_back_channel_order_sn("trade:deliveryback:channelordersn", 30 * 24 * 60, DataType.MAP),
    /**
     * 缓存已经通知仓库拣货的的外部交易号(回传渠道时避免重复调用,先缓存)
     */
    notifydeliveryback_channel_order_sn("trade:notifydeliveryback:channelordersn", 30 * 24 * 60, DataType.MAP),
    /**
     * 缓存渠道的token信息
     */
    channel_token("CHANNEL_TOKEN", 8 * 24 * 60, DataType.MAP),
    /**
     * 缓存未转单的时更新的地址信息
     */
    delivery_change_info("trade:deliverychange:channelordersn", 7 * 24 * 60, DataType.MAP),
    /**
     * 缓存店铺对应的公司编码
     */
    shopCode_contact_companyCode("SAPREPORT_COMPANYCODE", 7 * 24 * 60, DataType.MAP),
    /**
     * 批量新增赠品查询主数据 skuid
     */
    gift_sku_id("TRADE:GIFT:SKUID", 7 * 24 * 60, DataType.MAP),
    /**
     * 缓存店铺code对应的渠道sdk信息
     */
    shopCode_channel_info("SHOP_CODE_CHANNEL_INFO", 7 * 24 * 60, DataType.MAP),
    /**
     * 普通预售订单明细, 缓存7天
     */
    order_presell_item_chanelOrderSaleSn("ORDER_PRESELL_ITEM_CHANNELORDERSALESN", 7 * 24 * 60, DataType.MAP),
    /**
     * 京东到家回调oms售后单缓存, 缓存1天
     */
    jddj_udf_fallback_aftersaleid_cache("JDDJ_UDF_FALLBACK_AFTERSALEID_CACHE", 4 * 60, DataType.MAP),
    /**
     * 订单门店配置服务, 缓存7天
     */
    configure("CONFIGURE", 7 * 24 * 60, DataType.MAP),
    /**
     * 速卖通物流类型缓存, 缓存7天
     */
    aliexpress_logistics_type_cache("ALIEXPRESS_LOGISTICS_TYPE_CACHE", 7 * 24 * 60, DataType.MAP),

    /**
     * 淘宝地址信息, 缓存3天
     */
    taobao_modify_address_cache("TAOBAO_MODIFY_ADDRESS_CACHE", 3 * 24 * 60, DataType.MAP);

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
