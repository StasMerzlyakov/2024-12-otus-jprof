package ru.otus.jdbc.mapper;

public class DataTemplateJdbcException extends RuntimeException {
    public DataTemplateJdbcException(String message) {
        super(message);
    }

    public DataTemplateJdbcException(Throwable throwable) {
        super(throwable);
    }
}
