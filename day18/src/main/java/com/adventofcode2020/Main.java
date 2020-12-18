package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        var lines = Files.readAllLines(Paths.get("18.in"));
        var pattern = Pattern.compile("\\d|\\(|\\)|\\+|\\*");
        var stack = new ArrayDeque<String>();
        var queue = new ArrayDeque<String>();

        var sum = 0L;
        
        for (var line : lines) {
            pattern.matcher(line).results().forEach(r -> queue.push(r.group()));

            while (!queue.isEmpty()) {
                var group = queue.removeLast();

                switch (group) {
                    case "+" ->
                        stack.push("+");
                    case "*" ->
                        stack.push("*");
                    case "(" ->
                        stack.push("(");
                    case ")" -> {
                        queue.addLast(stack.pop());
                        stack.pop();
                    }
                    default -> {
                        var previous = stack.peek();

                        if (previous == null || "(".equals(previous)) {
                            stack.push(group);
                        } else if ("+".equals(previous)) {
                            stack.pop();
                            stack.push(Long.toString(Long.parseLong(stack.pop()) + Long.parseLong(group)));
                        } else if ("*".equals(previous)) {
                            stack.pop();
                            stack.push(Long.toString(Long.parseLong(stack.pop()) * Long.parseLong(group)));
                        }
                    }
                }
            }

            sum += Long.parseLong(stack.pop());
        }
        
        System.out.println("Part 1: " + sum);
    }
}
