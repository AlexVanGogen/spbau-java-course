package ru.spbau.mit.javacourse.streams.secondpart;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        Map<String, List<String>> compositions = ImmutableMap.<String, List<String>> builder()
                .put("A", Arrays.asList("abacaba", "d", ""))
                .put("B", Collections.emptyList())
                .put("C", Collections.singletonList("ugrwwhghwruiohgshvuweogvior"))
                .put("D", Collections.singletonList(""))
                .build();
        assertThat(findPrinter(compositions), equalTo("C"));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> order1 = ImmutableMap.<String, Integer> builder()
                .put("Potatoes", 100)
                .put("Garlic", 2801)
                .build();

        Map<String, Integer> order2 = ImmutableMap.<String, Integer> builder()
                .put("Tomatoes", 200)
                .put("Garlic", 199)
                .build();

        Map<String, Integer> summary = ImmutableMap.<String, Integer> builder()
                .put("Potatoes", 100)
                .put("Tomatoes", 200)
                .put("Garlic", 3000)
                .build();

        assertThat(calculateGlobalOrder(Arrays.asList(order1, order2)), equalTo(summary));
    }
}