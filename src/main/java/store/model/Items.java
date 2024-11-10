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
            equalsName(item, foundItems, name);
        }
        validateName(foundItems);
        validateQuantity(foundItems, quantity);
        return foundItems;
    }

    public void updateInventory(final String name, final int quantity) {
        Item promotionItem = getPromotionItem(name);
        Item normalItem = getNormalItem(name);

        if (promotionItem != null && promotionItem.getQuantity() < quantity) {
            normalItem.updateQuantity(quantity - promotionItem.getQuantity());
            promotionItem.updateQuantity(promotionItem.getQuantity());
            return;
        }
        if (promotionItem != null && promotionItem.getQuantity() >= quantity) {
            promotionItem.updateQuantity(quantity);
            return;
        }
        if (normalItem.getQuantity() >= quantity) {
            normalItem.updateQuantity(quantity);

        }
    }

    private Item getPromotionItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name) && !item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private Item getNormalItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name) && item.getPromotion().equals("null")) {
                return item;
            }
        }
        return null;
    }

    private void equalsName(Item item, List<Item> foundItems, String name) {
        if (item.getName().equals(name)) {
            foundItems.add(item);
        }
    }

    private void validateName(List<Item> foundItems) {
        if (foundItems.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private void validateQuantity(List<Item> items, int quantity) {
        int inventory = 0;
        for (Item item : items) {
            inventory += item.getQuantity();
        }
        if (inventory < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }
}
