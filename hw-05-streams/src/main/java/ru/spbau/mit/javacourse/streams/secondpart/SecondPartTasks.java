package ru.spbau.mit.javacourse.streams.secondpart;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream()
                .map(Paths::get)
                .flatMap(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        return Stream.of();
                    }
                })
                .filter(s -> s.contains(sequence))
                .collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int numberOfExperiments = 1000000;
        final Random random = new Random();
        final BiPredicate<Double, Double> hasHitTarget = (x, y) -> x * x + y * y <= 1.0;

        return (double)IntStream.range(0, numberOfExperiments)
                .reduce(
                        0,
                        (allHitsNumber, nextExperiment) -> allHitsNumber + (hasHitTarget.test(
                                2 * random.nextDouble() - 1,
                                2 * random.nextDouble() - 1
                        ) ? 1 : 0)
                ) / numberOfExperiments;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return Maps.transformValues(compositions, list -> (list != null) ? list.stream().mapToInt(String::length).sum() : 0)
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry<String, Integer>::getValue))
                .orElse(new AbstractMap.SimpleEntry<>("None", 0))
                .getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                Integer::sum
                        )
                );
    }
}
