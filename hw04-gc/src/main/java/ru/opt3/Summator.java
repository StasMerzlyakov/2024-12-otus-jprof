package ru.opt3;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        if (listValues.size() % 100_000 == 0) {
            listValues.clear();
        }
        sum += data.getValue() + random.nextInt();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + listValues.size();
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
