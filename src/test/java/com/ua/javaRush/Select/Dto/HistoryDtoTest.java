package com.ua.javaRush.Select.Dto;

import com.ua.javaRush.Select.Domain.History;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryDtoTest {

    @Test
    void readStories_shouldReturnStoriesFromJsonFile() {
        List<History> stories = HistoryDto.readStories();

        assertNotNull(stories);
        assertFalse(stories.isEmpty());

        for (History history : stories) {
            assertNotNull(history);
            assertNotNull(history.getName());
            assertNotNull(history.getInfo());
        }
    }
}