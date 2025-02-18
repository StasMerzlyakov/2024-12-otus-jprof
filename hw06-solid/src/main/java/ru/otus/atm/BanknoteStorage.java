package ru.otus.atm;

import java.util.Map;

public interface BanknoteStorage {
    Map<Banknote, Integer> getBanknotesCount();

    void takeBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException;

    void putBanknotes(Map<Banknote, Integer> banknotes) throws BanknoteStorageException;
}
