package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ResultSetMapper<T> implements Function<ResultSet, T> {

    private final EntityClassMetaData<T> entityClassMetaData;

    public ResultSetMapper(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public T apply(ResultSet resultSet) {
        List<Object> constructorArgs = new LinkedList<>();
        try {
            if (!resultSet.next()) {
                return null;
            }

            for (var id = 1; id <= entityClassMetaData.getAllFields().size(); id++) {
                constructorArgs.add(resultSet.getObject(id));
            }
        } catch (Exception e) {
            throw new ResultSetMapperException("can't extract object from result set: " + e.getMessage());
        }

        try {
            return entityClassMetaData.getConstructor().newInstance(constructorArgs.toArray());
        } catch (Exception e) {
            throw new ResultSetMapperException("can't create object: " + e.getMessage());
        }
    }
}
