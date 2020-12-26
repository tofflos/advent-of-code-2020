package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("19.in")).split("\r\n\r\n");
        var rules = input[0].lines()
                .map(l -> l.split(": "))
                .map(arr -> Map.entry(Integer.parseInt(arr[0]), arr[1]))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        var messages = input[1].lines().collect(Collectors.toList());
        var materializations = materialize(rules);

        System.out.println("Part 1: " + messages.stream()
                .filter(s -> materializations.get(0).contains(s))
                .count());

        System.out.println("Part 2: " + messages.stream()
                .filter(s -> materializations.get(0).contains(s) || extendedValidation(s, materializations.get(42), materializations.get(31)))
                .count());
    }

    static boolean extendedValidation(String message, Set<String> materializations42, Set<String> materializations31) {
        // Materialization42 and materalization31 contain strings that are 8 characters long and have no elements in common.
        // The sequence of binary numbers, when strings are interpreted as a=0 and b=1, does not match anything at https://oeis.org.
        // Shape of message must match the pattern 42x + 42y + 31y where y > 1 and x > y
        

        var length = materializations31.stream().findAny().orElseThrow().length();
        var count31 = 0;
        var count42 = 0;
        var t = message;

        while (t.length() >= length && materializations31.contains(t.substring(t.length() - length))) {
            count31++;
            t = t.substring(0, t.length() - length);
        }

        while (t.length() >= length && materializations42.contains(t.substring(0, length))) {
            count42++;
            t = t.substring(length);
        }

        return count31 > 0 && count42 > count31 && t.isEmpty();
    }

    static HashMap<Integer, Set<String>> materialize(Map<Integer, String> rules) {
        var materializations = new HashMap<Integer, Set<String>>();

        while (!rules.isEmpty()) {
            for (var entry : rules.entrySet()) {
                if (entry.getValue().contains("\"")) {
                    materializations.put(entry.getKey(), Set.of(entry.getValue().replace("\"", "")));
                } else if (Arrays.stream(entry.getValue().split(" \\| "))
                        .flatMap(s -> Arrays.stream(s.split(" ")))
                        .allMatch(s -> materializations.containsKey(Integer.parseInt(s)))) {

                    var strings = Arrays.stream(entry.getValue().split(" \\| "))
                            .map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).map(materializations::get).collect(Collectors.toList()))
                            .collect(Collectors.toList());

                    var combinations = strings.stream().map(Main::combine).collect(Collectors.toList());

                    materializations.put(entry.getKey(), combinations.stream().flatMap(t -> t.stream()).collect(Collectors.toSet()));
                }
            }

            materializations.keySet().forEach(rules::remove);
        }

        return materializations;
    }

    static Set<String> combine(List<Set<String>> strings) {
        var current = new HashSet<String>(strings.get(0));

        for (int i = 1; i < strings.size(); i++) {
            var next = new HashSet<String>();

            for (var c : current) {
                for (var s : strings.get(i)) {
                    next.add(c + s);
                }
            }

            current = next;
        }

        return current;
    }
}