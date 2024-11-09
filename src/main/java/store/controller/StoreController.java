package store.controller;

import store.model.Item;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StoreController {
    private final OutputView outputView;
    private final InputView inputView;
    private final StoreService storeService;

    public StoreController(OutputView outputView, InputView inputview, StoreService storeService) {
        this.outputView = outputView;
        this.inputView = inputview;
        this.storeService = storeService;
    }

    public void beforeRun() {
        try {
            BufferedReader promotions = storeService.loadPromotions();
            storeService.savePromotions(promotions);
            BufferedReader inventory = storeService.loadInventory();
            storeService.saveInventory(inventory);
        } catch (IOException e) {
        }
    }

    public void run() {
        while (true) {
            List<Item> inventory = storeService.getInventory();
            outputView.welcomeMessageAndInventory(inventory);

            Map<List<Item>, Integer> inventoryAndQuantity = findItem();

            break;//구매 종료 분기 코드 작성 이전까지 임시 사용
        }
    }

    private Map<List<Item>, Integer> findItem() {
        while (true) {
            try {
                String nameAndQuantity = inputView.askNameAndQuantity();
                return storeService.findItem(nameAndQuantity);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
