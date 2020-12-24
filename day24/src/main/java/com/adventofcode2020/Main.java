package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var tiles = Files.readString(Paths.get("24.in")).lines()
                .map(Hexagon::of)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println("Part 1: " + tiles.values().stream().filter(l -> l %2 == 1).count());
    }
}

record Hexagon(int q, int r) {

    static Pattern pattern = Pattern.compile("ne|nw|se|sw|e|w");

    static Hexagon of(String s) {
        int q = 0, r = 0;

        for (var direction : pattern.matcher(s).results().map(MatchResult::group).collect(Collectors.toList())) {
            switch (direction) {
                case "e" ->
                    q++;
                case "w" ->
                    q--;
                case "ne" -> {
                    q++;
                    r--;
                }
                case "se" -> {
                    r++;
                }
                case "nw" -> {
                    r--;
                }
                case "sw" -> {
                    q--;
                    r++;
                }
            }
        }

        return new Hexagon(q, r);
    }
}