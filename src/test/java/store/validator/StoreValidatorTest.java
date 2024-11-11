package store.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreValidatorTest {

    private StoreValidator storeValidator = new StoreValidator();

    @ParameterizedTest
    @CsvSource(value = {"'[사이다1]'", "'[[사이다-1]]'", "'[사이다-1]]'", "'  '"})
    @DisplayName("입력 예외 테스트")
    void validateFormat(final String input) {
        assertThatThrownBy(() -> storeValidator.validateFormat(input)).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"'[사이다-5],[사이다-3]'", "'[콜라-1],[콜라-8]'"})
    @DisplayName("중복 입력 예외 테스트")
    void validateDuplicate(final String input) {
        List<String> inputs = List.of(input.split(","));
        assertThatThrownBy(() -> storeValidator.validateDuplicate(inputs)).isInstanceOf(IllegalArgumentException.class);
    }
}