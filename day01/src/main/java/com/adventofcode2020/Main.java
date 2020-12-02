package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        var expenses = Files.lines(Paths.get("1.in"))
                .mapToInt(Integer::parseInt)
                .toArray();

        System.out.println("Day 1a: " + day1(expenses));
        System.out.println("Day 1b: " + day2(expenses));
    }

    static int day1(int[] expenses) {
        for (int i = 0; i < expenses.length; i++) {
            for (int j = 0; j < expenses.length; j++) {
                if (expenses[i] + expenses[j] == 2020) {
                    return expenses[i] * expenses[j];
                }
            }
        }

        throw new IllegalArgumentException();
    }

    static int day2(int[] expenses) {
        for (int i = 0; i < expenses.length; i++) {
            for (int j = 0; j < expenses.length; j++) {
                for (int k = 0; k < expenses.length; k++) {
                    if (expenses[i] + expenses[j] + expenses[k] == 2020) {
                        return expenses[i] * expenses[j] * expenses[k];
                    }
                }
            }
        }

        throw new IllegalArgumentException();
    }
}
