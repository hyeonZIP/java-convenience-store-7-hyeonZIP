package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Item;
import store.model.Items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class StoreServiceTest {
    private Items items;
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        items = new Items();
        storeService = new StoreService(items);
    }

    @Test
    @DisplayName("재고 파일 불러오기 테스트")
    void loadInventory() throws IOException {
        BufferedReader inventory = storeService.loadInventory();

        String expected = "콜라,1000,10,탄산2+1";

        assertThat(inventory.readLine()).contains(expected);
    }

    @Test
    @DisplayName("재고 파일 저장 테스트")
    void saveInventory() throws IOException {
        String inputFile = "콜라,1000,10,탄산2+1";

        BufferedReader inventory = new BufferedReader(new StringReader(inputFile));
        storeService.saveInventory(inventory);

        for (Item item : items.getItems()) {
            assertThat(item.getName()).isEqualTo("콜라");
            assertThat(item.getPrice()).isEqualTo(1000);
            assertThat(item.getQuantity()).isEqualTo(10);
            assertThat(item.getPromotion()).isEqualTo("탄산2+1");
        }
    }
}
