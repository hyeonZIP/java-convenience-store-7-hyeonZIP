package store.model;

import java.util.EnumMap;
import java.util.List;

public class Item {
    private final String name;
    private final int price;
    private final String promotion;
    EnumMap<PromotionResult, String> result = new EnumMap<>(PromotionResult.class);
    private int quantity;

    public Item(final List<String> items) {
        this.name = items.get(0);
        this.price = Integer.parseInt(items.get(1));
        this.quantity = Integer.parseInt(items.get(2));
        this.promotion = items.get(3);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public void updateQuantity(final int quantity) {
        this.quantity -= quantity;
    }

    public EnumMap<PromotionResult, String> deductPromotionItem(final int requestQuantity, final int buy, final int get) {
        result.put(PromotionResult.IS_PROMOTION, "Y");
        result.put(PromotionResult.ITEM_NAME, name);
        checkNonDiscountCount(requestQuantity, buy, get);
        checkInventoryMoreThanQuantity(requestQuantity, buy, get);
        result.put(PromotionResult.REQUEST_QUANTITY, String.valueOf(requestQuantity));
        result.put(PromotionResult.ITEM_PRICE, String.valueOf(price));
        return result;
    }

    public EnumMap<PromotionResult, String> deductNormalItem(final int requestQuantity) {
        result.put(PromotionResult.ITEM_NAME, name);
        result.put(PromotionResult.REQUEST_QUANTITY, String.valueOf(requestQuantity));
        result.put(PromotionResult.ITEM_PRICE, String.valueOf(price));
        result.put(PromotionResult.IS_PROMOTION, "N");
        return result;
    }

    private void checkInventoryMoreThanQuantity(final int requestQuantity, final int buy, final int get) {
        int calculatePromotionNumber = Math.min(quantity, requestQuantity);
        result.put(PromotionResult.DISCOUNT_COUNT, String.valueOf(calculatePromotionNumber / (buy + get)));
        int nonDiscountCount = calculatePromotionNumber % (buy + get);
        if (nonDiscountCount == buy) {
            result.put(PromotionResult.FREE_ITEM, "1");
        }
    }

    private void checkNonDiscountCount(final int requestQuantity, final int buy, final int get) {
        int remainQuantity = quantity / (buy + get) * (buy + get) - requestQuantity;
        result.put(PromotionResult.NON_DISCOUNT_COUNT, String.valueOf(remainQuantity));
    }

    public enum PromotionResult {
        ITEM_NAME,
        ITEM_PRICE,
        DISCOUNT_COUNT,
        NON_DISCOUNT_COUNT,
        FREE_ITEM,
        REQUEST_QUANTITY,
        IS_PROMOTION
    }
}