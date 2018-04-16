package ru.spbau.mit.javacourse.functional;

@FunctionalInterface
public interface Predicate<A> extends Function1<A, Boolean> {

    default Predicate<A> or(Predicate<? super A> second) {
        return arg -> apply(arg) || second.apply(arg);
    }

    default Predicate<A> and(Predicate<? super A> second) {
        return arg -> apply(arg) && second.apply(arg);
    }

    default Predicate<A> not() {
        return arg -> !apply(arg);
    }

    Predicate<?> ALWAYS_TRUE = x -> true;

    Predicate<?> ALWAYS_FALSE = x -> false;
}
