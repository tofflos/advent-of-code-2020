package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.HashMap;
import java.util.function.Consumer;

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

        memory.clear();

        for (var i = 0; i < input.size(); i++) {
            var t = input.get(i).split(" = ");

            if (t[0].startsWith("mask")) {
                mask1 = Long.parseLong(t[1].replace("X", "0"), 2);
                mask2 = Long.parseLong(t[1].replace("1", "0").replace("X", "1"), 2);
            } else {
                var address = Integer.parseInt(t[0].replace("mem[", "").replace("]", "")) | mask1;
                var bits = BitSet.valueOf(new long[]{mask2}).stream().toArray();
                var bs = BitSet.valueOf(new long[]{address});
                permutations(bits.length, counters -> {

                    for (int j = 0; j < counters.length; j++) {
                        bs.set(bits[j], counters[j] == 1);
                    }

                    memory.put(Long.toString(bs.toLongArray()[0]), (Long.parseLong(t[1])));
                });
            }
        }

        System.out.println("Part 2: " + memory.values().stream().mapToLong(Long::valueOf).sum());
    }

    static void permutations(int depth, Consumer<int[]> consumer) {
        int[] counters = new int[depth];

        permutations(0, counters, consumer);
    }

    static void permutations(int depth, int[] counters, Consumer<int[]> consumer) {

        for (int i = 0; i < 2; i++) {
            counters[depth] = i;

            if (depth < counters.length - 1) {
                permutations(depth + 1, counters, consumer);
            } else {
                consumer.accept(counters);
            }

        }
    }
}
