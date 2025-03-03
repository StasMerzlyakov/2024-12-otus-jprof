package ru.otus.processor;

import ru.otus.model.Message;

/**
 * Процессор, который поменяет местами значения field11 и field12
 */
public class SwapField11Field12Processor implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field12 = message.getField12();
        return message.toBuilder().field11(field12).field12(field11).build();
    }
}
