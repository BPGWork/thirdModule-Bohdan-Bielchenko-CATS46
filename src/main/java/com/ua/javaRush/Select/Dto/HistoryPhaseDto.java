package com.ua.javaRush.Select.Dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class HistoryPhaseDto {
    private static String RESOURCE_PATH;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, Map<String, String>> readHistory (String nameHistory) {
        RESOURCE_PATH = nameHistory + ".json";
        ClassLoader classLoader = HistoryPhaseDto.class.getClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                throw new IllegalStateException(
                        "JSON not found in classpath: " + RESOURCE_PATH
                );
            }

            return MAPPER.readValue(is, new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (Exception e) {
            throw new RuntimeException(
                    "Cannot load JSON file from classpath: " + RESOURCE_PATH, e
            );
        }
    }
}
