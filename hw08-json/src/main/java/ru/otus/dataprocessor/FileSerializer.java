package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    private final JsonMapper mapper;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = new JsonMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var outputStream = new FileOutputStream(fileName)) {
            mapper.writeValue(outputStream, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
