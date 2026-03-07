package com.ua.javaRush.Select.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class User {
    private String login;
    private String password;
    private Integer resultOfStoryEndings;
    private List<String> listOfStoryEndings;

    public User (String login, String password) {
        this.login = login;
        this.password = password;
        resultOfStoryEndings = 0;
        listOfStoryEndings = new ArrayList<>();
    }

    public void addStoryEnding (String storyEnding) {
        listOfStoryEndings.add(0, storyEnding);
    }
}
