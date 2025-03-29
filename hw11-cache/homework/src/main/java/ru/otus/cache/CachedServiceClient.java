package ru.otus.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

public class CachedServiceClient implements DBServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CachedServiceClient.class);

    private final DBServiceClient dbService;

    private final Map<String, Client> cache;

    public CachedServiceClient(DBServiceClient dbService) {
        this.dbService = dbService;
        this.cache = new WeakHashMap<>();
    }

    public Integer size() {
        return this.cache.size();
    }

    @Override
    public Client saveClient(Client client) {
        var saved = dbService.saveClient(client);
        cache.put("key" + saved.getId(), saved);
        log.info("saved client: {}", saved);
        return saved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cacheKey = "key" + id;
        var fromCache = cache.get(cacheKey);
        if (fromCache != null) {
            log.info("client restored from cache: {}", fromCache);
            return Optional.of(fromCache);
        }

        var fromDB = dbService.getClient(id);
        if (fromDB.isPresent()) {
            cache.put(cacheKey, fromDB.get());
        }

        return fromDB;
    }

    @Override
    public List<Client> findAll() {
        return dbService.findAll();
    }
}
