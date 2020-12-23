package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var cups = Arrays.stream(Files.readString(Paths.get("23.in")).split(""))
                .map(Integer::parseInt)
                .map(n -> {
                    var cup = new Cup();
                    cup.label = n;
                    return cup;
                })
                .collect(Collectors.toList());

        var one = game(cups, 100).next;
        
        System.out.println("Part 1: " + render(one));

        cups = Arrays.stream(Files.readString(Paths.get("23.in")).split(""))
                .map(Integer::parseInt)
                .map(n -> {
                    var cup = new Cup();
                    cup.label = n;
                    return cup;
                })
                .collect(Collectors.toList());

        var highest = cups.stream().max((c1, c2) -> Integer.compare(c1.label, c2.label)).orElseThrow();

        for (int i = highest.label + 1; cups.size() < 1000000; i++) {
            var cup = new Cup();
            cup.label = i;
            cups.add(cup);
        }

        one = game(cups, 10000000);
        
        System.out.println("Part 2: " + Math.multiplyFull(one.next.label, one.next.next.label));
    }

    static Cup game(List<Cup> cups, int rounds) {
        var highest = cups.stream().max((c1, c2) -> Integer.compare(c1.label, c2.label)).orElseThrow();
        var index = cups.stream().collect(Collectors.toMap(c -> c.label, c -> c));

        for (int i = 0; i < cups.size(); i++) {
            var current = cups.get(i);

            current.previous = cups.get(Math.floorMod(i - 1, cups.size()));
            current.next = cups.get(Math.floorMod(i + 1, cups.size()));
        }

        var current = cups.get(0);

        for (int i = 0; i < rounds; i++) {
            var one = current.next;
            var two = one.next;
            var three = two.next;

            current.next = three.next;
            three.previous = current;

            var destinationLabel = Math.floorMod(current.label - 1, highest.label);

            if (destinationLabel == 0) {
                destinationLabel = highest.label;
            }

            var destination = index.get(destinationLabel);

            while (destination == one || destination == two || destination == three) {
                destinationLabel = Math.floorMod(destination.label - 1, highest.label);
                if (destinationLabel == 0) {
                    destinationLabel = highest.label;
                }
                destination = index.get(destinationLabel);
            }

            var t = destination.next;

            destination.next = one;
            one.previous = destination;

            three.next = t;
            t.previous = three;

            current = current.next;

        }

        return index.get(1);
    }

    static String render(Cup cup) {
        var sb = new StringBuilder();
        var t = cup;

        for (int j = 0; j < 8; j++) {
            sb.append(t);
            t = t.next;
        }

        return sb.toString();
    }
}

class Cup {

    int label;
    Cup previous;
    Cup next;
}