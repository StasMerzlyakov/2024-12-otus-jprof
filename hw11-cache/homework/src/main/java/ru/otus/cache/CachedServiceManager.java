package ru.otus.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DBServiceManager;

public class CachedServiceManager implements DBServiceManager {

    private static final Logger log = LoggerFactory.getLogger(CachedServiceManager.class);

    private final DBServiceManager dbService;

    private final Map<String, Manager> cache;

    public CachedServiceManager(DBServiceManager dbService) {
        this.dbService = dbService;
        this.cache = new WeakHashMap<>();
    }

    public Integer size() {
        return this.cache.size();
    }

    @Override
    public Manager saveManager(Manager manager) {
        var saved = dbService.saveManager(manager);
        var key = saved.getNo();
        cache.put("key" + key, saved);
        log.info("saved manager: {}", saved);
        return saved;
    }

    @Override
    public Optional<Manager> getManager(long id) {
        var cacheKey = "key" + id;
        var fromCache = cache.get(cacheKey);
        if (fromCache != null) {
            log.info("manager restored from cache: {}", fromCache);
            return Optional.of(fromCache);
        }

        var fromDB = dbService.getManager(id);
        if (fromDB.isPresent()) {
            cache.put(cacheKey, fromDB.get());
        }

        return fromDB;
    }

    @Override
    public List<Manager> findAll() {
        return dbService.findAll();
    }
}
