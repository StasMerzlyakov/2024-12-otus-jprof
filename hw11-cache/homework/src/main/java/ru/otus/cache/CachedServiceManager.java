package ru.otus.cache;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DBServiceManager;

public class CachedServiceManager implements DBServiceManager {

    private static final Logger log = LoggerFactory.getLogger(CachedServiceManager.class);

    private final DBServiceManager dbService;

    private final MyCache<Long, Manager> cache;

    public CachedServiceManager(DBServiceManager dbService) {
        this.dbService = dbService;
        this.cache = new MyCache<>();
    }

    @Override
    public Manager saveManager(Manager manager) {
        var saved = dbService.saveManager(manager);
        var key = saved.getNo();
        cache.put(key, saved);
        log.info("saved manager: {}", saved);
        return saved;
    }

    @Override
    public Optional<Manager> getManager(long id) {
        var fromCache = cache.get(id);
        if (fromCache != null) {
            log.info("manager restored from cache: {}", fromCache);
            return Optional.of(fromCache);
        }

        var fromDB = dbService.getManager(id);
        if (fromDB.isPresent()) {
            var fromDBMananager = fromDB.get();
            cache.put(id, fromDBMananager);
            log.info("manager restored from db: {}", fromDBMananager);
        } else {
            log.info("manager {} is not present", id);
        }

        return fromDB;
    }

    @Override
    public List<Manager> findAll() {
        return dbService.findAll();
    }
}
