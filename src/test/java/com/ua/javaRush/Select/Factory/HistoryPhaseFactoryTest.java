package com.ua.javaRush.Select.Factory;

import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Dto.HistoryPhaseDto;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class HistoryPhaseFactoryTest {

    @Test
    void createHistory_shouldConvertMapToHistoryPhaseList() {
        Map<String, Map<String, String>> dtoMap = new LinkedHashMap<>();

        Map<String, String> startChoices = new LinkedHashMap<>();
        startChoices.put("Go left", "LeftPhase");

        Map<String, String> leftChoices = new LinkedHashMap<>();
        leftChoices.put("Fight", "END_WIN");

        dtoMap.put("Start", startChoices);
        dtoMap.put("LeftPhase", leftChoices);

        try (MockedStatic<HistoryPhaseDto> dtoMock = mockStatic(HistoryPhaseDto.class)) {
            dtoMock.when(() -> HistoryPhaseDto.readHistory("story1")).thenReturn(dtoMap);

            List<HistoryPhase> result = HistoryPhaseFactory.createHistory("story1");

            assertNotNull(result);
            assertEquals(2, result.size());

            assertEquals("Start", result.get(0).getTitle());
            assertEquals(startChoices, result.get(0).getPhaseChoice());

            assertEquals("LeftPhase", result.get(1).getTitle());
            assertEquals(leftChoices, result.get(1).getPhaseChoice());
        }
    }

    @Test
    void createHistory_shouldReturnEmptyList_whenDtoReturnsEmptyMap() {
        try (MockedStatic<HistoryPhaseDto> dtoMock = mockStatic(HistoryPhaseDto.class)) {
            dtoMock.when(() -> HistoryPhaseDto.readHistory("story1")).thenReturn(new LinkedHashMap<>());

            List<HistoryPhase> result = HistoryPhaseFactory.createHistory("story1");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}