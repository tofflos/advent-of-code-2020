package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var numbers = Arrays.stream(Files.readString(Paths.get("15.in")).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        for (int i = numbers.size(); i < 2020; i++) {
            var previous = numbers.get(i - 1);

            var t = numbers.subList(0, numbers.size() - 1).lastIndexOf(previous);

            if (t == -1) {
                numbers.add(0);
            } else {
                numbers.add(i - 1 - t);
            }
        }

        System.out.println("Part 1: " + numbers.get(numbers.size() - 1));
    }
}
