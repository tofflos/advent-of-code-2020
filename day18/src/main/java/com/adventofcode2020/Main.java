package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Part 1: " + calculate(Main::evaluate1));
        System.out.println("Part 2: " + calculate(Main::evaluate2));
    }

    static long calculate(TriConsumer<Deque<String>, Deque<String>, String> evaluator) throws IOException {
        var lines = Files.readAllLines(Paths.get("18.in"));
        var pattern = Pattern.compile("\\d|\\(|\\)|\\+|\\*");
        var stack = new ArrayDeque<String>();
        var queue = new ArrayDeque<String>();

        var sum = 0L;

        for (var line : lines) {
            pattern.matcher(line).results().forEach(r -> queue.push(r.group()));

            while (!queue.isEmpty()) {
                var current = queue.removeLast();

                switch (current) {
                    case "+", "*", "(" ->
                        stack.push(current);
                    case ")" -> {
                        queue.addLast(stack.pop());
                        stack.pop();
                    }
                    default ->
                        evaluator.accept(stack, queue, current);
                }
            }

            sum += Long.parseLong(stack.pop());
        }

        return sum;
    }

    static void evaluate1(Deque<String> stack, Deque<String> queue, String current) {
        var previous = stack.peek();

        if (previous == null || "(".equals(previous)) {
            stack.push(current);
        } else if ("+".equals(previous)) {
            stack.pop();
            queue.addLast(Long.toString(Long.parseLong(stack.pop()) + Long.parseLong(current)));
        } else if ("*".equals(previous)) {
            stack.pop();
            queue.addLast(Long.toString(Long.parseLong(stack.pop()) * Long.parseLong(current)));
        }
    }

    static void evaluate2(Deque<String> stack, Deque<String> queue, String current) {
        var previous = stack.peek();
        var next = queue.peekLast();

        if (previous == null || "(".equals(previous)) {
            stack.push(current);
        } else if ("+".equals(previous)) {
            stack.pop();
            queue.addLast(Long.toString(Long.parseLong(stack.pop()) + Long.parseLong(current)));
        } else if ("*".equals(previous) && !"+".equals(next)) {
            stack.pop();
            queue.addLast(Long.toString(Long.parseLong(stack.pop()) * Long.parseLong(current)));
        } else {
            stack.push(current);
            queue.addLast(queue.removeLast());
        }
    }
}

@FunctionalInterface
interface TriConsumer<A, B, C> {

    void accept(A a, B b, C c);
}