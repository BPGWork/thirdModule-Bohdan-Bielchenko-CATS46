package com.ua.javaRush.Select.Dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoryPhaseDtoTest {

    @Test
    void readHistory_shouldReturnMap_whenJsonExists() {
        Map<String, Map<String, String>> result = HistoryPhaseDto.readHistory("testHistory");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void readHistory_shouldThrowRuntimeException_whenJsonDoesNotExist() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> HistoryPhaseDto.readHistory("file_that_does_not_exist_123"));

        assertTrue(ex.getMessage().contains("Cannot load JSON file from classpath"));
    }
}