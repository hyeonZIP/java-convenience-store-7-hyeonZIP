package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Promotions {
    private final List<Promotion> promotions;

    public Promotions() {
        this.promotions = new ArrayList<>();
    }

    public void add(Promotion promotion) {
        promotions.add(promotion);
    }

    public Promotion getPromotion(String promotionName) {
        for (Promotion promotion : promotions) {
            if (promotion.getName().equals(promotionName)) {
                return promotion;
            }
        }
        return null;
    }

    public boolean isPromotionDate(String promotionName, LocalDate currentDate) {
        for (Promotion promotion : promotions) {
            if (promotion.getName().equals(promotionName)) {
                return currentDate.isAfter(promotion.getStartDate()) && currentDate.isBefore(promotion.getEndDate());
            }
        }
        return false;
    }

    public List<Integer> getBuyAndGet(Promotion promotion) {
        return List.of(promotion.getBuy(), promotion.getGet());
    }
}
