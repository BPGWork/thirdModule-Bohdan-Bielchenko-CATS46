package com.ua.javaRush.Select.Dto;

import com.ua.javaRush.Select.Domain.User;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDtoTest {

    @TempDir
    Path tempDir;

    @Test
    void readUsers_shouldReturnUsers_whenFileExists() throws Exception {
        Path webInf = tempDir.resolve("WEB-INF").resolve("data");
        Files.createDirectories(webInf);

        Path userFile = webInf.resolve("User.json");
        Files.writeString(userFile, """
        [
          {
            "login": "bohdan",
            "password": "123",
            "resultOfStoryEndings": 2,
            "listOfStoryEndings": ["END_1", "END_2"]
          }
        ]
        """);

        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/WEB-INF/data/User.json")).thenReturn(userFile.toString());

        List<User> users = UserDto.readUsers(context);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("bohdan", users.get(0).getLogin());
        assertEquals("123", users.get(0).getPassword());
        assertEquals(2, users.get(0).getResultOfStoryEndings());
        assertEquals(List.of("END_1", "END_2"), users.get(0).getListOfStoryEndings());
    }

    @Test
    void readUsers_shouldThrowRuntimeException_whenRealPathIsNull() {
        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/WEB-INF/data/User.json")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> UserDto.readUsers(context));
        assertTrue(ex.getMessage().contains("Cannot load User JSON file"));
    }

    @Test
    void writeUsers_shouldCreateFileAndWriteUsers_whenFileDoesNotExist() throws Exception {
        Path userFile = tempDir.resolve("WEB-INF").resolve("data").resolve("User.json");

        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/WEB-INF/data/User.json")).thenReturn(userFile.toString());

        User user = new User("bohdan", "123");

        UserDto.writeUsers(List.of(user), context);

        assertTrue(Files.exists(userFile));

        String content = Files.readString(userFile);
        assertTrue(content.contains("bohdan"));
        assertTrue(content.contains("123"));
    }

    @Test
    void writeUsers_shouldOverwriteExistingFile() throws Exception {
        Path webInf = tempDir.resolve("WEB-INF").resolve("data");
        Files.createDirectories(webInf);

        Path userFile = webInf.resolve("User.json");
        Files.writeString(userFile, "[]");

        ServletContext context = mock(ServletContext.class);
        when(context.getRealPath("/WEB-INF/data/User.json")).thenReturn(userFile.toString());

        User user = new User("alex", "qwerty");

        UserDto.writeUsers(List.of(user), context);

        String content = Files.readString(userFile);
        assertTrue(content.contains("alex"));
        assertTrue(content.contains("qwerty"));
    }
}