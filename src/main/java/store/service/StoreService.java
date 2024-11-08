package store.service;

import store.model.Item;
import store.model.Items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class StoreService {
    private static final String INVENTORY_PATH = "src/main/resources/products.md";

    private final Items items;

    public StoreService(Items items) {
        this.items = items;
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
}
