package ru.spbau.mit.javacourse.serializabletrie;

import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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

    /*
     * Invalid chars are either prohibited or not corresponded to current trie edge
     */
    @ParameterizedTest
    @ValueSource(chars = {'@', 'X'})
    void testInconsistentCharacterMakesDeserializationFail(char invalidChar) throws IOException {
        final int alphabetSize = 52;

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(o);
        out.writeInt(1);
        out.writeBoolean(false);
        out.writeBoolean(true);
        out.writeChar(invalidChar);
        out.writeInt(1);
        out.writeBoolean(true);
        for (int i = 0; i < alphabetSize; i++) {
            out.writeBoolean(false);
        }
        for (int i = 1; i < alphabetSize; i++) {
            out.writeBoolean(false);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(o.toByteArray());
        assertThrows(
                SerializedObjectFormatException.class,
                () -> ((StreamSerializable)trie).deserialize(in)
        );

        in.close();
        o.close();
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
