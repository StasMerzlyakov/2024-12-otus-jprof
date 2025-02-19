package ru.otus.solid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.exchange.GreedyBanknoteExchangeAlgorithm;
import ru.otus.solid.model.Banknote;
import ru.otus.solid.model.Blank;
import ru.otus.solid.recognition.SimpleRecognitionAlghorithm;
import ru.otus.solid.storage.BanknoteStorageImpl;

@DisplayName("проверка класса ATMImpl")
class IntegrationATMTest {

    @DisplayName("проверяем что ATM выдает правильную сумму")
    @Test
    void doInitTest() throws Exception {
        var atm500 = createATM500();
        assertThat(atm500.remaining()).isEqualTo(3325000);
    }

    @DisplayName("проверяем операцию выдачи")
    @Test
    void doGetMoneyTest() throws Exception {
        var atm500 = createATM500();

        var before = 3325000;
        var desireSum = 22550;

        assertThat(atm500.remaining()).isEqualTo(before);

        var result = assertDoesNotThrow(() -> atm500.getMoney(desireSum));

        // Проверяем только общую сумму без завязки на алгоритм размена
        var resultCount = result.entrySet().stream()
                .mapToInt(entry -> entry.getKey().nominal() * entry.getValue())
                .sum();

        assertThat(resultCount).isEqualTo(desireSum);

        assertThat(atm500.remaining()).isEqualTo(before - desireSum);
    }

    @DisplayName("проверяем операцию внесения денег")
    @Test
    void doPutMoneyTest() throws Exception {
        var atm500 = createATM500();

        var before = 3325000;

        assertThat(atm500.remaining()).isEqualTo(before);

        Iterable<Blank> blanksToPut = List.of(
                new Blank(100),
                new Blank(100),
                new Blank(100),
                new Blank(100),
                new Blank(100),
                new Blank(500),
                new Blank(5000),
                new Blank(600) // не должна быть принята
                ); // общая сумма 6000

        var operation = assertDoesNotThrow(atm500::startPutMoneyOperation);

        for (Blank blank : blanksToPut) {
            var result = assertDoesNotThrow(() -> operation.accept(blank));
            assertThat(result).isEqualTo(blank.nominal() != 600);
        }

        assertDoesNotThrow(operation::complete);

        assertThat(atm500.remaining()).isEqualTo(before + 6000);
    }

    private ATM createATM500() throws Exception {
        var storage = new BanknoteStorageImpl(Map.of(
                Banknote.BANKNOTE_50, 500,
                Banknote.BANKNOTE_100, 500,
                Banknote.BANKNOTE_500, 500,
                Banknote.BANKNOTE_1000, 500,
                Banknote.BANKNOTE_5000, 500));

        var exchangeAlgorithm = new GreedyBanknoteExchangeAlgorithm();
        var recognitionAlgorithm = new SimpleRecognitionAlghorithm();

        return new ATMBuilder()
                .withRecognitionAlgorithm(recognitionAlgorithm)
                .withBanknoteExchangeAlgorithm(exchangeAlgorithm)
                .withBanknoteStorage(storage)
                .build();
    }
}
