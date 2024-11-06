package store;

import store.controller.StoreController;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();

        StoreController storeController = new StoreController(outputView);
        storeController.run();
    }
}
