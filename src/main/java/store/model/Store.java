package store.model;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public class Store {
    private final Items items;
    private final Promotions promotions;
    private EnumMap<Receipt, Integer> receipt = new EnumMap<>(Receipt.class);
    private int membership;

    public Store(Items items, Promotions promotions) {
        this.items = items;
        this.promotions = promotions;
        this.membership = 0;
    }

    public boolean isPromotionDate(List<Item> items, LocalDate currentDate) {
        Item promotionItem = getPromotionItem(items);
        if (promotionItem != null) {
            return promotions.isPromotionDate(promotionItem.getPromotion(), currentDate);
        }
        return false;
    }

    public EnumMap<Item.PromotionResult, String> getRequestedQuantity(List<Item> items, int quantity) {
        Item promotionItem = getPromotionItem(items);
        if (promotionItem != null) {
            Promotion promotion = getPromotion(promotionItem);
            int buy = promotion.getBuy();
            int get = promotion.getGet();
            return promotionItem.deductPromotionItem(quantity, buy, get);
        }
        return null;
    }

    public void updateReceipt(EnumMap<Item.PromotionResult, String> promotionResult) {
        int addTotalCItemCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY));
        receipt.put(Receipt.TOTAL_ITEM_COUNT, receipt.getOrDefault(Receipt.TOTAL_ITEM_COUNT, 0) + addTotalCItemCount);

        int itemPrice = Integer.parseInt(promotionResult.get(Item.PromotionResult.ITEM_PRICE));
        int itemQuantity = Integer.parseInt(promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY));
        receipt.put(Receipt.TOTAL_PRICE, receipt.getOrDefault(Receipt.TOTAL_PRICE, 0) + itemPrice * itemQuantity);

        int itemDiscountCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.DISCOUNT_COUNT));
        receipt.put(Receipt.PROMOTION_DISCOUNT, receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0) + itemPrice * itemDiscountCount);

        int totalPrice = receipt.getOrDefault(Receipt.TOTAL_PRICE, 0);
        int promotionDiscount = receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0);
        receipt.put(Receipt.PAID_MONEY, totalPrice - promotionDiscount);
        //멤버십 할인은 프로모션 할인 적용 후의 금액에 할인
    }

    public void updateInventory(EnumMap<Item.PromotionResult, String> promotionResult) {
        String itemName = promotionResult.get(Item.PromotionResult.ITEM_NAME);
        String itemCount = promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY);
        items.updateInventory(itemName, Integer.parseInt(itemCount));
    }

    public void applyMembership() {
        int paidMoney = receipt.get(Receipt.PAID_MONEY);
        int membershipDiscount = paidMoney * 30 / 100;
        receipt.put(Receipt.MEMBERSHIP_DISCOUNT, membershipDiscount);
        receipt.put(Receipt.PAID_MONEY, paidMoney - membershipDiscount);
    }

    private Item getPromotionItem(List<Item> items) {
        for (Item item : items) {
            if (!item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Promotion getPromotion(Item promotionItem) {
        return promotions.getPromotion(promotionItem.getPromotion());
    }

    enum Receipt {
        TOTAL_PRICE,
        TOTAL_ITEM_COUNT,
        PROMOTION_DISCOUNT,
        MEMBERSHIP_DISCOUNT,
        PAID_MONEY
    }

}
