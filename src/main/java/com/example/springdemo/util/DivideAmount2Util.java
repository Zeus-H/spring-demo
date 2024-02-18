package com.example.springdemo.util;

import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DivideAmount2Util {

    public static <T> Map<T, BigDecimal> calculateDivideAmount(Collection<T> items, BigDecimal totalDivideAmount,
                                                               Function<T, BigDecimal> priceHandler,
                                                               Function<T, Integer> quantityHandler) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        Map<T, BigDecimal> totalAmounts = new HashMap<>();
        for (T item : items) {
            BigDecimal price = priceHandler.apply(item);
            Integer quantity = quantityHandler.apply(item);
            BigDecimal total = price.multiply(new BigDecimal(quantity));
            totalPrice = totalPrice.add(total);
            totalAmounts.put(item, total);
        }

        Map<T, BigDecimal> result = new HashMap<>();
        BigDecimal totalDistributed = BigDecimal.ZERO;
        for (T item : items) {
            BigDecimal amount = BigDecimal.ZERO;
            if (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal ratio = totalAmounts.get(item).divide(totalPrice, 20, RoundingMode.HALF_UP);
                amount = totalDivideAmount.multiply(ratio).setScale(2, RoundingMode.DOWN);
                totalDistributed = totalDistributed.add(amount);
            }
            result.put(item, amount);
        }

        BigDecimal remaining = totalDivideAmount.subtract(totalDistributed);
        if (remaining.compareTo(BigDecimal.ZERO) != 0) {
            // 先按数量排序，然后按总量降序排序
            List<T> sortedItems = items.stream()
                    .sorted(Comparator.comparing((T item) -> quantityHandler.apply(item))
                            .thenComparing(totalAmounts::get).reversed())
                    .collect(Collectors.toList());

            for (T item : sortedItems) {
                if (remaining.compareTo(BigDecimal.ZERO) == 0) break;
                BigDecimal currentAmount = result.get(item);
                BigDecimal adjustedAmount = currentAmount.add(remaining);
                // Adjust for the case where adding remaining still leaves a remainder
                adjustedAmount = adjustedAmount.setScale(2, RoundingMode.DOWN);
                result.put(item, adjustedAmount);
                remaining = totalDivideAmount.subtract(result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
                if (remaining.compareTo(BigDecimal.ZERO) == 0) break;
            }
        }

        // Adjust signs if totalDivideAmount is negative
        if (totalDivideAmount.compareTo(BigDecimal.ZERO) < 0) {
            result.forEach((item, amount) -> result.put(item, amount.negate()));
        }

        return result;
    }

    public static void main(String[] args) {
        String totalAmount = "3.33";
        List<Item> orderItems = Lists.newArrayList();
        orderItems.add(new Item("A", new BigDecimal("2"), 4));
        orderItems.add(new Item("B", new BigDecimal("1"), 4));

        Map<Item, BigDecimal> map = DivideAmount2Util.calculateDivideAmount(
                orderItems,
                new BigDecimal(totalAmount),
                Item::getSalePrice,
                Item::getSkuNumber
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

        public Item(String product, BigDecimal salePrice, Integer skuNumber) {
            this.product = product;
            this.salePrice = salePrice;
            this.skuNumber = skuNumber;
        }
    }
}
