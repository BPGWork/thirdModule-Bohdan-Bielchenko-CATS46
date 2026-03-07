package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Wrapper.HistoryWrapper;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    protected void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        restoreUserFromCookie(request, session);
        loadHistories(request);
        forwardToIndex(request, response);
    }

    protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (handleProfileRedirect(request, response)) {
            return;
        }

        if (handleHistoryChoice(request, response)) {
            return;
        }

        response.sendRedirect(request.getContextPath() + "/index");
    }

    private void restoreUserFromCookie (HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        String loginFromCookie = null;
        for (Cookie cookie : cookies) {
            if ("login".equals(cookie.getName())) {
                loginFromCookie = cookie.getValue();
                break;
            }
        }

        if (loginFromCookie == null || loginFromCookie.isBlank()) {
            setNeedAuthAttribute(request, session);
            return;
        }

        List<User> userList = UserWrapper.getUserList(getServletContext());
        for (User currentUser : userList) {
            if (loginFromCookie.equals(currentUser.getLogin())) {
                session.setAttribute("user", currentUser);
                return;
            }
        }
    }
    private void setNeedAuthAttribute (HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("needAuth", true);
        }
    }
    private void loadHistories (HttpServletRequest request) {
        List<History> historiesList = HistoryWrapper.getStoriesList();
        request.setAttribute("historyList", historiesList);
    }
    private void forwardToIndex (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private boolean handleProfileRedirect(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String profile = request.getParameter("profile");
        if ("profile".equals(profile)) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return true;
        }

        return false;
    }
    private boolean handleHistoryChoice(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String historyName = request.getParameter("choiceHistory");
        if (historyName == null || historyName.isBlank()) {
            return false;
        }

        List<History> historyList = HistoryWrapper.getStoriesList();
        HttpSession session = request.getSession();

        for (History history : historyList) {
            if (historyName.equals(history.getName())) {
                session.setAttribute("history", history);
                response.sendRedirect(request.getContextPath() + "/main");
                return true;
            }
        }

        return false;
    }
}
