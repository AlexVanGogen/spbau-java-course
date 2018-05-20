package ru.spbau.mit.javacourse.functional;

@FunctionalInterface
public interface Function1<A, R> {

    R apply(A argument);

    default <U> Function1<A, U> compose(Function1<? super R, ? extends U> g) {
        return x -> g.apply(apply(x));
    }
}
