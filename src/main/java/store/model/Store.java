package store.model;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public class Store {
    private final Items items;
    private final Promotions promotions;
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

}
