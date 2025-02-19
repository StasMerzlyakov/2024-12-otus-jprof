package ru.otus.solid.atm;

import java.util.Map;
import ru.otus.solid.ATM;
import ru.otus.solid.ATMException;
import ru.otus.solid.PutMoneyOperation;
import ru.otus.solid.exchange.BanknoteExchangeAlgorithm;
import ru.otus.solid.exchange.BanknoteExchangeAlgorithmException;
import ru.otus.solid.model.Banknote;
import ru.otus.solid.recognition.RecognitionAlgorithm;
import ru.otus.solid.storage.BanknoteStorage;
import ru.otus.solid.storage.BanknoteStorageException;

public class ATMImpl implements ATM {

    private final BanknoteStorage banknoteStorage;
    private final RecognitionAlgorithm recognitionAlgorithm;
    private final BanknoteExchangeAlgorithm banknoteExchangeAlgorithm;

    public ATMImpl(
            BanknoteStorage banknoteStorage,
            RecognitionAlgorithm recognitionAlgorithm,
            BanknoteExchangeAlgorithm banknoteExchangeAlgorithm) {
        this.banknoteStorage = banknoteStorage;
        this.recognitionAlgorithm = recognitionAlgorithm;
        this.banknoteExchangeAlgorithm = banknoteExchangeAlgorithm;
    }

    @Override
    public Map<Banknote, Integer> getMoney(int sumDesired) throws ATMException {
        var currentValues = banknoteStorage.getBanknotesCount();
        try {
            var exchange = banknoteExchangeAlgorithm.calculateExchange(currentValues, sumDesired);
            banknoteStorage.takeBanknotes(exchange);
            return exchange;
        } catch (BanknoteExchangeAlgorithmException | BanknoteStorageException e) {
            throw new ATMException(e.getMessage());
        }
    }

    @Override
    public PutMoneyOperation startPutMoneyOperation() {
        return new PutMoneyOperationImpl(recognitionAlgorithm, banknoteStorage);
    }

    @Override
    public int remaining() {
        return banknoteStorage.getBanknotesCount().entrySet().stream()
                .mapToInt(entry -> entry.getKey().nominal() * entry.getValue())
                .sum();
    }
}
