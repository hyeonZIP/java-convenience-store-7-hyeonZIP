package store;

import store.controller.StoreController;
import store.model.Items;
import store.service.StoreService;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();

        Items items = new Items();

        StoreService storeService = new StoreService(items);

        StoreController storeController = new StoreController(outputView, storeService);
        storeController.beforeRun();
        storeController.run();
    }
}
