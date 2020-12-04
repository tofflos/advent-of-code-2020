package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
                "byr", s -> s.matches("19[2-9][0-9]|200[0-2]"),
                "iyr", s -> s.matches("201[0-9]|2020"),
                "eyr", s -> s.matches("202[0-9]|2030"),
                "hgt", s -> s.matches("(1([5-8][0-9]|9[0-3])cm)|((59|[6][0-9]|7[0-6])in)"),
                "hcl", s -> s.matches("#[0-9a-f]{6}"),
                "ecl", s -> s.matches("amb|blu|brn|gry|grn|hzl|oth"),
                "pid", s -> s.matches("\\d{9}"),
                "cid", s -> true
        );

        return passport.entrySet().stream().allMatch((e) -> validators.get(e.getKey()).test(e.getValue()));
    }
}
