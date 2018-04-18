package ru.spbau.mit.javacourse.functional;

import java.util.LinkedList;

public class Collections {

    public static <T, U> LinkedList<U> map(Function1<? super T, U> f, Iterable<T> list) {
        LinkedList<U> mappedList = new LinkedList<>();
        list.forEach(element -> mappedList.add(f.apply(element)));
        return mappedList;
    }

    public static <T> LinkedList<T> filter(Predicate<? super T> by, Iterable<T> list) {
        LinkedList<T> filteredList = new LinkedList<>();
        list.forEach(element -> {
            if (by.apply(element)) {
                filteredList.add(element);
            }
        });
        return filteredList;
    }

    public static <T> LinkedList<T> takeWhile(Predicate<? super T> by, Iterable<T> list) {
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

    public static <T> LinkedList<T> takeUnless(Predicate<? super T> by, Iterable<T> list) {
        return takeWhile(by.not(), list);
    }

    public static <T, R> R foldl(Function2<? super R, ? super T, ? extends R> f, R initialValue, Iterable<T> list) {
        R currentValue = initialValue;
        for (T element : list) {
            currentValue = f.apply(currentValue, element);
        }
        return currentValue;
    }

    public static <T, R> R foldr(Function2<? super T, ? super R, ? extends R> f, R initialValue, Iterable<T> list) {
        LinkedList<T> reversedList = new LinkedList<>();
        list.forEach(reversedList::addFirst);

        R currentValue = initialValue;
        for (T element : reversedList) {
            currentValue = f.apply(element, currentValue);
        }
        return currentValue;
    }
}