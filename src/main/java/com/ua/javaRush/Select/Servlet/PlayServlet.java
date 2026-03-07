package com.ua.javaRush.Select.Servlet;

import com.ua.javaRush.Select.Domain.History;
import com.ua.javaRush.Select.Domain.HistoryPhase;
import com.ua.javaRush.Select.Domain.User;
import com.ua.javaRush.Select.Wrapper.HistoryPhaseWrapper;
import com.ua.javaRush.Select.Wrapper.UserWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/play")
public class PlayServlet extends HttpServlet {

    private static final String SESSION_HISTORY = "history";
    private static final String SESSION_USER = "user";
    private static final String SESSION_CURRENT_PHASE_TITLE = "currentPhaseTitle";
    private static final String SESSION_RESULT_TEXT = "resultText";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = getValidSession(request, response);
        if (session == null) {
            return;
        }

        History history = getHistoryFromSession(session, response, request);
        if (history == null) {
            return;
        }

        List<HistoryPhase> historyPhases = getHistoryPhases(history, response);
        if (historyPhases == null) {
            return;
        }

        String currentPhaseTitle = getOrInitCurrentPhaseTitle(session, historyPhases);
        HistoryPhase currentPhase = findPhaseByTitle(historyPhases, currentPhaseTitle);

        if (currentPhase == null) {
            writeError(response, "Phase not found: " + currentPhaseTitle);
            return;
        }

        preparePlayPage(request, history, currentPhase);
        forwardToPlayPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = getValidSession(request, response);
        if (session == null) {
            return;
        }

        History history = getHistoryFromSession(session, response, request);
        if (history == null) {
            return;
        }

        List<HistoryPhase> historyPhases = getHistoryPhases(history, response);
        if (historyPhases == null) {
            return;
        }

        String currentPhaseTitle = getOrInitCurrentPhaseTitle(session, historyPhases);
        HistoryPhase currentPhase = findPhaseByTitle(historyPhases, currentPhaseTitle);

        if (currentPhase == null) {
            writeError(response, "Current phase not found: " + currentPhaseTitle);
            return;
        }

        String userChoice = request.getParameter("result");
        String nextPhaseTitle = getNextPhaseTitle(currentPhase, userChoice, response);

        if (nextPhaseTitle == null) {
            return;
        }

        if (isEndPhase(nextPhaseTitle)) {
            handleEndPhase(session, history, nextPhaseTitle, request, response);
            return;
        }

        moveToNextPhase(session, historyPhases, nextPhaseTitle, response);
        response.sendRedirect(request.getContextPath() + "/play");
    }

    private HttpSession getValidSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/index");
            return null;
        }

        return session;
    }

    private History getHistoryFromSession(HttpSession session, HttpServletResponse response,
                                          HttpServletRequest request) throws IOException {
        History history = (History) session.getAttribute(SESSION_HISTORY);

        if (history == null) {
            response.sendRedirect(request.getContextPath() + "/index");
            return null;
        }

        return history;
    }

    private List<HistoryPhase> getHistoryPhases(History history, HttpServletResponse response)
            throws IOException {
        List<HistoryPhase> historyPhases = HistoryPhaseWrapper.getHistory(history.getName());

        if (historyPhases == null || historyPhases.isEmpty()) {
            writeError(response, "History phases are empty");
            return null;
        }

        return historyPhases;
    }

    private String getOrInitCurrentPhaseTitle(HttpSession session, List<HistoryPhase> historyPhases) {
        String currentPhaseTitle = (String) session.getAttribute(SESSION_CURRENT_PHASE_TITLE);

        if (currentPhaseTitle == null) {
            currentPhaseTitle = historyPhases.get(0).getTitle();
            session.setAttribute(SESSION_CURRENT_PHASE_TITLE, currentPhaseTitle);
        }

        return currentPhaseTitle;
    }

    private String getNextPhaseTitle(HistoryPhase currentPhase, String userChoice,
                                     HttpServletResponse response) throws IOException {
        String nextPhaseTitle = currentPhase.getValueWithMap(userChoice);

        if (nextPhaseTitle == null) {
            writeError(response, "Choice not found: " + userChoice);
            return null;
        }

        return nextPhaseTitle;
    }

    private boolean isEndPhase(String nextPhaseTitle) {
        return nextPhaseTitle.startsWith("END");
    }

    private void handleEndPhase(HttpSession session, History history, String endText,
                                HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = getUserFromSession(session, response);
        if (user == null) {
            return;
        }

        prepareUserForUpdate(user);
        updateUserStatistics(user, history, endText);
        saveUserState(session, user, endText);
        persistUser(user, request);

        session.removeAttribute(SESSION_CURRENT_PHASE_TITLE);
        response.sendRedirect(request.getContextPath() + "/result");
    }

    private User getUserFromSession(HttpSession session, HttpServletResponse response)
            throws IOException {
        User user = (User) session.getAttribute(SESSION_USER);

        if (user == null) {
            writeError(response, "User is null in PlayServlet");
            return null;
        }

        return user;
    }

    private void prepareUserForUpdate(User user) {
        if (user.getListOfStoryEndings() == null) {
            user.setListOfStoryEndings(new ArrayList<>());
        }

        if (user.getResultOfStoryEndings() == null) {
            user.setResultOfStoryEndings(0);
        }
    }

    private void updateUserStatistics(User user, History history, String endText) {
        user.addStoryEnding(history.getName() + ": " + endText);
        user.setResultOfStoryEndings(user.getResultOfStoryEndings() + 1);
    }

    private void saveUserState(HttpSession session, User user, String endText) {
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_RESULT_TEXT, endText);
    }

    private void persistUser(User user, HttpServletRequest request) {
        UserWrapper.changeUser(user, getServletContext());
    }

    private void moveToNextPhase(HttpSession session, List<HistoryPhase> historyPhases,
                                 String nextPhaseTitle, HttpServletResponse response)
            throws IOException {

        HistoryPhase nextPhase = findPhaseByTitle(historyPhases, nextPhaseTitle);

        if (nextPhase == null) {
            writeError(response, "Next phase not found: " + nextPhaseTitle);
            return;
        }

        session.setAttribute(SESSION_CURRENT_PHASE_TITLE, nextPhaseTitle);
    }

    private void preparePlayPage(HttpServletRequest request, History history, HistoryPhase currentPhase) {
        request.setAttribute("title", currentPhase.getTitle());
        request.setAttribute("choiceList", currentPhase.getListChoice());
        request.setAttribute("bgHistory", "image/historyBg/" + history.getName() + ".png");
    }

    private void forwardToPlayPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/play.jsp").forward(request, response);
    }

    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.getWriter().println(message);
    }

    private HistoryPhase findPhaseByTitle(List<HistoryPhase> historyPhases, String title) {
        for (HistoryPhase phase : historyPhases) {
            if (phase.getTitle().equals(title)) {
                return phase;
            }
        }
        return null;
    }
}