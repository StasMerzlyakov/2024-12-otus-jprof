package ru.otus.solid;

import ru.otus.solid.atm.ATMImpl;
import ru.otus.solid.exchange.BanknoteExchangeAlgorithm;
import ru.otus.solid.recognition.RecognitionAlgorithm;
import ru.otus.solid.storage.BanknoteStorage;

public class ATMBuilder {
    private BanknoteStorage banknoteStorage;
    private RecognitionAlgorithm recognitionAlgorithm;
    private BanknoteExchangeAlgorithm banknoteExchangeAlgorithm;

    ATMBuilder withBanknoteStorage(BanknoteStorage banknoteStorage) {
        this.banknoteStorage = banknoteStorage;
        return this;
    }

    ATMBuilder withRecognitionAlgorithm(RecognitionAlgorithm recognitionAlgorithm) {
        this.recognitionAlgorithm = recognitionAlgorithm;
        return this;
    }

    ATMBuilder withBanknoteExchangeAlgorithm(BanknoteExchangeAlgorithm banknoteExchangeAlgorithm) {
        this.banknoteExchangeAlgorithm = banknoteExchangeAlgorithm;
        return this;
    }

    ATM build() throws ConfigurationException {
        if (banknoteStorage == null) {
            throw new ConfigurationException("banknoteStorage is null");
        }

        if (banknoteExchangeAlgorithm == null) {
            throw new ConfigurationException("banknoteExchangeAlgorithm is null");
        }

        if (recognitionAlgorithm == null) {
            throw new ConfigurationException("recognitionAlgorithm is null");
        }

        return new ATMImpl(banknoteStorage, recognitionAlgorithm, banknoteExchangeAlgorithm);
    }
}
