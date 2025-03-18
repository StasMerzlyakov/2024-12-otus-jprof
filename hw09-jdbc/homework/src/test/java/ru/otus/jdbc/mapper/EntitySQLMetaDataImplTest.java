package ru.otus.jdbc.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

public class EntitySQLMetaDataImplTest {
    @Test
    @DisplayName("проверка Client")
    void doTest1() {
        var impl = new EntityClassMetaDataImpl(Client.class);
        var sqlMeta = new EntitySQLMetaDataImpl(impl);

        assertThat(sqlMeta.getSelectByIdSql()).isEqualTo("select id, name from client where id = ?");
        assertThat(sqlMeta.getSelectAllSql()).isEqualTo("select id, name from client");
        assertThat(sqlMeta.getInsertSql()).isEqualTo("insert into client(name) values (?)");
        assertThat(sqlMeta.getUpdateSql()).isEqualTo("update client set name = ? where id = ?");
    }

    @Test
    @DisplayName("проверка Manager")
    void doTest2() {
        var impl = new EntityClassMetaDataImpl(Manager.class);
        var sqlMeta = new EntitySQLMetaDataImpl(impl);

        assertThat(sqlMeta.getSelectByIdSql()).isEqualTo("select no, label, param1 from manager where no = ?");
        assertThat(sqlMeta.getSelectAllSql()).isEqualTo("select no, label, param1 from manager");
        assertThat(sqlMeta.getInsertSql()).isEqualTo("insert into manager(label, param1) values (?, ?)");
        assertThat(sqlMeta.getUpdateSql()).isEqualTo("update manager set label = ?, param1 = ? where no = ?");
    }
}
