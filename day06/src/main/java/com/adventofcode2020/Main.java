package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var frequencies = Arrays.stream(Files.readString(Paths.get("6.in")).split("\n\n"))
                .map(s -> s.split(""))
                .map(arr -> Arrays.stream(arr).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())))
                .collect(Collectors.toList());

        System.out.println("Part 1: " + frequencies.stream()
                        .mapToLong(answers -> answers.entrySet().stream().filter(e -> !"\n".equals(e.getKey())).count())
                        .sum());

        System.out.println("Part 2: " + frequencies.stream()
                        .mapToLong(answers -> {
                            var groupSize = answers.getOrDefault("\n", 0L) + 1;
                            return answers.entrySet().stream().filter(e -> !"\n".equals(e.getKey()) && groupSize == e.getValue()).count();
                        })
                        .sum());
    }
}
