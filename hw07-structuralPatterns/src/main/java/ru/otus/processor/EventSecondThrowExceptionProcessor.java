package ru.otus.processor;

import ru.otus.model.Message;

/**
 * Процессор, который выбрасывает исключение в четную секунду
 */
public class EventSecondThrowExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EventSecondThrowExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var second = dateTimeProvider.getDate().getSecond();

        if (second % 2 == 0) {
            throw new EventSecondException("even second");
        }

        return message;
    }
}
