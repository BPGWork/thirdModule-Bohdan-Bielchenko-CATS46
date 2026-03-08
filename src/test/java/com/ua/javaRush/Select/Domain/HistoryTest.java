package com.ua.javaRush.Select.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        History history = new History();

        assertNotNull(history);
        assertNull(history.getName());
        assertNull(history.getInfo());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        History history = new History();

        history.setName("Story 1");
        history.setInfo("Some info");

        assertEquals("Story 1", history.getName());
        assertEquals("Some info", history.getInfo());
    }
}