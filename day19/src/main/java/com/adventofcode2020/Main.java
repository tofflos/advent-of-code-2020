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

                    var combinations = strings.stream().map(Main::combinations).collect(Collectors.toList());

                    materializations.put(entry.getKey(), combinations.stream().flatMap(t -> t.stream()).collect(Collectors.toSet()));
                }
            }

            materializations.keySet().forEach(rules::remove);
        }

        System.out.println("Part 1: " + messages.stream().filter(s -> materializations.get(0).contains(s)).count());
    }

    static Set<String> combinations(List<Set<String>> strings) {
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