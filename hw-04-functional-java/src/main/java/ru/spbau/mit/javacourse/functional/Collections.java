package ru.spbau.mit.javacourse.functional;

import java.util.LinkedList;

public class Collections {

    public static <T, U> LinkedList<U> map(Function1<? super T, U> f, Iterable<? extends T> list) {
        LinkedList<U> mappedList = new LinkedList<>();
        list.forEach(element -> mappedList.add(f.apply(element)));
        return mappedList;
    }

    public static <T> LinkedList<T> filter(Predicate<? super T> by, Iterable<? extends T> list) {
        LinkedList<T> filteredList = new LinkedList<>();
        list.forEach(element -> {
            if (by.apply(element)) {
                filteredList.add(element);
            }
        });
        return filteredList;
    }

    public static <T> LinkedList<T> takeWhile(Predicate<? super T> by, Iterable<? extends T> list) {
        LinkedList<T> firstNAppropriateElements = new LinkedList<>();
        for (T element: list) {
            if (by.apply(element)) {
                firstNAppropriateElements.add(element);
            } else {
                break;
            }
        }
        return firstNAppropriateElements;
    }

    public static <T> LinkedList<T> takeUnless(Predicate<? super T> by, Iterable<? extends T> list) {
        return takeWhile(by.not(), list);
    }

    public static <T> T foldl(Function2<? super T, ? super T, ? extends T> f, T initialValue, Iterable<? extends T> list) {
        T currentValue = initialValue;
        for (T element : list) {
            currentValue = f.apply(currentValue, element);
        }
        return currentValue;
    }

    public static <T> T foldr(Function2<? super T, ? super T, ? extends T> f, T initialValue, Iterable<? extends T> list) {
        LinkedList<T> reversedList = new LinkedList<>();
        list.forEach(reversedList::addFirst);

        T currentValue = initialValue;
        for (T element : reversedList) {
            currentValue = f.apply(element, currentValue);
        }
        return currentValue;
    }
}