package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Item;
import store.model.Items;
import store.model.Promotions;
import store.model.ReceiptItems;
import store.model.Store;
import store.validator.StoreValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StoreServiceTest {
    private Items items;
    private Promotions promotions;
    private Store store;
    private StoreService storeService;
    private StoreValidator storeValidator;
    private ReceiptItems receiptItems;

    @BeforeEach
    void setUp() {
        items = new Items();
        promotions = new Promotions();
        receiptItems = new ReceiptItems();
        store = new Store(items, promotions, receiptItems);
        storeValidator = new StoreValidator();
        storeService = new StoreService(promotions, items, store, storeValidator);
    }

    @Test
    @DisplayName("재고 파일 불러오기 테스트")
    void loadInventory() throws IOException {
        BufferedReader inventory = storeService.loadInventory();
        String expected = "콜라,1000,10,탄산2+1";
        assertThat(inventory.readLine()).contains(expected);
    }

    @Test
    @DisplayName("항목 연계 생성 테스트")
    void saveInventory() throws IOException {
        String inputFile = "콜라,1000,10,탄산2+1\n";
        BufferedReader inventory = new BufferedReader(new StringReader(inputFile));
        storeService.saveInventory(inventory);
        List<Item> savedItems = items.getItems();
        assertThat(savedItems).hasSize(2);
        assertItemEquals(savedItems.getFirst(), "콜라", 1000, 10, "탄산2+1");
        assertItemEquals(savedItems.getLast(), "콜라", 1000, 0, "null");
    }

    private void assertItemEquals(Item item, String name, int price, int quantity, String promotion) {
        assertThat(item.getName()).isEqualTo(name);
        assertThat(item.getPrice()).isEqualTo(price);
        assertThat(item.getQuantity()).isEqualTo(quantity);
        assertThat(item.getPromotion()).isEqualTo(promotion);
    }
}
