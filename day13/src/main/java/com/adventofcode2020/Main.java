package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import static java.util.function.Predicate.not;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readAllLines(Paths.get("13.in"));
        var departure = Integer.parseInt(input.get(0));

        var busses = Arrays.stream(input.get(1).split(","))
                .filter(not("x"::equals))
                .map(Integer::parseInt)
                .collect(Collectors.toMap(Function.identity(), i -> departure + (i - departure % i)));

        var bus = busses.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .findFirst()
                .orElseThrow();

        System.out.println("Part 1: " + bus.getKey() * (bus.getValue() - departure));
    }
}