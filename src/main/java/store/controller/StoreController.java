package store.controller;

import store.model.Item;
import store.model.SelectItem;
import store.model.Store;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.EnumMap;
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

            saleItem(inventoryAndQuantity);

            EnumMap<Store.Receipt, Integer> receipt = storeService.getReceipt();
            List<SelectItem> selectItem = storeService.getSelectList();
            outputView.printResult(receipt, selectItem);
            outputView.askAdditionalBuy();
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

    private void saleItem(Map<List<Item>, Integer> inventoryAndQuantity) {
        for (Map.Entry<List<Item>, Integer> entry : inventoryAndQuantity.entrySet()) {
            boolean promotionDate = storeService.isPromotionDate(entry.getKey());
            EnumMap<Item.PromotionResult, String> promotionResult = storeService.getRequestedQuantity(entry.getKey(), entry.getValue());
            if (promotionDate) {
                askCustomerChoice(promotionResult);
            }
            if (!promotionDate) {
                storeService.makeReceiptBeforeMembership(promotionResult);
            }
        }
        outputView.askMembership();
        String yesOrNo = inputView.askMembership();
        if (yesOrNo.equals("Y")) {
            storeService.applyMembership();
        }
    }

    private void askCustomerChoice(EnumMap<Item.PromotionResult, String> promotionResult) {
        try {
            if (storeService.checkPromotionState(promotionResult)) {
                outputView.outOfPromotion(promotionResult.get(Item.PromotionResult.ITEM_NAME), Math.abs(Integer.parseInt(promotionResult.get(Item.PromotionResult.NON_DISCOUNT_COUNT))));
                String yesOrNo = inputView.askBuyingNoDiscountItem();
                if (yesOrNo.equals("N")) {
                    return;
                }
                storeService.makeReceiptBeforeMembership(promotionResult);
                return;
            }
            if (storeService.checkGettingFreeItem(promotionResult)) {
                outputView.freeItem(promotionResult.get(Item.PromotionResult.ITEM_NAME));
                String yesOrNo = inputView.askGetFreeItem();
                if (yesOrNo.equals("Y")) {
                    promotionResult.put(Item.PromotionResult.REQUEST_QUANTITY, String.valueOf(Integer.parseInt(promotionResult.get(Item.PromotionResult.REQUEST_QUANTITY)) + 1));
                    promotionResult.put(Item.PromotionResult.DISCOUNT_COUNT, String.valueOf(Integer.parseInt(promotionResult.get(Item.PromotionResult.DISCOUNT_COUNT) + 1)));
                }
                storeService.makeReceiptBeforeMembership(promotionResult);
                return;
            }
            storeService.makeReceiptBeforeMembership(promotionResult);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
