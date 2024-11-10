package store.model;

import java.time.LocalDate;
import java.util.List;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(List<String> promotion) {
        this.name = promotion.get(0);
        this.buy = Integer.parseInt(promotion.get(1));
        this.get = Integer.parseInt(promotion.get(2));
        this.startDate = LocalDate.parse(promotion.get(3));
        this.endDate = LocalDate.parse(promotion.get(4));
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
