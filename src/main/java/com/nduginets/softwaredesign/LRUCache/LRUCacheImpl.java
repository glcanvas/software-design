package com.nduginets.softwaredesign.LRUCache;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LRUCacheImpl<K, V> implements LRUCache<K, V> {

    private final Map<K, ListNode<K, V>> keyMap;
    private final int maxSize;
    private ListNode<K, V> headNode = new ListNode<>();
    private ListNode<K, V> tailNode = new ListNode<>();

    public LRUCacheImpl(int size) {
        keyMap = new HashMap<>(size);
        headNode.next = null;
        headNode.previous = tailNode;

        tailNode.next = headNode;
        tailNode.previous = null;
        this.maxSize = size;
    }

    @Override
    public void push(@NotNull K key, @NotNull V value) {
        assert key != null;
        assert value != null;
        int oldSize = size();

        if (keyMap.containsKey(key)) {
            pop(key);
        }

        if (keyMap.size() == maxSize) {
            ListNode<K, V> removedNode = tailNode.next;
            removedNode.next.previous = tailNode;
            tailNode.next = removedNode.next;
            keyMap.remove(removedNode.key);
        }

        ListNode<K, V> newNode = new ListNode<>(key, value);
        keyMap.put(key, newNode);

        ListNode<K, V> youngestNode = headNode.previous;
        newNode.next = headNode;
        newNode.previous = youngestNode;
        youngestNode.next = newNode;
        headNode.previous = newNode;

        assert oldSize <= size();
        assert key.equals(headNode.previous.key);
        assert value.equals(headNode.previous.value);
    }

    @Override
    public V take(K key) {
        assert key != null;
        int oldSize = size();
        if (!keyMap.containsKey(key)) {
            return null;
        }
        ListNode<K, V> updatedNode = keyMap.get(key);
        ListNode<K, V> previousNode = updatedNode.previous;
        ListNode<K, V> nextNode = updatedNode.next;

        nextNode.previous = previousNode;
        previousNode.next = nextNode;

        ListNode<K, V> headPrevOld = headNode.previous;
        headNode.previous = updatedNode;
        updatedNode.previous = headPrevOld;
        headPrevOld.next = updatedNode;

        assert oldSize == size();
        assert key.equals(headNode.previous.key);
        assert updatedNode.value.equals(headNode.previous.value);
        return updatedNode.value;
    }

    @Override
    public V pop(K key) {
        assert key != null;
        int oldSize = size();
        if (!keyMap.containsKey(key)) {
            return null;
        }
        ListNode<K, V> updatedNode = keyMap.get(key);
        ListNode<K, V> previousNode = updatedNode.previous;
        ListNode<K, V> nextNode = updatedNode.next;
        nextNode.previous = previousNode;
        previousNode.next = nextNode;
        keyMap.remove(key);

        assert !key.equals(headNode.previous.key);
        assert oldSize >= size();
        return updatedNode.value;
    }

    @Override
    public int size() {
        assert keyMap.size() <= maxSize;
        return keyMap.size();
    }


    private static final class ListNode<K, V> {
        private final K key;
        private final V value;
        private final boolean isBorder;

        private ListNode<K, V> previous = null;
        private ListNode<K, V> next = null;

        private ListNode(K key, V value) {
            isBorder = false;
            this.key = key;
            this.value = value;
        }

        private ListNode() {
            isBorder = true;
            this.key = null;
            this.value = null;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode<?, ?> listNode = (ListNode<?, ?>) o;
            return Objects.equals(key, listNode.key) &&
                    Objects.equals(value, listNode.value) &&
                    Objects.equals(previous, listNode.previous) &&
                    Objects.equals(next, listNode.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value, previous, next);
        }

        @Override
        public String toString() {
            if (key == null) {
                return "ListNode{NULL}";
            }
            return "ListNode{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
