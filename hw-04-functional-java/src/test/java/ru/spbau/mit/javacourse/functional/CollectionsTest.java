package ru.spbau.mit.javacourse.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CollectionsTest {

    private List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private List<String> strings = Arrays.asList("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");

    @Nested
    @DisplayName("Test Collections.map method")
    class MapTest {

        @Test
        void testMapIntToInt() {
            Function1<Integer, Integer> square = x -> x * x;
            List<Integer> squaredNumbers = Collections.map(square, numbers);
            assertThat(squaredNumbers.size(), equalTo(numbers.size()));
            for (int i = 0; i < squaredNumbers.size(); i++) {
                assertThat(squaredNumbers.get(i), equalTo(numbers.get(i) * numbers.get(i)));
            }
        }

        @Test
        void testMapStringToInt() {
            Function1<String, Integer> allLengths = String::length;
            List<Integer> lengthsOfStrings = Collections.map(allLengths, strings);
            assertThat(lengthsOfStrings.size(), equalTo(strings.size()));
            for (int i = 0; i < lengthsOfStrings.size(); i++) {
                assertThat(lengthsOfStrings.get(i), equalTo(strings.get(i).length()));
            }
        }

        @Test
        @DisplayName("Mapping function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Function1<Object, String> toString = Object::toString;
            List<String> stringifiedNumbers = Collections.map(toString, numbers);
            assertThat(stringifiedNumbers.size(), equalTo(numbers.size()));
            for (int i = 0; i < stringifiedNumbers.size(); i++) {
                assertThat(stringifiedNumbers.get(i), equalTo(numbers.get(i).toString()));
            }
        }
    }

    @Nested
    @DisplayName("Test Collections.map method")
    class FilterTest {

        @Test
        void testFilterIntegerCollection() {
            Predicate<Integer> isEven = x -> x % 2 == 0;
            List<Integer> evenNumbers = Collections.filter(isEven, numbers);
            assertThat(evenNumbers, equalTo(Arrays.asList(2, 4, 6, 8, 10)));
        }

        @Test
        void testFilterStringCollection() {
            Predicate<String> hasLength5 = s -> s.length() == 5;
            List<String> wordsWithLength5 = Collections.filter(hasLength5, strings);
            assertThat(wordsWithLength5, equalTo(Arrays.asList("quick", "brown", "jumps")));
        }

        @Test
        @DisplayName("Filtering function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Predicate<Object> notNull = Objects::nonNull;
            List<Integer> notNullObjects = Collections.filter(notNull, Arrays.asList(1, null, 2, 3, null));
            assertThat(notNullObjects, equalTo(Arrays.asList(1, 2, 3)));
        }
    }

    @Nested
    @DisplayName("Test Collections.takeWhile method")
    class TakeWhileTest {

        @Test
        void takeFromIntegerCollection() {
            Predicate<Integer> lessThan5 = x -> x < 5;
            List<Integer> prefixWithAllNumbersLessThan5 = Collections.takeWhile(lessThan5, numbers);
            assertThat(prefixWithAllNumbersLessThan5, equalTo(Arrays.asList(1, 2, 3, 4)));
        }

        @Test
        void takeFromStringCollection() {
            Predicate<String> isCapitalized = s -> s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
            List<String> prefixWithAllCapitalizedWords = Collections.takeWhile(isCapitalized, strings);
            assertThat(prefixWithAllCapitalizedWords, equalTo(java.util.Collections.singletonList("The")));
        }

        @Test
        @DisplayName("Taking function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Predicate<Object> notNull = Objects::nonNull;
            List<Integer> notNullObjects = Collections.takeWhile(notNull, Arrays.asList(1, null, 2, 3, null));
            assertThat(notNullObjects, equalTo(java.util.Collections.singletonList(1)));
        }
    }

    @Nested
    @DisplayName("Test Collections.takeUnless method")
    class TakeUnlessTest {

        @Test
        void takeFromIntegerCollection() {
            Predicate<Integer> greaterThan5 = x -> x > 5;
            List<Integer> prefixWithAllNumbersNotGreaterThan5 = Collections.takeUnless(greaterThan5, numbers);
            assertThat(prefixWithAllNumbersNotGreaterThan5, equalTo(Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void takeFromStringCollection() {
            Predicate<String> hasLength5 = s -> s.length() == 5;
            List<String> prefixWithAllWordsWithLengthNotEqual5 = Collections.takeUnless(hasLength5, strings);
            assertThat(prefixWithAllWordsWithLengthNotEqual5, equalTo(java.util.Collections.singletonList("The")));
        }

        @Test
        @DisplayName("Taking function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Predicate<Object> isNull = Objects::isNull;
            List<Integer> notNullObjects = Collections.takeUnless(isNull, Arrays.asList(1, null, 2, 3, null));
            assertThat(notNullObjects, equalTo(java.util.Collections.singletonList(1)));
        }
    }

    @Nested
    @DisplayName("Test Collections.foldl method")
    class FoldlTest {

        @Test
        void testIntegerCollection() {
            Function2<Integer, Integer, Integer> minus = (x, y) -> x - y;
            Integer foldResult = Collections.foldl(minus, 15, numbers);
            assertThat(foldResult, equalTo(-40));
        }

        @Test
        void testStringCollection() {
            Function2<String, String, String> concat = (x, y) -> x + y;
            String foldResult = Collections.foldl(concat, "+", strings);
            assertThat(foldResult, equalTo("+Thequickbrownfoxjumpsoverthelazydog"));
        }

        @Test
        @DisplayName("Folding function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Function2<Object, Object, String> concat = (o1, o2) -> o1.toString() + o2.toString();
            String foldResult = (String)Collections.foldl(concat, "0", numbers);
            assertThat(foldResult, equalTo("012345678910"));
        }
    }

    @Nested
    @DisplayName("Test Collections.foldr method")
    class FoldrTest {

        @Test
        void testIntegerCollection() {
            Function2<Integer, Integer, Integer> minus = (x, y) -> x - y;
            Integer foldResult = Collections.foldr(minus, 15, numbers);
            assertThat(foldResult, equalTo(10));
        }

        @Test
        void testStringCollection() {
            Function2<String, String, String> concat = (x, y) -> x + y;
            String foldResult = Collections.foldr(concat, "+", strings);
            assertThat(foldResult, equalTo("Thequickbrownfoxjumpsoverthelazydog+"));
        }

        @Test
        @DisplayName("Folding function can have more common argument type than common type of elements in list")
        void testConsumerSuperIsWorking() {
            Function2<Object, Object, String> concat = (o1, o2) -> o1.toString() + o2.toString();
            String foldResult = (String)Collections.foldr(concat, "0", numbers);
            assertThat(foldResult, equalTo("123456789100"));
        }
    }
}
