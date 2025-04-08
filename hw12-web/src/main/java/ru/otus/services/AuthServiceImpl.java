package ru.otus.services;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import ru.otus.helpers.FileSystemHelper;

/**
 * Простенькая реализация сервиса авторизации на основе списка user:pass в файле realmFilePath
 */
@SuppressWarnings({"squid:S112"})
public class AuthServiceImpl implements AuthService {

    private final Map<String, String> authMap;

    public AuthServiceImpl(String realmFilePath) throws Exception {
        authMap = loadUserMap(realmFilePath);
    }

    private Map<String, String> loadUserMap(String realmFilePath) throws Exception {
        Map<String, String> realmMap = new HashMap<>();

        var realResourcePath = FileSystemHelper.localFileNameOrResourceNameToFullPath(realmFilePath);

        var uri = URI.create(realResourcePath);

        try (Stream<String> linesStream = Files.lines(Path.of(uri))) {
            linesStream.forEach(line -> {
                String[] pair = line.split(":");
                if (pair.length != 2) {
                    throw new RuntimeException("can't parse real file " + realmFilePath);
                }
                realmMap.put(pair[0].trim(), pair[1].trim());
            });
        }

        return realmMap;
    }

    @Override
    public boolean authenticate(String login, String password) {
        String path = authMap.get(login);
        return path != null && path.equals(password);
    }
}
