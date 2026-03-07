package com.ua.javaRush.Select.Dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ua.javaRush.Select.Domain.User;
import jakarta.servlet.ServletContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserDto {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String USER_FILE = "/WEB-INF/data/User.json";

    public static List<User> readUsers(ServletContext context) {
        try {
            String realPath = context.getRealPath(USER_FILE);

            if (realPath == null) {
                throw new IllegalStateException("Real path is null for: " + USER_FILE);
            }

            Path path = Paths.get(realPath);

            if (!Files.exists(path)) {
                throw new IllegalStateException("User.json not found: " + path.toAbsolutePath());
            }

            return MAPPER.readValue(path.toFile(), new TypeReference<List<User>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Cannot load User JSON file", e);
        }
    }

    public static void writeUsers(List<User> users, ServletContext context) {
        try {
            String realPath = context.getRealPath(USER_FILE);

            if (realPath == null) {
                throw new IllegalStateException("Real path is null for: " + USER_FILE);
            }

            Path path = Paths.get(realPath);

            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), List.of());
            }

            MAPPER.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), users);

        } catch (Exception e) {
            throw new RuntimeException("Cannot save users JSON", e);
        }
    }
}