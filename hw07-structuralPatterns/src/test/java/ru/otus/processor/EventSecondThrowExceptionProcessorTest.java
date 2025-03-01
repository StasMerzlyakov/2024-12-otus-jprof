package ru.otus.processor;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

public class EventSecondThrowExceptionProcessorTest {

    @Test
    void checkThrowException() {
        var mock = mock(DateTimeProvider.class);
        when(mock.getDate()).thenReturn(LocalDateTime.of(2025, 3, 1, 14, 13, 30));
        var processor = new EventSecondThrowExceptionProcessor(mock);

        var message = new Message.Builder(1L).field8("field8").build();

        assertThatExceptionOfType(EventSecondException.class).isThrownBy(() -> processor.process(message));
    }

    @Test
    void checkDoesNotThrowException() {
        var mock = mock(DateTimeProvider.class);
        when(mock.getDate()).thenReturn(LocalDateTime.of(2025, 3, 1, 14, 13, 31));
        var processor = new EventSecondThrowExceptionProcessor(mock);

        var message = new Message.Builder(1L).field8("field8").build();

        assertThatNoException().isThrownBy(() -> processor.process(message));
    }
}
