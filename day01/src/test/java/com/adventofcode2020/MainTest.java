package com.adventofcode2020;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void day1() {
        int[] expenses = {1721, 979, 366, 299, 675, 1456};

        assertEquals(514579, Main.day1(expenses));
    }

    @Test
    void day2() {
        int[] expenses = {1721, 979, 366, 299, 675, 1456};

        assertEquals(241861950, Main.day2(expenses));
    }
}
