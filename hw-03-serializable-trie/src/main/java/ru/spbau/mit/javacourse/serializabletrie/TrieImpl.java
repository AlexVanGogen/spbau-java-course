package ru.spbau.mit.javacourse.serializabletrie;

import java.io.*;

public class TrieImpl implements Trie, StreamSerializable {

    public static final int ALPHABET_SIZE = 52;
    private final Node root;

    public TrieImpl() {
        root = new Node();
    }

    /**
     * Complexity: O(|element|)
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element
     */
    @Override
    public boolean add(String element) throws IllegalArgumentException {
        if (contains(element)) {
            return false;
        }
        Node currentNode = root;
        root.increaseNumberOfWordsAfter();
        for (final char ch: element.toCharArray()) {
            currentNode = currentNode.insertByCharIfAbsent(ch);
            currentNode.increaseNumberOfWordsAfter();
        }
        currentNode.setTerminal();
        return true;
    }

    /**
     * Complexity: O(|element|)
     */
    @Override
    public boolean contains(String element) throws IllegalArgumentException {
        Node currentNode = root;
        for (final char ch: element.toCharArray()) {
            if (currentNode == null) {
                return false;
            }
            currentNode = currentNode.goByChar(ch);
        }
        return currentNode != null && currentNode.isTerminal();
    }

    /**
     * Complexity: O(|element|)
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) throws IllegalArgumentException {
        if (!contains(element)) {
            return false;
        }
        Node currentNode = root;
        root.decreaseNumberOfWordsAfter();
        for (final char ch: element.toCharArray()) {
            if (currentNode.goByChar(ch).getNumberOfWordsAfter() == 1) {
                currentNode.removeBranchByChar(ch);
                return true;
            }
            currentNode = currentNode.goByChar(ch);
            currentNode.decreaseNumberOfWordsAfter();
        }
        currentNode.resetTerminal();
        return true;
    }

    /**
     * Complexity: O(1)
     */
    @Override
    public int size() {
        return root.getNumberOfWordsAfter();
    }

    /**
     * Complexity: O(|prefix|)
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) throws IllegalArgumentException {
        Node currentNode = root;
        for (final char ch: prefix.toCharArray()) {
            currentNode = currentNode.goByChar(ch);
            if (currentNode == null)
                return 0;
        }
        return currentNode.getNumberOfWordsAfter();
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        root.deserialize(in);
    }

    private final class Node {

        public Node() {
            next = new Node[ALPHABET_SIZE];
            isTerminal = false;
            numberOfWordsAfter = 0;
        }

        public Node goByChar(final char ch) throws IllegalArgumentException {
            final int mappedValue = mapCharToInt(ch);
            return next[mappedValue];
        }

        public Node insertByCharIfAbsent(final char ch) throws IllegalArgumentException {
            final int mappedValue = mapCharToInt(ch);
            if (next[mappedValue] == null) {
                next[mappedValue] = new Node();
            }
            return next[mappedValue];
        }

        public void removeBranchByChar(final char ch) throws IllegalArgumentException {
            final int mappedValue = mapCharToInt(ch);
            next[mappedValue] = null;
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public void setTerminal() {
            isTerminal = true;
        }

        public void resetTerminal() {
            isTerminal = false;
        }

        public int getNumberOfWordsAfter() {
            return numberOfWordsAfter;
        }

        public void increaseNumberOfWordsAfter() {
            numberOfWordsAfter++;
        }

        public void decreaseNumberOfWordsAfter() {
            numberOfWordsAfter--;
        }

        private int mapCharToInt(final char ch) throws IllegalArgumentException {
            if (ch >= 'a' && ch <= 'z') {
                return ch - 'a';
            } else if (ch >= 'A' && ch <= 'Z') {
                return ch - 'A' + (ALPHABET_SIZE >> 1);
            }
            throw new IllegalArgumentException("Character must be an alphabetic value");
        }

        private char mapIntToChar(final int number) throws IllegalArgumentException {
            if (number >= 0 && number < (ALPHABET_SIZE >> 1)) {
                return (char) (number + 'a');
            } else if (number >= (ALPHABET_SIZE >> 1) && number < ALPHABET_SIZE) {
                return (char) (number - (ALPHABET_SIZE >> 1) + 'A');
            }
            throw new IllegalArgumentException(String.format("Number must be in range [0, %d)", ALPHABET_SIZE));
        }

        public void serialize(OutputStream out) throws IOException {
            DataOutputStream dataWriter = new DataOutputStream(out);

            dataWriter.writeInt(numberOfWordsAfter);
            dataWriter.writeBoolean(isTerminal);
            for (int i = 0; i < next.length; i++) {
                dataWriter.writeBoolean(next[i] != null);
                if (next[i] != null) {
                    dataWriter.writeChar(mapIntToChar(i));
                    next[i].serialize(out);
                }
            }
        }

        public void deserialize(InputStream in) throws IOException {
            DataInputStream dataReader = new DataInputStream(in);
            deserializeSubtrie(dataReader);
            if (dataReader.available() > 0) {
                throw new SerializedObjectFormatException("Input stream has extra data");
            }
        }

        /**
         * @param dataReader stream with serialized object
         * @return number of terminal nodes inside current subtree
         * @throws IOException in I/O error occurs
         */
        private int deserializeSubtrie(DataInputStream dataReader) throws IOException {
            try {
                int numberOfTerminals = 0;
                Node deserializedTrieRoot = new Node();
                deserializedTrieRoot.numberOfWordsAfter = dataReader.readInt();
                deserializedTrieRoot.isTerminal = dataReader.readBoolean();
                if (deserializedTrieRoot.isTerminal) {
                    numberOfTerminals += 1;
                }
                for (int nextNodeIndex = 0; nextNodeIndex < deserializedTrieRoot.next.length; nextNodeIndex++) {
                    boolean isNextNodeNotNull = dataReader.readBoolean();
                    if (isNextNodeNotNull) {
                        char charToNextNode = dataReader.readChar();
                        if (!Character.isAlphabetic(charToNextNode) || mapCharToInt(charToNextNode) != nextNodeIndex) {
                            throw new SerializedObjectFormatException(String.format("%c: invalid character", charToNextNode));
                        }
                        deserializedTrieRoot.next[nextNodeIndex] = new Node();
                        numberOfTerminals += deserializedTrieRoot.next[nextNodeIndex].deserializeSubtrie(dataReader);
                    } else {
                        deserializedTrieRoot.next[nextNodeIndex] = null;
                    }
                }

                if (deserializedTrieRoot.numberOfWordsAfter != numberOfTerminals) {
                    throw new SerializedObjectFormatException("Actual number of words with some prefix differs from expected");
                }
                numberOfWordsAfter = deserializedTrieRoot.numberOfWordsAfter;
                isTerminal = deserializedTrieRoot.isTerminal;
                System.arraycopy(deserializedTrieRoot.next, 0, next, 0, next.length);
                return numberOfTerminals;
            } catch (EOFException e) {
                throw new SerializedObjectFormatException(e.getMessage(), e.getCause());
            }
        }

        private final Node[] next;
        private boolean isTerminal;
        private int numberOfWordsAfter;
    }
}
