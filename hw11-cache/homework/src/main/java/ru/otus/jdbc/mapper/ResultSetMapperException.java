package ru.otus.jdbc.mapper;

public class ResultSetMapperException extends RuntimeException {

    public ResultSetMapperException(String message) {
        super(message);
    }

    public ResultSetMapperException(Throwable throwable) {
        super(throwable);
    }
}
