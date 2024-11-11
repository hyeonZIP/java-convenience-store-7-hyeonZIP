package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("상품 찾기 테스트")
    void findItem() {
        String input = "[사이다-5],[콜라-6]";

        Map<List<Item>, Integer> result = storeService.findItem(input);

        assertThat(result).hasSize(2);
        for (Map.Entry<List<Item>, Integer> entry : result.entrySet()) {
            List<Item> items = entry.getKey();
            assertThat(items.getFirst().getPromotion()).isEqualTo("탄산2+1");
            assertThat(items.getLast().getPromotion()).isEqualTo("null");
        }
    }

    @ParameterizedTest
    @CsvSource(value = {"'[사이다-30],[콜라-30]'", "'[칠성사이다-3]'", "'[사이다-0]]'", "'[콜라--2]'"})
    @DisplayName("상품 찾기 예외 테스트")
    void findItemException(String input) {
        assertThatThrownBy(() -> storeService.findItem(input)).isInstanceOf(IllegalArgumentException.class);
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
