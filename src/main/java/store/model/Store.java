package store.model;

public class Store {
    private final Items items;
    private final Promotions promotions;
    private int membership;

    public Store(Items items, Promotions promotions) {
        this.items = items;
        this.promotions = promotions;
        this.membership = 0;
    }
}
