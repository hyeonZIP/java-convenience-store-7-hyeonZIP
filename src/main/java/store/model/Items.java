package store.model;

import java.util.ArrayList;
import java.util.List;

public class Items {
    private final List<Item> items;

    public Items() {
        this.items = new ArrayList<>();
    }

    public void add(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}
