package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readAllLines(Paths.get("14.in"));
        var mask1 = 0L;
        var mask2 = 0L;
        var memory = new HashMap<String, Long>();

        for (var i = 0; i < input.size(); i++) {
            var t = input.get(i).split(" = ");

            if (t[0].startsWith("mask")) {
                mask1 = Long.parseLong(t[1].replace("X", "0"), 2);
                mask2 = Long.parseLong(t[1].replace("X", "1"), 2);
            } else {
                memory.put(t[0], (Long.parseLong(t[1]) | mask1) & mask2);
            }
        }

        System.out.println("Part 1: " + memory.values().stream().mapToLong(Long::valueOf).sum());
    }
}