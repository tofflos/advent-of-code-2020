package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var groups = Arrays.stream(Files.readString(Paths.get("6.in")).split("\n\n"))
                .map(Group::fromString)
                .collect(Collectors.toList());

        System.out.println("Part 1: " + groups.stream()
                .mapToInt(g -> g.answerFrequencies().size())
                .sum());

        System.out.println("Part 2: " + groups.stream()
                .mapToLong(g -> g.answerFrequencies().entrySet().stream().filter(e -> e.getValue() == g.groupSize()).count())
                .sum());
    }
}

record Group(long groupSize, Map<String, Long> answerFrequencies) {

    static Group fromString(String s) {
        return new Group(s.lines().count(),
                Arrays.stream(s.replace("\n", "").split("")).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
    }
}
