package ru.otus.solid.exchange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.model.Banknote;

@DisplayName("Класс GreedyBanknoteExchangeAlgorithmTest")
class GreedyBanknoteExchangeAlgorithmTest {

    @DisplayName("если задать желаемую сумму меньше номинала минимальной купюры(50) - вылетит исключение")
    @Test
    void doCalculateExchangeWithTooLowDesiredSum() {
        Map<Banknote, Integer> actualBanknoteCounts = Map.of(
                Banknote.BANKNOTE_5000, 1,
                Banknote.BANKNOTE_1000, 1000);

        var algorithm = new GreedyBanknoteExchangeAlgorithm();

        var minimal = Banknote.BANKNOTE_50.nominal();

        assertThrows(
                BanknoteExchangeAlgorithmException.class,
                () -> algorithm.calculateExchange(actualBanknoteCounts, minimal - 1));
    }

    @DisplayName("если задать желаемую сумму, которую можно выдать - алгоритм выдаст правильные значения")
    @Test
    void doCalculateExchangeWithGoodDesiredSum() {
        Map<Banknote, Integer> actualBanknoteCounts = Map.of(
                Banknote.BANKNOTE_5000, 1,
                Banknote.BANKNOTE_100, 3,
                Banknote.BANKNOTE_50, 2,
                Banknote.BANKNOTE_1000, 15);

        var algorithm = new GreedyBanknoteExchangeAlgorithm();

        var desiredSum = 16200;

        var result = assertDoesNotThrow(() -> algorithm.calculateExchange(actualBanknoteCounts, desiredSum));

        assertThat(result)
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_100, 2,
                        Banknote.BANKNOTE_1000, 11,
                        Banknote.BANKNOTE_5000, 1));
    }

    @DisplayName("если задать желаемую сумму больше доступной - вылетит исключение")
    @Test
    void doCalculateExchangeWithTooHighDesiredSum() {
        Map<Banknote, Integer> actualBanknoteCounts = Map.of(
                Banknote.BANKNOTE_5000, 1,
                Banknote.BANKNOTE_1000, 10);

        var algorithm = new GreedyBanknoteExchangeAlgorithm();

        var minimal = 16000;

        assertThrows(
                BanknoteExchangeAlgorithmException.class,
                () -> algorithm.calculateExchange(actualBanknoteCounts, minimal));
    }
}
