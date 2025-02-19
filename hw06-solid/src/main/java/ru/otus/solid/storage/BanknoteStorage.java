package ru.otus.solid.storage;

import java.util.Map;
import ru.otus.solid.model.Banknote;

public interface BanknoteStorage {
    Map<Banknote, Integer> getBanknotesCount();

    void takeBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException;

    void putBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException;
}
