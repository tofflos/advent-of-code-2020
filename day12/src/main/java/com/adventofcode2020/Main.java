package com.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        var instructions = Files.readString(Paths.get("12.in"));

        var ship1 = new Ship();
        instructions.lines().forEach(ship1::navigate);
        System.out.println("Part 1: " + manhattan(ship1.x, ship1.y));

        var ship2 = new WaypointShip();
        instructions.lines().forEach(ship2::navigate);
        System.out.println("Part 2: " + manhattan(ship2.x, ship2.y));
    }

    static int manhattan(int a, int b) {
        return Math.abs(a) + Math.abs(b);
    }
}

class Ship {

    int y = 0;
    int x = 0;
    
    int d = 90;

    void navigate(String instuction) {
        var action = instuction.charAt(0);
        var value = Integer.parseInt(instuction.substring(1));

        switch (action) {
            case 'N' -> y += value;
            case 'S' -> y -= value;
            case 'E' -> x += value;
            case 'W' -> x -= value;
            case 'F' -> {
                switch (d) {
                    case 0 -> y += value;
                    case 90 -> x += value;
                    case 180 -> y -= value;
                    case 270 -> x -= value;
                }
            }
            case 'R' -> d = Math.floorMod(d + value, 360);
            case 'L' -> d = Math.floorMod(d - value, 360);
        }
    }
}

class WaypointShip {

    int y = 0;
    int x = 0;

    int dy = 1;
    int dx = 10;

    void navigate(String instruction) {
        var action = instruction.charAt(0);
        var value = Integer.parseInt(instruction.substring(1));

        switch (action) {
            case 'N' -> dy += value;
            case 'S' -> dy -= value;
            case 'E' -> dx += value;
            case 'W' -> dx -= value;
            case 'F' -> {
                y += dy * value;
                x += dx * value;
            }
            case 'R' -> {
                for (int i = 0; i < value / 90; i++) {
                    var t = dy;
                    dy = -dx;
                    dx = t;
                }
            }
            case 'L' -> {
                for (int i = 0; i < value / 90; i++) {
                    var t = dy;
                    dy = dx;
                    dx = -t;
                }
            }
        }
    }
}