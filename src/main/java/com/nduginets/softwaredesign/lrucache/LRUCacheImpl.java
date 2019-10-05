package com.nduginets.softwaredesign.lrucache;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LRUCacheImpl<K, V> implements LRUCache<K, V> {

    private final Map<K, Node<K, V>> keyMap;
    private final int maxCacheSize;
    private Node<K, V> headNode = new Node<>();
    private Node<K, V> tailNode = new Node<>();

    public LRUCacheImpl(int size) {
        assert size > 0;
        keyMap = new HashMap<>(size);
        headNode.previous = tailNode;
        tailNode.next = headNode;
        this.maxCacheSize = size;
    }

    @Override
    public void push(@NotNull K key, @NotNull V value) {
        assert key != null;
        assert value != null;
        int oldSize = size();

        pop(key);

        if (keyMap.size() == maxCacheSize) {
            Node<K, V> removedNode = tailNode.next;
            removedNode.next.previous = tailNode;
            tailNode.next = removedNode.next;
            keyMap.remove(removedNode.key);
        }

        Node<K, V> newNode = new Node<>(key, value);
        keyMap.put(key, newNode);
        moveToHead(newNode);

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

        Node<K, V> updatedNode = keyMap.get(key);
        mergeNeighbours(updatedNode);
        moveToHead(updatedNode);

        assert oldSize == size();
        assert key.equals(headNode.previous.key);
        assert updatedNode.value != null;
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

        Node<K, V> updatedNode = keyMap.get(key);
        mergeNeighbours(updatedNode);
        keyMap.remove(key);

        assert !key.equals(headNode.previous.key);
        assert oldSize >= size();
        return updatedNode.value;
    }

    @Override
    public int size() {
        assert keyMap.size() <= maxCacheSize;
        return keyMap.size();
    }

    private void moveToHead(Node<K, V> movedNode) {
        Node<K, V> previousHeadNode = headNode.previous;
        headNode.previous = movedNode;
        movedNode.previous = previousHeadNode;
        movedNode.next = headNode;
        previousHeadNode.next = movedNode;
    }

    private void mergeNeighbours(Node<K, V> currentNode) {
        Node<K, V> previousNode = currentNode.previous;
        Node<K, V> nextNode = currentNode.next;
        nextNode.previous = previousNode;
        previousNode.next = nextNode;
    }

    private static final class Node<K, V> {
        private final K key;
        private final V value;

        private Node<K, V> previous = null;
        private Node<K, V> next = null;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private Node() {
            this.key = null;
            this.value = null;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> Node = (Node<?, ?>) o;
            return Objects.equals(key, Node.key) &&
                    Objects.equals(value, Node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value, previous, next);
        }

        @Override
        public String toString() {
            if (key == null) {
                return "Node{NULL}";
            }
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
