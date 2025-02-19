package ru.otus.solid.exchange;

import java.util.Map;
import ru.otus.solid.model.Banknote;

public interface BanknoteExchangeAlgorithm {
    Map<Banknote, Integer> calculateExchange(Map<Banknote, Integer> actualBanknoteCounts, int sumDesired)
            throws BanknoteExchangeAlgorithmException;
}
