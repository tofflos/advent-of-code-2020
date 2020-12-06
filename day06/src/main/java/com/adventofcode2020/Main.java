package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("6.in"));
        
        var part1 = Arrays.stream(input.split("\n\n"))
                .map(s -> s.replace("\n", ""))
                .map(s -> s.split(""))
                .mapToLong(s -> Arrays.stream(s).distinct().count()).sum();

        System.out.println("Part 1: " + part1);

        var part2 = Arrays.stream(input.split("\n\n"))
                .map(s -> s.split(""))
                .map(s -> Arrays.stream(s).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())))
                .mapToLong(answers -> {
                    var groupSize = answers.getOrDefault("\n", 0L) + 1;
                    return answers.entrySet().stream()
                            .filter(e -> !"\n".equals(e.getKey()) && groupSize == e.getValue())
                            .count();
                }).sum();

        System.out.println("Part 2: " + part2);
    }
}
