package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("16.in")).split("\\r\\n\\r\\n");

        var validators = input[0].lines()
                .map(Validator::fromString)
                .collect(Collectors.toList());

        var ticket = input[1].replaceAll("your ticket:\\r\\n", "").lines()
                .map(l -> Arrays.stream(l.split(",")).mapToInt(Integer::parseInt).toArray())
                .findAny()
                .orElseThrow();

        var tickets = input[2].replaceAll("nearby tickets:\\r\\n", "").lines()
                .map(l -> Arrays.stream(l.split(",")).mapToInt(Integer::parseInt).toArray())
                .collect(Collectors.toList());

        System.out.println("Part 1: " + tickets.stream().
                mapToInt(t -> Arrays.stream(t).filter(i -> validators.stream().noneMatch(v -> v.validate(i))).sum())
                .sum());

        var valid = tickets.stream()
                .filter(t -> Arrays.stream(t).allMatch(n -> validators.stream().anyMatch(v -> v.validate(n))))
                .collect(Collectors.toList());

        var candidates = IntStream.range(0, validators.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        i -> validators.stream()
                                .filter(v -> valid.stream().allMatch(t -> v.validate(t[i])))
                                .collect(Collectors.toList())
                ));

        while (candidates.values().stream().anyMatch(l -> l.size() > 1)) {
            var identified = candidates.values().stream()
                    .filter(l -> l.size() == 1)
                    .map(l -> l.get(0))
                    .collect(Collectors.toList());

            candidates.values().stream()
                    .filter(l -> l.size() > 1)
                    .forEach(l -> l.removeAll(identified));
        }

        System.out.println("Part 2: " + IntStream.range(0, candidates.size())
                .filter(i -> candidates.get(i).get(0).name().startsWith("departure"))
                .mapToLong(i -> ticket[i])
                .reduce(Math::multiplyExact)
                .orElseThrow());
    }
}

record Validator(String name, int l1, int h1, int l2, int h2) {

    static Pattern pattern = Pattern.compile("^(.+): (\\d+)-(\\d+) or (\\d+)-(\\d+)$");

    boolean validate(int n) {
        return (l1 <= n && n <= h1) || (l2 <= n && n <= h2);
    }

    static Validator fromString(String s) {
        var matcher = pattern.matcher(s);

        if (matcher.matches()) {
            return new Validator(
                    matcher.group(1),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)));
        }

        throw new IllegalArgumentException("Unable to parse: " + s);
    }
}