package store.service;

import store.model.Item;
import store.model.Items;
import store.validator.StoreValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreService {
    private static final String INVENTORY_PATH = "src/main/resources/products.md";
    private final Items items;
    private final StoreValidator storeValidator;

    public StoreService(Items items, StoreValidator storeValidator) {
        this.items = items;
        this.storeValidator = storeValidator;
    }

    public BufferedReader loadInventory() throws IOException {
        BufferedReader inventory = new BufferedReader(new FileReader(INVENTORY_PATH));
        inventory.readLine();
        return inventory;
    }

    public void saveInventory(BufferedReader inventory) throws IOException {
        String line;
        while ((line = inventory.readLine()) != null) {
            List<String> item = List.of(line.split(","));
            items.add(new Item(item));
        }
    }

    public List<Item> getInventory() {
        return items.getItems();
    }

    public Map<String, Integer> findItem(String nameAndQuantity) {
        storeValidator.validateBlank(nameAndQuantity);
        Map<String, Integer> itemFormat = new HashMap<>();
        for (String item : nameAndQuantity.split(",")) {
            storeValidator.validateBrakets(item);
            String replacedItem = item.replace("[", "").replace("]", "");
            List<String> nameQuantity = List.of(replacedItem.split("-"));
            storeValidator.validateSize(nameQuantity);
            storeValidator.validateParsing(nameQuantity.get(1));
            //TODO 1. 재고 이름 검증 2. 수량 범위 검증 3. 함수 분리
            String name = nameQuantity.get(0);
            int quantity = Integer.parseInt(nameQuantity.get(1));
            itemFormat.put(name, quantity);
        }
        return itemFormat;
    }

}
