package org.pofay.jcip_example.chapter1and2;

import java.util.Collection;

public interface SimpleCache<K, V> {
    V get(K key);

    void put(K key, V value);

    int size();

    boolean containsKey(K key);

    void clear();

    Collection<V> values();
}
