package store.model;

public class ReceiptItem {
    private final String name;
    private final int quantity;
    private final int price;
    private final int promotion;

    public ReceiptItem(final String name, final int quantity, final int price, final int promotion) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getPromotion() {
        return promotion;
    }
}
