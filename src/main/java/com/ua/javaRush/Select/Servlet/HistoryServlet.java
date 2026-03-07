package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.io.IOException;

@WebServlet("/main")
public class HistoryServlet extends HttpServlet {

    protected void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }
        History history = (History) session.getAttribute("history");
        if (history == null) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        request.setAttribute("nameHistory", history.getName());
        request.setAttribute("info", history.getInfo());
        request.setAttribute("bgHistory", "image/historyBg/" + history.getName() + ".png");
        request.getRequestDispatcher("/history.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String start = request.getParameter("start"); // кнопка start
        if (start == null) {
            response.sendRedirect(request.getContextPath() + "/main");
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("index", 0);
        session.removeAttribute("resultText");

        response.sendRedirect(request.getContextPath() + "/play");
    }
}
