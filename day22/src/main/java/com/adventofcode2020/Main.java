package com.adventofcode2020;

import static com.adventofcode2020.Player.PLAYER1;
import static com.adventofcode2020.Player.PLAYER2;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        var input = Files.readString(Paths.get("22.in")).split("\r\n\r\n");

        var player1 = new ArrayDeque<Integer>(input[0].replace("Player 1:\r\n", "").lines().map(Integer::parseInt).collect(Collectors.toList()));
        var player2 = new ArrayDeque<Integer>(input[1].replace("Player 2:\r\n", "").lines().map(Integer::parseInt).collect(Collectors.toList()));

        System.out.println("Part 1: " + (combat(player1, player2) == PLAYER1
                ? score(player1.stream().collect(Collectors.toList()))
                : score(player2.stream().collect(Collectors.toList()))));

        player1 = new ArrayDeque<>(input[0].replace("Player 1:\r\n", "").lines().map(Integer::parseInt).collect(Collectors.toList()));
        player2 = new ArrayDeque<>(input[1].replace("Player 2:\r\n", "").lines().map(Integer::parseInt).collect(Collectors.toList()));

        System.out.println("Part 2: " + (recursive_combat(player1, player2) == PLAYER1
                ? score(player1.stream().collect(Collectors.toList()))
                : score(player2.stream().collect(Collectors.toList()))));
    }

    static Player combat(Deque<Integer> player1, Deque<Integer> player2) {

        while (!player1.isEmpty() && !player2.isEmpty()) {

            var card1 = player1.removeFirst();
            var card2 = player2.removeFirst();

            if (card1 > card2) {
                player1.addLast(card1);
                player1.addLast(card2);
            } else {
                player2.addLast(card2);
                player2.addLast(card1);
            }
        }

        return player1.isEmpty() ? PLAYER2 : PLAYER1;
    }

    static Player recursive_combat(Deque<Integer> player1, Deque<Integer> player2) {

        List<Pair> seen = new ArrayList<>();
        Player winner = null;

        while (!player1.isEmpty() && !player2.isEmpty()) {

            var l1 = player1.stream().collect(Collectors.toList());
            var l2 = player2.stream().collect(Collectors.toList());

            if (seen.stream().anyMatch(pair -> pair.player1().equals(l1) && pair.player2().equals(l2))) {
                return PLAYER1;
            }

            seen.add(new Pair(l1, l2));

            var card1 = player1.removeFirst();
            var card2 = player2.removeFirst();

            if (card1 <= player1.size() && card2 <= player2.size()) {
                var copy1 = new ArrayDeque<Integer>();
                var copy2 = new ArrayDeque<Integer>();

                player1.stream().limit(card1).forEach(copy1::addLast);
                player2.stream().limit(card2).forEach(copy2::addLast);

                winner = recursive_combat(copy1, copy2);
            } else {
                winner = card1 > card2 ? PLAYER1 : PLAYER2;
            }

            if (PLAYER1.equals(winner)) {
                player1.addLast(card1);
                player1.addLast(card2);
            } else {
                player2.addLast(card2);
                player2.addLast(card1);
            }
        }

        return winner;
    }

    static long score(List<Integer> cards) {
        var score = 0L;

        for (int i = 0; i < cards.size(); i++) {
            score += cards.get(cards.size() - 1 - i) * (i + 1);
        }

        return score;
    }
}

enum Player {
    PLAYER1, PLAYER2
}

record Pair(List<Integer> player1, List<Integer> player2) {

}