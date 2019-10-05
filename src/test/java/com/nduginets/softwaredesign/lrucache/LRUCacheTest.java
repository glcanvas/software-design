package com.nduginets.softwaredesign.lrucache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

class LRUCacheTest {

    private LRUCache<Integer, String> cache;
    private int cacheSize;

    @BeforeEach
    void init() {
        cacheSize = 5;
        cache = new LRUCacheImpl<>(cacheSize);
    }

    @Test
    void simpleAddTest() {
        for (int i = 0; i < cacheSize * 2; i++) {
            cache.push(i, Integer.toString(i));
            Assertions.assertEquals(Integer.toString(i), cache.take(i));
        }
        Assertions.assertEquals(cacheSize, cache.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 15})
    void replaceKeyTest(int bound) {
        for (int i = 0; i < bound; i++) {
            cache.push(i, Integer.toString(i));
        }
        for (int i = 0; i < bound; i++) {
            cache.push(i, Integer.toString(i + bound));
            Assertions.assertEquals(Integer.toString(i + bound), cache.take(i));
        }
        Assertions.assertEquals(Math.min(bound, cacheSize), cache.size());
    }

    @Test
    void removeKeys() {
        for (int i = 0; i < cacheSize; i++) {
            cache.push(i, Integer.toString(i));
        }
        Assertions.assertEquals(cacheSize, cache.size());

        for (int i = 0; i < cacheSize; i++) {
            String value = cache.pop(i);
            Assertions.assertEquals(Integer.toString(i), value);
            Assertions.assertEquals(cacheSize - i - 1, cache.size());
        }

    }

    @Test
    void simpleRemoveItemTest() {
        for (int i = 0; i < cacheSize + 1; i++) {
            cache.push(i, Integer.toString(i));
        }
        String result = cache.pop(1);
        Assertions.assertEquals("1", result);
        Assertions.assertEquals(4, cache.size());

        result = cache.pop(1);
        Assertions.assertEquals(4, cache.size());
        Assertions.assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("nullArguments")
    void nullableArgumentsTest(Integer key, String value) {
        Assertions.assertThrows(AssertionError.class, () -> cache.push(key, value));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void wrongArgumentConstructorTest(int size) {
        Assertions.assertThrows(AssertionError.class, () -> new LRUCacheImpl<String, String>(size));
    }

    private static Stream<Arguments> nullArguments() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(1, null),
                Arguments.of(null, "1")
        );
    }
}
