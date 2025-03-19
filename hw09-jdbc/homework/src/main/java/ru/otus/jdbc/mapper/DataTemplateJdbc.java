package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Function<ResultSet, T> rsHandler;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            Function<ResultSet, T> rsHandler,
            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.rsHandler = rsHandler;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rsHandler);
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelectAll(connection, entitySQLMetaData.getSelectAllSql(), rsHandler);
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> args = new LinkedList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                Object fieldValue = field.get(client);
                args.add(fieldValue);
            }
        } catch (Exception e) {
            throw new DataTemplateJdbcException("can't get field values: " + e.getMessage());
        }

        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), args);
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> args = new LinkedList<>();
        try {
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                Object fieldValue = field.get(client);
                args.add(fieldValue);
            }
        } catch (Exception e) {
            throw new DataTemplateJdbcException("can't get field values: " + e.getMessage());
        }

        Field idField = entityClassMetaData.getIdField();
        try {
            Object fieldValue = idField.get(client);
            args.add(fieldValue);
        } catch (Exception e) {
            throw new DataTemplateJdbcException("can't get id values: " + e.getMessage());
        }

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), args);
    }
}
