package store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class StoreServiceTest {
    private final StoreService storeService = new StoreService();

    @Test
    @DisplayName("재고 파일 불러오기 테스트")
    void loadInventory() throws IOException {
        BufferedReader inventory = storeService.loadInventory();

        String expected = "name,price,quantity,promotion";

        assertThat(inventory.readLine()).contains(expected);
    }
}
