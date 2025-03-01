package ru.otus.solid.recognition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.model.Banknote;
import ru.otus.solid.model.Blank;

@DisplayName("проверка работы алгоритма распознавания купюр")
class SimpleRecognitionAlghorithmTest {

    @DisplayName("если на вход подали бланк с атрибутами, соответствующими купюре в 50 рублей алгоритм вернет купюру")
    @Test
    void doBlank50Test() {
        var blank = new Blank(50);

        var algorithm = new SimpleRecognitionAlghorithm();

        var result = assertDoesNotThrow(() -> algorithm.recognize(blank));
        assertThat(result).isEqualTo(Banknote.BANKNOTE_50);
    }

    @DisplayName("если на вход подали бланк с атрибутами, соответствующими купюре в 5000 рублей алгоритм вернет купюру")
    @Test
    void doBlank5000Test() {
        var blank = new Blank(5000);

        var algorithm = new SimpleRecognitionAlghorithm();

        var result = assertDoesNotThrow(() -> algorithm.recognize(blank));
        assertThat(result).isEqualTo(Banknote.BANKNOTE_5000);
    }

    @DisplayName("если на вход подали бланк с атрибутами, не соответствующими никакой купюре - вылетит исключение")
    @Test
    void doRecognitionExceptionTest() {
        var blank = new Blank(150);

        var algorithm = new SimpleRecognitionAlghorithm();

        assertThrows(RecognitionException.class, () -> algorithm.recognize(blank));
    }
}
