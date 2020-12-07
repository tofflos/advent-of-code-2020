package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

    static Pattern pattern = Pattern.compile("(?<color>(\\w+)\\s(\\w+))(.+)bags contain\\s(?<children>(no other bags.)|(\\d+\\s\\w+\\s\\w+)(.+))+");

    static Bag fromString(String s) {
        var matcher = pattern.matcher(s);

        if (matcher.matches()) {
            var color = matcher.group("color");
            var children = matcher.group("children").replace("bags", "").replace("bag", "").replace(".", "").replace("no other", "").trim().split(", ");
            var policies = Arrays.stream(children)
                    .filter(l -> !l.isBlank())
                    .map(l -> {
                        return new Policy(Integer.parseInt(l, 0, 1, 10), l.substring(2).trim());
                    }).collect(Collectors.toList());

            return new Bag(color, policies);
        }

        throw new IllegalArgumentException("Unable to parse: " + s);
    }
}

record Policy(int quantity, String color) {

}
