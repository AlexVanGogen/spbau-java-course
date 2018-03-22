package ru.spbau.mit.javacourse.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {

    private LinkedList linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new LinkedList();
    }

    @Test
    void emptyListMustContainNothing() {
        assertAll (
                () -> assertFalse(linkedList.contains("")),
                () -> assertFalse(linkedList.contains("a")),
                () -> assertFalse(linkedList.contains("abacaba"))
        );
    }

    @Test
    void emptyListMustHaveZeroSize() {
        assertEquals(0, linkedList.size());
    }

    @Test
    void listWithOneElementMustContainNothingButThatElement() {
        assertNull(linkedList.put("aba", "caba"));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.contains("aba"));
        assertAll(
                () -> assertFalse(linkedList.contains("")),
                () -> assertFalse(linkedList.contains("caba"))
        );
    }

    @Test
    void putNodeToListIsTheSameAsPutPair() {
        assertNull(linkedList.put(linkedList.new Node("aba", "caba")));
        assertEquals(1, linkedList.size());
        assertTrue(linkedList.contains("aba"));
        assertAll(
                () -> assertFalse(linkedList.contains("")),
                () -> assertFalse(linkedList.contains("caba"))
        );
    }

    @Test
    void puttingPairWithExistingKeyMustNotHappen() {
        assertNull(linkedList.put("aba", "caba"));
        assertEquals("caba", linkedList.put("aba", "cadaba"));
        assertEquals("cadaba", linkedList.get("aba"));
    }

    @Test
    void successfulDeletingFromListWithOneElementMustReturnEmptyList() {
        assertNull(linkedList.put("aba", "caba"));
        assertEquals("caba", linkedList.remove("aba"));
        assertEquals(0, linkedList.size());
        assertFalse(linkedList.contains("aba"));
    }

    @Test
    void lastInsertedElementIsOnTheHeadPosition() {
        assertAll(
                () -> assertNull(linkedList.put("aba", "caba")),
                () -> assertNull(linkedList.put("bba", "cbba")),
                () -> assertNull(linkedList.put("cba", "ccba")),
                () -> assertNull(linkedList.put("dba", "cdba"))
        );
        assertEquals("dba", linkedList.getHead().getKey());
    }

    @Test
    void deletingByNonexistentKeyMustDoNothing() {
        linkedList.put("aba", "caba");
        linkedList.put("bba", "cbba");
        linkedList.put("cba", "ccba");
        linkedList.put("dba", "cdba");
        assertEquals(null, linkedList.remove("fba"));
        assertAll(
                () -> assertEquals("caba", linkedList.get("aba")),
                () -> assertEquals("cbba", linkedList.get("bba")),
                () -> assertEquals("ccba", linkedList.get("cba")),
                () -> assertEquals("cdba", linkedList.get("dba"))
        );
    }

    @Test
    void successfulDeletingFromListMustNotDestroyTheChain() {
        linkedList.put("aba", "caba");
        linkedList.put("bba", "cbba");
        linkedList.put("cba", "ccba");
        linkedList.put("dba", "cdba");
        linkedList.remove("bba");
        assertAll(
                () -> assertEquals("caba", linkedList.get("aba")),
                () -> assertNull(linkedList.get("bba")),
                () -> assertEquals("ccba", linkedList.get("cba")),
                () -> assertEquals("cdba", linkedList.get("dba"))
        );
    }
}
