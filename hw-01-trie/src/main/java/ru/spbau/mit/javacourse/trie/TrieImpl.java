package ru.spbau.mit.javacourse.trie;

public class TrieImpl implements Trie {

    public static final int ALPHABET_SIZE = 52;

    @Override
    public boolean add(String element) {
        return false;
    }

    @Override
    public boolean contains(String element) {
        return false;
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


    private final class Node {

        public Node() {
            next = new Node[ALPHABET_SIZE];
            isTerminal = false;
            wordsAfter = 1;
        }

        public Node findByChar(final char ch) throws IllegalArgumentException {
            final int mappedValue = mapCharToInt(ch);
            return next[mappedValue];
        }

        public Node insertByChar(final char ch) throws IllegalArgumentException, UnsupportedOperationException {
            final int mappedValue = mapCharToInt(ch);
            if (next[mappedValue].isPresent()) {
                throw new UnsupportedOperationException("Insertion to the present node is disallowed");
            } else {
                next[mappedValue] = new Node();
                return next[mappedValue];
            }
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public boolean isPresent() {
            return wordsAfter > 0;
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
