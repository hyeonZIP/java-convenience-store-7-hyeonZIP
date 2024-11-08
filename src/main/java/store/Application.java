package store;

import store.controller.StoreController;
import store.model.Items;
import store.service.StoreService;
import store.validator.StoreValidator;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();

        Items items = new Items();

        StoreValidator storeValidator = new StoreValidator();

        StoreService storeService = new StoreService(items, storeValidator);

        StoreController storeController = new StoreController(outputView, inputView, storeService);
        storeController.beforeRun();
        storeController.run();
    }
}
