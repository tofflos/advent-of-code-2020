package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        var map = Files.lines(Paths.get("3.in"))
                .collect(Collectors.toList());

        System.out.println("Day 3a: " + count(map, 3, 1));

        record Slope(int right, int down) {

        }

        System.out.println("Day 3b: "
                + Stream.of(new Slope(1, 1), new Slope(3, 1), new Slope(5, 1), new Slope(7, 1), new Slope(1, 2))
                        .map(s -> count(map, s.right(), s.down))
                        .reduce(Math::multiplyExact).orElseThrow());
    }

    static long count(List<String> map, int right, int down) {
        var maxX = map.get(0).length();
        var maxY = map.size();
        long x = 0, count = 0;

        for (int y = 0; y < maxY; y += down, x += right) {
            if (map.get(y).charAt((int) x % maxX) == '#') {
                count++;
            }
        }

        return count;
    }
}
