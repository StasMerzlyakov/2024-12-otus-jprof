package ru.otus.solid.atm;

import java.util.HashMap;
import java.util.Map;
import ru.otus.solid.ATMException;
import ru.otus.solid.PutMoneyOperation;
import ru.otus.solid.model.Banknote;
import ru.otus.solid.model.Blank;
import ru.otus.solid.recognition.RecognitionAlgorithm;
import ru.otus.solid.recognition.RecognitionException;
import ru.otus.solid.storage.BanknoteStorage;
import ru.otus.solid.storage.BanknoteStorageException;

public class PutMoneyOperationImpl implements PutMoneyOperation {

    private boolean isComplete;
    private RecognitionAlgorithm recognitionAlgorithm;
    private BanknoteStorage banknoteStorage;
    private Map<Banknote, Integer> banknotesToPut;

    public PutMoneyOperationImpl(RecognitionAlgorithm recognitionAlgorithm, BanknoteStorage banknoteStorage) {
        this.recognitionAlgorithm = recognitionAlgorithm;
        this.banknoteStorage = banknoteStorage;
        this.banknotesToPut = new HashMap<>();
        this.isComplete = false;
    }

    @Override
    public boolean accept(Blank blank) throws ATMException {
        if (isComplete) {
            throw new ATMException("operation completed");
        }

        try {
            var banknote = recognitionAlgorithm.recognize(blank);

            var currentCount = banknotesToPut.getOrDefault(banknote, 0);
            banknotesToPut.put(banknote, currentCount + 1);
            return true;
        } catch (RecognitionException re) {
            return false;
        }
    }

    @Override
    public void complete() throws ATMException {
        if (!isComplete) {
            try {
                banknoteStorage.putBanknotes(banknotesToPut);
            } catch (BanknoteStorageException e) {
                throw new ATMException(e.getMessage());
            }
            isComplete = true;
        }
    }
}
