package com.identity.platform.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This cache is used to store Authorization code. It will be used in the Code to
 * Token call
 * @param <K>
 * @param <V>
 */
@Component
public class AuthCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final long expirationTime = 15 * 60 * 1000; // 15 minutes in milliseconds

    /**
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis() + expirationTime);
        cache.put(key, entry);
        scheduleRemoval(key, entry);
    }

    /**
     *
     * @param key
     * @return
     */
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !isExpired(entry)) {
            return entry.getValue();
        }
        return null;
    }

    /**
     *
     * @param entry
     * @return
     */
    private boolean isExpired(CacheEntry<?> entry) {
        return System.currentTimeMillis() >= entry.getExpirationTime();
    }

    /**
     *
     * @param key
     * @param entry
     */
    private void scheduleRemoval(K key, CacheEntry<?> entry) {
        executor.schedule(() -> {
            cache.remove(key);
        }, entry.getExpirationTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     *
     * @param key
     */
    public void remove(K key) {
            cache.remove(key);
    }

    /**
     *
     * @param <V>
     */
    private static class CacheEntry<V> {
        private final V value;
        private final long expirationTime;

        public CacheEntry(V value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public V getValue() {
            return value;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}