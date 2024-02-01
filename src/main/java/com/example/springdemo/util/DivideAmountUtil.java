package com.example.springdemo.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 均摊工具类
 * @author fong on 2019/10/12.
 */
public class DivideAmountUtil {
    /**
     * 得出每条item的均摊金额
     * @param items                 明细集合
     * @param totalDivideAmount     用来均摊的金额(正数得出结果为正,负数得出结果为负)
     * @param totalAmountHandler    每个item的总金额(用于计算占比)(只取绝对值)
     */
    public static <T> Map<T, BigDecimal> calculateDivideAmount(Collection<T> items, BigDecimal totalDivideAmount, TotalAmountHandler<T> totalAmountHandler) {

        if (totalDivideAmount == null) {
            totalDivideAmount = BigDecimal.ZERO;
        }

        // 总价值
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (T item : items) {
            totalPrice = totalPrice.add( totalAmountHandler.at(item).abs() );
        }

        // 得出每条item的均摊金额
        Map<T, BigDecimal> result = Maps.newHashMap();
        for (T item : items) {
            if ( totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                result.put( item, totalDivideAmount.abs().multiply( totalAmountHandler.at(item).abs() ).divide( totalPrice,2, RoundingMode.DOWN) );
            }else{
                result.put( item, BigDecimal.ZERO );
            }
        }

        // 将 除不尽的金额 分配到 金额最大的明细中
        BigDecimal itemTotalDivideAmount = BigDecimal.ZERO;
        for (Map.Entry<T, BigDecimal> entry : result.entrySet()) {
            itemTotalDivideAmount = itemTotalDivideAmount.add(entry.getValue());
        }

        if (itemTotalDivideAmount.compareTo(totalDivideAmount.abs()) != 0) {

            T maxAmountItem = items.stream()
                    .max(Comparator.comparing(o -> totalAmountHandler.at(o).abs()))
                    .orElseThrow( () -> new IllegalArgumentException("item not find!"));

            BigDecimal bigDecimal = result.get(maxAmountItem);

            result.put(maxAmountItem,bigDecimal.add(totalDivideAmount.abs().subtract(itemTotalDivideAmount)));
        }

        // 若用来均摊的金额小于0,则以负数呈现
        if (totalDivideAmount.compareTo(BigDecimal.ZERO) < 0) {
            for (Map.Entry<T, BigDecimal> entry : result.entrySet()) {
                entry.setValue( BigDecimal.ZERO.subtract( entry.getValue() ) );
            }
        }

        return result;
    }

    /** 每个item的总金额(用于计算占比) */
    @FunctionalInterface
    public interface TotalAmountHandler<T>{
        /**
         * 明细项的总金额
         * @param item 明细项
         */
        BigDecimal at(T item);
    }

    public static void main(String[] args) {
        String totalAmount = "57.8";
        String[][] items = {{"49", "1"}, {"49", "1"}};

        List<Item> orderItems = Lists.newArrayList();
        int id = 1;
        for (String[] item : items) {
            Item saleItem = new Item();
            saleItem.setProduct(String.valueOf(id++));
            saleItem.setSalePrice(new BigDecimal(item[0]));
            saleItem.setSkuNumber(Integer.parseInt(item[1]));
            orderItems.add(saleItem);
        }

        Map<Item, BigDecimal> map = DivideAmountUtil.calculateDivideAmount(
                orderItems,
                new BigDecimal(totalAmount),
                e -> e.getSalePrice().multiply(new BigDecimal(e.getSkuNumber()))
        );

        for (Item item : map.keySet()) {
            System.err.println(item.getProduct() + " : " + map.get(item));
        }


    }

    @Data
    private static class Item {
        private String product;
        private BigDecimal salePrice;
        private Integer skuNumber;
    }

}
