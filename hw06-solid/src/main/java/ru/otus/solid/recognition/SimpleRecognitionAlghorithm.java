package ru.otus.solid.recognition;

import ru.otus.solid.model.Banknote;
import ru.otus.solid.model.Blank;

public class SimpleRecognitionAlghorithm implements RecognitionAlgorithm {

    @Override
    public Banknote recognize(Blank blank) throws RecognitionException {
        // константы в коде, но на выражение
        // case Banknote.BANKNOTE_50.nominal() ->  Banknote.BANKNOTE_50 компилятор java ругается; это не kotlin :((
        return switch (blank.nominal()) {
            case 50 -> Banknote.BANKNOTE_50;
            case 100 -> Banknote.BANKNOTE_100;
            case 500 -> Banknote.BANKNOTE_500;
            case 1000 -> Banknote.BANKNOTE_1000;
            case 5000 -> Banknote.BANKNOTE_5000;
            default -> throw new RecognitionException("unrecognized cupure");
        };
    }
}
