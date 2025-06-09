package org.pofay.jcip_example.chapter1and2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SynchronizedWithObjectCache<K, V> implements SimpleCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final Object lock = new Object();

    @Override
    public V get(K key) {
        synchronized (lock) {
            return cache.get(key);
        }
    }

    @Override
    public void put(K key, V value) {
        synchronized (lock) {
            cache.put(key, value);
        }
    }

    @Override
    public int size() {
        synchronized (lock) {
            return cache.size();
        }
    }

    @Override
    public boolean containsKey(K key) {
        synchronized (lock) {
            return cache.containsKey(key);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            cache.clear();
        }
    }

    @Override
    public Collection<V> values() {
        synchronized (lock) {
            return cache.values();
        }
    }
}
