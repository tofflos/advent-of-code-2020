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
        var weakness2 = part2(numbers, size, weakness1);

        System.out.println("Part 1: " + weakness1);
        System.out.println("Part 2: " + weakness2);
    }

    static long part1(List<Long> numbers, int size) {
        var queue = new ArrayDeque<Long>(numbers.subList(size, numbers.size()));
        var window = new ArrayDeque<Long>(numbers.subList(0, size));

        while(!queue.isEmpty()) {
            var q = queue.pop();
            
            if(window.stream()
                    .flatMap(w1 -> window.stream()
                            .map(w2 -> w1 + w2))
                    .noneMatch(q::equals)) {
                return q;
            }
            
            window.pop();
            window.add(q);
        }

        throw new IllegalArgumentException();
    }

    static long part2(List<Long> numbers, int premable, long weakness) {
        var queue = new ArrayDeque<Long>();

        for (var number : numbers) {
            queue.add(number);

            var sum = queue.stream().mapToLong(Long::valueOf).sum();

            while (sum > weakness) {
                queue.poll();
                sum = queue.stream().mapToLong(Long::longValue).sum();
            }

            if (sum == weakness) {
                var min = queue.stream().mapToLong(Long::longValue).min().orElseThrow();
                var max = queue.stream().mapToLong(Long::longValue).max().orElseThrow();
                return min + max;
            }
        }

        throw new IllegalArgumentException();
    }
}