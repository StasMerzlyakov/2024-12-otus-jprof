package ru.otus.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

public class SwapProcessorTest {

    @Test
    void swapTest() {
        var processor = new SwapProcessor();

        var field11 = "field11";
        var field12 = "field12";
        var inputMessage =
                new Message.Builder(1L).field11(field11).field12(field12).build();

        var outputMessage = processor.process(inputMessage);

        // проверка выходного сообщения
        assertThat(outputMessage.getField11()).isEqualTo(field12);
        assertThat(outputMessage.getField12()).isEqualTo(field11);

        // проверка исходного сообщения
        assertThat(inputMessage.getField11()).isEqualTo(field11);
        assertThat(inputMessage.getField12()).isEqualTo(field12);
    }
}
