package ru.otus.jdbc.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

class EntityClassMetaDataImplTest {
    @Test
    @DisplayName("проверка Client")
    void doTest1() {
        var impl = assertDoesNotThrow(() -> new EntityClassMetaDataImpl(Client.class));
        assertThat(impl.getName()).isEqualTo("client");
    }

    @Test
    @DisplayName("проверка Manager")
    void doTest2() {
        var impl = assertDoesNotThrow(() -> new EntityClassMetaDataImpl(Manager.class));
        assertThat(impl.getName()).isEqualTo("manager");
    }
}
