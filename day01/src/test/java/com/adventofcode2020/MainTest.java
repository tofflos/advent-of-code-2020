package com.adventofcode2020;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void day1a() {
        int[] expenses = {1721, 979, 366, 299, 675, 1456};

        assertEquals(514579, Main.day1a(expenses));
    }

    @Test
    void day1b() {
        int[] expenses = {1721, 979, 366, 299, 675, 1456};

        assertEquals(241861950, Main.day1b(expenses));
    }
}
