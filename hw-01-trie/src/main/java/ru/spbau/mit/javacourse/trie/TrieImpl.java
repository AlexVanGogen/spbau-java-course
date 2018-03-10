package ru.spbau.mit.javacourse.trie;

public class TrieImpl implements Trie {

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

        private final Node[] next;
        private boolean isTerminal;
        private int numberOfWordsAfter;
    }
}
