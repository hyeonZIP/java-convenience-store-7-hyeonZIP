package store.model;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public class Store {
    private static final double MEMBERSHIP_DISCOUNT = 0.3;
    private static final int MAXIMUM_MEMBERSHIP_DISCOUNT = 8000;
    private final Items items;
    private final Promotions promotions;
    private ReceiptItems receiptItems;
    private EnumMap<Receipt, Integer> receipt = new EnumMap<>(Receipt.class);
    private int membership;

    public Store(final Items items, final Promotions promotions, final ReceiptItems receiptItems) {
        this.items = items;
        this.promotions = promotions;
        this.membership = 0;
        this.receiptItems = receiptItems;
    }

    public boolean isPromotionDate(final List<Item> items, final LocalDate currentDate) {
        Item promotionItem = getPromotionItem(items);
        return promotionItem != null && promotions.isPromotionDate(promotionItem.getPromotion(), currentDate);
    }

    public EnumMap<Item.PromotionResult, String> getRequestedQuantity(final List<Item> items, final int quantity, final Boolean promotionDate) {
        Item promotionItem = getPromotionItem(items);
        if (promotionItem != null && promotionDate) {
            Promotion promotion = getPromotion(promotionItem);
            return promotionItem.deductPromotionItem(quantity, promotion.getBuy(), promotion.getGet());
        }
        Item normalItem = getNormalItem(items);
        return normalItem.deductNormalItem(quantity);
    }

    public void updateReceipt(final EnumMap<Item.PromotionResult, String> promotionResult) {
        updateTotalItemCount(promotionResult);
        updateTotalPrice(promotionResult);
        updatePromotionDiscount(promotionResult);
        updatePaidMoney();
        updateReceiptItems(promotionResult);
        updateNonPromotionPrice(promotionResult);
    }

    public void resetSelectList() {
        this.receiptItems = new ReceiptItems();
        this.receipt = new EnumMap<>(Receipt.class);
    }

    public void updateInventory(final EnumMap<Item.PromotionResult, String> promotionResult) {
        String itemName = promotionResult.get(Item.PromotionResult.ITEM_NAME);
        String itemCount = promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY);
        items.updateInventory(itemName, Integer.parseInt(itemCount));
    }

    public void applyMembership() {
        int nonPromotionPrice = receipt.getOrDefault(Receipt.NON_PROMOTION_PRICE, 0);
        int membershipDiscount = checkMaximumMembershipDiscount(nonPromotionPrice);
        int paidMoney = receipt.getOrDefault(Receipt.TOTAL_PRICE, 0) - (membershipDiscount + receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0));
        receipt.put(Receipt.PAID_MONEY, paidMoney);
    }

    private int checkMaximumMembershipDiscount(int nonPromotionPrice) {
        int membershipDiscount = (int) (nonPromotionPrice * MEMBERSHIP_DISCOUNT);
        if (membership != MAXIMUM_MEMBERSHIP_DISCOUNT && membershipDiscount + membership > MAXIMUM_MEMBERSHIP_DISCOUNT) {
            membershipDiscount = MAXIMUM_MEMBERSHIP_DISCOUNT - membership;
            membership = MAXIMUM_MEMBERSHIP_DISCOUNT;
        }
        receipt.put(Receipt.MEMBERSHIP_DISCOUNT, membershipDiscount);
        membership += membershipDiscount;
        return membershipDiscount;
    }

    public EnumMap<Receipt, Integer> getReceipt() {
        return receipt;
    }

    public List<ReceiptItem> getSelectedItem() {
        return receiptItems.getSelectedList();
    }

    private Item getPromotionItem(final List<Item> items) {
        for (Item item : items) {
            if (!item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Item getNormalItem(final List<Item> items) {
        for (Item item : items) {
            if (item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Promotion getPromotion(final Item promotionItem) {
        return promotions.getPromotion(promotionItem.getPromotion());
    }

    private void updateTotalItemCount(EnumMap<Item.PromotionResult, String> promotionResult) {
        int addTotalCItemCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY));
        receipt.merge(Receipt.TOTAL_ITEM_COUNT, addTotalCItemCount, Integer::sum);
    }

    private void updateTotalPrice(EnumMap<Item.PromotionResult, String> promotionResult) {
        int itemPrice = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
        int itemQuantity = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.REQUEST_QUANTITY, "0"));
        receipt.merge(Receipt.TOTAL_PRICE, itemPrice * itemQuantity, Integer::sum);
    }

    private void updatePromotionDiscount(EnumMap<Item.PromotionResult, String> promotionResult) {
        int itemDiscountCount = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.DISCOUNT_COUNT, "0"));
        int itemPrice = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
        receipt.merge(Receipt.PROMOTION_DISCOUNT, itemPrice * itemDiscountCount, Integer::sum);
    }

    private void updatePaidMoney() {
        int totalPrice = receipt.getOrDefault(Receipt.TOTAL_PRICE, 0);
        int promotionDiscount = receipt.getOrDefault(Receipt.PROMOTION_DISCOUNT, 0);
        receipt.put(Receipt.PAID_MONEY, totalPrice - promotionDiscount);
    }

    private void updateReceiptItems(EnumMap<Item.PromotionResult, String> promotionResult) {
        String itemName = promotionResult.getOrDefault(Item.PromotionResult.ITEM_NAME, "");
        int quantity = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.REQUEST_QUANTITY, "0"));
        int price = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
        int promotion = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.DISCOUNT_COUNT, "0"));
        receiptItems.add(new ReceiptItem(itemName, quantity, price, promotion));
    }

    private void updateNonPromotionPrice(EnumMap<Item.PromotionResult, String> promotionResult) {
        if (promotionResult.get(Item.PromotionResult.IS_PROMOTION).equals("N")) {
            int price = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.ITEM_PRICE, "0"));
            int quantity = Integer.parseInt(promotionResult.getOrDefault(Item.PromotionResult.REQUEST_QUANTITY, "0"));
            receipt.put(Receipt.NON_PROMOTION_PRICE, receipt.getOrDefault(Receipt.NON_PROMOTION_PRICE, 0) + price * quantity);
        }
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public Items getItems() {
        return items;
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
