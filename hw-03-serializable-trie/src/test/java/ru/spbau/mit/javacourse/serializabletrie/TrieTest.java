package ru.spbau.mit.javacourse.serializabletrie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


public class TrieTest {

    Trie trie;

    @BeforeEach
    void setUp() {
        trie = new TrieImpl();
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
                () -> assertTrue(trie.add("Abacaba"), "\"Abacaba\" != \"abacaba\""),
                () -> assertTrue(trie.add("Aba"), "\"Aba\" != \"aba\""),
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
        trie.add("Abacaba");
        trie.add("Aba");
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
                () -> assertTrue(trie.contains("Abacaba")),
                () -> assertTrue(trie.contains("Aba")),
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
                () -> assertFalse(trie.contains("Bcbdbcb")),
                () -> assertFalse(trie.contains("Bcb")),
                () -> assertFalse(trie.contains("BCBDBCB"))
        );
    }

    @Test
    @DisplayName("Test method TrieImpl.remove")
    void remove() {
        trie.add("a");
        trie.add("aba");
        trie.add("abacaba");
        trie.add("zbacaba");
        trie.add("z");
        trie.add("Z");
        trie.add("");
        trie.add("A");
        trie.add("Abacaba");
        trie.add("Aba");
        trie.add("ABACABA");
        assertAll(
                "Trie must successfully remove all words inserted above",
                () -> assertTrue(trie.remove("a")),
                () -> assertTrue(trie.remove("aba")),
                () -> assertTrue(trie.remove("abacaba")),
                () -> assertTrue(trie.remove("zbacaba")),
                () -> assertTrue(trie.remove("z")),
                () -> assertTrue(trie.remove("Z")),
                () -> assertTrue(trie.remove("")),
                () -> assertTrue(trie.remove("A")),
                () -> assertTrue(trie.remove("Abacaba")),
                () -> assertTrue(trie.remove("Aba")),
                () -> assertTrue(trie.remove("ABACABA"))
        );
        assertAll(
                "Trie must not contain deleted elements",
                () -> assertFalse(trie.contains("a")),
                () -> assertFalse(trie.contains("aba")),
                () -> assertFalse(trie.contains("abacaba")),
                () -> assertFalse(trie.contains("zbacaba")),
                () -> assertFalse(trie.contains("z")),
                () -> assertFalse(trie.contains("Z")),
                () -> assertFalse(trie.contains("")),
                () -> assertFalse(trie.contains("A")),
                () -> assertFalse(trie.contains("Abacaba")),
                () -> assertFalse(trie.contains("Aba")),
                () -> assertFalse(trie.contains("ABACABA"))
        );
        assertAll(
                "\"remove\" must return false when deleting not existing elements",
                () -> assertFalse(trie.remove("a")),
                () -> assertFalse(trie.remove("aba")),
                () -> assertFalse(trie.remove("abacaba")),
                () -> assertFalse(trie.remove("zbacaba")),
                () -> assertFalse(trie.remove("z")),
                () -> assertFalse(trie.remove("Z")),
                () -> assertFalse(trie.remove("")),
                () -> assertFalse(trie.remove("A")),
                () -> assertFalse(trie.remove("Abacaba")),
                () -> assertFalse(trie.remove("Aba")),
                () -> assertFalse(trie.remove("ABACABA"))
        );
    }

    @Test
    @DisplayName("Test method TrieImpl.size")
    void size() {
        assertEquals(0, trie.size(), "Trie must be empty initially");
        assertAll(
                "Trie size must increase by 1 when new word is adding",
                () -> trie.add("a"),
                () -> assertEquals(1, trie.size()),

                () -> trie.add("aba"),
                () -> assertEquals(2, trie.size()),

                () -> trie.add("abacaba"),
                () -> assertEquals(3, trie.size()),

                () -> trie.add("zbacaba"),
                () -> assertEquals(4, trie.size()),

                () -> trie.add("z"),
                () -> assertEquals(5, trie.size()),

                () -> trie.add("Z"),
                () -> assertEquals(6, trie.size()),

                () -> trie.add(""),
                () -> assertEquals(7, trie.size()),

                () -> trie.add("A"),
                () -> assertEquals(8, trie.size()),

                () -> trie.add("Abacaba"),
                () -> assertEquals(9, trie.size()),

                () -> trie.add("Aba"),
                () -> assertEquals(10, trie.size()),

                () -> trie.add("ABACABA"),
                () -> assertEquals(11, trie.size())
        );

        trie.add("a");
        trie.add("aba");
        trie.add("abacaba");
        trie.add("zbacaba");
        trie.add("z");
        trie.add("Z");
        trie.add("");
        trie.add("A");
        trie.add("Abacaba");
        trie.add("Aba");
        trie.add("ABACABA");
        assertEquals(11, trie.size(), "Adding words that are already exist must not affect the trie size");

        assertAll(
                "Trie size must decrease by 1 when word that already exists is deleting",
                () -> trie.remove("a"),
                () -> assertEquals(10, trie.size()),

                () -> trie.remove("aba"),
                () -> assertEquals(9, trie.size()),

                () -> trie.remove("abacaba"),
                () -> assertEquals(8, trie.size()),

                () -> trie.remove("zbacaba"),
                () -> assertEquals(7, trie.size()),

                () -> trie.remove("z"),
                () -> assertEquals(6, trie.size()),

                () -> trie.remove("Z"),
                () -> assertEquals(5, trie.size()),

                () -> trie.remove(""),
                () -> assertEquals(4, trie.size()),

                () -> trie.remove("A"),
                () -> assertEquals(3, trie.size()),

                () -> trie.remove("Abacaba"),
                () -> assertEquals(2, trie.size()),

                () -> trie.remove("Aba"),
                () -> assertEquals(1, trie.size()),

                () -> trie.remove("ABACABA"),
                () -> assertEquals(0, trie.size())
        );
    }

    @Test
    @DisplayName("Test method TrieImpl.howManyStartsWithPrefix")
    void howManyStartsWithPrefix() {
        assertEquals(0, trie.howManyStartsWithPrefix(""), "Trie must be empty initially");
        trie.add("a");
        trie.add("aba");
        trie.add("abacaba");
        trie.add("zbacaba");
        trie.add("z");
        trie.add("Z");
        trie.add("");
        trie.add("A");
        trie.add("Abacaba");
        trie.add("Aba");
        trie.add("ABACABA");
        assertAll(
                () -> assertEquals(11, trie.howManyStartsWithPrefix(""), "Any words starts with empty prefix"),
                () -> assertEquals(3, trie.howManyStartsWithPrefix("a")),
                () -> assertEquals(2, trie.howManyStartsWithPrefix("z")),
                () -> assertEquals(4, trie.howManyStartsWithPrefix("A")),
                () -> assertEquals(2, trie.howManyStartsWithPrefix("Aba")),
                () -> assertEquals(0, trie.howManyStartsWithPrefix("AbA"))
        );
        trie.remove("aba");
        trie.remove("A");
        assertAll(
                () -> assertEquals(9, trie.howManyStartsWithPrefix("")),
                () -> assertEquals(2, trie.howManyStartsWithPrefix("a")),
                () -> assertEquals(2, trie.howManyStartsWithPrefix("z")),
                () -> assertEquals(3, trie.howManyStartsWithPrefix("A")),
                () -> assertEquals(2, trie.howManyStartsWithPrefix("Aba")),
                () -> assertEquals(0, trie.howManyStartsWithPrefix("AbA"))
        );
        trie.add("AbAcAbA");
        assertAll(
                () -> assertEquals(10, trie.howManyStartsWithPrefix("")),
                () -> assertEquals(4, trie.howManyStartsWithPrefix("A")),
                () -> assertEquals(1, trie.howManyStartsWithPrefix("AbA"))
        );
    }

    @Test
    void testTrieOnLargeDictionary() {
        Logger log = Logger.getLogger("julLog");

        /* Test insertion and prefixes */
        try (FileReader fileReader = new FileReader("src/test/resources/ru.spbau.mit.javacourse.serializabletrie/dictionary.txt")) {
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String nextLine = reader.readLine();
                int addedWords = 0;

                while (nextLine != null) {
                    trie.add(nextLine);
                    addedWords++;
                    assertEquals(addedWords, trie.size());
                    nextLine = reader.readLine();
                }

                final int overallWords = addedWords;

                assertAll(
                        () -> assertEquals(overallWords, trie.howManyStartsWithPrefix(""), "Any word starts with empty prefix"),
                        () -> assertEquals(28, trie.howManyStartsWithPrefix("aa")),
                        () -> assertEquals(1178, trie.howManyStartsWithPrefix("ab")),
                        () -> assertEquals(4, trie.howManyStartsWithPrefix("android")),
                        () -> assertEquals(199, trie.howManyStartsWithPrefix("az")),
                        () -> assertEquals(7, trie.howManyStartsWithPrefix("bamboozl")),
                        () -> assertEquals(3718, trie.howManyStartsWithPrefix("be")),
                        () -> assertEquals(427, trie.howManyStartsWithPrefix("ben")),
                        () -> assertEquals(1, trie.howManyStartsWithPrefix("bz")),
                        () -> assertEquals(25, trie.howManyStartsWithPrefix("czar")),
                        () -> assertEquals(14, trie.howManyStartsWithPrefix("dare")),
                        () -> assertEquals(28, trie.howManyStartsWithPrefix("dash")),
                        () -> assertEquals(0, trie.howManyStartsWithPrefix("dqei")),
                        () -> assertEquals(2, trie.howManyStartsWithPrefix("gingerbread")),
                        () -> assertEquals(3, trie.howManyStartsWithPrefix("glucose")),
                        () -> assertEquals(2, trie.howManyStartsWithPrefix("ios")),
                        () -> assertEquals(8, trie.howManyStartsWithPrefix("java")),
                        () -> assertEquals(8, trie.howManyStartsWithPrefix("jingle")),
                        () -> assertEquals(0, trie.howManyStartsWithPrefix("kaggle")),
                        () -> assertEquals(9, trie.howManyStartsWithPrefix("lisp")),
                        () -> assertEquals(4, trie.howManyStartsWithPrefix("meow")),
                        () -> assertEquals(1, trie.howManyStartsWithPrefix("nostra")),
                        () -> assertEquals(1, trie.howManyStartsWithPrefix("nostradamus")),
                        () -> assertEquals(0, trie.howManyStartsWithPrefix("nostradamuscle")),
                        () -> assertEquals(11, trie.howManyStartsWithPrefix("nothing")),
                        () -> assertEquals(16, trie.howManyStartsWithPrefix("opinion")),
                        () -> assertEquals(28, trie.howManyStartsWithPrefix("pork")),
                        () -> assertEquals(19, trie.howManyStartsWithPrefix("python")),
                        () -> assertEquals(1740, trie.howManyStartsWithPrefix("qu")),
                        () -> assertEquals(1, trie.howManyStartsWithPrefix("renaissance")),
                        () -> assertEquals(17, trie.howManyStartsWithPrefix("slab")),
                        () -> assertEquals(28, trie.howManyStartsWithPrefix("trump")),
                        () -> assertEquals(2, trie.howManyStartsWithPrefix("ulyss")),
                        () -> assertEquals(2, trie.howManyStartsWithPrefix("vendetta")),
                        () -> assertEquals(6559, trie.howManyStartsWithPrefix("w")),
                        () -> assertEquals(4, trie.howManyStartsWithPrefix("xylyl")),
                        () -> assertEquals(0, trie.howManyStartsWithPrefix("ycombinator")),
                        () -> assertEquals(275, trie.howManyStartsWithPrefix("zoo"))
                );
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        /* Test deletion */
        try (FileReader fileReader = new FileReader("src/test/resources/ru.spbau.mit.javacourse.serializabletrie/dictionary.txt")) {
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String nextLine = reader.readLine();
                int addedWords = trie.size();

                while (nextLine != null) {
                    trie.remove(nextLine);
                    addedWords--;
                    assertEquals(addedWords, trie.size());
                    nextLine = reader.readLine();
                }
            }
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    @Test
    void testBadStrings() {
        assertThrows(IllegalArgumentException.class, () -> trie.add("<aba>"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("[aba]"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("(aba)"));
        assertThrows(IllegalArgumentException.class, () -> trie.add("аба"));
    }
}
