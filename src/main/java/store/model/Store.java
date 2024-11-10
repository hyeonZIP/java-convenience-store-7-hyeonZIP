package store.model;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public class Store {
    private final Items items;
    private final Promotions promotions;
    private SelectItems selectItems;
    private EnumMap<Receipt, Integer> receipt = new EnumMap<>(Receipt.class);
    private int membership;

    public Store(Items items, Promotions promotions, SelectItems selectItems) {
        this.items = items;
        this.promotions = promotions;
        this.membership = 0;
        this.selectItems = selectItems;
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
        Item normalItem = getNormalItem(items);
        if (promotionItem != null) {
            Promotion promotion = getPromotion(promotionItem);
            int buy = promotion.getBuy();
            int get = promotion.getGet();
            return promotionItem.deductPromotionItem(quantity, buy, get);
        }
        return normalItem.deductNormalItem(quantity);
    }

    public void updateReceipt(EnumMap<Item.PromotionResult, String> promotionResult) {
        int addTotalCItemCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY));
        receipt.put(Receipt.TOTAL_ITEM_COUNT, receipt.getOrDefault(Receipt.TOTAL_ITEM_COUNT, 0) + addTotalCItemCount);

        int itemPrice = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
        int itemQuantity = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.REQUEST_QUANTITY, "0"));
        receipt.put(Receipt.TOTAL_PRICE, receipt.getOrDefault(Receipt.TOTAL_PRICE, 0) + itemPrice * itemQuantity);

        int itemDiscountCount = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.DISCOUNT_COUNT, "0"));
        receipt.put(Receipt.PROMOTION_DISCOUNT, receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0) + itemPrice * itemDiscountCount);

        int totalPrice = receipt.getOrDefault(Receipt.TOTAL_PRICE, 0);
        int promotionDiscount = receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0);
        receipt.put(Receipt.PAID_MONEY, totalPrice - promotionDiscount);

        String itemName = promotionResult.getOrDefault(Item.PromotionResult.ITEM_NAME, "");
        int quantity = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.REQUEST_QUANTITY, "0"));
        int price = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
        int promotion = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.DISCOUNT_COUNT, "0"));
        selectItems.add(new SelectItem(itemName, quantity, price, promotion));
        if (promotionResult.get(Item.PromotionResult.IS_PROMOTION).equals("N")) {
            receipt.put(Receipt.NON_PROMOTION_PRICE, receipt.getOrDefault(Receipt.NON_PROMOTION_PRICE, 0) + price * quantity);
        }
    }

    public void resetSelectList() {
        this.selectItems = new SelectItems();
        this.receipt = new EnumMap<>(Receipt.class);
    }

    public void updateInventory(EnumMap<Item.PromotionResult, String> promotionResult) {
        String itemName = promotionResult.get(Item.PromotionResult.ITEM_NAME);
        String itemCount = promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY);
        items.updateInventory(itemName, Integer.parseInt(itemCount));
    }

    public void applyMembership() {
        int nonPromotionPrice = receipt.getOrDefault(Receipt.NON_PROMOTION_PRICE, 0);
        int membershipDiscount = nonPromotionPrice * 30 / 100;
        int paidMoney = receipt.getOrDefault(Receipt.TOTAL_PRICE, 0) - (membershipDiscount + receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0));
        receipt.put(Receipt.MEMBERSHIP_DISCOUNT, membershipDiscount);
        receipt.put(Receipt.PAID_MONEY, paidMoney);
    }

    public EnumMap<Receipt, Integer> getReceipt() {
        return receipt;
    }

    public List<SelectItem> getSelectedItem() {
        return selectItems.getSelectedList();
    }

    private Item getPromotionItem(List<Item> items) {
        for (Item item : items) {
            if (!item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Item getNormalItem(List<Item> items) {
        for (Item item : items) {
            if (item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Promotion getPromotion(Item promotionItem) {
        return promotions.getPromotion(promotionItem.getPromotion());
    }

    public enum Receipt {
        TOTAL_PRICE,
        TOTAL_ITEM_COUNT,
        PROMOTION_DISCOUNT,
        MEMBERSHIP_DISCOUNT,
        PAID_MONEY,
        NON_PROMOTION_PRICE
    }

}
