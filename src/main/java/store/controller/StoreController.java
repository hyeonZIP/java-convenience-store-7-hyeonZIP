package store.controller;

import store.model.Item;
import store.service.StoreService;
import store.view.OutputView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class StoreController {
    private final OutputView outputView;
    private final StoreService storeService;

    public StoreController(OutputView outputView, StoreService storeService) {
        this.outputView = outputView;
        this.storeService = storeService;
    }

    public void beforeRun() {
        try {
            BufferedReader inventory = storeService.loadInventory();
            storeService.saveInventory(inventory);
        } catch (IOException e) {
        }
    }

    public void run() {
        while (true) {
            List<Item> inventory = storeService.getInventory();
            outputView.welcomeMessageAndInventory(inventory);
            break;//구매 종료 분기 코드 작성 이전까지 임시 사용
        }
    }
}
