package store.model;

import java.util.List;

public class Item {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;

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
}