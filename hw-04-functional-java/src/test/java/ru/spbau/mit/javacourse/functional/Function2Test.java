package ru.spbau.mit.javacourse.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Function2Test {

    private Function2<Integer, Integer, Integer> plus = (x, y) -> x + y;
    private Function1<Integer, Integer> square = x -> x * x;
    private Function2<String, String, String> concat = (s1, s2) -> s1 + s2;

    @Nested
    @DisplayName("Test only Function2 calls")
    class Function2CallTest {

        @Test
        void testIntIntToIntFunction() {
            Function2<Integer, Integer, Integer> plus = (x, y) -> x + y;
            for (int x = -10; x <= 10; x++) {
                for (int y = -10; y <= 10; y++) {
                    assertThat(plus.apply(x, y), equalTo(x + y));
                }
            }
        }
        @Test
        void testStringStringToStringFunction() {
            Function2<String, String, String> concat = (x, y) -> x + y;
            assertThat(concat.apply("", "aba"), equalTo("aba"));
            assertThat(concat.apply("aba", ""), equalTo("aba"));
            assertThat(concat.apply("aba", "caba"), equalTo("abacaba"));
        }
    }

    @Test
    void testComposeUnaryAndBinaryFunctionsWithSameTypes() {
        Function2<Integer, Integer, Integer> squareOfSum = (x, y) -> (x + y) * (x + y);
        assertThat(plus.compose(square).apply(3, 4), equalTo(squareOfSum.apply(3, 4)));
    }

    @Test
    @DisplayName("Wrapping function can have more common argument type than wrapped function's return type")
    void testConsumerSuperIsWorking() {
        Function1<Number, Double> toDouble = Number::doubleValue;
        Function2<Integer, Integer, Double> sumOfDoubles = plus.compose(toDouble);
        assertThat(sumOfDoubles.apply(3, 5), equalTo(8.0));
    }

    @Test
    @DisplayName("Wrapping function can have more specific return type than return type of composition")
    void testProducerExtendsIsWorking() {
        Function1<Object, String> toString = Object::toString;
        Function2<Object, Object, Object> tru = (x, y) -> x;
        Function2<Object, Object, Object> castTruToString = tru.compose(toString);
        assertThat(castTruToString.apply(1, "abacaba"), equalTo("1"));
    }

    @Test
    void testBindFirstArgument() {
        Function1<String, String> concatRight = concat.bind1("aba");
        assertThat(concatRight.apply("caba"), equalTo("abacaba"));
    }

    @Test
    void testBindSecondArgument() {
        Function1<String, String> concatLeft = concat.bind2("aba");
        assertThat(concatLeft.apply("caba"), equalTo("cabaaba"));
    }

    @Test
    void testCurrying() {
        Function1<String, Function1<String, String>> concatFactory = concat.curry();
        assertThat(concatFactory.apply("aba").apply("caba"), equalTo("abacaba"));
        assertThat(concatFactory.apply("caba").apply("aba"), equalTo("cabaaba"));
    }

    @Test
    void testWrappingFunctionCannotBeNull() {
        assertThrows(NullPointerException.class, () -> plus.compose(null).apply(1, 2));
    }
}
