package com.ua.javaRush.Select.Wrapper;

import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Dto.UserDto;
import jakarta.servlet.ServletContext;

import java.util.ArrayList;
import java.util.List;

public class UserWrapper {
    private static final Object LOCK = new Object();

    public static List<User> getUserList(ServletContext context) {
        synchronized (LOCK) {
            return new ArrayList<>(UserDto.readUsers(context));
        }
    }

    public static User addUser(String login, String password, ServletContext context) {
        synchronized (LOCK) {
            List<User> userList = UserDto.readUsers(context);

            for (User currentUser : userList) {
                if (currentUser.getLogin().equals(login)) {
                    throw new IllegalArgumentException("User already exists: " + login);
                }
            }

            User user = new User(login, password);
            userList.add(user);
            UserDto.writeUsers(userList, context);

            return user;
        }
    }

    public static User addUser(User user, ServletContext context) {
        synchronized (LOCK) {
            List<User> userList = UserDto.readUsers(context);

            for (User currentUser : userList) {
                if (currentUser.getLogin().equals(user.getLogin())) {
                    throw new IllegalArgumentException("User already exists: " + user.getLogin());
                }
            }

            userList.add(user);
            UserDto.writeUsers(userList, context);

            return user;
        }
    }

    public static void changeUser(User user, ServletContext context) {
        synchronized (LOCK) {
            List<User> userList = UserDto.readUsers(context);
            String login = user.getLogin();

            for (int i = 0; i < userList.size(); i++) {
                User currentUser = userList.get(i);

                if (login.equals(currentUser.getLogin())) {
                    userList.set(i, user);
                    UserDto.writeUsers(userList, context);
                    return;
                }
            }

            throw new IllegalArgumentException("User not found: " + login);
        }
    }

    public static boolean checkUser(String loginCheck, ServletContext context) {
        synchronized (LOCK) {
            List<User> userList = UserDto.readUsers(context);

            for (User user : userList) {
                if (user.getLogin().equals(loginCheck)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static User findUserByLogin(String login, ServletContext context) {
        synchronized (LOCK) {
            List<User> userList = UserDto.readUsers(context);

            for (User user : userList) {
                if (user.getLogin().equals(login)) {
                    return user;
                }
            }

            return null;
        }
    }
}