package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Util.PasswordUtil;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (userExists(login, request)) {
            redirect(response, request, "/login");
            return;
        }

        if (!passwordsMatch(password, confirmPassword)) {
            redirect(response, request, "/registration");
            return;
        }

        User user = createUser(login, password, request);

        saveUserToSession(request, user);
        saveUserToCookie(response, user);

        redirect(response, request, "/index");
    }


    private boolean userExists(String login, HttpServletRequest request) {
        return UserWrapper.checkUser(login, request.getServletContext());
    }


    private boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }


    private User createUser(String login, String password, HttpServletRequest request) {
        String hash = PasswordUtil.hashPassword(password);
        return UserWrapper.addUser(login, hash, request.getServletContext());
    }


    private void saveUserToSession(HttpServletRequest request, User user) {
        request.getSession().setAttribute("user", user);
    }


    private void saveUserToCookie(HttpServletResponse response, User user) {
        Cookie cookie = new Cookie("login", user.getLogin());
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private void redirect(HttpServletResponse response, HttpServletRequest request, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }
}