package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/result")
public class ResultServlet extends HttpServlet {
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

        request.setAttribute("bgHistory", "image/historyBg/" + history.getName() + ".png");
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("goHome").equals("goHomePage")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/main");
        }
    }
}

