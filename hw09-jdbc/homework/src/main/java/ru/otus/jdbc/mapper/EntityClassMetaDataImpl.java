package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import ru.otus.jdbc.api.Id;

@SuppressWarnings("java:S3011")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> allArgumentsConstructor;
    private Field idField;
    private List<Field> allFields = new LinkedList<>();
    private List<Field> fieldsWithoutId = new LinkedList<>();

    public EntityClassMetaDataImpl(Class<T> clazz) {

        name = clazz.getSimpleName().toLowerCase();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                if (idField != null) {
                    throw new EntityClassMetaDataException("too more field with @Id annotation");
                }
                idField = field;
                allFields.add(idField);
            } else {
                fieldsWithoutId.add(field);
                allFields.add(field);
            }
        }

        try {
            var classArray = allFields.stream().map(Field::getType).toList().toArray(new Class[] {});
            allArgumentsConstructor = clazz.getConstructor(classArray);
        } catch (NoSuchMethodException e) {
            throw new EntityClassMetaDataException("can't find all arguments constructor");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return allArgumentsConstructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
