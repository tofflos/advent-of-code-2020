package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var passwords = Files.lines(Paths.get("2.in"))
                .map(Password::fromString)
                .collect(Collectors.toList());

        System.out.println("Day 2a: " + passwords.stream().filter(Password::isValid1).count());
        System.out.println("Day 2b: " + passwords.stream().filter(Password::isValid2).count());
    }
    
}

record Password(int min, int max, char character, String value) {

    static Pattern pattern = Pattern.compile("(\\d+)-(\\d+)\\s(.):\\s(.+)");

    static Password fromString(String s) {
        var matcher = pattern.matcher(s);

        if (matcher.matches()) {
            return new Password(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    matcher.group(3).charAt(0),
                    matcher.group(4));
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    boolean isValid1() {
        var count = value.chars().filter(c -> character == ((char) c)).count();

        return min <= count && count <= max;
    }

    boolean isValid2() {
        return value.charAt(min - 1) == character ^ value.charAt(max - 1) == character;
    }
}