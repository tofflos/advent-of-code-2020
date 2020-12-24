package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var hexagons = Files.readString(Paths.get("24.in")).lines()
                .map(Hexagon::of)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println("Part 1: " + hexagons.values().stream().filter(l -> l % 2 == 1).count());

        var current = hexagons;

        for (int i = 0; i < 100; i++) {
            var adjacent = current.keySet().stream().flatMap(h -> h.adjacent().stream()).collect(Collectors.toSet());
            var next = new HashMap<Hexagon, Long>();

            adjacent.forEach(h -> current.putIfAbsent(h, 0L));

            for (var entry : current.entrySet()) {
                var count = entry.getKey().adjacent().stream().map(h -> current.getOrDefault(h, 0L)).filter(l -> l % 2 == 1).count();

                if (entry.getValue() % 2 == 1 && (count == 0 || count > 2)) {
                    next.put(entry.getKey(), entry.getValue() + 1);
                } else if (entry.getValue() % 2 == 0 && count == 2) {
                    next.put(entry.getKey(), entry.getValue() + 1);
                } else {
                    next.put(entry.getKey(), entry.getValue());
                }
            }

            current.clear();
            current.putAll(next);
        }

        System.out.println("Part 2: " + hexagons.values().stream().filter(l -> l % 2 == 1).count());
    }
}

record Hexagon(int q, int r) {

    static Pattern pattern = Pattern.compile("ne|nw|se|sw|e|w");

    static Hexagon of(String s) {
        int q = 0, r = 0;

        for (var direction : pattern.matcher(s).results().map(MatchResult::group).collect(Collectors.toList())) {
            switch (direction) {
                case "e" ->
                    q++;
                case "w" ->
                    q--;
                case "ne" -> {
                    q++;
                    r--;
                }
                case "se" -> {
                    r++;
                }
                case "nw" -> {
                    r--;
                }
                case "sw" -> {
                    q--;
                    r++;
                }
            }
        }

        return new Hexagon(q, r);
    }

    Set<Hexagon> adjacent() {
        return Set.of(
                new Hexagon(q + 1, r),
                new Hexagon(q - 1, r),
                new Hexagon(q + 1, r - 1),
                new Hexagon(q, r + 1),
                new Hexagon(q, r - 1),
                new Hexagon(q - 1, r + 1)
        );
    }
}