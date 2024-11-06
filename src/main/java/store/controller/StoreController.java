package store.controller;

import store.view.OutputView;

public class StoreController {
    private final OutputView outputView;

    public StoreController(OutputView outputView) {
        this.outputView = outputView;
    }

    public void run() {
        while (true) {
            outputView.welcomeMessageAndProduct();
            break;//구매 종료 분기 코드 작성 이전까지 임시 사용
        }
    }
}
