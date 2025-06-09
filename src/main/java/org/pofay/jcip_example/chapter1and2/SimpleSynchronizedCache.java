package org.pofay.jcip_example.chapter1and2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleSynchronizedCache<K, V> implements SimpleCache<K, V> {
    private final Map<K,V> cache = new HashMap<>();

    @Override
    public synchronized V get(K key) {
        return cache.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public synchronized int size() {
        return cache.size();
    }

    @Override
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public synchronized void clear() {
        cache.clear();
    }

    @Override
    public synchronized Collection<V> values() {
        return cache.values();
    }

}
