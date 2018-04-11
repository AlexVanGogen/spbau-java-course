package ru.spbau.mit.javacourse.serializabletrie;

import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializedTrieTest {

    private Trie trie;
    private List<String> generalWordsForTesting = List.of(
            "a", "aba", "abacaba", "zbacaba",
            "z", "Z", "", "A", "Abacaba", "Aba", "ABACABA"
    );
    private List<String> singleWordForTesting = List.of("imalone");

    @BeforeEach
    void setUp() {
        trie = new TrieImpl();
    }

    @Test
    void testNullDataStreams() {
        assertThrows(NullPointerException.class, () -> ((StreamSerializable)trie).serialize(null));
        assertThrows(NullPointerException.class, () -> ((StreamSerializable)trie).deserialize(null));
    }

    @Test
    void testSerializeAndDeserializeTheSameTrie() throws IOException {
        addElementsToTrie(trie, generalWordsForTesting);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((StreamSerializable)trie).serialize(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ((StreamSerializable)trie).deserialize(in);

        assertTrieConsistsOfElementsInList(trie, generalWordsForTesting);

        in.close();
        out.close();
    }

    @Test
    void testSubstituteTrieWithLesserTrie() throws IOException {
        addElementsToTrie(trie, generalWordsForTesting);

        Trie trieWithOneWord = new TrieImpl();
        addElementsToTrie(trieWithOneWord, singleWordForTesting);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((StreamSerializable)trieWithOneWord).serialize(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ((StreamSerializable)trie).deserialize(in);

        assertTrieConsistsOfElementsInList(trie, singleWordForTesting);

        in.close();
        out.close();
    }

    @Test
    void testSubstituteTrieWithGreaterTrie() throws IOException {
        Trie trieWithManyWords = new TrieImpl();
        addElementsToTrie(trieWithManyWords, generalWordsForTesting);
        addElementsToTrie(trie, singleWordForTesting);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((StreamSerializable)trieWithManyWords).serialize(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ((StreamSerializable)trie).deserialize(in);

        assertTrieConsistsOfElementsInList(trie, generalWordsForTesting);

        in.close();
        out.close();
    }

    @Test
    void testDeserializationFailureWillNotOverwriteOriginalTrie() throws IOException {
        addElementsToTrie(trie, generalWordsForTesting);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((StreamSerializable)trie).serialize(out);

        byte[] originalTrieData = out.toByteArray();
        byte[] damagedTrieData = Arrays.copyOfRange(originalTrieData, 0, originalTrieData.length / 5);
        ByteArrayInputStream in = new ByteArrayInputStream(damagedTrieData);

        assertThrows(SerializedObjectFormatException.class, () -> ((StreamSerializable)trie).deserialize(in));
        assertTrieConsistsOfElementsInList(trie, generalWordsForTesting);

        in.close();
        out.close();
    }

    private void addElementsToTrie(Trie trie, List<String> words) {
        words.forEach(trie::add);
    }

    private void assertTrieConsistsOfElementsInList(Trie trie, List<String> words) {
        assertEquals(words.size(), trie.size());
        words.forEach((word) -> assertTrue(trie.contains(word)));
    }
}
