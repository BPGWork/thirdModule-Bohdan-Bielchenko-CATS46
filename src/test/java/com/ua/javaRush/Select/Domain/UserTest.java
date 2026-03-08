package com.ua.javaRush.Select.Domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getResultOfStoryEndings());
        assertNull(user.getListOfStoryEndings());
    }

    @Test
    void constructorWithLoginAndPassword_shouldInitializeFields() {
        User user = new User("bohdan", "123456");

        assertEquals("bohdan", user.getLogin());
        assertEquals("123456", user.getPassword());
        assertEquals(0, user.getResultOfStoryEndings());
        assertNotNull(user.getListOfStoryEndings());
        assertTrue(user.getListOfStoryEndings().isEmpty());
    }

    @Test
    void addStoryEnding_shouldAddNewEndingToBeginningOfList() {
        User user = new User("bohdan", "123456");

        user.addStoryEnding("Ending 1");
        user.addStoryEnding("Ending 2");

        List<String> endings = user.getListOfStoryEndings();

        assertEquals(2, endings.size());
        assertEquals("Ending 2", endings.get(0));
        assertEquals("Ending 1", endings.get(1));
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        User user = new User();

        user.setLogin("bohdan");
        user.setPassword("qwerty");
        user.setResultOfStoryEndings(5);
        user.setListOfStoryEndings(new ArrayList<>(List.of("END_1", "END_2")));

        assertEquals("bohdan", user.getLogin());
        assertEquals("qwerty", user.getPassword());
        assertEquals(5, user.getResultOfStoryEndings());
        assertEquals(List.of("END_1", "END_2"), user.getListOfStoryEndings());
    }
}