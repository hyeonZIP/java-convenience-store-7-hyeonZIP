package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String YES_NO_REGEX = "^[YN]$";

    public String askNameAndQuantity() {
        return Console.readLine();
    }

    public String askBuyingNoDiscountItem() {
        while (true) {
            try {
                String input = Console.readLine();
                validateYesOrNo(input);
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateYesOrNo(String input) {
        if (!input.matches(YES_NO_REGEX)) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
