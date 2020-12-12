package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.lines(Paths.get("10.in"))
                .mapToInt(Integer::parseInt)
                .sorted()
                .toArray();

        var adapters = new int[input.length + 2];

        adapters[0] = 0;
        adapters[adapters.length - 1] = input[input.length - 1] + 3;

        System.arraycopy(input, 0, adapters, 1, input.length);

        var differences = new int[adapters.length - 1];

        for (int i = 0; i < differences.length; i++) {
            differences[i] = adapters[i + 1] - adapters[i];
        }

        var frequencies = Arrays.stream(differences)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        System.out.println("Part 1: " + frequencies.get(1) * frequencies.get(3));
        System.out.println("Part 2: " + multipliers(adapters).stream().mapToLong(i -> (long) i).reduce(1, Math::multiplyExact));
    }

    static List<Integer> multipliers(int[] adapters) {
        var children = children(adapters);
        var multipliers = new ArrayList<Integer>();
        var sb = new StringBuilder();

        for (int i = 0; i < children.length; i++) {
            int c = children[i];

            if (c != 1) {
                sb.append(c);
            } else if (!sb.isEmpty()) {
                multipliers.add(
                    switch (sb.toString()) {
                            case "332" -> 7;
                            case "32" -> 4;
                            case "2" -> 2;
                            default -> throw new IllegalArgumentException("Unknown pattern: " + sb);
                });

                sb.setLength(0);
            }
        }

        return multipliers;
    }

    static int[] children(int[] adapters) {
        var children = new int[adapters.length];

        for (int i = 0; i < children.length; i++) {
            for (int j = i + 1; j < adapters.length && j < i + 4; j++) {
                if (adapters[j] - adapters[i] < 4) {
                    children[i]++;
                }
            }
        }

        return children;
    }
}
