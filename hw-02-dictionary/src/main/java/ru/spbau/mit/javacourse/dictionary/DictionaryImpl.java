package ru.spbau.mit.javacourse.dictionary;

public class DictionaryImpl implements Dictionary {

    private final int MAX_KEYS_IN_CHAIN = 10;
    private final int DEFAULT_POLYNOMIAL_HASH_PARAMETER = 2;
    private final int MINIMAL_TABLE_SIZE = 1001;
    private final double MIN_FILL_THRESHOLD = 0.1;
    private final double MAX_FILL_THRESHOLD = 10.0;
    private final double REHASH_MULTIPLIER = 2.0;

    private double fillCoefficient;
    private int tableSize;

    private LinkedList[] table;
    private int numberOfKeys;

    public DictionaryImpl() {
        fillCoefficient = 0.0;
        tableSize = MINIMAL_TABLE_SIZE;
        table = new LinkedList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList();
        }
        numberOfKeys = 0;
    }

    @Override
    public int size() {
        return numberOfKeys;
    }

    @Override
    public boolean contains(final String key) {
        return key != null && table[hashOf(key)].contains(key);
    }

    @Override
    public String get(final String key) {
        if (key == null) {
            return null;
        }
        return table[hashOf(key)].get(key);
    }

    @Override
    public String put(final String key, final String value) {
        if (key == null) {
            return null;
        }
        final int hash = hashOf(key);
        String oldValue = table[hash].put(key, value);
        if (oldValue == null) {
            numberOfKeys++;
            recalculateFillCoefficient(true);
            if (table[hash].size() > MAX_KEYS_IN_CHAIN) {
                rehash(true);
            }
        }
        return oldValue;
    }

    @Override
    public String remove(final String key) {
        if (key == null) {
            return null;
        }
        final int hash = hashOf(key);
        String deletedValue = table[hash].remove(key);
        if (deletedValue != null) {
            numberOfKeys--;
            recalculateFillCoefficient(false);
            rehash(false);
        }
        return deletedValue;
    }

    @Override
    public void clear() {
        for (LinkedList list: table) {
            list.clear();
        }
        numberOfKeys = 0;
    }

    private void rehash(final boolean increaseCapacity) {
        while (fillCoefficient < MIN_FILL_THRESHOLD && tableSize > MINIMAL_TABLE_SIZE
                || fillCoefficient > MAX_FILL_THRESHOLD) {
            tableSize = (int) (increaseCapacity ? tableSize * REHASH_MULTIPLIER : tableSize / REHASH_MULTIPLIER);
            final LinkedList[] newTable = new LinkedList[tableSize];
            for (int i = 0; i < tableSize; i++) {
                newTable[i] = new LinkedList();
            }
            for (LinkedList list : table) {
                LinkedList.Node currentNode = list.getHead();
                while (currentNode != null) {
                    newTable[hashOf(currentNode.getKey())].put(currentNode);
                    currentNode = currentNode.getNext();
                }
            }
            table = newTable;
            fillCoefficient = (double)(numberOfKeys)/tableSize;
        }
    }

    private int hashOf(final String key) {
        return hashOf(key, DEFAULT_POLYNOMIAL_HASH_PARAMETER);
    }

    private int hashOf(final String key, final int hashParameter) {
        int multiplier = 1;
        int result = 0;
        for (int i = key.length() - 1; i >= 0; i--) {
            result += multiplier * (int)key.charAt(i);
            multiplier *= hashParameter;
        }
        return result % tableSize;
    }

    private void recalculateFillCoefficient(final boolean onInsertion) {
        if (onInsertion) {
            fillCoefficient += 1. / tableSize;
        } else {
            fillCoefficient -= 1. / tableSize;
        }
    }
}
