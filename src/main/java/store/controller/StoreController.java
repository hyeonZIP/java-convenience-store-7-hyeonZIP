package store.controller;

import store.model.Item;
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
            boolean promotionDate = storeService.isPromotionDate(entry.getKey());//프로모션 기간일 경우 true/기간이 아니거나 프로모션이 존재하지 않을 경우 false
            EnumMap<Item.PromotionResult, String> promotionResult;//프로모션 재고에서 요구수량을 뺸 값
            if (promotionDate) {
                promotionResult = storeService.getRequestedQuantity(entry.getKey(), entry.getValue());//해당하는 프로모션 만큼 요구 수량을 차감하고 난 정수 반환
                askCustomerChoice(promotionResult);
            }
        }
    }

    private void askCustomerChoice(EnumMap<Item.PromotionResult, String> promotionResult) {
        try {
            int nonDiscountCount = Integer.parseInt(promotionResult.get(Item.PromotionResult.INSUFFICIENT_INVENTORY));
            if (nonDiscountCount < 0) {
                String itemName = promotionResult.get(Item.PromotionResult.ITEM_NAME);
                outputView.outOfPromotion(itemName, Math.abs(nonDiscountCount));
                String YesOrNo = inputView.askBuyingNoDiscountItem();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
