package ru.otus.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return data.stream()
                // группировка и суммирование по ключу name
                .collect(Collectors.groupingBy(
                        // группировка по ключу name
                        Measurement::name,
                        // сортировка по ключу name (результат - TreeMap)
                        TreeMap::new,
                        // суммирование по значению value
                        Collectors.summingDouble(Measurement::value)));
    }
}
