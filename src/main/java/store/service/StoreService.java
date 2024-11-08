package store.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StoreService {
    private static final String INVENTORY_PATH = "src/main/resources/products.md";

    public BufferedReader loadInventory() throws IOException {
        BufferedReader inventory = new BufferedReader(new FileReader(INVENTORY_PATH));
        inventory.readLine();

        return inventory;
    }
}
