package com.ua.javaRush.Select.Domain;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HistoryPhaseTest {

    @Test
    void getListChoice_shouldReturnAllKeysFromMap() {
        Map<String, String> phaseChoice = new LinkedHashMap<>();
        phaseChoice.put("Go left", "LEFT_PHASE");
        phaseChoice.put("Go right", "RIGHT_PHASE");

        HistoryPhase historyPhase = new HistoryPhase("Start", phaseChoice);

        List<String> result = historyPhase.getListChoice();

        assertEquals(2, result.size());
        assertEquals("Go left", result.get(0));
        assertEquals("Go right", result.get(1));
    }

    @Test
    void getValueWithMap_shouldReturnMappedValueByKey() {
        Map<String, String> phaseChoice = new LinkedHashMap<>();
        phaseChoice.put("Open door", "DOOR_PHASE");
        phaseChoice.put("Run away", "RUN_PHASE");

        HistoryPhase historyPhase = new HistoryPhase("Start", phaseChoice);

        assertEquals("DOOR_PHASE", historyPhase.getValueWithMap("Open door"));
        assertEquals("RUN_PHASE", historyPhase.getValueWithMap("Run away"));
    }

    @Test
    void getValueWithMap_shouldReturnNull_whenKeyDoesNotExist() {
        Map<String, String> phaseChoice = new LinkedHashMap<>();
        phaseChoice.put("Open door", "DOOR_PHASE");

        HistoryPhase historyPhase = new HistoryPhase("Start", phaseChoice);

        assertNull(historyPhase.getValueWithMap("Unknown choice"));
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        Map<String, String> phaseChoice = new LinkedHashMap<>();
        phaseChoice.put("Choice", "NEXT");

        HistoryPhase historyPhase = new HistoryPhase("Old title", phaseChoice);

        historyPhase.setTitle("New title");

        Map<String, String> newMap = new LinkedHashMap<>();
        newMap.put("Another choice", "END");
        historyPhase.setPhaseChoice(newMap);

        assertEquals("New title", historyPhase.getTitle());
        assertEquals(newMap, historyPhase.getPhaseChoice());
    }
}