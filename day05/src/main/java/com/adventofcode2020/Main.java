package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {

        var seats = Files.lines(Paths.get("5.in"))
                .map(Seat::fromString)
                .collect(Collectors.toList());

        var max = seats.stream().mapToInt(Seat::id).max().orElseThrow();
        System.out.println("Part 1: " + max);

        var min = seats.stream().mapToInt(Seat::id).min().orElseThrow();
        var possible = IntStream.range(min, max).boxed().collect(Collectors.toSet());
        var seen = seats.stream().map(Seat::id).collect(Collectors.toSet());

        System.out.println("Part 2: " + possible.stream().filter(id -> !seen.contains(id)).findAny().orElseThrow());
    }

}

record Seat(int row, int column) {

    static Seat fromString(String s) {

        return new Seat(
                Integer.parseInt(s.substring(0, 7).replace('F', '0').replace('B', '1'), 2),
                Integer.parseInt(s.substring(7, 10).replace('R', '1').replace('L', '0'), 2)
        );
    }

    int id() {
        return row * 8 + column;
    }
}
