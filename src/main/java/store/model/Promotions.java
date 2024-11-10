package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions() {
        this.promotions = new ArrayList<>();
    }

    public void add(final Promotion promotion) {
        promotions.add(promotion);
    }

    public Promotion getPromotion(final String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.getName().equals(promotionName)) {
                return promotion;
            }
        }
        return null;
    }

    public boolean isPromotionDate(final String promotionName, final LocalDate currentDate) {
        for (Promotion promotion : promotions) {
            if (promotion.getName().equals(promotionName)) {
                return currentDate.isAfter(promotion.getStartDate()) && currentDate.isBefore(promotion.getEndDate());
            }
        }
        return false;
    }
}
