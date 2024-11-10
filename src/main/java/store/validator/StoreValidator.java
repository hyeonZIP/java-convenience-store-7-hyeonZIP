package store.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreValidator {
    private static final String FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String ETC_ERROR = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String REGEX = "^\\[([가-힣a-zA-Z0-9]+)-([0-9]+)\\](,\\[([가-힣a-zA-Z0-9]+)-([0-9]+)\\])*$";
    private static final String HYPHEN = "-";

    public void validateFormat(final String input) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
    }

    public void validateDuplicate(final List<String> items) {
        Set<String> dupleCheckItem = new HashSet<>();
        for (String item : items) {
            String name = item.split(HYPHEN)[0];
            if (!dupleCheckItem.add(name)) {
                throw new IllegalArgumentException(ETC_ERROR);
            }
        }
    }
}
