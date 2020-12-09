package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var numbers = Files.lines(Paths.get("9.in"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        var size = 25;

        var weakness1 = part1(numbers, size);
        var weakness2 = part2(numbers, weakness1);

        System.out.println("Part 1: " + weakness1);
        System.out.println("Part 2: " + weakness2);
    }

    static long part1(List<Long> numbers, int size) {
        var window = new ArrayDeque<Long>(numbers.subList(0, size));

        for(var number : numbers.subList(size, numbers.size())) {
            
            if(window.stream()
                    .flatMap(n1 -> window.stream()
                            .map(n2 -> n1 + n2))
                    .noneMatch(number::equals)) {
                return number;
            }
            
            window.pop();
            window.add(number);
        }

        throw new IllegalArgumentException();
    }

    static long part2(List<Long> numbers, long weakness) {
        var window = new ArrayDeque<Long>();

        for (var number : numbers) {
            window.add(number);

            var sum = window.stream().mapToLong(Long::longValue).sum();

            while (sum > weakness) {
                window.poll();
                sum = window.stream().mapToLong(Long::longValue).sum();
            }

            if (sum == weakness) {
                var min = window.stream().mapToLong(Long::longValue).min().orElseThrow();
                var max = window.stream().mapToLong(Long::longValue).max().orElseThrow();
                return min + max;
            }
        }

        throw new IllegalArgumentException();
    }
}