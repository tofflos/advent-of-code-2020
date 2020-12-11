package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        var current = Files.lines(Paths.get("11.in"))
                .map(String::toCharArray)
                .toArray(char[][]::new);

        System.out.println("Part 1: " + occupied(current, Main::adjacent, 4));
        System.out.println("Part 2: " + occupied(current, Main::visible, 5));
    }

    static String render(char[][] seats) {
        var lines = new StringBuilder();

        for (var row : seats) {
            lines.append(String.valueOf(row));
            lines.append("\n");
        }

        return lines.toString();
    }

    static String adjacent(char[][] seats, int px, int py, int dx, int dy) {
        var sb = new StringBuilder();

        var x = px + dx;
        var y = py + dy;

        if (0 <= y && y < seats.length && 0 <= x && x < seats[y].length) {
            sb.append(seats[y][x]);
        }

        return sb.toString();
    }

    static String visible(char[][] seats, int px, int py, int dx, int dy) {
        var sb = new StringBuilder();

        var x = px;
        var y = py;

        while (true) {
            y += dy;
            x += dx;

            if (y < 0 || y >= seats.length || x < 0 || x >= seats.length) {
                break;
            }

            char seat = seats[y][x];

            sb.append(seat);

            if (seat == 'L' || seat == '#') {
                break;
            }
        }

        return sb.toString();
    }

    static long occupied(char[][] current, PentaFunction<char[][], Integer, Integer, Integer, Integer, String> function, int tolerance) {
        var deltas = new int[][]{{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

        while (true) {
            var next = new char[current.length][current[0].length];

            for (int y = 0; y < current.length; y++) {
                for (int x = 0; x < current[y].length; x++) {
                    var neighbours = new StringBuilder();

                    for (var delta : deltas) {
                        neighbours.append(function.apply(current, x, y, delta[0], delta[1]));
                    }

                    var seat = current[y][x];

                    if (seat == 'L' && neighbours.chars().noneMatch(i -> i == '#')) {
                        next[y][x] = '#';
                    } else if (seat == '#' && neighbours.chars().filter(i -> i == '#').count() >= tolerance) {
                        next[y][x] = 'L';
                    } else {
                        next[y][x] = seat;
                    }
                }
            }

            if (Arrays.deepEquals(current, next)) {
                break;
            }

            current = next;
        }

        return render(current).chars().filter(c -> c == '#').count();
    }
}

@FunctionalInterface
interface PentaFunction<A, B, C, D, E, R> {

    R apply(A a, B b, C c, D d, E e);
}
