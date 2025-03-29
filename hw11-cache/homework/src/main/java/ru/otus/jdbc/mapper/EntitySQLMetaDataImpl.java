package ru.otus.jdbc.mapper;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private EntityClassMetaData<T> entityClassMetaData;

    private Map<String, String> queryMap = new HashMap<>();

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return queryMap.computeIfAbsent(
                "selectAllSql",
                s -> String.format(
                        "select %s from %s",
                        entityClassMetaData.getAllFields().stream()
                                .map(Member::getName)
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getName()));
    }

    @Override
    public String getSelectByIdSql() {
        return queryMap.computeIfAbsent(
                "selectByIdSql",
                s -> String.format(
                        "select %s from %s where %s = ?",
                        entityClassMetaData.getAllFields().stream()
                                .map(Member::getName)
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getName(),
                        entityClassMetaData.getIdField().getName()));
    }

    @Override
    public String getInsertSql() {
        return queryMap.computeIfAbsent(
                "insertSql",
                s -> String.format(
                        "insert into %s(%s) values (%s)",
                        entityClassMetaData.getName(),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Member::getName)
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(field -> "?")
                                .collect(Collectors.joining(", "))));
    }

    @Override
    public String getUpdateSql() {
        return queryMap.computeIfAbsent(
                "updateSql",
                s -> String.format(
                        "update %s set %s where %s = ?",
                        entityClassMetaData.getName(),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(field -> field.getName() + " = ?")
                                .collect(Collectors.joining(", ")),
                        entityClassMetaData.getIdField().getName()));
    }
}
