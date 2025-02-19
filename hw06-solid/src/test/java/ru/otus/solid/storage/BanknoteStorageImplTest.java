package ru.otus.solid.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.model.Banknote;

@DisplayName("Класс BanknoteStorageImpl")
class BanknoteStorageImplTest {

    @DisplayName("если создать объект то он вернет правильное количество купюр")
    @Test
    void getBanknotesCountAfterCreationTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 1000,
                Banknote.BANKNOTE_5000, 1);

        var banknoteStorage = new BanknoteStorageImpl(startValues);
        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 100,
                        Banknote.BANKNOTE_100, 0,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 1000,
                        Banknote.BANKNOTE_5000, 1));
    }

    @DisplayName(
            "если создать объект то он вернет правильное количество купюр, даже при модификации исходной структуры")
    @Test
    void getBanknotesCountAfterModificationInputStructureTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = new HashMap<>(Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 10,
                Banknote.BANKNOTE_5000, 1));

        var banknoteStorage = new BanknoteStorageImpl(startValues);
        assertThat(banknoteStorage.getBanknotesCount()).containsAllEntriesOf(startValues);

        startValues.put(Banknote.BANKNOTE_500, 10);
        startValues.remove(Banknote.BANKNOTE_50);

        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 100,
                        Banknote.BANKNOTE_100, 0,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 10,
                        Banknote.BANKNOTE_5000, 1));
    }

    @DisplayName("если количество запрашиваемых купюр не превышает доступное - метод корректно отрабатывает")
    @Test
    void getBanknotesCountAfterTakeTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 10,
                Banknote.BANKNOTE_5000, 1);

        var banknoteStorage = new BanknoteStorageImpl(startValues);
        assertThat(banknoteStorage.getBanknotesCount()).containsAllEntriesOf(startValues);

        Map<Banknote, Integer> takeValues = Map.of(Banknote.BANKNOTE_50, 2, Banknote.BANKNOTE_5000, 1);

        assertDoesNotThrow(() -> banknoteStorage.takeBanknotes(takeValues));

        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 98,
                        Banknote.BANKNOTE_100, 0,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 10,
                        Banknote.BANKNOTE_5000, 0));
    }

    @DisplayName(
            "если количество запрашиваемых купюр превышает доступное - вылетит исключение и количество купюр не изменится")
    @Test
    void exceptionOnTakeTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 10,
                Banknote.BANKNOTE_5000, 1);

        var banknoteStorage = new BanknoteStorageImpl(startValues);
        assertThat(banknoteStorage.getBanknotesCount()).containsAllEntriesOf(startValues);

        Map<Banknote, Integer> takeValues = Map.of(
                Banknote.BANKNOTE_50, 2,
                Banknote.BANKNOTE_5000, 2);

        assertThrows(BanknoteStorageException.class, () -> banknoteStorage.takeBanknotes(takeValues));

        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 100,
                        Banknote.BANKNOTE_100, 0,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 10,
                        Banknote.BANKNOTE_5000, 1));
    }

    @DisplayName("если количество купюр, которые кладутся в хранилище корректное - количество купюр пересчитывается")
    @Test
    void putSuccessTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 10,
                Banknote.BANKNOTE_5000, 1);

        var banknoteStorage = new BanknoteStorageImpl(startValues);

        Map<Banknote, Integer> putValues = Map.of(
                Banknote.BANKNOTE_50, 2,
                Banknote.BANKNOTE_100, 200,
                Banknote.BANKNOTE_5000, 2);

        assertDoesNotThrow(() -> banknoteStorage.putBanknotes(putValues));

        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 102,
                        Banknote.BANKNOTE_100, 200,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 10,
                        Banknote.BANKNOTE_5000, 3));
    }

    @DisplayName(
            "если количество купюр, которые кладутся в хранилище не корректное - количество купюр в хранилище не меняется")
    @Test
    void putExceptionTest() throws BanknoteStorageException {
        Map<Banknote, Integer> startValues = Map.of(
                Banknote.BANKNOTE_50, 100,
                Banknote.BANKNOTE_1000, 10,
                Banknote.BANKNOTE_5000, 1);

        var banknoteStorage = new BanknoteStorageImpl(startValues);

        Map<Banknote, Integer> putValues = Map.of(
                Banknote.BANKNOTE_50, 2,
                Banknote.BANKNOTE_100, 200,
                Banknote.BANKNOTE_5000, -2);

        assertThrows(BanknoteStorageException.class, () -> banknoteStorage.putBanknotes(putValues));

        assertThat(banknoteStorage.getBanknotesCount())
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        Banknote.BANKNOTE_50, 100,
                        Banknote.BANKNOTE_100, 0,
                        Banknote.BANKNOTE_500, 0,
                        Banknote.BANKNOTE_1000, 10,
                        Banknote.BANKNOTE_5000, 1));
    }
}
