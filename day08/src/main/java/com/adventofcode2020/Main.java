package com.adventofcode2020;

import static com.adventofcode2020.Instruction.Operation.JMP;
import static com.adventofcode2020.Instruction.Operation.NOP;
import static com.adventofcode2020.Result.Status.EXCEPTIONAL;
import static com.adventofcode2020.Result.Status.NORMAL;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {

        var instructions = Files.lines(Paths.get("8.in"))
                .map(Instruction::fromString)
                .collect(Collectors.toList());

        System.out.println("Part 1: " + execute(instructions).value());
        System.out.println("Part 2: " + IntStream.range(0, instructions.size())
                .mapToObj(i -> execute(modify(instructions, i)))
                .filter(r -> r.status() == NORMAL)
                .findAny()
                .orElseThrow().value());
    }

    static Result execute(List<Instruction> instructions) {
        var accumulator = 0;
        var position = 0;
        var status = NORMAL;

        var executed = new int[instructions.size()];
        Arrays.fill(executed, 0);

        while (position < instructions.size()) {
            if (executed[position] > 0) {
                status = EXCEPTIONAL;
                break;
            }

            var instruction = instructions.get(position);

            executed[position]++;

            switch (instruction.operation()) {
                case ACC -> {
                    accumulator += instruction.argument();
                    position++;
                }
                case JMP -> {
                    position += instruction.argument();
                }
                case NOP -> {
                    position++;
                }
            }
        }

        return new Result(accumulator, status);
    }

    static List<Instruction> modify(List<Instruction> instructions, int position) {
        var copy = new ArrayList<Instruction>(instructions);
        var instruction = instructions.get(position);

        switch (instruction.operation()) {
            case JMP ->
                copy.set(position, new Instruction(NOP, instruction.argument()));
            case NOP ->
                copy.set(position, new Instruction(JMP, instruction.argument()));
        }

        return copy;
    }
}

record Instruction(Operation operation, int argument) {

    enum Operation {
        ACC,
        JMP,
        NOP
    }

    static Instruction fromString(String s) {
        return new Instruction(Operation.valueOf(s.substring(0, 3).toUpperCase()),Integer.parseInt(s.substring(4)));
    }
}

record Result(int value, Status status) {

    enum Status {
        EXCEPTIONAL,
        NORMAL
    }
}
