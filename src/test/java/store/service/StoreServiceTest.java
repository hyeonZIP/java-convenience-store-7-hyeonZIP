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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StoreServiceTest {
    private Items items;
    private Promotions promotions;
    private Store store;
    private StoreService storeService;
    private StoreValidator storeValidator;
    private ReceiptItems receiptItems;

    @BeforeEach
    void setUp() throws IOException {
        items = new Items();
        promotions = new Promotions();
        receiptItems = new ReceiptItems();
        store = new Store(items, promotions, receiptItems);
        storeValidator = new StoreValidator();
        storeService = new StoreService(promotions, items, store, storeValidator);
        BufferedReader inventory = storeService.loadInventory();
        storeService.saveInventory(inventory);
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
    void saveInventory() {
        List<Item> savedItems = items.getItems();
        List<List<String>> expected = new ArrayList<>();
        expected.add(List.of("오렌지주스", "1800", "0", "null"));
        expected.add(List.of("탄산수", "1200", "0", "null"));
        for (List<String> expect : expected) {
            assertThatAnySatisfy(savedItems, expect);
        }

    }

    @Test
    @DisplayName("")
    void findItem() {
        String input = "[사이다-5],[콜라-6]";

        Map<List<Item>, Integer> result = storeService.findItem(input);

        for (Map.Entry<List<Item>, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    private void assertThatAnySatisfy(List<Item> savedItems, List<String> expected) {
        assertThat(savedItems).anySatisfy(savedItem -> {
            assertThat(savedItem.getName()).isEqualTo(expected.get(0));
            assertThat(savedItem.getPrice()).isEqualTo(Integer.parseInt(expected.get(1)));
            assertThat(savedItem.getQuantity()).isEqualTo(Integer.parseInt(expected.get(2)));
            assertThat(savedItem.getPromotion()).isEqualTo(expected.get(3));
        });
    }
}
