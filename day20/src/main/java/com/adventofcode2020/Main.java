package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("20.in")).split("\\r\\n\\r\\n");
        var tiles = Arrays.stream(input).map(Tile::of).collect(Collectors.toList());
        var corners = new ArrayList<Tile>();

        for (var t1 : tiles) {
            var neighbours = 0;

            for (var t2 : tiles) {
                if (t1.id() != t2.id()) {
                    for (var o : t1.orientations()) {
                        if (o.up().equals(t2.down())
                                || o.down().equals(t2.up())
                                || o.left().equals(t2.right())
                                || o.right().equals(t2.left())) {
                            neighbours++;
                        }
                    }
                }
            }

            if (neighbours == 2) {
                corners.add(t1);
            }
        }

        System.out.println("Part 1: " + corners.stream().mapToLong(Tile::id).reduce(Math::multiplyExact).orElseThrow());
    }
}

record Tile(long id, List<String> lines) {

    static Tile of(String tile) {
        var lines = tile.lines().collect(Collectors.toList());

        return new Tile(Long.parseLong(lines.get(0).replace("Tile ", "").replace(":", "")),
                lines.subList(1, lines.size()).stream().collect(Collectors.toList()));
    }

    String up() {
        return lines.get(0);
    }

    String down() {
        return lines.get(lines.size() - 1);
    }

    String left() {
        return lines.stream().map(row -> row.substring(0, 1)).collect(Collectors.joining());
    }

    String right() {
        return lines.stream().map(row -> row.substring(row.length() - 1, row.length())).collect(Collectors.joining());
    }

    Tile flip() {
        var flipped = new ArrayList<String>(lines);

        Collections.reverse(flipped);

        return new Tile(id, flipped);
    }

    Tile rotate() {
        var arr = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = lines.get(lines.size() - 1 - j).charAt(i);
            }
        }

        return new Tile(id, Arrays.stream(arr).map(String::valueOf).collect(Collectors.toList()));
    }

    Set<Tile> orientations() {
        var orientations = new HashSet<Tile>();
        var tile = this;

        for (int i = 0; i < 2; i++) {
            tile = tile.rotate();
            orientations.add(tile);
            tile = tile.rotate();
            orientations.add(tile);
            tile = tile.rotate();
            orientations.add(tile);
            tile = tile.flip();
            orientations.add(tile);
        }

        return orientations;
    }
}
