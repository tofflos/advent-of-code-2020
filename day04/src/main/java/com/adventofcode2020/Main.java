package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var passports = Arrays.stream(Files.readString(Paths.get("4.in")).split("\n\n"))
                .map(Main::parse)
                .collect(Collectors.toList());

        System.out.println("Day 4a: " + passports.stream()
                .filter(Main::validate1)
                .count());

        System.out.println("Day 4b: " + passports.stream()
                .filter(Main::validate1)
                .filter(Main::validate2)
                .count());
    }

    static Map<String, String> parse(String passport) {
        return Arrays.stream(passport.replace("\n", " ").split(" "))
                .map(t -> t.split(":"))
                .collect(Collectors.toMap(t -> t[0], t -> t[1]));
    }

    static boolean validate1(Map<String, String> passport) {
        return passport.keySet().containsAll(List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"));
    }

    static boolean validate2(Map<String, String> passport) {
        Map<String, Predicate<String>> validators = Map.of(
                "byr", s -> validateYear(s, 1920, 2002),
                "iyr", s -> validateYear(s, 2010, 2020),
                "eyr", s -> validateYear(s, 2020, 2030),
                "hgt", Main::validateHgt,
                "hcl", s -> s.matches("#[a-z0-9]{6}"),
                "ecl", s -> s.matches("amb|blu|brn|gry|grn|hzl|oth"),
                "pid", s -> s.matches("\\d{9}"),
                "cid", s -> true
        );

        return passport.entrySet().stream().allMatch((e) -> validators.get(e.getKey()).test(e.getValue()));
    }

    static boolean validateYear(String s, int lower, int upper) {
        boolean b = false;

        if (s.matches("\\d{4}")) {
            var y = Integer.parseInt(s);

            b = lower <= y && y <= upper;
        }

        return b;
    }

    static boolean validateHgt(String s) {
        boolean b = false;

        var pattern = Pattern.compile("(\\d+)(cm|in)");
        var matcher = pattern.matcher(s);

        if (matcher.matches()) {
            var h = Integer.parseInt(matcher.group(1));

            b = switch (matcher.group(2)) {
                case "cm" ->
                    150 <= h && h <= 193;
                case "in" ->
                    59 <= h && h <= 76;
                default ->
                    false;
            };
        }

        return b;
    }

}
