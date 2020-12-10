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
    }
}
