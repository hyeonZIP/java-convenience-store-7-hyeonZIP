package store.validator;

import java.util.List;

public class StoreValidator {
    private static final String FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public void validateBlank(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }

    public void validateBrakets(String input) {
        if (!input.startsWith("[") || !input.endsWith("]")) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }

    public void validateSize(List<String> input) {
        if (input.size() != 2) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }

    public void validateParsing(String input) {
        try {
            Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }
}
