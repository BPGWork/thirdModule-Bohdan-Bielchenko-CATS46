package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Dto.HistoryDto;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class HistoryWrapperTest {

    @Test
    void getStoriesList_shouldReturnStoriesFromDto() {
        History history = new History();
        history.setName("Story1");
        history.setInfo("Info1");

        List<History> stories = List.of(history);

        try (MockedStatic<HistoryDto> dtoMock = mockStatic(HistoryDto.class)) {
            dtoMock.when(HistoryDto::readStories).thenReturn(stories);

            List<History> result = HistoryWrapper.getStoriesList();

            assertEquals(stories, result);
            assertEquals(1, result.size());
            assertEquals("Story1", result.get(0).getName());
        }
    }
}