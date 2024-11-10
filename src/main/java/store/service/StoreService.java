package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.model.Item;
import store.model.Items;
import store.model.Promotion;
import store.model.Promotions;
import store.model.Store;
import store.validator.StoreValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Store store;
    private final StoreValidator storeValidator;

    public StoreService(final Promotions promotions, final Items items, final Store store, final StoreValidator storeValidator) {
        this.promotions = promotions;
        this.items = items;
        this.store = store;
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

    public Map<List<Item>, Integer> findItem(final String nameAndQuantity) {
        storeValidator.validateFormat(nameAndQuantity);
        String replacedItem = nameAndQuantity.replace(LEFT_BRAKET, BLANK).replace(RIGHT_BRAKET, BLANK);
        Map<List<Item>, Integer> itemFormat = new HashMap<>();
        List<String> items = List.of(replacedItem.split(COMMA));
        storeValidator.validateDuplicate(items);
        for (String item : items) {
            itemFormat.putAll(splitHyphen(item));
        }
        return itemFormat;
    }

    public boolean isPromotionDate(List<Item> items) {
        return store.isPromotionDate(items, getDate());
    }

    public EnumMap<Item.PromotionResult, String> getRequestedQuantity(List<Item> items, int quantity) {
        return store.getRequestedQuantity(items, quantity);
    }

    public boolean checkPromotionState(EnumMap<Item.PromotionResult, String> promotionResult) {
        int nonDiscountCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.INSUFFICIENT_INVENTORY));
        return nonDiscountCount < 0;
    }

    public boolean checkGettingFreeItem(EnumMap<Item.PromotionResult, String> promotionResult) {
        return promotionResult.get(Item.PromotionResult.FREE_ITEM) != null;
    }

    public void makeReceiptBeforeMembership(EnumMap<Item.PromotionResult, String> promotionResult) {
        store.updateReceipt(promotionResult);
        store.updateInventory(promotionResult);
    }

    public void applyMembership() {
        store.applyMembership();
    }

    private LocalDate getDate() {
        LocalDateTime currentDate = DateTimes.now();
        return currentDate.toLocalDate();
    }

    private Map<List<Item>, Integer> splitHyphen(final String replacedItem) {
        List<String> nameQuantity = List.of(replacedItem.split(HYPHEN));
        return checkNameAndQuantity(nameQuantity);
    }

    private Map<List<Item>, Integer> checkNameAndQuantity(final List<String> nameQuantity) {
        String name = nameQuantity.getFirst();
        int quantity = Integer.parseInt(nameQuantity.getLast());
        return Map.of(items.checkNameAndQuantity(name, quantity), quantity);
    }
}
