package com.adventofcode2020;

import static com.adventofcode2020.Direction.DOWN;
import static com.adventofcode2020.Direction.LEFT;
import static com.adventofcode2020.Direction.RIGHT;
import static com.adventofcode2020.Direction.UP;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("20.in")).split("\r\n\r\n");
        var tiles = Arrays.stream(input).map(Tile::of).collect(Collectors.toList());
        var corners = new ArrayList<Tile>();

        for (var t1 : tiles) {
            var neighbours = 0;

            for (var t2 : tiles) {
                if (t1.getId() != t2.getId()) {
                    for (var o : t2.orientations()) {
                        if (t1.border(UP).equals(o.border(DOWN))
                                || t1.border(DOWN).equals(o.border(UP))
                                || t1.border(LEFT).equals(o.border(RIGHT))
                                || t1.border(RIGHT).equals(o.border(LEFT))) {
                            neighbours++;
                        }
                    }
                }
            }

            if (neighbours == 2) {
                corners.add(t1);
            }
        }

        System.out.println("Part 1: " + corners.stream().mapToLong(Tile::getId).reduce(Math::multiplyExact).orElseThrow());

        var deque = new ArrayDeque<Tile>();
        var table = new HashSet<Tile>();

        tiles.forEach(deque::addLast);
        table.add(deque.removeFirst());

        while (!deque.isEmpty()) {

            var unplaced = deque.removeFirst();

            var candidates = unplaced.orientations().stream()
                    .collect(Collectors.toMap(Function.identity(),
                            o -> table.stream()
                                    .filter(
                                            placed -> placed.border(UP).equals(o.border(DOWN))
                                            || placed.border(DOWN).equals(o.border(UP))
                                            || placed.border(LEFT).equals(o.border(RIGHT))
                                            || placed.border(RIGHT).equals(o.border(LEFT)))
                                    .collect(Collectors.toList())
                    ));

            var match = candidates.entrySet().stream()
                    .filter(e -> !e.getValue().isEmpty())
                    .max((e1, e2) -> Integer.compare(e1.getValue().size(), e2.getValue().size()));

            if (match.isEmpty()) {
                deque.addLast(unplaced);
            } else {
                table.add(match.get().getKey());
            }
        }

        for (var t1 : table) {
            for (var t2 : table) {
                if (t1.border(UP).equals(t2.border(DOWN))) {
                    t1.up = t2;
                    t2.down = t1;
                } else if (t1.border(DOWN).equals(t2.border(UP))) {
                    t1.down = t2;
                    t2.up = t1;
                } else if (t1.border(LEFT).equals(t2.border(RIGHT))) {
                    t1.left = t2;
                    t2.right = t1;
                } else if (t1.border(RIGHT).equals(t2.border(LEFT))) {
                    t1.right = t2;
                    t2.left = t1;
                }
            }
        }

        var topLeft = table.stream().filter(t1 -> t1.up == null && t1.left == null).findAny().orElseThrow();
        var size = (int) Math.sqrt(tiles.size());
        var picture = new Tile[size][size];

        for (int y = 0; y < picture.length; y++) {
            if (y == 0) {
                picture[0][0] = topLeft;
            } else {
                picture[y][0] = picture[y - 1][0].down;
            }

            for (int x = 1; x < picture[y].length; x++) {
                picture[y][x] = picture[y][x - 1].right;
            }
        }

        var foobar = new Tile(Long.MIN_VALUE, renderImage(picture).lines().collect(Collectors.toList()));

        var count = 0;
        Tile monstermash = null;

        outer:
        for (var o : foobar.orientations()) {
            for (int y = 0; y < o.getLines().size() - 3; y++) {
                for (int x = 0; x < o.getLines().get(y).length() - 20; x++) {
                    if (containsMonster(o.getLines(), x, y)) {
                        monstermash = o;
                        count++;
                    }
                }
            }
        }
        
        var roughness = Arrays.stream(monstermash.getLines().stream().collect(Collectors.joining()).split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).get("#") - count * 15;
                
        System.out.println("Part 2: " + roughness);

    }

    static boolean containsMonster(List<String> region, int x, int y) {
        var seamonster = """
                                           # 
                         #    ##    ##    ###
                          #  #  #  #  #  #   """.lines().collect(Collectors.toList());

        boolean b = true;

        for (int i = 0; i < seamonster.size(); i++) {
            for (int j = 0; j < seamonster.get(i).length(); j++) {
                if (seamonster.get(i).charAt(j) == '#' && region.get(i + y).charAt(j + x) != '#') {
                    b = false;
                    break;
                }
            }
        }

        return b;
    }

    static String renderId(Tile[][] picture) {
        return Arrays.stream(picture)
                .map(row -> Arrays.stream(row).map(t -> t.getId()).map(String::valueOf).collect(Collectors.joining(" ")))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }

    static String renderImage(Tile[][] picture) {
        return Arrays.stream(picture)
                .map(row -> IntStream.range(1, 9)
                .mapToObj(i -> Arrays.stream(row)
                .map(tile -> tile.getLines().get(i).substring(1, 9))
                .collect(Collectors.joining()))
                .collect(Collectors.joining(System.getProperty("line.separator"))))
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }

}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

class Tile {

    private final long id;
    private final List<String> lines;

    Tile up, down, left, right;

    public Tile(long id, List<String> lines) {
        this.id = id;
        this.lines = lines;
    }

    public long getId() {
        return id;
    }

    public List<String> getLines() {
        return lines;
    }

    static Tile of(String tile) {
        var lines = tile.lines().collect(Collectors.toList());

        return new Tile(Long.parseLong(lines.get(0).replace("Tile ", "").replace(":", "")),
                lines.subList(1, lines.size()).stream().collect(Collectors.toList()));
    }

    String border(Direction direction) {
        return switch (direction) {
            case UP ->
                lines.get(0);
            case DOWN ->
                lines.get(lines.size() - 1);
            case LEFT ->
                lines.stream().map(l -> l.substring(0, 1)).collect(Collectors.joining());
            case RIGHT ->
                lines.stream().map(l -> l.substring(l.length() - 1, l.length())).collect(Collectors.joining());
        };
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

    @Override
    public String toString() {
        return "Tile{" + "id=" + id + '}';
    }
}
