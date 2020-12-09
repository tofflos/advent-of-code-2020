package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var numbers = Files.lines(Paths.get("9.in"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        var preamble = 25;

        var weakness1 = part1(numbers, preamble);
        var weakness2 = part2(numbers, preamble, weakness1);

        System.out.println("Part 1: " + weakness1);
        System.out.println("Part 2: " + weakness2);
    }

    static long part1(List<Long> numbers, int preamble) {
        for (int i = 0; i < numbers.size(); i++) {
            if (!sums(numbers.subList(i, i + preamble)).contains(numbers.get(i + preamble))) {
                return numbers.get(i + preamble);
            }
        }

        throw new IllegalArgumentException();
    }

    static Set<Long> sums(List<Long> numbers) {
        var sums = new HashSet<Long>();

        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                if (i != j) {
                    sums.add(numbers.get(i) + numbers.get(j));
                }
            }
        }

        return sums;
    }

    static long part2(List<Long> numbers, int premable, long weakness) {
        var queue = new ArrayDeque<Long>();

        for (var number : numbers) {

            queue.add(number);

            var sum = queue.stream().mapToLong(Long::valueOf).sum();

            while (sum > weakness) {
                queue.poll();
                sum = queue.stream().mapToLong(Long::valueOf).sum();
            }

            if (sum == weakness) {
                var min = queue.stream().mapToLong(Long::valueOf).min().orElseThrow();
                var max = queue.stream().mapToLong(Long::valueOf).max().orElseThrow();
                return min + max;
            }
        }

        throw new IllegalArgumentException();
    }
}
