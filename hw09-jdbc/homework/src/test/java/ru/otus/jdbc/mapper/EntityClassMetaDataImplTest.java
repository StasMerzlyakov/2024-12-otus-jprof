package ru.otus.jdbc.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;

public class EntityClassMetaDataImplTest {
    @Test
    @DisplayName("проверка Client")
    void doTest1() {
        var impl = assertDoesNotThrow(() -> new EntityClassMetaDataImpl(Client.class));
        assertThat(impl.getName()).isEqualTo("client");
    }
}
