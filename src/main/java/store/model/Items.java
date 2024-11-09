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

    public List<Item> checkNameAndQuantity(String name, int quantity) {
        List<Item> foundItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(name)) {
                item.validateQuantity(quantity);
                foundItems.add(item);
            }
        }
        validateName(foundItems);
        return foundItems;
    }

    private void validateName(List<Item> foundItems) {
        if (foundItems.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }
}
