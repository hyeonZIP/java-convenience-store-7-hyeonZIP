package store.model;

import java.util.EnumMap;
import java.util.List;

public class Item {
    private final String name;
    private final int price;
    private final String promotion;
    private int quantity;

    public Item(List<String> items) {
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

    public void updateQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public EnumMap<PromotionResult, String> deductPromotionItem(int requestQuantity, int buy, int get) {
        EnumMap<PromotionResult, String> result = new EnumMap<>(PromotionResult.class);
        result.put(PromotionResult.ITEM_NAME, name);
        int remainQuantity = quantity / (buy + get) * (buy + get) - requestQuantity;
        result.put(PromotionResult.INSUFFICIENT_INVENTORY, String.valueOf(remainQuantity));
        int calculatePromotionNumber = Math.min(quantity, requestQuantity);
        result.put(PromotionResult.DISCOUNT_COUNT, String.valueOf(calculatePromotionNumber / (buy + get)));
        int nonDiscountCount = calculatePromotionNumber % (buy + get);//할인 못받는 갯수
        result.put(PromotionResult.NON_DISCOUNT_COUNT, String.valueOf(nonDiscountCount));
        if (nonDiscountCount == buy) {
            result.put(PromotionResult.FREE_ITEM, "1");
        }
        result.put(PromotionResult.REQUEST_QUANTITY, String.valueOf(requestQuantity));
        result.put(PromotionResult.ITEM_PRICE, String.valueOf(price));
        return result;
    }

    public enum PromotionResult {
        ITEM_NAME,
        ITEM_PRICE,
        INSUFFICIENT_INVENTORY,
        DISCOUNT_COUNT,
        NON_DISCOUNT_COUNT,
        FREE_ITEM,
        REQUEST_QUANTITY
    }
}