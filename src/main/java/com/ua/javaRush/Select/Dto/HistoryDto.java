package com.ua.javaRush.Select.Dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.javaRush.Select.Domain.History;

import java.io.InputStream;
import java.util.List;

public class HistoryDto {
    private static final String RESOURCE_PATH = "Stories.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<History> readStories () {
        ClassLoader classLoader = HistoryDto.class.getClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                throw new IllegalStateException(
                        "Stories.json not found in classpath: " + RESOURCE_PATH
                );
            }

            return MAPPER.readValue(is, new TypeReference<List<History>>() {});
        } catch (Exception e) {
            throw new RuntimeException(
                    "Cannot load Stories JSON file from classpath: " + RESOURCE_PATH, e
            );
        }
    }
}
