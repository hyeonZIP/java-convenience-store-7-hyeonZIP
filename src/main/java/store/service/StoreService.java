package store.service;

import store.model.Item;
import store.model.Items;
import store.model.Promotion;
import store.model.Promotions;
import store.validator.StoreValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoreService {
    private static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";
    private static final String INVENTORY_PATH = "src/main/resources/products.md";
    private static final String COMMA = ",";
    private static final String BLANK = "";
    private static final String HYPHEN = "-";
    private static final String LEFT_BRAKET = "[";
    private static final String RIGHT_BRAKET = "]";
    private final Items items;
    private final Promotions promotions;
    private final StoreValidator storeValidator;

    public StoreService(final Promotions promotions, final Items items, final StoreValidator storeValidator) {
        this.promotions = promotions;
        this.items = items;
        this.storeValidator = storeValidator;
    }

    public BufferedReader loadPromotions() throws IOException {
        BufferedReader promotions = new BufferedReader(new FileReader(PROMOTIONS_PATH));
        promotions.readLine();
        return promotions;
    }

    public BufferedReader loadInventory() throws IOException {
        BufferedReader inventory = new BufferedReader(new FileReader(INVENTORY_PATH));
        inventory.readLine();
        return inventory;
    }

    public void savePromotions(final BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            List<String> promotion = List.of(line.split(COMMA));
            promotions.add(new Promotion(promotion));
        }
    }

    public void saveInventory(final BufferedReader inventory) throws IOException {
        String line;
        while ((line = inventory.readLine()) != null) {
            List<String> item = List.of(line.split(COMMA));
            items.add(new Item(item));
        }
    }

    public List<Item> getInventory() {
        return items.getItems();
    }

    public List<Item> findItem(final String nameAndQuantity) {
        storeValidator.validateFormat(nameAndQuantity);
        String replacedItem = nameAndQuantity.replace(LEFT_BRAKET, BLANK).replace(RIGHT_BRAKET, BLANK);

        List<Item> itemFormat = new ArrayList<>();
        List<String> items = List.of(replacedItem.split(COMMA));
        storeValidator.validateDuplicate(items);
        for (String item : items) {
            itemFormat.addAll(splitHyphen(item));
        }
        return itemFormat;
    }

    private List<Item> splitHyphen(final String replacedItem) {
        List<String> nameQuantity = List.of(replacedItem.split(HYPHEN));
        return checkNameAndQuantity(nameQuantity);
    }

    private List<Item> checkNameAndQuantity(final List<String> nameQuantity) {
        String name = nameQuantity.getFirst();
        int quantity = Integer.parseInt(nameQuantity.getLast());
        return items.checkNameAndQuantity(name, quantity);
    }
}
