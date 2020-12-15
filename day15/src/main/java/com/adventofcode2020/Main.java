package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var numbers = Arrays.stream(Files.readString(Paths.get("15.in")).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        System.out.println("Part 1: " + game(numbers, 2020));
        System.out.println("Part 2: " + game(numbers, 30000000));
    }

    static int game(List<Integer> numbers, int turns) {
        var seen1 = new HashMap<Integer, Integer>();
        var seen2 = new HashMap<Integer, Integer>();

        for (int i = 0; i < numbers.size(); i++) {
            seen1.put(numbers.get(i), i);
        }

        for (int i = numbers.size(); i < turns; i++) {
            var previous = numbers.get(i - 1);
            var t1 = seen1.getOrDefault(previous, -1);
            var t2 = seen2.getOrDefault(previous, -1);
            var current = t1 == -1 || t2 == -1 ? 0 : t1 - t2;

            if (seen1.containsKey(current)) {
                seen2.put(current, seen1.get(current));
            }

            seen1.put(current, i);
            numbers.add(current);
        }
        
        return numbers.get(numbers.size() - 1);
    }
}