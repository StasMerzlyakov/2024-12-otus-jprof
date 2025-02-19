package ru.otus.solid;

import java.util.Map;
import ru.otus.solid.model.Banknote;

public interface ATM {

    /**
     * Получение средств
     * @param sumDesired
     * @return
     * @throws ATMException
     */
    Map<Banknote, Integer> getMoney(int sumDesired) throws ATMException;

    /**
     * Операция добавления денег вынесена в отдельный объект, так как в процессе добавления могу попасться как купюры,
     * принимаемые банкоматом, так и не принятые.
     *
     * @return
     */
    PutMoneyOperation startPutMoneyOperation();

    /**
     * Остаток денежных средств
     * @return
     */
    int remaining();
}
