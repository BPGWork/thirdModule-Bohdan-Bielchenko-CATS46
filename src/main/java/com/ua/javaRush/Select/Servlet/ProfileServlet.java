package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        System.out.println("SESSION = " + session);

        if (session == null) {
            response.getWriter().println("Session is null");
            return;
        }

        Object rawUser = session.getAttribute("user");
        System.out.println("SESSION USER = " + rawUser);

        if (rawUser == null) {
            response.getWriter().println("User is null in session");
            return;
        }

        User user = (User) rawUser;

        request.setAttribute("nickname", user.getLogin());
        request.setAttribute("result", user.getResultOfStoryEndings());
        request.setAttribute("listOfStoryEndings", user.getListOfStoryEndings());

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String btm = request.getParameter("btm");
        if (btm.equals("mainPage")) {
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }
}