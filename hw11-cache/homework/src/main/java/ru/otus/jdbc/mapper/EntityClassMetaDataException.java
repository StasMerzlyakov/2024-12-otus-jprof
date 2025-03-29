package ru.otus.jdbc.mapper;

public class EntityClassMetaDataException extends RuntimeException {
    public EntityClassMetaDataException(String message) {
        super(message);
    }

    public EntityClassMetaDataException(Throwable throwable) {
        super(throwable);
    }
}
