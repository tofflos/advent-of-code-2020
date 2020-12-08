package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    static Map<String, Bag> bags;

    public static void main(String[] args) throws IOException {

        bags = Files.lines(Paths.get("7.in"))
                .map(Bag::fromString)
                .collect(Collectors.toMap(b -> b.color(), Function.identity()));

        System.out.println("Part 1: " + bags.values().stream().filter(b -> isValid(b.color(), "shiny gold")).count());
        System.out.println("Part 2: " + (countChildren("shiny gold") - 1));
    }

    static boolean isValid(String parent, String child) {
        return bags.get(parent).policies().stream().anyMatch(p -> p.color().equals(child))
                || bags.get(parent).policies().stream().anyMatch(p -> isValid(p.color(), child));
    }

    static long countChildren(String color) {
        return bags.get(color).policies().stream().mapToLong(p -> p.quantity() * countChildren(p.color())).sum() + 1;
    }
}

record Bag(String color, List<Policy> policies) {

    static Pattern pattern = Pattern.compile("^(\\w+ \\w+) bags contain (.*)$");

    static Bag fromString(String s) {
        var matcher = pattern.matcher(s);

        if (matcher.matches()) {
            return new Bag(matcher.group(1), Policy.fromString(matcher.group(2)));
        }

        throw new IllegalArgumentException("Unable to parse: " + s);
    }
}

record Policy(int quantity, String color) {

    static Pattern pattern = Pattern.compile("(?>(\\d) (\\w+ \\w+))");

    static List<Policy> fromString(String s) {
        return pattern.matcher(s).results()
                .map(r -> new Policy(Integer.parseInt(r.group(1)), r.group(2)))
                .collect(Collectors.toList());
    }
}