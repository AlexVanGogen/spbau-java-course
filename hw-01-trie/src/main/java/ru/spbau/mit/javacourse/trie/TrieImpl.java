package ru.spbau.mit.javacourse.trie;

public class TrieImpl implements Trie {

    public static final int ALPHABET_SIZE = 52;
    private Node root;

    public TrieImpl() {
        root = new Node();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        Node currentNode = root;
        root.increaseWordsAfter();
        for (char ch: element.toCharArray()) {
            currentNode = currentNode.insertByCharIfAbsent(ch);
            currentNode.increaseWordsAfter();
        }
        currentNode.setTerminal();
        return true;
    }

    @Override
    public boolean contains(String element) {
        Node currentNode = root;
        for (char ch: element.toCharArray()) {
            if (currentNode == null) {
                return false;
            }
            currentNode = currentNode.goByChar(ch);
        }
        return isTerminal(currentNode);
    }

    @Override
    public boolean remove(String element) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return 0;
    }

    private boolean isTerminal(final Node node) {
        return node != null && node.isTerminal();
    }

    private final class Node {

        public Node() {
            next = new Node[ALPHABET_SIZE];
            isTerminal = false;
            wordsAfter = 0;
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

        @Deprecated
        public Node insertByChar(final char ch) throws IllegalArgumentException, UnsupportedOperationException {
            final int mappedValue = mapCharToInt(ch);
            if (next[mappedValue] == null) {
                throw new UnsupportedOperationException("Insertion to the present node is disallowed");
            } else {
                next[mappedValue] = new Node();
                return next[mappedValue];
            }
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

        public int getWordsAfter() {
            return wordsAfter;
        }

        public void increaseWordsAfter() {
            wordsAfter++;
        }

        public void decreaseWordsAfter() {
            wordsAfter--;
        }

        private int mapCharToInt(final char ch) throws IllegalArgumentException {
            if (ch >= 'a' && ch <= 'z') {
                return ch - 'a';
            } else if (ch >= 'A' && ch <= 'Z') {
                return ch - 'A' + (ALPHABET_SIZE >> 1);
            }
            throw new IllegalArgumentException("Character must be an alphabetic value");
        }

        private final Node[] next;
        private boolean isTerminal;
        private int wordsAfter;
    }
}
