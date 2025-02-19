package ru.otus.solid.recognition;

import ru.otus.solid.model.Banknote;
import ru.otus.solid.model.Blank;

public interface RecognitionAlgorithm {
    Banknote recognize(Blank blank) throws RecognitionException;
}
