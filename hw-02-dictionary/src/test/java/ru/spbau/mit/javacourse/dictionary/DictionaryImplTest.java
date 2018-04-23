package ru.spbau.mit.javacourse.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryImplTest {

    private DictionaryImpl dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new DictionaryImpl();
    }

    @Test
    void emptyDictionaryMustContainNothing() {
        assertAll(
                () -> assertFalse(dictionary.contains("")),
                () -> assertFalse(dictionary.contains("a")),
                () -> assertFalse(dictionary.contains("aba"))
        );
    }

    @Test
    void emptyDictionaryMustHaveZeroSize() {
        assertEquals(0, dictionary.size());
    }

    @Test
    void dictionaryWithOneElementMustContainNothingButThatElement() {
        assertNull(dictionary.put("aba", "caba"));
        assertEquals(1, dictionary.size());
        assertTrue(dictionary.contains("aba"));
        assertAll(
                () -> assertFalse(dictionary.contains("")),
                () -> assertFalse(dictionary.contains("caba"))
        );
    }

    @Test
    void puttingPairWithExistingKeyMustRewriteOldValue() {
        assertNull(dictionary.put("aba", "caba"));
        assertEquals("caba", dictionary.put("aba", "cadaba"));
        assertEquals("cadaba", dictionary.get("aba"));
    }

    @Test
    void successfulDeletingFromDictionaryWithOneElementMustReturnEmptyDictionary() {
        assertNull(dictionary.put("aba", "caba"));
        assertEquals("caba", dictionary.remove("aba"));
        assertEquals(0, dictionary.size());
        assertFalse(dictionary.contains("aba"));
    }

    @Test
    void deletingByNonexistentKeyMustDoNothing() {
        dictionary.put("aba", "caba");
        dictionary.put("bba", "cbba");
        dictionary.put("cba", "ccba");
        dictionary.put("dba", "cdba");
        assertNull(dictionary.remove("gba"));
        assertAll(
                () -> assertEquals("caba", dictionary.get("aba")),
                () -> assertEquals("cbba", dictionary.get("bba")),
                () -> assertEquals("ccba", dictionary.get("cba")),
                () -> assertEquals("cdba", dictionary.get("dba"))
        );
    }

    @Test
    void dictionaryAfterClearingMustContainNothing() {
        dictionary.put("aba", "caba");
        dictionary.put("bba", "cbba");
        dictionary.put("cba", "ccba");
        dictionary.put("dba", "cdba");
        dictionary.clear();
        assertEquals(0, dictionary.size());
    }

    @Test
    @DisplayName("Checking if rehashing works correctly")
    void addAndRemoveManyStrings() {
        for (int i = 1; i <= 100000; i++) {
            dictionary.put(String.valueOf(i), String.valueOf(i - 1));
        }
        for (int i = 1; i < 50000; i++) {
            assertNotNull(dictionary.remove(String.valueOf(i)));
        }
        for (int i = 50001; i <= 100000; i++) {
            assertNotNull(dictionary.remove(String.valueOf(i)));
        }
        assertEquals(1, dictionary.size());
        assertEquals(String.valueOf(49999), dictionary.get(String.valueOf(50000)));
    }

    @Test
    void dictionaryCanStoreVeryLongStrings() {
        String longString = Stream.generate(() -> "zyz").limit(100000).collect(Collectors.joining());
        dictionary.put(longString, "wow");
    }

    @Test
    void dictionaryDoesNotContainNullKey() {
        assertNull(dictionary.put(null, "a"));
        assertFalse(dictionary.contains(null));
    }
    
    @Test
    void putPairWithNullKeyDoesNothing() {
        assertNull(dictionary.put(null, "a"));
        assertEquals(0, dictionary.size());
    }

    @Test
    void gettingByNullKeyDoesNothing() {
        assertNull(dictionary.put(null, "a"));
        assertNull(dictionary.get(null));
    }

    @Test
    void removingByNullKeyDoesNothing() {
        assertNull(dictionary.remove(null));
    }
}
