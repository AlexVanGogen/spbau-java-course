package ru.spbau.mit.javacourse.dictionary;

public class DictionaryImpl implements Dictionary {

    private final int MAX_KEYS_IN_CHAIN = 10;
    private final double REHASH_MULTIPLIER = 2.0;
    private double fillCoefficient = 0.0;
    private int tableSize = 1007;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public String remove(String key) {
        return null;
    }

    @Override
    public void clear() {

    }

    private int hashOf(String key, int hashParameter) {
        int multiplier = 1;
        int result = 0;
        for (int i = key.length() - 1; i >= 0; i--) {
            result += multiplier * (int)key.charAt(i);
            multiplier *= hashParameter;
        }
        return result % tableSize;
    }
}
