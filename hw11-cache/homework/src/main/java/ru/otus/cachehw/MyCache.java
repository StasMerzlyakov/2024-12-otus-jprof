package ru.otus.cachehw;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<String, V> cache = new WeakHashMap<>();

    private final List<HwListener<K, V>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void put(K key, V value) {
        String cacheKey = createKey(key);
        cache.put(cacheKey, value);
        event(key, value, "put");
    }

    @Override
    public void remove(K key) {
        String cacheKey = createKey(key);
        cache.remove(cacheKey);
        event(key, null, "remove");
    }

    @Override
    public V get(K key) {
        String cacheKey = createKey(key);
        V value = cache.get(cacheKey);
        event(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void event(K key, V value, String action) {
        for (int i = 0; i < listeners.size(); ++i) {
            try {
                listeners.get(i).notify(key, value, action);
            } catch (Exception ex) {
                // логирование исключения
                logger.error("event error", ex);
            }
        }
    }

    private String createKey(K key) {
        return "key_" + key;
    }
}
