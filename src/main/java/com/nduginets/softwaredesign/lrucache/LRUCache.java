package com.nduginets.softwaredesign.lrucache;

import com.sun.istack.internal.NotNull;

public interface LRUCache<K, V> {

    /**
     * pre: k != null && value != null
     * <p>
     * post:
     * firstElement(lrucache) == (k, v) && @old size() <= size()
     */
    void push(@NotNull K key, @NotNull V value);

    /**
     * pre: key != null
     * <p>
     * post:
     * result == any && old size() == size() && firstElement(lrucache) == (k, v)
     */
    V take(K key);

    /**
     * pre: key != null
     * <p>
     * post:
     * result == any && @old size() >= size() && firstElement(lrucache) != (k, v)
     */
    V pop(K key);

    /**
     * pre: true
     * <p>
     * post: size() <= max cache size
     */
    int size();
}
