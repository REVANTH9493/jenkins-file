package com.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void addShouldReturnCorrectSum() {
        Calculator c = new Calculator();
        assertEquals(5, c.add(2, 3));
    }
}
