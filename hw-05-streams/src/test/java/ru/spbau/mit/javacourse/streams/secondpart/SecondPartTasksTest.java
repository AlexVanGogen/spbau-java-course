package ru.spbau.mit.javacourse.streams.secondpart;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static ru.spbau.mit.javacourse.streams.secondpart.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {

        List<String> files = IntStream.rangeClosed('A', 'N')
                .mapToObj(charCode -> String.format("src/test/resources/quotes/latin_%c.txt", charCode))
                .collect(Collectors.toList());
        List<String> linesContainsAveMaria = findQuotes(files, "Ave maria");
        assertThat(linesContainsAveMaria, hasSize(1));
        assertThat(linesContainsAveMaria, everyItem(containsString("Ave maria")));

        List<String> linesContainsDeusVult = findQuotes(files, "Deus vult");
        assertThat(linesContainsDeusVult, hasSize(1));
        assertThat(linesContainsDeusVult, everyItem(containsString("Deus vult")));

        List<String> linesContainsMakeAmericaGreatAgain = findQuotes(files, "Make America great again!");
        assertThat(linesContainsMakeAmericaGreatAgain, hasSize(0));
    }

    @Test
    public void testPiDividedBy4() {
        assertThat(piDividedBy4(), closeTo(Math.PI / 4, Math.PI / 64));
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new HashMap<>();
        compositions.put("A", Arrays.asList("abacaba", "d", ""));
        compositions.put("B", Collections.emptyList());
        compositions.put("C", Collections.singletonList("ugrwwhghwruiohgshvuweogvior"));
        compositions.put("D", Collections.singletonList(""));
        assertThat(findPrinter(compositions), equalTo("C"));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> order1 = new HashMap<>();
        order1.put("Potatoes", 100);
        order1.put("Garlic", 2801);

        Map<String, Integer> order2 = new HashMap<>();
        order2.put("Tomatoes", 200);
        order2.put("Garlic", 199);

        Map<String, Integer> summary = new HashMap<>();
        summary.put("Potatoes", 100);
        summary.put("Tomatoes", 200);
        summary.put("Garlic", 3000);

        assertThat(calculateGlobalOrder(Arrays.asList(order1, order2)), equalTo(summary));
    }
}