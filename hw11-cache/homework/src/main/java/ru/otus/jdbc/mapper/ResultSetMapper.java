package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("java:S3011")
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

            T t = entityClassMetaData.getConstructor().newInstance();

            var fields = entityClassMetaData.getAllFields();
            for (var id = 1; id <= fields.size(); id++) {

                var fieldValue = resultSet.getObject(id);
                var field = fields.get(id - 1);
                field.setAccessible(true);
                field.set(t, fieldValue);
            }
        } catch (Exception e) {
            throw new ResultSetMapperException(e);
        }

        try {
            return entityClassMetaData.getConstructor().newInstance(constructorArgs.toArray());
        } catch (Exception e) {
            throw new ResultSetMapperException(e);
        }
    }
}
