package ru.otus.solid;

import ru.otus.solid.model.Blank;

public interface PutMoneyOperation {

    /**
     * Вызывается для каждой купюры в пачке.
     *
     * @param blank купюра
     * @return true - если купюра принята банкоматом
     * false - если купюра не принимается
     * @throws ATMException, если метод вызван после метода complete
     */
    boolean accept(Blank blank) throws ATMException;

    /**
     * Завершает операцию. Добавляет распознанные банкноты в банкомат.
     */
    void complete() throws ATMException;
}
