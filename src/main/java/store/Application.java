package store;

import store.controller.StoreController;
import store.model.Items;
import store.model.Promotions;
import store.model.ReceiptItems;
import store.model.Store;
import store.service.StoreService;
import store.validator.StoreValidator;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        StoreController storeController = createStoreController();
        storeController.beforeRun();
        storeController.run();
    }

    private static StoreController createStoreController() {
        OutputView outputView = new OutputView();
        InputView inputView = new InputView();
        Store store = createStore();
        StoreValidator storeValidator = new StoreValidator();
        StoreService storeService = new StoreService(store.getPromotions(), store.getItems(), store, storeValidator);
        return new StoreController(outputView, inputView, storeService);
    }

    private static Store createStore() {
        Items items = new Items();
        Promotions promotions = new Promotions();
        ReceiptItems receiptItems = new ReceiptItems();
        return new Store(items, promotions, receiptItems);
    }
}
