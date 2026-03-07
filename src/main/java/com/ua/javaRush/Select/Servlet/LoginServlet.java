package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Util.PasswordUtil;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = findUserByCredentials(request);

        if (user == null) {
            redirectToLoginError(request, response);
            return;
        }

        saveUserToSession(request, user);
        saveUserToCookie(response, user);
        redirectToIndex(request, response);
    }

    private User findUserByCredentials(HttpServletRequest request) {
        List<User> userList = UserWrapper.getUserList(getServletContext());

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || password == null) {
            return null;
        }

        for (User user : userList) {
            if (login.equals(user.getLogin())) {
                if (PasswordUtil.checkPassword(password, user.getPassword())) {
                    return user;
                }
            }
        }

        return null;
    }
    private void saveUserToSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(false);
        session.setAttribute("user", user);
    }
    private void saveUserToCookie(HttpServletResponse response, User user) {
        Cookie cookie = new Cookie("login", user.getLogin());
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    private void redirectToIndex(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/index");
    }
    private void redirectToLoginError(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/index?error=1");
    }
}