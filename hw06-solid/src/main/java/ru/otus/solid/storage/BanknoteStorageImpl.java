package ru.otus.solid.storage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.solid.model.Banknote;

public class BanknoteStorageImpl implements BanknoteStorage {

    private static final Logger logger = LoggerFactory.getLogger(BanknoteStorageImpl.class);

    private Map<Banknote, Integer> storage = new HashMap<>(Map.of(
            Banknote.BANKNOTE_50, 0,
            Banknote.BANKNOTE_100, 0,
            Banknote.BANKNOTE_500, 0,
            Banknote.BANKNOTE_1000, 0,
            Banknote.BANKNOTE_5000, 0));

    public BanknoteStorageImpl(Map<Banknote, Integer> banknotes) throws BanknoteStorageException {
        logger.atDebug()
                .setMessage("BankImpl initiated with {}")
                .addArgument(banknotes.entrySet().stream()
                        .map(entry -> String.format("%s - %d", entry.getKey(), entry.getValue()))
                        .reduce(",", String::concat));
        checkInputValues(banknotes);
        storage.putAll(banknotes);
        logger.debug("BankImpl successfully initiated");
    }

    private void checkInputValues(Map<Banknote, Integer> banknotes) throws BanknoteStorageException {
        for (var entry : banknotes.entrySet()) {
            if (entry.getValue() < 0) {
                throw new BanknoteStorageException(
                        String.format("wong banknotes value %s for banknote %s", entry.getKey(), entry.getValue()));
            }
        }
        logger.debug("checkInputValues success");
    }

    @Override
    public Map<Banknote, Integer> getBanknotesCount() {
        return Collections.unmodifiableMap(this.storage);
    }

    @Override
    public void takeBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException {
        logger.atDebug()
                .setMessage("takeBanknotes start {}")
                .addArgument(banknotes.entrySet().stream()
                        .map(entry -> String.format(
                                "banknote: nominal %d, value: %d",
                                entry.getKey().nominal(), entry.getValue()))
                        .reduce(",", String::concat));

        HashMap<Banknote, Integer> newValues = new HashMap<>();
        newValues.putAll(storage);

        // сначала проверяем (транзакций нет)
        for (var entry : banknotes.entrySet()) {
            var countToTake = entry.getValue();
            var banknote = entry.getKey();
            if (countToTake < 0) {
                throw new BanknoteStorageException(
                        String.format("wong banknotes with nominal %d count - %d", banknote.nominal(), countToTake));
            }

            var currentCount = newValues.getOrDefault(banknote, 0);
            if (currentCount < countToTake) {
                throw new BanknoteStorageException(String.format(
                        "not enough banknotes with nominal %d - current value %d, desired quantity %d",
                        banknote.nominal(), currentCount, countToTake));
            }

            newValues.put(banknote, currentCount - countToTake);
        }

        storage = newValues;
        logger.debug("takeBanknotes complete");
    }

    @Override
    public void putBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException {
        logger.atDebug()
                .setMessage("putBanknotes start {}")
                .addArgument(banknotes.entrySet().stream()
                        .map(entry -> String.format("banknote: %s, value: %d", entry.getKey(), entry.getValue()))
                        .reduce(",", String::concat));

        checkInputValues(banknotes);

        banknotes.forEach((k, v) -> {
            var currentVal = storage.getOrDefault(k, 0);
            storage.put(k, currentVal + v);
        });
        logger.debug("putBanknotes complete");
    }
}
