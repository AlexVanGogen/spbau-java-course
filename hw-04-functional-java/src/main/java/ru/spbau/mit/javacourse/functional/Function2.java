package ru.spbau.mit.javacourse.functional;

@FunctionalInterface
public interface Function2<A1, A2, R> {

    R apply(A1 argument1, A2 argument2);

    default <U> Function2<A1, A2, U> compose(Function1<? super R, ? extends U> g) {
        return (x, y) -> g.apply(apply(x, y));
    }

    default Function1<A2, R> bind1(A1 argument1) {
        return y -> apply(argument1, y);
    }

    default Function1<A1, R> bind2(A2 argument2) {
        return x -> apply(x, argument2);
    }

    default Function1<A1, Function1<A2, R>> curry() {
        return x -> y -> apply(x, y);
    }
}
