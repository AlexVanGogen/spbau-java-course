package ru.spbau.mit.javacourse.streams.firstpart;

import ru.spbau.mit.javacourse.streams.Album;
import ru.spbau.mit.javacourse.streams.Artist;
import ru.spbau.mit.javacourse.streams.Track;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class FirstPartTasks {

    private FirstPartTasks() {}

    // Список названий альбомов
    public static List<String> allNames(Stream<Album> albums) {
        return albums
                .map(Album::getName)
                .collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    public static List<String> allNamesSorted(Stream<Album> albums) {
        return albums
                .map(Album::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    public static List<String> allTracksSorted(Stream<Album> albums) {
        return albums
                .flatMap(album -> album.getTracks().stream())
                .map(Track::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> albums) {
        return albums
                .filter(album -> album.getTracks().stream().anyMatch(t -> t.getRating() > 95))
                .sorted(Comparator.comparing(Album::getName))
                .collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(
                Collectors.groupingBy(Album::getArtist)
        );
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Artist' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        return albums.collect(
                Collectors.groupingBy(
                        Album::getArtist,
                        Collectors.mapping(Album::getName, Collectors.toList())
                ));
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        return albums.collect(
                Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .values()
                .stream()
                .reduce(
                        0L,
                        (allDuplicates, nextAlbumCount) -> allDuplicates + nextAlbumCount - 1
                );
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> albums) {
        return albums.min(
                Comparator.comparingInt(
                        album -> album.getTracks().stream()
                                .mapToInt(Track::getRating)
                                .max()
                                .orElse(0)
                ));
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {
        return albums.sorted(
                Comparator.comparingDouble(
                        (Album album) -> album.getTracks().stream()
                                .mapToDouble(Track::getRating)
                                .average()
                                .orElse(0)
                ).reversed())
                .collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        return stream.reduce(
                1,
                (a, b) -> ((a % modulo) * (b % modulo)) % modulo
        );
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        return Stream.of(strings)
                .collect(Collectors.joining(
                        ", ",
                        "<",
                        ">"
                ));
    }

    // Вернуть поток из объектов класса 'clazz'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> clazz) {
        return s.filter(clazz::isInstance).map(clazz::cast);
    }
}