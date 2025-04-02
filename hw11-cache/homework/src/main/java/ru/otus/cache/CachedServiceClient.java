package ru.otus.cache;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

public class CachedServiceClient implements DBServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CachedServiceClient.class);

    private final DBServiceClient dbService;

    private final HwCache<Long, Client> cache;

    public CachedServiceClient(DBServiceClient dbService, HwCache<Long, Client> cache) {
        this.dbService = dbService;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        var saved = dbService.saveClient(client);
        cache.put(saved.getId(), client);
        log.info("saved client: {}", saved);
        return saved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var fromCache = cache.get(id);
        if (fromCache != null) {
            log.info("client restored from cache: {}", fromCache);
            return Optional.of(fromCache);
        }

        var fromDB = dbService.getClient(id);
        if (fromDB.isPresent()) {
            var fromDBClient = fromDB.get();
            cache.put(id, fromDBClient);
            log.info("client restored from db: {}", fromDBClient);
        } else {
            log.info("client {} is not present", id);
        }

        return fromDB;
    }

    @Override
    public List<Client> findAll() {
        List<Client> all = dbService.findAll();
        all.forEach(client -> cache.put(client.getId(), client));
        return all;
    }
}
