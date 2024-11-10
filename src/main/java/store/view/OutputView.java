package store.view;

import store.model.Item;
import store.model.SelectItem;
import store.model.Store;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.List;

public class OutputView {
    private static final String LINE_SEPARATOR = "\n";
    private static final String WELCOME_MESSAGE = "\n안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.";
    private static final String DECIMAL_FORMAT = "#,###";
    private static final String ASK_BUYING_ITEM = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat(DECIMAL_FORMAT);
    private static final String OUT_OF_STOCK_PROMOTION = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String FREE_ITEM = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니가? (Y/N)";
    private static final String ASK_MEMBER_SHIP = "\n멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String RECEIPT_HEADER = "\n==============W 편의점================";
    private static final String ITEM_HEADER = String.format("%-12s %-12s %-12s", "상품명", "수량", "금액");
    private static final String PROMOTION = "=============증\t정==================";
    private static final String SEPARATOR = "====================================";
    private static final String TOTAL_PRICE = "총구매액";
    private static final String PROMOTION_DISCOUNT = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인";
    private static final String PAID_MONEY = "내실돈";
    private static final String ASK_ADDITIONAL_BUY = "\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String HYPHEN = "-";

    public void outOfPromotion(String itemName, int nonDiscountCount) {
        System.out.println(LINE_SEPARATOR + String.format(OUT_OF_STOCK_PROMOTION, itemName, nonDiscountCount));
    }

    public void freeItem(String itemName) {
        System.out.println(LINE_SEPARATOR + String.format(FREE_ITEM, itemName));
    }

    public void welcomeMessageAndInventory(List<Item> inventory) {
        printWelcomeMessage();
        printInventory(inventory);
    }

    public void printResult(EnumMap<Store.Receipt, Integer> result, List<SelectItem> selectList) {
        System.out.println(RECEIPT_HEADER);
        System.out.println(ITEM_HEADER);
        for (SelectItem selectItem : selectList) {
            int price = selectItem.getPrice() * selectItem.getQuantity();
            System.out.println(String.format("%-12s %-12s %-12s",
                    selectItem.getName(),
                    selectItem.getQuantity(),
                    PRICE_FORMAT.format(price)));
        }
        System.out.println(PROMOTION);
        for (SelectItem selectItem : selectList) {
            if (selectItem.getPromotion() != 0) {
                System.out.println(String.format("%-10s %-10s", selectItem.getName(), selectItem.getPromotion()));
            }
        }
        System.out.println(SEPARATOR);
        System.out.println(String.format("%-10s %-10s %-10s", TOTAL_PRICE, result.get(Store.Receipt.TOTAL_ITEM_COUNT), PRICE_FORMAT.format(result.get(Store.Receipt.TOTAL_PRICE))));
        System.out.println(String.format("%-20s %-10s", PROMOTION_DISCOUNT, HYPHEN + PRICE_FORMAT.format(result.get(Store.Receipt.PROMOTION_DISCOUNT))));
        System.out.println(String.format("%-20s %-10s", MEMBERSHIP_DISCOUNT, HYPHEN + PRICE_FORMAT.format(result.getOrDefault(Store.Receipt.MEMBERSHIP_DISCOUNT, 0))));
        System.out.println(String.format("%-20s %-10s", PAID_MONEY, PRICE_FORMAT.format(result.get(Store.Receipt.PAID_MONEY))));
    }

    public void askAdditionalBuy() {
        System.out.println(ASK_ADDITIONAL_BUY);
    }

    public void askMembership() {
        System.out.println(ASK_MEMBER_SHIP);
    }

    private void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE + LINE_SEPARATOR);
    }

    private void printInventory(List<Item> inventory) {
        StringBuilder wholeInventory = new StringBuilder();
        for (Item item : inventory) {
            wholeInventory.append(formatInventory(item)).append(LINE_SEPARATOR);
        }
        wholeInventory.append(LINE_SEPARATOR).append(ASK_BUYING_ITEM);
        System.out.println(wholeInventory);
    }

    private String formatInventory(Item item) {
        String quantity = formatQuantity(item.getQuantity());
        String promotion = formatPromotion(item.getPromotion());
        return String.format("- %s %s원 %s %s",
                item.getName(),
                PRICE_FORMAT.format(item.getPrice()),
                quantity,
                promotion);
    }

    private String formatQuantity(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    private String formatPromotion(String promotion) {
        if (promotion.equals("null")) {
            return "";
        }
        return promotion;
    }
}
