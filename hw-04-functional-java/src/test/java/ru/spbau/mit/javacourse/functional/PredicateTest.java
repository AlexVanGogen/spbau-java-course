package ru.spbau.mit.javacourse.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicateTest {

    @Nested
    @DisplayName("Test only Predicate calls")
    class PredicateCallTest {
        @Test
        void testIntPredicate() {
            Predicate<Number> isZero = x -> x.intValue() == 0;
            for (int i = -10; i <= 10; i++) {
                assertThat(isZero.apply(i), equalTo(i == 0));
            }
            for (double i = -10.0; i <= 10.0; i += 1.0) {
                assertThat(isZero.apply(i), equalTo(i == 0));
            }
        }

        @Test
        void testStringPredicate() {
            Predicate<String> isInUpperCase = s -> !s.isEmpty() && s.toUpperCase().equals(s);
            assertFalse(isInUpperCase.apply("CApS"));
            assertTrue(isInUpperCase.apply("CAPS"));
        }
    }

    @Test
    void testOrForPredicatesWithSameArgumentTypes() {
        Predicate<Integer> lessThan5 = x -> x < 5;
        Predicate<Integer> greaterThan42 = x -> x > 42;
        Predicate<Integer> greaterThan42OrLessThan5 = lessThan5.or(greaterThan42);

        assertTrue(greaterThan42OrLessThan5.apply(43));
        assertTrue(greaterThan42OrLessThan5.apply(4));
        assertFalse(greaterThan42OrLessThan5.apply(20));
    }

    @Test
    void testAndForPredicatesWithSameArgumentTypes() {
        Predicate<Integer> greaterThan5 = x -> x > 5;
        Predicate<Integer> lessThan42 = x -> x < 42;
        Predicate<Integer> greaterThan5AndLessThan42 = greaterThan5.and(lessThan42);

        assertFalse(greaterThan5AndLessThan42.apply(43));
        assertFalse(greaterThan5AndLessThan42.apply(4));
        assertTrue(greaterThan5AndLessThan42.apply(20));
    }

    @Test
    void testNot() {
        Predicate<Integer> greaterThan5 = x -> x > 5;
        Predicate<Integer> notLessThan5 = greaterThan5.not();

        assertTrue(greaterThan5.apply(4) != notLessThan5.apply(4));
        assertTrue(greaterThan5.apply(20) != notLessThan5.apply(20));
    }

    @Test
    void testOrIsLazy() {
        Predicate<Object> functionWithSideEffect = x -> {
            throw new IllegalStateException();
        };
        Predicate<?> trueOrSomethingElse = Predicate.ALWAYS_TRUE.or(functionWithSideEffect);
        Predicate<?> falseOrSomethingElse = Predicate.ALWAYS_FALSE.or(functionWithSideEffect);
        assertTrue(trueOrSomethingElse.apply(null));
        assertThrows(IllegalStateException.class, () -> falseOrSomethingElse.apply(null));
    }

    @Test
    void testAndIsLazy() {
        Predicate<Object> functionWithSideEffect = x -> {
            throw new IllegalStateException();
        };
        Predicate<?> trueAndSomethingElse = Predicate.ALWAYS_TRUE.and(functionWithSideEffect);
        Predicate<?> falseAndSomethingElse = Predicate.ALWAYS_FALSE.and(functionWithSideEffect);
        assertThrows(IllegalStateException.class, () -> trueAndSomethingElse.apply(null));
        assertFalse(falseAndSomethingElse.apply(null));
    }

    @Test
    @DisplayName("Second predicate can have more common type than first predicate's type")
    void testConsumerSuperIsWorking() {
        Predicate<Integer> lessThan5 = x -> x < 5;
        Predicate<Number> isPositive = x -> x.intValue() >= 0;
        Predicate<Integer> isPositiveAndLessThan5 = lessThan5.and(isPositive);

        assertFalse(isPositiveAndLessThan5.apply(-1));
        assertTrue(isPositiveAndLessThan5.apply(1));
        assertFalse(isPositiveAndLessThan5.apply(5));
    }
}
