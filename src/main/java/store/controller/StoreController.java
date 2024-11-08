package store.controller;

import store.service.StoreService;
import store.view.OutputView;

import java.io.BufferedReader;
import java.io.IOException;

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
        } catch (IOException e) {

        }

    }

    public void run() {
        while (true) {
            outputView.welcomeMessageAndProduct();
            break;//구매 종료 분기 코드 작성 이전까지 임시 사용
        }
    }
}
