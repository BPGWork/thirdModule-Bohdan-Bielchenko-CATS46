package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Factory.HistoryPhaseFactory;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class HistoryPhaseWrapperTest {

    @Test
    void getHistory_shouldReturnHistoryFromFactory() {
        List<HistoryPhase> phases = List.of(
                new HistoryPhase("Start", Map.of("Go", "Next"))
        );

        try (MockedStatic<HistoryPhaseFactory> factoryMock = mockStatic(HistoryPhaseFactory.class)) {
            factoryMock.when(() -> HistoryPhaseFactory.createHistory("story1")).thenReturn(phases);

            List<HistoryPhase> result = HistoryPhaseWrapper.getHistory("story1");

            assertEquals(phases, result);
        }
    }
}