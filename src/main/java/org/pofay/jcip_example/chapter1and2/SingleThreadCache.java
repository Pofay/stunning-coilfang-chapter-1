package org.pofay.jcip_example.chapter1and2;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleThreadCache<K, V> implements SimpleCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public Collection<V> values() {
        return cache.values();
    }
}
