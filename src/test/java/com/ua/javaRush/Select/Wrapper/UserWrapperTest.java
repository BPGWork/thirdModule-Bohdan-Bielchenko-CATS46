package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Dto.UserDto;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserWrapperTest {

    @Test
    void getUserList_shouldReturnCopyOfUsers() {
        ServletContext context = mock(ServletContext.class);

        List<User> original = new ArrayList<>();
        original.add(new User("bohdan", "123"));

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(original);

            List<User> result = UserWrapper.getUserList(context);

            assertEquals(1, result.size());
            assertNotSame(original, result);
        }
    }

    @Test
    void addUser_withLoginAndPassword_shouldAddAndReturnUser() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = new ArrayList<>();

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            User result = UserWrapper.addUser("bohdan", "123", context);

            assertNotNull(result);
            assertEquals("bohdan", result.getLogin());
            assertEquals("123", result.getPassword());
            assertEquals(1, users.size());

            dtoMock.verify(() -> UserDto.writeUsers(users, context));
        }
    }

    @Test
    void addUser_withLoginAndPassword_shouldThrowException_whenUserAlreadyExists() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = new ArrayList<>();
        users.add(new User("bohdan", "123"));

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> UserWrapper.addUser("bohdan", "456", context));

            assertTrue(ex.getMessage().contains("User already exists"));
        }
    }

    @Test
    void addUser_withUserObject_shouldAddAndReturnUser() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = new ArrayList<>();
        User newUser = new User("alex", "qwerty");

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            User result = UserWrapper.addUser(newUser, context);

            assertEquals(newUser, result);
            assertEquals(1, users.size());
            dtoMock.verify(() -> UserDto.writeUsers(users, context));
        }
    }

    @Test
    void addUser_withUserObject_shouldThrowException_whenUserAlreadyExists() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = new ArrayList<>();
        users.add(new User("alex", "123"));

        User duplicate = new User("alex", "999");

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> UserWrapper.addUser(duplicate, context));

            assertTrue(ex.getMessage().contains("User already exists"));
        }
    }

    @Test
    void changeUser_shouldReplaceExistingUser() {
        ServletContext context = mock(ServletContext.class);

        User oldUser = new User("bohdan", "123");
        User updatedUser = new User("bohdan", "456");

        List<User> users = new ArrayList<>();
        users.add(oldUser);

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            UserWrapper.changeUser(updatedUser, context);

            assertEquals("456", users.get(0).getPassword());
            dtoMock.verify(() -> UserDto.writeUsers(users, context));
        }
    }

    @Test
    void changeUser_shouldThrowException_whenUserNotFound() {
        ServletContext context = mock(ServletContext.class);

        List<User> users = new ArrayList<>();
        users.add(new User("alex", "123"));

        User updatedUser = new User("bohdan", "456");

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> UserWrapper.changeUser(updatedUser, context));

            assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test
    void checkUser_shouldReturnTrue_whenUserExists() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = List.of(new User("bohdan", "123"));

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            assertTrue(UserWrapper.checkUser("bohdan", context));
        }
    }

    @Test
    void checkUser_shouldReturnFalse_whenUserDoesNotExist() {
        ServletContext context = mock(ServletContext.class);
        List<User> users = List.of(new User("alex", "123"));

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(users);

            assertFalse(UserWrapper.checkUser("bohdan", context));
        }
    }

    @Test
    void findUserByLogin_shouldReturnUser_whenUserExists() {
        ServletContext context = mock(ServletContext.class);
        User user = new User("bohdan", "123");

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(List.of(user));

            User result = UserWrapper.findUserByLogin("bohdan", context);

            assertNotNull(result);
            assertEquals("bohdan", result.getLogin());
        }
    }

    @Test
    void findUserByLogin_shouldReturnNull_whenUserDoesNotExist() {
        ServletContext context = mock(ServletContext.class);

        try (MockedStatic<UserDto> dtoMock = mockStatic(UserDto.class)) {
            dtoMock.when(() -> UserDto.readUsers(context)).thenReturn(List.of(new User("alex", "123")));

            User result = UserWrapper.findUserByLogin("bohdan", context);

            assertNull(result);
        }
    }
}