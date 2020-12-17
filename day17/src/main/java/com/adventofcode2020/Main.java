package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readAllLines(Paths.get("17.in"));

        var bounds = new Bounds(0, input.get(0).length(), 0, input.size(), 0, 1, 0, 1);
        var current = new HashMap<Coordinate, Character>();

        for (int y = bounds.y1(); y < bounds.y2(); y++) {
            for (int x = bounds.x1(); x < bounds.x2(); x++) {
                current.put(new Coordinate(x, y, 0, 0), input.get(y).charAt(x));
            }
        }

        bounds = bounds.expand();

        for (int cycle = 0; cycle < 6; cycle++) {
            var next = new HashMap<Coordinate, Character>();

            for (int w = bounds.w1(); w < bounds.w2(); w++) {
                for (int z = bounds.z1(); z < bounds.z2(); z++) {
                    for (int y = bounds.y1(); y < bounds.y2(); y++) {
                        for (int x = bounds.x1(); x < bounds.x2(); x++) {
                            var coordinate = new Coordinate(x, y, z, w);
                            var state = current.getOrDefault(coordinate, '.');
                            var neighbours = neighbours(current, coordinate);
                            var n = neighbours.values().stream().filter(c -> c == '#').count();

                            if (state == '#') {
                                next.put(coordinate, n == 2 || n == 3 ? '#' : '.');
                            } else if (state == '.') {
                                next.put(coordinate, n == 3 ? '#' : '.');
                            }
                        }

                    }
                }
            }

            current = next;
            bounds = bounds.expand();
        }

        System.out.println("Part 2: " + current.values().stream().filter(c -> c == '#').count());
    }

    static Map<Coordinate, Character> neighbours(Map<Coordinate, Character> coordinates, Coordinate coordinate) {
        var neighbours = new HashMap<Coordinate, Character>();

        for (int y = coordinate.y() - 1; y <= coordinate.y() + 1; y++) {
            for (int x = coordinate.x() - 1; x <= coordinate.x() + 1; x++) {
                for (int z = coordinate.z() - 1; z <= coordinate.z() + 1; z++) {
                    for (int w = coordinate.w() - 1; w <= coordinate.w() + 1; w++) {
                        var neighbour = new Coordinate(x, y, z, w);

                        if (!neighbour.equals(coordinate)) {
                            neighbours.put(neighbour, coordinates.getOrDefault(neighbour, '.'));
                        }
                    }
                }
            }
        }

        return neighbours;
    }
}

record Bounds(int x1, int x2, int y1, int y2, int z1, int z2, int w1, int w2) {

    Bounds expand() {
        return new Bounds(x1 - 1, x2 + 1, y1 - 1, y2 + 1, z1 - 1, z2 + 1, w1 - 1, w2 + 1);
    }
}

record Coordinate(int x, int y, int z, int w) {

}
