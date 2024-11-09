package store.model;

import java.util.List;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final String startDate;
    private final String endDate;

    public Promotion(List<String> promotion) {
        this.name = promotion.get(0);
        this.buy = Integer.parseInt(promotion.get(1));
        this.get = Integer.parseInt(promotion.get(2));
        this.startDate = promotion.get(3);
        this.endDate = promotion.get(4);
    }
}
