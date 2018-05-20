package ru.spbau.mit.javacourse.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Function1Test {

    private Function1<Integer, Integer> triple = x -> x + x + x;
    private Function1<Integer, Integer> square = x -> x * x;

    @Nested
    @DisplayName("Test only Function1 calls")
    class Function1CallTest {

        @Test
        void testIntToIntFunction() {
            Function1<Integer, Integer> identity = x -> x;
            for (int i = -10; i <= 10; i++) {
                assertThat(identity.apply(i), equalTo(i));
            }
        }

        @Test
        void testNumberToDoubleFunction() {
            Function1<Number, Double> toDouble = Number::doubleValue;
            assertThat(toDouble.apply(5), equalTo(5.0));
        }
    }

    @Test
    void testSelfWrapping() {
        for (int i = -10; i <= 10; i++) {
            assertThat(triple.compose(triple).apply(i), equalTo(i * 9));
            assertThat(square.compose(square).apply(i), equalTo(i * i * i * i));
        }
    }

    @Test
    void testComposeTwoUnaryFunctionsWithSameTypes() {
        Function1<Integer, Integer> tripleAndSquare = x -> (x + x + x) * (x + x + x);
        Function1<Integer, Integer> squareAndTriple = x -> (x * x) + (x * x) + (x * x);
        for (int i = -10; i <= 10; i++) {
            assertThat(triple.compose(square).apply(i), equalTo(tripleAndSquare.apply(i)));
            assertThat(square.compose(triple).apply(i), equalTo(squareAndTriple.apply(i)));
        }
    }

    @Test
    @DisplayName("Wrapping function can have more common argument type than wrapped function's return type")
    void testConsumerSuperIsWorking() {
        Function1<Number, Double> toDouble = Number::doubleValue;
        Function1<Integer, Double> squareAndCastToDouble = square.compose(toDouble);
        assertThat(squareAndCastToDouble.apply(5), equalTo(25.0));
    }

    @Test
    @DisplayName("Wrapping function can have more specific return type than return type of composition")
    void testProducerExtendsIsWorking() {
        Function1<Object, String> toString = Object::toString;
        Function1<Object, Object> identity = x -> x;
        Function1<Object, Object> castIdentityToString = identity.compose(toString);
        assertThat(castIdentityToString.apply(1), equalTo("1"));
    }

    @Test
    void testWrappingFunctionCannotBeNull() {
        assertThrows(NullPointerException.class, () -> square.compose(null).apply(1));
    }
}
