package ru.spbau.mit.javacourse.trie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TrieTest {

    Trie trie;

    @BeforeEach
    void setUp() {
        trie = new TrieImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test method TrieImpl.add (and implicitly TrieImpl.contains)")
    void add() {
        assertAll(
                () -> assertTrue(trie.add("a"), "New word \"a\""),
                () -> assertTrue(trie.add("aba"), "New word \"aba\""),
                () -> assertTrue(trie.add("abacaba"), "New word \"abacaba\""),
                () -> assertTrue(trie.add("zbacaba"), "New word \"zbacaba\""),
                () -> assertTrue(trie.add("z"), "New word \"z\""),
                () -> assertTrue(trie.add("Z"), "New word \"Z\""),
                () -> assertFalse(trie.add("z"), "Word \"Z\" already exists"),
                () -> assertTrue(trie.add(""), "Empty word"),
                () -> assertTrue(trie.add("A"), "New word as \"A\" != \"a\""),
                () -> assertTrue(trie.add("Aba"), "\"Aba\" != \"aba\""),
                () -> assertTrue(trie.add("Abacaba"), "\"Abacaba\" != \"abacaba\""),
                () -> assertTrue(trie.add("ABACABA"), "\"ABACABA\" != \"abacaba\""),
                () -> assertFalse(trie.add(""), "Empty word already exists"),
                () -> assertFalse(trie.add("Aba"), "Word \"Aba\" already exists")
        );
    }

    @Test
    @DisplayName("Test method TrieImpl.contains")
    void contains() {
        trie.add("a");
        trie.add("aba");
        trie.add("abacaba");
        trie.add("zbacaba");
        trie.add("z");
        trie.add("Z");
        trie.add("");
        trie.add("A");
        trie.add("Aba");
        trie.add("Abacaba");
        trie.add("ABACABA");
        assertAll(
                "Trie must contain all words inserted above",
                () -> assertTrue(trie.contains("a")),
                () -> assertTrue(trie.contains("aba")),
                () -> assertTrue(trie.contains("abacaba")),
                () -> assertTrue(trie.contains("zbacaba")),
                () -> assertTrue(trie.contains("z")),
                () -> assertTrue(trie.contains("Z")),
                () -> assertTrue(trie.contains("")),
                () -> assertTrue(trie.contains("A")),
                () -> assertTrue(trie.contains("Aba")),
                () -> assertTrue(trie.contains("Abacaba")),
                () -> assertTrue(trie.contains("ABACABA"))
        );
        assertAll(
                "Trie must not contain all words that were not inserted above",
                () -> assertFalse(trie.contains("b")),
                () -> assertFalse(trie.contains("bcb")),
                () -> assertFalse(trie.contains("bcbdbcb")),
                () -> assertFalse(trie.contains("acbdbcb")),
                () -> assertFalse(trie.contains("y")),
                () -> assertFalse(trie.contains("Y")),
                () -> assertFalse(trie.contains("yay")),
                () -> assertFalse(trie.contains("B")),
                () -> assertFalse(trie.contains("Bcb")),
                () -> assertFalse(trie.contains("Bcbdbcb")),
                () -> assertFalse(trie.contains("BCBDBCB"))
        );
    }

    @Test
    void remove() {
    }

    @Test
    void size() {
    }

    @Test
    void howManyStartsWithPrefix() {
    }
}
