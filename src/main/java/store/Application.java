package store;

import store.controller.StoreController;
import store.service.StoreService;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();

        StoreService storeService = new StoreService();

        StoreController storeController = new StoreController(outputView, storeService);
        storeController.beforeRun();
        storeController.run();
    }
}
