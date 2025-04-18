package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final JsonMapper mapper;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = new JsonMapper();
    }

    @Override
    public List<Measurement> load() {
        try (var inputStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            return mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
